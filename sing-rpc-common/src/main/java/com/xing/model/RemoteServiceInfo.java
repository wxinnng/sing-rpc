package com.xing.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class RemoteServiceInfo {
    private String id;
    private String name;
    private String version;
    private String url;
    private String group;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;
}
