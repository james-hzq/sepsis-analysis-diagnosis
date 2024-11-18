package com.hzq.web.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.hzq.core.result.ResultEnum;
import com.hzq.security.service.PermissionService;
import com.hzq.security.util.SecurityUtils;
import com.hzq.web.exception.SystemException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author gc
 * @class com.hzq.web.service PermissionServiceImpl
 * @date 2024/11/18 17:54
 * @description 权限校验
 */
@Service("ps")
public class PermissionServiceImpl implements PermissionService {

    private static final Queue<String> rolesQueue = new ArrayDeque<>() {{
        offer("root");
        offer("admin");
        offer("user");
    }};

    @Override
    public boolean hasRoles(String permission) {
        // 从安全上下文中获取认证对象
        Authentication authentication = Optional.ofNullable(SecurityUtils.getAuthentication())
                .orElseThrow(() -> new SystemException(ResultEnum.ACCESS_FORBIDDEN));
        // 从安全上下文中获取权限信息
        Set<String> roles = ImmutableSet.copyOf(SecurityUtils.getRoles(authentication));
        // 将方法注解上的权限字符串转换为Set<String>
        String[] requiredPermission = permission.split(",");


        return false;
    }
}
