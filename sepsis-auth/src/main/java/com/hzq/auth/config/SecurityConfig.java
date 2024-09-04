package com.hzq.auth.config;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

/**
 * @author hua
 * @className com.hzq.auth.config SecurityConfig
 * @date 2024/9/1 22:48
 * @description 默认的安全配置
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    // 请求响应白名单列表
    private static final List<String> whitePaths = List.of(
    );

    /**
     * @return org.springframework.security.web.SecurityFilterChain
     * @author gc
     * @date 2024/9/2 17:00
     * @apiNote Spring Security 安全过滤器链配置
     **/
    @Bean
    @Order(0)
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        // 创建一个 MvcRequestMatcher 的构建器，用于根据路径匹配请求
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        // 配置 HTTP 请求的授权规则
        http.authorizeHttpRequests(requests -> {
                            // 对每一个白名单路径，配置请求匹配器并允许所有访问
                            if (CollectionUtil.isNotEmpty(whitePaths)) {
                                for (String whitelistPath : whitePaths) {
                                    requests.requestMatchers(mvcMatcherBuilder.pattern(whitelistPath)).permitAll();
                                }
                            }
                            // 对其他所有请求，要求进行身份验证
                            requests.anyRequest().authenticated();
                        }
                )
                // 禁用 CSRF（跨站请求伪造）保护
                .csrf(AbstractHttpConfigurer::disable)
                // 启用表单登录，使用默认配置
                .formLogin(Customizer.withDefaults())
                // 启用第三方授权登录
                .oauth2Login(Customizer.withDefaults());

        return http.build();
    }


    /**
     * @return org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
     * @author gc
     * @date 2024/9/2 17:06
     * @apiNote 不走过滤器链的放行配置
     **/
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                AntPathRequestMatcher.antMatcher("/doc.html")
        );
    }
}
