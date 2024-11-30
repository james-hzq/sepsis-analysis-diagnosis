package com.hzq.auth.login.info;

import lombok.Getter;
import org.springframework.util.StringUtils;

/**
 * @author hua
 * @enumName com.hzq.auth.login.info TokenType
 * @date 2024/11/30 15:02
 * @description 令牌类型
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

    public static TokenType getTokenType(String authorizationToken) {
        if (!StringUtils.hasText(authorizationToken)) return null;

        for (TokenType tokenType : TokenType.values()) {
            if (authorizationToken.startsWith(tokenType.getPrefix())) {
                return tokenType;
            }
        }
        return null;
    }
}
