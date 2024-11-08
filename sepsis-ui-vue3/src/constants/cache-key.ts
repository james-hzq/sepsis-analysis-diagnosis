const SYSTEM_NAME = "sepsis-analysis-diagnosis"

/***
 * @author hzq
 * @date 2024/11/8 10:12
 * @apiNote 缓存数据时用到的 Key。static 关键字用于定义静态属性，这意味着这些属性是属于类本身的，而不是类的实例。
 **/
class CacheKey {
  static readonly TOKEN = `${SYSTEM_NAME}-token-key`
  static readonly CONFIG_LAYOUT = `${SYSTEM_NAME}-config-layout-key`
  static readonly SIDEBAR_STATUS = `${SYSTEM_NAME}-sidebar-status-key`
  static readonly ACTIVE_THEME_NAME = `${SYSTEM_NAME}-active-theme-name-key`
  static readonly VISITED_VIEWS = `${SYSTEM_NAME}-visited-views-key`
  static readonly CACHED_VIEWS = `${SYSTEM_NAME}-cached-views-key`
}

export default CacheKey
