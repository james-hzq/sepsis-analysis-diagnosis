package com.hzq.system.controller;

import com.hzq.api.controller.system.SysUserApi;
import com.hzq.api.entity.system.SysUserEntity;
import com.hzq.common.exception.Result;
import com.hzq.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Result<?> list() {
        return null;
    }

    @Override
    public Result<?> insert(SysUserEntity sysUser) {
        return null;
    }

    @Override
    public Result<?> update(SysUserEntity sysUser) {
        return null;
    }

    @Override
    public Result<?> delete(String[] userIds) {
        return null;
    }
}
