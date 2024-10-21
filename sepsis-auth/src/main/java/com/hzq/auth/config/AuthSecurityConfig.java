package com.hzq.auth.config;

import com.hzq.auth.filter.CachedRequestBodyFilter;
import com.hzq.auth.filter.SystemLoginAuthenticationFilter;
import com.hzq.auth.login.system.SystemLoginAuthenticationProvider;
import com.hzq.auth.service.LoginUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
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
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class AuthSecurityConfig {
    // 跳过认证的白名单
    private static final List<String> whitePaths = List.of(
    );

    // Cors过滤器
    private final CorsFilter corsFilter;
    // 请求体缓存过滤器
    private final CachedRequestBodyFilter cachedRequestBodyFilter;
    // 密码管理器
    private final PasswordEncoder passwordEncoder;
    // 用于用户名密码认证的 UserDetailsService
    private final LoginUserService loginUserService;
    // OAuth2令牌生成器
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    // 注册客户端仓库
    private final RegisteredClientRepository registeredClientRepository;
    // 授权信息存储服务
    private final OAuth2AuthorizationService authorizationService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, HandlerMappingIntrospector introspector, AuthenticationManager authenticationManager) throws Exception {
        // 创建一个 MvcRequestMatcher 的构建器，用于根据路径匹配请求
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        SystemLoginAuthenticationFilter systemLoginAuthenticationFilter = new SystemLoginAuthenticationFilter();
        systemLoginAuthenticationFilter.setAuthenticationManager(authenticationManager);

        httpSecurity
                .authorizeHttpRequests(requests -> {
                    whitePaths.forEach(whitePath -> requests.requestMatchers(mvcMatcherBuilder.pattern(whitePath)).permitAll());
                    requests.anyRequest().authenticated();
                })
                // 配置系统用户名密码，表单登录
                .formLogin(AbstractHttpConfigurer::disable)
                // 禁用默认登出页面
                .logout(AbstractHttpConfigurer::disable)
                // 禁用 CSRF 保护
                .csrf(AbstractHttpConfigurer::disable)
                // 添加 CORS 过滤器
                .addFilter(corsFilter)
                // 添加请求体缓存过滤器
                .addFilterAfter(cachedRequestBodyFilter, CorsFilter.class)
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
     * b) AuthenticationProvider 接口提供
     * 了一个supports方法，用来验证是否支持某种身份验证方式，实现扩展
     **/
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        ProviderManager authenticationManager = (ProviderManager) authenticationConfiguration.getAuthenticationManager();
        List<AuthenticationProvider> providers = authenticationManager.getProviders();

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(loginUserService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        SystemLoginAuthenticationProvider systemLoginAuthenticationProvider = new SystemLoginAuthenticationProvider();
        systemLoginAuthenticationProvider.setTokenGenerator(tokenGenerator);
        systemLoginAuthenticationProvider.setAuthorizationService(authorizationService);
        systemLoginAuthenticationProvider.setRegisteredClientRepository(registeredClientRepository);

        providers.add(daoAuthenticationProvider);
        providers.add(systemLoginAuthenticationProvider);
        return authenticationManager;
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
