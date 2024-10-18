package com.hzq.auth.filter;

import com.hzq.auth.domain.LoginBody;
import com.hzq.web.util.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.Optional;

/**
 * @author gc
 * @class com.hzq.auth.filter SystemLoginAuthenticationFilter
 * @date 2024/10/16 14:46
 * @description 自定义表单登录认证逻辑 UsernamePasswordAuthenticationFilter
 * 1. SpringSecurity 表单登录操作默认的参数形式是 url 参数（就是http://xx.xx.xx:xx/login?username=xx&password=xx）
 * 2. 这种方式必须从请求 Params 里面取 username and password，但是Post请求，将json字符串存储在请求体里面
 * 3. 调用源码堆栈信息发现，UsernamePasswordAuthenticationFilter 用于处理表单登录的请求，从请求参数里面取信息
 */
@AllArgsConstructor
public class SystemLoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    // 过滤器拦截的请求路径
    private static final List<String> paths = List.of(
            "/oauth2/system/login"
    );

    /**
     * @param request 请求对象
     * @param response 响应对象
     * @return boolean
     * @author gc
     * @date 2024/10/18 14:55
     * @apiNote 重写 requiresAuthentication 方法，自定义哪些请求需要进行认证
     **/
    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String requestUri = request.getRequestURI();
        return paths.stream().anyMatch(requestUri::equals);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginBody loginBody = ServletUtils.getRequestBody(request, LoginBody.class);
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String username = Optional.ofNullable(loginBody.getUsername()).orElse("");
        String password = Optional.ofNullable(loginBody.getPassword()).orElse("");

        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);

        setDetails(request, authRequest);
        return authenticationManager.authenticate(authRequest);
    }
}
