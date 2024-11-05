package com.hzq.auth.handler;

import com.hzq.core.result.Result;
import com.hzq.web.exception.GlobalExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author gc
 * @class com.hzq.auth.handler AuthExceptionHandler
 * @date 2024/11/5 11:20
 * @description 授权服务全局错误处理器
 */
@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler extends GlobalExceptionHandler {

    /**
     * @author hua
     * @date 2024/8/24 12:24
     * @return com.hua.common.exception.Result<?>
     * @apiNote OAuth2 错误处理器
     **/
    @ExceptionHandler(OAuth2AuthenticationException.class)
    public Result<?> handleAccessDeniedException(OAuth2AuthenticationException e, HttpServletRequest request) {
        OAuth2Error error = e.getError();
        String errMessage = error.getErrorCode() + ": " + error.getDescription();
        log.error("请求地址'{}', 发送错误: {}", request.getRequestURI(), errMessage);
        return Result.error(Result.error(errMessage));
    }
}
