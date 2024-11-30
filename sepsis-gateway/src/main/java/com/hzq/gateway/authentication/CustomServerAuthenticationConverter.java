package com.hzq.gateway.authentication;

import com.hzq.gateway.constant.TokenType;
import com.hzq.gateway.exception.TokenAuthenticationException;
import com.hzq.gateway.strategy.converter.TokenConverterStrategyFactory;
import com.hzq.security.constant.SecurityConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author gc
 * @class com.hzq.gateway.config CustomServerAuthenticationConverter
 * @date 2024/11/25 16:16
 * @description 自定义网关认证对象转换器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public final class CustomServerAuthenticationConverter implements ServerAuthenticationConverter {

    private final TokenConverterStrategyFactory tokenConverterStrategyFactory;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        // 记录日志，进入这里说明非白名单路径，需要走认证流程
        log.debug("Request {} Enter the custom gateway authentication object converter", exchange.getRequest().getURI());

        return extractTokenFromRequest(exchange)
                // 从 token 值获取 TokenType 并转换为 Authentication
                .flatMap(this::convertToken)
                // 捕获转换期间出现的异常并封装
                .onErrorResume(error -> Mono.error(new TokenAuthenticationException("Error converting token to authentication object: " + error.getMessage())));
    }

    /**
     * @param exchange HTTP 请求-响应交互的协定
     * @return reactor.core.publisher.Mono<java.lang.String>
     * @author hzq
     * @date 2024/11/26 10:03
     * @apiNote 从请求头中提取 token，并进行前缀裁剪
     **/
    private Mono<String> extractTokenFromRequest(ServerWebExchange exchange) {
        return Mono
                // 创建 Mono 对象。如果值存在（非 null）返回一个包含该值的 Mono；如果值为 null，返回一个空的 Mono（即不包含任何元素，只会完成信号）。
                .justOrEmpty(exchange.getRequest().getHeaders().getFirst(SecurityConstants.REQUEST_HEAD_AUTHENTICATION))
                // 检查 token 是否包含自定义前缀
                .filter(token -> token.startsWith(SecurityConstants.TOKEN_PREFIX))
                // 裁剪 token 前缀
                .map(token -> token.substring(SecurityConstants.TOKEN_PREFIX.length()))
                // 处理空值，即 Mono.empty()
                .switchIfEmpty(Mono.error(new TokenAuthenticationException("The token is empty or has no common prefix")));
    }

    /**
     * @param token 裁剪掉前缀的 token 值
     * @return reactor.core.publisher.Mono<org.springframework.security.core.Authentication>
     * @author hzq
     * @date 2024/11/26 10:21
     * @apiNote 处理 Token，将其转换为 Authentication 对象。
     **/
    private Mono<Authentication> convertToken(String token) {
        return Mono
                // 根据 token 值获取所属的 token 类型
                .justOrEmpty(TokenType.getTokenTypeFromAuthorizationHeader(token))
                // 使用 token 转换工厂将 token 转换为 Authentication 认证对象，用于后续认证
                .flatMap(tokenType -> tokenConverterStrategyFactory.getStrategy(tokenType).convert(token))
                // 处理空值，即 Mono.empty()
                .switchIfEmpty(Mono.error(new TokenAuthenticationException("Token type does not match")));
    }
}
