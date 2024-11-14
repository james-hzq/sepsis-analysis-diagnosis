package com.hzq.gateway.config;

import com.hzq.gateway.constant.TokenConstants;
import com.hzq.gateway.constant.TokenType;
import com.hzq.gateway.exception.TokenAuthenticationException;
import com.hzq.gateway.strategy.TokenAuthenticationStrategy;
import com.hzq.gateway.strategy.TokenAuthenticationStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author gc
 * @class com.hzq.gateway.config CustomTokenAuthenticationConverter
 * @date 2024/11/14 15:17
 * @description 自定义Token转换器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationConverter implements ServerAuthenticationConverter {

    private final TokenAuthenticationStrategyFactory tokenAuthenticationStrategyFactory;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        log.info("Request entering authentication converter - request-path: {}, Method: {}", request.getURI().getPath(), request.getMethod());

        return Mono
                // 从请求中获取 token，若不存在或为空则返回 Mono.empty()
                .justOrEmpty(getAuthorization(request))
                // 如果 token 为 null 或空，返回错误，表示请求头中缺少或为空的 Authorization 参数
                .switchIfEmpty(Mono.error(new TokenAuthenticationException("The Authorization parameter in the request header does not exist or is empty")))
                // 对获取到的 token 进行处理
                .flatMap(token -> Mono
                        // 根据Token前缀判断是否可以进行Token认证
                        .justOrEmpty(TokenType.getTokenTypeFromAuthorizationHeader(token))
                        // 如果获取不到，则返回错误，表示 token 前缀不在系统可验证的范围内
                        .switchIfEmpty(Mono.error(new TokenAuthenticationException("The token prefix is not within the system's verifiable range")))
                        // 获取到 tokenType 后，选择认证策略并执行认证
                        .flatMap(tokenType -> {
                            TokenAuthenticationStrategy strategy = tokenAuthenticationStrategyFactory.getStrategy(tokenType);
                            return strategy.authenticate(token);
                        })
                )
                .onErrorResume(TokenAuthenticationException.class, ex -> {
                    // Customize error response based on the exception
                    log.error("Token Authentication error: {}", ex.getMessage(), ex);
                    return Mono.error(new TokenAuthenticationException("Custom message for authentication error: " + ex.getMessage()));
                });
    }

    /**
     * @author hua
     * @date 2024/9/22 18:43
     * @param request http请求
     * @return java.lang.String
     * @apiNote 获取请求头中的 Authorization 参数的 token 值
     **/
    private String getAuthorization(ServerHttpRequest request) {
        // 从请求头中获取 token
        String token = request.getHeaders().getFirst(TokenConstants.AUTHENTICATION);
        // 如果 token 为空，则返回空字符串。
        if (!StringUtils.hasText(token)) {
            return "";
        }
        // 裁剪前缀
        if (token.startsWith(TokenConstants.TOKEN_PREFIX)) {
            token = token.replaceFirst(TokenConstants.TOKEN_PREFIX, "");
        }
        return token;
    }
}
