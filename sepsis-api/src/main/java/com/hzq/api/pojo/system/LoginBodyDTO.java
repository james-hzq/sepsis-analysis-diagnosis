package com.hzq.api.pojo.system;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author hua
 * @className com.hzq.system.pojo.dto LoginBodyDTO
 * @date 2024/9/1 15:27
 * @description TODO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginBodyDTO {
    @NotBlank(message = "登录用户名称不得为空")
    private String username;

    @NotBlank(message = "登录用户密码不得为空")
    private String password;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
