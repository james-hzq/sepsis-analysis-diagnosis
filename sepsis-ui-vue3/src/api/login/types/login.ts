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
  // 验证码
  code: string
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
 * @date 2024/11/7 15:21
 * @apiNote OAuth2授权登录回调响应数据
 **/
export type OAuth2LoginResponseData = ApiResponseData<OAuth2LoginCallbackData>

/**
 * @author hzq
 * @date 2024/11/7 15:24
 * @apiNote 系统用户名密码登录验证码回调响应数据（返回验证码）
 **/
export type SystemLoginCodeResponseData = ApiResponseData<string>

/**
 * @author hzq
 * @date 2024/11/7 15:22
 * @apiNote 系统用户名密码登录回调响应数据（返回JWT）
 **/
export type SystemLoginResponseData = ApiResponseData<string>

/** 登录获取验证码响应数据 */
export type LoginCodeResponseData = ApiResponseData<string>

/** 登录获取token响应数据 */
export type LoginResponseData = ApiResponseData<string>
