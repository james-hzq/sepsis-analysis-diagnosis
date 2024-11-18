package com.hzq.gateway.strategy.authentication;

import com.hzq.gateway.constant.AuthenticationType;
import com.hzq.gateway.exception.TokenAuthenticationException;
import com.hzq.security.authentication.AccessTokenAuthentication;
import com.hzq.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;

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
    public Mono<Void> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .cast(AccessTokenAuthentication.class)
                .flatMap(this::validateToken)
                .doOnSuccess(auth -> {
                    auth.setAuthenticated(true);
                    SecurityUtils.setAuthentication(authentication);
                })
                .doOnError(error -> log.error("Token认证失败: {}", error.getMessage()))
                .then();
    }

    private Mono<AccessTokenAuthentication> validateToken(AccessTokenAuthentication accessTokenAuthentication) {
        Instant now = Instant.now();
        Instant issuedAt = accessTokenAuthentication.getIssuedAt();
        Instant expiresAt = accessTokenAuthentication.getExpiresAt();

        // 空值检查
        if (issuedAt == null || expiresAt == null) {
            return Mono.error(new TokenAuthenticationException("Token时间戳无效"));
        }

        // 检查是否完全过期
        if (now.isAfter(expiresAt)) {
            return Mono.error(new TokenAuthenticationException("access_token 已过期"));
        }

        return Mono.just(accessTokenAuthentication);
    }
}
