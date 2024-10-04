package com.hzq.auth.service;

import com.hzq.auth.domain.LoginUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hua
 * @className com.hzq.auth.service TokenService
 * @date 2024/10/4 16:04
 * @description TODO
 */
@Service
@AllArgsConstructor
public class TokenGeneratorService {
    private final JwtEncoder jwtEncoder;

    public String generateAccessToken(LoginUser loginUser) {
        // 获取当前时间和过期时间
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(3600); // 设置 Token 有效期为 2 小时

        // 创建 JWT 负载
        JwtClaimsSet.Builder claimsBuilder = createJwtClaimsSet(loginUser, issuedAt, expiresAt);

        // 生成 JWT
        Jwt jwt = this.jwtEncoder.encode(JwtEncoderParameters.from(claimsBuilder.build()));

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
    private JwtClaimsSet.Builder createJwtClaimsSet(LoginUser loginUser, Instant issuedAt, Instant expiresAt) {
        return JwtClaimsSet.builder()
                // 设置主题为用户名
                .subject(loginUser.getUsername())
                // 设置用户 ID
                .claim("userId", loginUser.getUserId())
                // 设置签发时间
                .issuedAt(issuedAt)
                // 设置过期时间
                .expiresAt(expiresAt);
//                // 设置用户角色
//                .claim("roles", getUserRoles(loginUser))
//                // 设置用户权限
//                .claim("perms", loginUser.getPerms());
    }

    /**
     * @author hua
     * @date 2024/10/4 16:51
     * @param loginUser 系统用户信息
     * @return java.util.Set<java.lang.String>
     * @apiNote TODO
     **/
    private Set<String> getUserRoles(LoginUser loginUser) {
        return loginUser.getAuthorities().stream()
                // 将 GrantedAuthority 转换为角色字符串
                .map(GrantedAuthority::getAuthority)
                // 收集为 Set
                .collect(Collectors.toSet());
    }
}
