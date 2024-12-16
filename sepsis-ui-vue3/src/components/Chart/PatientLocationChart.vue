<script setup lang="ts">
import {onMounted, onUnmounted, ref, watch} from 'vue'
import * as echarts from 'echarts'
import {PatientLocationData} from "@/api/analysis/report/types";
import {patientLocationApi} from "@/api/analysis/report";

// 父子组件
const props = defineProps({
  // 可选：如果需要从父组件传入初始数据
  initialData: {
    type: Array as () => PatientLocationData[],
    default: () => []
  }
})

// 定义变量存储图表节点、患者进入地点数据和 ECharts 图表对象
const chartRef = ref<HTMLDivElement | null>(null)
const patientLocationData = ref<PatientLocationData[]>([])
const patientLocationChart = ref<echarts.ECharts | null>(null)

/**
 * 获取患者入院分组数据
 */
const fetchPatientLocationData = async () => {
  const res = await patientLocationApi()
  patientLocationData.value = res.data
  updateChartData()
}

/**
 * 更新图表数据
 */
const updateChartData = () => {
  // 如果图表节点不存在或患者数据为空，则直接返回
  if (!chartRef.value || !patientLocationData.value.length) return
  // 如果图表尚未初始化，则初始化 ECharts 图表
  if (!patientLocationChart.value) {
    patientLocationChart.value = echarts.init(chartRef.value)
  }
  // 从患者数据中获取 sepsisNum 和 patientNum 的数据列表
  const sepsisArray = patientLocationData.value.map(item => item.sepsisNum)
  const patientArray = patientLocationData.value.map(item => item.patientNum)
  // 定义 ECharts 选项配置
  const option: echarts.EChartsOption = {
    title: {
      text: 'ICU患者入院来源分析',
      textStyle: {
        fontSize: 15,
      }
    },
    tooltip: {
      trigger: 'item',
      axisPointer: {
        type: 'shadow'
      },
    },
    legend: {
      data: ['Sepsis患者数量', 'ICU患者数量'],
      textStyle: {
        fontSize: 12
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'value',
      boundaryGap: [0, 0.01],
    },
    yAxis: {
      type: 'category',
      data: [
        '门诊手术转移',
        '诊所转诊',
        '急诊室',
        '未知',
        '院内转入或转出',
        '术后监护室',
        '医生转诊',
        '术中转移',
        "医院转院",
        "护理机构转移",
        "预约或自我推荐"
      ]
    },
    series: [
      {
        name: 'Sepsis患者数量',
        type: 'bar',
        data: sepsisArray
      },
      {
        name: 'ICU患者数量',
        type: 'bar',
        data: patientArray
      }
    ]
  }

  patientLocationChart.value.setOption(option)
}

// 监听数据变化
watch(patientLocationData, updateChartData)

// 组件挂载时获取数据并初始化图表
onMounted(() => {
  // 如果传入了初始数据，使用初始数据；否则获取数据
  if (props.initialData.length) {
    patientLocationData.value = props.initialData
  } else {
    fetchPatientLocationData()
  }
})

// 组件卸载时清理
onUnmounted(() => {
  if (patientLocationChart.value) {
    patientLocationChart.value.dispose()
  }
})
</script>

<template>
  <div ref="chartRef" class="patient-location-chart"/>
</template>

<style scoped lang="scss">
.patient-location-chart {
  width: 100%;
  height: 650px;
}
</style>
