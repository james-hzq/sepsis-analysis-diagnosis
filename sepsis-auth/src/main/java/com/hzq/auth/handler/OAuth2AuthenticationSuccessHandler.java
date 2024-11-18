package com.hzq.auth.handler;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.hzq.auth.login.user.BaseOAuth2User;
import com.hzq.redis.cache.RedisCache;
import com.hzq.security.authentication.AccessTokenAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author gc
 * @class com.hzq.auth.handler OAuth2AuthenticationSuccessHandler
 * @date 2024/11/4 15:35
 * @description 联合认证成功回调
 */
@Slf4j
@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    // OAuth2联合登录成功后，重定向到登录页面，并且附带access_token，下一次请求携带access_token
    private static final String REDIRECT_BASE_URL = "http://localhost:9050/callback";
    private static final String TOKEN_TYPE_PREFIX = "ACCESS-TOKEN:";
    private static final String LOGIN_TYPE = "?login-type=";
    private static final String ACCESS_TOKEN = "&access-token=";
    private static final String REFRESH_TOKEN = "&refresh-token=";
    private RedisCache redisCache;

    @Autowired
    public void setRedisCache(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken) {
            // 将认证主体转换为 BaseOAuth2User，如果需要用到其子类，需要给出标识单独判断
            BaseOAuth2User baseOAuth2User = (BaseOAuth2User) Optional.ofNullable(oAuth2AuthenticationToken.getPrincipal())
                    .orElseThrow(() -> new OAuth2AuthenticationException("认证对象主体为空"));
            // 获取 access_token
            OAuth2AccessToken accessToken = Optional.ofNullable(baseOAuth2User.getAccessToken())
                    .orElseThrow(() -> new OAuth2AuthenticationException("access_token未能成功生成"));
            // 获取时间差
            Instant issuedAt = Optional.ofNullable(accessToken.getIssuedAt())
                    .orElseThrow(() -> new OAuth2AuthenticationException("access_token签发时间为空"));
            Instant expiresAt = Optional.ofNullable(accessToken.getExpiresAt())
                    .orElseThrow(() -> new OAuth2AuthenticationException("access_token过期时间为空"));
            Integer secondsDifference = (int) Duration.between(issuedAt, expiresAt).getSeconds();
            // 获取 access_token 内容
            String accessTokenContext = Optional.ofNullable(accessToken.getTokenValue())
                    .orElseThrow(() -> new OAuth2AuthenticationException("access_token内容时间为空"));
            // 生成存入 redis 的认证对象信息
            Map<String, Object> details = ImmutableMap.<String, Object>builder()
                    .put("loginType", oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())
                    .put("username", baseOAuth2User.getName())
                    .put("avatar", Optional.ofNullable(baseOAuth2User.getAttribute("avatar_url")).orElse(""))
                    .build();
            Set<String> roles = ImmutableSet.<String>builder()
                    .add("user")
                    .build();
            AccessTokenAuthentication accessTokenAuthentication = new AccessTokenAuthentication()
                    .setAccessToken(accessTokenContext)
                    .setIssuedAt(issuedAt)
                    .setExpiresAt(expiresAt)
                    .setPrincipal(baseOAuth2User.getName())
                    .setRoles(roles)
                    .setDetails(details);
            // 重定向到前端
            String redirectUrl = REDIRECT_BASE_URL
                    + LOGIN_TYPE + "github"
                    + ACCESS_TOKEN + TOKEN_TYPE_PREFIX + accessTokenContext
                    + REFRESH_TOKEN;
            response.sendRedirect(redirectUrl);
            log.info("联合认证成功，进入回调方法，并且重定向 URL 成功，下面进行 Redis 用户信息 存储");
            // 将 access_token 和 access_token 授权的第三方用户信息存入 Redis，Key - access_token，Value - 用户信息
            redisCache.setCacheObject(accessTokenContext, accessTokenAuthentication, secondsDifference, TimeUnit.SECONDS);
        }
    }
}
