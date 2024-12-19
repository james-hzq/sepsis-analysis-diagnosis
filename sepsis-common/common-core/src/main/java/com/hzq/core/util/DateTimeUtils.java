package com.hzq.core.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @author hua
 * @className com.hzq.core.util DateTimeUtils
 * @date 2024/11/30 16:13
 * @description 日期、时间工具类
 */
public class DateTimeUtils {

    private DateTimeUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // UTC 时区标识
    public static final ZoneId UTC_ZONE = ZoneId.of("UTC");
    // 中国时区标识
    private static final ZoneId CHINA_ZONE = ZoneId.of("Asia/Shanghai");
    // 默认日期格式化器
    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // 默认时间格式化器
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    // 默认日期时间格式化器
    public static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String formatInstantToUtc(Instant instant) {
        return formatInstantToUtc(instant, DEFAULT_DATETIME_FORMATTER);
    }

    public static String formatInstantToUtc(Instant instant, DateTimeFormatter formatter) {
        return formatInstant(instant, UTC_ZONE, formatter);
    }

    public static String formatInstantToChina(Instant instant) {
        return formatInstantToChina(instant, DEFAULT_DATETIME_FORMATTER);
    }

    public static String formatInstantToChina(Instant instant, DateTimeFormatter formatter) {
        return formatInstant(instant, CHINA_ZONE, formatter);
    }

    /**
     * @author hua
     * @date 2024/11/30 16:29
     * @param instant   时间戳
     * @param zoneId    时区
     * @param formatter 自定义格式化器
     * @return java.lang.String
     * @apiNote 使用自定义格式化器格式化时间戳
     **/
    public static String formatInstant(Instant instant, ZoneId zoneId, DateTimeFormatter formatter) {
        if (instant == null || zoneId == null || formatter == null)
            throw new IllegalArgumentException("parameter must not be null");
        return instant.atZone(zoneId).format(formatter);
    }

    public static long calcInstantDiffMicros(Instant instantOne, Instant instantTwo) {
        return calcInstantDiff(instantOne, instantTwo, ChronoUnit.MICROS);
    }

    public static long calcInstantDiffMillis(Instant instantOne, Instant instantTwo) {
        return calcInstantDiff(instantOne, instantTwo, ChronoUnit.MILLIS);
    }

    public static long calcInstantDiffSeconds(Instant instantOne, Instant instantTwo) {
        return calcInstantDiff(instantOne, instantTwo, ChronoUnit.SECONDS);
    }

    public static long calcInstantDiffMinutes(Instant instantOne, Instant instantTwo) {
        return calcInstantDiff(instantOne, instantTwo, ChronoUnit.MINUTES);
    }

    public static long calcInstantDiffHours(Instant instantOne, Instant instantTwo) {
        return calcInstantDiff(instantOne, instantTwo, ChronoUnit.HOURS);
    }

    public static long calcInstantDiffDays(Instant instantOne, Instant instantTwo) {
        return calcInstantDiff(instantOne, instantTwo, ChronoUnit.DAYS);
    }

    public static long calcInstantDiffMonths(Instant instantOne, Instant instantTwo) {
        return calcInstantDiff(instantOne, instantTwo, ChronoUnit.MONTHS);
    }

    public static long calcInstantDiffYears(Instant instantOne, Instant instantTwo) {
        return calcInstantDiff(instantOne, instantTwo, ChronoUnit.YEARS);
    }

    /**
     * @author hua
     * @date 2024/11/30 16:32
     * @param instantOne 第一个时间戳
     * @param instantTwo 第二个时间戳
     * @param unit 时间单位
     * @return long
     * @apiNote 计算时间差
     **/
    public static long calcInstantDiff(Instant instantOne, Instant instantTwo, ChronoUnit unit) {
        if (instantOne == null || instantTwo == null || unit == null) throw new IllegalArgumentException("parameter must not be null");
        return Math.abs(instantOne.until(instantTwo, unit));
    }

