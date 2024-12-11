package com.hzq.system.server.controller;

import com.hzq.core.result.Result;
import com.hzq.security.annotation.RequiresPermissions;
import com.hzq.system.server.domain.dto.SysRoleForm;
import com.hzq.system.server.domain.vo.SysRoleVO;
import com.hzq.system.server.service.SysRoleService;
import com.hzq.web.validation.ValidationInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hua
 * @className com.hzq.system.server.controller SysRoleController
 * @date 2024/10/1 15:59
 * @description 系统角色请求处理器
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/role")
public class SysRoleController {

    private final SysRoleService sysRoleService;

    @PostMapping("/list")
    @RequiresPermissions("@ps.hasRolesAnd('admin')")
    public Result<List<SysRoleVO>> list(
            @Validated(value = {ValidationInterface.read.class}) @RequestBody SysRoleForm sysRoleForm
    ) {
        return Result.success(sysRoleService.list(sysRoleForm));
    }
}
