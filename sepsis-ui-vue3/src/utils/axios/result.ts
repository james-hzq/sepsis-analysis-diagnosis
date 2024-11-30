/**
 * @author hua
 * @date 2024/11/30 23:48
 * @apiNote HTTP状态码错误信息映射, 集中管理网络请求的错误消息
 **/
export const HTTP_ERROR_MESSAGES: Record<number, string> = {
  // 4xx 客户端错误
  400: "请求参数错误",
  401: "未授权，请重新登录",
  403: "拒绝访问，权限不足",
  404: "请求资源不存在",
  408: "请求超时，请稍后重试",
  // 5xx 服务器错误
  500: "服务器内部错误",
  501: "服务器功能未实现",
  502: "网关错误，无效的响应",
  503: "服务暂不可用",
  504: "网关超时，服务器未及时响应",
  505: "HTTP协议版本不支持"
}

/**
 * @author hua
 * @date 2024/11/30 23:57
 * @apiNote 前后端约定结果集合信息映射
 **/
export enum ApiResponseCode {
  BAD_REQUEST = 400,
  UNAUTHORIZED = 401,
  ACCESS_FORBIDDEN = 403,
  SERVER_ERROR = 500,
  SUCCESS = 1000,
  WARN = 2000,
  ERROR = 3000
}

/**
 * @author hua
 * @date 2024/12/1 0:10
 * @apiNote 业务状态码对应的消息
 **/
export const API_RESPONSE_MESSAGE: Record<ApiResponseCode, string> = {
  [ApiResponseCode.BAD_REQUEST]: "请求异常",
  [ApiResponseCode.UNAUTHORIZED]: "用户未认证或认证过期",
  [ApiResponseCode.ACCESS_FORBIDDEN]: "用户没有权限访问",
  [ApiResponseCode.SERVER_ERROR]: "服务器内部错误",
}

/**
 * @author hua
 * @date 2024/11/30 23:49
 * @apiNote 通用的错误处理函数
 **/
export function getErrorMessage(status: number): string {
  return HTTP_ERROR_MESSAGES[status] || "未知网络错误，请稍后重试"
}
