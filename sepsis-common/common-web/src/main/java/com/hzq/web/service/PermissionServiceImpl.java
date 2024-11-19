package com.hzq.web.service;

import com.google.common.collect.ImmutableSet;
import com.hzq.core.result.ResultEnum;
import com.hzq.security.service.PermissionService;
import com.hzq.security.util.PermissionUtils;
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

    /**
     * @param roles 接口上的角色字符串
     * @return boolean
     * @author hzq
     * @date 2024/11/19 14:43
     * @apiNote 校验用户是否同时拥有接口要求的所有权限
     **/
    @Override
    public boolean hasRolesAnd(String roles) {
        return checkRoles(roles, true);
    }

    /**
     * @param roles 接口上的角色字符串
     * @return boolean
     * @author hzq
     * @date 2024/11/19 14:44
     * @apiNote 校验用户是否同时拥有接口要求的所有权限
     **/
    @Override
    public boolean hasRolesOr(String roles) {
        // 校验用户是否拥有接口要求的任意权限
        return checkRoles(roles, false);
    }

    private boolean checkRoles(String roles, boolean requireAll) {
        // 获取当前用户的认证对象
        Authentication authentication = getAuthentication();

        // 获取用户当前拥有的权限信息并按层级分组
        Set<String> ownedRoles = ImmutableSet.copyOf(SecurityUtils.getRoles(authentication));
        Set<String>[] ownedRoleGroups = PermissionUtils.RoleComparator.getGroupRoles(ownedRoles);

        // 获取接口要求的权限信息并按层级分组
        String[] requiredRoles = roles.split(",");
        Set<String>[] requiredRoleGroups = PermissionUtils.RoleComparator.getGroupRoles(requiredRoles);

        // 根据校验类型进行权限匹配
        return requireAll
                ? PermissionUtils.RoleComparator.checkRolesAnd(ownedRoleGroups, requiredRoleGroups)
                : PermissionUtils.RoleComparator.checkRolesOr(ownedRoleGroups, requiredRoleGroups);
    }

    /**
     * @return org.springframework.security.core.Authentication
     * @author hzq
     * @date 2024/11/19 14:44
     * @apiNote 获取当前认证对象
     **/
    private Authentication getAuthentication() {
        return Optional.ofNullable(SecurityUtils.getAuthentication())
                .orElseThrow(() -> new SystemException(ResultEnum.ACCESS_FORBIDDEN));
    }
}
