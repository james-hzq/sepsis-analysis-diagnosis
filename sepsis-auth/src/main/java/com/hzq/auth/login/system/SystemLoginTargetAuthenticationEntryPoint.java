package com.hzq.auth.login.system;

import com.google.common.base.Strings;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.UrlUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author gc
 * @class com.hzq.auth.login.system SystemLoginTargetAuthenticationEntryPoint
 * @date 2024/10/16 17:06
 * @description TODO
 */
@Slf4j
public class SystemLoginTargetAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public SystemLoginTargetAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        String loginFormUrl = this.determineUrlToUseForThisRequest(request, response, authException);

        StringBuffer requestUrl = request.getRequestURL();
        String queryString = request.getQueryString();
        if (Strings.isNullOrEmpty(queryString)) {
            requestUrl.append("?").append(queryString);
        }
        String targetParameter = URLEncoder.encode(requestUrl.toString(), StandardCharsets.UTF_8);
        String targetUrl = loginFormUrl + "?target=" + targetParameter;
        log.info("未授权重定向到前后端分离登录页面：{}", targetUrl);
        this.redirectStrategy.sendRedirect(request, response, targetUrl);
    }
}

