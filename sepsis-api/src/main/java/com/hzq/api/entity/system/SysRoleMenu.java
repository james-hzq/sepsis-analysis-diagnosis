package com.hzq.api.entity.system;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hua
 * @className com.hzq.api.entity.system SysRoleMenu
 * @date 2024/8/31 9:47
 * @description 系统角色菜单映射实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_role_menu")
public class SysRoleMenu {
    @Id
    private Long roleId;
    private String menuId;
}
