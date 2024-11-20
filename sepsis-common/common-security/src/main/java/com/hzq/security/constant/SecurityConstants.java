package com.hzq.security.constant;

/**
 * @author gc
 * @class com.hzq.security.constant SecurityConstants
 * @date 2024/11/18 14:58
 * @description 用于安全的常量
 */
public class SecurityConstants {
    // 请求头常量
    public static final String REQUEST_HEAD_USERNAME = "X-USER-NAME";
    public static final String REQUEST_HEAD_ROLES = "X-USER-ROLES";
    public static final String REQUEST_HEAD_AUTHENTICATION = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String LOGIN_USER_INFO_HEADER = "login_user_info";
}
