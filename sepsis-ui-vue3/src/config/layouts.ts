import { getConfigLayout } from "@/utils/cache/local-storage"
import { LayoutModeEnum } from "@/constants/app-key"

/**
 * @author hzq
 * @date 2024/11/8 9:54
 * @apiNote 定义项目配置类型
 **/
export interface LayoutSettings {
  /** 是否显示 Settings Panel */
  showSettings: boolean
  /** 布局模式 */
  layoutMode: LayoutModeEnum
  /** 是否显示标签栏 */
  showTagsView: boolean
  /** 是否显示 Logo */
  showLogo: boolean
  /** 是否固定 Header */
  fixedHeader: boolean
  /** 是否显示页脚 Footer */
  showFooter: boolean
  /** 是否显示消息通知 */
  showNotify: boolean
  /** 是否显示切换主题按钮 */
  showThemeSwitch: boolean
  /** 是否显示全屏按钮 */
  showScreenfull: boolean
  /** 是否显示搜索按钮 */
  showSearchMenu: boolean
  /** 是否缓存标签栏 */
  cacheTagsView: boolean
  /** 开启系统水印 */
  showWatermark: boolean
  /** 是否显示灰色模式 */
  showGreyMode: boolean
  /** 是否显示色弱模式 */
  showColorWeakness: boolean
}

/**
 * @author hzq
 * @date 2024/11/8 9:53
 * @apiNote 默认项目配置
 **/
const defaultSettings: LayoutSettings = {
  layoutMode: LayoutModeEnum.Left,
  showSettings: true,
  showTagsView: true,
  fixedHeader: true,
  showFooter: true,
  showLogo: true,
  showNotify: true,
  showThemeSwitch: true,
  showScreenfull: true,
  showSearchMenu: true,
  cacheTagsView: false,
  showWatermark: true,
  showGreyMode: false,
  showColorWeakness: false
}

/**
 * @author hzq
 * @date 2024/11/8 9:55
 * @apiNote 到处项目配置
 * 1. ... 是 JavaScript 中的展开运算符的一种使用方式。
 * 2. 在这里，它的作用是将对象 defaultSettings 和 getConfigLayout() 的返回值进行浅拷贝合并，然后赋给 layoutSettings。
 * 3. 这种做法通常用于在不修改原始对象的情况下，创建一个新的对象，其中包含了原始对象的属性以及其他对象的属性。
 **/
export const layoutSettings: LayoutSettings = { ...defaultSettings, ...getConfigLayout() }
