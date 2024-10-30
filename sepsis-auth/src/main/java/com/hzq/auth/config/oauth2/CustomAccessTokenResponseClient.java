package com.hzq.auth.config.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * @author gc
 * @class com.hzq.auth.config.oauth2 CustomAccessTokenResponseClient
 * @date 2024/10/22 15:34
 * @description TODO
 */
@Slf4j
@Component
public class CustomAccessTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

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
        log.info("进入 CustomAccessTokenResponseClient 类");
        // 使用默认的 token 响应处理
        OAuth2AccessTokenResponse tokenResponse = defaultTokenResponseClient.getTokenResponse(authorizationGrantRequest);

        return tokenResponse;
    }
}
