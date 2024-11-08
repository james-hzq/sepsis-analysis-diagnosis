/** 定义 .env 文件的环境变量类型 */

/**
 * @author hzq
 * @date 2024/11/8 9:35
 * @apiNote 声明 vite 环境变量的类型（如果未声明则默认是 any）
 **/
interface ImportMetaEnv {
  // 应用标题
  readonly VITE_APP_TITLE: string
  // 后端接口公共路径
  readonly VITE_BASE_API: string
  // 路由模式：历史模式、哈希模式、HTML5 模式
  readonly VITE_ROUTER_HISTORY: "history" | "hash" | "html5"
  // 前端部署公共路径
  readonly VITE_PUBLIC_PATH: string
}

/**
 * @author hzq
 * @date 2024/11/8 9:37
 * @apiNote 声明 vite 环境变量类型
 **/
interface ImportMeta {
  readonly env: ImportMetaEnv
}
