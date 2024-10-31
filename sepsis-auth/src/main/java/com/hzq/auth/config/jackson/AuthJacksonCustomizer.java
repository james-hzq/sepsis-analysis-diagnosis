package com.hzq.auth.config.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hzq.jackson.JacksonCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType;
import org.springframework.stereotype.Component;

/**
 * @author hua
 * @className com.hzq.auth.config.jackson AuthJacksonCustomizer
 * @date 2024/10/31 10:54
 * @description TODO
 */
@Component
public class AuthJacksonCustomizer implements JacksonCustomizer {
    @Override
    public void customize(Jackson2ObjectMapperBuilder builder) {
        SimpleModule module = new SimpleModule();

        // 序列化配置
        module.addSerializer(OAuth2AuthorizationResponseType.class, new ToStringSerializer());

        // 反序列化配置
        module.addDeserializer(OAuth2AuthorizationResponseType.class, new OAuth2AuthorizationResponseTypeDeserializer());

        builder.modules(module);
        builder.modules(module);
    }
}
