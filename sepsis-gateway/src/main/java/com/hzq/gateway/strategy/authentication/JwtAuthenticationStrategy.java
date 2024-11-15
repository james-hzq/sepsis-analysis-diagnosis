package com.hzq.gateway.strategy.authentication;

import com.hzq.gateway.constant.AuthenticationType;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

/**
 * @author gc
 * @class com.hzq.gateway.strategy.authentication JwtAuthenticationStrategy
 * @date 2024/11/15 12:14
 * @description TODO
 */
public class JwtAuthenticationStrategy implements TokenAuthenticationStrategy {

    @Override
    public AuthenticationType getAuthenticationType() {
        return AuthenticationType.SYSTEM_USERNAME_PASSWORD_AUTHENTICATION;
    }

    @Override
    public Mono<Void> authenticate(Authentication authentication) {
        return null;
    }
}
