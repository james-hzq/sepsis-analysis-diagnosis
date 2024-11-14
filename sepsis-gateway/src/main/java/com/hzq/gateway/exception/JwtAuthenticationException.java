package com.hzq.gateway.exception;

/**
 * @author gc
 * @class com.hzq.gateway.exception JwtValidationException
 * @date 2024/11/14 16:00
 * @description 处理JWT认证异常
 */
public final class JwtAuthenticationException extends TokenAuthenticationException {

    public JwtAuthenticationException(String message) {
        super(message);
    }
}
