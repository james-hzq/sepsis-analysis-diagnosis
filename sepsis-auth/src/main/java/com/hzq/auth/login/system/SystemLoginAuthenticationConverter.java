package com.hzq.auth.login.system;

import com.google.common.base.Strings;
import com.hzq.auth.domain.LoginBody;
import com.hzq.core.result.ResultEnum;
import com.hzq.web.exception.SystemException;
import com.hzq.web.util.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author gc
 * @class com.hzq.auth.login.system SystemLoginAuthenticationConverter
 * @date 2024/10/14 9:59
 * @description 将前端传递的登录参数转换为自定义认证对象。
 */
@Slf4j
@Setter
public class SystemLoginAuthenticationConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {
        // 从请求体中解析出 LoginBody 对象
        LoginBody loginBody = ServletUtils.getRequestBody(request, LoginBody.class);

        // 判断是否为指定的认证类型，如果不是则返回 null
        String grantType = loginBody.getGrantType();

        if (!SystemLoginAuthenticationToken.AUTH_TYPE.getValue().equals(grantType)) {
            throw new SystemException(ResultEnum.AUTHORIZATION_MODE_ERROR);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Set<String> requestedScopes = new HashSet<>();

        // 从请求参数中获取用户名 和 密码
        String username = loginBody.getUsername();
        String password = loginBody.getPassword();
        if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(password)) {
            throw new SystemException(ResultEnum.USERNAME_OR_PASSWORD_ERROR);
        }

        Map<String, Object> additionalParameters = new HashMap<>() {{
           put(OAuth2ParameterNames.USERNAME, username);
           put(OAuth2ParameterNames.PASSWORD, password);
        }};

        // 返回自定义的认证令牌，包含客户端凭证、请求的 scope 和其他附加参数
        return new SystemLoginAuthenticationToken(
                authentication,
                requestedScopes,
                additionalParameters
        );
    }
}
