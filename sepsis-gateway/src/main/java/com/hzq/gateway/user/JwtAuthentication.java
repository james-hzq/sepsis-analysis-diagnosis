package com.hzq.gateway.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hua
 * @className com.hzq.gateway.user JwtAuthentication
 * @date 2024/11/30 17:18
 * @description 用于表示 jwt 网关统一认证的对象
 */
@Setter
@Getter
public class JwtAuthentication implements Authentication {

    // token 值
    private String jwt;
    // 颁发时间
    private Instant issuedAt;
    // 过期时间
    private Instant expiresAt;
    // 是否认证成功
    private boolean isAuthenticated;
    // 权限信息
    private Set<String> roles;
    // 存储有关身份验证请求的其他详细信息
    private Map<String, Object> details;
    // 身份验证的主体的身份，即用户名
    private Object principal;

    public JwtAuthentication() {
    }

    public JwtAuthentication setJwt(String jwt) {
        this.jwt = jwt;
        return this;
    }

    public JwtAuthentication setIssuedAt(Instant issuedAt) {
        this.issuedAt = issuedAt;
        return this;
    }

    public JwtAuthentication setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public JwtAuthentication setRoles(Set<String> roles) {
        this.roles = ImmutableSet.copyOf(roles);
        return this;
    }

    public JwtAuthentication setDetails(Map<String, Object> details) {
        this.details = ImmutableMap.copyOf(details);
        return this;
    }

    public JwtAuthentication setPrincipal(Object principal) {
        this.principal = principal;
        return this;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 在需要时将String转换为SimpleGrantedAuthority
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.toSet(),
                                ImmutableSet::copyOf
                        )
                );
    }

    @Override
    public Object getCredentials() {
        return jwt;
    }

    @Override
    public Object getDetails() {
        return details;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        if (this.principal != null) {
            return this.principal.toString();
        }
        return null;
    }
}
