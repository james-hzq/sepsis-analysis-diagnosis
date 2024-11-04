package com.hzq.jackson;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author hua
 * @interfaceName com.hzq.jackson JacksonCustomizer
 * @date 2024/10/31 10:48
 * @description 自定义扩展 Jackson ObjectMapper 的接口
 */
public interface JacksonCustomizer {

    /**
     * @author hua
     * @date 2024/11/2 10:53
     * @param builder Jackson2ObjectMapperBuilder 提供了一个流畅的构建API，用于配置 ObjectMapper 的功能
     * @apiNote 用于自定义 Jackson 对象映射器配置，允许定制 Jackson ObjectMapper
     **/
    void customize(Jackson2ObjectMapperBuilder builder);
}
