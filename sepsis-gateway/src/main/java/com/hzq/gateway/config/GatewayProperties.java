package com.hzq.gateway.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author hua
 * @className com.hzq.gateway.config GatewayProperties
 * @date 2024/11/2 10:23
 * @description 网关自定义配置属性
 */
@Data
@Configuration
public class GatewayProperties {

    // 网关白名单配置，无需进行 JWT 校验
    @Value("${hzq.gateway.white-uri-list}")
    private List<String> whiteUriList;
}
