/** 定义了一个 Pinia store，它管理了布局配置的状态，并将这些配置存储到 localStorage 中，以便在应用重启后仍然能够保留这些配置。 */

import { type Ref, ref, watch } from "vue"
import { defineStore } from "pinia"
import { type LayoutSettings, layoutSettings } from "@/config/layouts"
import { setConfigLayout } from "@/utils/cache/local-storage"

/**
 * @author hzq
 * @date 2024/11/7 16:44
 * @apiNote 定义 SettingsStore 类型，表示 store 中的状态
 **/
type SettingsStore = {
  // 使用映射类型来遍历 layoutSettings 对象的键, 为每个布局设置项创建响应式的变量
  [Key in keyof LayoutSettings]: Ref<LayoutSettings[Key]>
}

/**
 * @author hzq
 * @date 2024/11/7 16:46
 * @apiNote 定义 SettingsStoreKey 类型，表示 SettingsStore 对象中的键
 **/
type SettingsStoreKey = keyof SettingsStore

/**
 * @author hzq
 * @date 2024/11/7 16:46
 * @apiNote 定义一个 Pinia store
 **/
export const useSettingsStore = defineStore("settings", () => {
  // 初始化状态对象，用于存储响应式的布局配置项
  const state = {} as SettingsStore

  // 遍历 layoutSettings 对象的键值对
  for (const [key, value] of Object.entries(layoutSettings)) {
    // 使用类型断言来指定 key 的类型，将 value 包装在 ref 函数中，创建一个响应式变量
    const refValue = ref(value)
    // @ts-ignore, 告诉 TypeScript 忽略下一行的类型检查错误。
    state[key as SettingsStoreKey] = refValue
    // 监听每个响应式变量, 当配置发生变化时，调用 setConfigLayout 方法保存新配置
    watch(refValue, () => {
      // 获取当前的配置数据
      const settings = _getCacheData()
      // 将配置数据保存到 localStorage
      setConfigLayout(settings)
    })
  }

  /**
   * @author hzq
   * @date 2024/11/7 16:53
   * @apiNote 获取要缓存的数据：将 state 对象转化为 settings 对象
   **/
  const _getCacheData = () => {
    // 创建一个新的 LayoutSettings 对象
    const settings = {} as LayoutSettings

    // 提取每个响应式变量的值并赋值到 settings 对象
    for (const [key, value] of Object.entries(state)) {
      // @ts-ignore, 告诉 TypeScript 忽略下一行的类型检查错误。
      settings[key as SettingsStoreKey] = value.value
    }
    return settings
  }

  // 返回完整的配置对象
  return state
})
