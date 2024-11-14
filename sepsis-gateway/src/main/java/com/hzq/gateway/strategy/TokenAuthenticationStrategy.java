package com.hzq.gateway.strategy;

import com.hzq.gateway.constant.TokenType;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

/**
 * @author gc
 * @interface com.hzq.gateway TokenAuthenticationStrategy
 * @date 2024/11/14 17:00
 * @description token 验证的策略接口
 */
public interface TokenAuthenticationStrategy {

    /**
     * @param token token 字符串
     * @return reactor.core.publisher.Mono<org.springframework.security.core.Authentication>
     * @author hzq
     * @date 2024/11/14 17:02
     * @apiNote 进行 Token 认证
     **/
    Mono<Authentication> authenticate(String token);

    /**
     * @param tokenType token类型
     * @param token 字符串
     * @return java.lang.String
     * @author hzq
     * @date 2024/11/14 17:05
     * @apiNote 根据 tokenType 为 token 字符串裁剪前缀
     **/
    default String trimTokenPrefix(TokenType tokenType, String token) {
        return token.substring(tokenType.getPrefix().length());
    }
}
