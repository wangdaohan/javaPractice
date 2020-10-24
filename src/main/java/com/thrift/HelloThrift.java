package com.thrift;

import org.apache.thrift.TException;

/**
 * Created by patrick on 2016/4/19.
 */
public class HelloThrift implements HelloWorldService.Iface {

    public HelloThrift() {
    }

    @Override
    public String sayHello(String username) throws TException {
        return "Hi," + username + " welcome to my blog www.micmiu.com";
    }


}
