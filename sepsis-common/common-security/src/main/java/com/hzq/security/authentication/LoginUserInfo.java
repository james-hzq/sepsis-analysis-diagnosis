package com.hzq.security.authentication;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.ImmutableSet;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author gc
 * @class com.hzq.auth.login.user LoginUserInfo
 * @date 2024/11/19 17:40
 * @description 登录用户信息
 */
@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class LoginUserInfo {
    // 用户登录类型（system, github, google, ...）
    private String loginType;
    // 用户名
    private String username;
    // 用户拥有角色
    private Set<String> roles;
    // 用户授权成功时间
    private Instant issuedAt;
    // 用户授权失效时间
    private Instant expiresAt;

    public LoginUserInfo setLoginType(String loginType) {
        this.loginType = loginType;
        return this;
    }

    public LoginUserInfo setUsername(String username) {
        this.username = username;
        return this;
    }

    public LoginUserInfo setRoles(Set<String> roles) {
        this.roles = CollectionUtils.isEmpty(roles)
                ? Collections.unmodifiableSet(new HashSet<>())
                : Collections.unmodifiableSet(roles);
        return this;
    }

    public LoginUserInfo setIssuedAt(Instant issuedAt) {
        this.issuedAt = issuedAt;
        return this;
    }

    public LoginUserInfo setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }
}
