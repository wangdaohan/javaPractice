package com.patrick.netty;

import com.patrick.netty.server.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DisruptorNettyServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DisruptorNettyServerApplication.class, args);
		new NettyServer();
	}

}
