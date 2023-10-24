package com.example.study.server;

import com.example.study.pojo.dto.auth.Account;
import com.example.study.pojo.vo.request.RegisterVo;
import com.example.study.pojo.vo.request.ResetPwdVo;
import com.example.study.pojo.vo.response.AccountVo;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author: maifuwa
 * @date: 2023/9/27 下午1:35
 * @description: 用户注册登陆服务
 */
public interface AccountServer extends UserDetailsService {

    String sendEmailVerifyCode(String type, String email, String ip, String userAgent);
    AccountVo registerAccount(String type, RegisterVo vo);
    AccountVo resetPassword(String type, ResetPwdVo vo);
    AccountVo setAccountVo(Account account);

}
