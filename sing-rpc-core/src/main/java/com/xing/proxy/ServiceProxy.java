package com.xing.proxy;

import cn.hutool.core.collection.CollUtil;
import com.xing.RpcApplication;
import com.xing.config.RpcConfig;
import com.xing.constant.RpcConstant;
import com.xing.model.RpcRequest;
import com.xing.model.RpcResponse;
import com.xing.model.ServiceMetaInfo;
import com.xing.registry.Registry;
import com.xing.registry.RegistryFactory;
import com.xing.serializer.Serializer;
import com.xing.serializer.SerializerFactory;
import com.xing.server.tcp.VertxTcpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;


/**
 * 实现JDK的动态代理
 */
public class ServiceProxy implements InvocationHandler {



    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {


        //指定序列化器
        Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        //服务名称
        String serviceName = method.getDeclaringClass().getName();

        //构造请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args).build();

        try{
            //序列化
            byte[] data = serializer.serialize(rpcRequest);

            //(动态获取所有注册的服务)
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            //获得对应的注册中心的注册信息
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());

            if(CollUtil.isEmpty(serviceMetaInfos)){
                throw new RuntimeException("没有此服务！");
            }

            //TODO:负载均衡
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfos.get(0);


            //发送HTTP请求
//            try(HttpResponse response = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
//                    .body(data)
//                    .execute()
//            ){
//                byte[] result = response.bodyBytes();
//                return serializer.deserialize(result, RpcResponse.class).getData();
//            }

            //发送TCP请求
            RpcResponse rpcResponse = VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo);

            //返回结果
            return rpcResponse.getData();

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("ServiceProxy.invoke");
            System.err.println("调用服务失败");
        }
        return null;
    }
}
