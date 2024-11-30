package com.hzq.gateway.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author gc
 * @class com.hzq.gateway.config GatewaySecurityProperties
 * @date 2024/11/15 15:28
 * @description 网关安全服务配置
 */
@Data
@Configuration
public class GatewaySecurityProperties {

    // 网关白名单配置，跳过 认证 和 鉴权
    private List<String> whiteUriList = List.of(
            "/oauth2/**",
            "/auth/system/login",
            "/auth/user/info",
            "/favicon.ico"
    );
}
