package com.example.study.server.impl;

import com.example.study.pojo.dto.auth.Account;
import com.example.study.pojo.dto.course.ClassRoom;
import com.example.study.pojo.dto.course.ClassSchedule;
import com.example.study.pojo.dto.course.Course;
import com.example.study.repository.course.ClassScheduleRepository;
import com.example.study.server.ReminderService;
import com.example.study.utils.CourseTimeUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: maifuwa
 * @date: 2023/10/24 上午10:56
 * @description: 提醒上课实现类
 */
@Slf4j
@Service
public class ReminderServiceByQQBotImpl implements ReminderService {

    @Autowired
    CourseTimeUtil timeUtil;

//    @Autowired
//    RobotService robotService;

    @Autowired
    ClassScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public void reminderClass() {
        String courseNum = timeUtil.getCourseNum();
        List<ClassSchedule> schedules = scheduleRepository.getSchoolSchedulesByCourseNumLike(courseNum);
        for (ClassSchedule schedule : schedules) {
            List<Account> accounts = schedule.getAccounts();
            for (Account account : accounts) {
                this.reminderToAccount(schedule, schedule.getCourse(), schedule.getClassRoom(), account);
            }
        }
    }


    private void reminderToAccount(ClassSchedule schedule ,Course course, ClassRoom classRoom, Account account) {
        String message = "你好: " + account.getName()
                + " 你在: " + timeUtil.getCourseTime(schedule.getCourseNum())
                + " 有一节：" + course.getName()
                + " 请与上课时间五分钟之前赶往：" + classRoom.getLocation()
                + " 上课";
        log.info(message);
        // robotService.sendMessageToGroup(account.getQQNum(), message);
    }
}
