package com.hzq.gateway.strategy.authentication;

import com.hzq.gateway.constant.AuthenticationType;
import com.hzq.gateway.exception.TokenAuthenticationException;
import com.hzq.gateway.user.AccessTokenAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
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
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.defer(() -> {
            AccessTokenAuthentication accessTokenAuthentication = (AccessTokenAuthentication) authentication;

            Instant now = Instant.now();
            Instant issuedAt = accessTokenAuthentication.getIssuedAt();
            Instant expiresAt = accessTokenAuthentication.getExpiresAt();

            // 验证令牌时间戳
            if (issuedAt == null || expiresAt == null) {
                return Mono.error(new TokenAuthenticationException("access_token Invalid timestamp"));
            }

            // 检查令牌是否过期
            if (now.isAfter(expiresAt)) {
                return Mono.error(new TokenAuthenticationException("access_token has expired"));
            }

            // 设置认证状态并返回
            accessTokenAuthentication.setAuthenticated(true);
            return Mono.just(accessTokenAuthentication);
        });
    }
}
