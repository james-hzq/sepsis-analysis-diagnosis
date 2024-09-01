package com.hzq.api.pojo.system;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * @author hua
 * @className com.hzq.api.domain LoginUser
 * @date 2024/8/31 10:33
 * @description 用户登录信息对象
 */
public class LoginUserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 用户所属角色
     */
    private String roleKey;

    /**
     * 权限列表
     */
    private Set<String> permissions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginUserDTO loginUserDTO)) return false;
        return Objects.equals(userId, loginUserDTO.userId) &&
                Objects.equals(username, loginUserDTO.username) &&
                Objects.equals(token, loginUserDTO.token) &&
                Objects.equals(avatar, loginUserDTO.avatar) &&
                Objects.equals(loginTime, loginUserDTO.loginTime) &&
                Objects.equals(expireTime, loginUserDTO.expireTime) &&
                Objects.equals(roleKey, loginUserDTO.roleKey) &&
                Objects.equals(permissions, loginUserDTO.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, token, avatar, loginTime, expireTime, roleKey, permissions);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }
}
