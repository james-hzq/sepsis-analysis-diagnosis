package com.hzq.auth.login.client;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.Set;

/**
 * @author gc
 * @class com.hzq.auth.login.client SystemLoginClient
 * @date 2024/11/6 17:21
 * @description 系统登录配置
 */
@Configuration
@RequiredArgsConstructor
public class SystemLoginClient {

    private static final String ID = "sepsis";
    private static final String CLIENT_ID = "sepsis-web-client";
    private static final String CLIENT_SECRET = "sepsis";
    private static final String CLIENT_NAME = "脓毒症智能分析与诊询平台";
    private static final Set<String> scopes = Set.of("root", "admin", "user");
    private static final ClientAuthenticationMethod clientAuthenticationMethod = ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
    private static final AuthorizationGrantType authorizationGrantType = AuthorizationGrantType.PASSWORD;
    private static final String REDIRECT_URI = "http://127.0.0.1:9200/login/auth2/code/system";
    private static final String POST_LOGOUT_URI = "http://127.0.0.1:9200/logout";

    // 密码加密器，用于加密客户端密钥
    private final PasswordEncoder passwordEncoder;

    public RegisteredClient initSepsisSystemClient() {
        return RegisteredClient
                // 创建一个带有指定 ID的已注册客户端对象
                .withId(ID)
                // 设置客户端ID
                .clientId(CLIENT_ID)
                // 设置客户端密钥
                .clientSecret(passwordEncoder.encode(CLIENT_SECRET))
                // 设置客户端名称
                .clientName(CLIENT_NAME)
                // 设置客户端范围（scopes）
                .scopes(set -> set.addAll(scopes))
                // 设置客户端认证方法为基本身份验证
                .clientAuthenticationMethod(clientAuthenticationMethod)
                // 授权类型为密码授权模式
                .authorizationGrantType(authorizationGrantType)
                // 授权类型为刷新令牌授权模式
                .authorizationGrantType(authorizationGrantType)
                // 设置重定向URI
                .redirectUri(REDIRECT_URI)
                // 设置登出后的重定向URI
                .postLogoutRedirectUri(POST_LOGOUT_URI)
                // 设置令牌设置，包括访问令牌的存活时间为1天
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofHours(2)).build())
                // 设置客户端设置，需要授权同意
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                // 构建并返回已注册的客户端对象
                .build();
    }
}
