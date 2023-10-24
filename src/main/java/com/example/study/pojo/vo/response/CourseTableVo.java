package com.example.study.pojo.vo.response;

import lombok.Data;

/**
 * @author maifuwa
 * @date 2023/10/12 上午10:42
 * @description 课程表vo类
 */
@Data
public class CourseTableVo {

    // 课程名称
    String name;
    // 课程详细名称
    String fullName;
    // 课程所占学分
    Double credit;
    // 课程占的课数
    String courseNum;
    // 星期几
    Integer day;
    // 上几周
    String week;
    // 校园
    String campus;
    // 教室具体位置
    String location;
    // 教师
    String teacher;
}
