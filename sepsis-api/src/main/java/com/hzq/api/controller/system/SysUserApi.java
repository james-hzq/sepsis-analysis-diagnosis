package com.hzq.api.controller.system;

import com.hzq.api.domain.system.SysUserDTO;
import com.hzq.api.entity.system.SysUserEntity;
import com.hzq.common.exception.Result;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author hua
 * @interfaceName com.hzq.api.system SysUserApi
 * @date 2024/8/28 22:54
 * @description TODO
 */
@RequestMapping("/system/user")
public interface SysUserApi {
    @GetMapping("/list")
    Result<?> list();

    @PostMapping("/insert")
    Result<?> insert(@Valid @RequestBody SysUserDTO sysUser);

    @PutMapping("/update")
    Result<?> update(@Valid @RequestBody SysUserDTO sysUser);

    @DeleteMapping("/delete")
    Result<?> delete(String[] userIds);
}
