package com.patrick.disruptoromssettlement.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class AppConfig {

    /////////////////////UUID 相关配置////////////////////////////////
    @Value("${counter.dataCenterId}")
    private long dataCenterId;

    @Value("${counter.workerId}")
    private long workerId;
    /////////////////////////////////////////////////////

    /////////////////////交易所会员号////////////////////////////////
    @Value("${counter.memberid}")
    private short id;

}
