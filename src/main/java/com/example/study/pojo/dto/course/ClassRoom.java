package com.example.study.pojo.dto.course;

import jakarta.persistence.*;
import lombok.Data;


/**
 * @author: maifuwa
 * @date: 2023/10/8 上午11:13
 * @description: 教室表
 */
@Data
@Entity
public class ClassRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 50, unique = true)
    String location;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    ClassBuilding building;
}
