package com.hzq.system.server.service;

import com.hzq.system.server.dao.SysUserRoleDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hua
 * @className com.hzq.system.server.service SysUserRoleService
 * @date 2024/10/1 16:02
 * @description TODO
 */
@AllArgsConstructor
@Service
public class SysUserRoleService {
    private SysUserRoleDao sysUserRoleDao;

    public List<Long> selectRolesByUserId(Long userId) {
        return sysUserRoleDao.findRoleIdsByUserId(userId);
    }
}
