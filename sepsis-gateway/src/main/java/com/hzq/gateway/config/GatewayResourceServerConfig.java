package com.hzq.gateway.config;

import com.hzq.core.result.ResultEnum;
import com.hzq.gateway.filter.CustomAuthenticationWebFilter;
import com.hzq.gateway.util.WebFluxUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author hua
 * @className com.hzq.gateway.config ResourceServerConfig
 * @date 2024/9/26 20:32
 * @description 资源服务访问安全配置类
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebFluxSecurity
public class GatewayResourceServerConfig {

    private final GatewaySecurityProperties gatewaySecurityProperties;
    private final CustomAuthenticationWebFilter customAuthenticationWebFilter;
    private final CustomAuthorizationManager customAuthorizationManager;

    /**
     * @param serverHttpSecurity ServerHttpSecurity 类似于 HttpSecurity 但适用于 WebFlux。
     * @return org.springframework.security.web.SecurityFilterChain
     * @author hua
     * @date 2024/9/26 21:29
     * @apiNote 基于 webflux 的 spring security 安全过滤配置，用于处理请求认证和授权
     **/
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity
                // 自定义认证过滤器
                .addFilterBefore(customAuthenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                // 白名单内的请求路径可直接放行至网关过滤器，其余的需要鉴权
                .authorizeExchange(exchange -> {
                            List<String> whiteUriList = gatewaySecurityProperties.getWhiteUriList();
                            if (!whiteUriList.isEmpty()) {
                                exchange.pathMatchers(whiteUriList.toArray(String[]::new)).permitAll();
                            }
                            exchange.anyExchange().access(customAuthorizationManager);
                        }
                )
                // 配置认证和授权失败的处理器
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                        .authenticationEntryPoint(customAuthenticationEntryPoint())
                        .accessDeniedHandler(customAccessDeniedHandler())
                )
                // 禁用 CSRF
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                // 自定义 CORS 跨域
                .cors(corsSpec -> corsSpec.configurationSource(customCorsConfiguration()))
                // 禁用 httpBasic 身份验证
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                // 禁用默认登录页面
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                // 禁用默认登出页面
                .logout(ServerHttpSecurity.LogoutSpec::disable);

        log.info("sepsis resource server config init successfully");
        return serverHttpSecurity.build();
    }

    /**
     * @return org.springframework.security.web.server.ServerAuthenticationEntryPoint
     * @author hua
     * @date 2024/9/23 0:46
     * @apiNote 处理 token无效 或者 已过期 自定义响应
     * 1. 这里使用 ServerAuthenticationEntryPoint 而不是 AuthenticationEntryPoint
     * a) AuthenticationEntryPoint 适用于传统的 Spring Security（基于 Servlet 的应用），例如 Spring MVC 应用。
     * 处理请求的方式是基于阻塞式的 Servlet API (HttpServletRequest, HttpServletResponse)
     * b) ServerAuthenticationEntryPoint 适用于基于 Spring WebFlux 的应用（响应式应用），如 Spring WebFlux 的项目。
     * 处理请求的方式是基于非阻塞的 WebFlux API (ServerWebExchange)
     **/
    @Bean
    public ServerAuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (exchange, denied) -> Mono.defer(() -> {
            log.error("token validation error on request {}: {}", exchange.getRequest().getURI(), denied.getMessage());
            return Mono.just(exchange.getResponse());
        }).flatMap(response -> WebFluxUtils.writeResponse(response, ResultEnum.TOKEN_INVALID_OR_EXPIRED));
    }

    /**
     * @return org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
     * @author hua
     * @date 2024/9/26 20:53
     * @apiNote 处理用户未被授权 自定义响应
     * 1. 这里使用 ServerAccessDeniedHandler 而不是 AccessDeniedHandler
     * a) AccessDeniedHandler 适用于传统的 Spring Security（基于 Servlet 的应用），例如 Spring MVC 应用。
     * 处理请求的方式是基于阻塞式的 Servlet API (HttpServletRequest, HttpServletResponse)
     * b) ServerAccessDeniedHandler 适用于基于 Spring WebFlux 的应用（响应式应用），如 Spring WebFlux 的项目。
     * 处理请求的方式是基于非阻塞的 WebFlux API (ServerWebExchange)
     **/
    @Bean
    public ServerAccessDeniedHandler customAccessDeniedHandler() {
        return (exchange, denied) -> Mono.defer(() -> {
            log.error("access denied on request {}: {}", exchange.getRequest().getURI(), denied.getMessage());
            return Mono.just(exchange.getResponse());
        }).flatMap(response -> WebFluxUtils.writeResponse(response, ResultEnum.ACCESS_UNAUTHORIZED));
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
        // 给配置源对象设置过滤的参数
        // 参数一: 过滤的路径 == > 所有的路径都要求校验是否跨域
        // 参数二: 配置类
        configSource.registerCorsConfiguration("/**", corsConfiguration);
        return configSource;
    }
}
