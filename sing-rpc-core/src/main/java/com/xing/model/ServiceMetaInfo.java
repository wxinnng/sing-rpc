package com.xing.model;

import cn.hutool.core.util.StrUtil;
import com.xing.RpcApplication;
import lombok.Data;

@Data
public class ServiceMetaInfo {
    private String serviceName;
    private String serviceVersion = RpcApplication.getRpcConfig().getVersion();
    private String serviceHost;
    private Integer servicePort;
    private String serviceGroup = RpcApplication.getRpcConfig().getServiceGroup();
    private String token;
    /**
     * 获取服务键名
     *
     * @return
     */
    public String getServiceKey() {
        // 后续可扩展服务分组
        // return String.format("%s:%s:%s", serviceName, serviceVersion, serviceGroup);
        return String.format("%s/%s/%s", this.serviceName, this.serviceGroup,this.serviceVersion);
    }

    /**
     * 获取服务注册节点键名
     *
     * @return
     */
    public String getServiceNodeKey() {
        //rpc/service/group/version/ host:port
        return String.format("%s/%s:%s", getServiceKey(), this.serviceHost, this.servicePort);
    }




    /**
     * 获取完整服务地址
     *
     * @return
     */
    public String getServiceAddress() {
        if (!StrUtil.contains(serviceHost, "http")) {
            return String.format("http://%s:%s", serviceHost, servicePort);
        }
        return String.format("%s:%s", serviceHost, servicePort);
    }

}
