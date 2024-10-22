package com.hzq.auth.config;

import com.hzq.auth.constant.SecurityConstants;
import com.hzq.auth.constant.SecurityProperties;
import com.hzq.auth.filter.CachedRequestBodyFilter;
import com.hzq.auth.handler.HzqAuthenticationFailureHandler;
import com.hzq.auth.handler.HzqAuthenticationSuccessHandler;
import com.hzq.auth.handler.LoginTargetAuthenticationEntryPoint;
import com.hzq.auth.login.system.SystemLoginAuthenticationConverter;
import com.hzq.auth.login.system.SystemLoginAuthenticationProvider;
import com.hzq.auth.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
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

    // CORS 跨域配置
    private final CorsFilter corsFilter;
    private final SecurityProperties securityProperties;
    // OAuth2令牌生成器
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    // 注册客户端仓库
    private final RegisteredClientRepository registeredClientRepository;
    // 授权信息存储服务
    private final OAuth2AuthorizationService authorizationService;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authenticationServerFilterChain(HttpSecurity httpSecurity) throws Exception {
        // 配置默认的设置
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);

        // 添加跨域过滤器
        httpSecurity.addFilter(corsFilter);

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
                // 当未登录时访问认证端点时重定向至login页面
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
     * b) AuthenticationProvider 接口提供
     * 了一个supports方法，用来验证是否支持某种身份验证方式，实现扩展
     **/
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(securityProperties.getIssuerUrl())
                .build();
    }
}
