package io.github.wxinnng.filter;

import io.github.wxinnng.RpcApplication;
import io.github.wxinnng.constant.RpcConstant;
import io.github.wxinnng.exception.RequestRejectException;
import io.github.wxinnng.model.RpcRequest;
import io.github.wxinnng.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IdentityVerificationFilter implements ProviderFilter{


    @Override
    public void doFilter(RpcRequest rpcRequest, RpcResponse response, FilterChain filterChain) throws RuntimeException {

        if(!RpcApplication.getRpcConfig().getIdentify()){
            log.info("未开启身份验证");
        }else if(rpcRequest.getToken() == null) {
            log.error("token为空，拒绝请求！");
            response.setMessage(RpcConstant.AUTHENTICATION_FAIL);
            throw new RequestRejectException(RpcConstant.AUTHENTICATION_FAIL);
        }else{
            boolean result = rpcRequest.getToken().equals(RpcApplication.getRpcConfig().getToken());
            if (!result){
                log.error("token校验失败！");
                response.setMessage(RpcConstant.AUTHENTICATION_FAIL);
                throw new RequestRejectException(RpcConstant.AUTHENTICATION_FAIL);
            }else {
                log.info("token校验成功！");
            }
        }
        filterChain.doFilter(rpcRequest,response);
    }


    @Override
    public Integer getOrder() {
        return 0;
    }

    @Override
    public Integer getType() {
        return FilterKeys.PROVIDER_FILTER;
    }
}
