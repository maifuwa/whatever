package com.example.study.pojo.vo.request;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author: maifuwa
 * @date: 2023/9/28 下午7:24
 * @description: 重置密码vo类
 */
@Data
public class ResetPwdVo {

    @Email
    String email;

    @Length(min = 8, max = 20)
    String password;

    @Length(min = 6, max = 6)
    String code;
}
