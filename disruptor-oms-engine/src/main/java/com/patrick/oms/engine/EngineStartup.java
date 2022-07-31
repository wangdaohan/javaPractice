package com.patrick.oms.engine;

import com.client.checksum.CheckSum;
import com.client.codec.BodyCodec;
import com.client.codec.MsgCodec;
import com.patrick.oms.engine.bean.EngineConfig;

public class EngineStartup {
    public static void main(String[] args) throws Exception {
        new EngineConfig("engine.properties",
                new BodyCodec(),
                new CheckSum(),
                new MsgCodec()).startup();
    }
}
