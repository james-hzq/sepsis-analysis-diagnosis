package com.hzq.gateway.filter;

import com.hzq.gateway.config.GatewaySecurityProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.util.context.ContextView;

import java.util.List;

/**
 * @author gc
 * @class com.hzq.gateway.filter CustomSecurityContextWebFilter
 * @date 2024/11/15 16:31
 * @description TODO
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomSecurityContextWebFilter implements WebFilter {

    private final ServerSecurityContextRepository repository;
    private final GatewaySecurityProperties gatewaySecurityProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        log.info("Request entering CustomSecurityContextWebFilter - request-path: {}, Method: {}", request.getURI().getPath(), request.getMethod());

        // 使用 deferContextual 来确保在当前上下文中处理 SecurityContext
        return Mono.deferContextual(context -> {
            // 检查当前上下文是否已经存在 SecurityContext
            if (!context.hasKey(SecurityContext.class)) {
                // 如果没有 SecurityContext，则根据路径判断是否是白名单路径
                if (isWhiteListed(gatewaySecurityProperties.getWhiteUriList(), exchange)) {
                    // 如果是白名单路径，直接创建一个空的 SecurityContext
                    SecurityContext securityContext = createWhiteListSecurityContext();
                    // 更新上下文并传递给过滤器链
                    return chain.filter(exchange)
                            .contextWrite(Context.of(SecurityContext.class, securityContext)) // 正确传递新的上下文
                            .doOnTerminate(() -> log.info("Filter terminated for white-list path: {}", request.getURI().getPath()));
                } else {
                    // 否则，加载正常的 SecurityContext 并继续执行
                    return this.repository.load(exchange)
                            .doOnNext(securityContext -> {
                                log.info("Loaded SecurityContext from repository for path: {}", exchange.getRequest().getURI().getPath());
                            })
                            .flatMap(securityContext -> {
                                // 创建一个新的 Context 包含加载的 SecurityContext
                                Context newContext = Context.of(SecurityContext.class, securityContext);
                                // 更新上下文并传递给过滤器链
                                return chain.filter(exchange)
                                        .contextWrite(newContext); // 直接传递新的上下文
                            })
                            .doOnTerminate(() -> log.info("Filter terminated for non-white-list path: {}", request.getURI().getPath()));
                }
            } else {
                // 如果上下文中已有 SecurityContext，直接继续执行过滤器链
                return chain.filter(exchange)
                        .doOnTerminate(() -> log.info("Filter terminated, final state: {}", request.getURI().getPath()));
            }
        });
    }

    private boolean isWhiteListed(List<String> whiteUrlList, ServerWebExchange exchange) {
        return whiteUrlList.stream().anyMatch(whiteUri -> exchange.getRequest().getURI().getPath().startsWith(whiteUri.replace("/**", "")));
    }

    private SecurityContext createWhiteListSecurityContext() {
        // 创建一个包含白名单权限的 SecurityContext
        return new SecurityContextImpl(
                new PreAuthenticatedAuthenticationToken(
                        "whiteList",
                        null,
                        AuthorityUtils.createAuthorityList())
        );
    }
}
