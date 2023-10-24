package com.example.study.controller;

import com.example.study.constant.UserConst;
import com.example.study.pojo.RestBean;
import com.example.study.pojo.vo.response.CourseTableVo;
import com.example.study.server.ClassScheduleServer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${Spring.verifiycode}")
    String verifiycode;

    @PostMapping("/upload")
    public RestBean<List<CourseTableVo>> uploadAccountCourse(String course, String password, HttpServletRequest request) {
        if (! verifiycode.equals(password)) {
            return RestBean.failure(401, "请先联系管理员获取邀请码");
        }

        Integer accountId = (Integer) request.getAttribute(UserConst.ATTR_USER_ID);
        List<CourseTableVo> vos = scheduleServer.parseCourseJson(accountId, course);
        if (vos == null) {
            return RestBean.failure(400, "添加课程表失败，请联系管理员");
        }
        return RestBean.success(vos);
    }

    @GetMapping("/obtain")
    public RestBean<List<CourseTableVo>> obtainAccountCourse(HttpServletRequest request) {
        Integer accountId = (Integer) request.getAttribute(UserConst.ATTR_USER_ID);
        List<CourseTableVo> vos = scheduleServer.getSchedulesForAccount(accountId);
        if (vos == null) {
            return RestBean.failure(400, "获取课程表失败，请先添加课程表;如已添加，请联系管理员");
        }
        return RestBean.success(vos);
    }

}
