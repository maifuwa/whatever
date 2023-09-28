package com.example.study.listener;

import com.example.study.constant.AppConst;
import com.example.study.constant.MailConst;
import com.example.study.constant.ResourcePathConst;
import com.example.study.pojo.bo.MailParameter;
import com.example.study.server.GetUserInfoServer;
import com.example.study.utils.MailUtil;
import com.example.study.utils.SimpleUtils;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: maifuwa
 * @date: 2023/9/28 上午9:20
 * @description: 邮件发送消息队列监听器
 */
@Slf4j
@Component
@RabbitListener(queues = "mail")
public class MailQueueListener {

    @Resource
    MailUtil mailUtil;

    @Resource
    GetUserInfoServer getUserInfoServer;

    /**
     * 消息队列监听到事件回调方法
     * @param data 消息队列传递的参数
     */
    @RabbitHandler
    public void sendMail(Map<String, Object> data) {
        String type = (String) data.get("type");

        MailParameter parameter = new MailParameter();
        parameter.setTo((String) data.get("email"));

        if ("register".equals(type)) {
            parameter.setSubject(MailConst.REGISTER_EMAIL_SUBJECT);
            this.sendEmail(data, parameter);
        }else if ("reset".equals(type)) {
            parameter.setSubject(MailConst.RESET_EMAIL_SUBJECT);
            this.sendEmail(data, parameter);
        }
    }

    /**
     * 发送邮件方法
     * @param data 消息队列传递的参数
     * @param parameter 邮件参数
     */
    private void sendEmail(Map<String, Object> data, MailParameter parameter) {
        this.forVerifyEmail(parameter, (String) data.get("ip"), (String) data.get("userAgent"), (Integer) data.get("code"));
        try {
            mailUtil.sendHtmlMessage(parameter);
        } catch (MessagingException e) {
            log.info("邮件发送失败 to: {}", data.get("email"));
            throw new RuntimeException(e);
        }
    }

    /**
     * 针对发送验证码邮件 设置邮件参数
     * @param parameter 邮件参数
     * @param ip 参数之一：用户ip
     * @param userAgent 参数之一：用户userAgent
     * @param code 验证码
     */
    private void forVerifyEmail(MailParameter parameter, String ip, String userAgent, int code) {
        Map<String, Object> variables = new HashMap<>();
        String address = getUserInfoServer.getUserAddress(ip);
        String[] deviceAndBrowser = getUserInfoServer.getDeviceAndBrowser(userAgent);
        String[] dateAndTime = SimpleUtils.getDateTime();

        variables.put("appName", AppConst.APP_NAME);
        variables.put("ip", ip);
        variables.put("os", deviceAndBrowser[0]);
        variables.put("browser", deviceAndBrowser[1]);
        variables.put("address", address);
        variables.put("date", dateAndTime[0]);
        variables.put("time", dateAndTime[1]);
        variables.put("code", code);

        parameter.setTemplateEngineVariables(variables);
        parameter.setTemplateName(MailConst.TEMPLATE_NAME_VERIFY_EMAIL);
        parameter.setImagesAndPath(Map.of("logo", ResourcePathConst.LOGO_ROUND_PATH));
    }

}
