<script setup lang="ts">
import {getCurrentInstance, onMounted, ref, watch} from "vue";
import * as echarts from 'echarts';
import {scoreChartApi} from "@/api/analysis/statistics";
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
  // 得分图表Y轴数据最大值
  dataScope: {
    type: Object,
    default: {}
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
  getScoreChartData()
  }, {deep: true}
)

// 图表是否正在加载
const loading = ref<boolean>(false)
// 图表的ID
const sofaId = ref<string>('sofaId')
const gcsId = ref<string>('gcsId')

/**
 * 向后端请求图表数据
 */
const getScoreChartData = () => {
  loading.value = true
  scoreChartApi(props.url, props.analysisRequestData).then(res => {
    drawCharts(res.data)
  }).finally(() => {
    loading.value = false
  })
}

/**
 * 绘制图表
 */
const drawCharts = (data: DrawItemData<number>[][]) => {
  const [arrOne, arrTwo, arrThree, arrFour] = data.map((arr) => arr.map(obj => [obj.name, obj.value]))
  let total = 0;
  arrFour.forEach(item => total += item[1])
  const gcsMotorProb = computeGcsProb(arrTwo, total)
  const gcsVerbalProb = computeGcsProb(arrThree, total)
  const gcsEyesProb = computeGcsProb(arrFour, total)
  const leftChart = echarts.init(document.getElementById(sofaId.value))
  leftChart.setOption({
    title: {
      left: 'left',
      text: props.text,
      textStyle: { fontSize: 12 }
    },
    grid: [
      { left: '7%', top: '7%', width: '38%', height: '38%' },
      { right: '7%', top: '7%', width: '38%', height: '38%' },
      { left: '7%', bottom: '7%', width: '38%', height: '38%' },
      { right: '7%', bottom: '7%', width: '38%', height: '38%' }
    ],
    tooltip: {
      formatter: '{a}: [{c}] [分,人次]', // 在{c}后添加单位字符串
    },
    legend: {
      left: 'right', // 将图例靠左对齐
    },
    xAxis: [
      { gridIndex: 0, min: 0, max: 25, interval: 5 },
      { type: 'category', gridIndex: 1 }, // 修改为类目轴
      { type: 'category', gridIndex: 2 }, // 修改为类目轴
      { type: 'category', gridIndex: 3 } // 修改为类目轴
    ],
    yAxis: [
      { gridIndex: 0, min: 0, max: props.dataScope.oneScope, interval: props.dataScope.oneInterval },
      { gridIndex: 1, min: 0, max: props.dataScope.twoScope, interval: props.dataScope.twoInterval },
      { gridIndex: 2, min: 0, max: props.dataScope.threeScope, interval: props.dataScope.threeInterval },
      { gridIndex: 3, min: 0, max: props.dataScope.fourScope, interval: props.dataScope.fourInterval }
    ],
    series: [
      {
        name: 'SOFA ',
        type: 'line',
        xAxisIndex: 0,
        yAxisIndex: 0,
        smooth: true,
        symbol: 'circle',
        data: arrOne,
      },
      {
        name: 'GCS_MOTOR',
        type: 'bar',
        xAxisIndex: 1,
        yAxisIndex: 1,
        data: arrTwo,
        label: {
          show: true, // 显示标签
          position: 'top', // 标签显示在柱子的顶部
          color: '#000', // 标签文字颜色
        },
      },
      {
        name: 'GCS_VERBAL',
        type: 'bar',
        xAxisIndex: 2,
        yAxisIndex: 2,
        data: arrThree,
        label: {
          show: true, // 显示标签
          position: 'top', // 标签显示在柱子的顶部
          color: '#000', // 标签文字颜色
        },
      },
      {
        name: 'GCS_EYES',
        type: 'bar',
        xAxisIndex: 3,
        yAxisIndex: 3,
        data: arrFour,
        label: {
          show: true, // 显示标签
          position: 'top', // 标签显示在柱子的顶部
          color: '#000', // 标签文字颜色
        },
      }
    ]
  })
  const rightChart = echarts.init(document.getElementById(gcsId.value))
  rightChart.setOption({
    legend: {
      left: 'center',
      data: ['GCS_MOTOR 运动反应', 'GCS_VERBAL 语言反应', 'GCS_EYES 眼睛反应']
    },
    radar: {
      indicator: [
        { name: '1 分', max: 100 },
        { name: '2 分', max: 100 },
        { name: '3 分', max: 100 },
        { name: '4 分', max: 100 },
        { name: '5 分', max: 100 },
        { name: '6 分', max: 100 }
      ],
    },
    series: [
      {
        name: 'GCS 格拉斯哥昏迷分级量表分数占比',
        type: 'radar',
        areaStyle: {},
        data: [
          {
            value: gcsMotorProb,
            name: 'GCS_MOTOR 运动反应'
          },
          {
            value: gcsVerbalProb,
            name: 'GCS_VERBAL 语言反应'
          },
          {
            value: gcsEyesProb,
            name: 'GCS_EYES 眼睛反应',
          }
        ],
      }
    ]
  })
}

const computeGcsProb = (sourceArr, total) => {
  let gcsProb = [0, 0, 0, 0, 0, 0]
  for (let i = sourceArr.length - 1;i >= 0;i--) {
    const percentage = (sourceArr[i][1] / total) * 100;
    const roundedPercentage = Number(percentage.toFixed(2));
    gcsProb[i] += roundedPercentage
  }
  return gcsProb
}

onMounted(() =>{
  const uid = getCurrentInstance()?.uid || Math.random().toString(36).substring(2, 10)
  sofaId.value += uid
  gcsId.value += uid
  getScoreChartData()
})
</script>

<template>
  <div>
    <el-row>
      <el-col :span="12">
        <div class="score-chart-class" :id="sofaId" v-loading="loading" element-loading-text="加载中"/>
      </el-col>
      <el-col :span="12">
        <div class="score-chart-class" :id="gcsId" v-loading="loading" element-loading-text="加载中"/>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped lang="scss">
.score-chart-class {
  width: auto;
  height: 400px
}
</style>
