package com.hzq.auth.login.user;

import com.hzq.system.dto.SysUserRoleDTO;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gc
 * @class com.hzq.auth.login.user SysUserDetail
 * @date 2024/11/28 17:46
 * @description 系统用户信息
 */
@Getter
public class SysUserDetail implements UserDetails {

    private final String loginType = "system";
    private String username;
    private String password;
    private String email;
    private String avatar;
    private Character status;
    private Set<String> roles;

    public SysUserDetail() {
    }

    public SysUserDetail(SysUserRoleDTO sysUserRoleDTO) {
        this.username = sysUserRoleDTO.getUsername();
        this.password = sysUserRoleDTO.getPassword();
        this.email = sysUserRoleDTO.getEmail();
        this.avatar = sysUserRoleDTO.getAvatar();
        this.status = sysUserRoleDTO.getStatus();
        this.roles = sysUserRoleDTO.getRoles() == null
                ? Collections.emptySet()
                : Collections.unmodifiableSet(sysUserRoleDTO.getRoles());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles == null
                ? Collections.emptySet()
                : roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
