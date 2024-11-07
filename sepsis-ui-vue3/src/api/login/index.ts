import { request } from "@/utils/service"
import type * as Login from "./types/login"

/** 获取登录验证码 */
export function getLoginCodeApi() {
  return request<Login.LoginCodeResponseData>({
    url: "/oauth/login/code",
    method: "get"
  })
}

/** 系统登录并返回 Token */
export function SystemLoginApi(data: Login.SystemLoginRequestData) {
  return request<Login.LoginResponseData>({
    url: "/oauth/system/login",
    method: "post",
    data: data
  })
}

/** 获取用户详情 */
export function getLoginUserInfoApi() {
  return request<Login.LoginUserInfoResponseData>({
    url: "/oauth/login/user-info",
    method: "get"
  })
}

/**
 * @author hzq
 * @date 2024/11/7 15:31
 * @apiNote 获取系统用户名密码登录的用户详细信息
 **/
export function getSystemLoginUserInfoApi() {
  return request<Login.LoginUserInfoData>({
    url: "/auth/system/user/info",
    method: "get"
  })
}

/**
 * @author hzq
 * @date 2024/11/7 15:29
 * @apiNote 获取第三方登录的用户详细信息
 **/
export function getOAuth2LoginUserInfoApi() {
  return request<Login.LoginUserInfoData>({
    url: "/auth/oauth2/user/info",
    method: 'get'
  })
}
