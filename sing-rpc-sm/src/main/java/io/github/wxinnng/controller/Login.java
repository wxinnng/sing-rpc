package io.github.wxinnng.controller;

import io.github.wxinnng.RpcApplication;
import io.github.wxinnng.common.SRSMConstant;
import io.vertx.ext.web.RoutingContext;

public class Login extends AbstractApiController implements IApiController{
    @Override
    public void handle(RoutingContext routingContext) {
        String account = routingContext.request().getParam("account");
        String password = routingContext.request().getParam("password");
        if(account.equals(SRSMConstant.DEFAULT_ACCOUNT) && password.equals(RpcApplication.getRpcConfig().getToken())){
            doResponse(routingContext,"1");
        }else{
            doResponse(routingContext,"0");
        }
    }
}
