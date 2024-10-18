package com.hzq.auth.config;

import com.hzq.auth.filter.SystemLoginAuthenticationFilter;
import com.hzq.auth.login.github.GithubLoginClient;
import com.hzq.auth.login.system.SystemLoginAuthenticationProvider;
import com.hzq.core.util.RSAUtils;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @class com.hzq.auth.config AuthSecurityConfig
 * @author gc
 * @date 2024/10/16 15:53
 * @description TODO
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class AuthSecurityConfig {
    // 跳过认证的白名单
    private static final List<String> whitePaths = List.of(
    );

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, HandlerMappingIntrospector introspector, AuthenticationManager authenticationManager) throws Exception {
        // 创建一个 MvcRequestMatcher 的构建器，用于根据路径匹配请求
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        // 系统用户名密码登录认证过滤器
        SystemLoginAuthenticationFilter systemLoginAuthenticationFilter = new SystemLoginAuthenticationFilter(authenticationManager);

        httpSecurity
                // 通过 URL 匹配规则设置认证
                .authorizeHttpRequests(requests -> {
                    // 对每一个白名单路径，配置请求匹配器并允许所有访问
                    whitePaths.forEach(whitePath -> requests.requestMatchers(mvcMatcherBuilder.pattern(whitePath)).permitAll());
                    // 对其他所有请求，要求进行身份验证
                    requests.anyRequest().authenticated();
                })
                // 禁用默认登录页面
                .formLogin(AbstractHttpConfigurer::disable)
                // 禁用默认登出页面
                .logout(AbstractHttpConfigurer::disable)
                // 禁用 CSRF 保护
                .csrf(AbstractHttpConfigurer::disable)
                // 添加 CORS 跨域配置
                .cors(corsSpec -> corsSpec.configurationSource(customCorsConfiguration()))
                // 添加系统用户名密码认证过滤器
                .addFilterBefore(systemLoginAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

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
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * @return org.springframework.web.cors.reactive.CorsConfigurationSource
     * @author gc
     * @date 2024/10/18 16:05
     * @apiNote 自定义 CORS 跨域
     **/
    @Bean
    public CorsConfigurationSource customCorsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 放行所有域名，生产环境请对此进行修改
        corsConfiguration.addAllowedOriginPattern("*");
        // 放行的请求头
        corsConfiguration.addAllowedHeader("*");
        // 放行的请求方式，主要有：GET, POST, PUT, DELETE, OPTIONS
        corsConfiguration.addAllowedMethod("*");
        // 暴露头部信息
        corsConfiguration.addExposedHeader("*");
        // 是否允许发送cookie
        corsConfiguration.setAllowCredentials(true);
        // 初始化cors配置源对象
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        // 给配置源对象设置过滤的参数
        // 参数一: 过滤的路径 == > 所有的路径都要求校验是否跨域
        // 参数二: 配置类
        configurationSource.registerCorsConfiguration("/**", corsConfiguration);
        // 返回配置好的过滤器
        return configurationSource;
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
