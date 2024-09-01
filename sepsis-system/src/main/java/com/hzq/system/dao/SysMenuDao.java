package com.hzq.system.dao;

import com.hzq.api.entity.system.SysMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author hua
 * @interfaceName com.hzq.system.dao SysMenuDao
 * @date 2024/8/31 10:07
 * @description TODO
 */
public interface SysMenuDao extends JpaRepository<SysMenu, Long>, JpaSpecificationExecutor<SysMenu> {
}
