package com.hzq.gateway.filter;

import com.hzq.gateway.constant.TokenConstants;
import com.hzq.core.result.ResultEnum;
import com.hzq.gateway.util.WebFluxUtils;
import com.hzq.security.constant.SecurityConstants;
import com.hzq.security.util.SecurityUtils;
import com.nimbusds.jose.JWSObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author hua
 * @className com.hzq.gateway.config AuthFilter
 * @date 2024/9/16 22:31
 * @description 网关认证过滤器，设置最高优先级执行，为请求执行的第一个过滤器，执行完毕将请求传递给资源服务器安全配置
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GatewayFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求 {} 进入全局过滤器，进行路由转发", request.getURI());

        ServerHttpRequest mutatedRequest = Optional.ofNullable(SecurityUtils.getAuthentication())
                .map(authentication -> exchange.getRequest()
                        .mutate()
                        .header(SecurityConstants.REQUEST_HEAD_USERNAME, SecurityUtils.getUsername(authentication))
                        .header(SecurityConstants.REQUEST_HEAD_ROLES, SecurityUtils.getRoleStr(authentication))
                        .build()
                )
                .orElse(exchange.getRequest());

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }
}
