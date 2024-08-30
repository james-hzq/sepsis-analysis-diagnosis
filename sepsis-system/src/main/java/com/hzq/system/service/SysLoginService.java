package com.hzq.system.service;

import com.hzq.api.controller.system.SysLoginApi;
import com.hzq.common.exception.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hua
 * @className com.hua.system.service SysLoginService
 * @date 2024/8/24 10:30
 * @description TODO
 */
@Service
public class SysLoginService implements SysLoginApi {
    @Override
    public Result<?> login() {
        return Result.success();
    }
}
