package com.hzq.system.server.controller;

import com.hzq.system.server.service.SysUserRoleService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hua
 * @className com.hzq.system.server.controller SysUserRoleController
 * @date 2024/10/1 16:03
 * @description 用户角色关联表请求处理器
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/user-role")
public class SysUserRoleController {

    private final SysUserRoleService sysUserRoleService;

    /**
     * @param userId 用户唯一标识
     * @return com.hzq.core.result.Result<java.util.List<java.lang.Long>>
     * @author gc
     * @date 2024/10/8 16:37
     * @apiNote 根据 userId 查询当前用户所属的角色的 roleId 集合
     **/
    @GetMapping("/roleIds/{userId}")
    public List<Long> selectRolesByUserId(
            @PathVariable("userId") @NotNull(message = "userId不得为空") Long userId
    ) {
        return sysUserRoleService.selectRolesByUserId(userId);
    }
}
