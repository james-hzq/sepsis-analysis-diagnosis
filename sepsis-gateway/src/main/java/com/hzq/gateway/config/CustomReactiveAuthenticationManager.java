package com.hzq.gateway.config;

import com.hzq.gateway.constant.AuthenticationType;
import com.hzq.gateway.exception.TokenAuthenticationException;
import com.hzq.gateway.strategy.authentication.TokenAuthenticationStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author gc
 * @class com.hzq.gateway.config CustomReactiveAuthenticationManager
 * @date 2024/11/25 15:08
 * @description TODO
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final TokenAuthenticationStrategyFactory tokenAuthenticationStrategyFactory;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .flatMap(auth -> {
                    AuthenticationType authenticationType = Optional.ofNullable(
                            AuthenticationType.getAuthenticationType(authentication)
                    ).orElseThrow(() -> new TokenAuthenticationException("authenticationType is null or empty"));

                    return tokenAuthenticationStrategyFactory.getStrategy(authenticationType)
                                .authenticate(authentication);
                })
                .doOnSuccess(auth -> log.debug("Authentication manager completed: {}", auth))
                .doOnError(error -> log.error("Authentication manager error: {}", error.getMessage()));
    }
}
