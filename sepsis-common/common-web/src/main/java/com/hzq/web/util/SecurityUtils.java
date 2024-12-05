package com.hzq.web.util;

import com.google.common.collect.ImmutableList;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import java.util.List;

/**
 * @author hua
 * @className com.hzq.web.util SecurityUtils
 * @date 2024/12/3 11:08
 * @description 针对下游微服务实现的安全配置工具类
 */
public class SecurityUtils {

    // 独立微服务白名单路径（用于 feign 内部调用）
    public static final List<String> internalPath = ImmutableList.<String>builder()
            .add("/system/user/username/**")
            .add("/system/user-role/roleIds/**")
            .add("/system/role/role-keys/**")
            .build();

    /**
     * @author hua
     * @date 2024/12/3 11:15
     * @param path 每个微服务放行的路径
     * @return java.lang.String[]
     * @apiNote 获取当前微服务所有放行路径（feign内部调用放行路径 + 该微服务放行路径）
     **/
    public static String[] getWhiteUrlPath(String... path) {
        final String[] resPath = new String[path.length + internalPath.size()];
        int idx = 0;
        for (String s : internalPath) resPath[idx++] = s;
        for (String s : path) resPath[idx++] = s;
        return resPath;
    }

    /**
     * @author hua
     * @date 2024/12/3 11:10
     * @param httpSecurity Spring Security 安全配置对象
     * @apiNote 配置下游微服务默认安全配置
     **/
    public static void applyDefaultSecurity(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable);
    }
}
