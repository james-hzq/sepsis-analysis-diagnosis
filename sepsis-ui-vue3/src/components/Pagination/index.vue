<script setup lang="ts">
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
    required: true,
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
});

const handleSizeChange = () =>  {

}

const handleCurrentPageChange = () => {

}
</script>

<template>
  <div :class="{'hidden':hidden}" class="pagination-container">
    <el-pagination
      :size="size"
      :background="background"
      :layout="layout"
      :page-sizes="pageSizes"
      :total="total"
      :page-size="pageSize"
      :current-page="pageNum"
      @size-change="handleSizeChange()"
      @current-change="handleCurrentPageChange()"
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
