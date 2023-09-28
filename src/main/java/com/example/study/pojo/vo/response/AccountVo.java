package com.example.study.pojo.vo.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
public class AccountVo {
    String name;
    List<String> roles;
    String token;
    Instant expireTime;

}
