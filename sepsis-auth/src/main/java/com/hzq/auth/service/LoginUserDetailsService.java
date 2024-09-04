package com.hzq.auth.service;

import com.hzq.api.entity.system.SysUser;
import com.hzq.api.feign.system.SysRoleFeignClient;
import com.hzq.api.feign.system.SysUserFeignClient;
import com.hzq.api.feign.system.SysUserRoleFeignClient;
import com.hzq.auth.model.LoginUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

/**
 * @author gc
 * @class com.hzq.auth.service UserDetailsService
 * @date 2024/9/3 14:23
 * @description TODO
 */
@Service
@AllArgsConstructor
public class LoginUserDetailsService implements UserDetailsService {

    private final SysUserFeignClient sysUserFeignClient;
    private final SysUserRoleFeignClient sysUserRoleFeignClient;
    private final SysRoleFeignClient sysRoleFeignClient;
    /**
     * @param username 用户名
     * @return org.springframework.security.core.userdetails.UserDetails
     * @author gc
     * @date 2024/9/3 14:24
     * @apiNote 根据用户名获取用户信息(用户名、密码和角色权限)
     **/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 通过用户名查找用户表信息
        SysUser sysUser = sysUserFeignClient.selectByUsername(username).getData();
        // 通过用户ID查找角色信息
        List<Long> roleIds = sysUserRoleFeignClient.findRoleIdsByUserId(sysUser.getUserId()).getData();
        // 通过角色ID查找角色标识
        List<String> roleKeys = sysRoleFeignClient.findRoleKeyByRoleIds(roleIds).getData();

        sysUser.setRoles(new HashSet<>(roleKeys));
        return new LoginUser(sysUser);
    }
}
