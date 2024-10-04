package com.hzq.system.server.controller;

import com.hzq.system.server.service.SysMenuService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hua
 * @className com.hzq.system.server.controller SysMenuController
 * @date 2024/10/1 16:08
 * @description TODO
 */
@AllArgsConstructor
@RestController
@RequestMapping("/system/menu")
public class SysMenuController {
    private SysMenuService sysMenuService;
}
