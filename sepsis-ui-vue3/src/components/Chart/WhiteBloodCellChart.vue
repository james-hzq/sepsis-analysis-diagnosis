<script setup lang="ts">
import {getCurrentInstance, onMounted, ref, watch} from "vue";
import * as echarts from 'echarts';
import {whiteBloodCellChartApi} from "@/api/analysis/statistics";
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
    getWhiteBloodCellChartData()
  }, {deep: true}
)

// 图表是否正在加载
const loading = ref<boolean>(false)
// 图表的ID
const barId = ref<string>('barId')
const pieId = ref<string>('pieId')

/**
 * 向后端请求图表数据
 */
const getWhiteBloodCellChartData = () => {
  loading.value = true
  whiteBloodCellChartApi(props.url, props.analysisRequestData).then(res => {
    drawCharts(res.data)
  }).finally(() => {
    loading.value = false
  })
}

/**
 * 绘制图表
 */
const drawCharts = (data: DrawItemData<number>[]) => {
  const dataX = data.map(item => item.name).slice(0, 5)
  const minDataY = data.map(item => item.value).slice(0, 5)
  const maxDataY = data.map(item => item.value).slice(5, 10)
  const probData = dataX.slice(0, 5).map((name, i) => ({name, value: minDataY[i] + maxDataY[i]}));
  const leftChart = echarts.init(document.getElementById(barId.value))
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
    legend: {
      left: 'right',
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    yAxis: {
      type: 'value',
      boundaryGap: [0, 0.01]
    },
    xAxis: {
      type: 'category',
      data: dataX,
    },
    series: [
      {
        name: '白细胞分类计数最小值均值(10^9/L)',
        type: 'bar',
        data: minDataY,
        label: {
          show: true, // 显示标签
          position: 'top', // 标签显示在柱子的顶部
          color: '#000', // 标签文字颜色
        },
      },
      {
        name: '白细胞分类计数最大值均值(10^9/L)',
        type: 'bar',
        data: maxDataY,
        label: {
          show: true, // 显示标签
          position: 'top', // 标签显示在柱子的顶部
          color: '#000', // 标签文字颜色
        },
      }
    ]
  })
  const rightChart = echarts.init(document.getElementById(pieId.value))
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
        name: '白细胞种类计数比例',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['50%', '50%'],
        itemStyle: {
          borderRadius: 8
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
        areaStyle: {},
        data: probData
      }
    ]
  })
}

onMounted(() => {
  const uid = getCurrentInstance()?.uid || Math.random().toString(36).substring(2, 10)
  barId.value += uid
  pieId.value += uid
  getWhiteBloodCellChartData()
})
</script>

<template>
  <div>
    <el-row>
      <el-col :span="12">
        <div class="white-blood-cell-chart-class" :id="barId" v-loading="loading" element-loading-text="加载中"/>
      </el-col>
      <el-col :span="12">
        <div class="white-blood-cell-chart-class" :id="pieId" v-loading="loading" element-loading-text="加载中"/>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped lang="scss">
.white-blood-cell-chart-class {
  width: auto;
  height: 400px
}
</style>
