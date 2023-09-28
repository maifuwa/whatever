package com.example.study.repository;

import com.example.study.pojo.dto.auth.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
/**
 * @author: maifuwa
 * @date: 2023/9/28 下午6:32
 * @description: 用户的持久化接口
 */
public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Query("from Account where name = :like or email = :like")
    Account findAccountByEmailOrName(@Param("like") String like);

    @Query(value = "select role_name from role where id in (select roles_id from account_roles where account_id = :id)", nativeQuery = true)
    List<String> getAllRolesById(@Param("id") Integer id);
    Account findAccountByName(String name);
    Account findAccountByEmail(String email);
    boolean existsAccountByEmailOrName(String email, String name);
}
