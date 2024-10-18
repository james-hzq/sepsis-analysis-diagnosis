package com.hzq.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author gc
 * @class com.hzq.auth.config PassWordConfig
 * @date 2024/10/18 15:31
 * @description TODO
 */
@Configuration
public class PasswordConfig {
    /**
     * @return org.springframework.security.crypto.password.PasswordEncoder
     * @author gc
     * @date 2024/10/14 14:52
     * @apiNote 密码加密器
     **/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
