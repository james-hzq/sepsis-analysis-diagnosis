package com.hzq.security.authentication;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hua
 * @className com.hzq.gateway.strategy.token AccessTokenAuthentication
 * @date 2024/11/17 18:46
 * @description 用于 AccessToken 的身份认证和鉴权
 */
@Setter
@Getter
@ToString
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
    private Set<String> authorities;
    // 存储有关身份验证请求的其他详细信息，这里
    private Map<String, Object> details;
    // 身份验证的主体的身份，即用户名
    private Object principal;

    public AccessTokenAuthentication() {
    }

    private AccessTokenAuthentication(Builder builder) {
        this.accessToken = builder.accessToken;
        this.issuedAt = builder.issuedAt;
        this.expiresAt = builder.expiresAt;
        this.isAuthenticated = builder.isAuthenticated;
        this.authorities = builder.authorities;
        this.details = builder.details;
        this.principal = builder.principal;
    }

    public static class Builder {
        private String accessToken;
        private Instant issuedAt;
        private Instant expiresAt;
        private boolean isAuthenticated;
        private Set<String> authorities;
        private Map<String, Object> details;
        private Object principal;

        public Builder setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder setIssuedAt(Instant issuedAt) {
            this.issuedAt = issuedAt;
            return this;
        }

        public Builder setExpiresAt(Instant expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public Builder setIsAuthenticated(boolean isAuthenticated) {
            this.isAuthenticated = isAuthenticated;
            return this;
        }

        public Builder setAuthorities(Set<String> authorities) {
            this.authorities = new HashSet<>(authorities);
            return this;
        }

        public Builder setDetails(Map<String, Object> details) {
            this.details = new HashMap<>(details);
            return this;
        }

        public Builder setPrincipal(Object principal) {
            this.principal = principal;
            return this;
        }

        public AccessTokenAuthentication build() {
            return new AccessTokenAuthentication(this);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 在需要时将String转换为SimpleGrantedAuthority
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.collectingAndThen(
                        Collectors.toSet(),
                        ImmutableSet::copyOf
                ));
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
