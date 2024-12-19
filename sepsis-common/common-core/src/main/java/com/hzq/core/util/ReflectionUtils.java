package com.hzq.core.util;

import com.google.common.base.Strings;

import java.lang.reflect.Field;

/**
 * @author hua
 * @className com.hzq.core.util ReflectionUtils
 * @date 2024/12/18 21:26
 * @description 反射工具类
 */
public class ReflectionUtils {

    public static Object getFieldValue(Object object, String fieldName) {
        if (object == null || Strings.isNullOrEmpty(fieldName))
            throw new IllegalArgumentException("parameter must not be null");
        try {
            Class<?> clazz = object.getClass();
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            return null;
        }
    }
}
