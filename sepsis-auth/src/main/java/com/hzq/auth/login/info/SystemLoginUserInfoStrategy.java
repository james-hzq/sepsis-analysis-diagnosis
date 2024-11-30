package com.hzq.auth.login.info;

import com.hzq.auth.config.token.CustomJwtParser;
import com.hzq.jackson.util.JacksonUtils;
import com.hzq.redis.cache.RedisCache;
import com.hzq.security.authentication.LoginUserInfo;
import com.hzq.security.constant.SecurityConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/**
 * @author hua
 * @className com.hzq.auth.login.info SystemLoginUserInfoStrategy
 * @date 2024/11/30 15:11
 * @description 系统登录获取用户信息策略
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemLoginUserInfoStrategy implements LoginUserInfoStrategy {

    private final CustomJwtParser jwtParser;
    private final RedisCache redisCache;

    @Override
    public TokenType getTokenType() {
        return TokenType.SYSTEM_LOGIN_JWT_TOKEN;
    }

    @Override
    public LoginUserInfo getLoginUserInfo(String token) {
        token = token.substring(getTokenType().getPrefix().length());
        Jwt jwt = jwtParser.parseJwt(token);
        String redisKey = getTokenType().getPrefix() + jwt.getSubject();
        return JacksonUtils.parseObject((String) redisCache.getCacheObject(redisKey), LoginUserInfo.class);
    }

    @Override
    public boolean logout(String token) {
        token = token.substring(getTokenType().getPrefix().length());
        Jwt jwt = jwtParser.parseJwt(token);
        String redisKey = getTokenType().getPrefix() + jwt.getSubject();
        return redisCache.deleteCacheObject(redisKey);
    }
}
