package com.hzq.gateway.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author gc
 * @class com.hzq.gateway.exception AccessTokenAuthenticationException
 * @date 2024/11/14 16:16
 * @description 处理Access_token认证异常
 */
@Setter
@Getter
@ToString
public final class AccessTokenAuthenticationException extends TokenAuthenticationException {

    public AccessTokenAuthenticationException(String message) {
        super(message);
    }
}
