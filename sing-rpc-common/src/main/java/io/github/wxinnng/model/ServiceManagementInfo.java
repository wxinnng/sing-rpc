package io.github.wxinnng.model;

import lombok.Data;

@Data
public class ServiceManagementInfo {
    private String serviceName;
    private Integer versionCount;
    private Integer instanceCount;
    private Integer status;
    private Integer groupCount;
    private String remoteRegistry;
}
