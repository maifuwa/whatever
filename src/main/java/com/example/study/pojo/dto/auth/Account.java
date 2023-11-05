package com.example.study.pojo.dto.auth;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;

/**
 * @author: maifuwa
 * @date: 2023/9/27 下午1:35
 * @description: 用户表
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false, unique = true, length = 50)
    String name;
    @Column(nullable = false)
    String password;

    @Column(nullable = false, unique = true, length = 50)
    String email;

    @CreatedDate
    Instant createdDate;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    List<Role> roles;

    @OneToOne(cascade = {CascadeType.PERSIST}, orphanRemoval = true)
    AccountDetail detail;

    public long getQQNum() {
        String qqNum = this.email.split("@")[0];
        return Long.getLong(qqNum);
    }
}
