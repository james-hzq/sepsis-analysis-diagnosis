package com.hzq.system.server.domain.entity;

import com.hzq.web.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

/**
 * @author hua
 * @className com.hzq.api.entity.system SysUserEntity
 * @date 2024/8/28 22:09
 * @description 系统用户实体类
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_user")
public class SysUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String avatar;
    private Character status;
    private Character delFlag;
    @Transient
    private Set<String> roles;
    @Transient
    private Set<String> perms;
}
