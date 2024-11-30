/**
 * @author hzq
 * @date 2024/11/7 14:41
 * @apiNote 系统用户名密码登录参数
 **/
export interface SystemLoginRequestData {
  // 用户名
  username: string
  // 密码
  password: string
}

/**
 * @author hzq
 * @date 2024/11/7 14:42
 * @apiNote OAuth2第三方授权登录回调数据
 **/
export interface OAuth2LoginCallbackData {
  // 登录类型
  loginType: string
  // OAuth2客户端授权后生成的 access_token
  accessToken: string
  // OAuth2客户端授权后生成的 refresh_token
  refreshToken: string
}

/**
 * @author hzq
 * @date 2024/11/7 14:59
 * @apiNote 登录用户信息
 **/
export interface LoginUserInfoData {
  // 用户名
  username: string
  // 所属角色
  roles: string[]
}

/**
 * @param
 * @return
 * @author hzq
 * @date 2024/11/7 14:56
 * @apiNote 获取用户信息响应数据（系统登录 + 三方登录）
 **/
export type LoginUserInfoResponseData = ApiResponseData<LoginUserInfoData>

/**
 * @author hzq
 * @date 2024/11/7 15:22
 * @apiNote 系统用户名密码登录回调响应数据（返回JWT）
 **/
export type SystemLoginResponseData = ApiResponseData<string>

/**
 * @author hua
 * @date 2024/11/30 20:43
 * @apiNote 登出响应数据
 **/
export type LogoutResponseData = ApiResponseData<any>
