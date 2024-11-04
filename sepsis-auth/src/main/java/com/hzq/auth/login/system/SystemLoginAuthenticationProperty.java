package com.hzq.auth.login.system;

import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.List;
import java.util.Set;

/**
 * @author gc
 * @class com.hzq.auth.login.system SystemLoginAuthenticationProperty
 * @date 2024/10/21 11:16
 * @description 系统用户名和密码登录基础配置
 */
public class SystemLoginAuthenticationProperty {
    // 系统用户登录过滤器拦截路径
    public static final List<String> FILTER_INTERCEPTION_PATH = List.of(
            "/auth/system/login"
    );

    // 系统用户登录申请的访问范围
    public static final Set<String> REQUEST_SCOPES = Set.of(
            "user"
    );

    // 定义身份验证令牌类型
    public static final AuthorizationGrantType AUTH_TYPE = AuthorizationGrantType.PASSWORD;

    // 为系统用颁发授权令牌的客户端ID
    public static final String REGISTERED_CLIENT_ID = "sepsis-web-client";
}
