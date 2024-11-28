package com.hzq.auth.login.service;

import com.google.common.base.Strings;
import com.hzq.system.api.SysRoleFeignClient;
import com.hzq.system.api.SysUserFeignClient;
import com.hzq.system.api.SysUserRoleFeignClient;
import com.hzq.system.dto.SysUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
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
    private final SysRoleFeignClient sysRoleFeignClient;
    private final SysUserRoleFeignClient sysUserRoleFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (Strings.isNullOrEmpty(username)) throw new UsernameNotFoundException("登录用户名不得为空");
        SysUserDTO sysUserDTO = Optional.ofNullable(sysUserFeignClient.selectByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("用户名不存在"));
        List<Long> roleIds = Optional.ofNullable(sysUserRoleFeignClient.selectRolesByUserId(sysUserDTO.getUserId()))
                .orElseThrow(() -> new BadCredentialsException("用户无角色"));

        return null;
    }
}
