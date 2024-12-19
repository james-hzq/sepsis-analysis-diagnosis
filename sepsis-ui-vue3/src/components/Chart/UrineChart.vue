<script setup lang="ts">
import {getCurrentInstance, onMounted, ref, watch} from "vue";
import * as echarts from 'echarts';
import {urineChartApi} from "@/api/analysis/statistics";
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
    getUrineChartData()
  }, {deep: true}
)

// 图表是否正在加载
const loading = ref<boolean>(false)
// 图表的ID
const urineBarId = ref<string>('urineBarId')
const urinePieId = ref<string>('urinePieId')

/**
 * 向后端请求图表数据
 */
const getUrineChartData = () => {
  loading.value = true
  urineChartApi(props.url, props.analysisRequestData).then(res => {
    drawCharts(res.data)
  }).finally(() => {
    loading.value = false
  })
}

/**
 * 绘制图表
 */
const drawCharts = (data: DrawItemData<number>[][]) => {
  const leftDataX = data[0].map(item => item.name)
  const leftDataY = data[0].map(item => item.value)
  const leftChart = echarts.init(document.getElementById(urineBarId.value))
  leftChart.setOption({
    tooltip: {
      trigger: 'axis',
      position: function (pt) {
        return [pt[0], '10%'];
      }
    },
    legend: {
      left: 'center',
    },
    title: {
      left: 'left',
      text: props.text,
      textStyle: {fontSize: 12}
    },
    toolbox: {
      feature: {
        dataZoom: {
          yAxisIndex: 'none'
        },
        restore: {},
        saveAsImage: {}
      }
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: leftDataX
    },
    yAxis: {
      type: 'value',
      boundaryGap: [0, '100%'],
      min: 0,
      max: 120,
      interval: 30,
    },
    dataZoom: [
      {
        type: 'inside',
        start: 0,
        end: 10
      },
      {
        start: 0,
        end: 10
      }
    ],
    series: [
      {
        name: '尿输出量 (ml)：人次',
        type: 'line',
        symbol: 'none',
        sampling: 'lttb',
        itemStyle: {
          color: 'rgb(255, 70, 131)'
        },
        areaStyle: {

        },
        data: leftDataY
      }
    ]
  })
  const rightChart = echarts.init(document.getElementById(urinePieId.value))
  rightChart.setOption({
    tooltip: {
      trigger: 'item'
    },
    legend: {
      top: 'top'
    },
    toolbox: {
      show: true,
    },
    series: [
      {
        name: '尿量(ml)人次',
        label: {show: true, formatter: '{b}：\n{d}%', width: 100},
        type: 'pie',
        radius: [20, 150],
        center: ['50%', '50%'],
        roseType: 'area',
        itemStyle: {
          borderRadius: 8
        },
        data: data[1]
      }
    ]
  })
}

onMounted(() =>{
  const uid = getCurrentInstance()?.uid || Math.random().toString(36).substring(2, 10)
  urineBarId.value += uid
  urinePieId.value += uid
  getUrineChartData()
})
</script>

<template>
  <div>
    <el-row>
      <el-col :span="12">
        <div class="urine-chart-class" :id="urineBarId" v-loading="loading" element-loading-text="加载中"/>
      </el-col>
      <el-col :span="12">
        <div class="urine-chart-class" :id="urinePieId" v-loading="loading" element-loading-text="加载中"/>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped lang="scss">
.urine-chart-class {
  width: auto;
  height: 400px
}
</style>
