package com.hzq.auth.config.oauth2;

import com.hzq.auth.constant.SecurityConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author gc
 * @class com.hzq.auth.config.oauth2 CustomAuthorizationRequestResolver
 * @date 2024/10/22 15:19
 * @description TODO
 */
@Slf4j
@Component
public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {
    private final DefaultOAuth2AuthorizationRequestResolver defaultOAuth2AuthorizationRequestResolver;

    public CustomAuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        // DI通过构造器自动注入clientRegistrationRepository，实例化DefaultOAuth2AuthorizationRequestResolver处理
        this.defaultOAuth2AuthorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository,
                OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI
        );
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        return this.defaultOAuth2AuthorizationRequestResolver.resolve(request);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        log.info("进入");
        OAuth2AuthorizationRequest authorizationRequest = this.defaultOAuth2AuthorizationRequestResolver.resolve(request, clientRegistrationId);
        return customizeAuthorizationRequest(authorizationRequest);
    }

    private OAuth2AuthorizationRequest customizeAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest) {
        log.info("进入 CustomAuthorizationRequestResolver ");
        if (authorizationRequest == null) {
            return null;
        }

        // 从请求中获取 additionalParameters
        Map<String, Object> additionalParameters = authorizationRequest.getAdditionalParameters();
        // 从 additionalParameters 获取 registration_id
        String clientRegistrationId = (String) additionalParameters.get("registrationId");

        // 针对 GitHub 自定义参数
        if (SecurityConstants.THIRD_LOGIN_GITHUB.equals(clientRegistrationId)) {
            additionalParameters.put("prompt", "consent");
        }

        // 针对 Google 自定义参数
        if (SecurityConstants.THIRD_LOGIN_GOOGLE.equals(clientRegistrationId)) {
            additionalParameters.put("prompt", "select_account");
        }

        return OAuth2AuthorizationRequest.from(authorizationRequest)
                .additionalParameters(additionalParameters)
                .build();
    }
}
