package com.hzq.gateway.config;

import com.hzq.core.util.RSAUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import java.security.interfaces.RSAPublicKey;

/**
 * @author gc
 * @class com.hzq.gateway.config JWTConfig
 * @date 2024/11/14 15:20
 * @description 网关统一认证的配置
 */
@Configuration
public class AuthenticationConfig {

    /**
     * @return ReactiveJwtDecoder
     * @author hua
     * @date 2024/10/04
     * @apiNote 配置 Reactive JWT 解码器
     **/
    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() throws Exception {
        return NimbusReactiveJwtDecoder
                .withPublicKey((RSAPublicKey) RSAUtils.getPublicKey(RSAUtils.PUBLIC_KEY_CONTEXT))
                .build();
    }

    @Bean
    public ReactiveOAuth2TokenIntrospector oAuth2TokenIntrospector() {
        return new NimbusReactiveOAuth2TokenIntrospector(
                oauth2Properties.getIntrospectionUri(),
                oauth2Properties.getClientId(),
                oauth2Properties.getClientSecret()
        );
    }
}
