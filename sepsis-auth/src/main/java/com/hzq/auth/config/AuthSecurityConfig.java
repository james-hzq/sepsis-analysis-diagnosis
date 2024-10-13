package com.hzq.auth.config;

import com.hzq.auth.service.GithubLoginService;
import com.hzq.auth.service.LoginUserService;
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
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.security.interfaces.RSAPublicKey;
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
            "/oauth/system/login", "/oauth/login/user-info"
    );

    private final CorsFilter corsFilter;

    @Bean
    @Order(0)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity, HandlerMappingIntrospector introspector) throws Exception {
        httpSecurity
                // 配置 HTTP 请求的授权规则
                .authorizeHttpRequests(requests -> {
                            // 创建一个 MvcRequestMatcher 的构建器，用于根据路径匹配请求
                            MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
                            // 对每一个白名单路径，配置请求匹配器并允许所有访问
                            whitePaths.forEach(whitePath -> requests.requestMatchers(mvcMatcherBuilder.pattern(whitePath)).permitAll());
                            // 对其他所有请求，要求进行身份验证
                            requests.anyRequest().authenticated();
                        }
                )
                // 禁用默认登录页面
                .formLogin(AbstractHttpConfigurer::disable)
                // 禁用默认登出页面
                .logout(AbstractHttpConfigurer::disable)
                // 禁用 CSRF 保护
                .csrf(AbstractHttpConfigurer::disable)
                // 配置 CORS 跨域
                .addFilter(corsFilter);

        return httpSecurity.build();
    }

    /**
     * @author hua
     * @date 2024/10/13 11:10
     * @return org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
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
