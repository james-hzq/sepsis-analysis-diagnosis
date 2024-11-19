package com.hzq.security.constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author gc
 * @enum com.hzq.security.constant RoleEnum
 * @date 2024/11/19 9:13
 * @description 基础角色, 按照层级划分
 * 1. JVM 加载类的过程主要分为以下几个阶段：加载、验证、准备、解析、初始化
 * 2. 对于枚举类来说，枚举的每个常量实际上是静态的 final 实例。JVM 会将每个常量声明为一个静态字段，并在类初始化时按声明顺序逐一初始化。
 * 3. 枚举类的静态代码块会在所有静态字段（包括枚举常量）的初始化之后执行。
 * 4. 因此，在静态代码块中调用 RoleEnum.values() 或访问任何枚举常量时，枚举常量已经完成初始化。
 */
@Getter
public enum RoleEnum {
    ROOT("root", 0),
    ADMIN("admin", 1),
    USER("user", 2);

    private final String key;
    private final Integer level;

    RoleEnum(String key, Integer level) {
        this.key = key;
        this.level = level;
    }

    // 角色缓存，避免角色校验时重复转换
    private static final Map<String, RoleEnum> ROLE_CACHE = Arrays.stream(values())
            .collect(Collectors.toMap(RoleEnum::getKey, Function.identity()));

    // 缓存角色层级中最靠后的层级
    private static final Integer LAST_LEVEL = Arrays.stream(values())
            .mapToInt(RoleEnum::getLevel)
            .min()
            .orElse(0);

    public static RoleEnum getRoleEnum(String role) {
        return ROLE_CACHE.get(role);
    }

    public static int getLastLevel() {
        return LAST_LEVEL;
    }
}
