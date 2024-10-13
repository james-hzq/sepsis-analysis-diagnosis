package com.hzq.auth.config;

import com.hzq.auth.handler.HzqAuthenticationFailureHandler;
import com.hzq.auth.handler.HzqAuthenticationSuccessHandler;
import com.hzq.auth.login.github.GithubLoginClient;
import com.hzq.auth.login.system.SystemLoginClient;
import com.hzq.auth.service.LoginUserService;
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
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

import java.security.interfaces.RSAPublicKey;
import java.util.List;

/**
 * @author gc
 * @class com.hzq.auth.config AuthServerConfig
 * @date 2024/10/12 15:56
 * @description TODO
 */
@Configuration
@RequiredArgsConstructor
public class AuthServerConfig {
    private final GithubLoginClient githubLoginClient;
    private final SystemLoginClient systemLoginClient;
    private final CorsFilter corsFilter;
    private final LoginUserService loginUserService;
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

        httpSecurity.addFilter(corsFilter);

        httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                // 自定义授权模式转换器(Converter)
                .tokenEndpoint(tokenEndpoint -> tokenEndpoint
                        .accessTokenRequestConverters(
                                authenticationConverters ->
                                        // 自定义授权模式转换器(Converter)
                                        authenticationConverters.addAll(
                                                List.of(
                                                )
                                        )
                        )
                        .authenticationProviders(
                                authenticationProviders ->
                                        // 自定义授权模式提供者(Provider)
                                        authenticationProviders.addAll(
                                                List.of(
                                                )
                                        )
                        )
                        .accessTokenResponseHandler(new HzqAuthenticationSuccessHandler())
                        .errorResponseHandler(new HzqAuthenticationFailureHandler())
                );

        return httpSecurity.build();
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
     * @author hua
     * @date 2024/10/8 22:04
     * @return org.springframework.security.oauth2.jwt.JwtEncoder
     * @apiNote 配置一个 JwtEncoder，用于生成 JWT（JSON Web Token），并且使用 RSA 公钥和私钥对 JWT 进行签名。
     **/
    @SneakyThrows
    @Bean
    public JwtEncoder jwtEncoder() {
        // 从 RSAUtils 获取公钥和私钥
        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) RSAUtils.getPublicKey(RSAUtils.PUBLIC_KEY_CONTEXT))
                .privateKey(RSAUtils.getPrivateKey(RSAUtils.PRIVATE_KEY_CONTEXT))
                .build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(rsaKey));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    public OAuth2TokenGenerator<?> tokenGenerator(JWKSource<SecurityContext> jwkSource) {
        JwtGenerator jwtGenerator = new JwtGenerator(new NimbusJwtEncoder(jwkSource));
        jwtGenerator.setJwtCustomizer(jwtCustomizer);

        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
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
        RegisteredClient systemRegisteredClient = systemLoginClient.systemRegisteredClient();
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
