export interface LoginRequestData {
  /** 用户名 */
  username: string
  /** 密码 */
  password: string
  /** 验证码 */
  code: string
}

/** 登录获取验证码响应数据 */
export type LoginCodeResponseData = ApiResponseData<string>

/** 登录获取token响应数据 */
export type LoginResponseData = ApiResponseData<string>

/** 登录获取用户信息响应数据 */
export type LoginUserInfoResponseData = ApiResponseData<{ username: string; roles: string[] }>
