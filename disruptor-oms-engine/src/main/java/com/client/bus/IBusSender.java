package com.client.bus;

import com.client.bean.msg.CommonMsg;

public interface IBusSender {
    void startup();
    void publish(CommonMsg commonMsg);
}
