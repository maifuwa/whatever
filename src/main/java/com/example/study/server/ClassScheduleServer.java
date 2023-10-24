package com.example.study.server;


import com.example.study.pojo.dto.course.ClassRoom;
import com.example.study.pojo.dto.course.ClassSchedule;
import com.example.study.pojo.dto.course.Course;
import com.example.study.pojo.vo.response.CourseTableVo;

import java.util.List;

/**
 * @author maifuwa
 * @date 2023/10/12 上午10:54
 * @description 录入课程表
 */
public interface ClassScheduleServer {

    Course addCourse(String name, String fullName, Double credit, String category, String nature);

    ClassRoom addClassRoom(String location, String building);

    ClassRoom addClassRoom(String location, String building, String campus);

    ClassSchedule addClassSchedule(String fullName, String location, String teacher, String courseNum, Integer day, String week);

    boolean mergeAccountAndClassSchedule(String accountName, String fullName, String location, String teacher, String courseNum, Integer day, String week);

    boolean mergeAccountAndClassSchedule(Integer accountId, Long classScheduleId);


    List<CourseTableVo> parseCourseJson(Integer accountId, String courseJson);

    List<CourseTableVo> getSchedulesForAccount(Integer accountId);

    List<CourseTableVo> parseSchedulesToVo(List<ClassSchedule> schedules);
}
