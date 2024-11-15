package com.hzq.gateway.config;

import com.hzq.gateway.constant.AuthenticationType;
import com.hzq.gateway.strategy.authentication.TokenAuthenticationStrategy;
import com.hzq.gateway.strategy.authentication.TokenAuthenticationStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author gc
 * @class com.hzq.gateway.config CustomAuthenticationManager
 * @date 2024/11/15 11:02
 * @description TODO
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationManager implements ReactiveAuthenticationManager {

    private final TokenAuthenticationStrategyFactory tokenAuthenticationStrategyFactory;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        log.info("请求进入 CustomAuthenticationManager");
        AuthenticationType authenticationType = AuthenticationType.getAuthenticationType(authentication);
        TokenAuthenticationStrategy strategy = tokenAuthenticationStrategyFactory.getStrategy(authenticationType);
        return null;
    }
}
