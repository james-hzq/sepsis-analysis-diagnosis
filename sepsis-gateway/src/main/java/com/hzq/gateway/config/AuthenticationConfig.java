package com.hzq.gateway.config;

import com.hzq.core.util.RSAUtils;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.security.interfaces.RSAPublicKey;

/**
 * @author gc
 * @class com.hzq.gateway.config JWTConfig
 * @date 2024/11/14 15:20
 * @description 网关统一认证的配置
 */
@Configuration
public class AuthenticationConfig {

    /**
     * @return ReactiveJwtDecoder
     * @author hua
     * @date 2024/10/04
     * @apiNote 配置 Reactive JWT 解码器
     **/
    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() throws Exception {
        return NimbusReactiveJwtDecoder
                .withPublicKey((RSAPublicKey) RSAUtils.getPublicKey(RSAUtils.PUBLIC_KEY_CONTEXT))
                .build();
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
}
