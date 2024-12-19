<script setup lang="ts">
import {getCurrentInstance, onMounted, ref, watch} from "vue";
import {heightAndWeightChartApi} from "@/api/analysis/statistics";
import * as echarts from 'echarts';
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
  getHeightAndWeightChartData()
  }, {deep: true}
)

// 图表是否正在加载
const loading = ref<boolean>(false)
// 图表的ID
const heightId = ref<string>('heightId')
const weightId = ref<string>('weightId')

/**
 * 向后端请求图表数据
 */
const getHeightAndWeightChartData = () => {
  loading.value = true
  heightAndWeightChartApi(props.url, props.analysisRequestData).then(res => {
    drawCharts(res.data)
  }).finally(() => {
    loading.value = false
  })
}

/**
 * 绘制图表
 */
const drawCharts = (data: DrawItemData<number>[]) => {
  if (data.length !== 12) return
  // 对数据进行处理，便于图表展示
  const weightX = data.slice(0, 4).map(item => item.name)
  const weightY = data.slice(0, 4).map(item => item.value)
  const heightX = data.slice(4, 8).map(item => item.name)
  const heightY = data.slice(4, 8).map(item => item.value)
  const manData = [data[8].value, data[10].value]
  const womanData = [data[9].value, data[11].value]

  const colors = ['#5470C6', '#EE6666']
  const leftChart = echarts.init(document.getElementById(heightId.value))
  leftChart.setOption({
    color: colors,
    tooltip: {
      trigger: 'none',
      axisPointer: {
        type: 'cross'
      }
    },
    title: {
      left: 'left',
      text: props.text,
      textStyle: {fontSize: 12}
    },
    legend: {},
    grid: {
      top: 70,
      bottom: 50
    },
    xAxis: [
      {
        type: 'category',
        axisTick: {
          alignWithLabel: true
        },
        axisLine: {
          onZero: false,
          lineStyle: {
            color: colors[1]
          }
        },
        axisPointer: {
          label: {
            formatter: function (params) {
              return (
                '体重(kg) ' + params.value + (params.seriesData.length ? '：' + params.seriesData[0].data : '') + ' 人'
              );
            }
          }
        },
        // 体重的标签
        data: weightX,
      },
      {
        type: 'category',
        axisTick: {
          alignWithLabel: true
        },
        axisLine: {
          onZero: false,
          lineStyle: {
            color: colors[0]
          }
        },
        axisPointer: {
          label: {
            formatter: function (params) {
              return (
                '身高(cm) ' + params.value + (params.seriesData.length ? '：' + params.seriesData[0].data : '') + ' 人'
              );
            }
          }
        },
        // prettier-ignore
        data: heightX
      }
    ],
    yAxis: [
      {
        type: 'value'
      }
    ],
    series: [
      {
        name: '身高(cm)',
        type: 'line',
        xAxisIndex: 1,
        smooth: true,
        emphasis: {
          focus: 'series'
        },
        data: heightY
      },
      {
        name: '体重(kg)',
        type: 'line',
        smooth: true,
        emphasis: {
          focus: 'series'
        },
        data: weightY
      }
    ]
  })
  const rightChart = echarts.init(document.getElementById(weightId.value))
  rightChart.setOption({
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {},
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      axisLabel: {
        fontWeight: 'bold'
      },
      data: ['体重平均值(kg)', '身高平均值(cm)']
    },
    yAxis: {
      type: 'value',
      boundaryGap: [0, 0.01]
    },
    series: [
      {
        name: '男性',
        type: 'bar',
        data: manData,
        label: {
          show: true, // 显示标签
          position: 'top', // 标签显示在柱子的顶部
          color: '#000', // 标签文字颜色
        },
      },
      {
        name: '女性',
        type: 'bar',
        data: womanData,
        label: {
          show: true, // 显示标签
          position: 'top', // 标签显示在柱子的顶部
          color: '#000', // 标签文字颜色
        },
      }
    ]
  })
}

onMounted(() =>{
  const uid = getCurrentInstance()?.uid || Math.random().toString(36).substring(2, 10)
  heightId.value += uid
  heightId.value += uid
  getHeightAndWeightChartData()
})
</script>

<template>
  <div>
    <el-row>
      <el-col :span="12">
        <div class="height-and-weight-chart-class" :id="heightId" v-loading="loading" element-loading-text="加载中"></div>
      </el-col>
      <el-col :span="12">
        <div class="height-and-weight-chart-class" :id="weightId" v-loading="loading" element-loading-text="加载中"></div>
      </el-col>
    </el-row>
  </div>
</template>


<style scoped lang="scss">
.height-and-weight-chart-class {
  width: auto;
  height: 400px
}
</style>
