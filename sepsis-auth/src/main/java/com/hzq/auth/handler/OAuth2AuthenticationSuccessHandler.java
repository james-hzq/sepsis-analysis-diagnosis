package com.hzq.auth.handler;

import com.hzq.core.result.Result;
import com.hzq.jackson.JacksonUtil;
import com.hzq.redis.cache.RedisCache;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
    private final RedisCache redisCache;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) oAuth2AuthenticationToken.getPrincipal();
        // 将 access_token 和 access_token 授权的第三方用户信息存入 Redis，并返回加密的 access_token
        log.info("第三方联合登录成功, authentication = {}", authentication.toString());

        // 将 authentication 信息序列化为 JSON
        String authenticationJson = URLEncoder.encode(JacksonUtil.toJsonString(authentication), StandardCharsets.UTF_8);

        // 构造重定向 URL
        String redirectUrl = "http://localhost:9050/";

        // 重定向到指定的 URL
        response.sendRedirect(redirectUrl);
    }
}
