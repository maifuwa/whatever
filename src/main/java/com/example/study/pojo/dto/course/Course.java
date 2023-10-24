package com.example.study.pojo.dto.course;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @author: maifuwa
 * @date: 2023/10/8 下午4:37
 * @description: 课程表
 */
@Data
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 50)
    String name;

    @Column(length = 50, unique = true)
    String fullName;

    Double credit;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    SubjectPlatform subjectPlatform;
}
