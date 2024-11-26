package com.hzq.gateway.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.stereotype.Component;

/**
 * @author hua
 * @className com.hzq.gateway.filter AuthenticationWebFilter
 * @date 2024/11/17 9:46
 * @description 自定义认证过滤器, 替换网关原有认证过滤器
 */
@Slf4j
@Component
public final class CustomAuthenticationWebFilter extends AuthenticationWebFilter {

    public CustomAuthenticationWebFilter(
            ReactiveAuthenticationManager authenticationManager,
            ServerWebExchangeMatcher serverWebExchangeMatcher,
            ServerAuthenticationConverter serverAuthenticationConverter,
            ServerAuthenticationSuccessHandler serverAuthenticationSuccessHandler,
            ServerAuthenticationFailureHandler serverAuthenticationFailureHandler
    ) {
        super(authenticationManager);

        // 设置认证过滤路径规则
        setRequiresAuthenticationMatcher(serverWebExchangeMatcher);

        // 设置认证转换器
        setServerAuthenticationConverter(serverAuthenticationConverter);

        // 设置认证成功处理器
        setAuthenticationSuccessHandler(serverAuthenticationSuccessHandler);

        // 设置认证失败处理器
        setAuthenticationFailureHandler(serverAuthenticationFailureHandler);
    }
}
