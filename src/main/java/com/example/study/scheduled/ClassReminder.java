package com.example.study.scheduled;

import com.example.study.server.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author: maifuwa
 * @date: 2023/10/24 上午10:55
 * @description: 每天提醒用户上课定时任务
 */
@Component
public class ClassReminder {

    @Autowired
    ReminderService reminderService;

    // 秒 分 小时 天 月份 星期 年(可忽略)
    @Scheduled(cron = "0 40 7 * * MON-FRI")
    @Scheduled(cron = "0 25 9 * * MON-FRI")
    @Scheduled(cron = "0 40 13 * * MON-FRI")
    @Scheduled(cron = "0 20 15 * * MON-FRI")
    @Scheduled(cron = "0 10 16 * * MON-FRI")
    @Scheduled(cron = "0 40 18 * * MON-FRI")
    public void sendSomeThing() {
        reminderService.reminderClass();
    }
}
