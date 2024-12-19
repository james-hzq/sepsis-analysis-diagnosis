/**
 * 定义图表统计参数请求接口
 */
export interface AnalysisRequestData {
  // 起始时间
  startTime: string
  // 终止时间
  endTime: string
}

/**
 * 定义用于绘制图表的对象，与后端协同一致
 */
export interface DrawItemData<T> {
  name: string
  value: T
}

/**
 * 年龄图表展示数据
 */
export interface AgeChartData {
  // 年龄人数对应数组
  ageData: number[]
  // 年龄分组数据
  list: DrawItemData<number>[]
}

/**
 * 心率和呼吸展示数据
 */
export interface HeartAndBreathChartData {
  // 心率和呼吸 最大值、均值、最小值
  heartAndBreathList: DrawItemData<number>[]
  // 心率均值分组数据
  heartAvgList: DrawItemData<number>[]
}

export type AgeChartResponseData = ApiResponseData<AgeChartData>
export type HeightAndWeightChartResponseData = ApiResponseData<DrawItemData<number>[]>
export type HeartAndBreathChartResponseData = ApiResponseData<HeartAndBreathChartData>
export type EndChartResponseData = ApiResponseData<DrawItemData<number>[]>
export type ScoreChartResponseData = ApiResponseData<DrawItemData<number>[][]>
