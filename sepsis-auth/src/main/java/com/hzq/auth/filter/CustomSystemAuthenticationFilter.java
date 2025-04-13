package com.hzq.auth.filter;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author gc
 * @class com.hzq.auth.filter CustomSystemAuthenticationFilter
 * @date 2024/11/28 17:43
 * @description 默认的 UsernamePasswordAuthenticationFilter 是从请求的 param 里面获取用户名和密码
 */
public class CustomSystemAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
}
