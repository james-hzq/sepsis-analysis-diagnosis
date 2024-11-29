package com.hzq.web.config;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * @author gc
 * @class com.hzq.web.config WebSecurityProperties
 * @date 2024/11/29 16:31
 * @description 微服务安全配置
 */
public class WebSecurityProperties {

    // 独立微服务白名单路径（用于 feign 内部调用）
    public static final List<String> internalPath = ImmutableList.<String>builder()
            .add("/system/user/username/**")
            .add("/system/user-role/roleIds/**")
            .add("/system/role/role-keys/**")
            .build();
}
