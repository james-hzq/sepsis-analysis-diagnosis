package com.hzq.web.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // 添加安全配置规则，防止 feign 间进行远程服务调用受到报错 401 未认证
        httpSecurity.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(WebSecurityProperties.internalPath.toArray(String[]::new)).permitAll()
                .anyRequest().authenticated()
        );
        return httpSecurity.build();
    }
}
