package com.hzq.system.server.service;

import com.hzq.system.server.dao.SysRoleDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author hua
 * @className com.hzq.system.server.service SysRoleService
 * @date 2024/10/1 15:59
 * @description 系统角色业务处理类
 */
@RequiredArgsConstructor
@Service
public class SysRoleService extends SysBaseService {

    private final SysRoleDao sysRoleDao;

    public Set<String> selectRoleKeys(List<Long> roleIds) {
        return sysRoleDao.findRoleKeysByRoleIds(roleIds);
    }
}