    /**
     * @author hua
     * @date 2024/12/6 17:34
     * @param timestamp 时间戳
     * @return java.time.LocalDateTime
     * @apiNote 将时间戳转换为时间对象
     **/
    public static LocalDateTime convertTimestampToLocalDateTime(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    public static LocalDateTime convertUtcInstantToLocalDateTime(Instant instant) {
        return convertInstantToLocalDateTime(instant, UTC_ZONE);
    }

    public static LocalDateTime convertChinaInstantToLocalDateTime(Instant instant) {
        return convertInstantToLocalDateTime(instant, CHINA_ZONE);
    }

    /***
     * @author hua
     * @date 2024/12/17 20:43
     * @param instant 瞬时时刻
     * @param zoneId 时区信息
     * @return java.time.LocalDateTime
     * @apiNote 根据给定的时刻创建本地日期时间
     **/
    public static LocalDateTime convertInstantToLocalDateTime(Instant instant, ZoneId zoneId) {
        if (instant == null || zoneId == null) throw new IllegalArgumentException("parameter must not be null");
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    public static LocalDateTime convertStringToUtcLocalDateTime(String timeStr) {
        return convertStringToUtcLocalDateTime(timeStr, DEFAULT_DATETIME_FORMATTER);
    }

    public static LocalDateTime convertStringToUtcLocalDateTime(String timeStr, DateTimeFormatter formatter) {
        return convertStringToLocalDateTime(timeStr, UTC_ZONE, formatter);
    }

    public static LocalDateTime convertStringToChinaLocalDateTime(String timeStr) {
        return convertStringToChinaLocalDateTime(timeStr, DEFAULT_DATETIME_FORMATTER);
    }

    public static LocalDateTime convertStringToChinaLocalDateTime(String timeStr, DateTimeFormatter formatter) {
        return convertStringToLocalDateTime(timeStr, CHINA_ZONE, formatter);
    }

    /***
     * @author hua
     * @date 2024/12/17 20:46
     * @param timeStr   时间字符串
     * @param zoneId    时区
     * @param formatter 自定义格式化器
     * @return java.time.LocalDateTime
     * @apiNote 使用 formatter 解析文本，返回本地日期时间
     **/
    public static LocalDateTime convertStringToLocalDateTime(String timeStr, ZoneId zoneId, DateTimeFormatter formatter) {
        if (timeStr == null || zoneId == null || formatter == null) throw new IllegalArgumentException("parameter must not be null");
        return LocalDateTime.parse(timeStr, formatter.withZone(zoneId));
    }

    public static LocalDate convertStringToUtcLocalDate(String timeStr) {
        return convertStringToUtcLocalDate(timeStr, DEFAULT_DATE_FORMATTER);
    }

    public static LocalDate convertStringToUtcLocalDate(String timeStr, DateTimeFormatter formatter) {
        return convertStringToLocalDate(timeStr, UTC_ZONE, formatter);
    }

    public static LocalDate convertStringToChinaLocalDate(String timeStr) {
        return convertStringToChinaLocalDate(timeStr, DEFAULT_DATE_FORMATTER);
    }

    public static LocalDate convertStringToChinaLocalDate(String timeStr, DateTimeFormatter formatter) {
        return convertStringToLocalDate(timeStr, CHINA_ZONE, formatter);
    }

    /***
     * @author hua
     * @date 2024/12/17 20:46
     * @param timeStr   时间字符串
     * @param zoneId    时区
     * @param formatter 自定义格式化器
     * @return java.time.LocalDate
     * @apiNote 使用 formatter 解析文本，返回本地日期时间
     **/
    public static LocalDate convertStringToLocalDate(String timeStr, ZoneId zoneId, DateTimeFormatter formatter) {
        if (timeStr == null || zoneId == null || formatter == null) throw new IllegalArgumentException("parameter must not be null");
        return LocalDate.parse(timeStr, formatter);
    }
}
