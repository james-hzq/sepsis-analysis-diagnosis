package com.hzq.system.service;

import com.hzq.core.result.Result;
import org.springframework.stereotype.Service;

/**
 * @author hua
 * @className com.hua.system.service SysLoginService
 * @date 2024/8/24 10:30
 * @description TODO
 */
@Service
public class SysLoginService {
    public Result<?> login() {
        return Result.success();
    }
}
