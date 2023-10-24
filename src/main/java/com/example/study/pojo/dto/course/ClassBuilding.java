package com.example.study.pojo.dto.course;

import jakarta.persistence.*;
import lombok.Data;


/**
 * @author: maifuwa
 * @date: 2023/10/10 上午9:49
 * @description: 教学楼表
 */
@Data
@Entity
public class ClassBuilding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(length = 20)
    String campus;

    @Column(length = 20)
    String building;

}
