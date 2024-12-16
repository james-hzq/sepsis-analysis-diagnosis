export interface PatientReportData {
  // ICU患者总数
  patientNum: number
  // ICU患者Sepsis数
  sepsisNum: number
  // sepsis 占比
  sepsisPro: number
}

export interface PatientTableRequestData {
  // 当前页面
  pageNum: number
  // 每页条数
  pageSize: number
  // 排序字段
  orderBy: string | null
  // 排序方式
  direction: 'ASC' | 'DESC' | null
}

export interface PatientTableData {
  // 编号
  id: string
  // 患者编号
  patientId: string
  // 入ICU编号
  icuId: string
  // 入院时间
  inTime: string
  // 出院时间
  outTime: string
}

export interface PatientLocationData {
  // 入院分组名称
  name: string
  // ICU患者数量
  patientNum: number
  // Sepsis患者数量
  sepsisNum: number
}

export type PatientReportResponseData = ApiResponseData<PatientReportData>
export type PatientTableResponseData = ApiResponseData<PatientTableData[]>
export type PatientLocationResponseData = ApiResponseData<PatientLocationData[]>
