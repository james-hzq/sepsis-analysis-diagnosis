package com.hzq.system.server.domain.dto;

import com.hzq.web.validation.ValidationInterface;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author hua
 * @className com.hzq.system.server.domain.dto SysRoleForm
 * @date 2024/12/11 16:10
 * @description 角色CRUD表单
 */
@Data
@ToString
@EqualsAndHashCode
public class SysRoleForm {

    @NotBlank(message = "当前操作用户不得为空", groups = {
            ValidationInterface.create.class,
            ValidationInterface.read.class,
            ValidationInterface.update.class,
            ValidationInterface.delete.class,
    })
    private String currUsername;

    @NotBlank(message = "角色编号不得为空", groups = {
            ValidationInterface.update.class,
            ValidationInterface.delete.class
    })
    private String roleId;

    @NotBlank(message = "角色标识不得为空", groups = {
            ValidationInterface.create.class,
            ValidationInterface.update.class
    })
    private String roleKey;

    @NotBlank(message = "角色名称不得为空", groups = {
            ValidationInterface.create.class,
            ValidationInterface.update.class
    })
    private String roleName;


    @NotEmpty(message = "角色状态不得为空", groups = {
            ValidationInterface.create.class,
            ValidationInterface.update.class
    })
    private Character status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
