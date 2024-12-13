import { request } from "@/utils/service"
import type * as Index from "./types/index"

/**
 * 请求省份地图数据
 */
export function provinceMapApi() {
  return request<Index.ProvinceCityMapResponseData>({
    url: "/system/index/province",
    method: "get",
  })
}

/**
 * 请求城市地图数据
 */
export function cityMapApi(icuCity: string) {
  return request<Index.ProvinceCityMapResponseData>({
    url: "/system/index/city",
    method: "get",
    params: {
      icuCity: icuCity
    }
  })
}

/**
 * 请求城市医院信息
 */
export function hospitalInfoApi(icuCity: string) {
  return request<Index.HospitalInfoResponseData>({
    url: "/system/index/hosp",
    method: "get",
    params: {
      icuCity: icuCity
    }
  })
}
