package com.hzq.security.service;

/**
 * @author gc
 * @interface com.hzq.security.service PermissionService
 * @date 2024/11/18 16:54
 * @description 权限校验接口common-security只提供权限校验的抽象层，具体的实现由下方微服务实现
 */
public interface PermissionService {

    boolean hasRoles(String permission);
}
