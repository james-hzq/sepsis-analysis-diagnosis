package com.hzq.gateway.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author gc
 * @class com.hzq.gateway.exception GatewayProcessingException
 * @date 2024/11/26 16:17
 * @description 网关异常
 */
@Setter
@Getter
@ToString
public class GatewayProcessingException extends RuntimeException {

    public GatewayProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
