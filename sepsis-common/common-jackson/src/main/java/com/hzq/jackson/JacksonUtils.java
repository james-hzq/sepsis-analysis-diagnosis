package com.hzq.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * @author hua
 * @className com.huz.hzq.util.jackson JacksonUtils
 * @date 2024/8/11 19:56
 * @description Jackson 工具类
 */
@Slf4j
@Component
public class JacksonUtils implements ApplicationContextAware {

    private static ObjectMapper objectMapper;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        if (objectMapper == null) {
            objectMapper = applicationContext.getBean(ObjectMapper.class);
            log.info("JacksonUtil 中的 objectMapper 加载了自定义的 ObjectMapper");
        }
    }

    /**
     * @author hua
     * @date 2024/11/2 10:35
     * @param object 待序列化对象
     * @return java.lang.String
     * @apiNote 将对象序列化为 Json 字符串
     **/
    public static String toJsonString(Object object) {
        if (Objects.isNull(object)) {
            return "";
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @author hua
     * @date 2024/11/2 10:36
     * @param text 待反序列化文本
     * @param clazz 待反序列化类型
     * @return T
     * @apiNote 将Json字符串反序列化为指定Java对象
     **/
    public static <T> T parseObject(String text, Class<T> clazz) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(text, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @author hua
     * @date 2024/11/2 10:37
     * @param bytes 待反序列化字节数组
     * @param clazz 待反序列化类型
     * @return T
     * @apiNote 将字节数组反序列化为指定Java对象
     **/
    public static <T> T parseObject(byte[] bytes, Class<T> clazz) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return objectMapper.readValue(bytes, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @author hua
     * @date 2024/11/2 10:38
     * @param text 待反序列化文本
     * @param typeReference 表示要反序列化的类型
     * @return T
     * @apiNote 将Json字符串反序列化为指定Java对象
     **/
    public static <T> T parseObject(String text, TypeReference<T> typeReference) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(text, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
