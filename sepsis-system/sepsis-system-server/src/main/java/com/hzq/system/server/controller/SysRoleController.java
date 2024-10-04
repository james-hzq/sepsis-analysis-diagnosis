package com.hzq.system.server.controller;

import com.hzq.system.server.service.SysRoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hua
 * @className com.hzq.system.server.controller SysRoleController
 * @date 2024/10/1 15:59
 * @description TODO
 */
@AllArgsConstructor
@RestController
@RequestMapping("/system/role")
public class SysRoleController {
    private SysRoleService sysRoleService;
}
