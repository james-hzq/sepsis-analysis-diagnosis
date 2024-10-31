package com.hzq.jackson;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author hua
 * @interfaceName com.hzq.jackson JacksonCustomizer
 * @date 2024/10/31 10:48
 * @description TODO
 */
public interface JacksonCustomizer {

    void customize(Jackson2ObjectMapperBuilder builder);
}
