package com.hzq.web.filter;

import com.hzq.security.authorization.AuthorizationContext;
import com.hzq.security.authorization.AuthorizationInfo;
import com.hzq.security.constant.SecurityConstants;
import com.hzq.web.util.ServletUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author gc
 * @class com.hzq.web.filter AuthorizationFilter
 * @date 2024/11/20 10:02
 * @description 鉴权过滤器
 */
@Slf4j
@Configuration
public class AuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取鉴权信息
        String username = ServletUtils.getParameter(SecurityConstants.REQUEST_HEAD_USERNAME);
        String roles = ServletUtils.getParameter(SecurityConstants.REQUEST_HEAD_ROLES);

        // 保存到上下文
        if (username != null && roles != null) {
            AuthorizationInfo authInfo = new AuthorizationInfo(username, AuthorizationInfo.getRoles(roles));
            AuthorizationContext.setAuthorizationInfo(authInfo);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            // 清理上下文，确保当前线程的上下文被清空
            AuthorizationContext.clear();
        }
    }
}
