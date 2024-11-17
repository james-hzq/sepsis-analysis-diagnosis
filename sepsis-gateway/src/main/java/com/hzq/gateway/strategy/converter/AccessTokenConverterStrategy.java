package com.hzq.gateway.strategy.converter;

import com.hzq.gateway.constant.TokenType;
import com.hzq.gateway.exception.TokenAuthenticationException;
import com.hzq.redis.cache.RedisCache;
import com.hzq.security.authentication.AccessTokenAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author gc
 * @class com.hzq.gateway.strategy AccessTokenAuthenticationStrategy
 * @date 2024/11/14 17:10
 * @description access_token 验证的策略
 */
@Component
@RequiredArgsConstructor
public final class AccessTokenConverterStrategy implements TokenConverterStrategy {

    private final RedisCache redisCache;

    @Override
    public TokenType getTokenType() {
        return TokenType.OAUTH2_LOGIN_ACCESS_TOKEN;
    }

    @Override
    public Mono<Authentication> convert(String token) {
        String realToken = trimTokenPrefix(getTokenType(), token);
        AccessTokenAuthentication accessTokenAuthentication = Optional.ofNullable((AccessTokenAuthentication) redisCache.getCacheObject(realToken))
                .orElseThrow(() -> new TokenAuthenticationException("token 无效"));
        return Mono.just(accessTokenAuthentication);
    }
}
