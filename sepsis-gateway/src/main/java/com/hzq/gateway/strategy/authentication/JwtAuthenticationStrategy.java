package com.hzq.gateway.strategy.authentication;

import com.hzq.gateway.constant.AuthenticationType;
import com.hzq.gateway.exception.JwtAuthenticationException;
import com.hzq.gateway.user.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * @author gc
 * @class com.hzq.gateway.strategy.authentication JwtAuthenticationStrategy
 * @date 2024/11/15 12:14
 * @description JWT 认证策略
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationStrategy implements TokenAuthenticationStrategy {

    @Override
    public AuthenticationType getAuthenticationType() {
        return AuthenticationType.SYSTEM_USERNAME_PASSWORD_AUTHENTICATION;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.defer(() -> {
            JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication;

            if (isTokenNotValid(jwtAuthentication.getIssuedAt(), jwtAuthentication.getExpiresAt())) {
                return Mono.error(new JwtAuthenticationException("jwt is invalid"));
            }

            // 设置认证状态并返回
            jwtAuthentication.setAuthenticated(true);
            return Mono.just(jwtAuthentication);
        });
    }
}
