package com.hzq.system.server.dao;

import com.hzq.system.server.domain.entity.SysRoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author hua
 * @interfaceName com.hzq.system.server.dao SysRoleMenuDao
 * @date 2024/10/1 16:04
 * @description TODO
 */
public interface SysRoleMenuDao extends JpaRepository<SysRoleMenu, SysRoleMenu.SysRoleMenuId> {
}
