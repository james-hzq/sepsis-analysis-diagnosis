package com.hzq.auth.config;

import com.hzq.auth.config.oauth2.CustomAccessTokenResponseClient;
import com.hzq.auth.config.oauth2.CustomAuthorizationRequestResolver;
import com.hzq.auth.constant.SecurityConstants;
import com.hzq.auth.constant.SecurityProperties;
import com.hzq.auth.filter.CachedRequestBodyFilter;
import com.hzq.auth.filter.SystemLoginAuthenticationFilter;
import com.hzq.auth.handler.HzqAuthenticationSuccessHandler;
import com.hzq.auth.handler.LoginTargetAuthenticationEntryPoint;
import com.hzq.auth.handler.SystemLoginFailureHandler;
import com.hzq.auth.handler.SystemLoginSuccessHandler;
import com.hzq.auth.service.LoginUserService;
import com.hzq.auth.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

/**
 * @class com.hzq.auth.config AuthSecurityConfig
 * @author gc
 * @date 2024/10/16 15:53
 * @description TODO
 */
@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class AuthSecurityConfig {

    // 自定义安全配置
    private final SecurityProperties securityProperties;
    // Cors过滤器
    private final CorsFilter corsFilter;
    //
    private final CustomAuthorizationRequestResolver customAuthorizationRequestResolver;
    //
    private final CustomAccessTokenResponseClient customAccessTokenResponseClient;

    private final PasswordEncoder passwordEncoder;
    private final LoginUserService loginUserService;
    private final OAuth2AuthorizedClientService oAuth2AuthorizationService;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {
        CachedRequestBodyFilter cachedRequestBodyFilter = new CachedRequestBodyFilter();

        SystemLoginAuthenticationFilter systemLoginAuthenticationFilter = new SystemLoginAuthenticationFilter();
        systemLoginAuthenticationFilter.setAuthenticationManager(authenticationManager);

        // 添加过滤器
        httpSecurity
                .addFilter(corsFilter)
                .addFilterBefore(systemLoginAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(cachedRequestBodyFilter, SystemLoginAuthenticationFilter.class);

        OAuth2AuthorizationServerConfigurer httpConfigurer = httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class);

        if (httpConfigurer != null) {
            // 认证服务配置
            httpConfigurer
                    // 开启OpenID Connect 1.0协议相关端点
                    .oidc(oidcConfigurer -> oidcConfigurer
                            .providerConfigurationEndpoint(provider -> provider
                                    .providerConfigurationCustomizer(builder -> builder
                                            // 为OIDC端点添加短信认证码的登录方式
                                            .grantType(SecurityConstants.GRANT_TYPE_SMS_CODE)
                                    )
                            )
                    )
                    // 让认证服务器元数据中有自定义的认证方式
                    .authorizationServerMetadataEndpoint(metadata -> metadata.authorizationServerMetadataCustomizer(customizer -> customizer.grantType(SecurityConstants.GRANT_TYPE_SMS_CODE)));
        }

        // 禁用 csrf 与 cors
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.cors(AbstractHttpConfigurer::disable);

        httpSecurity
                // 当未登录时访问认证端点时重定向至 login 页面
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginTargetAuthenticationEntryPoint(securityProperties.getLoginPageUrl()),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                )
                // 添加BearerTokenAuthenticationFilter，将认证服务当做一个资源服务，解析请求头中的token
                .oauth2ResourceServer((resourceServer) -> resourceServer
                        .jwt(Customizer.withDefaults())
                        .accessDeniedHandler(SecurityUtils::exceptionHandler)
                        .authenticationEntryPoint(SecurityUtils::exceptionHandler));

        httpSecurity.authorizeHttpRequests(authorize -> authorize
                        // 放行静态资源和不需要认证的url
                        .requestMatchers(securityProperties.getWhiteUriList().toArray(new String[0])).permitAll()
                        .anyRequest().authenticated()
                )
                // 指定登录页面
                .formLogin(formLogin -> formLogin
                        .loginPage(securityProperties.getLoginPageUrl())
                        .loginProcessingUrl(securityProperties.getLoginApiUrl())
                        .successHandler(new SystemLoginSuccessHandler())
                        .failureHandler(new SystemLoginFailureHandler())
                );

        // 联合身份认证
        httpSecurity.oauth2Login(oauth2Login -> oauth2Login
                .loginPage(securityProperties.getLoginPageUrl())
                .loginProcessingUrl("/auth/github/callback")
                .authorizationEndpoint(authorization -> authorization
                        .authorizationRequestResolver(customAuthorizationRequestResolver)
                )
                .tokenEndpoint(token -> token
                        .accessTokenResponseClient(customAccessTokenResponseClient)
                )
                .successHandler(new HzqAuthenticationSuccessHandler(oAuth2AuthorizationService))
        );

        return httpSecurity.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(loginUserService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

    /**
     * @return org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
     * @author hua
     * @date 2024/10/13 11:10
     * @apiNote 不走过滤器链的放行配置
     **/
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                AntPathRequestMatcher.antMatcher("/doc.html"),
                AntPathRequestMatcher.antMatcher("/swagger-ui/**")
        );
    }
}
