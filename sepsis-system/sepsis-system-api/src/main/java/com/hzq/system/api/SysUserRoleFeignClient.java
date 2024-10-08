package com.hzq.system.api;

import com.hzq.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author gc
 * @interface com.hzq.system.api SysUserRoleFeignClient
 * @date 2024/10/8 11:04
 * @description TODO
 */
@FeignClient(contextId = "sys-user-role", name = "sepsis-system")
public interface SysUserRoleFeignClient {
    @GetMapping("/system/user-role/roleIds/{userId}")
    Result<List<Long>> selectRolesByUserId(@PathVariable("userId") Long userId);
}
