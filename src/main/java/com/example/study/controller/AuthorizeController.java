package com.example.study.controller;

import com.example.study.pojo.auth.IpAddress;
import com.example.study.pojo.RestBean;
import com.example.study.server.MailServer;
import com.example.study.utils.Const;
import com.example.study.utils.GetUserInfoUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/api/auth/")
public class AuthorizeController {

    @Autowired
    MailServer authorizeServer;

    @Autowired
    RestTemplate restTemplate;

    @PostMapping("/verifyemail")
    public RestBean<Void> doVerify(@Email String email, HttpServletRequest request) {
        Map<String, Object> valueMap = new HashMap<>();

        String ip = request.getRemoteAddr();
        IpAddress address =  restTemplate.getForObject(Const.API_IP_TO_LOCATION +ip, IpAddress.class);
        String[] info = GetUserInfoUtil.getUserInfo(request.getHeader("User-Agent"));
        String[] dataTime = GetUserInfoUtil.getDateTime();
        String code = String.valueOf(GetUserInfoUtil.getVerifyCode());

        valueMap.put("appName", Const.APP_NAME);
        valueMap.put("ip", ip);
        valueMap.put("os", info[0]);
        valueMap.put("browser", info[1]);
        valueMap.put("address", GetUserInfoUtil.getAddress(Optional.of(address)));
        valueMap.put("date", dataTime[0]);
        valueMap.put("time", dataTime[1]);
        valueMap.put("code", code);

        return authorizeServer.sendVerifyEmail(email, valueMap);
    }

    @PostMapping("login")
    public RestBean<Void> doLogin() {
        return RestBean.success();
    }

    @PostMapping("signup")
    public RestBean<Void> doSignup() {return RestBean.success();}
}
