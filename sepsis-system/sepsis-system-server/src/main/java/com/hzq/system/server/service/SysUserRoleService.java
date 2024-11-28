package com.hzq.system.server.service;

import com.hzq.system.server.dao.SysUserRoleDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hua
 * @className com.hzq.system.server.service SysUserRoleService
 * @date 2024/10/1 16:02
 * @description 用户角色关联业务类
 */
@RequiredArgsConstructor
@Service
public class SysUserRoleService {

    private final SysUserRoleDao sysUserRoleDao;

    public List<Long> selectRolesByUserId(Long userId) {
        return sysUserRoleDao.findRoleIdsByUserId(userId);
    }
}
