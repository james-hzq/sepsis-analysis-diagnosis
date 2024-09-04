package com.hzq.system.service;

import com.hzq.api.entity.system.SysUserRole;
import com.hzq.system.dao.SysUserRoleDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author gc
 * @class com.hzq.system.service SysUserRoleService
 * @date 2024/9/4 9:49
 * @description TODO
 */
@Service
@AllArgsConstructor
public class SysUserRoleService {
    private SysUserRoleDao sysUserRoleDao;

    public List<SysUserRole> findSysUserRoleByUserId(Long userId) {
        return sysUserRoleDao.findSysUserRoleByUserId(userId);
    }
}
