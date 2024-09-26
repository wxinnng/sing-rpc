package io.github.wxinnng.loadbalancer;

import io.github.wxinnng.model.ServiceMetaInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 轮询算法
 */
@Slf4j
public class RoundRobinLoadBalancer implements LoadBalancer{

    //轮询到服务的下标
    private final AtomicInteger currenIndex = new AtomicInteger(0);

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if(serviceMetaInfoList.isEmpty()){
            return null;
        }

        int size = serviceMetaInfoList.size();

        if(size == 1){
            //只有一个服务，直接返回即可
            return serviceMetaInfoList.get(0);
        }
        //取模算法轮询
        int index = currenIndex.getAndIncrement() % size;

        return serviceMetaInfoList.get(index);
    }
}
