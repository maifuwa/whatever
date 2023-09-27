package com.example.study.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: maifuwa
 * @date: 2023/9/27 下午1:35
 * @description: 用来测试用户权限访问的
 */
@Controller
public class IndexController {


    @GetMapping("/")
    public String index() {
        return "index";
    }
}
