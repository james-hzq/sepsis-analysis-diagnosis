package com.hzq.auth.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;

/**
 * @author hua
 * @className com.hzq.auth.util OAuth2AuthenticationProviderUtils
 * @date 2024/10/13 16:04
 * @description 在 OAuth2 认证与授权过程中，处理与客户端身份认证及授权撤销（失效）相关的逻辑。
 */
public class OAuth2AuthenticationProviderUtils {

    public OAuth2AuthenticationProviderUtils() {
    }

    /**
     * @author hua
     * @date 2024/10/13 16:07
     * @param authentication 认证对象
     * @return org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken
     * @apiNote 获取已认证的客户端身份
     **/
    public static OAuth2ClientAuthenticationToken getAuthenticatedClient(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;

        // 判断 authentication.getPrincipal() 是否是 OAuth2ClientAuthenticationToken 类型
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }

        // 如果客户端身份已认证，返回客户端身份
        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }

        // 如果未认证，抛出无效客户端异常
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }
}
