package com.hzq.system.server.domain.entity;

import com.hzq.system.server.domain.dto.SysUserForm;
import com.hzq.web.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Optional;

/**
 * @author hua
 * @className com.hzq.system.server.domain.entity SysUser
 * @date 2024/9/26 14:35
 * @description 系统用户实体类
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_user")
public class SysUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id", columnDefinition="BIGINT UNSIGNED comment '自增主键,用户编号'")
    private Long userId;

    @Column(name="username", length = 30, nullable = false, columnDefinition="varchar(30) comment '用户名称'")
    private String username;

    @Column(name="password", columnDefinition="varchar(255) comment '用户名称'")
    private String password;

    @Column(name="email", length = 100, nullable = false, columnDefinition="varchar(100) comment '绑定邮箱'")
    private String email;

    @Column(name="avatar", columnDefinition="varchar(255) comment '用户头像'")
    private String avatar;

    @Column(name="status", columnDefinition="char(1) comment '用户状态(0: 是, 1: 否)'")
    private Character status;

    @Column(name="del_flag", columnDefinition="char(1) comment '是否删除(0: 是, 1: 否)'")
    private Character delFlag;
}
