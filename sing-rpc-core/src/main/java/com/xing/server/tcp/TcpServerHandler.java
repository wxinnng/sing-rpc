package com.xing.server.tcp;

import com.xing.filter.Filter;
import com.xing.filter.FilterChain;
import com.xing.model.RpcRequest;
import com.xing.model.RpcResponse;
import com.xing.protocol.*;
import com.xing.registry.LocalRegistry;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.PriorityQueue;

/**
 * 服务端处理器
 */
@Slf4j
public class TcpServerHandler implements Handler<NetSocket> {
    @Override
    public void handle(NetSocket socket) {

        TcpBufferHandlerWrapper handlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
            // 接受请求，解码
            ProtocolMessage<RpcRequest> protocolMessage;
            try {
                protocolMessage = (ProtocolMessage<RpcRequest>) ProtocolMessageDecoder.decode(buffer);
            } catch (IOException e) {
                throw new RuntimeException("协议消息解码错误");
            }
            RpcRequest rpcRequest = protocolMessage.getBody();

            //调用provider的过滤器链
            RpcResponse rpcResponse = new RpcResponse();
            boolean canContinue = FilterChain.doProviderFilter(rpcRequest, rpcResponse);

            // 处理请求
            // 构造响应结果对象
            //只用能通过过滤器链，才能继续执行，业务代码。
            if(canContinue){
                doService(rpcRequest, rpcResponse);
            }
            ProtocolMessage.Header header = protocolMessage.getHeader();
            // 发送响应，编码
            header.setType((byte) ProtocolMessageTypeEnum.RESPONSE.getKey());
            header.setStatus((byte) ProtocolMessageStatusEnum.OK.getValue());
            ProtocolMessage<RpcResponse> responseProtocolMessage = new ProtocolMessage<>(header, rpcResponse);
            try {
                Buffer encode = ProtocolMessageEncoder.encode(responseProtocolMessage);
                socket.write(encode);
            } catch (IOException e) {
                throw new RuntimeException("协议消息编码错误");
            }
        });

        //处理连接
        socket.handler(handlerWrapper);
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
