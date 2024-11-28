package com.hzq.system.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author gc
 * @interface com.hzq.system.api SysUserRoleFeignClient
 * @date 2024/10/8 11:04
 * @description 用户角色远程调用服务
 */
@FeignClient(contextId = "sys-user-role", name = "sepsis-system", path = "/system/user-role")
public interface SysUserRoleFeignClient {

    @GetMapping("/roleIds/{userId}")
    List<Long> selectRolesByUserId(@PathVariable("userId") Long userId);
}
