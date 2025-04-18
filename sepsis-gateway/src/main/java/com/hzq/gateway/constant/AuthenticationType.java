package com.hzq.gateway.constant;

import com.hzq.gateway.user.AccessTokenAuthentication;
import com.hzq.gateway.user.JwtAuthentication;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * @author gc
 * @enum com.hzq.gateway.constant AuthenticationType
 * @date 2024/11/15 11:11
 * @description 认证对象类型枚举
 */
@Getter
public enum AuthenticationType {
    OAUTH2_ACCESS_TOKEN_AUTHENTICATION(AccessTokenAuthentication.class),
    SYSTEM_USERNAME_PASSWORD_AUTHENTICATION(JwtAuthentication.class);

    private final Class<? extends Authentication> clazz;

    AuthenticationType(Class<? extends Authentication> clazz) {
        this.clazz = clazz;
    }

    public static AuthenticationType getAuthenticationType(Authentication authentication) {
        if (authentication == null) return null;

        for (AuthenticationType authenticationType : AuthenticationType.values()) {
            if (authenticationType.getClazz().equals(authentication.getClass())) {
                return authenticationType;
            }
        }
        return null;
    }
}
