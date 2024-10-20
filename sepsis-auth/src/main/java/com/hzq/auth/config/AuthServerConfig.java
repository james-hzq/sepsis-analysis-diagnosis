package com.hzq.auth.config;

import com.hzq.auth.handler.HzqAuthenticationFailureHandler;
import com.hzq.auth.handler.HzqAuthenticationSuccessHandler;
import com.hzq.auth.login.system.SystemLoginAuthenticationConverter;
import com.hzq.auth.login.system.SystemLoginAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.util.List;

/**
 * @className com.hzq.auth.config AuthServerConfig
 * @author hua
 * @date 2024/10/20 15:16
 * @description TODO
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AuthServerConfig {
    @Bean
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        SystemLoginAuthenticationProvider systemLoginAuthenticationProvider = new SystemLoginAuthenticationProvider();

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                // 自定义授权模式转换器(Converter)
                .tokenEndpoint(tokenEndpoint -> tokenEndpoint
                        .accessTokenRequestConverters(
                                authenticationConverters ->
                                        authenticationConverters.addAll(
                                                List.of(
                                                        new SystemLoginAuthenticationConverter()
                                                )
                                        )
                        )
                        .authenticationProviders(
                                authenticationProviders -> // <2>
                                        // 自定义授权模式提供者(Provider)
                                        authenticationProviders.addAll(
                                                List.of(
                                                        systemLoginAuthenticationProvider
                                                )
                                        )
                        )
                        .accessTokenResponseHandler(new HzqAuthenticationSuccessHandler()) // 自定义成功响应
                        .errorResponseHandler(new HzqAuthenticationFailureHandler()) // 自定义失败响应
                );

        return http.build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }
}
