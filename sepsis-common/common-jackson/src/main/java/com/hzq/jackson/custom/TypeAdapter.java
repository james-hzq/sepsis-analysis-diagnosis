package com.hzq.jackson.custom;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;

/**
 * @author gc
 * @interface com.hzq.jackson.custom TypeAdapter
 * @date 2024/11/18 10:21
 * @description Jackson 通用扩展配置适配器接口
 */
public interface TypeAdapter<T> {

    /**
     * @param value 要序列化的对象
     * @param jsonGenerator JSON 生成器，用于构建 JSON 输出
     * @author hzq
     * @date 2024/11/18 10:22
     * @apiNote 序列化方法，将指定类型的对象序列化为 JSON 表示。
     **/
    void serialize(T value, JsonGenerator jsonGenerator) throws IOException;

    /**
     * @param jsonParser JSON 解析器，用于读取 JSON 输入
     * @return T
     * @author hzq
     * @date 2024/11/18 10:23
     * @apiNote 反序列化方法，将 JSON 表示解析为指定类型的对象。
     **/
    T deserialize(JsonParser jsonParser) throws IOException;

    /**
     * @return java.lang.Class<T>
     * @author hzq
     * @date 2024/11/18 10:23
     * @apiNote 获取当前适配器支持处理的类型
     **/
    Class<T> getType();
}
