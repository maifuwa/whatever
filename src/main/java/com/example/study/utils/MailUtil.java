package com.example.study.utils;

import com.example.study.pojo.bo.MailParameter;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;
import java.util.Optional;

/**
 * @author: maifuwa
 * @date: 2023/9/27 下午6:27
 * @description: 发送邮件的工具类
 */
@Component
public class MailUtil {

    @Resource
    JavaMailSender sender;

    @Resource
    SpringTemplateEngine templateEngine;

    @Value("${Spring.mail.username}")
    String from;

    @Value("${Spring.mail.nickname}")
    String nickname;

    /**
     * 获取邮件的from
     * @return String
     */
    private String getFrom() {
        return nickname + "<" + from + ">";
    }

    /**
     * 发送文本邮件
     * @param mailParameter 邮件参数
     */
    public void sendTxtMessage(MailParameter mailParameter) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(mailParameter.getSubject());
        message.setText(mailParameter.getContent());
        message.setTo(mailParameter.getTo());
        message.setFrom(this.getFrom());

        sender.send(message);
    }

    /**
     * 发送html邮件
     * @param mailParameter 邮件参数
     * @throws MessagingException 邮件发送失败异常
     */
    public void sendHtmlMessage(MailParameter mailParameter) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(this.getFrom());
        helper.setTo(mailParameter.getTo());
        helper.setSubject(mailParameter.getSubject());

        // 获取要发送 html 字符串
        Context context = new Context();
        Optional.ofNullable(mailParameter.getTemplateEngineVariables()).ifPresent(context::setVariables);
        String content = templateEngine.process(mailParameter.getTemplateName(), context);
        helper.setText(content, true);

        // 添加图片 到邮件附件
        Optional.ofNullable(mailParameter.getImagesAndPath()).ifPresent((imagesAndPath) -> {
            this.addImage(imagesAndPath, helper);
        });

        sender.send(message);
    }

    /**
     * 给邮件添加图片附件
     * @param imagesAndPath 图片名字和路径
     * @param helper 邮件配置类
     */
    private void addImage(Map<String, String> imagesAndPath, MimeMessageHelper helper) {
        for (Map.Entry<String, String> entry : imagesAndPath.entrySet()) {
            String image = entry.getKey();
            String path = entry.getValue();
            try {
                helper.addInline(image, new ClassPathResource(path));
            } catch (MessagingException e) {
                throw new RuntimeException("图片路径不对");
            }
        }
    }

}
