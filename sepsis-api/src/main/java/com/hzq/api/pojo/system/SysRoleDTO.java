package com.hzq.api.pojo.system;

import com.hzq.web.validation.ValidationInterface;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
