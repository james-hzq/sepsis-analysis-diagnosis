package com.hzq.web.filter;

import com.hzq.security.constant.SecurityConstants;
import com.hzq.web.util.SecurityUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author gc
 * @class com.hzq.web.filter InternalTokenFilter
 * @date 2024/11/29 16:29
 * @description 内部服务需要验证是否有内部密钥
 */
@Component
public class InternalTokenFilter extends OncePerRequestFilter {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {

        if (isInternalPath(request.getRequestURI())) {
            String internalToken = request.getHeader(SecurityConstants.REQUEST_HEAD_INTERNAL_TOKEN);
            if (StringUtils.hasText(internalToken) && "hzq".equals(internalToken)) {
                filterChain.doFilter(request, response);
            } else  {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid internal token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isInternalPath(String path) {
        // 判断该路径是否在白名单内
        return !SecurityUtils.internalPath.isEmpty() && SecurityUtils.internalPath
                .stream()
                .anyMatch(whitePath -> antPathMatcher.match(whitePath, path));
    }
}
