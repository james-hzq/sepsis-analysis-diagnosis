package com.hzq.system.controller;

import com.hzq.api.controller.system.SysUserApi;
import com.hzq.api.pojo.system.SysUserDTO;
import com.hzq.core.result.Result;
import com.hzq.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gc
 * @class com.hzq.system.controller SysUserController
 * @date 2024/8/29 10:03
 * @description TODO
 */
@RestController
public class SysUserController implements SysUserApi {
    private final SysUserService sysUserService;

    @Autowired
    public SysUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Override
    public Result<?> list(SysUserDTO sysUserDTO) {
        return Result.success(sysUserService.list(sysUserDTO));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> insert(SysUserDTO sysUserDTO) {
        int insert = sysUserService.insert(sysUserDTO);
        return Result.success();
    }

    @Override
    public Result<?> update(SysUserDTO sysUserDTO) {
        return null;
    }

    @Override
    public Result<?> delete(String[] userIds) {
        return null;
    }
}
