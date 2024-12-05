export interface UserTableData {
  // 用户唯一编号
  userId: number
  // 用户名
  username: string
  // 邮箱
  email: string
  // 所属角色
  roles: string
  // 用户状态
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

export interface UserTableRequestData {
  // 用户唯一编号
  userId: string
  // 用户名（模糊查询）
  username: string
  // 邮箱
  email: string
  // 用户状态
  status: string
  // 创建开始时间
  startTime: string
  // 创建结束时间
  endTime: string
  // 当前页面
  page: number
  // 查询条数
  total: number
}

export type UserTableResponseData = ApiResponseData<UserTableData[]>
