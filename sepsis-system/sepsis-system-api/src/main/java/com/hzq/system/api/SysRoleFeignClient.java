package com.hzq.system.api;

import com.hzq.core.result.Result;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author gc
 * @interface com.hzq.system.api SysRoleFeignClient
 * @date 2024/10/8 16:54
 * @description 角色远程调用服务
 */
@FeignClient(contextId = "sys-role", name = "sepsis-system", path = "/system/role")
public interface SysRoleFeignClient {

    @GetMapping("/roleKeys/{roleIds}")
    Result<List<String>> selectRoleKeys(@PathVariable("roleIds") List<Long> roleIds);
}
