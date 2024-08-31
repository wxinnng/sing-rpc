package com.xing.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.xing.model.RpcRequest;
import com.xing.model.RpcResponse;
import com.xing.model.User;
import com.xing.serializer.JDKSerializer;
import com.xing.serializer.Serializer;
import com.xing.service.UserService;

public class UserServiceProxy implements UserService {
    @Override
    public User getUser(User user) {
        //指定序列化器
        Serializer serializer = new JDKSerializer();

        //发送请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .args(new Object[]{user})
                .build();
        try{
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            byte[] result = null;

            //尝试朝后端发送请求
            try(HttpResponse httpResponse = HttpRequest.post("http://localhost:9001")
                    .body(bodyBytes)
                    .execute()
            ){
                //拿到最终结果
                result = httpResponse.bodyBytes();
            }catch (Exception e){
                e.printStackTrace();
                System.err.println("请求发送失败!");
            }
            //反序列化成Java对象
            RpcResponse rpcResponse = serializer.deserialize(result,RpcResponse.class);
            //返回最终结果
            return (User) rpcResponse.getData();
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
