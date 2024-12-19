<script setup lang="ts">
import {getCurrentInstance, onMounted, ref, watch} from "vue";
import * as echarts from 'echarts';
import {endChartApi} from "@/api/analysis/statistics";
import {DrawItemData} from "@/api/analysis/statistics/types";

// 父子传值组件
const props = defineProps({
  // 向后端请求数据的 URL
  url: {
    type: String,
    required: true
  },
  // 向后端请求数据的 参数
  analysisRequestData: {
    type: Object,
    required: true
  },
  // 图标主题
  text: {
    type: String,
    default: ''
  },
  // 图表偏转角度
  rotate: {
    type: Number,
    default: 0
  },
})

// 监听组件传值的变化, deep深度监听对象内部的变化, 即对象引用不变，但是对象内的字段值变化了
watch(() => props.analysisRequestData, () => {
  getEndChartData()
  }, {deep: true}
)

// 图表是否正在加载
const loading = ref<boolean>(false)
// 图表的ID
const barId = ref<string>('bar-end-chart-id')
const pieId = ref<string>('pie-end-chart-id')

/**
 * 向后端请求图表数据
 */
const getEndChartData = () => {
  loading.value = true
  endChartApi(props.url, props.analysisRequestData).then(res => {
    drawCharts(res.data)
  }).finally(() => {
    loading.value = false
  })
}

/**
 * 绘制图表
 */
const drawCharts = (data: DrawItemData<number>[]) => {
  const dataX = data.map(item => item.name)
  const dataY = data.map(item => item.value)
  // 基于刚刚准备好的 DOM 容器，初始化 EChart 实例
  const leftChart = echarts.init(document.getElementById(barId.value))
  leftChart.setOption({
    title: {
      text: props.text,
      textStyle: { fontSize: 12 }
    },
    tooltip: {},
    xAxis: {
      data: dataX,
      axisLabel: { rotate: props.rotate }
    },
    yAxis: {},
    series: [{
      name: '患者数',
      type: 'bar',
      label: { show: true, position: 'top' },
      data: dataY,
    },
    ]
  })
  const rightChart = echarts.init(document.getElementById(pieId.value))
  rightChart.setOption({
    title: { text: '' },
    tooltip: {},
    series: [{
      name: '患者数',
      type: 'pie',
      label: { show: true, formatter: '{b}：\n{d}%', width: 100 },
      data: data,
    }]
  })
}

onMounted(() =>{
  const uid = getCurrentInstance()?.uid || Math.random().toString(36).substring(2, 10)
  barId.value += uid
  pieId.value += uid
  getEndChartData()
})
</script>

<template>
  <div>
    <el-row>
      <el-col :span="12">
        <div class="heart-and-breath-chart-class" :id="barId" v-loading="loading" element-loading-text="加载中"/>
      </el-col>
      <el-col :span="12">
        <div class="heart-and-breath-chart-class" :id="pieId" v-loading="loading" element-loading-text="加载中"/>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped lang="scss">
.heart-and-breath-chart-class {
  width: auto;
  height: 400px
}
</style>
