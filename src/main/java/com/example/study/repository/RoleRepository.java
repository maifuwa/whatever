package com.example.study.repository;

import com.example.study.pojo.dto.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: maifuwa
 * @date: 2023/9/28 下午6:32
 * @description: 用户权限的持久化接口
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {

    List<Role> findRolesByRoleNameIn(String... name);

}
