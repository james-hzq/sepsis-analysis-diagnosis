package com.hzq.system.server.controller;

import com.google.common.base.Strings;
import com.hzq.core.result.Result;
import com.hzq.core.result.ResultEnum;
import com.hzq.security.annotation.RequiresPermissions;
import com.hzq.system.dto.SysUserRoleDTO;
import com.hzq.system.server.domain.dto.SysUserForm;
import com.hzq.system.server.domain.vo.SysUserRoleVO;
import com.hzq.system.server.service.SysUserService;
import com.hzq.web.exception.SystemException;
import com.hzq.web.util.PageUtils;
import com.hzq.web.validation.ValidationInterface;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hua
 * @className com.hzq.system.server.controller SysUserController
 * @date 2024/9/26 14:41
 * @description 系统用户请求处理器
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/user")
public class SysUserController {

    private final SysUserService sysUserService;

    /**
     * @param username 用户名
     * @return com.hzq.core.result.Result<com.hzq.system.dto.SysUserDTO>
     * @author gc
     * @date 2024/10/8 16:35
     * @apiNote 根据用户名查询用户信息
     **/
    @GetMapping("/username/{username}")
    public SysUserRoleDTO selectSysUserWithRolesByUsername(
            @PathVariable("username") @NotBlank(message = "查询用户名不得为空") String username
    ) {
        return sysUserService.selectSysUserWithRolesByUsername(username);
    }

    /**
     * @param sysUserForm 用户查询表单对象
     * @return com.hzq.core.result.Result<java.util.List < com.hzq.system.server.domain.vo.SysUserRoleVO>>
     * @author gc
     * @date 2024/10/8 16:36
     * @apiNote 根据用户表单查询用户信息(附带角色)
     **/
    @PostMapping("/list")
    @RequiresPermissions("@ps.hasRolesAnd('admin')")
    public Result<Page<SysUserRoleVO>> list(
            @Validated(value = {ValidationInterface.read.class}) @RequestBody SysUserForm sysUserForm
    ) {
        Pageable pageable = PageUtils.buildPageRequest();
        return Result.success(sysUserService.list(sysUserForm, pageable));
    }

    /**
     * @author hua
     * @date 2024/12/5 16:01
     * @apiNote 新增用户(包括角色)
     **/
    @PostMapping("/create")
    @RequiresPermissions("@ps.hasRolesAnd('admin')")
    public Result<Void> create(
            @Validated(value = {ValidationInterface.create.class}) @RequestBody SysUserForm sysUserForm
    ) {
        sysUserService.createSysUserBySysUserForm(sysUserForm);
        return Result.success();
    }

    /**
     * @author hua
     * @date 2024/12/10 9:22
     * @return com.hzq.core.result.Result<java.lang.Void>
     * @apiNote 更新用户（包括角色）
     **/
    @PostMapping("/update")
    @RequiresPermissions("@ps.hasRolesAnd('admin')")
    public Result<Void> update(
        @Validated(value = {ValidationInterface.update.class}) @RequestBody SysUserForm sysUserForm
    ) {
        sysUserService.updateSysUserBySysUserForm(sysUserForm);
        return Result.success();
    }

    @PostMapping("/delete")
    @RequiresPermissions("@ps.hasRolesAnd('admin')")
    public Result<Void> delete(@RequestBody String userId) {
        if (Strings.isNullOrEmpty(userId)) throw new SystemException(ResultEnum.USER_ID_NOT_EMPTY);
        sysUserService.deleteSysUserBySysUserForm(userId);
        return Result.success();
    }
}
