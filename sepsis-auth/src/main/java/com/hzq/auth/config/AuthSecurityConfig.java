package com.hzq.auth.config;

import com.hzq.auth.filter.HzqUsernamePasswordAuthenticationFilter;
import com.hzq.auth.handler.HzqAuthenticationFailureHandler;
import com.hzq.auth.handler.HzqAuthenticationSuccessHandler;
import com.hzq.core.result.Result;
import com.hzq.core.result.ResultEnum;
import com.hzq.jackson.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.nio.charset.StandardCharsets;
import java.util.List;

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
    // 请求响应白名单列表
    private static final List<String> whitePaths = List.of(
            "/oauth/login/user-info"
    );

    private final CorsFilter corsFilter;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity, HandlerMappingIntrospector introspector, AuthenticationManager authManager) throws Exception {
        // 创建一个 MvcRequestMatcher 的构建器，用于根据路径匹配请求
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        httpSecurity
                // 添加 CORS 跨域配置过滤器
                .addFilter(corsFilter)
                // 添加自定义用户名密码认证过滤器
                .addFilterBefore(getUsernamePasswordAuthenticationFilter(authManager), UsernamePasswordAuthenticationFilter.class)
                // 通过 URL 匹配规则设置认证
                .authorizeHttpRequests(requests -> {
                    // 对每一个白名单路径，配置请求匹配器并允许所有访问
                    whitePaths.forEach(whitePath -> requests.requestMatchers(mvcMatcherBuilder.pattern(whitePath)).permitAll());
                    // 对其他所有请求，要求进行身份验证
                    requests.anyRequest().authenticated();
                })
                // 配置登录页面
                .formLogin(formLogin -> formLogin
                        .loginPage("/oauth/system/login")
                        .successHandler((request, response, authentication) -> {
                            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.getWriter().write(JacksonUtil.toJsonString(Result.success()));
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.getWriter().write(JacksonUtil.toJsonString(Result.error(ResultEnum.ACCESS_FORBIDDEN, exception.getMessage())));
                        })
                )
                // 禁用默认登出页面
                .logout(AbstractHttpConfigurer::disable)
                // 禁用 CSRF 保护
                .csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
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

    private UsernamePasswordAuthenticationFilter getUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        // 自定义的 UsernamePasswordAuthenticationFilter 实现
        HzqUsernamePasswordAuthenticationFilter filter = new HzqUsernamePasswordAuthenticationFilter();

        // 设置处理登录请求的路径
        filter.setFilterProcessesUrl("/oauth/system/login");

        // 设置认证失败和成功的处理器
        filter.setAuthenticationFailureHandler(new HzqAuthenticationFailureHandler());
        filter.setAuthenticationSuccessHandler(new HzqAuthenticationSuccessHandler());

        filter.setAuthenticationManager(authenticationManager);

        return filter;
    }
}
