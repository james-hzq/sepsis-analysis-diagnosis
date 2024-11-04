import { request } from "@/utils/service"
/**
 * 获取授权确认页面相关数据
 * @param queryString 查询参数，地址栏参数
 * @returns 授权确认页面相关数据
 */
/** 系统登录并返回 Token */
export function getConsentParameters(queryString: string) {
  return request<String>({
    url: "/oauth2/consent/parameters/" + queryString,
    method: "get",
  })
}

/**
 * 提交授权确认
 * @param data 客户端、scope等
 * @param requestUrl 请求地址(授权码与设备码授权提交不一样)
 * @returns 是否确认成功
 */
export function submitApproveScope(data: any, requestUrl: string) {
  return request<String>({
    url: requestUrl,
    method: "post",
    data,
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  })
}
