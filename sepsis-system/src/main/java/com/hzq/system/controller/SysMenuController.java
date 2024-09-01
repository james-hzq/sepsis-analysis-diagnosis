package com.hzq.system.controller;

import com.hzq.api.controller.system.SysMenuApi;
import com.hzq.core.result.Result;
import com.hzq.system.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hua
 * @className com.hzq.system.controller SysMenuController
 * @date 2024/8/31 10:05
 * @description TODO
 */
@RestController
public class SysMenuController implements SysMenuApi {
    private final SysMenuService sysMenuService;

    @Autowired
    public SysMenuController(SysMenuService sysMenuService) {
        this.sysMenuService = sysMenuService;
    }

    @Override
    public Result<?> list() {
        return null;
    }
}
