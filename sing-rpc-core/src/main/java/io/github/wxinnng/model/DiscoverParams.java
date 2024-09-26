package io.github.wxinnng.model;

import io.github.wxinnng.RpcApplication;
import lombok.Data;

@Data
public class DiscoverParams {
    private String version = RpcApplication.getRpcConfig().getVersion();
    private String group = RpcApplication.getRpcConfig().getServiceGroup();
    private Boolean mock = false;
}
