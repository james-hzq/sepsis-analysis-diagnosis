package com.hzq.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.client.endpoint.*;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

/**
 * @author gc
 * @class com.hzq.auth.config CustomAccessTokenResponseClient
 * @date 2024/11/4 12:26
 * @description 配置处理访问令牌（access token）响应的客户端
 */
@Slf4j
@Component
public class CustomAccessTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    // 设置 ACCESS_TOKEN 的过期时间
    private static final long ACCESS_TOKEN_EXPIRES_SECONDS = 3600;

    // 默认使用DefaultAuthorizationCodeTokenResponseClient来获取token
    private final DefaultAuthorizationCodeTokenResponseClient defaultTokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();

    public CustomAccessTokenResponseClient() {
        OAuth2AccessTokenResponseHttpMessageConverter messageConverter = new OAuth2AccessTokenResponseHttpMessageConverter();
        // 你可以自定义 RestTemplate，如果需要处理自定义的响应或者拦截器
        RestTemplate restTemplate = new RestTemplate(
                Arrays.asList(
                        new FormHttpMessageConverter(),
                        messageConverter
                )
        );
        // 添加拦截器或自定义配置
        defaultTokenResponseClient.setRestOperations(restTemplate);
    }

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
        log.info("进入 CustomAccessTokenResponseClient 类, getTokenResponse 方法");
        // 使用默认的 token 响应处理
        OAuth2AccessTokenResponse tokenResponse = defaultTokenResponseClient.getTokenResponse(authorizationGrantRequest);
        OAuth2AccessToken accessToken = tokenResponse.getAccessToken();

        // 使用 Builder 创建新的 OAuth2AccessTokenResponse，目的是设置过期时间
        return OAuth2AccessTokenResponse.withResponse(tokenResponse)
                .tokenType(accessToken.getTokenType())
                .expiresIn(ACCESS_TOKEN_EXPIRES_SECONDS)
                .scopes(accessToken.getScopes())
                .additionalParameters(tokenResponse.getAdditionalParameters())
                .build();
    }
}
