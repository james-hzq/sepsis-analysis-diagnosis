/***
 * @author hzq
 * @date 2024/11/8 9:52
 * @apiNote 路由配置
 **/
interface RouteSettings {
  /**
   * 是否开启动态路由功能？
   * 1. 开启后需要后端配合，在查询用户详情接口返回当前用户可以用来判断并加载动态路由的字段（该项目用的是角色 roles 字段）
   * 2. 假如项目不需要根据不同的用户来显示不同的页面，则应该将 dynamic: false
   */
  dynamic: boolean
  /** 当动态路由功能关闭时：
   * 1. 应该将所有路由都写到常驻路由里面（表明所有登录的用户能访问的页面都是一样的）
   * 2. 系统自动给当前登录用户赋值一个没有任何作用的默认角色
   */
  defaultRoles: Array<string>
  /**
   * 是否开启三级及其以上路由缓存功能？
   * 1. 开启后会进行路由降级（把三级及其以上的路由转化为二级路由）
   * 2. 由于都会转成二级路由，所以二级及其以上路由有内嵌子路由将会失效
   */
  thirdLevelRouteCache: boolean
}

/**
 * @author hzq
 * @date 2024/11/8 9:57
 * @apiNote 默认路由配置
 * 1. export default：
 * a) 可以用于导出一个默认的值，每个模块只能有一个默认导出。
 * b) 在导入时，可以使用 import ... from ... 的形式来导入默认导出的值，无需使用大括号 {}。
 * 2. export const：
 * a) 用于导出一个具名的常量、变量或函数，可以有多个具名导出。
 * b) 在导入时，需要使用与导出的名称相匹配的语法来导入，需要使用大括号 {}。
 **/
const routeSettings: RouteSettings = {
  dynamic: true,
  defaultRoles: ["user"],
  thirdLevelRouteCache: false
}

export default routeSettings
