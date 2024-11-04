package com.hzq.auth.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author gc
 * @class com.hzq.auth.config SecurityProperties
 * @date 2024/10/22 14:23
 * @description 安全自定义配置
 */
@Data
@Configuration
public class SecurityProperties {
    // 登录页面路径
    @Value("${hzq.security.login-page-url}")
    private String loginPageUrl;

    // 登录API路径
    @Value("${hzq.security.login-api-url}")
    private String loginApiUrl;

    // 授权确认页面
    @Value("${hzq.security.consent-page-uri}")
    private String consentPageUri;

    // 授权码验证页面
    @Value("${hzq.security.device-activate-uri}")
    private String deviceActivateUri;

    // 授权码验证成功后页面
    @Value("${hzq.security.device-activated-uri}")
    private String deviceActivatedUri;

    // token签发地址：如果需要通过ip访问这里就是ip，如果是有域名映射就填域名，通过什么方式访问该服务这里就填什么
    @Value("${hzq.security.issuer-url}")
    private String issuerUrl;

    // 跳过认证的白名单路径
    @Value("${hzq.security.white-uri-list}")
    private List<String> whiteUriList;
}
