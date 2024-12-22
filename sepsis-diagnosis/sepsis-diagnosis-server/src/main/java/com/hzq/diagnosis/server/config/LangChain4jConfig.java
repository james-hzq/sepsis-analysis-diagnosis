package com.hzq.diagnosis.server.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * @author hua
 * @className com.hzq.diagnosis.server.config LangChain4jConfig
 * @date 2024/12/21 11:47
 * @description LangChain4j 配置类
 */
@Configuration
public class LangChain4jConfig {

    private final LangChain4jProperties properties;

    public LangChain4jConfig(LangChain4jProperties langChain4jProperties) {
        this.properties = langChain4jProperties;
    }

    @Bean
    @Primary
    public ChatLanguageModel chatLanguageModel() {
        Proxy httpProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(properties.getProxyHttpHost(), properties.getProxyHttpPort()));
        return OpenAiChatModel.builder()
                .apiKey(properties.getApiKey())
                .modelName(properties.getModelName())
                .baseUrl(properties.getApiBaseUrl())
                .logRequests(properties.getLogRequests())
                .logResponses(properties.getLogResponses())
                .build();
    }
}
