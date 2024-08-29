package com.hzq.api.entity.system;

import com.hzq.api.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

/**
 * @author hua
 * @className com.hzq.api.entity.system SysUserEntity
 * @date 2024/8/28 22:09
 * @description 系统用户实体类
 */
@Entity
@Table(name = "sys_user")
public class SysUserEntity extends BaseEntity {
    @Id
    private String userId;
    private String userName;
    private String password;
    private String email;
    private String avatar;
    private char status;
    private char delFlag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SysUserEntity that)) return false;
        if (!super.equals(o)) return false;
        return status == that.status &&
                delFlag == that.delFlag &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(password, that.password) &&
                Objects.equals(email, that.email) &&
                Objects.equals(avatar, that.avatar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, userName, password, email, avatar, status, delFlag);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userId", userId)
                .append("userName", userName)
                .append("password", password)
                .append("email", email)
                .append("avatar", avatar)
                .append("status", status)
                .append("delFlag", delFlag)
                .toString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public char getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(char delFlag) {
        this.delFlag = delFlag;
    }
}
