package com.example.study.pojo.vo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author: maifuwa
 * @date: 2023/9/27 下午6:27
 * @description: 注册账户的vo类
 */
@Data
public class RegisterVo {

    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")
    @Length(min = 1, max = 16)
    String userName;

    @Length(min = 8, max = 20)
    String password;

    @Email
    String email;

    @Length(min = 6, max = 6)
    Integer code;
}
