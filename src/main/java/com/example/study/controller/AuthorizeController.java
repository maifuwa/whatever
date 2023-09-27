package com.example.study.controller;

import com.example.study.pojo.RestBean;
import com.example.study.server.GetUserInfoServer;
import com.example.study.server.MailServer;
import com.example.study.utils.SimpleUtils;
import com.example.study.constant.AppConst;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Email;
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

    @Resource
    MailServer mailServer;

    @Resource
    GetUserInfoServer getUserInfoServer;

    @PostMapping("signup")
    public RestBean<Void> doSignup() {return RestBean.success();}

    @PostMapping("/verifyemail")
    public RestBean<Void> doVerify(@Email String email, HttpServletRequest request) {
        Map<String, Object> valueMap = new HashMap<>();

        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        String address = getUserInfoServer.getUserAddress(ip);
        String[] deviceAndBrowser = getUserInfoServer.getDeviceAndBrowser(userAgent);
        String[] dateAndTime = SimpleUtils.getDateTime();
        int code = SimpleUtils.getVerifyCode();


        valueMap.put("appName", AppConst.APP_NAME);
        valueMap.put("ip", ip);
        valueMap.put("os", deviceAndBrowser[0]);
        valueMap.put("browser", deviceAndBrowser[1]);
        valueMap.put("address", address);
        valueMap.put("date", dateAndTime[0]);
        valueMap.put("time", dateAndTime[1]);
        valueMap.put("code", code);

        return mailServer.sendVerifyEmail(email, valueMap);
    }

}
