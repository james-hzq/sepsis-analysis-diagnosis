package com.hzq.auth.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

/**
 * @author gc
 * @class com.hzq.auth.config AuthServerConfig
 * @date 2024/11/4 14:09
 * @description 授权服务配置
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AuthServerConfig {

    private final AuthSecurityProperties authSecurityProperties;
    private final CorsFilter corsFilter;

    @Bean
    public SecurityFilterChain authServerFilterChain(HttpSecurity httpSecurity) throws Exception {
        // 配置默认的设置
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);

        // 添加过滤器
        httpSecurity.addFilter(corsFilter);

        // 禁用默认配置 CSRF保护 与 CORS跨域
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(authSecurityProperties.getIssuerUrl())
                .build();
    }
}
