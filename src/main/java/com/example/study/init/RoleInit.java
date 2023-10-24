package com.example.study.init;

import com.example.study.constant.UserConst;
import com.example.study.pojo.dto.auth.Role;
import com.example.study.repository.auth.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: maifuwa
 * @date: 2023/10/24 下午5:02
 * @description: 初始化角色表，免得忘记添加
 */
@Component
public class RoleInit {

    @Autowired
    RoleRepository roleRepository;

    // @PostConstruct
    void initRole() {
        Role role = new Role();
        role.setRoleName(UserConst.ROLE_DEFAULT);
        Role role1 = new Role();
        role1.setRoleName(UserConst.ROLE_VIP);
        Role role2 = new Role();
        role2.setRoleName(UserConst.ROLE_MODERATOR);
        Role role3 = new Role();
        role3.setRoleName(UserConst.ROLE_ADMINISTRATOR);
        roleRepository.saveAll(List.of(role, role1, role2, role3));
    }
}
