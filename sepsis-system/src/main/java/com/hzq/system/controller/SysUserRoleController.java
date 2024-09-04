package com.hzq.system.controller;

import com.hzq.api.controller.system.SysUserRoleApi;
import com.hzq.api.entity.system.SysUserRole;
import com.hzq.core.result.Result;
import com.hzq.system.service.SysUserRoleService;
import com.hzq.web.exception.SystemException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * @author gc
 * @class com.hzq.system.controller SysUserRoleController
 * @date 2024/9/4 9:46
 * @description TODO
 */
@RestController
@AllArgsConstructor
public class SysUserRoleController implements SysUserRoleApi {
    private SysUserRoleService sysUserRoleService;

    @Override
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
