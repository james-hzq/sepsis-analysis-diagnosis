package com.hzq.gateway.strategy.authentication;

import com.hzq.gateway.constant.AuthenticationType;
import com.hzq.gateway.exception.TokenAuthenticationException;
import com.hzq.gateway.strategy.converter.AccessTokenAuthentication;
import com.hzq.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.time.Instant;
import java.util.function.Function;

/**
 * @author gc
 * @class com.hzq.gateway.strategy.authentication AccessTokenAuthenticationStrategy
 * @date 2024/11/15 11:44
 * @description TODO
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AccessTokenAuthenticationStrategy implements TokenAuthenticationStrategy {

    @Override
    public AuthenticationType getAuthenticationType() {
        return AuthenticationType.OAUTH2_ACCESS_TOKEN_AUTHENTICATION;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .flatMap(this::validateToken) // 验证 token 并返回认证信息
                .map(auth -> {
                    auth.setAuthenticated(true); // 标记认证成功
                    return auth;
                })
                .flatMap(auth ->
                        Mono.just(auth)
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth))
                )
                .doOnSuccess(auth -> log.info("Authentication complete: {}", auth));
    }

    private Mono<Authentication> validateToken(Authentication authentication) {
        AccessTokenAuthentication accessTokenAuthentication = (AccessTokenAuthentication) authentication;
        Instant now = Instant.now();
        Instant issuedAt = accessTokenAuthentication.getIssuedAt();
        Instant expiresAt = accessTokenAuthentication.getExpiresAt();

        // 空值检查
        if (issuedAt == null || expiresAt == null) {
            return Mono.error(new TokenAuthenticationException("access_token 时间戳无效"));
        }

        // 检查是否完全过期
        if (now.isAfter(expiresAt)) {
            return Mono.error(new TokenAuthenticationException("access_token 已过期"));
        }

        return Mono.just(accessTokenAuthentication);
    }
}
