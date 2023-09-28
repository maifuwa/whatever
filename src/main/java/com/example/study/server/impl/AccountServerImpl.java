package com.example.study.server.impl;

import com.example.study.constant.MailConst;
import com.example.study.pojo.dto.auth.Account;
import com.example.study.repository.AccountRepository;
import com.example.study.server.AccountServer;
import com.example.study.utils.SimpleUtils;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AccountServerImpl implements AccountServer {

    @Resource
    AccountRepository repository;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Resource
    AmqpTemplate rabbitTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = repository.findAccountByEmailOrName(username);

        if (account == null) {
            throw new UsernameNotFoundException("用户名或邮箱错误");
        }
        return User
                .withUsername(account.getName())
                .password(account.getPassword())
                .roles(repository.getAllRolesById(account.getId()).toArray(String[]::new))
                .build();
    }


    @Override
    public String sendEmailVerifyCode(String type, String email, String ip, String userAgent) {
        if (!isEmailTimeOut(MailConst.VERIFY_EMAIL_DATA + email)) {
            return "验证码仍有效，请不要频繁请求";
        }
        int code = SimpleUtils.getVerifyCode();
        Map<String, Object> data = Map.of("type", type, "email", email, "ip", ip, "userAgent", userAgent, "code", code);
        rabbitTemplate.convertAndSend(MailConst.EMAIL_SEND_MQ, data);
        redisTemplate.opsForValue()
                .set(MailConst.VERIFY_EMAIL_DATA + email, String.valueOf(code), 5, TimeUnit.MINUTES);
        return "发送成功";
    }

    private boolean isEmailTimeOut(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
