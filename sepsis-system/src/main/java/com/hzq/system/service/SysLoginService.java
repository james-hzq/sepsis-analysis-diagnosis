package com.hzq.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gc
 * @class com.hzq.system.service SysLoginService
 * @date 2024/8/29 9:47
 * @description TODO
 */
@Service
public class SysLoginService {
    private final SysUserService sysUserService;

    @Autowired
    public SysLoginService(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    public String login() {
        return "";
    }
}
