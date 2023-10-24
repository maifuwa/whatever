package com.example.study.pojo.dto.course;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @author: maifuwa
 * @date: 2023/10/10 上午9:59
 * @description: 学科平台表
 */
@Data
@Entity
public class SubjectPlatform {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    // 课程类别(如：专业平台课程)
    @Column(length = 20)
    String category;

    // 课程性质(如：选修)
    @Column(length = 20)
    String nature;
}
