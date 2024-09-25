package com.hzq.system.server.controller;

import com.hzq.core.result.Result;
import com.hzq.system.server.domain.dto.SysUserDTO;
import com.hzq.system.server.domain.entity.SysUser;
import com.hzq.system.server.service.SysUserService;
import com.hzq.web.exception.SystemException;
import com.hzq.web.validation.ValidationInterface;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author gc
 * @interface com.hzq.system.server.controller SysUserController
 * @date 2024/9/25 15:25
 * @description TODO
 */
@AllArgsConstructor
@RestController
@RequestMapping("/system/user")
public class SysUserController {

    private final SysUserService sysUserService;

    @GetMapping("/selectByUsername")
    public Result<SysUser> selectByUsername(String username) {
        SysUser sysUser = Optional.ofNullable(sysUserService.selectByUsername(username))
                .orElseThrow(() -> new SystemException("用户不存在"));
        return Result.success(sysUser);
    }

    @GetMapping("/list")
    public Result<List<SysUser>> list(@Validated(value = ValidationInterface.select.class) @RequestBody SysUserDTO sysUserDTO) {
        return Result.success(sysUserService.list(sysUserDTO));
    }

    @PostMapping("/insert")
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> insert(@Validated(value = ValidationInterface.add.class) @RequestBody SysUserDTO sysUserDTO) {
        int insert = sysUserService.insert(sysUserDTO);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<Void> update(@Validated(value = ValidationInterface.update.class) @RequestBody SysUserDTO sysUserDTO) {
        return null;
    }

    @DeleteMapping("/delete")
    public Result<Void> delete(String[] userIds) {
        return null;
    }
}
