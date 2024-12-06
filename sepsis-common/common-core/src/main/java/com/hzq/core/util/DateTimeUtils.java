package com.hzq.core.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * @author hua
 * @className com.hzq.core.util DateTimeUtils
 * @date 2024/11/30 16:13
 * @description 日期时间工具类
 */
public class DateTimeUtils {

    private DateTimeUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // UTC 时区标识
    public static final ZoneId UTC_ZONE = ZoneId.of("UTC");
    // 中国时区标识
    private static final ZoneId CHINA_ZONE = ZoneId.of("Asia/Shanghai");
    // 默认时间单位为秒
    private static final ChronoUnit DEFAULT_CHRONOUNIT = ChronoUnit.SECONDS;
    // 默认日期时间格式化器
    public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * @author hua
     * @date 2024/11/30 16:14
     * @return java.time.ZonedDateTime
     * @apiNote UTC 时间转换为中国本地时间
     **/
    public static ZonedDateTime utcToChina(Instant utcInstant) {
        return Objects.requireNonNull(utcInstant, "UTC Instant must not be null").atZone(CHINA_ZONE);
    }

    /**
     * @author hua
     * @date 2024/11/30 16:14
     * @return java.time.ZonedDateTime
     * @apiNote 中国本地时间转换为 UTC 时间戳
     **/
    public static Instant chinaToUtc(ZonedDateTime chinaTime) {
        return Objects.requireNonNull(chinaTime, "China ZonedDateTime must not be null").toInstant();
    }

    /**
     * @author hua
     * @date 2024/11/30 16:28
     * @return java.lang.String
     * @apiNote 格式化时间戳为可读字符串
     **/
    public static String formatChina(Instant instant) {
        return formatChina(instant, DEFAULT_FORMATTER);
    }

    /**
     * @author hua
     * @date 2024/11/30 16:29
     * @param instant   时间戳
     * @param formatter 自定义格式化器
     * @return java.lang.String
     * @apiNote 使用自定义格式化器格式化时间戳
     **/
    public static String formatChina(Instant instant, DateTimeFormatter formatter) {
        return (instant == null ? Instant.now() : instant)
                .atZone(CHINA_ZONE)
                .format(formatter == null ? DEFAULT_FORMATTER : formatter);
    }

    /**
     * @author hua
     * @date 2024/11/30 16:32
     * @param instantOne 第一个时间戳
     * @param instantTwo 第二个时间戳
     * @param unit 时间单位
     * @return long
     * @apiNote 时间差（秒）
     **/
    public static long compareInSeconds(Instant instantOne, Instant instantTwo, ChronoUnit unit) throws IllegalAccessException {
        if (instantOne == null || instantTwo == null) throw new IllegalAccessException("instant must not be null");
        return Math.abs(instantOne.until(instantTwo, unit == null ? DEFAULT_CHRONOUNIT : unit));
    }

    /**
     * @author hua
     * @date 2024/12/6 17:34
     * @param timestamp 时间戳
     * @return java.time.LocalDateTime
     * @apiNote 将时间戳转换为时间对象
     **/
    public static LocalDateTime convertToLocalDateTime(Object timestamp) {
        if (timestamp == null) return null;
        if (timestamp instanceof LocalDateTime t) return t;
        if (timestamp instanceof Timestamp t) return t.toLocalDateTime();
        return null;
    }
}
