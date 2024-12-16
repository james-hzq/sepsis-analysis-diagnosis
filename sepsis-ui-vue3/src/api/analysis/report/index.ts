import { request } from "@/utils/service"
import type * as Index from "./types/index"
import {PatientLocationResponseData} from "./types/index";

/**
 * 请求患者概括
 */
export function patientReportApi() {
  return request<Index.PatientReportResponseData>({
    url: "/analysis/report/summarize",
    method: "get",
  })
}

/**
 * 请求患者列表
 */
export function patientTableApi(pageReq: PageRequestData) {
  return request<Index.PatientTableResponseData>({
    url: "/analysis/report/list",
    method: "get",
    params: pageReq
  })
}

/**
 * 请求患者入院分组信息
 */
export function patientLocationApi() {
  return request<Index.PatientLocationResponseData>({
    url: "/analysis/report/location",
    method: "get"
  })
}
