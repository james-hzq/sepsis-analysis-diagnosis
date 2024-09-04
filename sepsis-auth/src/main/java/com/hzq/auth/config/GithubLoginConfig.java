package com.hzq.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

/**
 * @author gc
 * @class com.hzq.auth.config GithubLoginConfig
 * @date 2024/9/4 12:26
 * @description GitHub 第三方登录授权配置
 */
@Configuration
public class GithubLoginConfig {

    private static final String REGISTRATION_ID = "github";
    private static final String CLIENT_NAME = "GitHub";

    @Value("spring.security.oauth2.client.registration.github.client-id")
    private String clientId;

    @Value("spring.security.oauth2.client.registration.github.client-secret")
    private String clientSecret;

    @Value("spring.security.oauth2.client.registration.github.redirect-uri")
    private String redirectUri;

    @Value("spring.security.oauth2.client.registration.github.scope")
    private String scope;

    @Value("spring.security.oauth2.client.provider.github.authorization-uri")
    private String authorizationUri;

    @Value("spring.security.oauth2.client.provider.github.token-uri")
    private String tokenUri;

    @Value("spring.security.oauth2.client.provider.github.user-info-uri")
    private String userInfoUri;

    @Value("spring.security.oauth2.client.provider.github.user-name-attribute")
    private String userNameAttributeName;


    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.githubClientRegistration());
    }

    private ClientRegistration githubClientRegistration() {
        return ClientRegistration
                // 使用指定的注册ID创建客户端注册对象
                .withRegistrationId(REGISTRATION_ID)
                // 设置 GitHub 客户端ID
                .clientId(clientId)
                // 设置 GitHub 客户端密钥
                .clientSecret(clientSecret)
                // 使用基本客户端密钥进行客户端身份验证
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                // 授权类型为授权码模式
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                // 指定重定向URI
                .redirectUri(redirectUri)
                // 指定访问令牌的权限范围
                .scope(scope)
                // GitHub 的授权终端点
                .authorizationUri(authorizationUri)
                // GitHub 的令牌终端点
                .tokenUri(tokenUri)
                // GitHub 用户信息终端点
                .userInfoUri(userInfoUri)
                // GitHub 返回的用户信息，那个字段是用户名字段
                .userNameAttributeName(userNameAttributeName)
                // 客户端名称为 GitHub
                .clientName(CLIENT_NAME)
                // 构建客户端注册对象
                .build();
    }
}
