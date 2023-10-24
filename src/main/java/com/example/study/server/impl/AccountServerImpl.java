package com.example.study.server.impl;

import com.example.study.constant.MailConst;
import com.example.study.constant.UserConst;
import com.example.study.pojo.dto.auth.Account;
import com.example.study.pojo.dto.auth.Role;
import com.example.study.pojo.vo.request.RegisterVo;
import com.example.study.pojo.vo.request.ResetPwdVo;
import com.example.study.pojo.vo.response.AccountVo;
import com.example.study.repository.auth.AccountRepository;
import com.example.study.repository.auth.RoleRepository;
import com.example.study.server.AccountServer;
import com.example.study.utils.JwtUtil;
import com.example.study.utils.SimpleUtils;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: maifuwa
 * @date: 2023/9/27 下午1:35
 * @description: 用户注册登陆服务实现类
 */
@Slf4j
@Service
public class AccountServerImpl implements AccountServer {

    @Resource
    JwtUtil jwtUtil;
    @Resource
    PasswordEncoder passwordEncoder;
    @Resource
    AccountRepository accountRepository;
    @Resource
    RoleRepository roleRepository;

    @Autowired
    StringRedisTemplate redisTemplate;
    @Resource
    AmqpTemplate rabbitTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByEmailOrName(username);

        if (account == null) {
            throw new UsernameNotFoundException("用户名或邮箱错误");
        }
        return User
                .withUsername(account.getName())
                .password(account.getPassword())
                .roles(accountRepository.getAllRolesById(account.getId()).toArray(String[]::new))
                .build();
    }


    @Override
    public String sendEmailVerifyCode(String type, String email, String ip, String userAgent) {
        String key = this.canSendEmailVerifyCode(type, email);
        if (key.length() < 16) {
            return key;
        }

        int code = SimpleUtils.getVerifyCode();
        Map<String, Object> data = Map.of("type", type, "email", email, "ip", ip, "userAgent", userAgent, "code", code);
        rabbitTemplate.convertAndSend(MailConst.EMAIL_SEND_MQ, data);
        redisTemplate.opsForValue()
                .set(key, String.valueOf(code), 5, TimeUnit.MINUTES);
        return "发送成功";
    }

    private String canSendEmailVerifyCode(String type, String email) {
        String key = this.getKey(type, email);
        if (!MailConst.VERIFY_EMAIL_TYPE_REGISTER.equals(type) &&
                !accountRepository.existsAccountByEmailOrName(email, null)) {
            return "请先注册";
        } else if (this.getCode(key) != null) {
            return "验证码仍有效，请不要频繁请求";
        } else {
            return key;
        }
    }

    private String getKey(String type, String email) {
        return "verify:" + type + ":email:" + email + ":code";
    }

    private String getCode(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    @Transactional
    public AccountVo registerAccount(String type, RegisterVo vo) {
        String name = vo.getName();
        String email = vo.getEmail();
        AccountVo accountVo = new AccountVo();
        if (accountRepository.existsAccountByEmailOrName(email, name)) {
            accountVo.setToken("用户名或邮箱已被注册");
        } else if (!vo.getCode().equals(this.getCode(this.getKey(type, email)))) {
            accountVo.setToken("验证码错误");
        } else {
            Account account = new Account();
            account.setName(name);
            account.setEmail(email);
            account.setPassword(passwordEncoder.encode(vo.getPassword()));
            List<Role> roles = roleRepository.findRolesByRoleNameIn(UserConst.ROLE_DEFAULT);
            account.setRoles(roles);
            accountRepository.save(account);
            return setAccountVo(account);
        }
        return accountVo;
    }

    @Override
    public AccountVo resetPassword(String type, ResetPwdVo vo) {
        AccountVo accountVo = new AccountVo();
        if (!accountRepository.existsAccountByEmailOrName(vo.getEmail(), null)) {
            accountVo.setToken("请先注册");
        } else if (!vo.getCode().equals(this.getCode(this.getKey(type, vo.getEmail())))) {
            accountVo.setToken("验证码错误");
        } else {
            Account account = accountRepository.findAccountByEmailOrName(vo.getEmail());
            account.setPassword(passwordEncoder.encode(vo.getPassword()));
            accountRepository.save(account);
            return setAccountVo(account);
        }
        return accountVo;
    }


    public AccountVo setAccountVo(Account account) {
        AccountVo accountVo = new AccountVo();
        List<String> roles = accountRepository.getAllRolesById(account.getId());
        Instant expireTime = jwtUtil.expireTime();
        String jwt = jwtUtil.createJwt(account, roles, expireTime);

        accountVo.setName(account.getName());
        accountVo.setRoles(roles);
        accountVo.setExpireTime(expireTime);
        accountVo.setToken(jwt);
        return accountVo;
    }

}
