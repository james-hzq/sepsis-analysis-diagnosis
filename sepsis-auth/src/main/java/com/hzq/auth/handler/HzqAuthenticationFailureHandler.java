package com.hzq.auth.handler;

import com.hzq.core.result.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

/**
 * @author gc
 * @class com.hzq.auth.handler HzqAuthenticationFailureHandler
 * @date 2024/10/12 17:04
 * @description 认证失败处理器
 */
public class HzqAuthenticationFailureHandler implements AuthenticationFailureHandler {
    // Spring 提供的一个 HttpMessageConverter 实现，它将 Java 对象转换为 JSON 格式的 HTTP 响应，或者将 JSON 格式的 HTTP 请求转换为 Java 对象。
    private final HttpMessageConverter<Object> accessTokenHttpResponseConverter = new MappingJackson2HttpMessageConverter();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        accessTokenHttpResponseConverter.write(Result.error(Integer.parseInt(error.getErrorCode()), error.getDescription()), null, httpResponse);
    }
}
