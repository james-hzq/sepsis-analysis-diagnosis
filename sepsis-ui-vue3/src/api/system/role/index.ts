import { request } from "@/utils/service"
import type * as Role from "./types/role"

/**
 * 查询角色
 **/
export function roleTableApi(data: Role.RoleFormRequestData) {
  return request<Role.RoleTableResponseData>({
    url: "/system/role/list",
    method: "post",
    data: data
  })
}

/**
 * 新增角色
 */
export function createRoleApi(data: Role.RoleFormRequestData) {
  return request<Role.CreateRoleResponseData>({
    url: "/system/role/create",
    method: 'post',
    data: data
  })
}

/**
 * 修改用户
 */
export function updateRoleApi(data: Role.RoleFormRequestData) {
  return request<Role.UpdateRoleResponseData>({
    url: "/system/role/update",
    method: 'post',
    data: data
  })
}

/**
 * 删除角色
 */
export function deleteRoleApi(data: String) {
  return request<Role.DeleteRoleResponseData>({
    url: "/system/role/delete",
    method: 'post',
    data: data
  })
}

