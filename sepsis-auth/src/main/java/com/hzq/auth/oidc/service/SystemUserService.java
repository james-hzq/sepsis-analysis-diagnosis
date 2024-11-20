package com.hzq.auth.oidc.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author gc
 * @class com.hzq.auth.login.service SystemUserService
 * @date 2024/11/11 13:59
 * @description TODO
 */
@Service
public class SystemUserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
