package com.hzq.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author gc
 * @class com.hzq.auth.config PasswordConfig
 * @date 2024/11/4 9:22
 * @description 密码增强配置
 */
@Configuration
public class PasswordConfig {

    /**
     * @return org.springframework.security.crypto.password.PasswordEncoder
     * @author gc
     * @date 2024/11/4 9:22
     * @apiNote 密码增强器
     **/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
