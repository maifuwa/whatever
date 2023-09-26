package com.example.study.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;


@Component
@Slf4j
public class MailUtil {

    @Autowired
    JavaMailSender sender;

    @Autowired
    SpringTemplateEngine templateEngine;

    @Value("${Spring.mail.username}")
    String from;

    @Value("${Spring.mail.nickname}")
    String nickname;

    public void sendTxtMessage(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setText(content);
        message.setTo(to);
        message.setFrom(nickname + "<" + from + ">");

        sender.send(message);
    }


    private MimeMessageHelper sendTemplateMessage(String to, String subject, Map<String, Object> valueMap, String templateName, MimeMessage message) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        message.setFrom(nickname + "<" + from + ">");
        helper.setTo(to);
        helper.setSubject(subject);

        // 利用 Thymeleaf 引擎渲染 HTML
        Context context = new Context();
        context.setVariables(valueMap);  // 设置注入的变量
        String content = templateEngine.process(templateName, context);
        helper.setText(content, true);

        return helper;
    }


    public void sendTemplateMessage(String to, String subject, Map<String, Object> valueMap, String templateName) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        sendTemplateMessage(to, subject, valueMap, templateName, message);
        sender.send(message);
    }

    public void sendTemplateMessage(String to, String subject, Map<String, Object> valueMap, String templateName, Map<String, String> imagesAndPath) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = sendTemplateMessage(to, subject, valueMap, templateName, message);
        addImage(imagesAndPath, helper);
        sender.send(message);
    }


    public void addImage(Map<String, String> imagesAndPath, MimeMessageHelper helper) throws MessagingException {
        for (Map.Entry<String, String> entry : imagesAndPath.entrySet()) {
            String image = entry.getKey();
            String path = entry.getValue();
            helper.addInline(image, new ClassPathResource(path));
        }
    }

}
