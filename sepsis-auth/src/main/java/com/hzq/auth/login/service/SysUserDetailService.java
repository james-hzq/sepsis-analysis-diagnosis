package com.hzq.auth.login.service;

import com.google.common.base.Strings;
import com.hzq.auth.login.user.SysUserDetail;
import com.hzq.system.api.SysUserFeignClient;
import com.hzq.system.dto.SysUserRoleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author hua
 * @className com.hzq.auth.login.service SysUserDetailService
 * @date 2024/11/10 19:38
 * @description 系统用户信息服务
 */
@Service
@RequiredArgsConstructor
public class SysUserDetailService implements UserDetailsService {

    private final SysUserFeignClient sysUserFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (Strings.isNullOrEmpty(username)) throw new UsernameNotFoundException("登录用户名不得为空");
        // 通过用户名查找用户(包含角色信息)
        SysUserRoleDTO sysUserRoleDTO = Optional.ofNullable(sysUserFeignClient.selectSysUserWithRolesByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        // 生成 UserDetails
        return new SysUserDetail(sysUserRoleDTO);
    }
}
