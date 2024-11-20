package com.hzq.auth.oidc.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author hua
 * @className com.hzq.auth.login.service SysUserDetailService
 * @date 2024/11/10 19:38
 * @description TODO
 */
public class SysUserDetailService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
