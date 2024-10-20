package com.hzq.auth.login.system;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author hua
 * @className com.hzq.auth.login.system SystemLoginAuthenticationToken
 * @date 2024/10/13 15:47
 * @description 系统用户名密码登录授权模式身份验证令牌
 */
@Getter
public class SystemLoginAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {
    // 定义身份验证令牌类型
    public static final AuthorizationGrantType AUTH_TYPE = AuthorizationGrantType.PASSWORD;
    // 令牌申请的访问范围
    private Set<String> scopes;

    /**
     * @author hua
     * @date 2024/10/13 15:55
     * @param authentication 身份验证主体
     * @param scopes 令牌申请的访问范围
     * @param additionalParameters 自定义额外参数(用户名和密码)
     * @apiNote TODO
     **/
    public SystemLoginAuthenticationToken(Authentication authentication, Set<String> scopes, Map<String, Object> additionalParameters) {
        this(AUTH_TYPE, authentication, additionalParameters);
        this.scopes = Collections.unmodifiableSet(scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
    }

    /**
     * @author hua
     * @date 2024/10/13 15:50
     * @param authorizationGrantType 令牌访问类型
     * @param clientPrincipal 客户端信息
     * @param additionalParameters 自定义额外参数(用户名和密码)
     * @apiNote 继承的默认构造器
     **/
    protected SystemLoginAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal, Map<String, Object> additionalParameters) {
        super(authorizationGrantType, clientPrincipal, additionalParameters);
    }

    /**
     * @author hua
     * @date 2024/10/13 15:59
     * @return java.lang.Object
     * @apiNote 用户凭证(密码)
     **/
    @Override
    public Object getCredentials() {
        return this.getAdditionalParameters().get(OAuth2ParameterNames.PASSWORD);
    }
}
