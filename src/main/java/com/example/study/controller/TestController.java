package com.example.study.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class TestController {

    @GetMapping("/test")
    public String doTest(Map<String, Object> map) {
        map.put("title", "测试成功");
        return "test";
    }

    @GetMapping("/verify")
    public String doVerify() {
        return "verifyEmail";
    }
}
