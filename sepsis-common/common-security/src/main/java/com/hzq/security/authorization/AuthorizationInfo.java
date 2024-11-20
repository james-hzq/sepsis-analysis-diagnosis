package com.hzq.security.authorization;

import lombok.*;

import java.util.Set;

/**
 * @author gc
 * @class com.hzq.security.authorization AuthorizationInfo
 * @date 2024/11/20 10:19
 * @description 用户鉴权的类
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationInfo {
    private String username;
    private Set<String> roles;

    public static Set<String> getRoles(String roles) {
        return Set.of(roles.split(","));
    }
}
