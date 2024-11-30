package com.hzq.auth.config.token;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Optional;

/**
 * @author hua
 * @className com.hzq.auth.config.token CustomJwtParser
 * @date 2024/11/30 15:53
 * @description JWT解析器
 */
@Configuration
@RequiredArgsConstructor
public class CustomJwtParser {

    private final JwtDecoder jwtDecoder;

    /**
     * @author hua
     * @date 2024/11/30 15:56
     * @param token JWT String
     * @return org.springframework.security.oauth2.jwt.Jwt
     * @apiNote 解析并验证 JWT 令牌
     **/
    public Jwt parseJwt(String token) {
        try {
            Assert.hasText(token, "Jwt 不得为空");
            return jwtDecoder.decode(token);
        } catch (JwtException e) {
            throw new RuntimeException("Invalid JWT token: " + e.getMessage(), e);
        }
    }

    /**
     * @author hua
     * @date 2024/11/30 16:04
     * @param jwt jwt 令牌
     * @param claimName 要提取的声明名称
     * @return T
     * @apiNote 从 JWT 中提取特定声明
     **/
    public <T> T extractClaim(Jwt jwt, String claimName) {
        return jwt.getClaim(claimName);
    }

    /**
     * @author hua
     * @date 2024/11/30 15:58
     * @param jwt Jwt对象
     * @return boolean
     * @apiNote 验证 JWT 的有效期
     **/
    public boolean isTokenExpired(@NotNull Jwt jwt) {
        try {
            return Optional.ofNullable(jwt.getExpiresAt())
                    .orElseThrow(() -> new IllegalArgumentException("JWT 没有设置 有效期"))
                    .isBefore(Instant.now());
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token: " + e.getMessage(), e);
        }
    }
}
