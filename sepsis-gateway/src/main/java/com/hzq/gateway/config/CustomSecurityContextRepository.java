package com.hzq.gateway.config;

import com.hzq.gateway.constant.TokenConstants;
import com.hzq.gateway.constant.TokenType;
import com.hzq.gateway.strategy.converter.TokenConverterStrategy;
import com.hzq.gateway.strategy.converter.TokenConverterStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

/**
 * @author gc
 * @class com.hzq.gateway.config CustomSecurityContextRepository
 * @date 2024/11/15 14:56
 * @description TODO
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomSecurityContextRepository implements ServerSecurityContextRepository {

    private final GatewaySecurityProperties gatewaySecurityProperties;
    private final CustomAuthenticationManager customAuthenticationManager;
    private final TokenConverterStrategyFactory tokenConverterStrategyFactory;


    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        log.info("Request entering CustomSecurityContextRepository - request-path: {}, Method: {}", request.getURI().getPath(), request.getMethod());

        if (isWhiteListed(gatewaySecurityProperties.getWhiteUriList(), request.getURI().getPath())) {
            log.info("White list path: {}, creating white list authentication", request.getURI().getPath());
            // 为白名单路径创建一个匿名认证的 SecurityContext
            // 创建一个带有特定权限的认证
            Authentication authentication = new PreAuthenticatedAuthenticationToken(
                    "whiteList",
                    null,
                    AuthorityUtils.createAuthorityList("ROLE_WHITE_LIST", "WHITE_LIST_ACCESS")
            );
            authentication.setAuthenticated(true);

            SecurityContext securityContext = new SecurityContextImpl(authentication);
            return Mono.just(securityContext);
        }

        String token = getAuthorization(request);

        if (token.isEmpty()) {
            return Mono.empty();
        }
        TokenType tokenType = TokenType.getTokenTypeFromAuthorizationHeader(token);
        TokenConverterStrategy strategy = tokenConverterStrategyFactory.getStrategy(tokenType);

        return strategy.convert(token)
                .flatMap(authToken -> customAuthenticationManager.authenticate(authToken)
                        .map((Function<Authentication, SecurityContext>) SecurityContextImpl::new));
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

    private boolean isWhiteListed(List<String> whiteUrlList, String requestPath) {
        return whiteUrlList.stream().anyMatch(whiteUri -> requestPath.startsWith(whiteUri.replace("/**", "")));
    }
}
