package com.hzq.auth.filter;

import com.google.common.base.Strings;
import com.hzq.auth.domain.LoginBody;
import com.hzq.auth.login.system.SystemLoginAuthenticationConverter;
import com.hzq.core.result.ResultEnum;
import com.hzq.web.exception.SystemException;
import com.hzq.web.util.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

/**
 * @author gc
 * @class com.hzq.auth.filter UsernameAuthenticationFilter
 * @date 2024/10/16 14:46
 * @description 自定义表单登录认证逻辑 UsernamePasswordAuthenticationFilter
 * 1. SpringSecurity 表单登录操作默认的参数形式是 url 参数（就是<a href="http://xx.xx.xx:xx/login?username=xx&password=xx">...</a>）
 * 2. 这种方式必须从请求 Params 里面取 username and password，但是Post请求，将json字符串存储在请求体里面
 * 3. 调用源码堆栈信息发现，UsernamePasswordAuthenticationFilter 用于处理表单登录的请求，从请求参数里面取信息
 */
@Setter
public class SystemLoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public SystemLoginAuthenticationFilter() {
    }

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
        if (!request.getMethod().equals("POST")) {
            throw new SystemException(ResultEnum.BAD_REQUEST_METHOD);
        }

        // 从请求体中解析出 LoginBody 对象
        LoginBody loginBody = ServletUtils.getRequestBody(request, LoginBody.class);

        // 从请求参数中获取用户名 和 密码
        String username = loginBody.getUsername();
        String password = loginBody.getPassword();

        if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(password)) {
            throw new SystemException(ResultEnum.USERNAME_OR_PASSWORD_ERROR);
        }

        Authentication authenticate = UsernamePasswordAuthenticationToken.unauthenticated(username, password);

        return super.getAuthenticationManager().authenticate(authenticate);
    }
}
