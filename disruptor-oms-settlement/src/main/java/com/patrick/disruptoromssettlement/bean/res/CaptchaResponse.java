package com.patrick.disruptoromssettlement.bean.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
public class CaptchaResponse {
    private String id;
    private String imageBase64;
}
