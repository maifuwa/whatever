package com.example.study.pojo.dto.file;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class FileCache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(unique = true, length = 50)
    String fileMd5;

    @Column(length = 20)
    String filename;

    public FileCache(String fileMd5, String filename) {
        this.fileMd5 = fileMd5;
        this.filename = filename;
    }
}
