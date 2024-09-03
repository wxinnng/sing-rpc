package com.xing.loadbalancer;

import com.xing.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ConsistentHashLoadBalancer implements LoadBalancer{

    //虚拟节点存放
    private final TreeMap<Integer,ServiceMetaInfo> virtualNodes = new TreeMap<>();

    //虚拟节点个数
    private static final int VIRTUAL_NODE_NUM = 100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {

        //节点列表是空，就直接返回null即可
        if(serviceMetaInfoList.isEmpty())
            return null;

        //构建虚拟节点环(每一个服务实例都有 VIRTUAL_NODE_NUM个节点)
        for(ServiceMetaInfo serviceMetaInfo: serviceMetaInfoList){
            for(int i = 0 ;i < VIRTUAL_NODE_NUM;i ++){
                int hash = getHash(serviceMetaInfo.getServiceAddress() + "#" + i);
                virtualNodes.put(hash,serviceMetaInfo);
            }
        }

        // 获得调用请求的hash值
        int hash = getHash(requestParams);

        // 选择最接近且大于等于请求的hash值的虚拟节点
        Map.Entry<Integer,ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);

        if(entry == null){
            //如果没有比请求hash大的节点，直接返回第一个节点
            entry = virtualNodes.firstEntry();
        }

        //返回节点信息
        return entry.getValue();



    }


    /**
     * 可以扩展成别的hash算法
     * @param key
     * @return
     */
    private int getHash(Object key){
        return key.hashCode();
    }
}
