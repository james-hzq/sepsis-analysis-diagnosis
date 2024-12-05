package com.hzq.system.server.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hua
 * @className com.hzq.system.server.domain.entity SysUserRole
 * @date 2024/12/4 9:38
 * @description 系统用户角色多对多关系中间表
 */
@Data
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "sys_user_role")
public class SysUserRole {

    @EmbeddedId
    private SysUserRolePK pk;

    public SysUserRole() {

    }

    public SysUserRole(SysUserRolePK pk) {
        this.pk = pk;
    }
}
