package com.hzq.system.dao;

import com.hzq.api.entity.system.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author gc
 * @interface com.hzq.system.dao SysRoleDao
 * @date 2024/8/30 16:52
 * @description TODO
 */
public interface SysRoleDao extends JpaRepository<SysRole, Long>, JpaSpecificationExecutor<SysRole> {

}
