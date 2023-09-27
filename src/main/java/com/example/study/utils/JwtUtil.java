package com.example.study.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.study.pojo.dto.auth.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author: maifuwa
 * @date: 2023/9/26 下午9:15
 * @description: 创建、解析、处理 jwt令牌
 */

@Slf4j
@Component
public class JwtUtil {


    @Value("${Spring.security.jwt.key}")
    private String key;

    @Value("${Spring.security.jwt.expire}")
    private long expire;


    /**
     * 使用配置文件，计算令牌过期时间
     * @return 令牌过期时间
     */
    public Instant expireTime() {
        return ZonedDateTime.now().plusHours(expire).toInstant();
    }


    /**
     * 使用传入的用户信息，创建jwt令牌
     * @param account 用户信息
     * @param roles 用户身份
     * @param expireTime 令牌过期时间
     * @return 令牌
     */
    public String createJwt(Account account, List<String> roles, Instant expireTime) {
        Algorithm algorithm = Algorithm.HMAC256(key);
        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("id", account.getId())
                .withClaim("username", account.getName())
                .withClaim("roles", roles)
                .withExpiresAt(expireTime)
                .withIssuedAt(Instant.now())
                .sign(algorithm);

    }

    /**
     * 判断传入字符是不是 token
     * @param authorization 需判断字符
     * @return token
     */
    private String parseAuthorization(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }
        return authorization.substring(7);
    }

    /**
     * 解析authorization，创建一个包含token的jwt解析器
     * @param authorization 请求头携带的令牌
     * @return DecodedJWT
     */
    public DecodedJWT resolveJwt(String authorization) {
        String token = this.parseAuthorization(authorization);
        if (token == null) {
            return null;
        }

        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            DecodedJWT decodedJWT= jwtVerifier.verify(token);
            Map<String, Claim> claimMas = decodedJWT.getClaims();
            return Instant.now().isAfter(claimMas.get("exp").asInstant()) ? null : decodedJWT;
        } catch (JWTVerificationException e) {
            log.info("创建jwt解析器失败");
            return null;
        }
    }

    /**
     * 从令牌中解析出Spring Security 框架的UserDetails类
     * @param decodedJWT 包含token的jwt解析器
     * @return UserDetails
     */
    public UserDetails parseToUser(DecodedJWT decodedJWT) {
        Map<String, Claim> claimMas = decodedJWT.getClaims();
        return User
                .withUsername(claimMas.get("username").asString())
                .authorities(claimMas.get("roles").asArray(String.class))
                .password("conNotPassNull")
                .build();
    }

    public Integer parseToUserId(DecodedJWT decodedJWT) {
        return decodedJWT.getClaims().get("id").asInt();
    }
}
