<script setup lang="ts">
import {getCurrentInstance, onMounted, ref, watch} from "vue";
import {heartAndBreathChartApi} from "@/api/analysis/statistics";
import * as echarts from 'echarts';
import {HeartAndBreathChartData} from "@/api/analysis/statistics/types";

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
  getHeartAndBreathChartData()
  }, {deep: true}
)

// 图表是否正在加载
const loading = ref<boolean>(false)
// 图表的ID
const heartId = ref<string>('heartId')
const breathId = ref<string>('breathId')

/**
 * 向后端请求图表数据
 */
const getHeartAndBreathChartData = () => {
  loading.value = true
  heartAndBreathChartApi(props.url, props.analysisRequestData).then(res => {
    drawCharts(res.data)
  }).finally(() => {
    loading.value = false
  })
}

/**
 * 绘制图表
 */
const drawCharts = (data: HeartAndBreathChartData) => {
  const dataX = data.heartAndBreathList.map(item => item.name)
  const dataY = data.heartAndBreathList.map(item => item.value)
  const leftChart = echarts.init(document.getElementById(heartId.value))
  leftChart.setOption({
    title: {
      left: 'left',
      text: props.text,
      textStyle: {fontSize: 12}
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    xAxis: {
      type: 'category',
      data: dataX,
      axisLabel: { rotate: props.rotate }
    },
    yAxis: {
      type: 'value',
    },
    series: [
      {
        data: dataY,
        type: 'bar',
        showBackground: false,
        backgroundStyle: {
          color: 'rgba(180, 180, 180, 0.2)'
        },
        label: {
          show: true, // 显示标签
          position: 'top', // 标签显示在柱子的顶部
          color: '#000', // 标签文字颜色
        },
      }
    ]
  })
  const rightChart = echarts.init(document.getElementById(breathId.value))
  rightChart.setOption({
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: 'Access From',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {show: true, formatter: '{b}：\n{d}%', width: 100},
        emphasis: {
          label: {
            show: true,
            fontSize: 40,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: data.heartAvgList
      }
    ]
  })
}

onMounted(() =>{
  const uid = getCurrentInstance()?.uid || Math.random().toString(36).substring(2, 10)
  heartId.value += uid
  breathId.value += uid
  getHeartAndBreathChartData()
})
</script>

<template>
  <div>
    <el-row>
      <el-col :span="12">
        <div class="heart-and-breath-chart-class" :id="heartId" v-loading="loading" element-loading-text="加载中"/>
      </el-col>
      <el-col :span="12">
        <div class="heart-and-breath-chart-class" :id="breathId" v-loading="loading" element-loading-text="加载中"/>
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
