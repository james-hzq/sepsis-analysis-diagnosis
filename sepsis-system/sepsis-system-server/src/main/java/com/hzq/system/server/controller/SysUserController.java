package com.hzq.system.server.controller;

import com.google.common.base.Strings;
import com.hzq.core.result.Result;
import com.hzq.core.result.ResultEnum;
import com.hzq.system.dto.SysUserDTO;
import com.hzq.system.server.domain.dto.SysUserForm;
import com.hzq.system.server.domain.vo.SysUserVO;
import com.hzq.system.server.service.SysUserService;
import com.hzq.web.exception.SystemException;
import com.hzq.web.validation.ValidationInterface;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author hua
 * @className com.hzq.system.server.controller SysUserController
 * @date 2024/9/26 14:41
 * @description 系统用户请求处理器
 */
@AllArgsConstructor
@RestController
@RequestMapping("/system/user")
public class SysUserController {
    private final SysUserService sysUserService;

    /**
     * @param username 用户名
     * @return com.hzq.core.result.Result<com.hzq.system.dto.SysUserDTO>
     * @author gc
     * @date 2024/10/8 16:35
     * @apiNote 根据用户名查询用户信息
     **/
    @GetMapping("/username/{username}")
    public Result<SysUserDTO> selectSysUserByUsername(@PathVariable("username") String username) {
        if (Strings.isNullOrEmpty(username))
            throw new SystemException("查询用户名不得为空");
        return Result.success(sysUserService.selectSysUserByUsername(username));
    }

    /**
     * @param sysUserForm 用户查询表单对象
     * @return com.hzq.core.result.Result<java.util.List<com.hzq.system.server.domain.vo.SysUserVO>>
     * @author gc
     * @date 2024/10/8 16:36
     * @apiNote 根据用户表单查询用户信息
     **/
    @GetMapping("/list")
    public Result<List<SysUserVO>> list(@Validated @RequestBody SysUserForm sysUserForm) {
        List<SysUserVO> sysUserVOList = Optional.ofNullable(sysUserService.list(sysUserForm))
                .orElseThrow(() -> new SystemException(ResultEnum.SERVER_ERROR));
        return Result.success(sysUserVOList);
    }
}
