package com.hzq.diagnosis.server.security;

import com.hzq.web.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author gc
 * @class com.hzq.web.config WebSecurityConfig
 * @date 2024/11/29 15:44
 * @description 微服务安全配置
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain serverSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // 配置默认安全策略
        SecurityUtils.applyDefaultSecurity(httpSecurity);

        // 添加安全配置规则，防止 feign 间进行远程服务调用受到报错 401 未认证
        httpSecurity.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(SecurityUtils.getWhiteUrlPath("/diagnosis/**")).permitAll()
                .anyRequest().authenticated()
        );
        return httpSecurity.build();
    }
}
