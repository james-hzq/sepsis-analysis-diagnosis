package com.hzq.system.server.dao;

import com.hzq.system.server.domain.entity.SysUserRole;
import com.hzq.system.server.domain.entity.SysUserRolePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author hua
 * @interfaceName com.hzq.system.server.dao SysUserRoleDao
 * @date 2024/12/5 18:09
 * @description 系统用户角色 DAO 层
 */
public interface SysUserRoleDao extends JpaRepository<SysUserRole, SysUserRolePK> {

    @Query("select ur.pk.roleId from SysUserRole ur where ur.pk.userId = :userId")
    List<Long> findRoleIdsRolesByUserId(@Param("userId") Long userId);

    @Query("select ur from SysUserRole ur where ur.pk.userId in :userIds")
    List<SysUserRole> findSysUserRolesByUserIds(@Param("userIds") List<Long> userIds);
}
