package com.example.study.pojo.vo.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * @author: maifuwa
 * @date: 2023/9/27 下午1:35
 * @description: 用户vo类
 */
@Data
public class AccountVo {
    String name;
    String token;
    String avatarUrl;
    String introduction;
}
