package com.hzq.common.exception;

/**
 * @author hua
 * @enum com.hua.common.exception ResultEnum
 * @date 2024/8/24 10:33
 * @description TODO
 */
public enum ResultEnum {
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求异常"),
    UNAUTHORIZED(401, "用户未认证"),
    FORBIDDEN(403, "用户未授权"),
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
