package com.hzq.gateway.strategy;

import com.hzq.gateway.constant.TokenType;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

/**
 * @author gc
 * @class com.hzq.gateway.strategy AccessTokenAuthenticationStrategy
 * @date 2024/11/14 17:10
 * @description access_token 验证的策略
 */
public class AccessTokenAuthenticationStrategy implements TokenAuthenticationStrategy {

    private static final TokenType TOKEN_TYPE = TokenType.OAUTH2_LOGIN_ACCESS_TOKEN;

    @Override
    public Mono<Authentication> authenticate(String token) {
        String realToken = trimTokenPrefix(TOKEN_TYPE, token);
        return null;
    }
}
