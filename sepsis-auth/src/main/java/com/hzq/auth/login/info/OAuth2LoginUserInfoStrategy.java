package com.hzq.auth.login.info;

import com.hzq.jackson.util.JacksonUtils;
import com.hzq.redis.cache.RedisCache;
import com.hzq.security.authentication.LoginUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author hua
 * @className com.hzq.auth.login.info OAuth2LoginUserInfoStrategy
 * @date 2024/11/30 15:06
 * @description OAuth2 第三方授权获取用户信息策略
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginUserInfoStrategy implements LoginUserInfoStrategy {

    private final RedisCache redisCache;

    @Override
    public TokenType getTokenType() {
        return TokenType.OAUTH2_LOGIN_ACCESS_TOKEN;
    }

    @Override
    public LoginUserInfo getLoginUserInfo(String token) {
        return JacksonUtils.parseObject((String) redisCache.getCacheObject(token), LoginUserInfo.class);
    }

    @Override
    public boolean logout(String token) {
        return redisCache.deleteCacheObject(token);
    }
}
