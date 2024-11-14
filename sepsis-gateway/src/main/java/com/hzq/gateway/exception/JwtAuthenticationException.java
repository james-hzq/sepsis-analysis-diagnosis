package com.hzq.gateway.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author gc
 * @class com.hzq.gateway.exception JwtValidationException
 * @date 2024/11/14 16:00
 * @description 处理JWT认证异常
 */
@Setter
@Getter
@ToString
public final class JwtAuthenticationException extends TokenAuthenticationException {

    public JwtAuthenticationException(String message) {
        super(message);
    }
}
