<script setup lang="ts">
import {cityMapApi, hospitalInfoApi, provinceMapApi} from "@/api/index";
import {onMounted, ref} from "vue";
import AnHuiProvinceMap from '@/components/Map/AnHuiProvinceMap.vue'
import AnHuiCityMap from '@/components/Map/AnHuiCityMap.vue'
import {type HospitalInfoData, type ProvinceCityMapData} from "@/api/index/types"

// 城市站点单选框
const radio = ref('合肥市')
// 当前展示城市
const currentCity = ref<string>('合肥市')
// 医院信息表格
const hospInfoTable = ref<HospitalInfoData[]>(null)
// 安徽省地图数据
const provinceMapData = ref<ProvinceCityMapData[]>(null)
// 安徽省地级市地图数据
const cityMapData = ref<ProvinceCityMapData[]>(null)

/**
 * 展示城市站点信息，展示城市地图
 */
const showCityDistribution = async (icuCity) => {
  const [hospInfoResData, cityResData] = await Promise.all([
    hospitalInfoApi(icuCity),
    cityMapApi(icuCity)
  ])
  currentCity.value = icuCity
  hospInfoTable.value = hospInfoResData.data
  cityMapData.value = cityResData.data
}

onMounted(async () => {
  // 获取省份地图数据
  const [provinceResData] = await Promise.all([
    provinceMapApi()
  ])
  provinceMapData.value = provinceResData.data
  // 展示城市数据
  await showCityDistribution(radio.value)
})
</script>

<template>
  <div class="app-container">
    <div class="flex-container">
      <AnHuiProvinceMap
        :province-data="provinceMapData"
        @map-data-loaded="(data) => provinceMapData = data"
      />
      <AnHuiCityMap
        :city="currentCity"
        :city-data="cityMapData"
        @city-map-updated="(data) => cityMapData = data"
      />
    </div>

    <div class="city-distribution">
      <span class="one">城市站点信息：</span>
      <el-radio-group v-model="radio" size="large">
        <el-radio-button label="合肥市" @click.native="showCityDistribution('合肥市')"/>
        <el-radio-button label="安庆市" @click.native="showCityDistribution('安庆市')"/>
        <el-radio-button label="蚌埠市" @click.native="showCityDistribution('蚌埠市')"/>
        <el-radio-button label="亳州市" @click.native="showCityDistribution('亳州市')"/>
        <el-radio-button label="池州市" @click.native="showCityDistribution('池州市')"/>
        <el-radio-button label="滁州市" @click.native="showCityDistribution('滁州市')"/>
        <el-radio-button label="阜阳市" @click.native="showCityDistribution('阜阳市')"/>
        <el-radio-button label="黄山市" @click.native="showCityDistribution('黄山市')"/>
        <el-radio-button label="淮北市" @click.native="showCityDistribution('淮北市')"/>
        <el-radio-button label="淮南市" @click.native="showCityDistribution('淮南市')"/>
        <el-radio-button label="六安市" @click.native="showCityDistribution('六安市')"/>
        <el-radio-button label="马鞍山市" @click.native="showCityDistribution('马鞍山市')"/>
        <el-radio-button label="宿州市" @click.native="showCityDistribution('宿州市')"/>
        <el-radio-button label="铜陵市" @click.native="showCityDistribution('铜陵市')"/>
        <el-radio-button label="芜湖市" @click.native="showCityDistribution('芜湖市')"/>
        <el-radio-button label="宣城市" @click.native="showCityDistribution('宣城市')"/>
      </el-radio-group>
    </div>

    <div>
      <el-table :data="hospInfoTable" height="250" stripe border fit highlight-current-row style="width: 100%">
        <el-table-column align="center" prop="id" label="编号" width="180"></el-table-column>
        <el-table-column align="center" prop="icuCity" label="城市" width="180"></el-table-column>
        <el-table-column align="center" prop="icuDist" label="地区" width="180"></el-table-column>
        <el-table-column align="center" prop="hospName" label="医院名称"></el-table-column>
        <el-table-column align="center" prop="icu" label="科室/部门" width="230"></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<style scoped lang="scss">

.flex-container {
  display: flex;
}

.city-distribution {
  margin-bottom: 10px;
}

.one {
  margin: 0;
  padding: 0;
  line-height: 1.5em;
  font-family: "Times New Roman", Times, serif;
  font-weight: bold;
  font-size: 17px;
  color: #FF0000;
}
</style>
