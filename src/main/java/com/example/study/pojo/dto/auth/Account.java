package com.example.study.pojo.dto.auth;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false, unique = true, length = 50)
    String name;
    @Column(nullable = false, length = 255)
    String password;

    @Column(nullable = false, unique = true, length = 50)
    String email;

    @Temporal(TemporalType.DATE)
    @CreatedDate
    Instant createDate = Instant.now();

    String role;
}
