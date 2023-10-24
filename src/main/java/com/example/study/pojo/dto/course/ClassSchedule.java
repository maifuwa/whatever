package com.example.study.pojo.dto.course;

import com.example.study.pojo.dto.auth.Account;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

/**
 * @author: maifuwa
 * @date: 2023/10/9 下午3:24
 * @description: 学生课程表
 */
@Data
@Entity
public class ClassSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // 先不计较学期的事情，大不了一年一删库
//    @CreatedDate
//    Instant createTime;

    @ManyToOne(fetch = FetchType.LAZY)
    Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    ClassRoom classRoom;

    String teacher;

    String courseNum;

    Integer day;

    String week;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    List<Account> accounts;
}
