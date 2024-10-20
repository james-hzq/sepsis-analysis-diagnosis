package com.hzq.auth.config;

import com.hzq.auth.domain.LoginUser;
import com.hzq.core.constant.LoginConstants;
import com.hzq.core.util.RSAUtils;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.token.*;

import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
            // 检查令牌类型和主体类型
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType()) &&
                    context.getPrincipal() instanceof UsernamePasswordAuthenticationToken
            ) {
                Optional.ofNullable(context.getPrincipal().getPrincipal()).ifPresent(principal -> {
                    JwtClaimsSet.Builder claims = context.getClaims();
                    // 如果主体是LoginUser类型，即系统用户登录，则添加自定义字段
                    if (principal instanceof LoginUser loginUser) {

                        claims.claim(LoginConstants.SYSTEM_LOGIN_USER_ID, loginUser.getUserId());
                        claims.claim(LoginConstants.SYSTEM_LOGIN_USER_NAME, loginUser.getUsername());

                        // 这里存入角色至JWT，解析JWT的角色用于鉴权的位置: ResourceServerConfig#jwtAuthenticationConverter
                        Set<String> roles = AuthorityUtils.authorityListToSet(context.getPrincipal().getAuthorities())
                                .stream()
                                .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
                        claims.claim(LoginConstants.SYSTEM_LOGIN_USER_ROLES, roles);
                    }
                });
            }
        };
    }
}
