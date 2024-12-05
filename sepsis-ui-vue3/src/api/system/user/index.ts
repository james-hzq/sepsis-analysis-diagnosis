import { request } from "@/utils/service"
import type * as User from "./types/user"

/**
 * @author hua
 * @date 2024/12/1 16:06
 * @apiNote 分页查询用户
 **/
export function userTableApi(data: User.UserTableRequestData) {
  return request<User.UserTableResponseData>({
    url: "/system/user/list",
    method: "post",
    data: data
  })
}
