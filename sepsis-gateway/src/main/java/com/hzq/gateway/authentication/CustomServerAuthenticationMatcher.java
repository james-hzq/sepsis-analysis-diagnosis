package com.hzq.gateway.authentication;

import com.hzq.gateway.config.GatewaySecurityProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author gc
 * @class com.hzq.gateway.authentication CustomServerAuthenticationMatcher
 * @date 2024/11/26 12:24
 * @description 自定义认证过滤器，路过过滤规则
 */
@Slf4j
@Component
@RequiredArgsConstructor
public final class CustomServerAuthenticationMatcher implements ServerWebExchangeMatcher {

    private final GatewaySecurityProperties gatewaySecurityProperties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        // 获取请求路径
        String path = exchange.getRequest().getPath().toString();
        // 获取白名单集合
        List<String> whiteUriPath = gatewaySecurityProperties.getWhiteUriList();
        // 判断该路径是否在白名单内
        boolean isWhitePathPattern = !whiteUriPath.isEmpty() && whiteUriPath
                .stream()
                .anyMatch(whitePath -> antPathMatcher.match(whitePath, path));
        // 在白名单内的放行不需要认证，表现为路径不匹配，不走过滤器。不在白名单内的需要认证，表现为路径匹配，走过滤器
        return isWhitePathPattern ? ServerWebExchangeMatcher.MatchResult.notMatch() : ServerWebExchangeMatcher.MatchResult.match();
    }
}
