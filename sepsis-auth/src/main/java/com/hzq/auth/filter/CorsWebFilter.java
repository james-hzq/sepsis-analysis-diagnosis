package com.hzq.auth.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author gc
 * @class com.hzq.auth.filter CorsWebFilter
 * @date 2024/10/21 9:42
 * @description 配 CORS 跨域
 */
@Component
public class CorsWebFilter {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 放行所有域名，生产环境请对此进行修改
        corsConfiguration.addAllowedOriginPattern("*");
        // 放行的请求头
        corsConfiguration.addAllowedHeader("*");
        // 放行的请求方式，主要有：GET, POST, PUT, DELETE, OPTIONS
        corsConfiguration.addAllowedMethod("*");
        // 暴露头部信息
        corsConfiguration.addExposedHeader("*");
        // 是否允许发送cookie
        corsConfiguration.setAllowCredentials(true);
        // 初始化cors配置源对象
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        // 给配置源对象设置过滤的参数
        // 参数一: 过滤的路径 == > 所有的路径都要求校验是否跨域
        // 参数二: 配置类
        configurationSource.registerCorsConfiguration("/**", corsConfiguration);
        // 返回配置好的过滤器
        return new CorsFilter(configurationSource);
    }

}
