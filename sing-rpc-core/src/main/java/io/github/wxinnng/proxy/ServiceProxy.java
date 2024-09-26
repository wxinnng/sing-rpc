package io.github.wxinnng.proxy;

import cn.hutool.core.collection.CollUtil;
import io.github.wxinnng.RpcApplication;
import io.github.wxinnng.config.RpcConfig;
import io.github.wxinnng.fault.retry.RetryStrategy;
import io.github.wxinnng.fault.retry.RetryStrategyFactory;
import io.github.wxinnng.fault.tolerant.TolerantStrategy;
import io.github.wxinnng.fault.tolerant.TolerantStrategyFactory;
import io.github.wxinnng.filter.ConsumerFilterChain;
import io.github.wxinnng.loadbalancer.LoadBalancer;
import io.github.wxinnng.loadbalancer.LoadBalancerFactory;
import io.github.wxinnng.model.DiscoverParams;
import io.github.wxinnng.model.RpcRequest;
import io.github.wxinnng.model.RpcResponse;
import io.github.wxinnng.model.ServiceMetaInfo;
import io.github.wxinnng.registry.Registry;
import io.github.wxinnng.registry.RegistryFactory;
import io.github.wxinnng.server.tcp.VertxTcpClient;
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
        serviceMetaInfo.setServiceGroup(this.discoverParams.getGroup());
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
