package com.hzq.system.server.controller;

import com.hzq.core.result.Result;
import com.hzq.system.server.service.SysUserRoleService;
import com.hzq.web.exception.SystemException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hua
 * @className com.hzq.system.server.controller SysUserRoleController
 * @date 2024/10/1 16:03
 * @description TODO
 */
@AllArgsConstructor
@RestController
@RequestMapping("/system/user-role")
public class SysUserRoleController {
    private SysUserRoleService sysUserRoleService;

    @GetMapping("/roleIds/{userId}")
    public Result<List<Long>> selectRolesByUserId(@PathVariable("userId") Long userId) {
        if (userId == null) throw new SystemException("userId cannot be null");
        return Result.success(sysUserRoleService.selectRolesByUserId(userId));
    }
}
