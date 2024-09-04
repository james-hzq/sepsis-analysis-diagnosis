package com.hzq.api.controller.system;

import com.hzq.api.pojo.system.SysRoleDTO;
import com.hzq.core.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author gc
 * @interface com.hzq.api.controller.system SysRoleApi
 * @date 2024/8/30 15:43
 * @description TODO
 */
@RequestMapping("/system/role")
public interface SysRoleApi {

    @GetMapping("/list")
    Result<?> list(@RequestBody SysRoleDTO sysRoleDTO);

    @GetMapping("/findRoleKeyByRoleIds")
    Result<List<String>> findRoleKeyByRoleIds(List<Long> list);
}
