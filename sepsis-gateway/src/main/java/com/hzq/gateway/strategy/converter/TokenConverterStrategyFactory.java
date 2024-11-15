package com.hzq.gateway.strategy.converter;

import com.google.common.collect.ImmutableMap;
import com.hzq.gateway.constant.TokenType;
import com.hzq.gateway.exception.TokenAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author hua
 * @className com.hzq.gateway.strategy TokenConverterStrategyFactory
 * @date 2024/11/15 0:01
 * @description Token认证策略工厂
 */
@Component
public class TokenConverterStrategyFactory {

    private final Map<TokenType, TokenConverterStrategy> tokenStrategies;

    @Autowired
    public TokenConverterStrategyFactory(List<TokenConverterStrategy> strategies) {
        this.tokenStrategies = strategies.stream()
                .collect(
                        ImmutableMap.toImmutableMap(
                                TokenConverterStrategy::getTokenType,
                                Function.identity()
                        )
                );
    }

    /**
     * @author hua
     * @date 2024/11/15 0:09
     * @param tokenType token 令牌类型
     * @return com.hzq.gateway.strategy.authentication.TokenAuthenticationStrategy
     * @apiNote 根据 token 令牌类型获取对应处理策略
     **/
    public TokenConverterStrategy getStrategy(TokenType tokenType) {
        return Optional.ofNullable(tokenStrategies.get(tokenType))
                .orElseThrow(() -> new TokenAuthenticationException("No strategy found for token type: " + tokenType.getPrefix()));
    }
}
