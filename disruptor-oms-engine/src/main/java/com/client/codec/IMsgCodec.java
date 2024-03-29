package com.client.codec;

import com.client.bean.msg.CommonMsg;
import io.vertx.core.buffer.Buffer;


public interface IMsgCodec {

    //TCP <-> CommonMsg
    Buffer encodeToBuffer(CommonMsg msg);

    CommonMsg decodeFromBuffer(Buffer buffer);

}
