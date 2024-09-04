package com.hzq.api.feign.system;

import com.hzq.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author gc
 * @interface com.hzq.api.feign.system SysRoleFeignClient
 * @date 2024/9/4 10:39
 * @description TODO
 */
@FeignClient(value = "sepsis-system")
public interface SysRoleFeignClient {
    @GetMapping("/system/role/findRoleKeyByRoleIds")
    Result<List<String>> findRoleKeyByRoleIds(List<Long> list);
}
