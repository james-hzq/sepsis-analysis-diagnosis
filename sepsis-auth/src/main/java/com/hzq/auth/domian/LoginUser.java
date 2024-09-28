package com.hzq.auth.domian;

import com.hzq.core.constant.UserConstants;
import com.hzq.system.dto.SysUserDTO;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hua
 * @className com.hzq.auth.domian LoginUser
 * @date 2024/9/28 10:10
 * @description 系统用户信息（包含用户名、密码和权限），用户名和密码用于认证，认证成功之后授予权限
 */
@Data
@ToString
public class LoginUser implements UserDetails {
    /** 用户ID */
    private Long userId;

    /** 用户名 */
    private String username;

    /** 用户密码 */
    private String password;

    /** 账户是否启用 */
    private Boolean enabled;

    /** 用户的权限集合，表示用户在系统中拥有的权限 */
    private Collection<GrantedAuthority> authorities;

    /** 用户的权限标识集合, 通常用于更细粒度的权限控制 */
    private Set<String> perms;

    public LoginUser(SysUserDTO sysUserDTO) {
        this.userId = sysUserDTO.getUserId();
        this.username = sysUserDTO.getUsername();
        this.password = sysUserDTO.getPassword();
        this.enabled = UserConstants.STATUS_OK.equals(sysUserDTO.getStatus());
        if (sysUserDTO.getRoles() != null && !sysUserDTO.getRoles().isEmpty()) {
            this.authorities = sysUserDTO.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }
        this.perms = sysUserDTO.getPerms();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
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
        return this.enabled;
    }
}
