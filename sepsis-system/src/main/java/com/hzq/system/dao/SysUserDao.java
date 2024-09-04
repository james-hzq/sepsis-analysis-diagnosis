package com.hzq.system.dao;

import com.hzq.api.entity.system.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @author hua
 * @interfaceName com.hzq.system.dao SysUserDao
 * @date 2024/8/29 23:14
 * @description TODO
 */
public interface SysUserDao extends JpaRepository<SysUser, Long>, JpaSpecificationExecutor<SysUser> {

    @Query(value = "select u from SysUser u where u.username =?1")
    SysUser findByUsername(String username);
}
