<script setup lang="ts">
import { computed } from "vue";

// 当页面大小或页码发生变化时向父组件发出事件
const emits = defineEmits(["update:page", "update:size"]);

/**
 * 计算当前页码
 */
const currentPage = computed({
  get: () => props.pageNum,
  set: (val) => emits("update:page", val)
})

/**
 * 计算当前每页大小
 */
const pageSize = computed({
  get: () => props.pageSize,
  set: (val) => emits("update:size", val)
});

// 定义接收的 props
const props = defineProps({
  // 总条数
  total: {
    type: Number,
    required: true,
  },
  // 当前页码
  pageNum: {
    type: Number,
    default: 1,
  },
  // 当前页页码总数
  pageSize: {
    type: Number,
    default: 10,
  },
  // 页码大小选择
  pageSizes: {
    type: Array as () => number[],
    default: () => [5, 10, 20, 30, 50],
  },
  //设置最大页码按钮数。 页码按钮的数量，当总页数超过该值时会折叠
  pagerCount: {
    type: Number,
    default: 7,
  },
  // 布局
  layout: {
    type: String,
    default: 'total, sizes, prev, pager, next, jumper',
  },
  // 背景
  background: {
    type: Boolean,
    default: true
  },
  // 自动滚动
  autoScroll: {
    type: Boolean,
    default: true
  },
  // 大小
  size: {
    type: String,
    default: 'default'
  },
  // 是否隐藏
  hidden: {
    type: Boolean,
    default: false
  }
})
</script>

<template>
  <div :class="{'hidden':hidden}" class="pagination-container">
    <el-pagination
      :size="size"
      :background="background"
      :layout="layout"
      :page-sizes="pageSizes"
      :pager-count="pagerCount"
      :total="total"
      v-model:page-size="pageSize"
      v-model:current-page="currentPage"
    />
  </div>
</template>

<style scoped lang="scss">
.pagination-container {
  display: flex;
  justify-content: center;
  background: transparent;
  padding: 20px
}
.pagination-container.hidden {
  display: none;
}
</style>
