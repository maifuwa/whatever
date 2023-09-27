package com.example.study.server.impl;

import com.example.study.pojo.RestBean;
import com.example.study.server.MailServer;
import com.example.study.utils.MailUtil;
import com.example.study.constant.MailConst;
import com.example.study.constant.ResourcePathConst;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class MailServerImpl implements MailServer {

    @Resource
    MailUtil mailUtil;

    public RestBean<Void> sendVerifyEmail(String to, Map<String, Object> valueMap) {
        try {
            Map<String, String> imagesAndPath = Map.of("logo", ResourcePathConst.LOGO_ROUND_PATH);

            mailUtil.sendTemplateMessage(to, MailConst.REGISTER_EMAIL_SUBJECT, valueMap, MailConst.VERIFY_EMAIL_TEMPLATE_NAME, imagesAndPath);
        } catch (MessagingException e) {
            log.error("邮件发送失败，to: {}, template: verifyEmail", to);
            return RestBean.failure(400, "请检查您的邮箱地址");
        }
        return RestBean.success();
    }
}
