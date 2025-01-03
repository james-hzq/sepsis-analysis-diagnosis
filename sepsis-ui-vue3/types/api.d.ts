/**
 * @author hzq
 * @date 2024/11/8 9:35
 * @apiNote 所有 api 接口的响应数据都应该准守该格式
 **/
interface ApiResponseData<T> {
  code: number
  message: string
  data: T
}
