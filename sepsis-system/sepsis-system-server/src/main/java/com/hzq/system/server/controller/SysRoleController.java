package com.hzq.system.server.controller;

import com.hzq.core.result.Result;
import com.hzq.system.server.service.SysRoleService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * @author hua
 * @className com.hzq.system.server.controller SysRoleController
 * @date 2024/10/1 15:59
 * @description 系统角色请求处理器
 */
@AllArgsConstructor
@RestController
@RequestMapping("/system/role")
public class SysRoleController {
    private SysRoleService sysRoleService;

    /**
     * @param roleIds 角色唯一标识集合
     * @return com.hzq.core.result.Result<java.util.List<java.lang.String>>
     * @author gc
     * @date 2024/10/8 16:36
     * @apiNote 根据角色唯一标识集合查询角色Key的集合
     **/
    @GetMapping("/roleKeys/{roleIds}")
    public Result<Set<String>> selectRoleKeys(@PathVariable("roleIds") @NotNull(message = "roleIds不得为空") List<Long> roleIds) {
        return Result.success(sysRoleService.selectRoleKeys(roleIds));
    }
}
