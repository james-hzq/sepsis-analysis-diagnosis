package com.hzq.auth.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

/**
 * @author hua
 * @className com.hzq.gateway.config AuthSecurityConfig
 * @date 2024/9/28 14:36
 * @description 授权服务安全配置类，所有被网关转发到该微服务的请求，都会在此被拦截，根据规则进行请求放行和授权处理
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class AuthSecurityConfig {
    // 请求响应白名单列表
    private static final List<String> whitePaths = List.of(
            "/oauth/**"
    );

    private final UserDetailsService loginUserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, HandlerMappingIntrospector handlerMappingIntrospector) throws Exception {
        // 创建一个 MvcRequestMatcher 的构建器，用于根据路径匹配请求
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(handlerMappingIntrospector);

        httpSecurity
                // 配置 HTTP 请求的授权规则
                .authorizeHttpRequests(requests -> {
                            // 对每一个白名单路径，配置请求匹配器并允许所有访问
                            whitePaths.forEach(whitePath -> requests.requestMatchers(mvcMatcherBuilder.pattern(whitePath)).permitAll());
                            // 对其他所有请求，要求进行身份验证
                            requests.anyRequest().authenticated();
                        }
                )
                // 设置自定义的身份验证提供者，进行用户认证
                .authenticationProvider(daoAuthenticationProvider())
                // 禁用默认登录页面
                .formLogin(AbstractHttpConfigurer::disable)
                // 禁用默认登出页面
                .logout(AbstractHttpConfigurer::disable)
                // 禁用 CSRF 保护
                .csrf(AbstractHttpConfigurer::disable)
                // 配置 CORS 跨域
                .cors(corsConfigurer -> corsConfigurer.configurationSource(customCorsConfiguration()));

        return httpSecurity.build();
    }

    /**
     * @author hua
     * @date 2024/9/28 23:27
     * @return org.springframework.security.authentication.dao.DaoAuthenticationProvider
     * @apiNote 进行基于数据库的用户认证，使用 `UserDetailsService` 来加载用户的详细信息，并使用指定的密码编码器对用户输入的密码进行编码和验证。
     **/
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // 提供用户详细信息
        provider.setUserDetailsService(loginUserService);
        // 提供密码编码器
        provider.setPasswordEncoder(passwordEncoder());
        // 是否隐藏用户不存在异常，默认:true-隐藏；false-抛出异常；
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    /**
     * @author hua
     * @date 2024/9/28 23:25
     * @return org.springframework.security.crypto.password.PasswordEncoder
     * @apiNote 此 Bean 提供一个委托式密码编码器，使用强散列哈希加密实现
     **/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * @return org.springframework.web.cors.reactive.CorsConfigurationSource
     * @author hua
     * @date 2024/9/28 8:21
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
        // 使用 UrlBasedCorsConfigurationSource 来注册 CorsConfiguration, 注册路径为所有请求
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", corsConfiguration);
        return configSource;
    }
}
