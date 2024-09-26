package com.hzq.system.server.domain.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author hua
 * @className com.hzq.system.server.domain.entity SysRoleMenu
 * @date 2024/9/26 16:38
 * @description 系统角色菜单映射实体类
 */
@Data
@ToString
@Entity
@Table(name = "sys_role_menu")
public class SysRoleMenu implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private SysRoleMenu.SysRoleMenuId id;
    public SysRoleMenu() {}
    public SysRoleMenu(Long roleId, Long menuId) {
        this.id = new SysRoleMenu.SysRoleMenuId(roleId, menuId);
    }

    @Embeddable
    @Data
    public static class SysRoleMenuId implements Serializable {
        private Long roleId;
        private Long menuId;
        public SysRoleMenuId() {}
        public SysRoleMenuId(Long roleId, Long menuId) {
            this.roleId = roleId;
            this.menuId = menuId;
        }
    }
}
