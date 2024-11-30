package com.hzq.auth.login.info;

import com.google.common.collect.ImmutableMap;
import com.hzq.web.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author hua
 * @className com.hzq.auth.login.info LoginUserInfoStrategyFactory
 * @date 2024/11/30 15:13
 * @description 用户信息策略工厂
 */
@Component
public class LoginUserInfoStrategyFactory {

    private final Map<TokenType, LoginUserInfoStrategy> loginUserInfoStrategyMap;

    @Autowired
    public LoginUserInfoStrategyFactory(List<LoginUserInfoStrategy> strategies) {
        this.loginUserInfoStrategyMap = strategies.stream()
                .collect(
                        ImmutableMap.toImmutableMap(
                                LoginUserInfoStrategy::getTokenType,
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
    public LoginUserInfoStrategy getStrategy(TokenType tokenType) {
        return loginUserInfoStrategyMap.get(tokenType);
    }
}
