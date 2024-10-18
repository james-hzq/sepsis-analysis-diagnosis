package com.hzq.auth.config;

import com.hzq.auth.login.github.GithubLoginClient;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.List;
import java.util.Set;

/**
 * @author gc
 * @class com.hzq.auth.config RegisteredClientConfig
 * @date 2024/10/18 15:29
 * @description TODO
 */
@Configuration
@AllArgsConstructor
public class OAuth2ClientConfig {
    private final GithubLoginClient githubLoginClient;
    private final PasswordEncoder passwordEncoder;

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
        RegisteredClient systemRegisteredClient = initSepsisSystemClient();
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

    private RegisteredClient initSepsisSystemClient() {
        String id = "sepsis";
        String clientId = "sepsis-web-client";
        String clientSecret = passwordEncoder.encode("sepsis");
        String clientName = "脓毒症智能分析与诊询平台";
        Set<String> scopes = Set.of("root", "admin", "user");

        return RegisteredClient
                // 创建一个带有指定 ID的已注册客户端对象
                .withId(id)
                // 设置客户端ID
                .clientId(clientId)
                // 设置客户端密钥
                .clientSecret(clientSecret)
                // 设置客户端名称
                .clientName(clientName)
                // 设置客户端认证方法为基本身份验证
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                // 授权类型为密码授权模式
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                // 授权类型为刷新令牌授权模式
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                // 设置重定向URI
                .redirectUri("http://127.0.0.1:9200/authorized")
                // 设置登出后的重定向URI
                .postLogoutRedirectUri("http://127.0.0.1:9200/logout")
                // 设置客户端范围（scopes）
                .scopes(set -> set.addAll(scopes))
                // 设置令牌设置，包括访问令牌的存活时间为1天
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofDays(1)).build())
                // 设置客户端设置，需要授权同意
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                // 构建并返回已注册的客户端对象
                .build();
    }
}
