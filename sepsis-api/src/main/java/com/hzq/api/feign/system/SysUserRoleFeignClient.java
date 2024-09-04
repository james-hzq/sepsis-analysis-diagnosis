package com.hzq.api.feign.system;

import com.hzq.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author gc
 * @interface com.hzq.api.feign.system SysUserRoleFeginClient
 * @date 2024/9/4 9:42
 * @description TODO
 */
@FeignClient(value = "sepsis-system")
public interface SysUserRoleFeignClient {

    @GetMapping("/system/user-role/roleIdsByUserId")
    Result<List<Long>> findRoleIdsByUserId(Long userId);
}
