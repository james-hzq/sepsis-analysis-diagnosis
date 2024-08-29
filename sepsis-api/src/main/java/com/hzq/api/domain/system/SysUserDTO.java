package com.hzq.api.domain.system;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author gc
 * @class com.hzq.system.domain SysUserDTO
 * @date 2024/8/29 15:17
 * @description 用户表单传输对象
 */
public class SysUserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String userId;

    @NotNull(message = "用户昵称不得为空")
    @Length(min = 1, max = 20, message = "用户昵称长度必须在 1 ~ 20 个字符之间")
    private String username;

    @NotNull(message = "用户密码不得为空")
    @Length(min = 5, max = 16, message = "用户密码长度必须在 5 ~ 16 个字符之间")
    private String password;

    @NotNull(message = "邮箱不得为空")
    @Email(message = "邮箱格式无效")
    private String email;

    @NotNull(message = "用户状态不得为空")
    @Length(min = 1, max = 1, message = "用户状态必须是 1 个字符")
    private char status;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userId", userId)
                .append("username", username)
                .append("password", password)
                .append("email", email)
                .append("status", status)
                .toString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public @NotNull(message = "用户昵称不得为空") @Length(min = 1, max = 20, message = "用户昵称长度必须在 1 ~ 20 个字符之间") String getUsername() {
        return username;
    }

    public void setUsername(@NotNull(message = "用户昵称不得为空") @Length(min = 1, max = 20, message = "用户昵称长度必须在 1 ~ 20 个字符之间") String username) {
        this.username = username;
    }

    public @NotNull(message = "用户密码不得为空") @Length(min = 5, max = 16, message = "用户密码长度必须在 5 ~ 16 个字符之间") String getPassword() {
        return password;
    }

    public void setPassword(@NotNull(message = "用户密码不得为空") @Length(min = 5, max = 16, message = "用户密码长度必须在 5 ~ 16 个字符之间") String password) {
        this.password = password;
    }

    public @NotNull(message = "邮箱不得为空") @Email(message = "邮箱格式无效") String getEmail() {
        return email;
    }

    public void setEmail(@NotNull(message = "邮箱不得为空") @Email(message = "邮箱格式无效") String email) {
        this.email = email;
    }

    @NotNull(message = "用户状态不得为空")
    @Length(min = 1, max = 1, message = "用户状态必须是 1 个字符")
    public char getStatus() {
        return status;
    }

    public void setStatus(@NotNull(message = "用户状态不得为空") @Length(min = 1, max = 1, message = "用户状态必须是 1 个字符") char status) {
        this.status = status;
    }
}
