package com.hzq.system.server.domain.dto;

import com.hzq.web.validation.ValidationInterface;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author gc
 * @class com.hzq.api.domain.system SysRoleDTO
 * @date 2024/8/30 15:43
 * @description TODO
 */
@Data
@ToString
public class SysRoleDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "roleId不得为空", groups = {
            ValidationInterface.delete.class,
            ValidationInterface.update.class,
    })
    private Long roleId;

    @NotBlank(message = "角色标识不得为空", groups = {
            ValidationInterface.add.class,
            ValidationInterface.update.class,
    })
    @Length(min = 1, max = 20, message = "角色标识长度必须在 1 ~ 20 个字符之间", groups = {
            ValidationInterface.add.class,
            ValidationInterface.update.class,
    })
    private String roleKey;

    @NotBlank(message = "角色名称不得为空", groups = {
            ValidationInterface.add.class,
            ValidationInterface.update.class,
    })
    @Length(min = 1, max = 20, message = "角色名称长度必须在 1 ~ 20 个字符之间", groups = {
            ValidationInterface.add.class,
            ValidationInterface.update.class,
    })
    private String roleName;

    @NotNull(message = "角色状态不得为空", groups = {
            ValidationInterface.add.class,
            ValidationInterface.update.class
    })
    private Character status;

    private Date startTime;
    private Date endTime;
}
