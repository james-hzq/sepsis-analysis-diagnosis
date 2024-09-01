package com.hzq.api.pojo.system;

import jakarta.validation.constraints.NotBlank;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author hua
 * @className com.hzq.system.pojo.dto LoginBodyDTO
 * @date 2024/9/1 15:27
 * @description TODO
 */
public class LoginBodyDTO {
    @NotBlank(message = "登录用户名称不得为空")
    private String username;

    @NotBlank(message = "登录用户密码不得为空")
    private String password;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
