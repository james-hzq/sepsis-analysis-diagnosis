package com.hzq.system.server.controller;

import com.google.common.base.Strings;
import com.hzq.core.result.Result;
import com.hzq.system.dto.SysUserDTO;
import com.hzq.system.server.service.SysUserService;
import com.hzq.web.exception.SystemException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public Result<SysUserDTO> selectSysUserByUsername(@PathVariable("username") String username) {
        if (Strings.isNullOrEmpty(username))
            throw new SystemException("用户名不得为空");
        return Result.success(sysUserService.selectSysUserByUsername(username));
    }
}
