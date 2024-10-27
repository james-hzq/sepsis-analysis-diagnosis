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
    data
  })
}

/** Github授权登录并返回 Token */
export function GithubLoginApi() {
  return request<Login.LoginResponseData>({
    url: "/auth/github/login",
    method: "post",
  })
}

/** 获取用户详情 */
export function getLoginUserInfoApi() {
  return request<Login.LoginUserInfoResponseData>({
    url: "/oauth/login/user-info",
    method: "get"
  })
}
