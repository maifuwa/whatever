package com.example.study.repository.course;

import com.example.study.pojo.dto.course.ClassRoom;
import com.example.study.pojo.dto.course.Course;
import com.example.study.pojo.dto.course.ClassSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author maifuwa
 * @date 2023/10/13 下午3:43
 * @description 课程表的持久化接口
 */
public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, Long> {
    ClassSchedule getSchoolScheduleByCourseAndClassRoomAndTeacherAndCourseNumAndDayAndWeek(Course course, ClassRoom classRoom, String teacher, String courseNum, Integer day, String week);

    @Query(nativeQuery = true, value = "select * from school_schedule  where id in (" +
            "select school_schedule_id from school_schedule_users where account_id = :accountId)")
    List<ClassSchedule> getSchoolSchedulesByAccount_Id(@Param("accountId") Integer accountId);

    List<ClassSchedule> getSchoolSchedulesByCourseNumLike(String like);
}
