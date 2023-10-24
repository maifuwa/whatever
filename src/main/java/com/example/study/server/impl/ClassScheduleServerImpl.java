package com.example.study.server.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.study.pojo.dto.auth.Account;
import com.example.study.pojo.dto.course.*;
import com.example.study.pojo.vo.response.CourseTableVo;
import com.example.study.repository.auth.AccountRepository;
import com.example.study.repository.course.*;
import com.example.study.server.ClassScheduleServer;
import com.example.study.utils.CourseTimeUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author: maifuwa
 * @date: 2023/10/24 下午2:46
 * @description: 录入课程表实现类
 */
@Service
public class ClassScheduleServerImpl implements ClassScheduleServer {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    SubjectPlatformRepository subjectPlatformRepository;

    @Override
    @Transactional
    public Course addCourse(String name, String fullName, Double credit, String category, String nature) {
        Course course = courseRepository.findCourseByFullName(fullName);
        if (course != null) {
            return course;
        }
        course = new Course();
        course.setName(name);
        course.setFullName(fullName);
        course.setCredit(credit);

        SubjectPlatform subjectPlatform = subjectPlatformRepository.findSubjectPlatformByCategoryAndNature(category, nature);
        if (subjectPlatform == null) {
            subjectPlatform = new SubjectPlatform();
            subjectPlatform.setCategory(category);
            subjectPlatform.setNature(nature);
        }
        course.setSubjectPlatform(subjectPlatform);
        return courseRepository.save(course);
    }


    @Autowired
    ClassRoomRepository classRoomRepository;

    @Autowired
    ClassBuildingRepository classBuildingRepository;

    @Override
    @Transactional
    public ClassRoom addClassRoom(String location, String building) {
        return this.addClassRoom(location, building, "新校区");
    }
    @Override
    @Transactional
    public ClassRoom addClassRoom(String location, String building, String campus) {
        ClassRoom classRoom = classRoomRepository.findClassRoomByLocation(location);
        if (classRoom != null) {
            return classRoom;
        }
        classRoom = new ClassRoom();
        classRoom.setLocation(location);

        ClassBuilding classBuilding = classBuildingRepository.findClassBuildingByBuildingAndCampus(building, campus);
        if (classBuilding == null) {
            classBuilding = new ClassBuilding();
            classBuilding.setBuilding(building);
            classBuilding.setCampus(campus);
        }
        classRoom.setBuilding(classBuilding);
        return classRoomRepository.save(classRoom);
    }


    @Autowired
    ClassScheduleRepository scheduleRepository;

    @Autowired
    AccountRepository accountRepository;

