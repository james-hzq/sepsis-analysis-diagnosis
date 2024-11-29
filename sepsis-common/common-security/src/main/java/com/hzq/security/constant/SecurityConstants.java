package com.hzq.security.constant;

/**
 * @author gc
 * @class com.hzq.security.constant SecurityConstants
 * @date 2024/11/18 14:58
 * @description 用于安全的常量
 */
public class SecurityConstants {
    /** 请求头常量 */
    // 用户名
    public static final String REQUEST_HEAD_USERNAME = "X-USER-NAME";
    // 角色
    public static final String REQUEST_HEAD_ROLES = "X-USER-ROLES";
    // feign 内部调用访问令牌
    public static final String REQUEST_HEAD_INTERNAL_TOKEN = "X-INTERNAL-TOKEN";
    // token
    public static final String REQUEST_HEAD_AUTHENTICATION = "Authorization";

    /** Token 常量 */
    // 所有token前缀
    public static final String TOKEN_PREFIX = "Bearer ";
    // ACCESS_TOKEN 前缀
    public static final String ACCESS_TOKEN_PREFIX = "ACCESS-TOKEN:";
    // JWT 前者最
    public static final String JWT_PREFIX = "JWT:";
}
