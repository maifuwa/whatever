package com.example.study.utils;

import love.forte.simbot.Identifies;
import love.forte.simbot.application.Application;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.component.mirai.bot.MiraiBotManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: maifuwa
 * @date: 2023/11/1 下午8:10
 * @description:
 */
@Component
public class BotUtil {

    @Autowired
    Application application;

    @Value("${spring.botId:1416632423}")
    long id;

    public Bot findBot() {
        return this.findBot(id);
    }

    public Bot findBot(long botId) {
        MiraiBotManager miraiBotManager = application.getBotManagers().getFirst(MiraiBotManager.class);
        return miraiBotManager.get(Identifies.ID(botId));
    }
}
