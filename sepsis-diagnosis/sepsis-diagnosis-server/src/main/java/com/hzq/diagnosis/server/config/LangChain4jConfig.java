package com.hzq.diagnosis.server.config;

import com.hzq.diagnosis.server.service.AssistantService;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.*;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

/**
 * @author hua
 * @className com.hzq.diagnosis.server.config LangChain4jConfig
 * @date 2024/12/21 11:47
 * @description LangChain4j 配置类
 */
@Configuration
public class LangChain4jConfig {

    private static final String KNOWLEDGE_PATH = "F:\\DevPro\\sepsis\\sepsis-analysis-diagnosis-dev-home\\knowledge";

    private final LangChain4jProperties properties;

    public LangChain4jConfig(LangChain4jProperties langChain4jProperties) {
        this.properties = langChain4jProperties;
    }

    @Bean
    @Primary
    public Proxy httpProxy() {
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
                properties.getProxyHttpHost(), properties.getProxyHttpPort()
        ));
    }

    @Bean
    @Primary
    public ChatLanguageModel chatLanguageModel() {
        return OpenAiChatModel.builder()
                .apiKey(properties.getApiKey())
                .modelName(properties.getChatModelName())
                .baseUrl(properties.getApiBaseUrl())
                .logRequests(properties.getLogRequests())
                .logResponses(properties.getLogResponses())
                .build();
    }

    @Bean
    @Primary
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.withMaxMessages(10);
    }

    @Bean
    @Primary
    public EmbeddingModel embeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .apiKey(properties.getApiKey())
                .modelName(properties.getEmbeddingModelName())
                .baseUrl(properties.getApiBaseUrl())
                .logRequests(properties.getLogRequests())
                .logResponses(properties.getLogResponses())
                .build();
    }

    @Bean
    @Primary
    public EmbeddingStore<TextSegment> embeddingStore(EmbeddingModel model) {
        EmbeddingStore<TextSegment> store = new InMemoryEmbeddingStore<>();
        List<Document> documents = FileSystemDocumentLoader.loadDocuments(KNOWLEDGE_PATH);

        DocumentSplitter splitter = DocumentSplitters.recursive(1000, 200);

        for (Document doc : documents) {
            List<TextSegment> splitDocuments = splitter.split(doc);
            for (TextSegment textSegment : splitDocuments) {
                Embedding embedding = model.embed(textSegment).content();
                store.add(embedding, textSegment);
            }
        }
        return store;
    }

    @Bean
    @Primary
    public EmbeddingStoreContentRetriever embeddingStoreContentRetriever(EmbeddingModel model) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore(model))
                .embeddingModel(model)
                .maxResults(3)
                .minScore(0.7)
                .build();
    }

    @Bean
    public AssistantService assistantService(EmbeddingModel model) {
        return AiServices.builder(AssistantService.class)
                .chatLanguageModel(chatLanguageModel())
                .chatMemory(chatMemory())
//                .contentRetriever(embeddingStoreContentRetriever(model))
                .build();
    }
}
