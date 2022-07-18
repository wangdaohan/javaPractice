package com.demo.disruptordemo.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

/**
 * RbData 转化为 RbCmd 去ringbuffer被人消费
 *
 * RbCmd 转化为 RbData 为消费者消费 for further process
 */

@Builder
@ToString
@AllArgsConstructor
public class RbData {
    public int code;
    public String msg;
}
