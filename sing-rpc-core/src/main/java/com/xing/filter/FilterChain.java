package com.xing.filter;


import com.xing.RpcApplication;
import com.xing.model.RpcRequest;
import com.xing.model.RpcResponse;
import com.xing.spi.SpiLoader;

import java.util.*;

public interface FilterChain {
    void doFilter(RpcRequest request,RpcResponse response);
}
