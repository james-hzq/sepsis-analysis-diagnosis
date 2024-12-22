package com.hzq.diagnosis.server.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author hua
 * @className com.hzq.diagnosis.server.config LangChain4jProperties
 * @date 2024/12/21 11:29
 * @description LangChain4j 配置类
 */
@Data
@Configuration
public class LangChain4jProperties {

    @Value(value = "${lang-chain4j.open-ai.chat-model.api-key}")
    private String apiKey;

    @Value(value = "${lang-chain4j.open-ai.chat-model.model-name}")
    private String modelName;

    @Value(value = "${lang-chain4j.open-ai.chat-model.api-base-url}")
    private String apiBaseUrl;

    @Value(value = "${lang-chain4j.open-ai.chat-model.proxy-http-host}")
    private String proxyHttpHost;

    @Value(value = "${lang-chain4j.open-ai.chat-model.proxy-http-port}")
    private Integer proxyHttpPort;

    @Value(value = "${lang-chain4j.open-ai.chat-model.log-requests}")
    private Boolean logRequests;

    @Value(value = "${lang-chain4j.open-ai.chat-model.log-responses}")
    private Boolean logResponses;
}
