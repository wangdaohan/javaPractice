package com.patrick.disruptoromsraftseq;

import com.client.codec.BodyCodec;

public class RaftSeqStartUp3 {
    public static void main(String[] args) throws Exception {
        String configName = "raft-seq3.properties";
        new RaftSeqConfig(configName,new BodyCodec()).startup();
    }
}
