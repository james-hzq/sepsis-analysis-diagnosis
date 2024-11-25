package com.hzq.gateway.strategy.converter;

import com.hzq.gateway.constant.TokenType;
import com.hzq.jackson.util.JacksonUtils;
import com.hzq.redis.cache.RedisCache;
import com.hzq.security.authentication.LoginUserInfo;
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
public final class AccessTokenConverterStrategy implements TokenConverterStrategy {

    private final RedisCache redisCache;

    @Override
    public TokenType getTokenType() {
        return TokenType.OAUTH2_LOGIN_ACCESS_TOKEN;
    }

    @Override
    public Mono<Authentication> convert(String token) {
        // 从 redis 中获取用户信息
        LoginUserInfo loginUserInfo = JacksonUtils.parseObject((String) redisCache.getCacheObject(token), LoginUserInfo.class);
        // 使用用户信息生成 authentication 认证对象，用于后续认证
        Authentication authentication = new AccessTokenAuthentication()
                .setAccessToken(loginUserInfo.getToken())
                .setPrincipal(loginUserInfo.getUsername())
                .setRoles(loginUserInfo.getRoles())
                .setIssuedAt(loginUserInfo.getIssuedAt())
                .setExpiresAt(loginUserInfo.getExpiresAt());

        return Mono.just(authentication);
    }
}
