package com.example.study.repository;

import com.example.study.pojo.dto.auth.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Query("from Account where name = :like or email = :like")
    public Account findAccountByEmailOrName(@Param("like") String like);
}
