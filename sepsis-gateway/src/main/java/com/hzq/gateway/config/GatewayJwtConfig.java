package com.hzq.gateway.config;

import com.hzq.core.util.RSAUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import java.security.interfaces.RSAPublicKey;

/**
 * @author hua
 * @className com.hzq.gateway.config GatewayJwtConfig
 * @date 2024/11/30 17:39
 * @description 网关JWT配置
 */
@Configuration
public class GatewayJwtConfig {

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
}
