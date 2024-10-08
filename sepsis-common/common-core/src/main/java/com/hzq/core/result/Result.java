package com.hzq.core.result;

import lombok.Data;

/**
 * @author hua
 * @className com.hua.common.exception Result
 * @date 2024/8/24 10:32
 * @description 通用结果集封装
 */
@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public Result() {}

    public Result(ResultEnum resultEnum) {
        this(resultEnum.getCode(), resultEnum.getMessage(), null);
    }

    public Result(ResultEnum resultEnum, T data) {
        this(resultEnum.getCode(), resultEnum.getMessage(), data);
    }

    public Result(int code, String message) {
        this(code, message, null);
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success() {
        return new Result<>(ResultEnum.OPERATION_SUCCESS);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultEnum.OPERATION_SUCCESS, data);
    }

    public static <T> Result<T> success(ResultEnum resultEnum) {
        return new Result<>(resultEnum);
    }

    public static <T> Result<T> success(ResultEnum resultEnum, T data) {
        return new Result<>(resultEnum, data);
    }

    public static <T> Result<T> success(int code, String msg) {
        return new Result<>(code, msg);
    }

    public static <T> Result<T> success(int code, String msg, T data) {
        return new Result<>(code, msg, data);
    }

    public static <T> Result<T> error() {
        return new Result<>(ResultEnum.SERVER_ERROR);
    }

    public static <T> Result<T> error(T data) {
        return new Result<>(ResultEnum.SERVER_ERROR, data);
    }

    public static <T> Result<T> error(ResultEnum resultEnum) {
        return new Result<>(resultEnum);
    }

    public static <T> Result<T> error(ResultEnum resultEnum, T data) {
        return new Result<>(resultEnum, data);
    }

    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg);
    }

    public static <T> Result<T> error(int code, String msg, T data) {
        return new Result<>(code, msg, data);
    }
}
