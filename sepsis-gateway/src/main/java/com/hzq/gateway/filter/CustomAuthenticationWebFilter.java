package com.hzq.gateway.filter;

import com.hzq.gateway.config.GatewaySecurityProperties;
import com.hzq.gateway.constant.AuthenticationType;
import com.hzq.gateway.constant.TokenConstants;
import com.hzq.gateway.constant.TokenType;
import com.hzq.gateway.exception.TokenAuthenticationException;
import com.hzq.gateway.strategy.authentication.TokenAuthenticationStrategyFactory;
import com.hzq.gateway.strategy.converter.TokenConverterStrategyFactory;
import com.hzq.security.constant.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hua
 * @className com.hzq.gateway.filter AuthenticationWebFilter
 * @date 2024/11/17 9:46
 * @description 自定义认证过滤器
 */
@Slf4j
@Component
public class CustomAuthenticationWebFilter implements WebFilter {

    private final GatewaySecurityProperties gatewaySecurityProperties;
    private final TokenConverterStrategyFactory tokenConverterStrategyFactory;
    private final TokenAuthenticationStrategyFactory tokenAuthenticationStrategyFactory;
    private final ServerWebExchangeMatcher requiresAuthenticationMatcher;
    private final AntPathMatcher matcher = new AntPathMatcher();

    public CustomAuthenticationWebFilter(
            GatewaySecurityProperties gatewaySecurityProperties,
            TokenConverterStrategyFactory tokenConverterStrategyFactory,
            TokenAuthenticationStrategyFactory tokenAuthenticationStrategyFactory
    ) {
        this.gatewaySecurityProperties = gatewaySecurityProperties;
        this.tokenConverterStrategyFactory = tokenConverterStrategyFactory;
        this.tokenAuthenticationStrategyFactory = tokenAuthenticationStrategyFactory;
        this.requiresAuthenticationMatcher = this::matchesRequest;
    }

    /**
     * @author hua
     * @date 2024/11/17 10:03
     * @param exchange 传入的 Http 封装
     * @return reactor.core.publisher.Mono<org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult>
     * @apiNote 检查请求是否需要认证
     **/
    private Mono<ServerWebExchangeMatcher.MatchResult> matchesRequest(ServerWebExchange exchange) {
        // 获取请求路径
        String path = exchange.getRequest().getPath().value();

        // 检查路径是否在白名单中
        boolean isWhitelisted = gatewaySecurityProperties.getWhiteUriList()
                .stream()
                .anyMatch(pattern -> matcher.match(pattern, path));

        return isWhitelisted ? ServerWebExchangeMatcher.MatchResult.notMatch() : ServerWebExchangeMatcher.MatchResult.match();
    }

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        return this.requiresAuthenticationMatcher.matches(exchange)
                // 如果路径匹配上了白名单路径，那么进行 token 转换和认证，并将认证成功的信息加到请求头，否则直接走过滤器链
                .flatMap(matchResult -> matchResult.isMatch() ?
                        convert(exchange)
                                .flatMap(this::authenticate)
                                .then(chain.filter(exchange)) :
                        chain.filter(exchange)
                )
                .onErrorResume(AuthenticationException.class, e -> {
                    log.error("Authentication failed: {}", e.getMessage());
                    return Mono.error(e);
                });
    }


    /**
     * @author hua
     * @date 2024/11/17 10:47
     * @param exchange 传入的 Http 封装
     * @return reactor.core.publisher.Mono<org.springframework.security.core.Authentication>
     * @apiNote 提取请求头中的 token 字符串，并且转换为认证对象
     **/
    private Mono<Authentication> convert(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        // 从请求头获取 token 值
        String token = Optional.ofNullable(getAuthorization(request))
                .orElseThrow(() -> new TokenAuthenticationException("token is null or empty"));
        // 根据 token 前缀匹配 token 类型
        TokenType tokenType  = Optional.ofNullable(TokenType.getTokenTypeFromAuthorizationHeader(token))
                .orElseThrow(() -> new TokenAuthenticationException("tokeType is null or empty"));
        // 根据 token 转换策略将 token string 转换为 Authentication 对象
        return this.tokenConverterStrategyFactory.getStrategy(tokenType).convert(token);
    }

    /**
     * @author hua
     * @date 2024/11/17 10:54
     * @param authentication 认证对象
     * @return reactor.core.publisher.Mono<java.lang.Void>
     * @apiNote 根据 token 认证策略认证 Authentication 对象
     **/
    private Mono<Void> authenticate(Authentication authentication) {
        // 根据 Authentication 对象匹配 AuthenticationType
        AuthenticationType authenticationType = Optional.ofNullable(AuthenticationType.getAuthenticationType(authentication))
                .orElseThrow(() -> new TokenAuthenticationException("authenticationType is null or empty"));
        // 根据 token 认证策略认证 Authentication 对象
        return this.tokenAuthenticationStrategyFactory.getStrategy(authenticationType).authenticate(authentication);
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
            return null;
        }
        // 裁剪前缀
        if (token.startsWith(TokenConstants.TOKEN_PREFIX)) {
            token = token.replaceFirst(TokenConstants.TOKEN_PREFIX, "");
        }
        return token;
    }
}
