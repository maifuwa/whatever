package com.example.study.config;

import com.example.study.constant.MailConst;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author: maifuwa
 * @date: 2023/9/28 上午9:17
 * @description: 消息队列的配置类
 */
@Configuration
public class RabbitmqConfiguration {

    @Bean("mailQueue")
    public Queue createMailQueue() {
        return QueueBuilder
                .durable(MailConst.EMAIL_SEND_MQ)
                .build();
    }


}
