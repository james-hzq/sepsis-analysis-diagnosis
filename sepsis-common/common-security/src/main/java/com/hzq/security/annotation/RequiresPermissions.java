package com.hzq.security.annotation;

import org.intellij.lang.annotations.Language;

import java.lang.annotation.*;

/**
 * @author gc
 * @annotation com.hzq.security.annotation RequiresPermissions
 * @date 2024/11/18 16:25
 * @description 权限判断注解
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RequiresPermissions {

    @Language("SpEL")
    String value() default "user";
}