    @Override
    @Transactional
    public ClassSchedule addClassSchedule(String fullName, String location, String teacher, String courseNum, Integer day, String week) {
        Course course = courseRepository.findCourseByFullName(fullName);
        ClassRoom classRoom = classRoomRepository.findClassRoomByLocation(location);
        if (course == null || classRoom == null) {
            throw new RuntimeException("课程非法录入 课名：" + fullName + " 地址：" + location);
        }

        ClassSchedule schedule = scheduleRepository.getSchoolScheduleByCourseAndClassRoomAndTeacherAndCourseNumAndDayAndWeek(course, classRoom, teacher, courseNum, day, week);
        if (schedule != null) {
            return schedule;
        }

        schedule = new ClassSchedule();
        schedule.setCourse(course);
        schedule.setClassRoom(classRoom);
        schedule.setTeacher(teacher);
        schedule.setCourseNum(courseNum);
        schedule.setDay(day);
        schedule.setWeek(week);
        return scheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    public boolean mergeAccountAndClassSchedule(String accountName, String fullName, String location, String teacher, String courseNum, Integer day, String week) {
        Account account = accountRepository.findAccountByEmailOrName(accountName);
        ClassSchedule schedule = this.findClassSchedule(fullName, location, teacher, courseNum, day, week);
        if (account == null || schedule == null) {
            return false;
        }
        return this.mergeAccountAndClassSchedule(account, schedule);
    }

    @Override
    @Transactional
    public boolean mergeAccountAndClassSchedule(Integer accountId, Long classScheduleId) {
       Account account = accountRepository.findById(accountId).orElse(null);
       ClassSchedule schedule = scheduleRepository.findById(classScheduleId).orElse(null);
       if (account == null || schedule == null) {
           return false;
       }
       return this.mergeAccountAndClassSchedule(account, schedule);
    }

    private boolean mergeAccountAndClassSchedule(Account account, ClassSchedule schedule) {
        try {
            List<Account> accounts = Optional.ofNullable(schedule.getAccounts())
                    .orElse(new ArrayList<>());
            if (accounts.contains(account)) {
                return true;
            }
            accounts.add(account);
            schedule.setAccounts(accounts);
            scheduleRepository.save(schedule);
            return true;
        }catch (Exception e) {
            return false;
        }
    }


    private ClassSchedule findClassSchedule(String fullName, String location, String teacher, String courseNum, Integer day, String week) {
        Course course = courseRepository.findCourseByFullName(fullName);
        ClassRoom classRoom = classRoomRepository.findClassRoomByLocation(location);
        if (course == null || classRoom == null) {
           return null;
        }

        return scheduleRepository.getSchoolScheduleByCourseAndClassRoomAndTeacherAndCourseNumAndDayAndWeek(course, classRoom, teacher, courseNum, day, week);
    }



    @Autowired
    CourseTimeUtil timeUtil;

    @Override
    @Transactional
    public List<CourseTableVo> parseCourseJson(Integer accountId, String courseJson) {
        List<JSONObject> kbList = this.parseCourseJSON(courseJson);
        if (kbList == null) {
            return null;
        }

        for (JSONObject kb : kbList) {
            String name = kb.getString("kcmc");
            String fullName = kb.getString("jxbmc");
            Double credit = kb.getDouble("xf");
            String category = kb.getString("kclb");
            String nature = kb.getString("kcxz");

            String building = kb.getString("lh");
            String location = kb.getString("cdmc");
            String campus = kb.getString("xqmc");

            String teacher = kb.getString("xm");
            String courseNum = kb.getString("jc");
            Integer day = timeUtil.parseDay(kb.getString("xqjmc"));
            String week = kb.getString("zcd");


            this.addCourse(name, fullName, credit, category, nature);
            this.addClassRoom(location, building, campus);
            ClassSchedule schedule =  this.addClassSchedule(fullName, location, teacher, courseNum, day, week);

            this.mergeAccountAndClassSchedule(accountId, schedule.getId());
        }

        return this.getSchedulesForAccount(accountId);
    }

    @Override
    @Transactional
    public List<CourseTableVo> getSchedulesForAccount(Integer accountId) {
        return this.parseSchedulesToVo(this.getSchedulesFormAccount(accountId));
    }

    @Override
    @Transactional
    public List<CourseTableVo> parseSchedulesToVo(List<ClassSchedule> schedules) {
        return schedules.stream().map(schedule -> {
            CourseTableVo vo = new CourseTableVo();
            ClassRoom classRoom = schedule.getClassRoom();
            Course course = schedule.getCourse();

            vo.setName(course.getName());
            vo.setFullName(course.getFullName());
            vo.setCredit(course.getCredit());
            vo.setCampus(classRoom.getBuilding().getCampus());
            vo.setLocation(classRoom.getLocation());
            vo.setTeacher(schedule.getTeacher());
            vo.setCourseNum(schedule.getCourseNum());
            vo.setDay(schedule.getDay());
            vo.setWeek(schedule.getWeek());

            return vo;
        }).toList();
    }

    private List<ClassSchedule> getSchedulesFormAccount(Integer accountId) {
        return scheduleRepository.getSchoolSchedulesByAccount_Id(accountId);
    }

    private List<JSONObject> parseCourseJSON(String courseJson) {
        try {
            if (courseJson.startsWith("[")) {
                return JSONArray.parseArray(courseJson).toList(JSONObject.class);
            }else {
                return JSONObject.parse(courseJson).getJSONArray("kbList").toList(JSONObject.class);
            }
        }catch (Exception e) {
            return null;
        }
    }
}
