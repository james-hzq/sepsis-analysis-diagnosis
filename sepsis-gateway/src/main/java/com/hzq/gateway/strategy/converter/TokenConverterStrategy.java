package com.hzq.gateway.strategy.converter;

import com.hzq.gateway.constant.TokenType;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

/**
 * @author gc
 * @interface com.hzq.gateway TokenConverterStrategy
 * @date 2024/11/14 17:00
 * @description token 转换的策略接口
 */
public interface TokenConverterStrategy {

    /**
     * @author hua
     * @date 2024/11/15 0:12
     * @return com.hzq.gateway.constant.TokenType
     * @apiNote 获取当前转换策略锁认证的令牌类型
     **/
    TokenType getTokenType();

    /**
     * @param token token 字符串
     * @return reactor.core.publisher.Mono<org.springframework.security.core.Authentication>
     * @author hzq
     * @date 2024/11/14 17:02
     * @apiNote 将 token 字符串转换为转换为认证对象
     **/
    Mono<Authentication> convert(String token);
}
