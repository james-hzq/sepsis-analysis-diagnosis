package com.hzq.api.feign.system;

import com.hzq.api.entity.system.SysUser;
import com.hzq.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author gc
 * @interface com.hzq.api.feign SysUserFeignClient
 * @date 2024/9/3 14:44
 * @description TODO
 */
@FeignClient(value = "sepsis-system")
public interface SysUserFeignClient {

    @GetMapping("/system/user/selectByUsername")
    Result<SysUser> selectByUsername(@RequestParam("username") String username);
}
