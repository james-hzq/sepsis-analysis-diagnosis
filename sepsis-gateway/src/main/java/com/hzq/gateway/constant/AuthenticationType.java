package com.hzq.gateway.constant;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

/**
 * @author gc
 * @enum com.hzq.gateway.constant AuthenticationType
 * @date 2024/11/15 11:11
 * @description 认证对象类型枚举
 */
@Getter
public enum AuthenticationType {
    OAUTH2_ACCESS_TOKEN_AUTHENTICATION(OAuth2AuthenticationToken.class),
    SYSTEM_USERNAME_PASSWORD_AUTHENTICATION(UsernamePasswordAuthenticationToken.class);

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
