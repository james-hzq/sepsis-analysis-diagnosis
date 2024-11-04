package com.hzq.auth.config;

import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * @author gc
 * @class com.hzq.auth.config.oauth2 CustomOAuth2TokenCustomizer
 * @date 2024/10/22 15:58
 * @description 自定义 JWT 令牌
 */
public final class CustomOAuth2TokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    @Override
    public void customize(JwtEncodingContext context) {
    }
}
