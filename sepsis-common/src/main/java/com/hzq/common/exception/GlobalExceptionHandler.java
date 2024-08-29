package com.hzq.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author hua
 * @date 2023/11/28
 * @apiNote 全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * @author hua
     * @date 2024/8/24 12:24
     * @return com.hua.common.exception.Result<?>
     * @apiNote 请求方法错误处理器
     **/
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<?> httpRequestMethodNotSupportHandler(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("请求地址'{}',不支持'{}'请求", request.getRequestURI(), e.getMethod());
        return Result.error(ResultEnum.BAD_REQUEST);
    }


    /**
     * @author hua
     * @date 2024/8/24 12:24
     * @return com.hua.common.exception.Result<?>
     * @apiNote 全局自定义异常处理器
     **/
    @ExceptionHandler(SystemException.class)
    public Result<?> SystemExceptionHandler(SystemException e, HttpServletRequest request) {
        log.error(e.getMsg(), request.getRequestURI(), e);
        return Result.error(e.getCode(), e.getMsg());
    }

    /**
     * @author hua
     * @date 2024/8/24 12:24
     * @return com.hua.common.exception.Result<?>
     * @apiNote 运行时异常处理器
     **/
    @ExceptionHandler(RuntimeException.class)
    public Result<?> runtimeExceptionHandler(RuntimeException e, HttpServletRequest request) {
        log.error("请求地址'{}',发生未知异常.", request.getRequestURI(), e);
        return Result.error(e.getMessage());
    }

    /**
     * @author hua
     * @date 2024/8/24 12:24
     * @return com.hua.common.exception.Result<?>
     * @apiNote 系统异常处理器
     **/
    @ExceptionHandler(Exception.class)
    public Result<?> exceptionHandler(Exception e, HttpServletRequest request) {
        log.error("请求地址'{}',发生系统异常.", request.getRequestURI(), e);
        return Result.error(e.getMessage());
    }
}
