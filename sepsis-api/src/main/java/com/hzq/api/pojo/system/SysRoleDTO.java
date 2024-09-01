package com.hzq.api.pojo.system;

import com.hzq.web.validation.ValidationInterface;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public String getRoleName() {
        return roleName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
