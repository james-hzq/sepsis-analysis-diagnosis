package com.hzq.system.server.dao;

import com.hzq.system.server.domain.entity.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author hua
 * @interfaceName com.hzq.system.server.dao SysUserRoleDao
 * @date 2024/10/1 16:01
 * @description TODO
 */
public interface SysUserRoleDao extends JpaRepository<SysUserRole, SysUserRole.SysUserRoleId> {
    @Query("select ur.id.roleId from SysUserRole ur where ur.id.userId = :userId")
    List<Long> findRoleIdsByUserId(@Param("userId") Long userId);
}
