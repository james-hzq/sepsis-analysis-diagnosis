package com.hzq.gateway.strategy;

import com.hzq.gateway.constant.TokenType;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

/**
 * @author gc
 * @class com.hzq.gateway.service JwtAuthenticationStrategy
 * @date 2024/11/14 17:03
 * @description jwt 验证的策略
 */
public class JwtAuthenticationStrategy implements TokenAuthenticationStrategy {

    private static final TokenType TOKEN_TYPE = TokenType.SYSTEM_LOGIN_JWT_TOKEN;

    @Override
    public Mono<Authentication> authenticate(String token) {
        String realToken = trimTokenPrefix(TOKEN_TYPE, token);
        return null;
    }
}
