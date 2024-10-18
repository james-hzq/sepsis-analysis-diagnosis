package com.hzq.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;

/**
 * @author gc
 * @class com.hzq.auth.config OAuth2AuthorizationServiceConfig
 * @date 2024/10/18 15:38
 * @description OAuth2 授权服务配置
 */
@Configuration
public class OAuth2AuthorizationServiceConfig {
    /**
     * @return org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
     * @author gc
     * @date 2024/10/18 15:53
     * @apiNote 创建基于内存的OAuth2授权服务
     **/
    @Bean
    public OAuth2AuthorizationService authorizationService() {
        return new InMemoryOAuth2AuthorizationService();
    }

    /**
     * @return org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService
     * @author gc
     * @date 2024/10/15 11:13
     * @apiNote 将授权同意信息存储在内存中
     **/
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService() {
        return new InMemoryOAuth2AuthorizationConsentService();
    }
}
