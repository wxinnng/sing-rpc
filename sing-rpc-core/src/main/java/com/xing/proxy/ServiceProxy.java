package com.xing.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.xing.RpcApplication;
import com.xing.model.RpcRequest;
import com.xing.model.RpcResponse;
import com.xing.serializer.Serializer;
import com.xing.serializer.SerializerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 实现JDK的动态代理
 */
public class ServiceProxy implements InvocationHandler {


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //指定序列化器
        Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        //构造请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args).build();

        try{
            //序列化
            byte[] data = serializer.serialize(rpcRequest);
            //发送请求
            try(HttpResponse response = HttpRequest.post("http://localhost:9001/")
                    .body(data)
                    .execute()
            ){
                byte[] result = response.bodyBytes();
                return serializer.deserialize(result, RpcResponse.class).getData();
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("ServiceProxy.invoke");
            System.err.println("调用服务失败");
        }
        return null;
    }
}
