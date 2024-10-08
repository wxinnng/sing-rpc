package io.github.wxinnng.server.tcp;


import io.github.wxinnng.exception.RequestRejectException;
import io.github.wxinnng.filter.ProviderFilterChain;
import io.github.wxinnng.model.RpcRequest;
import io.github.wxinnng.model.RpcResponse;
import io.github.wxinnng.protocol.*;
import io.github.wxinnng.registry.LocalRegistry;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Method;

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

           try{
               //尝试执行过滤器链
               ProviderFilterChain providerFilterChain = new ProviderFilterChain();
               providerFilterChain.doFilter(rpcRequest, rpcResponse);
               doService(rpcRequest, rpcResponse);
           }catch(RequestRejectException e){
               //日志记录一下
               log.info("请求拒绝，拒绝原因：{}",e.getMessage());
               //要不是没有安全请求通过，要不就是被限流，后面就可以不用处理，也不用响应什么数据了
               return ;
           }catch (Exception e){
               log.info("请求有异常！");
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
