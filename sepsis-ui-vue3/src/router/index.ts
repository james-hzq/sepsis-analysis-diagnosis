import { type RouteRecordRaw, createRouter } from "vue-router"
import { history, flatMultiLevelRoutes } from "./helper"
import routeSettings from "@/config/route"

const Layouts = () => import("@/layouts/index.vue")

/**
 * @author hzq
 * @date 2024/11/8 9:22
 * @apiNote 除了 login/redirect/callback/error 等隐藏页面，其他页面建议设置 Name 属性
 **/
export const constantRoutes: RouteRecordRaw[] = [
  {
    path: "/login",
    component: () => import("@/views/login/index.vue"),
    meta: {
      hidden: true
    }
  },
  {
    path: "/callback",
    component: () => import("@/views/login/callback.vue"),
    meta: {
      hidden: true
    }
  },
  {
    path: "/error",
    component: () => import("@/views/error/index.vue"),
    meta: {
      hidden: true
    }
  },
  {
    path: "/",
    component: Layouts,
    redirect: "/dashboard",
    children: [
      {
        path: "dashboard",
        component: () => import("@/views/dashboard/index.vue"),
        name: "Dashboard",
        meta: {
          title: "首页",
          svgIcon: "dashboard",
          affix: true
        }
      }
    ]
  },
]

/**
 * @author hzq
 * @date 2024/11/8 9:27
 * @apiNote 动态路由, 用来放置有权限 (Roles 属性) 的路由, 必须带有 Name 属性
 **/
export const dynamicRoutes: RouteRecordRaw[] = [
  {
    path: "/system",
    component: Layouts,
    redirect: "/system/user",
    name: "System",
    meta: {
      title: "后台管理",
      svgIcon: "lock",
      roles: ["root", "admin"],
      alwaysShow: true
    },
    children: [
      {
        path: "user",
        component: () => import("@/views/system/user/index.vue"),
        name: "SystemUser",
        meta: {
          title: "用户管理",
          roles: ["root", "admin"]
        }
      },
      {
        path: "role",
        component: () => import("@/views/system/role/index.vue"),
        name: "SystemRole",
        meta: {
          title: "角色管理",
          roles: ["root", "admin"]
        }
      }
    ]
  },
]

/**
 * @author hzq
 * @date 2024/11/8 9:28
 * @apiNote 配置路由器的历史记录模式和路由表。createRouter 方法用于创建 Vue Router 实例。
 **/
const router = createRouter({
  // 使用自定义的路由历史记录（通常是 HTML5 history 模式）
  history,
  // 配置路由，如果开启了三级缓存（thirdLevelRouteCache），则将路由扁平化处理；否则，直接使用原始路由配置
  routes: routeSettings.thirdLevelRouteCache ? flatMultiLevelRoutes(constantRoutes) : constantRoutes
})

/**
 * @author hzq
 * @date 2024/11/8 9:30
 * @apiNote 重置路由
 **/
export function resetRouter() {
  // 注意：所有动态路由路由必须带有 Name 属性，否则可能会不能完全重置干净
  try {
    router.getRoutes().forEach((route) => {
      const { name, meta } = route
      if (name && meta.roles?.length) {
        router.hasRoute(name) && router.removeRoute(name)
      }
    })
  } catch {
    // 强制刷新浏览器也行，只是交互体验不是很好
    window.location.reload()
  }
}

export default router
