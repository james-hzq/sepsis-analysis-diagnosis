package com.hzq.system.server.dao;

import com.hzq.system.server.domain.entity.SysMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author hua
 * @interfaceName com.hzq.system.server.dao SysMenuDao
 * @date 2024/10/1 16:06
 * @description TODO
 */
public interface SysMenuDao extends JpaRepository<SysMenu, Long>, JpaSpecificationExecutor<SysMenu> {
}
