package com.hzq.gateway.config;

import com.hzq.core.result.ResultEnum;
import com.hzq.core.util.RSAUtils;
import com.hzq.gateway.util.WebFluxUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import reactor.core.publisher.Mono;

import java.security.interfaces.RSAPublicKey;

/**
 * @author hua
 * @className com.hzq.gateway.config ResourceServerConfig
 * @date 2024/9/26 20:32
 * @description 资源访问配置类，经过网关过滤器链的请求，会在此处考虑是否拦截
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebFluxSecurity
public class ResourceServerConfig {
    public static final String PUBLIC_KEY_CONTEXT = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIYSxMgMlbUgAY5D2zMCB79R" +
            "/hJnAEnh2EyjkNL2ZRAAONQvgGydur/VHHECjoMSA82Vwsr5ijuXdN0wceoct4ECAwEAAQ==";
    public static final String PRIVATE_KEY_CONTEXT = "MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAhhLEyAyV" +
            "tSABjkPbMwIHv1H+EmcASeHYTKOQ0vZlEAA41C+AbJ26v9UccQKOgxIDzZXCyvmKO5d03TBx6" +
            "hy3gQIDAQABAkA2etDwc0CwIWnQa91Z7ETGpuQliSoyW22/sqVKPCoT5lNjEbpbtrcIEb/EK5" +
            "4DbUc0e/2HzPtxj4Z5C1h9CwA7AiEA0qLRk6PXsXizJJRo8b39cO6aV86Mib0LSw7yzyPmr0c" +
            "CIQCi8sBTHtQre63+SLRfkWmMAToHCVc9j4xoeicHzHqW9wIgY9BkH+J0Q9U+jwcE9AlkIC/x" +
            "U8q9Lkg3IcpjpWUN2+ECIHWlmJAqwPsIF+5w5bHeVfscY53y84bh3nkMQKPT0WqvAiBiD1wab" +
            "4cuREWQEElR2sPVPR6f1bwz0p28Ut3ILOZ0WQ==";
    // 存放在 resource 下的公钥文件
    private static final String PUBLIC_KEY_FILE_NAME = "public.key";
    // 请求白名单，该集合中的路径，跳过认证，可直接进入系统
    private static final String[] whitesUrIs = new String[] {"/system/**"};

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
                // 配置访问限制：通过 URL 模式限制请求的访问
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers(whitesUrIs).permitAll()
                        .anyExchange().authenticated())
                // 配置认证和授权失败的处理器
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint())
                        .accessDeniedHandler(customAccessDeniedHandler()))
                // 配置 OAuth 2.0 资源服务器保护支持
                .oauth2ResourceServer(oauth2 -> oauth2
                        // 配置自定义 JWT 身份验证转换器
                        .jwt(jwtSpec -> jwtSpec
                                .jwtAuthenticationConverter(customJwtConverter())
                                .publicKey(getRsaPublicKey())
                        ))
                .csrf(ServerHttpSecurity.CsrfSpec::disable);
        log.info("sepsis gateway resource server config init successfully");
        return serverHttpSecurity.build();
    }

    /**
     * @author hua
     * @date 2024/9/26 21:39
     * @apiNote 自定义权限管理器，默认转换器 JwtGrantedAuthoritiesConverter
     * 1. ServerHttpSecurity 没有将 jwt 中 authorities 的负载部分当做 Authentication。需要把 jwt 的 Claim 中的 authorities 加入
     **/
    @Bean
    public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> customJwtConverter() {
        // 创建 JwtGrantedAuthoritiesConverter，用于将 JWT 中的权限声明转换为 Spring Security 的 GrantedAuthority
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // 设置权限前缀，Spring Security 通常会为角色加上 "ROLE_" 前缀，这里手动指定前缀
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        // 指定 JWT 中用于存储权限声明的 Claim 名称，默认为 "authorities"
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        // 创建 JwtAuthenticationConverter，用于将 JWT 转换为 Spring Security 的 Authentication 对象
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        // 将自定义的权限转换器设置到 JwtAuthenticationConverter 中
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        // 使用 ReactiveJwtAuthenticationConverterAdapter 包装 JwtAuthenticationConverter，使其适应响应式编程模型
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }

    /**
     * @return java.security.interfaces.RSAPublicKey
     * @author hua
     * @date 2024/9/26 20:55
     * @apiNote 从本地 Resources 资源下读取公钥文件的内容，并转换为 RSAPublicKey
     **/
    @SneakyThrows
    @Bean
    public RSAPublicKey getRsaPublicKey() {
        // 生成 RSA 公钥: 最好是从文件里面读取，然后生成，但是我这里报错，后续处理
        return (RSAPublicKey) RSAUtils.getPublicKey(PUBLIC_KEY_CONTEXT);
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
        return (exchange, e) -> Mono.defer(() -> Mono.just(exchange.getResponse()))
                .flatMap(response -> WebFluxUtils.writeResponse(response, ResultEnum.TOKEN_INVALID_OR_EXPIRED));
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
        return (exchange, denied) -> Mono.defer(() -> Mono.just(exchange.getResponse()))
                .flatMap(response -> WebFluxUtils.writeResponse(response, ResultEnum.ACCESS_UNAUTHORIZED));
    }
}
