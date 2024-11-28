package com.hzq.auth.config.auth;

import com.hzq.auth.config.oauth2.CustomAccessTokenResponseClient;
import com.hzq.auth.config.oauth2.CustomAuthorizationRequestRepository;
import com.hzq.auth.config.oauth2.CustomAuthorizationRequestResolver;
import com.hzq.auth.handler.*;
import com.hzq.auth.login.service.SysUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.filter.CorsFilter;

/**
 * @author gc
 * @class com.hzq.auth.config AuthSecurityConfig
 * @date 2024/11/4 9:20
 * @description 授权服务基本配置
 */
@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class AuthSecurityConfig {

    // 授权安全配置
    private final AuthSecurityProperties authSecurityProperties;
    // CORS 过滤器
    private final CorsFilter corsFilter;
    // 密码增强器
    private final PasswordEncoder passwordEncoder;
    // 系统用户服务
    private final SysUserDetailService sysUserDetailService;
    // 客户端注册的存储库
    private final ClientRegistrationRepository clientRegistrationRepository;
    // OAuth2AuthorizationRequest 存储库
    private final CustomAuthorizationRequestRepository customAuthorizationRequestRepository;
    //OAuth2AuthorizationRequest 解析程序
    private final CustomAuthorizationRequestResolver customAuthorizationRequestResolver;
    // 处理访问令牌（access token）响应的客户端。
    private final CustomAccessTokenResponseClient customAccessTokenResponseClient;
    // 获取用户信息服务
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;
    // OAuth2 授权成功回调
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    // OAuth2 授权失败回调
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    // 系统用户登录成功回调
    private final SystemAuthenticationSuccessHandler systemAuthenticationSuccessHandler;
    // 系统用户登录失败回调
    private final SystemAuthenticationFailureHandler systemAuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain authSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // 添加 CORS 过滤器
        httpSecurity.addFilter(corsFilter);

        // 禁用默认配置 CSRF保护 与 CORS跨域
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable);

        // 添加安全配置规则
        httpSecurity.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(authSecurityProperties.getWhiteUriList().toArray(new String[0])).permitAll()
                .anyRequest().authenticated()
        );

        // 用于处理整个过滤链中的异常。它会在请求被拒绝或抛出异常时触发，通常用于处理请求被拒绝的情况，如未登录用户访问需要认证的资源，或认证信息失效的情况。
        // 当请求进入过滤链，但用户没有认证信息或权限，导致 AccessDeniedException 或 AuthenticationException，则会触发 exceptionHandling 的处理器。
        httpSecurity.exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer
                .defaultAuthenticationEntryPointFor(
                        new LoginTargetAuthenticationEntryPoint(authSecurityProperties.getLoginPageUri()),
                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                )
        );

        // 配置登录认证
        httpSecurity
                // 添加表单登录认证
                .formLogin(formLogin -> formLogin
                        .loginPage(authSecurityProperties.getLoginPageUri())
                        .loginProcessingUrl(authSecurityProperties.getSystemLoginPath())
                        .successHandler(systemAuthenticationSuccessHandler)
                        .failureHandler(systemAuthenticationFailureHandler)
                )
                // 添加联合登录认证
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage(authSecurityProperties.getLoginPageUri())
                        // 设置客户端注册的存储库
                        .clientRegistrationRepository(clientRegistrationRepository)
                        // 配置 Authorization Server 的授权端点
                        .authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig
                                // 设置用于存储 OAuth2AuthorizationRequest 的存储库
                                .authorizationRequestRepository(customAuthorizationRequestRepository)
                                // 设置用于解析 OAuth2AuthorizationRequest 的解析程序
                                .authorizationRequestResolver(customAuthorizationRequestResolver)
                        )
                        // 令牌端点配置，用于处理访问令牌（access token）的请求和响应
                        .tokenEndpoint(tokenEndpointConfig -> tokenEndpointConfig
                                // 配置处理访问令牌（access token）响应的客户端。
                                .accessTokenResponseClient(customAccessTokenResponseClient)
                        )
                        // 配置获取用户信息服务
                       .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                               .userService(oAuth2UserService)
                       )
                        // 配置成功回调
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        // 配置失败回调
                        .failureHandler(oAuth2AuthenticationFailureHandler)
                );

        return httpSecurity.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(sysUserDetailService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * @return org.springframework.security.authentication.AuthenticationManager
     * @author gc
     * @date 2024/10/18 14:15
     * @apiNote 返回自定义的 AuthenticationManager 认证方式
     **/
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
