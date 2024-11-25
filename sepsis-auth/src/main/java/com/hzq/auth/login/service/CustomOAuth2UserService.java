package com.hzq.auth.login.service;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author gc
 * @class com.hzq.auth.oidc.service CustomOAuth2UserService
 * @date 2024/11/25 9:30
 * @description TODO
 */
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final Map<String, OAuth2UserService<OAuth2UserRequest, OAuth2User>> oidcUserServiceMap = new HashMap<>();

    public CustomOAuth2UserService setOidcUserService(String registrationId, OAuth2UserService<OAuth2UserRequest, OAuth2User> oidcUserService) {
        this.oidcUserServiceMap.put(registrationId, oidcUserService);
        return this;
    }

    public CustomOAuth2UserService() {
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = Optional.ofNullable(oidcUserServiceMap.get(registrationId))
                .orElseThrow(() -> new OAuth2AuthenticationException("没有该 OAuth2UserService 实例"));

        return oAuth2UserService.loadUser(userRequest);
    }
}
