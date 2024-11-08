package com.hzq.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author gc
 * @class com.hzq.auth.config LoginTargetAuthenticationEntryPoint
 * @date 2024/11/6 14:27
 * @description 错误处理，重定向至登录处理
 */
/**
 * @author gc
 * @class com.hzq.auth.config LoginTargetAuthenticationEntryPoint
 * @date 2024/11/6 14:27
 * @description 错误处理，重定向至登录处理
 */
@Slf4j
public class LoginTargetAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public LoginTargetAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("错误是: {}", authException.getMessage());
        // 获取登录表单的地址
        String loginForm = determineUrlToUseForThisRequest(request, response, authException);
        if (!UrlUtils.isAbsoluteUrl(loginForm)) {
            // 不是绝对路径调用父类方法处理
            super.commence(request, response, authException);
            return;
        }

        log.debug("出现错误{}，重定向至前后端分离的登录页面：{}", authException.getMessage(), loginForm);
        this.redirectStrategy.sendRedirect(request, response, loginForm);
    }
}
