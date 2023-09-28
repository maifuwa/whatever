package com.example.study.pojo.bo;

import lombok.Data;

import java.util.Map;

/**
 * @author: maifuwa
 * @date: 2023/9/28 上午9:43
 * @description: 邮件发送所需参数
 */
@Data
public class MailParameter {
    String to;
    String subject;
    String content;
    String templateName;
    Map<String, Object> templateEngineVariables;
    Map<String, String> imagesAndPath;
}
