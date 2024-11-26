package com.hzq.gateway.authentication;

import com.hzq.gateway.constant.AuthenticationType;
import com.hzq.gateway.exception.TokenAuthenticationException;
import com.hzq.gateway.strategy.authentication.TokenAuthenticationStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author gc
 * @class com.hzq.gateway.config CustomReactiveAuthenticationManager
 * @date 2024/11/25 15:08
 * @description 自定义网关认证管理器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public final class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    // 网关统一认证策略工厂
    private final TokenAuthenticationStrategyFactory tokenAuthenticationStrategyFactory;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono
                // 根据 authentication 获取所属的 authentication 类型
                .justOrEmpty(AuthenticationType.getAuthenticationType(authentication))
                // 处理 authentication type 为空
                .switchIfEmpty(Mono.error(new TokenAuthenticationException("No authentication strategy processing")))
                // 使用 token 认证工厂进行认证
                .flatMap(authenticationType -> tokenAuthenticationStrategyFactory.getStrategy(authenticationType).authenticate(authentication))
                // 捕获转换期间出现的异常并封装
                .onErrorResume(error -> Mono.error(new TokenAuthenticationException("Error authenticating: " + error.getMessage())));
    }
}
