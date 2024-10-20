package com.hzq.auth.handler;

import com.hzq.auth.login.system.SystemLoginAuthenticationConverter;
import com.hzq.core.result.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author gc
 * @class com.hzq.auth.handler HzqAuthenticationFailureHandler
 * @date 2024/10/12 17:04
 * @description 认证失败处理器
 */
@Slf4j
public class HzqAuthenticationFailureHandler implements AuthenticationFailureHandler {
    // HttpMessageConverter 是 Spring MVC 提供的一个策略接口。可以将 HTTP请求转换为Java对象，将Java对象转换为HTTP响应
    private final HttpMessageConverter<Object> httpMessageConverter = new MappingJackson2HttpMessageConverter();


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 从 AuthenticationException 中获取 OAuth2Error 对象，以便进一步处理认证失败的细节。
        OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();
        // 将 HttpServletResponse 对象包装成 Spring 框架中的 ServerHttpResponse 的实现，以便后续的消息转换处理
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        log.error("在请求 {} 上收到错误 {}", request.getRequestURI(), error.getErrorCode());
        // 使用 HttpMessageConverter 将 Java对象转换为HTTP响应
        httpMessageConverter.write(
                Result.error(Integer.parseInt(error.getErrorCode()), error.getDescription()),
                null,
                httpResponse
        );
    }
}
