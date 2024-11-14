package com.hzq.gateway.exception;

import com.hzq.core.result.ResultEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.AuthenticationException;

/**
 * @author gc
 * @class com.hzq.gateway.exception CustomAuthenticationException
 * @date 2024/11/14 15:57
 * @description 自定义Token认证过程出现的异常（基类）
 */
@Setter
@Getter
@ToString
public class TokenAuthenticationException extends AuthenticationException {
    public TokenAuthenticationException(String message) {
        super(message);
    }
}
