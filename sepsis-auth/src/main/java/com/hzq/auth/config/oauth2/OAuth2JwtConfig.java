package com.hzq.auth.config.oauth2;

import com.hzq.core.util.RSAUtils;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.security.interfaces.RSAPublicKey;

/**
 * @author gc
 * @class com.hzq.auth.config OAuth2JwtConfig
 * @date 2024/10/18 15:29
 * @description OAuth2 JWT 配置类
 */
@Configuration
public class OAuth2JwtConfig {

    /**
     * @return com.nimbusds.jose.jwk.source.JWKSource<com.nimbusds.jose.proc.SecurityContext>
     * @author gc
     * @date 2024/10/18 15:21
     * @apiNote JWK 是一种表示加密密钥（特别是用于 JWT 的密钥）的标准格式。
     **/
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
     * @param jwtEncoder JWT 编码器
     * @return org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator<?>
     * @author gc
     * @date 2024/10/15 10:27
     * @apiNote 该方法用于生成 OAuth2 Token，包括 JWT、Access Token 和 Refresh Token 的生成器的组合
     **/
    @Bean
    public OAuth2TokenGenerator<?> tokenGenerator(JwtEncoder jwtEncoder) {
        // 创建一个 JwtGenerator 实例，用于生成 JWT token,
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
        // 设置 JWT 的自定义器，可以在生成 JWT token 时对其进行自定义操作。
        jwtGenerator.setJwtCustomizer(jwtTokenCustomizer());
        // 创建一个 OAuth2AccessTokenGenerator 实例，用于生成 OAuth2 访问令牌（Access Token）。
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        // 创建一个 OAuth2RefreshTokenGenerator 实例，用于生成 OAuth2 刷新令牌（Refresh Token）。
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        // 创建一个 OAuth2 令牌生成器的委托类，用于委托生成 JWT、Access Token 和 Refresh Token 的具体实现类。
        return new DelegatingOAuth2TokenGenerator(jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }

    /**
     * @author hua
     * @date 2024/10/8 22:04
     * @return org.springframework.security.oauth2.jwt.JwtEncoder
     * @apiNote 编码（加密）JWT（JSON Web Token）
     **/
    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        // 使用 Nimbus JOSE + JWT 库的 JWT 编码器，并将其传递给 JwtGenerator，用于对 JWT 进行编码。
        return new NimbusJwtEncoder(jwkSource);
    }

    /**
     * @author hua
     * @date 2024/10/8 22:04
     * @return org.springframework.security.oauth2.jwt.JwtEncoder
     * @apiNote 解码（验证）JWT（JSON Web Token）
     **/
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * @author hua
     * @date 2024/10/13 9:15
     * @return org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer<org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext>
     * @apiNote 自定义 JWT 令牌
     **/
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        return context -> {
        };
    }

    /**
     * 自定义jwt解析器，设置解析出来的权限信息的前缀与在jwt中的key
     *
     * @return jwt解析器 JwtAuthenticationConverter
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // 设置解析权限信息的前缀，设置为空是去掉前缀
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        // 设置权限信息在jwt claims中的key
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
