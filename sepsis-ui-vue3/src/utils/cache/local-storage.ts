/** 统一处理 localStorage */

import CacheKey from "@/constants/cache-key"
import { type SidebarOpened, type SidebarClosed } from "@/constants/app-key"
import { type TagView } from "@/store/modules/tags-view"
import { type LayoutSettings } from "@/config/layouts"

/**
 * @author hzq
 * @date 2024/11/7 16:01
 * @apiNote 获取系统布局配置，从 localStorage 中获取数据并解析为 LayoutSettings 类型
 **/
export const getConfigLayout = () => {
  const json = localStorage.getItem(CacheKey.CONFIG_LAYOUT)
  return json ? (JSON.parse(json) as LayoutSettings) : null
}

/**
 * @author hzq
 * @date 2024/11/7 16:03
 * @apiNote 设置系统布局配置，将 LayoutSettings 类型数据转换为 JSON 字符串存储到 localStorage
 **/
export const setConfigLayout = (settings: LayoutSettings) => {
  localStorage.setItem(CacheKey.CONFIG_LAYOUT, JSON.stringify(settings))
}

/**
 * @author hzq
 * @date 2024/11/7 16:04
 * @apiNote 移除系统布局配置，从 localStorage 中删除对应 key 的数据
 **/
export const removeConfigLayout = () => {
  localStorage.removeItem(CacheKey.CONFIG_LAYOUT)
}

/**
 * @author hzq
 * @date 2024/11/7 16:04
 * @apiNote 获取侧边栏状态，返回 localStorage 中保存的侧边栏状态
 **/
export const getSidebarStatus = () => {
  return localStorage.getItem(CacheKey.SIDEBAR_STATUS)
}

/**
 * @author hzq
 * @date 2024/11/7 16:05
 * @apiNote 设置侧边栏状态，接受 SidebarOpened 或 SidebarClosed 类型并保存到 localStorage
 **/
export const setSidebarStatus = (sidebarStatus: SidebarOpened | SidebarClosed) => {
  localStorage.setItem(CacheKey.SIDEBAR_STATUS, sidebarStatus)
}

/**
 * @author hzq
 * @date 2024/11/7 16:07
 * @apiNote 获取访问过的视图，从 localStorage 中获取 JSON 数据并解析为 TagView[] 数组
 **/
export const getVisitedViews = () => {
  const json = localStorage.getItem(CacheKey.VISITED_VIEWS)
  // 空值合并运算符: 检查前面的值是否为 null 或 undefined。如果是，则返回右侧的值；如果不是，则返回左侧的值。
  return JSON.parse(json ?? "[]") as TagView[]
}

/**
 * @author hzq
 * @date 2024/11/7 16:08
 * @apiNote 设置访问过的视图，接受 TagView[] 数组，将其转换为 JSON 字符串并存储到 localStorage
 **/
export const setVisitedViews = (views: TagView[]) => {
  views.forEach((view) => {
    // 删除不必要的属性，防止 JSON.stringify 处理到循环引用
    delete view.matched
    delete view.redirectedFrom
  })
  localStorage.setItem(CacheKey.VISITED_VIEWS, JSON.stringify(views))
}

/**
 * @author hzq
 * @date 2024/11/7 16:11
 * @apiNote 获取缓存的视图名称数组，从 localStorage 中获取并解析为 string[] 类型
 **/
export const getCachedViews = () => {
  const json = localStorage.getItem(CacheKey.CACHED_VIEWS)
  // 空值合并运算符: 检查前面的值是否为 null 或 undefined。如果是，则返回右侧的值；如果不是，则返回左侧的值。
  return JSON.parse(json ?? "[]") as string[]
}

/**
 * @author hzq
 * @date 2024/11/7 16:11
 * @apiNote 设置缓存的视图名称数组，接受 string[] 类型并转换为 JSON 字符串存储到 localStorage
 **/
export const setCachedViews = (views: string[]) => {
  localStorage.setItem(CacheKey.CACHED_VIEWS, JSON.stringify(views))
}
