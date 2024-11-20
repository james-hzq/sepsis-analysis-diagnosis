package com.hzq.auth.oidc.service;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author gc
 * @class com.hzq.auth.oidc.service CustomOidcUserService
 * @date 2024/11/20 14:18
 * @description 自定义OIDC用户信息加载服务
 */
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final Map<String, OAuth2UserService<OidcUserRequest, OidcUser>> oidcUserServiceMap = new HashMap<>();

    public CustomOidcUserService setOidcUserService(String registrationId, OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService) {
        this.oidcUserServiceMap.put(registrationId, oidcUserService);
        return this;
    }

    public CustomOidcUserService() {
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserService<OidcUserRequest, OidcUser> oAuth2UserService = Optional.ofNullable(oidcUserServiceMap.get(registrationId))
                .orElseThrow(() -> new OAuth2AuthenticationException("没有该 OAuth2UserService 实例"));

        return oAuth2UserService.loadUser(userRequest);
    }
}
