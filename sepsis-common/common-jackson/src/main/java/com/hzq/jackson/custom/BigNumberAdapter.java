package com.hzq.jackson.custom;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @author hua
 * @className com.huz.hzq.util.jackson BigNumberSerializer
 * @date 2024/8/11 21:32
 * @description 大数的序列化
 */
@JacksonStdImpl
public class BigNumberAdapter extends NumberSerializer implements TypeAdapter<Number> {
    // 根据 JS Number.MAX_SAFE_INTEGER 与 Number.MIN_SAFE_INTEGER 得来
    private static final long MAX_SAFE_INTEGER = 9007199254740991L;
    private static final long MIN_SAFE_INTEGER = -9007199254740991L;
    // 单例实例
    public static final BigNumberAdapter INSTANCE = new BigNumberAdapter(Number.class);

    private BigNumberAdapter(Class<? extends Number> rawType) {
        super(rawType);
    }

    /**
     * @param value 要序列化的数值对象
     * @param jsonGenerator JSON 生成器，用于输出 JSON 内容
     * @param provider 序列化提供者，用于获取序列化上下文
     * @author hzq
     * @date 2024/11/18 11:16
     * @apiNote 此方法被 Jackson 框架在对象序列化过程中自动调用，确保序列化后的结果在前端 JavaScript 环境中能够被正确处理。
     **/
    @Override
    public void serialize(Number value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        if (value.longValue() > MIN_SAFE_INTEGER && value.longValue() < MAX_SAFE_INTEGER) {
            super.serialize(value, jsonGenerator, provider);
        } else {
            jsonGenerator.writeString(value.toString());
        }
    }

    /**
     * @param value 要序列化的数值对象
     * @param jsonGenerator JSON 生成器，用于输出 JSON 内容
     * @author hzq
     * @date 2024/11/18 11:17
     * @apiNote 此方法在实现 `TypeAdapter` 接口时提供序列化逻辑，适用于需要在特定条件下对数值进行格式化处理以兼容 JavaScript 的场景。
     **/
    @Override
    public void serialize(Number value, JsonGenerator jsonGenerator) throws IOException {
        if (value.longValue() > MIN_SAFE_INTEGER && value.longValue() < MAX_SAFE_INTEGER) {
            jsonGenerator.writeNumber(value.longValue());
        } else {
            jsonGenerator.writeString(value.toString());
        }
    }

    @Override
    public Number deserialize(JsonParser parser) throws IOException {
        String text = parser.getText().trim();
        try {
            // 尝试将其解析为长整型
            return Long.parseLong(text);
        } catch (NumberFormatException e) {
            // 如果解析失败，返回大整数
            return new BigInteger(text);
        }
    }

    @Override
    public Class<Number> getType() {
        return Number.class;
    }
}
