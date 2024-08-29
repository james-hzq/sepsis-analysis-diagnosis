package com.hzq.common.jackson;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author hua
 * @className com.huz.hzq.common.jackson JacksonConfig
 * @date 2024/8/11 21:35
 * @description Springboot jackson 自动配置
 */
@AutoConfiguration(before = JacksonAutoConfiguration.class)
public class JacksonConfig {

    /**
     * @author hua
     * @date 2024/8/11 21:36
     * @return org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
     * @apiNote springboot 在构建ObjectMapper时默认使用这个构建器
     **/
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return JacksonUtil.CUSTOMIZER;
    }
}
