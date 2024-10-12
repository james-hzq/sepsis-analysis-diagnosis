package com.hzq.auth.service;

import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author gc
 * @class com.hzq.auth.service GithubLoginService
 * @date 2024/10/12 14:03
 * @description TODO
 */
@AllArgsConstructor
public class GithubLoginService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2User(new HashSet<>(), new HashMap<>(), "");
        // 提取用户信息，进行认证或存储
        String username = oAuth2User.getAttribute("login");
        System.out.println(username);
        // 你可以在这里添加逻辑来处理用户的存储或认证
        return oAuth2User;
    }
}
