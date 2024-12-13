package com.hzq.security.util;

import com.google.common.collect.Sets;
import com.hzq.security.constant.RoleEnum;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author gc
 * @class com.hzq.security.util RoleUtils
 * @date 2024/11/19 10:13
 * @description 权限工具类，用于权限校验
 */
public class PermissionUtils {

    /**
     * @author hzq
     * @date 2024/11/19 10:43
     * @apiNote 角色比较器
     **/
    public static class RoleComparator {

        /**
         * @param roles 角色数组
         * @return java.util.Set<java.lang.String>[]
         * @author hzq
         * @date 2024/11/19 14:23
         * @apiNote 将角色数组按照角色层级进行分组
         **/
        @SuppressWarnings("unchecked")
        public static Set<String>[] getGroupRoles(String[] roles) {
            Set<String>[] roleSet = (Set<String>[]) new Set[RoleEnum.getLastLevel() + 1];
            for (int i = 0;i < roleSet.length;i++) roleSet[i] = new HashSet<>();
            Arrays.stream(roles)
                    .map(RoleEnum::getRoleEnum)
                    .forEach(roleEnum -> roleSet[roleEnum.getLevel()].add(roleEnum.getKey()));
            return roleSet;
        }

        /**
         * @param roles 角色集合
         * @return java.util.Set<java.lang.String>[]
         * @author hzq
         * @date 2024/11/19 14:23
         * @apiNote 将角色集合按照角色层级进行分组
         **/
        @SuppressWarnings("unchecked")
        public static Set<String>[] getGroupRoles(Set<String> roles) {
            Set<String>[] roleSet = (Set<String>[]) new Set[RoleEnum.getLastLevel() + 1];
            for (int i = 0;i < roleSet.length;i++) roleSet[i] = new HashSet<>();
            roles.stream()
                    .map(RoleEnum::getRoleEnum)
                    .forEach(roleEnum -> roleSet[roleEnum.getLevel()].add(roleEnum.getKey()));
            return roleSet;
        }

        /**
         * @param ownedRoles 用户拥有的分组角色集合
         * @param requiredRoles 接口需要的分组角色集合
         * @return boolean
         * @author hzq
         * @date 2024/11/19 14:30
         * @apiNote 与逻辑角色权限进行检查
         **/
        public static boolean checkRolesAnd(Set<String>[] ownedRoles, Set<String>[] requiredRoles) {
            for (int i = 0; i < requiredRoles.length; i++) {
                Set<String> ownedRoleSet = ownedRoles[i];
                Set<String> requiredRoleSet = requiredRoles[i];

                if (ownedRoleSet.isEmpty() && requiredRoleSet.isEmpty()) continue;
                if (ownedRoleSet.isEmpty() || requiredRoleSet.isEmpty()) return requiredRoleSet.isEmpty();
                if (!ownedRoleSet.containsAll(requiredRoleSet)) return false;
            }
            return true;
        }

        /**
         * @param ownedRoles 用户拥有的分组角色集合
         * @param requiredRoles 接口需要的分组角色集合
         * @return boolean
         * @author hzq
         * @date 2024/11/19 14:30
         * @apiNote 或逻辑角色权限进行检查
         **/
        public static boolean checkRolesOr(Set<String>[] ownedRoles, Set<String>[] requiredRoles) {
            for (int i = 0; i < requiredRoles.length; i++) {
                Set<String> ownedRoleSet = ownedRoles[i];
                Set<String> requiredRoleSet = requiredRoles[i];

                if (ownedRoleSet.isEmpty() && requiredRoleSet.isEmpty()) continue;
                if (ownedRoleSet.isEmpty() || requiredRoleSet.isEmpty()) return requiredRoleSet.isEmpty();
                for (String role : requiredRoleSet) {
                    if (ownedRoleSet.contains(role)) return true;
                }
            }
            return false;
        }
    }
}
