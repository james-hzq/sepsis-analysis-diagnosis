package com.hzq.auth.handler;

import com.hzq.auth.config.auth.AuthSecurityProperties;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author gc
 * @class com.hzq.auth.handler OAuth2AuthenticationFailureHandler
 * @date 2024/11/6 14:45
 * @description 联合认证失败回调
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final AuthSecurityProperties authSecurityProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 重定向到登录页面，并携带错误信息
        String redirectUrl = authSecurityProperties.getLoginPageUri() + "?error=" + exception.getMessage();
        log.error("联合认证失败，进入自定义错误处理类，错误信息如下：{}\n" +
                "将请求重定向到登录页面 {}",
                exception.getMessage(), redirectUrl
        );
        response.sendRedirect(redirectUrl);
    }
}
