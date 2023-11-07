package com.example.study.config;

import com.example.study.filter.JwtAuthenticationFilter;
import com.example.study.pojo.RestBean;
import com.example.study.pojo.dto.auth.Account;
import com.example.study.repository.auth.AccountRepository;
import com.example.study.constant.UserConst;
import com.example.study.server.AccountServer;
import com.example.study.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Resource
    AccountRepository repository;
    @Resource
    AccountServer accountServer;
    @Resource
    JwtUtil jwtUtil;
    @Resource
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(conf -> conf
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .anyRequest().hasAnyAuthority(UserConst.ROLE_DEFAULT)
                )
                .formLogin(conf -> conf
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler(this::handleProcess)
                        .failureHandler(this::handleProcess)
                )
                .logout(conf -> conf
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(this::onLogoutSuccess)
                )
                .exceptionHandling(conf -> conf
                        .accessDeniedHandler(this::handleProcess)
                        .authenticationEntryPoint(this::handleProcess)
                )
                .sessionManagement(conf -> conf
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)   // 暂时关闭csrf验证。之后考虑是否开启
                .build();
    }


    private void handleProcess(HttpServletRequest request, HttpServletResponse response,
                      Object exceptionOrAuthentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();

        if (exceptionOrAuthentication instanceof AccessDeniedException exception) {
            writer.write(RestBean.forbidden(exception.getMessage()).asJSONString());
        } else if (exceptionOrAuthentication instanceof Exception exception) {
            writer.write(RestBean.unauthorized(exception.getMessage()).asJSONString());
        }else if (exceptionOrAuthentication instanceof  Authentication authentication) {
            User user = (User)authentication.getPrincipal();
            Account account = repository.findAccountByEmailOrName(user.getUsername());
            writer.write(RestBean.success(accountServer.setAccountVo(account)).asJSONString());
        }
    }

    private void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                         Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();

        if (jwtUtil.revokeToken(request.getHeader("Authorization"))) {
            writer.write(RestBean.success("退出登录成功").asJSONString());
            return;
        }
        writer.write(RestBean.unauthorized("Full authentication is required to access this resource").asJSONString());
    }

}
