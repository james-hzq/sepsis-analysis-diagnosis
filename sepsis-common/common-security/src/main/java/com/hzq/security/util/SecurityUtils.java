package com.hzq.security.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author gc
 * @class com.hzq.security.util SecurityUtils
 * @date 2024/11/18 14:17
 * @description 安全上下文工具类
 */
public class SecurityUtils {

    // 私有构造器，防止实例化
    private SecurityUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * @param authentication 要存储的认证对象
     * @author hzq
     * @date 2024/11/18 15:17
     * @apiNote 设置当前的 Authentication 对象（适用于同步环境）
     **/
    public static void setAuthentication(Authentication authentication) {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            throw new IllegalStateException("No SecurityContext available");
        }
        context.setAuthentication(authentication);
    }

    /**
     * @return org.springframework.security.core.Authentication
     * @author hzq
     * @date 2024/11/18 15:32
     * @apiNote 获取当前的 Authentication 对象（适用于同步环境）
     **/
    public static Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            throw new IllegalStateException("No SecurityContext available");
        }
        return context.getAuthentication();
    }

    /**
     * @return java.lang.String
     * @author hzq
     * @date 2024/11/18 15:51
     * @apiNote 获取当前的 Authentication 对象的用户名（适用于同步环境）
     **/
    public static String getUsername(Authentication authentication) {
        return authentication.getName();
    }

    /**
     * @return java.util.Set<java.lang.String>
     * @author hzq
     * @date 2024/11/18 15:52
     * @apiNote 获取当前的 Authentication 对象的角色（适用于同步环境）
     **/
    public static Set<String> getRoles(Authentication authentication) {
        return AuthorityUtils.authorityListToSet(authentication.getAuthorities())
                .stream()
                .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
    }

    public static String getRoleStr(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    /**
     * @author hzq
     * @date 2024/11/18 15:33
     * @apiNote 清除当前线程中的 SecurityContext（适用于同步环境）
     **/
    public static void clearAuthenticationSync() {
        SecurityContextHolder.clearContext();
    }
}
