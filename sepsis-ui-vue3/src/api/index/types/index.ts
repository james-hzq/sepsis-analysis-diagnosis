export interface ProvinceCityMapData {
  // 省份名称
  name: string
  // 站点医院数量
  value: number
}

export interface HospitalInfoData {
  // 编号
  id: number
  // 城市
  icuCity: string
  // 地区
  icuDist: string
  // icu
  icu: string
  // 医院名称
  hospName: string
}

export type ProvinceCityMapResponseData = ApiResponseData<ProvinceMapData[]>
export type HospitalInfoResponseData = ApiResponseData<HospitalInfoData[]>
