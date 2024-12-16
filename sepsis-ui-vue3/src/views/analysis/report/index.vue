<script setup lang="ts">
import {onMounted, reactive, ref} from "vue";
import {
  PatientLocationData,
  PatientReportData,
  PatientTableData,
} from "@/api/analysis/report/types";
import {patientLocationApi, patientReportApi, patientTableApi} from "@/api/analysis/report";
import PatientLocationChart from '@/components/Chart/PatientLocationChart.vue'
import Pagination from "@/components/Pagination/index.vue";

// 患者填报概况数据
const patientReportData = ref<PatientReportData>({
  patientNum: 0,
  sepsisNum: 0,
  sepsisPro: 0.00
})
// 患者填报概况进度条颜色
const patientReportProgressColor = [
  {color: '#f56c6c', percentage: 20},
  {color: '#e6a23c', percentage: 40},
  {color: '#5cb87a', percentage: 60},
  {color: '#1989fa', percentage: 80},
  {color: '#6f7ad3', percentage: 100},
]

/**
 * 获取患者概括
 */
const getPatientReportData = () => {
  patientReportApi().then(res => {
    patientReportData.value = res.data
  })
}

// 病人信息数据
const patientTableData = ref<PatientTableData[]>([])
const total = ref(0)
const patientTablePageQuery: PageRequestData = reactive({
  pageNum: 1,
  pageSize: 20,
  orderBy: 'id',
  direction: 'ASC'
})

/**
 * 获取患者表格数据
 */
const getPatientTableData = () => {
  patientTableApi(patientTablePageQuery).then(res => {
    total.value = res.data.totalElements
    patientTableData.value = res.data.content
  })
}

/**
 * 更新页码
 */
const handlePageChange = (val) => {
  patientTablePageQuery.pageNum = val
  getPatientTableData()
}

/**
 * 更新每页大小
 */
const handleSizeChange = (val) => {
  patientTablePageQuery.pageSize = val
  patientTablePageQuery.pageNum = 1
  getPatientTableData()
}

// 图表组件引用
const patientLocationChartRef = ref(null)

onMounted(() => {
  getPatientReportData()
  getPatientTableData()
})
</script>

<template>
  <div class="app-container">
    <div>
      <el-descriptions :column="2" size="default" border>
        <el-descriptions-item
          label="ICU患者总数"
          label-align="center"
          label-class-name="patient-report-item-label"
          class-name="patient-report-item-content"
          align="center"
        >
          <span class="patient-report-item-span">{{ patientReportData.patientNum }}</span>
        </el-descriptions-item>

        <el-descriptions-item
          label="Sepsis患者总数"
          label-align="center"
          label-class-name="patient-report-item-label"
          class-name="patient-report-item-content"
          align="center"
        >
          <span class="patient-report-item-span">{{ patientReportData.sepsisNum }}</span>
        </el-descriptions-item>

        <el-descriptions-item
          label="Sepsis患者占比"
          label-align="center"
          label-class-name="patient-report-item-label"
          class-name="patient-report-item-content"
          align="center"
        >
          <el-progress :color="patientReportProgressColor" :text-inside="true" :stroke-width="26"
                       :percentage=patientReportData.sepsisPro></el-progress>
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <div class="patient-report-statistics">
      <div class="patient-report-statistics-item">
        <el-table height="650" :data="patientTableData" stripe style="width: 100%">
          <el-table-column key="id" prop="id" label="编号" align="center" width="70px"/>
          <el-table-column label="患者编号" key="patientId" align="center" prop="patientId" />
          <el-table-column label="入ICU编号" key="icuId" align="center" prop="icuId" />
          <el-table-column label="入院时间" key="inTime" align="center" prop="inTime" />
          <el-table-column label="出院时间" key="outTime" align="center" prop="outTime" />
        </el-table>
        <Pagination
          :total="total"
          :page-num="patientTablePageQuery.pageNum"
          :page-size="patientTablePageQuery.pageSize"
          @update:page="handlePageChange"
          @update:size="handleSizeChange"
        />
      </div>

      <div class="patient-report-statistics-item">
        <PatientLocationChart ref="patientLocationChartRef"/>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
:deep(.patient-report-item-label) {
  background: var(--el-color-success-light-9) !important;
}

:deep(.patient-report-item-content) {
  background: var(--el-color-danger-light-9);
}

.patient-report-item-span {
  font-size: 18px
}

.patient-report-statistics {
  display: flex;
  margin-top: 15px;
  height: 700px;
  gap: 10px;
  width: 100%;
}

.patient-report-statistics-item {
  flex: 1;
  height: 650px;
  width: 50%;
}
</style>
