package com.hzq.auth.domain.user;

import org.springframework.security.core.GrantedAuthority;

import java.util.Map;
import java.util.Set;

/**
 * @author gc
 * @class com.hzq.auth.login.github GithubOAuth2User
 * @date 2024/11/4 17:34
 * @description Github OAuth2 用户类
 */
public class GithubOAuth2User extends BaseOAuth2User {
    private String accessToken;

    protected GithubOAuth2User(Set<GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey) {
        super(authorities, attributes, nameAttributeKey);
    }
}
