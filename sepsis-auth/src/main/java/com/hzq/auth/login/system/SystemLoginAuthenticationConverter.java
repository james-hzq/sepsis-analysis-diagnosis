package com.hzq.auth.login.system;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.hzq.web.exception.SystemException;
import com.hzq.web.util.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;

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

        // 判断是否为指定的认证类型，如果不是则返回 null
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!SystemLoginAuthenticationToken.AUTH_TYPE.getValue().equals(grantType)) {
            return null;
        }

        // 获取当前的认证主体，即客户端凭证
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

        // 获取请求参数，将请求参数转换为 Map<String, String> 类型
        Map<String, String> parameters = ServletUtils.getRequestParams(request);

        // 从请求参数中获取 scope 值
        String scope = parameters.get(OAuth2ParameterNames.SCOPE);
        Set<String> requestedScopes = null;
        if (!Strings.isNullOrEmpty(scope)) {
            // 如果 scope 不为空，则将其分割成 Set 集合
            requestedScopes = new HashSet<>(Splitter.on(" ").splitToList(scope));
        }

        // 从请求参数中获取用户名
        String username = parameters.get(OAuth2ParameterNames.USERNAME);
        if (Strings.isNullOrEmpty(username)) {
            throw new SystemException("");
        }

        // 从请求参数中获取密码
        String password = parameters.get(OAuth2ParameterNames.PASSWORD);
        if (Strings.isNullOrEmpty(password)) {
            throw new SystemException("");
        }

        // 将除 grant_type 和 scope 之外的其他请求参数过滤并收集为 Map<String, Object>
        Map<String, Object> additionalParameters = parameters
                .entrySet()
                .stream()
                .filter(e -> !Set.of(OAuth2ParameterNames.GRANT_TYPE, OAuth2ParameterNames.SCOPE).contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // 返回自定义的认证令牌，包含客户端凭证、请求的 scope 和其他附加参数
        return new SystemLoginAuthenticationToken(
                clientPrincipal,
                requestedScopes,
                additionalParameters
        );
    }
}
