<script setup lang="ts">
import {type AnalysisRequestData} from "/src/api/analysis/statistics/types";
import {reactive} from "vue";
import UrineChart from "@/components/Chart/UrineChart.vue";
import WhiteBloodCellChart from "@/components/Chart/WhiteBloodCellChart.vue";
import EndChart from "@/components/Chart/EndChart.vue";
import ScoreChart from "@/components/Chart/ScoreChart.vue";

const icuAnalysisQuery: AnalysisRequestData = reactive({
  startTime: '2110-01-01',
  endTime: '2212-01-01',
})

const dataScope = {
  oneScope: 4800,
  twoScope: 24000,
  threeScope: 18000,
  fourScope: 18000,
  oneInterval: 800,
  twoInterval: 4000,
  threeInterval: 3000,
  fourInterval: 3000,
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
        <UrineChart url="/sepsis/urine-chart" :analysis-request-data="icuAnalysisQuery" text="SEPSIS患者第一天尿量检测" :rotate="18"/>
      </el-card>
      <el-card class="icu-analysis-content">
        <WhiteBloodCellChart url="/sepsis/white-blood-cell-chart" :analysis-request-data="icuAnalysisQuery" text="SEPSIS患者第一天白细胞分类计数" :rotate="18"/>
      </el-card>
      <el-card class="icu-analysis-content">
        <EndChart url="/sepsis/end-chart" :analysis-request-data="icuAnalysisQuery" text="SEPSIS患者结局" :rotate="18"/>
      </el-card>
      <el-card class="icu-analysis-content">
        <ScoreChart url="/sepsis/score-chart" :analysis-request-data="icuAnalysisQuery" :dataScope=dataScope text="SEPSIS患者第一天 SOFA 和 GCS 评分" :rotate="18"/>
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
