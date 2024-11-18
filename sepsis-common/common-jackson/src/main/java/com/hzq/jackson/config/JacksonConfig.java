package com.hzq.jackson.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.hzq.jackson.custom.BigNumberAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;

/**
 * @author hua
 * @className com.huz.hzq.util.jackson JacksonConfig
 * @date 2024/8/11 21:35
 * @description Spring MVC 请求和响应的序列化配置
 */
@Slf4j
@AutoConfiguration(before = JacksonAutoConfiguration.class)
public class JacksonConfig {

    private static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder, Jackson2ObjectMapperBuilderCustomizer customizer) {
        ObjectMapper objectMapper = builder.build();
        log.info("jackson objectMapper config init successfully");
        return objectMapper;
    }

    /**
     * @return org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
     * @author hua
     * @date 2024/8/11 21:36
     * @apiNote springboot 在构建 ObjectMapper 时默认使用这个构建器
     **/
    @Bean
    @Primary
    public Jackson2ObjectMapperBuilderCustomizer customizer(List<JacksonCustomizer> customizers) {
        return builder -> {
            // 加载默认 Jackson 配置
            applyDefaultConfigurations(builder);
            // 应用扩展配置
            customizers.forEach(customizer -> {
                customizer.customize(builder);
                log.info("jackson customer config init successfully : {}", customizer.getClass().getName());
            });
        };
    }

    /**
     * @author hua
     * @date 2024/8/11 19:57
     * @apiNote
     * 1. 在Spring Boot应用中手动定义并注入了一个ObjectMapper Bean，这个自定义的ObjectMapper实例会替换掉Spring Boot默认配置的ObjectMapper。
     * 2. 因此，SpringBoot 提供了一个类，进行扩展和修改默认的 ObjectMapper 配置
     **/
    private void applyDefaultConfigurations(Jackson2ObjectMapperBuilder builder) {
        builder
                // 序列化时，对象为 null，是否抛异常
                .failOnEmptyBeans(false)
                // 反序列化时，json 中包含 pojo 不存在属性时，是否抛异常
                .failOnUnknownProperties(false)
                // 禁止将 java.util.Date, Calendar 序列化为数字(时间戳)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                // 设置 java.util.Date, Calendar 序列化、反序列化的格式
                .dateFormat(new SimpleDateFormat(DEFAULT_DATETIME_PATTERN))
                // 设置 java.util.Date、Calendar 序列化、反序列化的时区
                .timeZone(TimeZone.getTimeZone("GMT+8"));

        // 配置 Jackson 序列化 和 反序列化 部分特殊类型
        builder
                .serializerByType(BigDecimal.class, ToStringSerializer.instance)
                .serializerByType(BigInteger.class, BigNumberAdapter.INSTANCE)
                .serializerByType(Long.class, BigNumberAdapter.INSTANCE)
                .serializerByType(Instant.class, ToStringSerializer.instance)
                .deserializerByType(Instant.class, InstantDeserializer.INSTANT);

        // 配置 Jackson 序列化 和 反序列化 LocalDateTime、LocalDate、LocalTime 时使用的格式
        builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATETIME_PATTERN)))
                .serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                .serializers(new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)))
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATETIME_PATTERN)))
                .deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                .deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));

        // 添加对 guava 集合的序列化支持
        builder.modules(new GuavaModule());

        // 配置默认类型信息，以解决序列化对象中嵌套对象的类型信息问题
        builder.postConfigurer(objectMapper -> objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY)
        );

        log.info("jackson default config init successfully");
    }
}
