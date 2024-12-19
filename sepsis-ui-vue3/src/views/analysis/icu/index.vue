<script setup lang="ts">
import {type AnalysisRequestData} from "/src/api/analysis/statistics/types";
import {reactive, ref} from "vue";
import AgeChart from '@/components/Chart/AgeChart.vue'
import HeightAndWeightChart from '@/components/Chart/HeightAndWeightChart.vue'
import HeartAndBreathChart from '@/components/Chart/HeartAndBreathChart.vue'
import EndChart from '@/components/Chart/EndChart.vue'
import ScoreChart from '@/components/Chart/ScoreChart.vue'

const icuAnalysisQuery: AnalysisRequestData = reactive({
  startTime: '2110-01-01',
  endTime: '2212-01-01',
})

const dataScope = {
  oneScope: 12000,
  twoScope: 60000,
  threeScope: 42000,
  fourScope: 42000,
  oneInterval: 2000,
  twoInterval: 10000,
  threeInterval: 7000,
  fourInterval: 7000,
}

</script>

<template>
  <div class="app-container">
    <div class="icu-analysis-query">
      <el-form :inline="true" class="demo-form-inline">
        <el-form-item label="开始日期">
          <el-date-picker v-model="icuAnalysisQuery.startTime" value-format="YYYY-MM-DD" type="date"/>
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker v-model="icuAnalysisQuery.endTime" value-format="YYYY-MM-DD" type="date"/>
        </el-form-item>
      </el-form>
    </div>

    <div>
      <el-card class="icu-analysis-content">
        <AgeChart url="/icu/age-chart" :analysis-request-data="icuAnalysisQuery" text="ICU患者年龄分布" :rotate="18"/>
      </el-card>
      <el-card class="icu-analysis-content">
        <HeightAndWeightChart url="/icu/height-weight-chart" :analysis-request-data="icuAnalysisQuery" text="ICU患者身高和体重分布" :rotate="18"/>
      </el-card>
      <el-card class="icu-analysis-content">
        <HeartAndBreathChart url="/icu/heart-breath-chart" :analysis-request-data="icuAnalysisQuery" text="ICU患者第一天心率和呼吸(右图为心率分布)" :rotate="18"/>
      </el-card>
      <el-card class="icu-analysis-content">
        <EndChart url="/icu/end-chart" :analysis-request-data="icuAnalysisQuery" text="ICU患者第一天结局" :rotate="18"/>
      </el-card>
      <el-card class="icu-analysis-content">
        <ScoreChart url="/icu/score-chart" :analysis-request-data="icuAnalysisQuery" :dataScope=dataScope text="ICU患者第一天 SOFA 和 GCS 评分" :rotate="18"/>
      </el-card>
    </div>
  </div>
</template>

<style scoped lang="scss">
.icu-analysis-query {
  margin-bottom: 20px
}

.icu-analysis-content {
  margin-bottom: 30px;
}
</style>
