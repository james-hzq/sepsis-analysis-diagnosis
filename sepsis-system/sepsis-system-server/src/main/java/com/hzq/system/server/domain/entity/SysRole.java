package com.hzq.system.server.domain.entity;

import com.hzq.web.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author gc
 * @class com.hzq.api.entity.system SysRole
 * @date 2024/8/30 15:33
 * @description 系统角色实体类
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_role")
public class SysRole extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;
    private String roleKey;
    private String roleName;
    private Character status;
}
