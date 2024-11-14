package com.hzq.gateway.config;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hzq.gateway.constant.TokenConstants;
import com.hzq.gateway.constant.TokenType;
import com.hzq.gateway.exception.TokenAuthenticationException;
import com.hzq.gateway.strategy.AccessTokenAuthenticationStrategy;
import com.hzq.gateway.strategy.JwtAuthenticationStrategy;
import com.hzq.gateway.strategy.TokenAuthenticationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

    private static final Map<TokenType, TokenAuthenticationStrategy> tokenStrategies =
            ImmutableMap.<TokenType, TokenAuthenticationStrategy>builder()
                    .put(TokenType.SYSTEM_LOGIN_JWT_TOKEN, new JwtAuthenticationStrategy())
                    .put(TokenType.OAUTH2_LOGIN_ACCESS_TOKEN, new AccessTokenAuthenticationStrategy())
                    .build();

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();

        return Mono
                // 从请求中获取 token，若不存在或为空则返回 Mono.empty()
                .justOrEmpty(getAuthorization(request))
                // 在流订阅时打印请求路径和方法, 表示请求进入认证转换器
                .doOnSubscribe(subscription -> log.info("Request entering authentication converter - request-path: {}, Method: {}", request.getURI().getPath(), request.getMethod()))
                // 如果 token 为 null 或空，返回错误，表示请求头中缺少或为空的 Authorization 参数
                .switchIfEmpty(Mono.error(new TokenAuthenticationException("The Authorization parameter in the request header does not exist or is empty")))
                // 对获取到的 token 进行处理
                .flatMap(token ->
                        // 获取 tokenType
                        Mono.justOrEmpty(TokenType.getTokenTypeFromAuthorizationHeader(token))
                                // 如果获取不到，则返回错误，表示 token 前缀不在系统可验证的范围内
                                .switchIfEmpty(Mono.error(new TokenAuthenticationException("The token prefix is not within the system's verifiable range")))
                                // 获取到 tokenType 后，选择认证策略并执行认证
                                .flatMap(tokenType -> {
                                    TokenAuthenticationStrategy strategy = tokenStrategies.get(tokenType);
                                    return strategy.authenticate(token);
                                })
                );
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
