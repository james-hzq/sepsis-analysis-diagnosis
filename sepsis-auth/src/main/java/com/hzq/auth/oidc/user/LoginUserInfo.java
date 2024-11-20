package com.hzq.auth.oidc.user;

import lombok.*;

import java.util.Set;

/**
 * @author gc
 * @class com.hzq.auth.login.user LoginUserInfo
 * @date 2024/11/19 17:40
 * @description 登录用户信息
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserInfo {
    private String username;
    private Set<String> roles;
}
