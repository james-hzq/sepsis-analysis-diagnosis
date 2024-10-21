package com.hzq.auth.login.system;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

/**
 * @author gc
 * @class com.hzq.auth.login.system SystemLoginAuthenticationConverter
 * @date 2024/10/21 15:41
 * @description TODO
 */
public class SystemLoginAuthenticationConverter implements AuthenticationConverter {
    @Override
    public Authentication convert(HttpServletRequest request) {
        return null;
    }
}
