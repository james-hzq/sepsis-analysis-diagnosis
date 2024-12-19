import { request } from "@/utils/service"
import type * as Index from "@/api/analysis/statistics/types"

/**
 * 请求患者年龄图表数据
 */
export function ageChartApi(url: String, icuAnalysisRequestData: Index.AnalysisRequestData) {
  return request<Index.AgeChartResponseData>({
    url: "/analysis" + url,
    method: "get",
    params: icuAnalysisRequestData
  })
}

/**
 * 请求患者身高和体重图表数据
 */
export function heightAndWeightChartApi(url: String, icuAnalysisRequestData: Index.AnalysisRequestData) {
  return request<Index.HeightAndWeightChartResponseData>({
    url: "/analysis" + url,
    method: "get",
    params: icuAnalysisRequestData
  })
}

/**
 * 请求患者心率和呼吸图表数据
 */
export function heartAndBreathChartApi(url: String, icuAnalysisRequestData: Index.AnalysisRequestData) {
  return request<Index.HeartAndBreathChartResponseData>({
    url: "/analysis" + url,
    method: "get",
    params: icuAnalysisRequestData
  })
}

/**
 * 请求入ICU第一天尿量图表数据
 */
export function urineChartApi(url: String, icuAnalysisRequestData: Index.AnalysisRequestData) {
  return request<Index.UrineChartResponseData>({
    url: "/analysis" + url,
    method: "get",
    params: icuAnalysisRequestData
  })
}

/**
 * 请求入ICU第一天白细胞计数图表数据
 */
export function whiteBloodCellChartApi(url: String, icuAnalysisRequestData: Index.AnalysisRequestData) {
  return request<Index.WhiteBloodCellChartResponseData>({
    url: "/analysis" + url,
    method: "get",
    params: icuAnalysisRequestData
  })
}

/**
 * 请求患者结局
 */
export function endChartApi(url: String, icuAnalysisRequestData: Index.AnalysisRequestData) {
  return request<Index.EndChartResponseData>({
    url: "/analysis" + url,
    method: "get",
    params: icuAnalysisRequestData
  })
}

/**
 * 请求入ICU第一天得分图表数据
 */
export function scoreChartApi(url: String, icuAnalysisRequestData: Index.AnalysisRequestData) {
  return request<Index.ScoreChartResponseData>({
    url: "/analysis" + url,
    method: "get",
    params: icuAnalysisRequestData
  })
}
