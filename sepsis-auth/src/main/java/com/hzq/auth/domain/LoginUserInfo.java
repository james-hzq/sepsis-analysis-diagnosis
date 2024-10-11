package com.hzq.auth.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

/**
 * @author gc
 * @class com.hzq.web.base LoginUserInfo
 * @date 2024/10/11 14:35
 * @description 登录用户信息
 */
@Data
@ToString
@EqualsAndHashCode
public class LoginUserInfo {
    private String sub;
    private String username;
    private Set<String> roles;
}
