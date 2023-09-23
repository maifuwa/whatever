package com.example.study.controller;

import com.example.study.entity.RestBean;
import com.example.study.server.AuthorizeServer;
import com.example.study.utils.SomeUtil;
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

        String ip = request.getRemoteAddr();
        String[] info = SomeUtil.getUserInfo(request.getHeader("User-Agent"));
        String[] dataTime = SomeUtil.getDateTime();
        String code = String.valueOf(SomeUtil.getVerifyCode());

        valueMap.put("ip", ip);
        valueMap.put("os", info[0]);
        valueMap.put("browser", info[1]);
        valueMap.put("address", "暂时先不获取");
        valueMap.put("date", dataTime[0]);
        valueMap.put("time", dataTime[1]);
        valueMap.put("code", code);

        return authorizeServer.sendVerifyEmail(email, valueMap);
    }
}
