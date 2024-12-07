package com.hzq.system.server.service;

import com.google.common.base.Strings;
import com.hzq.core.result.ResultEnum;
import com.hzq.system.dto.SysUserRoleDTO;
import com.hzq.system.server.dao.SysRoleDao;
import com.hzq.system.server.dao.SysUserDao;
import com.hzq.system.server.dao.SysUserRoleDao;
import com.hzq.system.server.domain.dto.SysUserForm;
import com.hzq.system.server.domain.entity.SysUser;
import com.hzq.system.server.domain.entity.SysUserRole;
import com.hzq.system.server.domain.entity.SysUserRolePK;
import com.hzq.system.server.domain.vo.SysUserRoleVO;
import com.hzq.web.exception.SystemException;
import com.hzq.web.util.PageUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author hua
 * @className com.hzq.system.server.service SysUserService
 * @date 2024/9/26 14:43
 * @description 系统用户业务处理类
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysUserService {

    private final PasswordEncoder passwordEncoder;
    private final SysUserDao sysUserDao;
    private final SysRoleDao sysRoleDao;
    private final SysUserRoleDao sysUserRoleDao;

    /**
     * @param username 用户名
     * @return com.hzq.system.dto.SysUserRoleDTO
     * @author hua
     * @date 2024/12/5 16:17
     * @apiNote 根据用户名查询用户角色信息
     **/
    public SysUserRoleDTO selectSysUserWithRolesByUsername(String username) {
        return Optional.ofNullable(sysUserDao.findSysUserByUsername(username))
                .map(sysUser -> {
                    List<Long> roleIds = sysUserRoleDao.findRoleIdsRolesByUserId(sysUser.getUserId());
                    if (CollectionUtils.isEmpty(roleIds)) return null;

                    Set<String> roles = sysRoleDao.findRoleKeysByRoleIds(roleIds);
                    if (CollectionUtils.isEmpty(roles)) return null;

                    return new SysUserRoleDTO()
                            .setUserId(sysUser.getUserId())
                            .setUsername(sysUser.getUsername())
                            .setPassword(sysUser.getPassword())
                            .setEmail(sysUser.getEmail())
                            .setAvatar(sysUser.getAvatar())
                            .setStatus(sysUser.getStatus())
                            .setDelFlag(sysUser.getDelFlag())
                            .setRoles(roles);
                })
                .orElse(null);
    }

    /**
     * @param sysUserForm 用户表单
     * @return java.util.List<com.hzq.system.server.domain.vo.SysUserRoleVO>
     * @author hua
     * @date 2024/12/5 16:18
     * @apiNote 查询所有用户和其所属角色
     **/
    public Page<SysUserRoleVO> list(SysUserForm sysUserForm, Pageable pageable) {
        // 动态查询的条件
        Long userId = Strings.isNullOrEmpty(sysUserForm.getUserId()) ? null : Long.parseLong(sysUserForm.getUserId());
        String username = sysUserForm.getUsername();
        String email = sysUserForm.getEmail();
        Character status = sysUserForm.getStatus();
        LocalDateTime startTime = sysUserForm.getStartTime();
        LocalDateTime endTime = sysUserForm.getEndTime();
        // 执行动态查询
        Page<Tuple> resultPage = sysUserDao.findSysUsersBySysUserFrom(
                userId, username, email, status, startTime, endTime, pageable
        );
        List<SysUserRoleVO> list = resultPage.stream().map(SysUserRoleVO::new).toList();

        return new PageImpl<>(list, pageable, resultPage.getTotalElements());
    }

    /**
     * @param sysUserForm 用户表单
     * @author hua
     * @date 2024/12/5 16:21
     * @apiNote 创建用户, 默认是 user 角色
     **/
    @Transactional(rollbackFor = Exception.class)
    public void createSysUserBySysUserForm(SysUserForm sysUserForm) {
        // 检查用户是否已经存在
        if (sysUserDao.findSysUserByUsername(sysUserForm.getUsername()) != null) {
            throw new SystemException(ResultEnum.USERNAME_EXISTED);
        }
        // 查询默认角色
        Set<Long> roleIds = Optional.ofNullable(sysRoleDao.findRoleIdsByRoleKeys(sysUserForm.getRoles()))
                .orElseThrow(() -> new SystemException(ResultEnum.DEFAULT_ROLE_NOT_EXIST));
        // 插入用户实体对象到用户表
        SysUser sysUser = sysUserDao.save(createSysUser(sysUserForm));
        // 创建用户角色关联（默认是 user）
        sysUserRoleDao.saveAll(createSysUserRole(sysUser.getUserId(), roleIds));
    }

    private SysUser createSysUser(SysUserForm sysUserForm) {
        SysUser sysUser = new SysUser();
        sysUser.setUsername(sysUserForm.getUsername());
        sysUser.setPassword(passwordEncoder.encode(sysUserForm.getPassword()));
        sysUser.setEmail(sysUserForm.getEmail());
        sysUser.setStatus(sysUserForm.getStatus());
        sysUser.setDelFlag('0');
        sysUser.create(sysUserForm.getCurrUsername());
        return sysUser;
    }

    private List<SysUserRole> createSysUserRole(Long userId, Set<Long> roleIds) {
        return roleIds
                .stream()
                .map(roleId -> new SysUserRole(new SysUserRolePK(userId, roleId))).toList();
    }
}
