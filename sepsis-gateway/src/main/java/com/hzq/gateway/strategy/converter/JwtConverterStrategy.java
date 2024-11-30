package com.hzq.gateway.strategy.converter;

import com.hzq.gateway.constant.TokenType;
import com.hzq.gateway.exception.JwtAuthenticationException;
import com.hzq.gateway.user.JwtAuthentication;
import com.hzq.jackson.util.JacksonUtils;
import com.hzq.redis.cache.RedisCache;
import com.hzq.security.authentication.LoginUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

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

    private final ReactiveJwtDecoder reactiveJwtDecoder;
    private final RedisCache redisCache;

    @Override
    public TokenType getTokenType() {
        return TokenType.SYSTEM_LOGIN_JWT_TOKEN;
    }

    @Override
    public Mono<Authentication> convert(String token) {
        return reactiveJwtDecoder.decode(token.substring(getTokenType().getPrefix().length()))
                // 获取 JWT 的 subject（用户名）
                .map(Jwt::getSubject)
                .flatMap(username -> {
                    // 从 Redis 中获取登录信息（LoginUserInfo）
                    String redisKey = getTokenType().getPrefix() + username;
                    String loginInfoStr = Optional.ofNullable((String) redisCache.getCacheObject(redisKey))
                            .orElseThrow(() -> new JwtAuthenticationException("The jwt is invalid or has expired"));

                    return Mono.just(loginInfoStr)
                            .map(infoStr -> JacksonUtils.parseObject(infoStr, LoginUserInfo.class))
                            .map(loginUserInfo -> new JwtAuthentication()
                                    .setPrincipal(loginUserInfo.getUsername())
                                    .setRoles(loginUserInfo.getRoles())
                                    .setIssuedAt(loginUserInfo.getIssuedAt())
                                    .setExpiresAt(loginUserInfo.getExpiresAt()));
                });
    }
}
