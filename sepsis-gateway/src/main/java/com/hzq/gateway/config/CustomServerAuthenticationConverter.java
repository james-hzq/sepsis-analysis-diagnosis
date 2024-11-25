package com.hzq.gateway.config;

import com.hzq.gateway.constant.TokenType;
import com.hzq.gateway.exception.TokenAuthenticationException;
import com.hzq.gateway.strategy.converter.TokenConverterStrategy;
import com.hzq.gateway.strategy.converter.TokenConverterStrategyFactory;
import com.hzq.security.constant.SecurityConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author gc
 * @class com.hzq.gateway.config CustomServerAuthenticationConverter
 * @date 2024/11/25 16:16
 * @description TODO
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomServerAuthenticationConverter implements ServerAuthenticationConverter {

    private final TokenConverterStrategyFactory tokenConverterStrategyFactory;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        // 从请求头中获取 token
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(SecurityConstants.REQUEST_HEAD_AUTHENTICATION))
                .filter(header -> header.startsWith(SecurityConstants.TOKEN_PREFIX))
                .map(header -> header.substring(SecurityConstants.TOKEN_PREFIX.length()))
                .filter(StringUtils::hasText)
                .flatMap(token -> {
                    try {
                        TokenType tokenType = TokenType.getTokenTypeFromAuthorizationHeader(token);
                        return tokenConverterStrategyFactory.getStrategy(tokenType).convert(token)
                                .doOnSuccess(auth -> log.info("Successfully converted token to authentication: {}", auth))
                                .doOnError(error -> log.error("Error converting token: {}", error.getMessage()));
                    } catch (Exception e) {
                        log.error("Error processing token: {}", e.getMessage());
                        return Mono.error(new TokenAuthenticationException("Failed to process token: " + e.getMessage()));
                    }
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.debug("No authentication token found in request");
                    return Mono.empty();
                }));
    }
}
