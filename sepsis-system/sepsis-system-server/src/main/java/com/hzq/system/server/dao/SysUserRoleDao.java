package com.hzq.system.server.dao;

import com.hzq.system.server.domain.entity.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author gc
 * @interface com.hzq.system.dao SysUserRoleDao
 * @date 2024/9/4 9:50
 * @description TODO
 */
public interface SysUserRoleDao extends JpaRepository<SysUserRole, SysUserRole.SysUserRoleId> {
    @Query(value = "select ur from SysUserRole ur where ur.id.userId = :userId")
    List<SysUserRole> findSysUserRoleByUserId(@Param("userId") Long userId);
}
