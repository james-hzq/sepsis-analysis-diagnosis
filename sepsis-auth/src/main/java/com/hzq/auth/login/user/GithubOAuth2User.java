package com.hzq.auth.login.user;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

import java.util.Collection;
import java.util.Map;

/**
 * @author gc
 * @class com.hzq.auth.login.github GithubOAuth2User
 * @date 2024/11/4 17:34
 * @description Github OAuth2 用户类
 */
@Getter
public class GithubOAuth2User extends BaseOAuth2User {

    public GithubOAuth2User(Collection<GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, OAuth2AccessToken accessToken) {
        super(authorities, attributes, nameAttributeKey, accessToken);
    }
}
