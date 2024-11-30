package com.hzq.gateway.strategy.authentication;

import com.hzq.gateway.constant.AuthenticationType;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * @author gc
 * @class com.hzq.gateway.strategy TokenAuthenticationStrategy
 * @date 2024/11/15 11:07
 * @description token 认证（校验）的策略接口
 */
public interface TokenAuthenticationStrategy {

    /**
     * @author hua
     * @date 2024/11/15 0:12
     * @return com.hzq.gateway.constant.TokenType
     * @apiNote 获取当前认证策略锁认证的令牌类型
     **/
    AuthenticationType getAuthenticationType();

    /**
     * @param authentication 认证对象
     * @return reactor.core.publisher.Mono<java.lang.Void>
     * @author hzq
     * @date 2024/11/15 11:43
     * @apiNote 校验认证对象（验签）
     **/
    Mono<Authentication> authenticate(Authentication authentication);

    /**
     * @author hua
     * @date 2024/11/30 17:27
     * @param issuedAt 颁发时间
     * @param expiresAt 过期时间
     * @return boolean
     * @apiNote 验证 TOKEN 是否过期
     **/
    default boolean isTokenValid(Instant issuedAt, Instant expiresAt) {
        if (issuedAt == null || expiresAt == null) {
            return false;
        }
        return Instant.now().isBefore(expiresAt);
    }

    /**
     * @author hua
     * @date 2024/11/30 21:04
     * @param issuedAt 颁发时间
     * @param expiresAt 过期时间
     * @return boolean
     * @apiNote 验证 TOKEN 是否未过期
     **/
    default boolean isTokenNotValid(Instant issuedAt, Instant expiresAt) {
        return !isTokenValid(issuedAt, expiresAt);
    }
}
