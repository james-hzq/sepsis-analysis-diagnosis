package com.hzq.system.server.dao;

import com.hzq.system.server.domain.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author hua
 * @interfaceName com.hzq.system.server.dao SysRoleDao
 * @date 2024/10/1 15:57
 * @description TODO
 */
public interface SysRoleDao extends JpaRepository<SysRole, Long>, JpaSpecificationExecutor<SysRole> {
}
