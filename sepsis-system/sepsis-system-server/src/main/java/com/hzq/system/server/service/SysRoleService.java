package com.hzq.system.server.service;

import com.hzq.system.server.dao.SysRoleDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
}
