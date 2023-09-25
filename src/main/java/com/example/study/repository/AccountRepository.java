package com.example.study.repository;

import com.example.study.pojo.auth.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {

}
