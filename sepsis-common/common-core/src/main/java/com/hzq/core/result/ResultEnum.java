package com.hzq.core.result;

import lombok.Getter;

/**
 * @author hua
 * @enum com.hua.common.exception ResultEnum
 * @date 2024/8/24 10:33
 * @description 非 HTTP 状态码，用于结果集返回码，便于前端的成功、错误、警告处理
 */
@Getter
public enum ResultEnum {

    // 客户端操作失败 4xx
    BAD_REQUEST_FORMAT(400, "请求消息内容格式不正确"),
    BAD_REQUEST_METHOD(400, "请求方法异常"),
    AUTHENTICATION_FAILED(401, "用户认证失败"),
    TOKEN_INVALID_OR_EXPIRED(401, "Token无效或已过期"),
    ACCESS_FORBIDDEN(403, "用户无权限访问"),

    // 服务端操作失败 5xx
    BAD_RESPONSE_FORMAT(500, "响应消息内容格式不正确"),
    SERVER_ERROR(500, "服务器内部错误"),

    // 自定义业务成功提示 1xxx
    OPERATION_SUCCESS(1000, "操作成功"),

    // 自定义业务警告提示 2xxx

    // 自定义业务失败提示 3xxx
    USERNAME_OR_PASSWORD_ERROR(3000, "用户名或密码错误"),
    USER_UNAVAILABLE(3000, "用户被禁用"),
    USER_PASSWORD_EXPIRED(3000, "密码超过有效期，请修改密码"),
    USERNAME_EXISTED(3000, "用户名已存在"),
    DEFAULT_ROLE_NOT_EXIST(3000, "系统默认角色不存在, 请反馈给管理员"),
    UNKNOWN_ERROR(3000, "未知错误, 请反馈给系统管理员"),
    CUSTOM_ERROR(3000, "自定义异常");

    private final int code;
    private final String message;

    ResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
