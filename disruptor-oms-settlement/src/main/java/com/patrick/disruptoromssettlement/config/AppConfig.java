package com.patrick.disruptoromssettlement.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class AppConfig {

    @Value("${counter.dataCenterId}")
    private long dataCenterId;
    @Value("${counter.workerId}")
    private long workerId;


}
