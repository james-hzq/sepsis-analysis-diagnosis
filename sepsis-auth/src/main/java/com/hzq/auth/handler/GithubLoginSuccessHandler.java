package com.hzq.auth.handler;

import com.hzq.core.result.Result;
import com.hzq.jackson.JacksonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author hua
 * @className com.hzq.auth.handler GithubLoginSuccessHandler
 * @date 2024/10/31 19:57
 * @description TODO
 */
@Slf4j
public class GithubLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("用户名密码表单登录成功, authentication = ", authentication.toString());
        System.out.println(authentication.toString());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(JacksonUtil.toJsonString(Result.success()));
        response.getWriter().flush();
    }
}
