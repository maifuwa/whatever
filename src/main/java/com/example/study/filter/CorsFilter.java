package com.example.study.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author: maifuwa
 * @date: 2023/11/3 下午11:09
 * @description:
 */
@Slf4j
@Component
@Order(-105)
public class CorsFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if ( request.getHeader("Origin") == null || !request.getHeader("Origin").contains("localhost")) {
            chain.doFilter(request, response);
            return;
        }

        response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, X-Custom-Header, Authorization");

        chain.doFilter(request, response);
    }
}
