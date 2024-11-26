package com.hzq.gateway.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author gc
 * @class com.hzq.gateway.handler CustomServerSuccessHandler
 * @date 2024/11/26 9:31
 * @description 自定义认证成功处理器
 */
@Slf4j
@Component
public final class CustomServerAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        log.debug("Authentication successful, enter the authentication success handler");
        ServerWebExchange exchange = webFilterExchange.getExchange();
        return webFilterExchange.getChain().filter(exchange);
    }
}
