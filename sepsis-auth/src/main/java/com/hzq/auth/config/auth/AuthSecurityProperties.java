package com.hzq.auth.config.auth;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author gc
 * @class com.hzq.auth.config AuthSecurityProperties
 * @date 2024/11/4 9:23
 * @description 授权服务安全基础配置
 */
@Data
@Configuration
public class AuthSecurityProperties {

    // 登录页面地址
    private String loginPageUri = "http://localhost:9050/login";

    // token签发地址：如果需要通过ip访问这里就是ip，如果是有域名映射就填域名，通过什么方式访问该服务这里就填什么
    private String issuerUrl = "http://127.0.0.1:9200";
    // 授权白名单配置，跳过认证
    private List<String> whiteUriList = List.of(
            "/login",
            "/favicon.ico",
            "/login/oauth2/code/**",
            "/error"
    );
}
