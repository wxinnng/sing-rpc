package io.github.wxinnng.ratelimiting;

import io.github.wxinnng.RpcApplication;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 滑动窗口限流
 */
@Slf4j
public class SlidingWindowRateLimiter implements Limiter{

    // 存储请求时间的队列
    private final Deque<Long> requests = new ArrayDeque<>();

    @Override
    public synchronized boolean tryAcquire() {
        log.info("滑动窗口限流");
        //当前系统时间
        long now = System.currentTimeMillis();
        //时间间隔,窗口大小
        long windowSizeInMillis = RpcApplication.getRpcConfig().getTimeInterval();
        //最大请求数
        int maxRequests = RpcApplication.getRpcConfig().getMaxRequests();
        // 清理窗口开始前的请求记录
        while (!requests.isEmpty() && now - requests.getFirst() >= windowSizeInMillis) {
            requests.pollFirst();
        }


        // 检查当前窗口内的请求量是否超过限制
        if (requests.size() < maxRequests) {
            requests.offer(now);
            return true;
        } else {
            return false;
        }
    }

}


