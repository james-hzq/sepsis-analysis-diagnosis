package com.hzq.gateway.strategy.authentication;

import com.hzq.gateway.constant.AuthenticationType;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

/**
 * @author gc
 * @class com.hzq.gateway.strategy.authentication AccessTokenAuthenticationStrategy
 * @date 2024/11/15 11:44
 * @description TODO
 */
public class AccessTokenAuthenticationStrategy implements TokenAuthenticationStrategy {

    @Override
    public AuthenticationType getAuthenticationType() {
        return AuthenticationType.OAUTH2_ACCESS_TOKEN_AUTHENTICATION;
    }

    @Override
    public Mono<Void> authenticate(Authentication authentication) {
        return null;
    }
}
