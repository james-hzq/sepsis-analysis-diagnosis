/** 定义了一个 Pinia store，用于管理和控制应用的侧边栏状态及设备类型。*/

import { reactive, ref, watch } from "vue"
import { defineStore } from "pinia"
import { getSidebarStatus, setSidebarStatus } from "@/utils/cache/local-storage"
import { DeviceEnum, SIDEBAR_OPENED, SIDEBAR_CLOSED } from "@/constants/app-key"

/**
 * @author hzq
 * @date 2024/11/7 17:38
 * @apiNote // 定义 Sidebar 接口，用于表示侧边栏的状态
 **/
interface Sidebar {
  // 侧边栏是否打开
  opened: boolean
  // 是否不使用动画
  withoutAnimation: boolean
}

/**
 * @author hzq
 * @date 2024/11/7 17:38
 * @apiNote 设置侧边栏状态的本地缓存
 **/
function handleSidebarStatus(opened: boolean) {
  // 根据侧边栏状态设置本地缓存，打开时保存打开状态，关闭时保存关闭状态
  opened ? setSidebarStatus(SIDEBAR_OPENED) : setSidebarStatus(SIDEBAR_CLOSED)
}

/**
 * @author hzq
 * @date 2024/11/7 17:39
 * @apiNote 定义名为 "app" 的 Pinia store
 **/
export const useAppStore = defineStore("app", () => {
  // 定义侧边栏状态
  const sidebar: Sidebar = reactive({
    // 获取本地缓存中的侧边栏状态，若未关闭则认为侧边栏是打开的
    opened: getSidebarStatus() !== SIDEBAR_CLOSED,
    // 默认不禁用动画
    withoutAnimation: false
  })

  // 定义设备类型，默认设备类型为桌面设备
  const device = ref<DeviceEnum>(DeviceEnum.Desktop)

  /**
   * @author hzq
   * @date 2024/11/7 17:40
   * @apiNote 监听侧边栏打开状态的变化，状态变化时执行 handleSidebarStatus 更新本地缓存
   **/
  watch(
    () => sidebar.opened,
    (opened) => handleSidebarStatus(opened)
  )

  /**
   * @author hzq
   * @date 2024/11/7 17:41
   * @apiNote 切换侧边栏的状态
   **/
  const toggleSidebar = (withoutAnimation: boolean) => {
    sidebar.opened = !sidebar.opened
    sidebar.withoutAnimation = withoutAnimation
  }

  /**
   * @author hzq
   * @date 2024/11/7 17:41
   * @apiNote 关闭侧边栏
   **/
  const closeSidebar = (withoutAnimation: boolean) => {
    sidebar.opened = false
    sidebar.withoutAnimation = withoutAnimation
  }

  /**
   * @author hzq
   * @date 2024/11/7 17:41
   * @apiNote 切换设备类型
   **/
  const toggleDevice = (value: DeviceEnum) => {
    device.value = value
  }

  // 返回响应式数据和方法，供组件访问
  return { device, sidebar, toggleSidebar, closeSidebar, toggleDevice }
})
