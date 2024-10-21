package com.hzq.auth.config;

import com.hzq.auth.filter.CachedRequestBodyFilter;
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
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

/**
 * @author gc
 * @class com.hzq.auth.config AuthServerConfig
 * @date 2024/10/21 15:34
 * @description TODO
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AuthServerConfig {

    private final CorsFilter corsFilter;
    // 请求体缓存过滤器
    private final CachedRequestBodyFilter cachedRequestBodyFilter;

    @Bean
    public SecurityFilterChain authFilterChain(HttpSecurity httpSecurity) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);

        httpSecurity.addFilter(corsFilter);
        httpSecurity.addFilterAfter(cachedRequestBodyFilter, CorsFilter.class);
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        SystemLoginAuthenticationProvider systemLoginAuthenticationProvider = new SystemLoginAuthenticationProvider();


        httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
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
                                authenticationProviders ->
                                        authenticationProviders.addAll(
                                                List.of(
                                                        systemLoginAuthenticationProvider
                                                )
                                        )
                        )
                        .accessTokenResponseHandler(new HzqAuthenticationSuccessHandler()) // 自定义成功响应
                        .errorResponseHandler(new HzqAuthenticationFailureHandler()) // 自定义失败响应
                );
        return httpSecurity.build();
    }



    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .build();
    }
}
