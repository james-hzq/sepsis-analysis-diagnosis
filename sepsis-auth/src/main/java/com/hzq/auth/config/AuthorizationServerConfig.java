package com.hzq.auth.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzq.auth.handler.HzqAuthenticationFailureHandler;
import com.hzq.auth.handler.HzqAuthenticationSuccessHandler;
import com.hzq.auth.model.LoginUser;
import com.hzq.core.constant.RedisConstants;
import com.hzq.redis.cache.RedisCache;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;

/**
 * @author hua
 * @className com.hzq.auth.config AuthorizationServerConfig
 * @date 2024/9/1 22:24
 * @description 授权服务器配置
 */
@Configuration
@AllArgsConstructor
public class AuthorizationServerConfig {
    private final OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer;
    private final RedisCache redisCache;

    /**
     * @param authenticationConfiguration Spring Security 提供的一个配置类，用于创建和配置 AuthenticationManager。
     * @return org.springframework.security.authentication.AuthenticationManager
     * @author gc
     * @date 2024/9/4 16:37
     * @apiNote 创建并返回一个 AuthenticationManager 实例。
     **/
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * @param httpSecurity Spring Security 提供的一个配置类，用于定义 Web 安全的具体配置
     * @param authenticationManager 一个接口，负责验证用户的身份信息，用于根据传入的认证请求，（如用户名密码登录、OAuth2 授权码登录等），返回认证结果（成功或失败）。
     * @param tokenGenerator 负责创建和签发 OAuth2 令牌。在授权服务器中，它被用来生成最终返回给客户端的令牌，如访问令牌（Access Token）和刷新令牌（Refresh Token）。
     * @return org.springframework.security.web.SecurityFilterChain
     * @author gc
     * @date 2024/9/4 16:15
     * @apiNote TODO
     **/
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity httpSecurity,
            AuthenticationManager authenticationManager,
            OAuth2TokenGenerator<?> tokenGenerator

    ) throws Exception {
        // 应用默认的 OAuth2 授权服务器安全配置，设置基础的安全机制。
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);

        // 获取 OAuth2 授权服务器配置类的配置器。
        httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .tokenEndpoint(tokenEndpoint -> tokenEndpoint
                        // 自定义授权模式转换器（Converter），用于处理不同类型的授权请求。
                        .accessTokenRequestConverters(
                                authenticationConverters ->
                                        authenticationConverters.addAll(
                                                List.of()
                                        )
                        )
                        // 自定义授权模式提供者（Provider），用于处理不同类型的身份验证。
                        .authenticationProviders(authenticationProviders ->
                                authenticationProviders.addAll(
                                        List.of()
                                )
                        )
                        // 设置自定义的认证成功处理器，在身份验证成功后执行特定操作。
                        .accessTokenResponseHandler(new HzqAuthenticationSuccessHandler())
                        // 设置自定义的认证失败处理器，在身份验证失败后执行特定操作。
                        .errorResponseHandler(new HzqAuthenticationFailureHandler())
                );

        // 配置异常处理，指定当用户未认证且请求HTML页面时，重定向到 "/system/login" 登录页面。
        httpSecurity.exceptionHandling(exceptions -> exceptions
                .defaultAuthenticationEntryPointFor(
                        new LoginUrlAuthenticationEntryPoint("/system/login"),
                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                )
        );

        // 配置 OAuth2 资源服务器，启用 JWT 令牌支持，并使用默认设置。
        httpSecurity.oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(Customizer.withDefaults()));

        return httpSecurity.build();
    }

    /**
     * @return com.nimbusds.jose.jwk.source.JWKSource<com.nimbusds.jose.proc.SecurityContext>
     * @author gc
     * @date 2024/9/4 16:46
     * @apiNote 从 Redis 中获取 JWKSet，如果不存在则生成新的 RSA 密钥对，并将生成的 JWKSet 存储到 Redis 中。
     **/
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        // 从 Redis 中获取 JWKSet (JWT密钥对，包含非对称加密的公钥和私钥)
        String jwkSetStr = redisCache.getCacheObject(RedisConstants.JWK_SET_KEY);
        if (StringUtils.isNotBlank(jwkSetStr)) {
            // 如果 Redis 中存在 JWKSet，解析JWKSet并返回
            JWKSet jwkSet;
            try {
                jwkSet = JWKSet.parse(jwkSetStr);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            return new ImmutableJWKSet<>(jwkSet);
        } else {
            // 如果 Redis 中不存在 JWKSet，生成新的JWKSet
            KeyPair keyPair = generateRsaKey();
            RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                    .privateKey((RSAPrivateKey) keyPair.getPrivate())
                    .keyID(UUID.randomUUID().toString())
                    .build();
            JWKSet jwkSet = new JWKSet(rsaKey);

            // 将 JWKSet 存储在Redis中
            redisCache.setCacheObject(RedisConstants.JWK_SET_KEY, jwkSet.toString(Boolean.FALSE));
            // 返回一个不可变的 JWKSet 实例，用于保证线程安全性和避免意外修改。
            return new ImmutableJWKSet<>(jwkSet);
        }
    }


    /**
     * @return java.security.KeyPair
     * @author gc
     * @date 2024/9/4 16:46
     * @apiNote 用于生成一个 RSA 密钥对，其中包括一个公钥和一个私钥。
     **/
    private KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    /**
     * @param jwkSource JWKSource 是一个接口，用于提供 JSON Web Key（JWK）的来源。
     * @return org.springframework.security.oauth2.jwt.JwtDecoder
     * @author gc
     * @date 2024/9/4 16:56
     * @apiNote 定义一个名为 jwtDecoder 的 Bean，返回 JwtDecoder 对象。这个 Bean 用于解码和验证 JWT。
     **/
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * @param jwkSource JWKSource 是一个接口，用于提供 JSON Web Key（JWK）的来源。
     * @return org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator<?>
     * @author gc
     * @date 2024/9/4 16:59
     * @apiNote 定义一个名为 tokenGenerator 的 Bean，这个 Bean 用于生成 OAuth2 令牌（包括访问令牌和刷新令牌）。
     **/
    @Bean
    public OAuth2TokenGenerator<?> tokenGenerator(JWKSource<SecurityContext> jwkSource) {
        // 创建 JwtGenerator 实例，使用传入的 JWKSource<SecurityContext> 对象初始化 NimbusJwtEncoder。
        JwtGenerator jwtGenerator = new JwtGenerator(new NimbusJwtEncoder(jwkSource));
        // // 设置 JwtCustomizer，定制 JWT 的内容和属性。
        jwtGenerator.setJwtCustomizer(jwtCustomizer);
        // 创建 OAuth2AccessTokenGenerator 实例，用于生成访问令牌（Access Token）。
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        // 创建 OAuth2RefreshTokenGenerator 实例，用于生成刷新令牌（Refresh Token）。
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        // DelegatingOAuth2TokenGenerator 将调用不同的生成器来生成各种类型的令牌。
        return new DelegatingOAuth2TokenGenerator(jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }

    /**
     * @return org.springframework.security.crypto.password.PasswordEncoder
     * @author gc
     * @date 2024/9/4 11:04
     * @apiNote 密码加密器
     **/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
