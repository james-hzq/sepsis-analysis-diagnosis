import { request } from "@/utils/service"
import type * as Login from "./types/login"

/**
 * @author hua
 * @date 2024/11/30 20:45
 * @apiNote 系统登录并返回 Token
 **/
export function systemLoginApi(data: Login.SystemLoginRequestData) {
  return request<Login.SystemLoginResponseData>({
    url: "/auth/system/login",
    method: "post",
    params: {
      username: data.username,
      password: data.password
    }
  })
}

/**
 * @author hua
 * @date 2024/11/30 20:45
 * @apiNote 获取用户详情
 **/
export function loginUserInfoApi() {
  return request<Login.LoginUserInfoResponseData>({
    url: "/auth/user/info",
    method: "post"
  })
}

/**  */
/**
 * @author hua
 * @date 2024/11/30 20:45
 * @apiNote 系统登出
 **/
export function logoutApi() {
  return request<Login.LogoutResponseData>({
    url: "/auth/user/logout",
    method: "get"
  })
}
