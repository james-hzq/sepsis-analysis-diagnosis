package com.hzq.auth.filter;

import com.hzq.auth.domain.LoginBody;
import com.hzq.web.util.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.checkerframework.checker.units.qual.A;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Optional;

/**
 * @author gc
 * @class com.hzq.auth.filter HzqUsernamePasswordAuthenticationFilter
 * @date 2024/10/16 14:46
 * @description 自定义表单登录认证逻辑 UsernamePasswordAuthenticationFilter
 * 1. SpringSecurity 表单登录操作默认的参数形式是 url 参数（就是http://xx.xx.xx:xx/login?username=xx&password=xx）
 * 2. 这种方式必须从请求 Params 里面取 username and password，但是Post请求，将json字符串存储在请求体里面
 * 3. 调用源码堆栈信息发现，UsernamePasswordAuthenticationFilter 用于处理表单登录的请求，从请求参数里面取信息
 */
public class HzqUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginBody loginBody = ServletUtils.getRequestBody(request, LoginBody.class);

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String username = Optional.ofNullable(loginBody.getUsername()).orElse("");
        String password = Optional.ofNullable(loginBody.getPassword()).orElse("");

        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken
                .unauthenticated(username, password);

        setDetails(request, authRequest);
        return super.getAuthenticationManager().authenticate(authRequest);
    }
}
