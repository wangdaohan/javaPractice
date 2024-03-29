package com.patrick.disruptoromssettlement.bean.res;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class Account {
    @NonNull
    private int id;
    @NonNull
    private long uid;
    @NonNull
    private String lastLoginDate;
    @NonNull
    private String lastLoginTime;

    private String token;
}
