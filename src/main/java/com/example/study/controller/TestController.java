package com.example.study.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@Slf4j
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
