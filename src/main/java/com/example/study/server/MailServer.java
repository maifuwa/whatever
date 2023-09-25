package com.example.study.server;

import com.example.study.pojo.RestBean;

import java.util.Map;

public interface MailServer {

    RestBean<Void> sendVerifyEmail(String to, Map<String, Object> valueMap);

}
