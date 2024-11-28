import { request } from "@/utils/service"
import type * as Login from "./types/login"

/** 系统登录并返回 Token */
export function systemLoginApi(data: Login.SystemLoginRequestData) {
  return request<Login.LoginResponseData>({
    url: "/auth/system/login",
    method: "post",
    data: data
  })
}

/** 获取用户详情 */
export function loginUserInfoApi() {
  return request<Login.LoginUserInfoResponseData>({
    url: "/auth/user/info",
    method: "post"
  })
}
