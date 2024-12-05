package com.hzq.system.server.domain.entity;

import com.hzq.web.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

/**
 * @author hua
 * @className com.hzq.system.server.domain.entity SysRole
 * @date 2024/9/26 16:34
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
    @Column(name="role_id", nullable = false, columnDefinition="comment '自增主键,角色编号'")
    private Long roleId;

    @Column(name="role_key", unique = true, length = 30, nullable = false, columnDefinition="varchar(30) comment '角色key'")
    private String roleKey;

    @Column(name="role_name", length = 30, nullable = false, columnDefinition="varchar(30) comment '角色名称'")
    private String roleName;

    @Column(name="status", nullable = false, columnDefinition="char(1) comment '用户状态(0: 是, 1: 否)'")
    private Character status;
}
