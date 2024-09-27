package com.hzq.system.server.controller;

import com.google.common.base.Strings;
import com.hzq.core.result.Result;
import com.hzq.system.server.domain.dto.SysUserDTO;
import com.hzq.system.server.domain.entity.SysUser;
import com.hzq.system.server.service.SysUserService;
import com.hzq.web.exception.SystemException;
import com.hzq.web.validation.ValidationInterface;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hua
 * @className com.hzq.system.server.controller SysUserController
 * @date 2024/9/26 14:41
 * @description 系统用户请求处理器
 */
@AllArgsConstructor
@RestController
@RequestMapping("/system/user")
public class SysUserController {
    private final SysUserService sysUserService;

    @GetMapping("/username/{username}")
    public Result<SysUser> getSysUserByUsername(@PathVariable("username") String username) {
        if (Strings.isNullOrEmpty(username))
            throw new SystemException("username is null or empty");
        return Result.success(sysUserService.getSysUserByUsername(username));
    }

    @GetMapping("/list")
    public Result<List<SysUser>> list(@Validated(value = ValidationInterface.select.class) @RequestBody SysUserDTO sysUserDTO) {
        return Result.success(sysUserService.list(sysUserDTO));
    }
}
