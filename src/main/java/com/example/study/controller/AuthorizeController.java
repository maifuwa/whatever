package com.example.study.controller;

import com.example.study.constant.MailConst;
import com.example.study.constant.UserConst;
import com.example.study.pojo.RestBean;
import com.example.study.pojo.vo.request.RegisterVo;
import com.example.study.pojo.vo.request.ResetPwdVo;
import com.example.study.pojo.vo.response.AccountVo;
import com.example.study.server.AccountServer;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: maifuwa
 * @date: 2023/9/27 下午1:35
 * @description: 用户登陆注册的api
 */
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
            return RestBean.unauthorized(vo.getToken());
        }
        return RestBean.success(vo);
    }

    @PostMapping("/verifyemail")
    public RestBean<String> doVerify(@Email String email, String type, HttpServletRequest request) {
//        if (! email.toLowerCase().contains("qq")) {
//            return RestBean.failure(400, "目前只支持qq邮箱");
//        }
        if (MailConst.VERIFY_EMAIL_TYPE.contains(type)) {
            return RestBean.success(accountServer.sendEmailVerifyCode(type, email, request.getRemoteAddr(), request.getHeader("User-Agent")));
        }
        return RestBean.unauthorized("参数错误");
    }

    @PostMapping("/changeprofile")
    public RestBean<AccountVo> changProfile(String name, String introduction, HttpServletRequest request) {
        Integer accountId = (Integer) request.getAttribute(UserConst.ATTR_USER_ID);
        return this.parseAccountVo(accountServer.changeProfile(accountId, name, introduction));
    }

}
