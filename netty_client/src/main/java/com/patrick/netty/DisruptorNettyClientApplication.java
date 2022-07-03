package com.patrick.netty;

import com.patrick.netty.client.NettyClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DisruptorNettyClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(DisruptorNettyClientApplication.class, args);
		NettyClient client = new NettyClient();
		client.sendData();

	}

}
