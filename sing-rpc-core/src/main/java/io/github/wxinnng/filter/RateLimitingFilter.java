package io.github.wxinnng.filter;

import io.github.wxinnng.RpcApplication;
import io.github.wxinnng.constant.RpcConstant;
import io.github.wxinnng.exception.RequestRejectException;
import io.github.wxinnng.model.RpcRequest;
import io.github.wxinnng.model.RpcResponse;
import io.github.wxinnng.ratelimiting.Limiter;
import io.github.wxinnng.ratelimiting.LimiterFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RateLimitingFilter implements ProviderFilter{
    @Override
    public void doFilter(RpcRequest rpcRequest, RpcResponse response, FilterChain filterChain) throws RequestRejectException {
        //限流配置
        String limiterKey = RpcApplication.getRpcConfig().getLimiterKey();
        //拿到服务的限流器(默认都是单例的，缓存在map中)
        Limiter limiter = LimiterFactory.getInstance(limiterKey);
        boolean tryResult = limiter.tryAcquire();
        if(!tryResult){
            System.err.println("===========================被限流=================================");
            throw new RequestRejectException(RpcConstant.REQUEST_LIMIT);
        }
        //限流通过，继续执行下面的操作。
        filterChain.doFilter(rpcRequest,response);
    }

    @Override
    public Integer getOrder() {
        return FilterKeys.PRIMARY_FILTER_ORDER;
    }

    @Override
    public Integer getType() {
        return FilterKeys.PROVIDER_FILTER;
    }
}
