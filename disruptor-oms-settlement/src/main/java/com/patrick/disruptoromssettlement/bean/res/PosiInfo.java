package com.patrick.disruptoromssettlement.bean.res;

import lombok.*;

/**
 * 持仓信息
 */
@Data
@NoArgsConstructor
@ToString
public class PosiInfo {

    private int id;
    private long uid;
    private int code;
    private String name;
    private long cost;
    private long count;


}
