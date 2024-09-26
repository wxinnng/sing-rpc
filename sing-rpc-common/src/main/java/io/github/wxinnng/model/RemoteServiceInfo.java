package io.github.wxinnng.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class RemoteServiceInfo {
    private String serviceName;
    private String serviceVersion ;
    private String serviceHost;
    private Integer servicePort;
    private String serviceGroup;
    private String token;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registerTime;
    private String id;
}
