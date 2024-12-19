<script setup lang="ts">
import {getCurrentInstance, onMounted, ref, watch} from "vue";
import {ageChartApi} from "@/api/analysis/statistics";
import {AgeChartData} from "@/api/analysis/statistics/types";
import * as echarts from 'echarts';

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
  getAgeChartData()
  }, {deep: true}
)

// 图表是否正在加载
const loading = ref<boolean>(false)
// 图表的ID
const barId = ref<string>('barChart')
const pieId = ref<string>('pieChart')

/**
 * 向后端请求图表数据
 */
const getAgeChartData = () => {
  loading.value = true
  ageChartApi(props.url, props.analysisRequestData).then(res => {
    drawCharts(res.data)
  }).finally(() => {
    loading.value = false
  })
}

/**
 * 绘制图表
 */
const drawCharts = (dataForDraw: AgeChartData) => {
  let ageDataArr = []
  for (let i = 0; i < 99; i++) {
    ageDataArr.push([i + 1, dataForDraw.ageData[i]])
  }
  const barChart = echarts.init(document.getElementById(barId.value))
  barChart.setOption({
    tooltip: {
      trigger: 'axis',
      position: function (pt) {
        return [pt[0], '10%'];
      }
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
      type: 'value',
      boundaryGap: false,
      interval: 10,
      min: 0,
      max: 100,
    },
    yAxis: {
      type: 'value',
      boundaryGap: [0, '100%'],
      interval: 400,
      min: 0,
      max: 2800
    },
    dataZoom: [
      {
        type: 'inside',
        start: 0,
        end: 100
      },
      {
        start: 0,
        end: 100
      }
    ],
    series: [
      {
        name: '数量',
        type: 'line',
        smooth: true,
        symbol: 'none',
        itemStyle: {
          color: '#3c4efb'
        },
        areaStyle: {},
        data: ageDataArr
      }
    ]
  })

  const pieChart = echarts.init(document.getElementById(pieId.value))
  pieChart.setOption({
    title: {text: ''},
    tooltip: {},
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [{
      name: '患者数',
      type: 'pie',
      label: {show: true, formatter: '{b}：\n{d}%', width: 100},
      data: dataForDraw.list,
    }]
  })
}

onMounted(() =>{
  const uid = getCurrentInstance()?.uid || Math.random().toString(36).substring(2, 10)
  barId.value += uid
  pieId.value += uid
  getAgeChartData()
})
</script>

<template>
  <div>
    <el-row>
      <el-col :span="12">
        <div class="age-chart-class" :id="barId" v-loading="loading" element-loading-text="加载中"></div>
      </el-col>
      <el-col :span="12">
        <div class="age-chart-class" :id="pieId" v-loading="loading" style="width:auto; height:400px" element-loading-text="加载中"></div>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped lang="scss">
.age-chart-class {
  width: auto;
  height: 400px
}
</style>
