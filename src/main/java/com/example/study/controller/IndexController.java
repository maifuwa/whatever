package com.example.study.controller;

import com.example.study.constant.UserConst;
import com.example.study.pojo.dto.auth.Account;
import com.example.study.repository.auth.AccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * @author: maifuwa
 * @date: 2023/9/27 下午1:35
 * @description: 用来测试用户权限访问的
 */
@Controller
public class IndexController {

    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/")
    public String index(HttpServletRequest request, Map<String, Object> valueMap) {
        Integer accountId = (Integer) request.getAttribute(UserConst.ATTR_USER_ID);
        Account account = accountRepository.findById(accountId).get();
        valueMap.put("account", account);
        return "index";
    }
}
