package com.hzq.system.server.dao;

import com.hzq.system.server.domain.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author hua
 * @interfaceName com.hzq.system.server.dao SysUserDao
 * @date 2024/9/26 14:44
 * @description TODO
 */
public interface SysUserDao extends JpaRepository<SysUser, Long>, JpaSpecificationExecutor<SysUser> {
    @Query("select u from SysUser u where u.username = :username")
    SysUser findSysUserByUsername(@Param("username") String username);
}
