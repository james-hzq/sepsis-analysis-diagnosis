package com.hzq.system.server.service;

import com.hzq.core.result.ResultEnum;
import com.hzq.system.dto.SysUserDTO;
import com.hzq.system.server.dao.SysUserDao;
import com.hzq.system.server.domain.entity.SysUser;
import com.hzq.web.exception.SystemException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author hua
 * @className com.hzq.system.server.service SysUserService
 * @date 2024/9/26 14:43
 * @description TODO
 */
@AllArgsConstructor
@Service
public class SysUserService extends SystemBaseService {
    private final SysUserDao sysUserDao;

    public SysUserDTO selectSysUserByUsername(String username) {
        // 从数据库根据用户名查询出 SysUser 实体对象
        SysUser sysUser = Optional.ofNullable(sysUserDao.findSysUserByUsername(username))
                .orElseThrow(() -> new SystemException(ResultEnum.USERNAME_OR_PASSWORD_ERROR));
        // 将实体对象转换为系统用户传输对象
        return new SysUserDTO()
                .setUserId(sysUser.getUserId())
                .setUsername(sysUser.getUsername())
                .setPassword(sysUser.getPassword())
                .setEmail(sysUser.getEmail())
                .setAvatar(sysUser.getAvatar())
                .setStatus(sysUser.getStatus())
                .setDelFlag(sysUser.getDelFlag());
    }
}
