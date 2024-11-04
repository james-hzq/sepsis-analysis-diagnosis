package com.hzq.auth.config.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType;

import java.io.IOException;

/**
 * @author hua
 * @className com.hzq.auth.config.jackson OAuth2AuthorizationResponseTypeDeserializer
 * @date 2024/10/31 10:58
 * @description TODO
 */
public class OAuth2AuthorizationResponseTypeDeserializer extends StdDeserializer<OAuth2AuthorizationResponseType> {
    public OAuth2AuthorizationResponseTypeDeserializer() {
        super(OAuth2AuthorizationResponseType.class);
    }

    @Override
    public OAuth2AuthorizationResponseType deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException {
        String value = p.getText();
        return new OAuth2AuthorizationResponseType(value);
    }
}
