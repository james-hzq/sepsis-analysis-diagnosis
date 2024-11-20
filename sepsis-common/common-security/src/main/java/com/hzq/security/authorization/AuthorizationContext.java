package com.hzq.security.authorization;

/**
 * @author gc
 * @class com.hzq.security.authorization AuthorizationContext
 * @date 2024/11/20 10:22
 * @description 各个微服务存放鉴权信息，使用ThreadLocal进行线程隔离
 */
public class AuthorizationContext {
    private static final ThreadLocal<AuthorizationInfo> CONTEXT = new ThreadLocal<>();

    public static void setAuthorizationInfo(AuthorizationInfo authorizationInfo) {
        CONTEXT.set(authorizationInfo);
    }

    public static AuthorizationInfo getAuthorizationInfo() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
