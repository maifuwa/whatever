package com.example.study.repository.course;

import com.example.study.pojo.dto.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author maifuwa
 * @date 2023/10/13 下午3:43
 * @description 课程的持久化接口
 */
public interface CourseRepository extends JpaRepository<Course, Long> {

    Course findCourseByFullName(String fullName);

    boolean existsByFullName(String fullName);

}
