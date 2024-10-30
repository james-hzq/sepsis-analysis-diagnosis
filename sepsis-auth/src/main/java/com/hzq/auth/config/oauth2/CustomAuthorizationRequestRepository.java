package com.hzq.auth.config.oauth2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hzq.jackson.JacksonUtil;
import com.hzq.redis.cache.RedisCache;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author gc
 * @class com.hzq.auth.config.oauth2 CustomAuthorizationRequestRepository
 * @date 2024/10/30 17:15
 * @description TODO
 */
@Component
public class CustomAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final String AUTHORIZATION_REQUEST_PREFIX = "oauth2_auth_request:"; // Redis key 前缀
    private static final Integer AUTHORIZATION_REQUEST_EXPIRATION_MINUTES = 10; // 设置过期时间
    @Resource
    private RedisCache redisCache;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        String state = request.getParameter("state");
        if (state == null) {
            return null;
        }
        return redisCache.getCacheObject(AUTHORIZATION_REQUEST_PREFIX + state);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeAuthorizationRequest(request, response);
            return;
        }
        String state = authorizationRequest.getState();
        if (state != null) {
            redisCache.setCacheObject(
                    AUTHORIZATION_REQUEST_PREFIX + state, JacksonUtil.toJsonString(authorizationRequest), AUTHORIZATION_REQUEST_EXPIRATION_MINUTES, TimeUnit.MINUTES
            );
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        String state = request.getParameter("state");
        if (state == null) {
            return null;
        }
        String redisKey = AUTHORIZATION_REQUEST_PREFIX + state;
        OAuth2AuthorizationRequest authorizationRequest = JacksonUtil.parseObject((String) redisCache.getCacheObject(redisKey), OAuth2AuthorizationRequest.class);
        redisCache.deleteCacheObject(redisKey);
        return authorizationRequest;
    }
}
