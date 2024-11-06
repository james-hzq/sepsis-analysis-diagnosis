package com.hzq.auth.login.client;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

/**
 * @author gc
 * @class com.hzq.auth.config GithubLoginClient
 * @date 2024/10/12 9:53
 * @description Github 第三方登录配置
 */
@Data
@Configuration
public class GithubLoginClient {

    private static final String registrationId = "github";

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.github.client-name}")
    private String clientName;

    @Value("${spring.security.oauth2.client.registration.github.authorization-grant-type}")
    private AuthorizationGrantType authorizationGrantType;

    @Value("${spring.security.oauth2.client.registration.github.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.github.scope}")
    private String scope;

    @Value("${spring.security.oauth2.client.provider.github.authorization-uri}")
    private String authorizationUri;

    @Value("${spring.security.oauth2.client.provider.github.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.provider.github.user-info-uri}")
    private String userInfoUri;

    @Value("${spring.security.oauth2.client.provider.github.user-name-attribute}")
    private String userNameAttributeName;

    public ClientRegistration githubClientRegistration() {
        return ClientRegistration
                .withRegistrationId(registrationId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .clientName(clientName)
                .authorizationGrantType(authorizationGrantType)
                .redirectUri(redirectUri)
                .scope(scope)
                .authorizationUri(authorizationUri)
                .tokenUri(tokenUri)
                .userInfoUri(userInfoUri)
                .userNameAttributeName(userNameAttributeName)
                .build();
    }
}
