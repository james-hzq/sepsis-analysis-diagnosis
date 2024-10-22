package com.hzq.auth.constant;

import lombok.Data;
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
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "hzq.security")
public class SecurityProperties {
    // 登录页面路径
    private String loginPageUrl;

    // 登录API路径
    private String loginApiUrl;

    // 授权确认页面
    private String consentPageUri;

    // 授权码验证页面
    private String deviceActivateUri;

    // 授权码验证成功后页面
    private String deviceActivatedUri;

    // 跳过认证的白名单路径
    private List<String> whiteUriList;

    // token签发地址：如果需要通过ip访问这里就是ip，如果是有域名映射就填域名，通过什么方式访问该服务这里就填什么
    private String issuerUrl;
}
