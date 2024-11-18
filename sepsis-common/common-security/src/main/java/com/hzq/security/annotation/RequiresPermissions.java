package com.hzq.security.annotation;

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

    // 必须具备的角色
    String value();
}
