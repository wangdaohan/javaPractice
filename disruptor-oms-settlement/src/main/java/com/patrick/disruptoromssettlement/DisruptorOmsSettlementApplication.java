package com.patrick.disruptoromssettlement;

import com.patrick.disruptoromssettlement.config.AppConfig;
import com.patrick.disruptoromssettlement.uuid.GudyUuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class DisruptorOmsSettlementApplication {

	public static void main(String[] args) {
		SpringApplication.run(DisruptorOmsSettlementApplication.class, args);
	}

	@Autowired
	private AppConfig appConfig;

	@PostConstruct
	private void init(){
		//初始化UUID生成器
		GudyUuid.getInstance().init(appConfig.getDataCenterId(), appConfig.getWorkerId());
	}

}
