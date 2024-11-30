package com.hzq.auth.handler;

import com.google.common.collect.ImmutableMap;
import com.hzq.core.result.Result;
import com.hzq.core.result.ResultEnum;
import com.hzq.jackson.util.JacksonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * @author hua
 * @className com.hzq.auth.handler SystemAuthenticationFailureHandler
 * @date 2024/11/28 23:05
 * @description 自定义系统用户名密码登录失败处理器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ErrorConverter errorConverter = new AuthenticationErrorTranslator();
    private final ResponseWriter responseWriter = new JsonResponseWriter();

    // 错误转换器
    interface ErrorConverter {
        Result<String> convert(AuthenticationException exception);
    }

    // 响应写入接口
    interface ResponseWriter {
        void write(HttpServletResponse response, Result<String> result) throws IOException;
    }

    // 认证错误翻译器实现
    static class AuthenticationErrorTranslator implements ErrorConverter {
        private static final Map<Class<? extends AuthenticationException>, ResultEnum> ERROR_MAPPING =
                ImmutableMap.<Class<? extends AuthenticationException>, ResultEnum>builder()
                        .put(UsernameNotFoundException.class, ResultEnum.USERNAME_OR_PASSWORD_ERROR)
                        .put(BadCredentialsException.class, ResultEnum.USERNAME_OR_PASSWORD_ERROR)
                        .put(AccountExpiredException.class, ResultEnum.USER_UNAVAILABLE)
                        .put(LockedException.class, ResultEnum.USER_UNAVAILABLE)
                        .put(DisabledException.class, ResultEnum.USER_UNAVAILABLE)
                        .put(CredentialsExpiredException.class, ResultEnum.USER_PASSWORD_EXPIRED)
                        .build();

        @Override
        public Result<String> convert(AuthenticationException exception) {
            return Optional.ofNullable(ERROR_MAPPING.get(exception.getClass()))
                    .map(resultEnum -> Result.error(resultEnum, exception.getMessage()))
                    .orElse(Result.error(ResultEnum.SERVER_ERROR, exception.getMessage()));
        }
    }

    // JSON响应写入器实现
    static class JsonResponseWriter implements ResponseWriter {
        @Override
        public void write(HttpServletResponse response, Result<String> result) throws IOException {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JacksonUtils.toJsonString(result));
        }
    }


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("Authentication failed: {} - {}", exception.getClass().getSimpleName(), exception.getMessage());
        // 错误转换
        Result<String> errorResult = errorConverter.convert(exception);
        // 写入响应
        responseWriter.write(response, errorResult);
    }
}
