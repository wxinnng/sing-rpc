package com.xing.ratelimiting;

import com.xing.RpcApplication;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * token令牌桶限流算法
 */
@Slf4j
public class TokenBucketRateLimiter implements Limiter{

    public final AtomicInteger tokens ;
    public final ScheduledExecutorService scheduler ;
    public final Integer maxTokes = RpcApplication.getRpcConfig().getMaxTokens();

    public TokenBucketRateLimiter(){
        this.tokens = new AtomicInteger(maxTokes);
        this.scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::fillTokens, 1000, RpcApplication.getRpcConfig().getTimeInterval(), TimeUnit.MILLISECONDS);
    }

    /**
     * 拿令牌
     * @return
     */
    @Override
    public boolean tryAcquire() {
        log.info("token令牌桶限流算法,进行限流");
        if(tokens.get() > 0){
            System.err.println("拿取令牌 :" + tokens.get());
            tokens.decrementAndGet();
            return true;
        }
        return false;
    }

    /**
     * 填充令牌
     */
    public void fillTokens() {
        if(tokens.get() < maxTokes ){
            System.err.println("令牌填充 :" + tokens.get());
            //如果令牌桶没有慢就继执行
            tokens.incrementAndGet();
        }
    }
}
