import { request } from "@/utils/service"
import type * as User from "./types/user"

/**
 * 分页查询用户
 **/
export function userTableApi(data: User.UserFormRequestData) {
  return request<User.UserTableResponseData>({
    url: "/system/user/list",
    method: "post",
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize
    },
    data: data
  })
}

/**
 * 新增用户
 */
export function createUserApi(data: User.UserFormRequestData) {
  return request<User.CreateUserResponseData>({
    url: "/system/user/create",
    method: 'post',
    data: data
  })
}
