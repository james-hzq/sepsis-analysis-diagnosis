package com.hzq.gateway.config;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.util.List;


/**
 * @author gc
 * @class com.hzq.gateway.config CustomAuthorizationManager
 * @date 2024/11/14 13:46
 * @description 鉴权管理器，用于判断是否有资源的访问权限
 */
@Slf4j
@Component
public class CustomAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private static final List<String> whitePathList = Lists.newArrayList(

    );

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        String requestPath = request.getURI().getPath();
        // 记录日志
        log.info("Request entering authorization manager - request-path: {}, Method: {}", request.getURI().getPath(), request.getMethod());

        // 授权白名单路径直接放行
        if (isWhiteListPath(requestPath)) {
            return Mono.just(new AuthorizationDecision(true));
        }

        // 非白名单路径需要鉴权
        return authentication
                .filter(Authentication::isAuthenticated)
                .flatMap(auth -> checkAuthorities(auth, requestPath))
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

    private Mono<AuthorizationDecision> checkAuthorities(Authentication authentication, String path) {
        return Mono.defer(() -> {

            return Mono.just(new AuthorizationDecision(false));
        });
    }

    private boolean isWhiteListPath(String path) {
        return whitePathList.stream()
                .anyMatch(whitePath ->
                        PathPatternParser.defaultInstance
                                .parse(whitePath)
                                .matches(PathContainer.parsePath(path))
                );
    }

    public void updateWhitePathList(List<String> newWhitePathList) {
        if (!CollectionUtils.isEmpty(newWhitePathList)) {
            synchronized (whitePathList) {
                whitePathList.clear();
                whitePathList.addAll(newWhitePathList);
            }
            log.info("the AuthorizationManager-White-Path-List is updated: {}", whitePathList);
        }
    }
}
