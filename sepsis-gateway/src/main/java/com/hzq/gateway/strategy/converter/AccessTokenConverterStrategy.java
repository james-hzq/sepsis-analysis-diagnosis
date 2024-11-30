package com.hzq.gateway.strategy.converter;

import com.hzq.gateway.user.AccessTokenAuthentication;
import com.hzq.gateway.constant.TokenType;
import com.hzq.gateway.exception.AccessTokenAuthenticationException;
import com.hzq.jackson.util.JacksonUtils;
import com.hzq.redis.cache.RedisCache;
import com.hzq.security.authentication.LoginUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        // Mono.defer 是一个懒加载的工厂方法，只有在 Mono 被订阅时，defer 内部的逻辑才会被执行。
        return Mono.defer(() -> {
            // redisCache.getCacheObject(token) 的调用是延迟到订阅时才发生。
            String loginInfoStr = Optional.ofNullable((String) redisCache.getCacheObject(token))
                    .orElseThrow(() -> new AccessTokenAuthenticationException("The token is invalid or has expired"));

            return Mono.just(loginInfoStr)
                    .map(infoStr -> JacksonUtils.parseObject(infoStr, LoginUserInfo.class))
                    .map(loginUserInfo -> new AccessTokenAuthentication()
                            .setPrincipal(loginUserInfo.getUsername())
                            .setRoles(loginUserInfo.getRoles())
                            .setIssuedAt(loginUserInfo.getIssuedAt())
                            .setExpiresAt(loginUserInfo.getExpiresAt())
                    );
        });
    }
}
