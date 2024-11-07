/** 定义了一个 Pinia store，用于管理浏览器标签页的状态，特别是有关已访问和已缓存标签页的数据。 */

import { ref, watchEffect } from "vue"
import { defineStore } from "pinia"
import { useSettingsStore } from "./settings"
import { type RouteLocationNormalized } from "vue-router"
import { getVisitedViews, setVisitedViews, getCachedViews, setCachedViews } from "@/utils/cache/local-storage"

/**
 * @author hzq
 * @date 2024/11/7 17:01
 * @apiNote 定义 TagView 类型，表示标签视图，部分路由信息
 **/
export type TagView = Partial<RouteLocationNormalized>

/**
 * @author hzq
 * @date 2024/11/7 17:02
 * @apiNote 定义一个 Pinia store，用于管理标签视图
 **/
export const useTagsViewStore = defineStore("tags-view", () => {
  // 从 settings store 中获取 cacheTagsView 设置，判断是否缓存标签页
  const { cacheTagsView } = useSettingsStore()
  // 创建响应式变量 visitedViews，存储已访问的标签页
  const visitedViews = ref<TagView[]>(cacheTagsView ? getVisitedViews() : [])
  // 创建响应式变量 cachedViews，存储已缓存的视图名称
  const cachedViews = ref<string[]>(cacheTagsView ? getCachedViews() : [])

  /**
   * @author hzq
   * @date 2024/11/7 17:07
   * @apiNote 缓存标签栏数据，监听响应式数据的变化，当 visitedViews 或 cachedViews 发生变化时执行
   **/
  watchEffect(() => {
    setVisitedViews(visitedViews.value)
    setCachedViews(cachedViews.value)
  })

  /**
   * @author hzq
   * @date 2024/11/7 17:08
   * @apiNote 添加已访问标签页
   **/
  const addVisitedView = (view: TagView) => {
    // 检查是否已经存在相同的 visitedView
    const index = visitedViews.value.findIndex((v) => v.path === view.path)

    if (index !== -1) {
      // 如果路径不同，则更新该视图，防止 query 参数丢失
      visitedViews.value[index].fullPath !== view.fullPath && (visitedViews.value[index] = { ...view })
    } else {
      // 添加新的 visitedView
      visitedViews.value.push({ ...view })
    }
  }

  /**
   * @author hzq
   * @date 2024/11/7 17:11
   * @apiNote 删除已访问的标签页
   **/
  const delVisitedView = (view: TagView) => {
    // 查找该视图的索引，如果找到，删除该视图
    const index = visitedViews.value.findIndex((v) => v.path === view.path)
    if (index !== -1) visitedViews.value.splice(index, 1)
  }

  /**
   * @author hzq
   * @date 2024/11/7 17:10
   * @apiNote 添加已缓存的视图
   **/
  const addCachedView = (view: TagView) => {
    // 如果视图名称不是字符串，返回
    if (typeof view.name !== "string") return
    // 如果视图名称已存在，返回
    if (cachedViews.value.includes(view.name)) return
    // 如果该视图需要保持活跃，则将视图名称添加到缓存中
    if (view.meta?.keepAlive) cachedViews.value.push(view.name)
  }

  /**
   * @author hzq
   * @date 2024/11/7 17:12
   * @apiNote 删除已缓存的视图
   **/
  const delCachedView = (view: TagView) => {
    // 如果视图名称不是字符串，返回
    if (typeof view.name !== "string") return
    // 查找该视图名称的索引，如果找到，删除该视图
    const index = cachedViews.value.indexOf(view.name)
    if (index !== -1) cachedViews.value.splice(index, 1)
  }

  /**
   * @author hzq
   * @date 2024/11/7 17:13
   * @apiNote 删除除当前视图外的所有已访问标签页
   **/
  const delOthersVisitedViews = (view: TagView) => {
    // // 过滤出所有的标签页，保留 affix 属性的标签和当前视图
    visitedViews.value = visitedViews.value.filter((v) => {
      return v.meta?.affix || v.path === view.path
    })
  }

  /**
   * @author hzq
   * @date 2024/11/7 17:13
   * @apiNote 删除除当前视图外的所有已缓存视图
   **/
  const delOthersCachedViews = (view: TagView) => {
    // 如果视图名称不是字符串，返回
    if (typeof view.name !== "string") return
    // 查找该视图名称的索引，如果找到，保留当前视图的缓存，如果没有找到，清空缓存
    const index = cachedViews.value.indexOf(view.name)
    if (index !== -1) {
      cachedViews.value = cachedViews.value.slice(index, index + 1)
    } else {
      // 如果 index = -1, 没有缓存的 tags
      cachedViews.value = []
    }
  }

  /**
   * @author hzq
   * @date 2024/11/7 17:14
   * @apiNote 删除所有已访问标签页
   **/
  const delAllVisitedViews = () => {
    // 保留固定标签页（affix 为 true 的标签）
    visitedViews.value = visitedViews.value.filter((tag) => tag.meta?.affix)
  }

  /**
   * @author hzq
   * @date 2024/11/7 17:15
   * @apiNote 删除所有已缓存视图
   **/
  const delAllCachedViews = () => {
    cachedViews.value = []
  }

  // 返回 store 中的所有状态和方法
  return {
    visitedViews,
    cachedViews,
    addVisitedView,
    addCachedView,
    delVisitedView,
    delCachedView,
    delOthersVisitedViews,
    delOthersCachedViews,
    delAllVisitedViews,
    delAllCachedViews
  }
})
