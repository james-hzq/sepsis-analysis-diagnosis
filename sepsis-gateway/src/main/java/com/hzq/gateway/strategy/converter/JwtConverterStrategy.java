package com.hzq.gateway.strategy.converter;

import com.hzq.gateway.constant.TokenType;
import com.hzq.redis.cache.RedisCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author gc
 * @class com.hzq.gateway.service JwtAuthenticationStrategy
 * @date 2024/11/14 17:03
 * @description jwt 验证的策略
 */
@Slf4j
@Component
@RequiredArgsConstructor
public final class JwtConverterStrategy implements TokenConverterStrategy {

    private final RedisCache redisCache;

    @Override
    public TokenType getTokenType() {
        return TokenType.SYSTEM_LOGIN_JWT_TOKEN;
    }

    @Override
    public Mono<Authentication> convert(String token) {
        String realToken = trimTokenPrefix(getTokenType(), token);
        // TODO JWT验签
        return null;
    }
}
