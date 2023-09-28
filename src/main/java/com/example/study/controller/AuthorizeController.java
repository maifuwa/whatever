package com.example.study.controller;

import com.example.study.constant.MailConst;
import com.example.study.pojo.RestBean;
import com.example.study.pojo.dto.auth.Account;
import com.example.study.pojo.vo.request.RegisterVo;
import com.example.study.pojo.vo.request.ResetPwdVo;
import com.example.study.pojo.vo.response.AccountVo;
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

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/auth/")
public class AuthorizeController {

    @Resource
    AccountServer accountServer;

    @PostMapping("/signup")
    public RestBean<AccountVo> doSignup(@Valid @RequestBody RegisterVo vo) {
       return this.parseAccountVo(accountServer.registerAccount(MailConst.VERIFY_EMAIL_TYPE_REGISTER, vo));
    }

    @PostMapping("/reset")
    public RestBean<AccountVo> doResetPassword(@Valid @RequestBody ResetPwdVo vo) {
        return this.parseAccountVo(accountServer.resetPassword(MailConst.VERIFY_EMAIL_TYPE_RESET, vo));
    }

    private RestBean<AccountVo> parseAccountVo(AccountVo vo) {
        if (vo.getToken().length() < 50) {
            return RestBean.failure(400, vo.getToken());
        }
        return RestBean.success(vo);
    }

    @PostMapping("/verifyemail")
    public RestBean<String> doVerify(@Email String email, String type, HttpServletRequest request) {
        if (MailConst.VERIFY_EMAIL_TYPE.contains(type)) {
            return RestBean.success(accountServer.sendEmailVerifyCode(type, email, request.getRemoteAddr(), request.getHeader("User-Agent")));
        }
        return RestBean.failure(400, "参数错误");
    }



}
