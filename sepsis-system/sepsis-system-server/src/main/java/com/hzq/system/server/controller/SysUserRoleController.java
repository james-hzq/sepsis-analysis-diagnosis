package com.hzq.system.server.controller;

import com.hzq.core.result.Result;
import com.hzq.system.server.domain.entity.SysUserRole;
import com.hzq.system.server.service.SysUserRoleService;
import com.hzq.web.exception.SystemException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * @author gc
 * @interface com.hzq.system.server.controller SysUserRoleController
 * @date 2024/9/25 15:34
 * @description TODO
 */
@AllArgsConstructor
@RestController
@RequestMapping("/system/user-role")
public class SysUserRoleController {
    private SysUserRoleService sysUserRoleService;

    @GetMapping("/roleIdsByUserId")
    public Result<List<Long>> findRoleIdsByUserId(Long userId) {
        if (userId == null) throw new SystemException("userId is null");
        // 从数据库根据 userId 查找出对应的 角色映射
        List<SysUserRole> list = Optional.ofNullable(sysUserRoleService.findSysUserRoleByUserId(userId))
                .orElseThrow(() -> new SystemException("无法根据用户ID找到角色信息"));
        // 提取出 userId 对应的 角色Id
        List<Long> resList = list.stream().map(sysUserRole -> sysUserRole.getId().getRoleId()).toList();
        return Result.success(resList);
    }
}
