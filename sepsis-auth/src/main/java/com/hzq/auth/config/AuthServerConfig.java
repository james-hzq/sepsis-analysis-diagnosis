package com.hzq.auth.config;

import com.hzq.auth.handler.HzqAuthenticationFailureHandler;
import com.hzq.auth.handler.HzqAuthenticationSuccessHandler;
import com.hzq.auth.login.github.GithubLoginClient;
import com.hzq.auth.login.system.SystemLoginAuthenticationConverter;
import com.hzq.auth.login.system.SystemLoginAuthenticationProvider;
import com.hzq.core.util.RSAUtils;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.List;
import java.util.Set;

/**
 * @author gc
 * @class com.hzq.auth.config AuthServerConfig
 * @date 2024/10/12 15:56
 * @description 授权服务配置
 */
@Configuration
@RequiredArgsConstructor
public class AuthServerConfig {
    private final GithubLoginClient githubLoginClient;
    private final OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer;

    /**
     * @return org.springframework.security.web.SecurityFilterChain
     * @author gc
     * @date 2024/10/12 16:16
     * @apiNote 授权服务器端点配置
     **/
    @Bean
    @Order(1)
    public SecurityFilterChain authFilterChain(HttpSecurity httpSecurity) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);

        SystemLoginAuthenticationProvider systemLoginAuthenticationProvider = new SystemLoginAuthenticationProvider();

        httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                // 自定义授权模式转换器(Converter)
                .tokenEndpoint(tokenEndpoint -> tokenEndpoint
                        .accessTokenRequestConverters(
                                authenticationConverters ->
                                        // 自定义授权模式转换器(Converter)
                                        authenticationConverters.addAll(
                                                List.of(
                                                        new SystemLoginAuthenticationConverter()
                                                )
                                        )
                        )
                        .authenticationProviders(
                                authenticationProviders ->
                                        // 自定义授权模式提供者(Provider)
                                        authenticationProviders.addAll(
                                                List.of(
                                                        systemLoginAuthenticationProvider
                                                )
                                        )
                        )
                        .accessTokenResponseHandler(new HzqAuthenticationSuccessHandler())
                        .errorResponseHandler(new HzqAuthenticationFailureHandler())
                );
        DefaultSecurityFilterChain securityFilterChain = httpSecurity.build();

        OAuth2TokenGenerator<?> tokenGenerator = httpSecurity.getSharedObject(OAuth2TokenGenerator.class);
        AuthenticationManager authenticationManager = httpSecurity.getSharedObject(AuthenticationManager.class);
        OAuth2AuthorizationService authorizationService = httpSecurity.getSharedObject(OAuth2AuthorizationService.class);

        systemLoginAuthenticationProvider.setTokenGenerator(tokenGenerator);
        systemLoginAuthenticationProvider.setAuthorizationService(authorizationService);
        systemLoginAuthenticationProvider.setAuthenticationManager(authenticationManager);

        return securityFilterChain;
    }

    /**
     * @author hua
     * @date 2024/10/2 9:11
     * @param authenticationConfiguration  Spring Security 提供的 AuthenticationConfiguration 对象，该对象用于构建和配置 AuthenticationManager。
     * @return org.springframework.security.authentication.AuthenticationManager
     * @apiNote 返回一个用于身份认证的 AuthenticationManager 实例，可以在整个应用中注入和使用。
     * 1. 在 Spring Security 5.x 及其之后的版本中，AuthenticationManager 不再自动暴露为一个可注入的 bean。
     * 2. 需要手动通过 AuthenticationConfiguration 获取并将其暴露为 bean。此方法将根据 Spring Security 的配置，
     * 3. 常用于自定义身份验证流程，例如在控制器中使用 AuthenticationManager 进行认证。
     **/
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * @return org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
     * @author gc
     * @date 2024/10/15 11:05
     * @apiNote 构建一个用于配置授权服务器的类。
     **/
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    @SneakyThrows
    public JWKSource<SecurityContext> jwkSource() {
        // 从 RSAUtils 获取公钥和私钥
        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) RSAUtils.getPublicKey(RSAUtils.PUBLIC_KEY_CONTEXT))
                .privateKey(RSAUtils.getPrivateKey(RSAUtils.PRIVATE_KEY_CONTEXT))
                .build();
        // 构建JWKSet
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * @author hua
     * @date 2024/10/8 22:04
     * @return org.springframework.security.oauth2.jwt.JwtEncoder
     * @apiNote 配置一个 JwtEncoder，用于生成 JWT（JSON Web Token），并且使用 RSA 公钥和私钥对 JWT 进行签名。
     **/
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * @param jwkSource JSON Web Key (JWK) 数据源，用于提供 JWT 的签名和验证
     * @return org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator<?>
     * @author gc
     * @date 2024/10/15 10:27
     * @apiNote 该方法用于生成 OAuth2 Token，包括 JWT、Access Token 和 Refresh Token 的生成器的组合
     **/
    @Bean
    public OAuth2TokenGenerator<?> tokenGenerator(JWKSource<SecurityContext> jwkSource) {
        // 使用 Nimbus JOSE + JWT 库的 JWT 编码器，并将其传递给 JwtGenerator，用于对 JWT 进行编码。
        NimbusJwtEncoder jwtEncoder = new NimbusJwtEncoder(jwkSource);
        // 创建一个 JwtGenerator 实例，用于生成 JWT token,
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
        // 设置 JWT 的自定义器，可以在生成 JWT token 时对其进行自定义操作。
        jwtGenerator.setJwtCustomizer(jwtCustomizer);
        // 创建一个 OAuth2AccessTokenGenerator 实例，用于生成 OAuth2 访问令牌（Access Token）。
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        // 创建一个 OAuth2RefreshTokenGenerator 实例，用于生成 OAuth2 刷新令牌（Refresh Token）。
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        // 创建一个 OAuth2 令牌生成器的委托类，用于委托生成 JWT、Access Token 和 Refresh Token 的具体实现类。
        return new DelegatingOAuth2TokenGenerator(jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
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

    /**
     * @return org.springframework.security.crypto.password.PasswordEncoder
     * @author gc
     * @date 2024/10/14 14:52
     * @apiNote 密码加密器
     **/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    private RegisteredClient initSepsisSystemClient() {
        String id = "sepsis";
        String clientId = "sepsis-web-client";
        String clientSecret = passwordEncoder().encode("sepsis");
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
