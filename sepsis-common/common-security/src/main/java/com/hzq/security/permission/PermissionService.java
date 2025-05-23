package com.hzq.security.permission;

import com.google.common.collect.ImmutableSet;
import com.hzq.security.authorization.AuthorizationContext;
import com.hzq.security.authorization.AuthorizationInfo;
import com.hzq.security.util.PermissionUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author gc
 * @class com.hzq.web.service PermissionServiceImpl
 * @date 2024/11/18 17:54
 * @description 权限校验
 */
@Service("ps")
@Setter
@Getter
public class PermissionService {
    public final String ps = "ps";

    /**
     * @param roles 角色
     * @return boolean
     * @author hzq
     * @date 2024/11/19 14:33
     * @apiNote 含有 角色1 && 角色2 .......
     **/
    public boolean hasRolesAnd(String roles) {
        return checkRoles(roles, true);
    }

    /**
     * @param roles 角色
     * @return boolean
     * @author hzq
     * @date 2024/11/19 14:33
     * @apiNote 含有 角色1 || 角色2 .......
     **/
    public boolean hasRolesOr(String roles) {
        // 校验用户是否拥有接口要求的任意权限
        return checkRoles(roles, false);
    }

    private boolean checkRoles(String roles, boolean requireAll) {
        AuthorizationInfo authorizationInfo = AuthorizationContext.getAuthorizationInfo();
        if (authorizationInfo == null) return false;
        // 获取用户当前拥有的权限信息并按层级分组
        Set<String> ownedRoles = ImmutableSet.copyOf(authorizationInfo.getRoles());
        Set<String>[] ownedRoleGroups = PermissionUtils.RoleComparator.getGroupRoles(ownedRoles);

        // 获取接口要求的权限信息并按层级分组
        String[] requiredRoles = roles.split(",");
        Set<String>[] requiredRoleGroups = PermissionUtils.RoleComparator.getGroupRoles(requiredRoles);

        // 根据校验类型进行权限匹配
        return requireAll
                ? PermissionUtils.RoleComparator.checkRolesAnd(ownedRoleGroups, requiredRoleGroups)
                : PermissionUtils.RoleComparator.checkRolesOr(ownedRoleGroups, requiredRoleGroups);
    }
}
