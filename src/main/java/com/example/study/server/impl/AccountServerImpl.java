package com.example.study.server.impl;

import com.example.study.pojo.dto.auth.Account;
import com.example.study.repository.AccountRepository;
import com.example.study.server.AccountServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountServerImpl implements AccountServer {

    @Autowired
    AccountRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = repository.findAccountByEmailOrName(username);
        if (account == null) {
            throw new UsernameNotFoundException("用户名或邮箱错误");
        }
        return User
                .withUsername(account.getName())
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }


}
