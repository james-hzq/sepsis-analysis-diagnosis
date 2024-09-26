package com.hzq.core.result;

/**
 * @author hua
 * @enum com.hua.common.exception ResultEnum
 * @date 2024/8/24 10:33
 * @description TODO
 */
public enum ResultEnum {
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求方法异常"),
    ACCESS_UNAUTHORIZED(401, "用户访问未被授权"),
    TOKEN_INVALID_OR_EXPIRED(401, "Token无效或已过期"),
    ACCESS_FORBIDDEN(403, "用户无权限访问"),
    ERROR(500, "服务器内部错误"),
    CUSTOM_ERROR(1000, "自定义异常");

    private int code;
    private String msg;

    ResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
