package com.hzq.system.server.controller;

import com.hzq.core.result.Result;
import com.hzq.system.server.domain.entity.SysMenu;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gc
 * @interface com.hzq.system.server.controller SysMenuController
 * @date 2024/9/25 15:36
 * @description TODO
 */
@AllArgsConstructor
@RestController
@RequestMapping("/system/menu")
public class SysMenuController {

    @GetMapping("/list")
    public Result<List<SysMenu>> list() {
        return null;
    }
}
