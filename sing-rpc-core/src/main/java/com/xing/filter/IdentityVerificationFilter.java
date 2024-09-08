package com.xing.filter;

import com.xing.RpcApplication;
import com.xing.constant.RpcConstant;
import com.xing.model.RpcRequest;
import com.xing.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IdentityVerificationFilter implements Filter{
    @Override
    public boolean doFilter(RpcRequest rpcRequest, RpcResponse response) {

        //如果没开启请求校验，就直接返回即可。
        if(! RpcApplication.getRpcConfig().getIdentify()){
            log.info("未开启身份验证");
            return true;
        }
        if(rpcRequest.getToken() == null)
        {
            log.error("token为空，拒绝请求！");
            response.setMessage(RpcConstant.AUTHENTICATION_FAIL);
            return false;
        }
        boolean result = rpcRequest.getToken().equals(RpcApplication.getRpcConfig().getToken());
        if (!result){
            log.error("token校验失败！");
            response.setMessage(RpcConstant.AUTHENTICATION_FAIL);
        }else {
            log.info("token校验成功！");
        }
        return result;
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
