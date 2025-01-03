package com.hzq.auth.login.service;

import com.hzq.auth.login.user.GithubOAuth2User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author hua
 * @className com.hzq.auth.service GithubOAuth2UserService
 * @date 2024/11/4 21:50
 * @description Github 用户信息加载服务
 */
@Slf4j
public final class GithubOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final ParameterizedTypeReference<Map<String, Object>> PARAMETERIZED_RESPONSE_TYPE = new ParameterizedTypeReference<>() {
    };
    private final Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter = new OAuth2UserRequestEntityConverter();
    private final RestOperations restOperations;

    public GithubOAuth2UserService() {
        this.restOperations = new RestTemplate();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Assert.notNull(userRequest, "userRequest cannot be null");
        if (!StringUtils.hasText(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri())) {
            throw new OAuth2AuthenticationException("客户端注册的 UserInfoEndpoint 中缺少必需的 UserInfo Uri");
        }

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        if (!StringUtils.hasText(userNameAttributeName)) {
            throw new OAuth2AuthenticationException("客户端注册的 UserInfoEndpoint 中缺少必需的“用户名”属性名称");
        }
        // 请求转换
        RequestEntity<?> request = this.requestEntityConverter.convert(userRequest);
        // 发送用户信息请求，得到响应
        ResponseEntity<Map<String, Object>> response = getResponse(request);
        // 获取响应体
        Map<String, Object> userAttributes = response.getBody();
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        authorities.add(new OAuth2UserAuthority(userAttributes));
        OAuth2AccessToken token = userRequest.getAccessToken();
        for (String authority : token.getScopes()) {
            authorities.add(new SimpleGrantedAuthority(authority));
        }
        return new GithubOAuth2User(authorities, userAttributes, userNameAttributeName, token);
    }

    private ResponseEntity<Map<String, Object>> getResponse(RequestEntity<?> request) throws OAuth2AuthenticationException{
        return this.restOperations.exchange(request, PARAMETERIZED_RESPONSE_TYPE);
    }
}
