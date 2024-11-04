package com.hzq.auth.handler;

import com.hzq.auth.domain.user.BaseOAuth2User;
import com.hzq.jackson.JacksonUtils;
import com.hzq.redis.cache.RedisCache;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author gc
 * @class com.hzq.auth.handler OAuth2AuthenticationSuccessHandler
 * @date 2024/11/4 15:35
 * @description TODO
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final String REDIRECT_URL = "http://localhost:9050/login?access_token=";
    private final RedisCache redisCache;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        BaseOAuth2User baseOAuth2User = (BaseOAuth2User) oAuth2AuthenticationToken.getPrincipal();
        // 将 access_token 和 access_token 授权的第三方用户信息存入 Redis，并返回加密的 access_token
        String redisKey = baseOAuth2User.getAccessToken();
        redisCache.setCacheObject(redisKey, baseOAuth2User);
        // 构造重定向 URL
        String redirectUrl = REDIRECT_URL + baseOAuth2User.getAccessToken();
        // 重定向到指定的 URL
        response.sendRedirect(redirectUrl);
    }
}
