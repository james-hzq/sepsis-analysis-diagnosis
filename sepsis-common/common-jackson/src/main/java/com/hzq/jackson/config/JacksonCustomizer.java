package com.hzq.jackson.config;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author hua
 * @interfaceName com.hzq.jackson JacksonCustomizer
 * @date 2024/10/31 10:48
 * @description 自定义 Jackson ObjectMapper 的配置接口
 */
public interface JacksonCustomizer {

    /**
     * @param builder Jackson2ObjectMapperBuilder 是 Spring Framework 提供的一个构建器类，用于配置和构建 Jackson ObjectMapper 实例
     * @author gc
     * @date 2024/11/4 9:13
     * @apiNote 定制 Jackson2ObjectMapperBuilder 对象
     **/
    void customize(Jackson2ObjectMapperBuilder builder);
}
