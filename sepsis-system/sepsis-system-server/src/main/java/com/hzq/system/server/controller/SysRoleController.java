package com.hzq.system.server.controller;

import com.hzq.core.result.Result;
import com.hzq.system.server.domain.dto.SysRoleDTO;
import com.hzq.system.server.domain.entity.SysRole;
import com.hzq.system.server.service.SysRoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gc
 * @interface com.hzq.system.server.controller SysRoleController
 * @date 2024/9/25 15:33
 * @description TODO
 */
@AllArgsConstructor
@RestController
@RequestMapping("/system/role")
public class SysRoleController {
    private final SysRoleService sysRoleService;

    @GetMapping("/list")
    public Result<List<SysRole>> list(@RequestBody SysRoleDTO sysRoleDTO) {
        List<SysRole> list = sysRoleService.list(sysRoleDTO);
        return Result.success(list);
    }

    @GetMapping("/findRoleKeyByRoleIds")
    public Result<List<String>> findRoleKeyByRoleIds(List<Long> list) {
        List<String> resList = sysRoleService.findRoleKeyByRoleIds(list);
        return Result.success(resList);
    }
}
