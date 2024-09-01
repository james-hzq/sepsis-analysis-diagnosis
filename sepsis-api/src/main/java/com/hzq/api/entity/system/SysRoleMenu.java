package com.hzq.api.entity.system;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

/**
 * @author hua
 * @className com.hzq.api.entity.system SysRoleMenu
 * @date 2024/8/31 9:47
 * @description 系统角色菜单映射实体类
 */
@Entity
@Table(name = "sys_role_menu")
public class SysRoleMenu {
    @Id
    private Long roleId;
    private String menuId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SysRoleMenu that)) return false;
        return Objects.equals(roleId, that.roleId) && Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, menuId);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
