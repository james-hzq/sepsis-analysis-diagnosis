package com.hzq.auth.service;

import com.hzq.auth.domain.LoginUser;
import com.hzq.system.api.SysUserFeignClient;
import com.hzq.system.dto.SysUserDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author hua
 * @className com.hzq.auth.service LoginUserDetailService
 * @date 2024/9/28 11:04
 * @description TODO
 */
@Service
@AllArgsConstructor
public class LoginUserService implements UserDetailsService {
    private final SysUserFeignClient sysUserFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 通过用户名查找用户信息
        SysUserDTO sysUserDTO = Optional.ofNullable(sysUserFeignClient.selectByUsername(username).getData())
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        // 通过用户ID查找用户所属角色集合
        List<Long> roleIds = Optional.ofNullable(sysUserFeignClient.selectRolesByUserId(sysUserDTO.getUserId()).getData())
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        // 通过用户ID查找角色信息
        return new LoginUser(sysUserDTO);
    }
}
