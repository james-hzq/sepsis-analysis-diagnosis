package com.hzq.auth.domain.user;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author gc
 * @class com.hzq.auth.login.github GithubOAuth2User
 * @date 2024/11/4 17:34
 * @description Github OAuth2 用户类
 */
@Getter
public class GithubOAuth2User extends BaseOAuth2User {

    public GithubOAuth2User(Collection<GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, String accessToken) {
        super(authorities, attributes, nameAttributeKey, accessToken);
    }
}
