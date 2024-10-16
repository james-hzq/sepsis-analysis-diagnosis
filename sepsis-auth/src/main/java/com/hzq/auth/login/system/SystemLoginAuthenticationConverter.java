package com.hzq.auth.login.system;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.hzq.auth.domain.LoginBody;
import com.hzq.core.result.ResultEnum;
import com.hzq.web.exception.SystemException;
import com.hzq.web.util.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author gc
 * @class com.hzq.auth.login.system SystemLoginAuthenticationConverter
 * @date 2024/10/14 9:59
 * @description 系统用户名密码登录模式参数解析器
 */
@Slf4j
public class SystemLoginAuthenticationConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {
        // 从请求体中解析出 LoginBody 对象
        LoginBody loginBody = ServletUtils.getRequestBody(request, LoginBody.class);

        // 判断是否为指定的认证类型，如果不是则返回 null
        String grantType = loginBody.getGrantType();

        if (!SystemLoginAuthenticationToken.AUTH_TYPE.getValue().equals(grantType)) {
            return null;
        }

        // 获取当前的认证主体，即客户端凭证
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

        Set<String> requestedScopes = new HashSet<>();

        // 从请求参数中获取用户名 和 密码
        String username = loginBody.getUsername();
        String password = loginBody.getPassword();
        if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(password)) {
            throw new SystemException(ResultEnum.USERNAME_OR_PASSWORD_ERROR);
        }

        Map<String, Object> additionalParameters = new HashMap<>() {{
           put("username", username);
           put("password", password);
           put("code", loginBody.getCode());
        }};

//        // 将除 grant_type 和 scope 之外的其他请求参数过滤并收集为 Map<String, Object>
//        Map<String, Object> additionalParameters = parameters
//                .entrySet()
//                .stream()
//                .filter(e -> !Set.of(OAuth2ParameterNames.GRANT_TYPE, OAuth2ParameterNames.SCOPE).contains(e.getKey()))
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // 返回自定义的认证令牌，包含客户端凭证、请求的 scope 和其他附加参数
        return new SystemLoginAuthenticationToken(
                clientPrincipal,
                requestedScopes,
                additionalParameters
        );
    }
}
