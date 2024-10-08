package com.hzq.system.server.service;

import com.hzq.system.server.dao.SysRoleDao;
import com.hzq.system.server.domain.entity.SysRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author hua
 * @className com.hzq.system.server.service SysRoleService
 * @date 2024/10/1 15:59
 * @description TODO
 */
@AllArgsConstructor
@Service
public class SysRoleService extends SysBaseService {
    private SysRoleDao sysRoleDao;

    public List<String> selectRoleKeys(List<Long> roleIds) {
        List<SysRole> sysRoleList = sysRoleDao.findSysRolesByRoleIds(roleIds);
        return sysRoleList.stream().map(SysRole::getRoleKey).toList();
    }
}
