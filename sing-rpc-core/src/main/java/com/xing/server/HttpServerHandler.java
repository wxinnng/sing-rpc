package com.xing.server;


import com.xing.RpcApplication;
import com.xing.filter.Filter;
import com.xing.filter.FilterChain;
import com.xing.filter.FilterKeys;
import com.xing.model.RpcRequest;
import com.xing.model.RpcResponse;
import com.xing.registry.LocalRegistry;
import com.xing.serializer.Serializer;
import com.xing.serializer.SerializerFactory;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;
import java.lang.reflect.Method;
import java.util.PriorityQueue;

/**
 * HTTP 请求处理
 */
@Slf4j
public class HttpServerHandler implements Handler<HttpServerRequest> {

    @Override
    public void handle(HttpServerRequest request) {

        // 指定序列化器(加载配置 & 默认配置)
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 记录日志
        log.info("Test:Received request: {} {}" ,request.method() , request.uri());

        // 异步处理 HTTP 请求
        request.bodyHandler(body -> {

            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 构造响应结果对象
            RpcResponse rpcResponse = new RpcResponse();
            // 如果请求为 null，直接返回
            if (rpcRequest == null) {
                rpcResponse.setMessage("rpcRequest is null");
                doResponse(request, rpcResponse, serializer);
                return;
            }

            //调用provider的过滤器链
            FilterChain.doProviderFilter(rpcRequest, rpcResponse);

            doService(rpcRequest, rpcResponse);
            // 响应
            doResponse(request, rpcResponse, serializer);
        });
    }

    /**
     * 响应
     *
     * @param request
     * @param rpcResponse
     * @param serializer
     */
    void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = request.response()
                .putHeader("content-type", "application/json");
        try {
            //结束此次调用
            httpServerResponse.end(Buffer.buffer(serializer.serialize(rpcResponse)));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }
    /**
     * 执行业务逻辑代码
     * @param rpcRequest
     * @param rpcResponse
     */
    private void doService(RpcRequest rpcRequest, RpcResponse rpcResponse) {
        try {
            // 获取要调用的服务实现类，通过反射调用
            Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
            Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
            Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());
            // 封装返回结果
            rpcResponse.setData(result);
            rpcResponse.setDataType(method.getReturnType());
            rpcResponse.setMessage("ok");
        } catch (Exception e) {
            e.printStackTrace();
            rpcResponse.setMessage(e.getMessage());
            rpcResponse.setException(e);
        }
    }

}

