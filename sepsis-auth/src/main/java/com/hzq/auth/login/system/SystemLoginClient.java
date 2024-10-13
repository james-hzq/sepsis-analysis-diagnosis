package com.hzq.auth.login.system;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author hua
 * @className com.hzq.auth.login.system SystemLoginClient
 * @date 2024/10/13 16:47
 * @description 系统用户名和密码登录的客户端，因为这里没有第三方客户端，所以写死在内存中。
 */
@Component
public class SystemLoginClient {
    // 唯一的客户端 ID
    public static final String SYSTEM_REGISTERED_ID = "sepsis";
    // 客户端的标识符
    public static final String SYSTEM_REGISTERED_CLIENT_ID = "sepsis-client";
    // 客户端密钥
    public static final String SYSTEM_REGISTERED_CLIENT_SECRET = "sepsis-secret";
    // 定义支持的作用域
    public static final Set<String> SCOPES = Set.of("root", "admin", "user");

    // 定义系统的 RegisteredClient
    public RegisteredClient systemRegisteredClient() {
        return RegisteredClient
                .withId(SYSTEM_REGISTERED_ID)
                .clientId(SYSTEM_REGISTERED_CLIENT_ID)
                .clientSecret(SYSTEM_REGISTERED_CLIENT_SECRET)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(SystemLoginAuthenticationToken.AUTH_TYPE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .scopes(set -> set.addAll(SCOPES))
                .build();
    }
}
