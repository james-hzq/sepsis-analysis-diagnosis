package com.hzq.auth.config.oauth2;

import com.hzq.auth.login.client.GithubLoginClient;
import com.hzq.auth.login.client.SystemLoginClient;
import com.hzq.auth.login.service.CustomOAuth2UserService;
import com.hzq.auth.login.service.GithubOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.List;

/**
 * @author gc
 * @class com.hzq.auth.config OAuth2ClientConfig
 * @date 2024/11/4 9:55
 * @description 授权服务的客户端配置
 */
@Configuration
@RequiredArgsConstructor
public class OAuth2ClientConfig {

    private final GithubLoginClient githubLoginClient;
    private final SystemLoginClient systemLoginClient;

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOidcUserService() {
        return new CustomOAuth2UserService()
                .setOidcUserService("github", new GithubOAuth2UserService());
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService() {
        return new InMemoryOAuth2AuthorizedClientService(githubClientRegistrationRepository());
    }

    /**
     * @return org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
     * @author gc
     * @date 2024/10/18 15:53
     * @apiNote 创建基于内存的OAuth2授权服务
     **/
    @Bean
    public OAuth2AuthorizationService authorizationService() {
        return new InMemoryOAuth2AuthorizationService();
    }

    /**
     * @return org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService
     * @author gc
     * @date 2024/10/15 11:13
     * @apiNote 将授权同意信息存储在内存中
     **/
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService() {
        return new InMemoryOAuth2AuthorizationConsentService();
    }

    /**
     * @author hua
     * @date 2024/10/13 17:11
     * @return org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
     * @apiNote 用于存储 OAuth2 授权服务器的客户端信息，特别是 RegisteredClient 实例。
     * 1. InMemoryRegisteredClientRepository 用于存储 OAuth2 授权服务器的客户端信息 (RegisteredClient)，如密码授权、客户端凭据授权等。
     * 2. InMemoryClientRegistrationRepository 用于存储与外部 OAuth2 提供者（如 GitHub、Google 等）集成的客户端注册信息 (ClientRegistration)。
     **/
    @Bean
    public RegisteredClientRepository systemClientRegistrationRepository() {
        RegisteredClient systemRegisteredClient = systemLoginClient.initSepsisSystemClient();
        return new InMemoryRegisteredClientRepository(List.of(systemRegisteredClient));
    }

    /**
     * @author hua
     * @date 2024/10/13 17:11
     * @return org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
     * @apiNote 用于存储与外部 OAuth2 提供者（如 GitHub、Google 等）集成的客户端注册信息，特别是 ClientRegistration 实例。
     * 1. InMemoryRegisteredClientRepository 用于存储 OAuth2 授权服务器的客户端信息 (RegisteredClient)，如密码授权、客户端凭据授权等。
     * 2. InMemoryClientRegistrationRepository 用于存储与外部 OAuth2 提供者（如 GitHub、Google 等）集成的客户端注册信息 (ClientRegistration)。
     **/
    @Bean
    public ClientRegistrationRepository githubClientRegistrationRepository() {
        ClientRegistration githubClientRegistration = githubLoginClient.githubClientRegistration();
        return new InMemoryClientRegistrationRepository(List.of(githubClientRegistration));
    }
}
