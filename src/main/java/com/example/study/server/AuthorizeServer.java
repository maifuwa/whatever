package com.example.study.server;

import com.example.study.entity.RestBean;

import java.util.Map;

public interface AuthorizeServer {

    RestBean<Void> sendVerifyEmail(String to, Map<String, Object> valueMap);

}
