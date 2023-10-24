package com.example.study.repository.course;

import com.example.study.pojo.dto.course.SubjectPlatform;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author maifuwa
 * @date 2023/10/13 下午3:43
 * @description 学科平台的持久化接口
 */
public interface SubjectPlatformRepository extends JpaRepository<SubjectPlatform, Integer> {

    SubjectPlatform findSubjectPlatformByCategoryAndNature(String category, String nature);
}
