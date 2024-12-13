<script setup lang="ts">
import {ref, onMounted, watch} from 'vue'
import axios from "axios";
import * as echarts from 'echarts'
import {type ProvinceCityMapData} from "@/api/index/types";

// 安徽省地图默认数据
const provinceMapData = ref<ProvinceCityMapData[]>([
  {name: '安庆市', value: 0},
  {name: '蚌埠市', value: 0},
  {name: '亳州市', value: 0},
  {name: '池州市', value: 0},
  {name: '滁州市', value: 0},
  {name: '阜阳市', value: 0},
  {name: '合肥市', value: 0},
  {name: '淮北市', value: 0},
  {name: '淮南市', value: 0},
  {name: '黄山市', value: 0},
  {name: '六安市', value: 0},
  {name: '马鞍山市', value: 0},
  {name: '宿州市', value: 0},
  {name: '铜陵市', value: 0},
  {name: '芜湖市', value: 0},
  {name: '宣城市', value: 0},
])

// 父子组件定义
const props = defineProps<{
  provinceData: {
    type: ProvinceCityMapData[],
    required: true
  }
}>()

/**
 * 监听外部数据变化
 */
watch(() => props.provinceData, (newData: ProvinceCityMapData[]) => {
  if (newData) {
    const dataMap = new Map(newData.map(item => [item.name, item.value]))

    // 直接更新 provinceMapData 的值
    provinceMapData.value = provinceMapData.value.map(province => ({
      ...province,
      value: dataMap.get(province.name) ?? province.value
    }))

    // 触发地图重绘
    showProvinceMap()
  }
}, { immediate: true })

const emits = defineEmits(['mapDataLoaded'])

/**
 * 将请求到的地图数据更新到 provinceMapData 中
 */
const setValueToProvinceMapData = (res: ProvinceCityMapData[]) => {
  if (!res) return
  const dataMap = new Map(res.map(item => [item.name, item.value]))
  provinceMapData.value.forEach(province => {
    if (dataMap.has(province.name)) {
      province.value = dataMap.get(province.name)
    }
  })
}

/**
 * 展示省份地图
 */
const showProvinceMap = async () => {
  const {data} = await axios.get('json/anhui.json')
  const provinceChart = echarts.init(document.querySelector('.an-hui-province-map') as HTMLDivElement)
  echarts.registerMap('anhui', data)

  provinceChart.setOption({
    visualMap: {
      min: 0,
      max: 30,
      text: ['High', 'Low'],
      realtime: false,
      calculable: true,
      inRange: {
        color: ['#d4e8ff', '#a6d8ff', '#7ac8ff', '#4db9ff', '#2196f3', '#0b79d0', '#004a85']
      },
    },
    series: [
      {
        name: '安徽省医院地图分布',
        type: 'map',
        map: 'anhui',
        label: {show: true},
        itemStyle: {
          normal: {
            shadowBlur: 10,
            shadowColor: 'rgba(0, 0, 0, 0.2)',
            shadowOffsetX: 10,
            shadowOffsetY: 10,
          }
        },
        data: provinceMapData.value
      }
    ]
  })
}

onMounted(async () => {
  await setValueToProvinceMapData(props.provinceData)
  await showProvinceMap()
  emits('mapDataLoaded', provinceMapData.value)
})

</script>

<template>
  <div class="an-hui-province-map"/>
</template>

<style scoped lang="scss">
.an-hui-province-map {
  background: #F5FFFA;
  width: 100%;
  height: 500px;
  border: 2px solid #eee;
  margin-left: 10px;
  margin-bottom: 10px;
}
</style>
