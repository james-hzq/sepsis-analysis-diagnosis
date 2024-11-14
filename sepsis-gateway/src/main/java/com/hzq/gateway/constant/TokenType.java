package com.hzq.gateway.constant;

import lombok.Getter;
import org.springframework.util.StringUtils;

/**
 * @author gc
 * @enum com.hzq.gateway.constant TokenType
 * @date 2024/11/14 15:10
 * @description Token类型枚举
 */
@Getter
public enum TokenType {
    SYSTEM_LOGIN_JWT_TOKEN("JWT:"),
    OAUTH2_LOGIN_ACCESS_TOKEN("ACCESS-TOKEN:"),
    OAUTH2_LOGIN_REFRESH_TOKEN("REFRESH-TOKEN:"),;

    private final String prefix;

    TokenType(String prefix) {
        this.prefix = prefix;
    }

    public static TokenType getTokenTypeFromAuthorizationHeader(String authorizationToken) {
        if (!StringUtils.hasText(authorizationToken)) return null;

        for (TokenType tokenType : TokenType.values()) {
            if (authorizationToken.startsWith(tokenType.getPrefix())) {
                return tokenType;
            }
        }
        return null;
    }
}
