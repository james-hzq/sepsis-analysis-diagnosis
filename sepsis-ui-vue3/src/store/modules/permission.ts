/** 定义了一个 Pinia store，用于处理和管理应用中的路由权限。 */

import { ref } from "vue"
import store from "@/store"
import { defineStore } from "pinia"
import { type RouteRecordRaw } from "vue-router"
import { constantRoutes, dynamicRoutes } from "@/router"
import { flatMultiLevelRoutes } from "@/router/helper"
import routeSettings from "@/config/route"

/**
 * @author hzq
 * @date 2024/11/7 17:23
 * @apiNote 权限校验函数，用于检查当前角色是否有访问指定路由的权限
 **/
const hasPermission = (roles: string[], route: RouteRecordRaw) => {
  const routeRoles = route.meta?.roles
  return routeRoles ? roles.some((role) => routeRoles.includes(role)) : true
}


/**
 * @author hzq
 * @date 2024/11/7 17:26
 * @apiNote 动态路由过滤函数，根据用户的角色过滤可访问的路由
 **/
const filterDynamicRoutes = (routes: RouteRecordRaw[], roles: string[]) => {
  // 结果数组，用于存储符合权限的路由
  const res: RouteRecordRaw[] = []
  // 遍历所有路由
  routes.forEach((route) => {
    // 拷贝当前路由，防止修改原数据
    const tempRoute = { ...route }
    // 检查当前用户是否有权限访问该路由
    if (hasPermission(roles, tempRoute)) {
      // 如果当前路由有子路由，则递归过滤子路由
      if (tempRoute.children) {
        tempRoute.children = filterDynamicRoutes(tempRoute.children, roles)
      }
      // 将符合权限的路由添加到结果数组
      res.push(tempRoute)
    }
  })
  return res
}

/**
 * @author hzq
 * @date 2024/11/7 17:27
 * @apiNote 定义名为 permission 的权限 store
 **/
export const usePermissionStore = defineStore("permission", () => {
  // 存储可访问的路由
  const routes = ref<RouteRecordRaw[]>([])
  // 存储有访问权限的动态路由
  const addRoutes = ref<RouteRecordRaw[]>([])

  /**
   * @author hzq
   * @date 2024/11/7 17:30
   * @apiNote 根据角色生成可访问的路由，最终可访问路由 = 常驻路由 + 有访问权限的动态路由
   **/
  const setRoutes = (roles: string[]) => {
    // 根据角色过滤动态路由, 将过滤后的动态路由添加到可访问的路由中
    const accessedRoutes = filterDynamicRoutes(dynamicRoutes, roles)
    _set(accessedRoutes)
  }

  /**
   * @author hzq
   * @date 2024/11/7 17:34
   * @apiNote 设置所有路由（常驻路由 + 所有动态路由）
   **/
  const setAllRoutes = () => {
    _set(dynamicRoutes)
  }

  /**
   * @author hzq
   * @date 2024/11/7 17:34
   * @apiNote 私有方法，用于设置路由数据
   **/
  const _set = (accessedRoutes: RouteRecordRaw[]) => {
    // // 合并常驻路由和动态路由
    routes.value = constantRoutes.concat(accessedRoutes)
    // 如果启用了三级路由缓存，则将动态路由扁平化，否则直接使用动态路由
    addRoutes.value = routeSettings.thirdLevelRouteCache ? flatMultiLevelRoutes(accessedRoutes) : accessedRoutes
  }

  return { routes, addRoutes, setRoutes, setAllRoutes }
})

/**
 * @author hzq
 * @date 2024/11/7 17:35
 * @apiNote 用于在 setup 外部访问 usePermissionStore
 **/
export function usePermissionStoreHook() {
  return usePermissionStore(store)
}
