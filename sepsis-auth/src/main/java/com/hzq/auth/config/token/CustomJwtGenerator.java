package com.hzq.auth.config.token;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * @author hua
 * @className com.hzq.auth.util JWTUtils
 * @date 2024/11/30 15:23
 * @description JWT生成器
 */
@Component
@RequiredArgsConstructor
public class CustomJwtGenerator {

    private final JwtEncoder jwtEncoder;

    public String createJwt(String subject, Instant issuedAt, Instant expiresAt) {
        JwtClaimsSet jwtClaimsSet = createJwtClaimsSet(subject, issuedAt, expiresAt);
        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }

    public JwtClaimsSet createJwtClaimsSet(String subject, Instant issuedAt, Instant expiresAt) {
        return JwtClaimsSet.builder()
                .subject(subject)
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .build();
    }
}
