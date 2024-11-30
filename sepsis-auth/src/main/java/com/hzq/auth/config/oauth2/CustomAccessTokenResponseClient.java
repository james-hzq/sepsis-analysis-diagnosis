package com.hzq.auth.config.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.client.endpoint.*;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Arrays;

/**
 * @author gc
 * @class com.hzq.auth.config CustomAccessTokenResponseClient
 * @date 2024/11/4 12:26
 * @description 配置处理访问令牌（access token）响应的客户端
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class CustomAccessTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    // 设置 ACCESS_TOKEN 的过期时间
    private static final long ACCESS_TOKEN_EXPIRES_SECONDS = 10800;
    // 设置 RestTemplate 的过期时间
    private static final Integer REST_TEMPLATE_EXPIRES_SECONDS = 5;

    // 默认使用DefaultAuthorizationCodeTokenResponseClient来获取token
    private final DefaultAuthorizationCodeTokenResponseClient defaultTokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();

    public CustomAccessTokenResponseClient() {
        // 配置向OAuth2第三方客户端发送访问令牌和用户请求的 RestTemplate 的配置
        RestTemplate restTemplate = new RestTemplate();

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(REST_TEMPLATE_EXPIRES_SECONDS));
        requestFactory.setReadTimeout(Duration.ofSeconds(REST_TEMPLATE_EXPIRES_SECONDS));
        restTemplate.setRequestFactory(requestFactory);

        OAuth2AccessTokenResponseHttpMessageConverter messageConverter = new OAuth2AccessTokenResponseHttpMessageConverter();
        restTemplate.setMessageConverters(Arrays.asList(new FormHttpMessageConverter(), messageConverter));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());

        // 添加拦截器或自定义配置
        this.defaultTokenResponseClient.setRestOperations(restTemplate);
    }

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
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
