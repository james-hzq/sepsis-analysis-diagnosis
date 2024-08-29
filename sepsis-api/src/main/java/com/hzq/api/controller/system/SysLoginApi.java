package com.hzq.api.controller.system;

import com.hzq.common.exception.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hua
 * @interfaceName com.hua.api.system SysLoginApi
 * @date 2024/8/24 10:29
 * @description TODO
 */
@RequestMapping("/system")
public interface SysLoginApi {

    @RequestMapping("/login")
    Result<?> login();
}
