import axios, { type AxiosInstance, type AxiosRequestConfig } from "axios"
import { useUserStoreHook } from "@/store/modules/user"
import { ElMessage } from "element-plus"
import { get, merge } from "lodash-es"
import { getToken } from "./cache/cookies"
import {API_RESPONSE_MESSAGE, ApiResponseCode, HTTP_ERROR_MESSAGES} from "@/utils/axios/result";

/**
 * @author hua
 * @date 2024/11/30 23:40
 * @apiNote 退出登录并强制刷新页面（会重定向到登录页）
 **/
function logout() {
  useUserStoreHook().logout()
  location.reload()
}

/**
 * @author hua
 * @date 2024/11/30 23:51
 * @apiNote 创建请求方法
 **/
function createRequest(service: AxiosInstance) {
  return function <T>(config: AxiosRequestConfig): Promise<T> {
    const token = getToken()
    const defaultConfig = {
      headers: {
        Authorization: token ? `Bearer ${token}` : "",
        "Content-Type": "application/json"
      },
      timeout: 5000,
      baseURL: import.meta.env.VITE_BASE_API,
      data: {}
    }
    // 将默认配置 defaultConfig 和传入的自定义配置 config 进行合并成为 mergeConfig
    const mergeConfig = merge(defaultConfig, config)
    return service(mergeConfig)
  }
}

/**
 * @author hua
 * @date 2024/11/30 23:41
 * @apiNote 创建请求实例
 **/
function createService() {
  // 创建一个 axios 实例命名为 service
  const service = axios.create()

  // 请求拦截
  service.interceptors.request.use(
    config => config,
    error => Promise.reject(error)
  )

  // 响应拦截（可根据具体业务作出相应的调整）
  service.interceptors.response.use(
    response => {
      const { data, request } = response

      // 二进制数据则直接返回
      if (request?.responseType === "blob" || request?.responseType === "arraybuffer") {
        return data
      }

      // 这个 code 是和后端约定的业务 code
      const code = data.code
      const message = data.message

      // 如果没有 code, 代表这不是项目后端开发的 api
      if (code === undefined) {
        ElMessage.error("非本系统的接口")
        return Promise.reject(new Error("非本系统的接口"))
      }

      switch (code) {
        case ApiResponseCode.SUCCESS:
          return data
        case ApiResponseCode.BAD_REQUEST:
          ElMessage.error(API_RESPONSE_MESSAGE[ApiResponseCode.BAD_REQUEST])
          break
        case ApiResponseCode.UNAUTHORIZED:
          ElMessage.error(API_RESPONSE_MESSAGE[ApiResponseCode.UNAUTHORIZED])
          return logout()
        case ApiResponseCode.ACCESS_FORBIDDEN:
          ElMessage.error(API_RESPONSE_MESSAGE[ApiResponseCode.ACCESS_FORBIDDEN])
          break
        case ApiResponseCode.SERVER_ERROR:
          ElMessage.error(API_RESPONSE_MESSAGE[ApiResponseCode.SERVER_ERROR])
          break
        case ApiResponseCode.WARN:
          ElMessage.warning(message || "未知警告")
          break
        case ApiResponseCode.ERROR:
          ElMessage.error(message || "未知错误")
          break
        default:
          ElMessage.error("暂无该状态码，请练习管理员")
          return Promise.reject(new Error("Error"))
      }
    },
    (error) => {
      // status 是 HTTP 状态码
      const status = get(error, "response.status")
      // 根据 HTTP Status 获取对应错误信息
      const errorMessage = HTTP_ERROR_MESSAGES[status] || "未知网络错误"

      console.error('Network Error:', {
        status,
        message: errorMessage,
        details: error.response?.data
      })

      if (status === StatusCode.UNAUTHORIZED) {
        handleLogout()
      }

      ElMessage.error(errorMessage)
      return Promise.reject(error)
    }
  )
  return service
}

/**
 * @author hua
 * @date 2024/11/30 23:52
 * @apiNote 用于网络请求的实例
 **/
const service = createService()

/**
 * @author hua
 * @date 2024/11/30 23:52
 * @apiNote 用于网络请求的方法
 **/
export const request = createRequest(service)
