package com.hzq.system.server.dao;

import com.hzq.system.server.domain.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * @author hua
 * @interfaceName com.hzq.system.server.dao SysRoleDao
 * @date 2024/10/1 15:57
 * @description 系统角色 DAO 层
 */
public interface SysRoleDao extends JpaRepository<SysRole, Long>, JpaSpecificationExecutor<SysRole> {

    @Query("select distinct r.roleKey from SysRole r where r.roleId in :roleIds")
    Set<String> findRoleKeysByRoleIds(@Param("roleIds") List<Long> roleIds);

    @Query("select r.roleId from SysRole r where r.roleKey in :roleKey")
    Set<Long> findRoleIdsByRoleKeys(@Param("roleKey") List<String> roleKey);
}
