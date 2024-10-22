package com.hzq.auth.login.system;

import com.google.common.base.Strings;
import com.hzq.auth.domain.LoginBody;
import com.hzq.core.result.ResultEnum;
import com.hzq.web.exception.SystemException;
import com.hzq.web.util.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;

import java.util.Map;

/**
 * @author gc
 * @class com.hzq.auth.login.system SystemLoginAuthenticationConverter
 * @date 2024/10/21 15:41
 * @description TODO
 */
public class SystemLoginAuthenticationConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {
        LoginBody loginBody = ServletUtils.getRequestBody(request, LoginBody.class);

        // 判断授权类型是否满足
        if (!SystemLoginAuthenticationProperty.AUTH_TYPE.getValue().equals(loginBody.getGrantType())) {
            return null;
        }

        // 客户端信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 从请求参数中获取用户名 和 密码
        String username = loginBody.getUsername();
        String password = loginBody.getPassword();

        if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(password)) {
            throw new SystemException(ResultEnum.USERNAME_OR_PASSWORD_ERROR);
        }


        Map<String, Object> additionalParameters = Map.of(
                OAuth2ParameterNames.USERNAME, username,
                OAuth2ParameterNames.PASSWORD, password
        );

        return new SystemLoginAuthenticationToken(
                authentication,
                null,
                additionalParameters
        );
    }
}
