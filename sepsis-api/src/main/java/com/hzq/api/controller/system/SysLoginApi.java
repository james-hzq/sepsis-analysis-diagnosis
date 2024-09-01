package com.hzq.api.controller.system;

import com.hzq.api.pojo.system.LoginBodyDTO;
import com.hzq.core.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author hua
 * @interfaceName com.hua.api.system SysLoginApi
 * @date 2024/8/24 10:29
 * @description TODO
 */
@RequestMapping("/system")
public interface SysLoginApi {

    @RequestMapping("/login")
    Result<?> login(@Valid @RequestBody LoginBodyDTO loginBodyDTO);

    @GetMapping("/system/getRouters")
    Result<?> getRouters(HttpServletRequest request);
}
