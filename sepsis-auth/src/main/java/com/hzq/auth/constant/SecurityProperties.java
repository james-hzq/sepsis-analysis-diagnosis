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
@Configuration
public class SecurityProperties {
    // 登录页面路径
    private String loginPageUrl = "/login";

    // 登录API路径
    private String loginApiUrl = "/auth/system/login";

    // 授权确认页面
    private String consentPageUri = "/oauth2/consent";

    // 授权码验证页面
    private String deviceActivateUri = "/activate";

    // 授权码验证成功后页面
    private String deviceActivatedUri = "/activated";

    // 跳过认证的白名单路径
    private List<String> whiteUriList = List.of(
            "/auth/system/login", "/auth/github/login", "/auth/github/callback"
    );

    // token签发地址：如果需要通过ip访问这里就是ip，如果是有域名映射就填域名，通过什么方式访问该服务这里就填什么
    private String issuerUrl = "http://127.0.0.1:9200";
}
