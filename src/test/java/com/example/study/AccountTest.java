package com.example.study;


import com.example.study.pojo.dto.auth.Account;
import com.example.study.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class AccountTest {

    @Autowired
    AccountRepository repository;

    @Autowired
    PasswordEncoder encoder;

    @Test
    void addAccount() {
        Account account = new Account();
        account.setName("张三");
        account.setEmail("1938311440@qq.com");
        account.setPassword(encoder.encode("12346789"));
        account.setRole("user");

        System.out.println(repository.save(account));
    }


}
