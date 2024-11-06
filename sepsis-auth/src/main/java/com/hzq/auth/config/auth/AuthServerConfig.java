package com.hzq.auth.config.auth;

import com.hzq.auth.handler.LoginTargetAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
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

        // 配置错误处理
        httpSecurity.exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer
                .defaultAuthenticationEntryPointFor(
                        new LoginTargetAuthenticationEntryPoint(authSecurityProperties.getLoginPageUri()),
                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                )
        );

        return httpSecurity.build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(authSecurityProperties.getIssuerUrl())
                .build();
    }
}
