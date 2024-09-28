package com.hzq.core.result;

import lombok.Getter;

/**
 * @author hua
 * @enum com.hua.common.exception ResultEnum
 * @date 2024/8/24 10:33
 * @description TODO
 */
@Getter
public enum ResultEnum {
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求方法异常"),
    ACCESS_UNAUTHORIZED(401, "用户访问未被授权"),
    TOKEN_INVALID_OR_EXPIRED(401, "Token无效或已过期"),
    ACCESS_FORBIDDEN(403, "用户无权限访问"),
    ERROR(500, "服务器内部错误"),
    JWT_PARSE_ERROR(1000, "JWT 格式不正确，解析失败"),
    USERNAME_OR_PASSWORD_ERROR(1000, "用户名或密码错误"),
    CUSTOM_ERROR(1000, "自定义异常");

    private final int code;
    private final String msg;

    ResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
