package com.example.study.pojo.auth;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false, unique = true)
    String name;
    @Column(nullable = false)
    String password;
    @Column(nullable = false, unique = true)
    String email;

    @Temporal(TemporalType.DATE)
    @CreatedDate
    Date createDate = new Date();

    String role;
}
