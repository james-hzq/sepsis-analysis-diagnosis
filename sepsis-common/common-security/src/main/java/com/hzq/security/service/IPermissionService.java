package com.hzq.security.service;

/**
 * @author gc
 * @interface com.hzq.security.service PermissionService
 * @date 2024/11/18 16:54
 * @description 权限校验接口common-security只提供权限校验的抽象层，具体的实现由下方微服务实现
 */
public interface IPermissionService {

    /**
     * @param roles 角色
     * @return boolean
     * @author hzq
     * @date 2024/11/19 14:33
     * @apiNote 含有 角色1 && 角色2 .......
     **/
    boolean hasRolesAnd(String roles);

    /**
     * @param roles 角色
     * @return boolean
     * @author hzq
     * @date 2024/11/19 14:33
     * @apiNote 含有 角色1 || 角色2 .......
     **/
    boolean hasRolesOr(String roles);
}
