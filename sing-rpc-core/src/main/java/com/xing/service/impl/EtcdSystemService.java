package com.xing.service.impl;

import cn.hutool.json.JSONUtil;
import com.xing.constant.RpcConstant;
import com.xing.model.RemoteServiceInfo;
import com.xing.model.ServiceManagementInfo;
import com.xing.model.ServiceMetaInfo;
import com.xing.registry.RegistryKeys;
import com.xing.service.SystemService;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.KeyValue;
import io.etcd.jetcd.options.GetOption;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.netty.util.ThreadDeathWatcher.watch;
@Slf4j
public class EtcdSystemService implements SystemService {


    private static KV kvClient;

    public static void setClient(KV client){
        EtcdSystemService.kvClient = client;
    }

    @Override
    public List<RemoteServiceInfo> getServiceInfo() {
        List<RemoteServiceInfo> serviceMetaInfo = null;
        // 前缀查询
        try{
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.get(
                            ByteSequence.from(RegistryKeys.ETCD_ROOT_PATH, StandardCharsets.UTF_8),
                            getOption)
                    .get()
                    .getKvs();
            // 解析服务信息
            serviceMetaInfo = keyValues.stream()
                    .map(keyValue -> {
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value, RemoteServiceInfo.class);
                    })
                    .collect(Collectors.toList());
        }catch (Exception e){
            log.error("远程服务调用异常！{}",e.getMessage());
        }
        return serviceMetaInfo;
    }


    @Override
    public List<ServiceManagementInfo> getServiceManagementInfo() {
        ArrayList<ServiceManagementInfo> serviceManagementInfos = new ArrayList<>();
        ServiceManagementInfo serviceManagementInfo = new ServiceManagementInfo();
        serviceManagementInfo.setServiceName("userService");
        serviceManagementInfo.setInstanceCount(12);
        serviceManagementInfo.setGroupCount(38);
        serviceManagementInfo.setVersionCount(2);
        for (int i = 0; i < 9; i++){
            serviceManagementInfos.add(serviceManagementInfo);
        }
        return serviceManagementInfos;
    }
}
