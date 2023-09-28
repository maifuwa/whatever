package com.example.study.server;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountServer extends UserDetailsService {

    String sendEmailVerifyCode(String type, String email, String ip, String userAgent);

}
