package com.hzq.api.controller.system;

import com.hzq.core.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author hua
 * @interfaceName com.hzq.api.controller.system SysMenuApi
 * @date 2024/8/31 10:04
 * @description TODO
 */
@RequestMapping("/system/menu")
public interface SysMenuApi {

    @GetMapping("/list")
    Result<?> list();
}
