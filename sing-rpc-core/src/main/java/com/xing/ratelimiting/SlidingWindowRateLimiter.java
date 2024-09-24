package com.xing.ratelimiting;

import com.xing.RpcApplication;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 滑动窗口限流
 */
public class SlidingWindowRateLimiter implements Limiter{

    // 存储请求时间的队列
    private final Deque<Long> requests = new ArrayDeque<>();

    @Override
    public synchronized boolean tryAcquire() {
        //当前系统时间
        long now = System.currentTimeMillis();
        //时间间隔,窗口大小
        long windowSizeInMillis = RpcApplication.getRpcConfig().getTimeInterval();
        //最大请求数
        int maxRequests = RpcApplication.getRpcConfig().getMaxRequests();
        // 清理窗口开始前的请求记录
//        System.err.println("maxRequest : " + maxRequests);
//        System.err.println("timeIntervals : " + windowSizeInMillis);
        while (!requests.isEmpty() && now - requests.getFirst() >= windowSizeInMillis) {
            requests.pollFirst();
        }


        // 检查当前窗口内的请求量是否超过限制
        if (requests.size() < maxRequests) {
            requests.offer(now);
//            System.err.println("queue size :" + requests.size());
//            System.err.println("now:" + now);
//            System.out.println("queueFirst: " + requests.getFirst());
//            System.err.println( now - requests.getFirst());
            return true;
        } else {
            return false;
        }
    }

}


