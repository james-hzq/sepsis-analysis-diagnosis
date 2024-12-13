<script setup lang="ts">
import {ref, onMounted, watch} from 'vue'
import axios from "axios";
import * as echarts from 'echarts'
import {type ProvinceCityMapData} from "@/api/index/types";

// 安徽省城市默认数据
const cityDataMap = ref<Record<string, ProvinceCityMapData[]>>({
  '合肥市': [
    {name: '蜀山区', value: 0},
    {name: '瑶海区', value: 0},
    {name: '长丰县', value: 0},
    {name: '包河区', value: 0},
    {name: '肥东县', value: 0},
    {name: '庐阳区', value: 0},
    {name: '庐江县', value: 0},
    {name: '肥西县', value: 0},
    {name: '巢湖市', value: 0}
  ],
  '安庆市': [
    {name: '桐城市', value: 0},
    {name: '岳西县', value: 0},
    {name: '潜山市', value: 0},
    {name: '太湖县', value: 0},
    {name: '怀宁县', value: 0},
    {name: '宜秀区', value: 0},
    {name: '望江县', value: 0},
    {name: '宿松县', value: 0},
    {name: '迎江区', value: 0},
    {name: '大观区', value: 0},
  ],
  '蚌埠市': [
    {name: '固镇县', value: 0},
    {name: '怀远县', value: 0},
    {name: '五河县', value: 0},
    {name: '淮上区', value: 0},
    {name: '禹会区', value: 0},
    {name: '蚌山区', value: 0},
    {name: '龙子湖区', value: 0},
  ],
  '亳州市': [
    {name: '谯城区', value: 0},
    {name: '涡阳县', value: 0},
    {name: '蒙城县', value: 0},
    {name: '利辛县', value: 0},
  ],
  '池州市': [
    {name: '东至县', value: 0},
    {name: '贵池区', value: 0},
    {name: '石台县', value: 0},
    {name: '青阳县', value: 0},
  ],
  '滁州市': [
    {name: '凤阳县', value: 0},
    {name: '明光市', value: 0},
    {name: '天长市', value: 0},
    {name: '定远县', value: 0},
    {name: '来安县', value: 0},
    {name: '全椒县', value: 0},
    {name: '南谯区', value: 0},
    {name: '琅琊区', value: 0}
  ],
  '阜阳市': [
    {name: '太和县', value: 0},
    {name: '界首市', value: 0},
    {name: '临泉县', value: 0},
    {name: '颍泉区', value: 0},
    {name: '颍州区', value: 0},
    {name: '颍东区', value: 0},
    {name: '颍上县', value: 0},
    {name: '阜南县', value: 0}
  ],
  '淮北市': [
    {name: '杜集区', value: 0},
    {name: '相山区', value: 0},
    {name: '徽州区', value: 0},
    {name: '烈山区', value: 0},
    {name: '濉溪县', value: 0},
  ],
  '淮南市': [
    {name: '凤台县', value: 0},
    {name: '潘集区', value: 0},
    {name: '八公山区', value: 0},
    {name: '大通区', value: 0},
    {name: '谢家集区', value: 0},
    {name: '田家庵区', value: 0},
    {name: '寿县', value: 0},
  ],
  '黄山市': [
    {name: '黄山区', value: 0},
    {name: '祁门县', value: 0},
    {name: '徽州区', value: 0},
    {name: '歙县', value: 0},
    {name: '屯溪区', value: 0},
    {name: '休宁县', value: 0},
    {name: '黟县', value: 0},
  ],
  '六安市': [
    {name: '霍邱县', value: 0},
    {name: '叶集区', value: 0},
    {name: '裕安区', value: 0},
    {name: '金安区', value: 0},
    {name: '金寨县', value: 0},
    {name: '舒城县', value: 0},
    {name: '霍山县', value: 0},
  ],
  '马鞍山市': [
    {name: '含山县', value: 0},
    {name: '和县', value: 0},
    {name: '花山区', value: 0},
    {name: '雨山区', value: 0},
    {name: '博望区', value: 0},
    {name: '当涂县', value: 0},
  ],
  '宿州市': [
    {name: '砀山县', value: 0},
    {name: '埇桥区', value: 0},
    {name: '灵璧县', value: 0},
    {name: '泗县', value: 0},
    {name: '萧县', value: 0},
  ],
  '铜陵市': [
    {name: '枞阳县', value: 0},
    {name: '郊区', value: 0},
    {name: '铜官区', value: 0},
    {name: '义安区', value: 0},
  ],
  '芜湖市': [
    {name: '无为市', value: 0},
    {name: '鸠江区', value: 0},
    {name: '镜湖区', value: 0},
    {name: '弋江区', value: 0},
    {name: '繁昌区', value: 0},
    {name: '南陵县', value: 0},
    {name: '湾沚区', value: 0},
  ],
  '宣城市': [
    {name: '宣州区', value: 0},
    {name: '泾县', value: 0},
    {name: '郎溪县', value: 0},
    {name: '绩溪县', value: 0},
    {name: '旌德县', value: 0},
    {name: '广德市', value: 0},
    {name: '宁国市', value: 0},
  ]
})

// 父子组件定义
const props = defineProps<{
  city: {
    type: string
    required: true
  },
  cityData: {
    type: ProvinceCityMapData[],
    required: true
  }
}>()


const emits = defineEmits(['cityMapUpdated'])

/**
 * 监听外部数据变化
 */
watch([() => props.city, () => props.cityData], async ([newCity, newData]) => {
  if (newCity && newData) {
    await setValueToCityMapData(newCity, newData)
    await showCityMap(newCity)
  }
}, { immediate: true })

/**
 * 将请求到的地图数据更新到 cityDataMap 中
 */
const setValueToCityMapData = async (city: string, res: ProvinceCityMapData[]) => {
  if (!city || !res) return
  const dataMap = new Map(res.map(item => [item.name, item.value]))
  cityDataMap.value[city].forEach(item => {
    if (dataMap.has(item.name)) {
      item.value = dataMap.get(item.name)
    }
  })
}

/**
 * 展示城市地图
 */
const showCityMap = async (icuCity: string) => {
  if (!icuCity) return
  const {data} = await axios.get(`json/${icuCity}.json`)
  const cityChart = echarts.init(document.querySelector('.an-hui-city-map') as HTMLDivElement)
  echarts.registerMap(icuCity, data)

  const currentCityData = cityDataMap.value[icuCity]
  const maxNum = icuCity === '合肥' ? 15 : 5

  cityChart.setOption({
    visualMap: {
      min: 0,
      max: maxNum,
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
        map: icuCity,
        label: {show: true},
        data: currentCityData,
        itemStyle: {
          normal: {
            shadowBlur: 10,
            shadowColor: 'rgba(0, 0, 0, 0.2)',
            shadowOffsetX: 10,
            shadowOffsetY: 10
          }
        },
      }
    ]
  })
}

onMounted(async () => {
  await setValueToCityMapData(props.cityData)
  await showCityMap()
  emits('cityMapUpdated', cityDataMap.value[props.city])
})
</script>

<template>
  <div class="an-hui-city-map"/>
</template>

<style scoped lang="scss">
.an-hui-city-map {
  background: #F5FFFA;
  width: 100%;
  height: 500px;
  border: 2px solid #eee;
  margin-left: 10px;
  margin-bottom: 10px;
}
</style>
