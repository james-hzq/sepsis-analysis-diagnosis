package com.hzq.system.server.dao;

import com.hzq.system.server.domain.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author hua
 * @interfaceName com.hzq.system.server.dao SysRoleDao
 * @date 2024/10/1 15:57
 * @description 角色DAO
 */
public interface SysRoleDao extends JpaRepository<SysRole, Long>, JpaSpecificationExecutor<SysRole> {

    @Query(value = "select r from SysRole r where r.roleId in :roleIds")
    List<SysRole> findSysRolesByRoleIds(@Param("roleIds") List<Long> roleIds);
}
