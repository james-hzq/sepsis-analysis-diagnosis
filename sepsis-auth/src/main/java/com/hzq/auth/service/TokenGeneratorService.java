package com.hzq.auth.service;

import com.hzq.auth.domain.LoginUser;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * @author hua
 * @className com.hzq.auth.service TokenService
 * @date 2024/10/4 16:04
 * @description TODO
 */
@Service
@AllArgsConstructor
public class TokenGeneratorService {
    // JWT默认的有效时间（单位：s）
    private static final int DEFAULT_EXPIRE_INTERVAL = 3600;
    // 签署 JWS 或 加密 JWE 的接口
    private final JwtEncoder jwtEncoder;

    public String generateAccessToken(LoginUser loginUser) {
        // 获取当前时间和过期时间
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(DEFAULT_EXPIRE_INTERVAL);
        // 创建 JWT 负载
        JwtClaimsSet jwtClaimsSet = createJwtClaimsSet(loginUser, issuedAt, expiresAt);
        // 生成 JWT
        Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet));
        // 返回生成的 JWT 字符串
        return jwt.getTokenValue();
    }

    /**
     * @author hua
     * @date 2024/10/4 16:52
     * @param loginUser 登录用户信息
     * @param issuedAt  Token 签发时间
     * @param expiresAt Token 过期时间
     * @return org.springframework.security.oauth2.jwt.JwtClaimsSet.Builder
     * @apiNote 创建 JWT 负载
     **/
    private JwtClaimsSet createJwtClaimsSet(LoginUser loginUser, Instant issuedAt, Instant expiresAt) {
        return JwtClaimsSet.builder()
                // 设置主题为用户名
                .subject(loginUser.getUsername())
                // 设置签发时间
                .issuedAt(issuedAt)
                // 设置过期时间
                .expiresAt(expiresAt)
                // 设置用户 ID
                .claim("userId", loginUser.getUserId())
                // 设置用户名
                .claim("username", loginUser.getUsername())
                // 设置用户所属角色
                .claim("roles", loginUser.getRoles())
                // 设置用户拥有权限
//                .claim("perms", loginUser.getPerms())
                .build();
    }
}
