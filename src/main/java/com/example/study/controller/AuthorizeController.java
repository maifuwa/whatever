package com.example.study.controller;

import com.example.study.entity.RestBean;
import com.example.study.server.AuthorizeServer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/api/auth/")
public class AuthorizeController {

    @Autowired
    AuthorizeServer authorizeServer;

    @PostMapping("/verifyEmail")
    public RestBean<Void> doVerify(@Email String email, HttpServletRequest request) {
        Map<String, Object> valueMap = new HashMap<>();

        // TODO 生成6为数密码  获取用户信息: 操作系统，地址，ip地址，日期，时间(CST)


        return authorizeServer.sendVerifyEmail(email, valueMap);
    }
}
