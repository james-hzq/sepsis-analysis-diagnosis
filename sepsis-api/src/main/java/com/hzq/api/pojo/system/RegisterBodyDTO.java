package com.hzq.api.pojo.system;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author hua
 * @className com.hzq.system.pojo.dto RegisterBodyDTO
 * @date 2024/9/1 15:29
 * @description TODO
 */
public class RegisterBodyDTO {
    private String username;

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
