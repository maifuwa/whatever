package com.example.study.controller;

import com.alibaba.fastjson2.JSONObject;
import com.example.study.entity.IpAddress;
import com.example.study.utils.GetUserInfoUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@Slf4j
public class TestController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/test")
    public String doTest(Map<String, Object> map) {
        map.put("title", "测试成功");
        return "test";
    }

    @GetMapping("/ip")
    public String doVerify(HttpServletRequest request) {
        String ip = "183.197.99.198";
        IpAddress address = restTemplate.getForObject("http://ip-api.com/json/"+ip, IpAddress.class);
         String info = GetUserInfoUtil.getAddress(Optional.of(address));
        log.info(info);
        return "verifyEmail";
    }

}
