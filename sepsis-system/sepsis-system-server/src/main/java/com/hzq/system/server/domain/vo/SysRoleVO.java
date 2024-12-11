package com.hzq.system.server.domain.vo;

import com.hzq.system.server.domain.entity.SysRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author hua
 * @className com.hzq.system.server.domain.vo SysRoleVO
 * @date 2024/12/11 16:06
 * @description 用于前端角色展示的对象
 */
@Data
@Accessors(chain = true)
@ToString
@EqualsAndHashCode
public class SysRoleVO {
    private Long roleId;
    private String roleKey;
    private String roleName;
    private Character status;
    private LocalDateTime createTime;
    private String createBy;
    private LocalDateTime updateTime;
    private String updateBy;

    public SysRoleVO() {
    }

    public SysRoleVO(SysRole sysRole) {
        this.roleId = sysRole.getRoleId();
        this.roleKey = sysRole.getRoleKey();
        this.roleName = sysRole.getRoleName();
        this.status = sysRole.getStatus();
        this.createTime = sysRole.getCreateTime();
        this.createBy = sysRole.getCreateBy();
        this.updateTime = sysRole.getUpdateTime();
        this.updateBy = sysRole.getUpdateBy();
    }
}
