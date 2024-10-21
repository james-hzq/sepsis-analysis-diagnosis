package com.hzq.auth.filter;

import com.hzq.web.cache.request.CachedBodyHttpServletRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author gc
 * @class com.hzq.auth.filter CachedRequestBodyFilter
 * @date 2024/10/21 9:31
 * @description TODO
 */
@Component
public class CachedRequestBodyFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 包装 HttpServletRequest，只包装一次请求
        if (request instanceof CachedBodyHttpServletRequest) {
            filterChain.doFilter(request, response);
        }
        CachedBodyHttpServletRequest cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest(request);
        // 继续执行过滤器链
        filterChain.doFilter(cachedBodyHttpServletRequest, response);
    }
}
