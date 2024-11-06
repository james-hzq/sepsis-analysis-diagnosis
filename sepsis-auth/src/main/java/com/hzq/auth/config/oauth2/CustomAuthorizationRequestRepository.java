package com.hzq.auth.config.oauth2;

import com.hzq.jackson.JacksonUtils;
import com.hzq.redis.cache.RedisCache;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author gc
 * @class com.hzq.auth.config CustomAuthorizationRequestRepository
 * @date 2024/11/4 11:12
 * @description 配置用于存储 OAuth2AuthorizationRequest 的存储库
 */
@Slf4j
@Component
@RequiredArgsConstructor
public final class CustomAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    // Redis key 前缀
    private static final String AUTHORIZATION_REQUEST_PREFIX = "oauth2_auth_request:";
    // 设置过期时间
    private static final Integer AUTHORIZATION_REQUEST_EXPIRATION_MINUTES = 10;

    private final RedisCache redisCache;

    /**
     * @param request 请求对象
     * @return org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
     * @author gc
     * @date 2024/11/4 11:22
     * @apiNote 从请求中加载OAuth 2.0授权请求，具体为从 Redis 中取处授权请求对象
     **/
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Assert.notNull(request, "request cannot be null");

        String state = request.getParameter(OAuth2ParameterNames.STATE);
        if (!StringUtils.hasText(state)) {
            return null;
        }

        return redisCache.getCacheObject(AUTHORIZATION_REQUEST_PREFIX + state);
    }

    /**
     * @param authorizationRequest OAuth2授权请求对象
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @author gc
     * @date 2024/11/4 11:31
     * @apiNote 保存OAuth2授权请求到Redis缓存中，如果授权请求为null，则从缓存中移除对应的授权请求
     **/
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(response, "response cannot be null");
        if (authorizationRequest == null) {
            removeAuthorizationRequest(request, response);
            return;
        }
        String state = authorizationRequest.getState();
        Assert.hasText(state, "authorizationRequest.state cannot be empty");
        redisCache.setCacheObject(
                AUTHORIZATION_REQUEST_PREFIX + state,
                JacksonUtils.toJsonString(authorizationRequest),
                AUTHORIZATION_REQUEST_EXPIRATION_MINUTES,
                TimeUnit.MINUTES
        );
    }

    /**
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @return org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
     * @author gc
     * @date 2024/11/4 11:36
     * @apiNote 从Redis缓存中移除授权请求
     **/
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(response, "response cannot be null");

        String state = request.getParameter(OAuth2ParameterNames.STATE);
        if (!StringUtils.hasText(state)) {
            return null;
        }

        String redisKey = AUTHORIZATION_REQUEST_PREFIX + state;
        String oauth2AuthorizationRequestCache = redisCache.getCacheObject(redisKey);
        OAuth2AuthorizationRequest oAuth2AuthorizationRequest = JacksonUtils.parseObject(oauth2AuthorizationRequestCache, OAuth2AuthorizationRequest.class);
        redisCache.deleteCacheObject(redisKey);
        return oAuth2AuthorizationRequest;
    }
}
