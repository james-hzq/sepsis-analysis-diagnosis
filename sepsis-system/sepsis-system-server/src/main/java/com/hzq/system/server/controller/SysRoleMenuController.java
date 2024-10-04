package com.hzq.system.server.controller;

import com.hzq.system.server.service.SysRoleMenuService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hua
 * @className com.hzq.system.server.controller SysRoleMenuController
 * @date 2024/10/1 16:05
 * @description TODO
 */
@AllArgsConstructor
@RestController
@RequestMapping("/system/role-menu")
public class SysRoleMenuController {
    private SysRoleMenuService sysRoleMenuService;
}
