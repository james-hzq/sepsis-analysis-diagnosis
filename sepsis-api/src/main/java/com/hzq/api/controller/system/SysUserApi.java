package com.hzq.api.controller.system;
import com.hzq.api.domain.system.SysUserDTO;
import com.hzq.common.exception.Result;
import com.hzq.common.util.validation.ValidationInterface;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author hua
 * @interfaceName com.hzq.api.system SysUserApi
 * @date 2024/8/28 22:54
 * @description TODO
 */
@RequestMapping("/system/user")
public interface SysUserApi {
    @GetMapping("/list")
    Result<?> list(@Validated(value = ValidationInterface.select.class) @RequestBody SysUserDTO sysUserDTO);

    @PostMapping("/insert")
    Result<?> insert(@Validated(value = ValidationInterface.add.class) @RequestBody SysUserDTO sysUserDTO);

    @PutMapping("/update")
    Result<?> update(@Validated(value = ValidationInterface.update.class) @RequestBody SysUserDTO sysUserDTO);

    @DeleteMapping("/delete")
    Result<?> delete(String[] userIds);
}
