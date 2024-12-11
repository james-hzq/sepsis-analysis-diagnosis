export interface RoleTableData {
  // 角色唯一编号
  roleId: number
  // 角色标识
  roleKey: string
  // 角色名称
  roleName: string
  // 角色状态
  status: string
  // 创建时间
  createTime: string
  // 创建者
  createBy: string
  // 更新时间
  updateTime: string
  // 更新者
  updateBy: string
}

export interface RoleFormRequestData {
  // 当前登录用户
  currUsername: string
  // 角色唯一编号
  roleId: string | null
  // 角色标识（模糊查询）
  roleKey: string
  // 角色名称
  roleName: string
  // 角色状态
  status: string
  // 创建开始时间
  startTime: string
  // 创建结束时间
  endTime: string
}

export type RoleTableResponseData = ApiResponseData<RoleTableData[]>
export type CreateRoleResponseData = ApiResponseData<any>
export type UpdateRoleResponseData = ApiResponseData<any>
export type DeleteRoleResponseData = ApiResponseData<any>
