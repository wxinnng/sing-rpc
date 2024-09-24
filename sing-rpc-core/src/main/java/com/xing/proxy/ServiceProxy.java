package com.xing.proxy;

import cn.hutool.core.collection.CollUtil;
import com.xing.RpcApplication;
import com.xing.config.RpcConfig;
import com.xing.fault.retry.RetryStrategy;
import com.xing.fault.retry.RetryStrategyFactory;
import com.xing.fault.tolerant.TolerantStrategy;
import com.xing.fault.tolerant.TolerantStrategyFactory;
import com.xing.filter.ConsumerFilterChain;
import com.xing.loadbalancer.LoadBalancer;
import com.xing.loadbalancer.LoadBalancerFactory;
import com.xing.model.DiscoverParams;
import com.xing.model.RpcRequest;
import com.xing.model.RpcResponse;
import com.xing.model.ServiceMetaInfo;
import com.xing.registry.Registry;
import com.xing.registry.RegistryFactory;
import com.xing.server.tcp.VertxTcpClient;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实现JDK的动态代理
 */
@Slf4j
public class ServiceProxy implements InvocationHandler {

    private DiscoverParams discoverParams;

    public ServiceProxy(DiscoverParams discoverParams){
        this.discoverParams = discoverParams;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //服务名称
        String serviceName = method.getDeclaringClass().getName();
        //构造请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args).build();

        //动态获取所有注册的服务
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        //获得对应的注册中心的注册信息
        Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        //设置服务信息
        serviceMetaInfo.setServiceVersion(this.discoverParams.getVersion());
        //TODO:其他的信息，如分组，mock这些
        serviceMetaInfo.setServiceName(serviceName);
        log.info("服务发现: {}",serviceMetaInfo);
        //进行服务发现
        List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());

        if(CollUtil.isEmpty(serviceMetaInfos)){
            throw new RuntimeException("没有此服务！" + serviceMetaInfo);
        }

        //拿到配置的负载均衡器
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
        //将方法名称作为负载均衡的参数
        Map<String,Object> params = new HashMap<>();
        params.put("methodName",rpcRequest.getMethodName());
        //获得负载到的服务信息
        ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(params,serviceMetaInfos);

        //填上对应的token(这个是要解决服务提供端的token校验的步骤，和消费端的过滤器没有关系)。
        rpcRequest.setToken(selectedServiceMetaInfo.getToken());

        //调用过滤器链
        ConsumerFilterChain consumerFilterChain = new ConsumerFilterChain();
        consumerFilterChain.doFilter(rpcRequest,null);


        //返回结果
        RpcResponse rpcResponse = null;
        try{
            //发送TCP请求
            //重试机制
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            rpcResponse = retryStrategy.doRetry(() ->
                VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo)
            );

        }catch (Exception e){
            log.error("请求出错，执行重试: {} 和容错机制: {}",rpcConfig.getRetryStrategy(),rpcConfig.getTolerantStrategy());
            //启用容错机制
            //拿到配置的策略
            TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
            //执行容错机制
            Map<String,Object> context = new HashMap<>();
            context.put("returnType",method.getReturnType());
            rpcResponse = tolerantStrategy.doTolerant(context,e);
        }
        //返回结果
        return rpcResponse.getData();
    }
}
