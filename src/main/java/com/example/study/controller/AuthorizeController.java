package com.example.study.controller;

import com.example.study.pojo.RestBean;
import com.example.study.pojo.vo.request.RegisterVo;
import com.example.study.server.AccountServer;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/auth/")
public class AuthorizeController {

    @Resource
    AccountServer accountServer;

    @PostMapping("signup")
    public RestBean<Void> doSignup(@RequestBody @Valid RegisterVo vo) {

        return RestBean.success();
    }

    @PostMapping("/verifyemail")
    public RestBean<String> doVerify(@Email String email,
                                     @Pattern(regexp = "(register|reset)") String type,
                                     HttpServletRequest request) {

        return RestBean.success(accountServer.sendEmailVerifyCode(type, email, request.getRemoteAddr(), request.getHeader("User-Agent")));

    }

}
