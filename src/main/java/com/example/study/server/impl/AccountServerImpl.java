package com.example.study.server.impl;

import com.example.study.constant.MailConst;
import com.example.study.constant.UserConst;
import com.example.study.pojo.dto.auth.Account;
import com.example.study.pojo.dto.auth.Role;
import com.example.study.pojo.vo.request.RegisterVo;
import com.example.study.pojo.vo.response.AccountVo;
import com.example.study.repository.AccountRepository;
import com.example.study.repository.RoleRepository;
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
        String key = this.getKey(type, email);
        if (isEmailTimeOut(key)) {
            return "验证码仍有效，请不要频繁请求";
        }
        int code = SimpleUtils.getVerifyCode();
        Map<String, Object> data = Map.of("type", type, "email", email, "ip", ip, "userAgent", userAgent, "code", code);
        rabbitTemplate.convertAndSend(MailConst.EMAIL_SEND_MQ, data);
        redisTemplate.opsForValue()
                .set(key, String.valueOf(code), 5, TimeUnit.MINUTES);
        return "发送成功";
    }

    private String getKey(String type, String email) {
        if ("register".equals(type)) {
            return MailConst.VERIFY_REGISTER_EMAIL_DATA + email;
        }else if ("reset".equals(type)) {
            return MailConst.VERIFY_RESET_EMAIL_DATA + email;
        }
        return "";
    }
    private boolean isEmailTimeOut(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    @Transactional
    public AccountVo registerAccount(RegisterVo vo) {
        AccountVo accountVo = new AccountVo();

        if (accountRepository.existsAccountByEmailOrName(vo.getEmail(), vo.getUsername())) {
           accountVo.setToken("用户名或邮箱已被注册");
        }else if (vo.getCode() == null){
            accountVo.setToken("请先获取验证码");
        } else if (!vo.getCode().equals(redisTemplate.opsForValue().get(this.getKey("register", vo.getEmail())))) {
            accountVo.setToken("验证码错误");
        }else {
            Account account = new Account();
            account.setName(vo.getUsername());
            account.setEmail(vo.getEmail());
            account.setPassword(passwordEncoder.encode(vo.getPassword()));
            List<Role> roles = roleRepository.findRolesByRoleNameIn(UserConst.ROLE_DEFAULT);
            account.setRoles(roles);
            accountRepository.save(account);

            List<String> rolesName = roles.stream().map(Role::getRoleName).toList();
            Instant expireTime = jwtUtil.expireTime();
            String jwt = jwtUtil.createJwt(account, rolesName, expireTime);

            accountVo.setName(account.getName());
            accountVo.setRoles(rolesName);
            accountVo.setExpireTime(expireTime);
            accountVo.setToken(jwt);
        }
        return accountVo;
    }


}
