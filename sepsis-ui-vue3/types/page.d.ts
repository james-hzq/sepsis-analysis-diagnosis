/**
 * @author hzq
 * @date 2024/11/8 9:35
 * @apiNote 所有 分页请求（支持多字段同序排序） 接口的请求数据都应该附加该类型
 **/
interface PageRequestData {
  // 当前页码
  pageNum: number
  // 每页大小
  pageSize: number
  // 排序字段
  orderBy: string | null
  // 排序方向
  direction: 'ASC' | 'DESC' | null
}
