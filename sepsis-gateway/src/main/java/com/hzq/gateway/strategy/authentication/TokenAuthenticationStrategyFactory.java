package com.hzq.gateway.strategy.authentication;

import com.google.common.collect.ImmutableMap;
import com.hzq.gateway.constant.AuthenticationType;
import com.hzq.gateway.constant.TokenType;
import com.hzq.gateway.exception.TokenAuthenticationException;
import com.hzq.gateway.strategy.converter.TokenConverterStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author gc
 * @class com.hzq.gateway.strategy.authentication TokenAuthenticationStrategyFactory
 * @date 2024/11/15 12:15
 * @description TODO
 */
@Component
public class TokenAuthenticationStrategyFactory {

    private final Map<AuthenticationType, TokenAuthenticationStrategy> tokenStrategies;

    @Autowired
    public TokenAuthenticationStrategyFactory(List<TokenAuthenticationStrategy> strategies) {
        this.tokenStrategies = strategies.stream()
                .collect(
                        ImmutableMap.toImmutableMap(
                                TokenAuthenticationStrategy::getAuthenticationType,
                                Function.identity()
                        )
                );
    }

    /**
     * @author hua
     * @date 2024/11/15 0:09
     * @param authenticationType token 令牌类型
     * @return com.hzq.gateway.strategy.authentication.TokenAuthenticationStrategy
     * @apiNote 根据 token 令牌类型获取对应处理策略
     **/
    public TokenAuthenticationStrategy getStrategy(AuthenticationType authenticationType) {
        return Optional.ofNullable(tokenStrategies.get(authenticationType))
                .orElseThrow(() -> new TokenAuthenticationException("No strategy found for token authentication: " + authenticationType.getClazz()));
    }
}
