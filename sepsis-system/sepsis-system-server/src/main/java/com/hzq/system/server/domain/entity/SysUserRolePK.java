package com.hzq.system.server.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author hua
 * @className com.hzq.system.server.domain.entity SysUserRoleId
 * @date 2024/12/5 16:33
 * @description sys_user_role 联合主键
 * 关于联合主键类，要遵守以下几点JPA规范：
 * 1. 必须提供一个public的无参数构造函数。
 * 2. 必须实现序列化接口。
 * 3. 必须重写hashCode()和equals()这两个方法。这两个方法应该采用复合主键的字段作为判断这个对象是否相等的。
 * 4. 联合主键类的类名结尾一般要加上PK两个字母代表一个主键类，不是要求而是一种命名风格。
 */
@Data
@EqualsAndHashCode
@Embeddable
public class SysUserRolePK implements Serializable {

    @Column(name = "user_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '用户ID'")
    private Long userId;

    @Column(name = "role_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '角色ID'")
    private Long roleId;

    public SysUserRolePK() {

    }

    public SysUserRolePK(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
