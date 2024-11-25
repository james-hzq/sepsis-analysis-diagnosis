package com.hzq.gateway.strategy.converter;

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
 * @author gc
 * @class com.hzq.gateway.strategy.converter AccessTokenAuthentication
 * @date 2024/11/25 10:15
 * @description 用于表示 oauth2 access_token 网关统一认证的对象
 */
@Setter
@Getter
public class AccessTokenAuthentication implements Authentication {

    // token 值
    private String accessToken;
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

    public AccessTokenAuthentication() {
    }

    public AccessTokenAuthentication setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public AccessTokenAuthentication setIssuedAt(Instant issuedAt) {
        this.issuedAt = issuedAt;
        return this;
    }

    public AccessTokenAuthentication setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public AccessTokenAuthentication setRoles(Set<String> roles) {
        this.roles = ImmutableSet.copyOf(roles);
        return this;
    }

    public AccessTokenAuthentication setDetails(Map<String, Object> details) {
        this.details = ImmutableMap.copyOf(details);
        return this;
    }

    public AccessTokenAuthentication setPrincipal(Object principal) {
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
        return accessToken;
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
