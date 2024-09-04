package com.hzq.api.pojo.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
