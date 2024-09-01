package com.hzq.system.controller;

import com.hzq.api.controller.system.SysLoginApi;
import com.hzq.api.pojo.system.LoginBodyDTO;
import com.hzq.core.result.Result;
import com.hzq.system.service.SysLoginService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gc
 * @class com.hzq.system.controller SysLoginController
 * @date 2024/8/29 9:36
 * @description TODO
 */
@RestController
public class SysLoginController implements SysLoginApi {
    private final SysLoginService sysLoginService;

    @Autowired
    public SysLoginController(SysLoginService sysLoginService) {
        this.sysLoginService = sysLoginService;
    }

    @Override
    public Result<?> login(LoginBodyDTO loginBodyDTO) {
        return null;
    }

    @Override
    public Result<?> getRouters(HttpServletRequest request) {
        return null;
    }
}
