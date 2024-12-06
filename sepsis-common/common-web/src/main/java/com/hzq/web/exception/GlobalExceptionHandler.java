package com.hzq.web.exception;

import com.hzq.core.result.Result;
import com.hzq.core.result.ResultEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hua
 * @date 2023/11/28
 * @apiNote 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @author hua
     * @date 2024/8/24 12:24
     * @return com.hua.common.exception.Result<?>
     * @apiNote 请求方法错误处理器
     **/
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public Result<?> httpRequestMethodNotSupportHandler(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("请求地址'{}',不支持'{}'请求", request.getRequestURI(), e.getMethod());
        return Result.error(ResultEnum.BAD_REQUEST_METHOD);
    }

    /**
     * @return com.hzq.common.exception.Result<?>
     * @author gc
     * @date 2024/8/30 16:29
     * @apiNote
     * 1. 当 Spring MVC 控制器无法反序列化 HTTP 请求消息反序列化会抛出 HttpMessageNotReadableException 异常
     * 2. 当 Spring MVC 控制器无法序列化 HTTP 响应消息时会抛出此抛出 HttpMessageNotWritableException 异常。
     **/
    @ExceptionHandler(value = {HttpMessageNotReadableException.class, HttpMessageNotWritableException.class})
    public Result<?> httpMessageConversionExceptionHandler(HttpMessageConversionException e, HttpServletRequest request) {
        if (e instanceof HttpMessageNotReadableException) {
            log.error("请求地址'{}', 发生请求消息反序列化异常: {}", request.getRequestURI(), e.getMessage());
            return Result.error(ResultEnum.BAD_REQUEST_FORMAT);
        }
        if (e instanceof HttpMessageNotWritableException) {
            log.error("请求地址'{}', 发生响应消息序列化异常: {}", request.getRequestURI(), e.getMessage());
            return Result.error(ResultEnum.BAD_RESPONSE_FORMAT);
        }
        return Result.error(ResultEnum.SERVER_ERROR);
    }

    /**
     * @author gc
     * @return com.hzq.common.exception.Result<?>
     * @date 2024/8/29 17:51
     * @apiNote 参数校验异常处理器 (@Validated @Valid)
     **/
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class})
    public Result<?> bindExceptionHandler(BindException e, HttpServletRequest request) {
        // 获取错误字段和信息
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String msg = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(";"));
        log.error("请求地址'{}', 发生方法参数校验异常, 异常信息'{}'", request.getRequestURI(), msg);
        return Result.error(ResultEnum.CUSTOM_ERROR.getCode(), msg);
    }

    /**
     * @author gc
     * @return com.hzq.common.exception.Result<?>
     * @date 2024/8/29 17:51
     * @apiNote 参数校验异常处理器 (@NotBlank @NotNull @NotEmpty)
     **/
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result<?> constraintViolationHandler(ConstraintViolationException e, HttpServletRequest request) {
        // 获取错误字段和信息
        Set<ConstraintViolation<?>> constraintViolationSet = e.getConstraintViolations();
        String msg = constraintViolationSet.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(";"));
        log.error("请求地址'{}', 发生字段参数校验异常'{}'", request.getRequestURI(), msg);
        return Result.error(ResultEnum.CUSTOM_ERROR.getCode(), msg);
    }

    /**
     * @author hua
     * @date 2024/8/24 12:24
     * @return com.hua.common.exception.Result<?>
     * @apiNote 全局自定义业务异常处理器
     **/
    @ExceptionHandler(value = SystemException.class)
    public Result<?> SystemExceptionHandler(SystemException e, HttpServletRequest request) {
        log.error(e.getMessage(), request.getRequestURI(), e);
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * @author hua
     * @date 2024/8/24 12:24
     * @return com.hua.common.exception.Result<?>
     * @apiNote 运行时异常处理器
     **/
    @ExceptionHandler(value = RuntimeException.class)
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
    @ExceptionHandler(value = Exception.class)
    public Result<?> exceptionHandler(Exception e, HttpServletRequest request) {
        log.error("请求地址'{}',发生系统异常.", request.getRequestURI(), e);
        return Result.error(e.getMessage());
    }
}
