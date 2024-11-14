package com.hzq.gateway.strategy;

import com.hzq.gateway.constant.TokenType;
import com.hzq.redis.cache.RedisCache;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author gc
 * @class com.hzq.gateway.strategy AccessTokenAuthenticationStrategy
 * @date 2024/11/14 17:10
 * @description access_token 验证的策略
 */
@Component
@RequiredArgsConstructor
public class AccessTokenAuthenticationStrategy implements TokenAuthenticationStrategy {

    private final RedisCache redisCache;

    @Override
    public TokenType getTokenType() {
        return TokenType.OAUTH2_LOGIN_ACCESS_TOKEN;
    }

    @Override
    public Mono<Authentication> authenticate(String token) {
        String realToken = trimTokenPrefix(getTokenType(), token);
        // TODO ACCESS_TOKEN 验签
        return null;
    }
}
