package com.example.study.pojo.dto.auth;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @author: maifuwa
 * @date: 2023/9/27 上午11:05
 * @description: 用户身份表 以后可能添加更多用户身份
 */
@Data
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false, unique = true)
    String roleName;

}
