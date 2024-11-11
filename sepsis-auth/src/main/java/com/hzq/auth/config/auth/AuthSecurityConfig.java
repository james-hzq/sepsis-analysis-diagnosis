package com.hzq.auth.config.auth;

import com.hzq.auth.config.oauth2.CustomAccessTokenResponseClient;
import com.hzq.auth.config.oauth2.CustomAuthorizationRequestRepository;
import com.hzq.auth.config.oauth2.CustomAuthorizationRequestResolver;
import com.hzq.auth.handler.LoginTargetAuthenticationEntryPoint;
import com.hzq.auth.handler.OAuth2AuthenticationFailureHandler;
import com.hzq.auth.handler.OAuth2AuthenticationSuccessHandler;
import com.hzq.auth.login.service.CustomOAuth2UserService;
import com.hzq.auth.login.service.GithubOAuth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
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

    private final AuthSecurityProperties authSecurityProperties;
    private final CorsFilter corsFilter;
    private final CustomAuthorizationRequestRepository customAuthorizationRequestRepository;
    private final CustomAuthorizationRequestResolver customAuthorizationRequestResolver;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final CustomAccessTokenResponseClient customAccessTokenResponseClient;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain authSecurityFilterChain(HttpSecurity httpSecurity, OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService) throws Exception {

        // 添加过滤器
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
                )
                // 添加联合登录认证
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage(authSecurityProperties.getLoginPageUri())
                        // 配置 Authorization Server 的授权端点
                        .authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig
                                // 设置用于存储 OAuth2AuthorizationRequest 的存储库
                                .authorizationRequestRepository(customAuthorizationRequestRepository)
                                // 设置用于解析 OAuth2AuthorizationRequest 的解析程序
                                .authorizationRequestResolver(customAuthorizationRequestResolver)
                        )
                        // 设置客户端注册的存储库
                        .clientRegistrationRepository(clientRegistrationRepository)
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

    /**
     * @return org.springframework.security.authentication.AuthenticationManager
     * @author gc
     * @date 2024/10/18 14:15
     * @apiNote 返回自定义的 AuthenticationManager 认证方式
     * 1. AuthenticationManager
     * a) AuthenticationManager 是一个顶级接口，提供 authenticate()方法，接收 Authentication 身份认证对象，用来处理身份验证请求。
     * b) AuthenticationManager 的认证方法成功后一个Authentication对象，如果发生异常将会抛出 AuthenticationException
     * 2. ProviderManager
     * a) ProviderManager 是 AuthenticationManager 的一个实现类，有一个成员变量，List<AuthenticationProvider> providers。
     * b) ProviderManager 主要是对 AuthenticationProvider 链进行管理，
     * 3. AuthenticationProvider
     * a) AuthenticationProvider 通常按照认证请求链顺序去执行，若返回非null响应表示程序验证通过，不再尝试验证其它的provider。
     *    如果后续提供的身份验证程序成功地对请求进行身份认证，则忽略先前的身份验证异常及null响应，并将使用成功的身份验证。
     *    如果没有provider提供一个非null响应，或者有一个新的抛出AuthenticationException，那么最后的AuthenticationException将会抛出。
     * b) AuthenticationProvider 接口提供了一个supports方法，用来验证是否支持某种身份验证方式，实现扩展
     **/
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
        return new CustomOAuth2UserService()
                .setOAuth2UserService("github", new GithubOAuth2UserService());
    }
}
