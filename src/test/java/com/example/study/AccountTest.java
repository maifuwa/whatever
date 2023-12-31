package com.example.study;


import com.example.study.constant.UserConst;
import com.example.study.pojo.dto.auth.Account;
import com.example.study.pojo.dto.auth.Role;
import com.example.study.repository.auth.AccountRepository;
import com.example.study.repository.auth.RoleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootTest
public class AccountTest {

    @Autowired
    AccountRepository repository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Test
    public void addRoles() {
        Role role = new Role();
        role.setRoleName(UserConst.ROLE_DEFAULT);
        Role role1 = new Role();
        role1.setRoleName(UserConst.ROLE_VIP);
        Role role2 = new Role();
        role2.setRoleName(UserConst.ROLE_MODERATOR);
        Role role3 = new Role();
        role3.setRoleName(UserConst.ROLE_ADMINISTRATOR);
        System.out.println(roleRepository.saveAll(List.of(role, role1, role2, role3)));
    }

    @Test
    void addAccount() {
        Account account = new Account();
        account.setName("张三");
        account.setEmail("46546564565@qq.com");
        account.setPassword(encoder.encode("123456789"));

        System.out.println(repository.save(account));
    }

    @Test
    void changeRoles() {
        Account account = repository.findById(1).orElse(new Account());
        Role role = new Role();
        role.setId(1);
        account.setRoles(List.of(role));
        System.out.println(repository.save(account));
    }

    @Test
    @Transactional
    void addNewAccount() {
        Account account = new Account();
        account.setName("李四");
        account.setEmail("bezzuq@mailto.plus");
        account.setPassword(encoder.encode("123456789"));

        List<Role> roles = roleRepository.findRolesByRoleNameIn(UserConst.ROLE_DEFAULT, UserConst.ROLE_VIP);
        account.setRoles(roles);
        System.out.println(repository.save(account));
    }

    @Test
    void getRoles() {
        System.out.println(repository.getAllRolesById(1));
    }
}
