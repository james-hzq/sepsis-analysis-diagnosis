package com.hzq.api.controller.system;

import com.hzq.core.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author gc
 * @interface com.hzq.api.controller.system SysUserRoleApi
 * @date 2024/9/4 9:44
 * @description TODO
 */
@RequestMapping("/system/user-role")
public interface SysUserRoleApi {

    @GetMapping("/roleIdsByUserId")
    Result<List<Long>> findRoleIdsByUserId(Long userId);
}
