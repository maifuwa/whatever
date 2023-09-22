package com.example.study.server.impl;

import com.example.study.entity.RestBean;
import com.example.study.server.AuthorizeServer;
import com.example.study.utils.MailUtil;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class AuthorizeServerImpl implements AuthorizeServer {

    @Autowired
    MailUtil mailUtil;

    public RestBean<Void> sendVerifyEmail(String to, Map<String, Object> valueMap) {
        try {
            String templateName = "verifyEmail";
            String subject = "准许登录 Firefox";
            Map<String, String> imagesAndPath = Map.of("logo", "static/images/firefox-logo.png");

            mailUtil.sendTemplateMessage(to, subject, valueMap, templateName, imagesAndPath);
        } catch (MessagingException e) {
            log.error("邮件发送失败，to: {}, template: verifyEmail", to);
            return RestBean.failure(400, "请检查您的邮箱地址");
        }
        return RestBean.success();
    }
}
