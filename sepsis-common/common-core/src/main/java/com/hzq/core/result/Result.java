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
    private String msg;
    private T data;

    public Result(ResultEnum resultEnum) {
        this(resultEnum.getCode(), resultEnum.getMsg(), null);
    }

    public Result(ResultEnum resultEnum, T data) {
        this(resultEnum.getCode(), resultEnum.getMsg(), data);
    }

    public Result(int code, String msg) {
        this(code, msg, null);
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result<?> success() {
        return new Result<>(ResultEnum.SUCCESS);
    }

    public static <T> Result<?> success(T data) {
        return new Result<>(ResultEnum.SUCCESS, data);
    }

    public static <T> Result<?> success(String msg) {
        return new Result<>(ResultEnum.SUCCESS.getCode(), msg);
    }

    public static <T> Result<?> success(String msg, T data) {
        return new Result<>(ResultEnum.SUCCESS.getCode(), msg, data);
    }

    public static Result<?> error(ResultEnum resultEnum) {
        return new Result<>(resultEnum);
    }

    public static <T> Result<?> error(ResultEnum resultEnum, T data) {
        return new Result<>(resultEnum, data);
    }

    public static Result<?> error(int code, String msg) {
        return new Result<>(code, msg);
    }

    public static <T> Result<?> error(int code, String msg, T data) {
        return new Result<>(code, msg, data);
    }

    public static Result<?> error(String msg) {
        return new Result<>(ResultEnum.CUSTOM_ERROR.getCode(), msg);
    }

    public static <T> Result<?> error(String msg, T data) {
        return new Result<>(ResultEnum.CUSTOM_ERROR.getCode(), msg, data);
    }
}
