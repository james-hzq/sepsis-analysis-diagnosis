package com.hzq.gateway.filter;

import com.hzq.gateway.exception.GatewayProcessingException;
import com.hzq.security.constant.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * @author hua
 * @className com.hzq.gateway.config AuthFilter
 * @date 2024/9/16 22:31
 * @description 网关认证过滤器，设置最高优先级执行，为请求执行的第一个过滤器，执行完毕将请求传递给资源服务器安全配置
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GatewayGlobalFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        log.info("Request {} enters the gateway global filter for routing forwarding", request.getURI());
        // 从网关安全上下文中获取 authentication对象。如果存在并且已经认证，那么将认证的用户信息添加到请求头，否则传递原始请求
        return ReactiveSecurityContextHolder.getContext()
                // 如果是白名单内的请求路径，那就是匿名请求，安全上下文中没有 SecurityContext，因此返回一个空的安全上下文，进行处理
                .defaultIfEmpty(SecurityContextHolder.createEmptyContext())
                // 进行路由转发
                .flatMap(securityContext -> routerForward(exchange, chain, securityContext))
                // 处理获取安全上下文失败的情况
                .onErrorResume(this::handleError);
    }

    /**
     * @return reactor.core.publisher.Mono<java.lang.Void>
     * @author hzq
     * @date 2024/11/26 17:35
     * @apiNote 路由转发
     **/
    private Mono<Void> routerForward(ServerWebExchange exchange, GatewayFilterChain chain, SecurityContext securityContext) {
        Authentication authentication = securityContext.getAuthentication();

        // 直接处理匿名请求
        if (authentication == null || !authentication.isAuthenticated()) {
            return chain.filter(exchange);
        }

        // 已认证用户，注入用户信息
        String username = authentication.getName();
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header(SecurityConstants.REQUEST_HEAD_USERNAME, username)
                .header(SecurityConstants.REQUEST_HEAD_ROLES, roles)
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    /**
     * @param error 异常
     * @return reactor.core.publisher.Mono<java.lang.Void>
     * @author hzq
     * @date 2024/11/26 16:18
     * @apiNote 网关过滤器异常处理
     **/
    private Mono<Void> handleError(Throwable error) {
        log.error("Gateway filter error", error);
        return Mono.error(new GatewayProcessingException("Gateway processing failed", error));
    }
}
