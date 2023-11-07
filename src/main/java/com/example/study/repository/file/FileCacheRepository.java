package com.example.study.repository.file;

import com.example.study.pojo.dto.file.FileCache;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileCacheRepository extends JpaRepository<FileCache, Integer> {

    boolean existsFileCacheByFileMd5(String fileMd5);

    FileCache findFileCacheByFileMd5(String fileMd5);
}
