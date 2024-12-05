package com.hzq.system.server.dao;

import com.hzq.system.server.domain.entity.SysUser;
import com.hzq.system.server.domain.vo.SysUserRoleVO;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * @author hua
 * @interfaceName com.hzq.system.server.dao SysUserDao
 * @date 2024/9/26 14:44
 * @description 系统用户 DAO 层
 */
public interface SysUserDao extends JpaRepository<SysUser, Long>, JpaSpecificationExecutor<SysUser> {

    @Query("select u from SysUser u where u.username = :username")
    SysUser findSysUserByUsername(@Param("username") String username);

    @Query(
            nativeQuery = true,
            value = "select t1.`user_id`, t1.`username`, t1.`email`, t2.`roleKey`, t1.`status`, t1.`create_time`, t1.`create_by`, t1.`update_time`, t1.`update_by` " +
                    "from sys_user as t1 " +
                    "inner join " +
                        "(select u.user_id, group_concat(r.role_key) as roleKey " +
                        "from sys_user as u " +
                        "inner join sys_user_role as ur on u.user_id = ur.user_id " +
                        "inner join sys_role as r on ur.role_id = r.role_id " +
                        "where u.del_flag = '0' " +
                            "and (:userId is null or u.user_id = :userId) " +
                            "and (:username is null or :username = '' or u.username like concat(:username, '%')) " +
                            "and (:email is null or :email = '' or u.email like concat(:email, '%')) " +
                            "and (:status is null or :status = '' or u.status = :status) " +
                            "and (:startTime is null or u.create_time >= :startTime) " +
                            "and (:endTime is null or u.create_time <= :endTime) " +
                        "group by u.user_id) as t2 " +
                    "on t1.user_id = t2.user_id " +
                    "order by t1.user_id"
    )
    List<Tuple> findSysUsersBySysUserFrom(
            @Param("userId") Long userId,
            @Param("username") String username,
            @Param("email") String email,
            @Param("status") Character status,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime
    );
}
