package com.hzq.gateway.authentication;

import com.hzq.core.result.ResultEnum;
import com.hzq.gateway.util.WebFluxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author gc
 * @class com.hzq.gateway.handler CustomServerAuthenticationFailureHandler
 * @date 2024/11/26 9:37
 * @description 自定义认证失败处理器
 */
@Slf4j
@Component
public final class CustomServerAuthenticationFailureHandler implements ServerAuthenticationFailureHandler {

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        log.error("Authentication failed, enter the authentication failure handler, [ {} ]", exception.getMessage());
        return Mono.defer(() -> WebFluxUtils.writeResponse(
                webFilterExchange.getExchange().getResponse(),
                ResultEnum.TOKEN_INVALID_OR_EXPIRED,
                exception.getMessage()
        ));
    }
}
