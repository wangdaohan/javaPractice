package com.eegsmart.service;
import com.eegsmart.osgi.*;

public class MyTest {
	private MyService myService;

	public void setMyService(MyService myService) {
		this.myService = myService;
	}

	void start() {

		System.out.println("start.......");
		System.out.println(myService.getHello());

	}

	void stop() {
		System.out.println("stop.....");
	}

}
