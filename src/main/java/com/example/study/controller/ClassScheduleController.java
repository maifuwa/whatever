package com.example.study.controller;

import com.example.study.constant.UserConst;
import com.example.study.pojo.vo.response.CourseTableVo;
import com.example.study.server.ClassScheduleServer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: maifuwa
 * @date: 2023/10/24 下午4:03
 * @description: 上传获取课程表的api
 */
@RestController
@RequestMapping("/schedule")
public class ClassScheduleController {

    @Autowired
    ClassScheduleServer scheduleServer;

    @PostMapping("/upload")
    public List<CourseTableVo> uploadAccountCourse(String course, HttpServletRequest request) {
        Integer accountId = (Integer) request.getAttribute(UserConst.ATTR_USER_ID);
        return scheduleServer.parseCourseJson(accountId, course);
    }

    @GetMapping("/obtain")
    public List<CourseTableVo> obtainAccountCourse(HttpServletRequest request) {
        Integer accountId = (Integer) request.getAttribute(UserConst.ATTR_USER_ID);
        return scheduleServer.getSchedulesForAccount(accountId);
    }

}
