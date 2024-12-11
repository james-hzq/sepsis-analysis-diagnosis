<script setup lang="ts">
import {computed, reactive, ref} from "vue";
import { useUserStore } from "@/store/modules/user"
import {type RoleFormRequestData, type RoleTableData} from "@/api/system/role/types/role";
import {FormInstance} from "element-plus";
import {Key, List, Message, User} from "@element-plus/icons-vue";
import {roleTableApi} from "@/api/system/role";
import messageUtils from '@/utils/modal'

// 引入用户 Pinia Store
const userStore = useUserStore();
const loginUsername = computed(() => userStore.username);

// 角色状态选择器
const roleStatusOptions = [
  {value: '0', label: '正常'},
  {value: '1', label: '停用'}
]

// 角色表格数据
const roleTableData = ref<RoleTableData[]>([])

// 角色表格查询表单
const roleTableFormRef = ref<FormInstance | null>(null)
const roleTableFormData: RoleFormRequestData = reactive({
  currUsername: loginUsername,
  roleId: "",
  roleKey: "",
  roleName: "",
  status: "",
  startTime: "",
  endTime: "",
})

/**
 * 新增角色
 */
const createRole = () => {
  messageUtils.msgWarning("暂未开启增加角色功能")
}

/**
 * 批量删除角色
 */
const deleteRole = () => {
  messageUtils.msgWarning("暂未开启批量删除角色功能")
}

/**
 * 展示角色信息
 */
const getRoleTable = () => {
  // 更新查询参数
  roleTableApi(roleTableFormData).then(res => {
    roleTableData.value = res.data
  })
}

/**
 * 搜索按钮
 */
const handleSearch = () => {
  getRoleTable();
}

/**
 * 修改单个角色
 */
const handleEdit = (row) => {
  messageUtils.msgWarning("暂未开启修改角色功能")
}

/**
 * 删除单个角色
 */
const handleDelete = (row) => {
  messageUtils.msgWarning("暂未开启删除角色功能")
}


getRoleTable()

</script>

<template>
  <div class="app-container">
    <div class="role-search-form">
      <el-form ref="roleTableFormRef" :model="roleTableFormData">
        <el-form-item prop="roleId">
          <el-input v-model.trim="roleTableFormData.roleId" placeholder="查询角色编号" clearable tabindex="1"
                    :prefix-icon="List" size="large"/>
        </el-form-item>
        <el-form-item prop="roleKey">
          <el-input v-model.trim="roleTableFormData.roleKey" placeholder="查询角色标识" clearable tabindex="2"
                    :prefix-icon="User" size="large"/>
        </el-form-item>
        <el-form-item prop="status">
          <el-select v-model="roleTableFormData.status" placeholder="查询角色状态" clearable tabindex="4" size="large">
            <template #prefix>
              <el-icon>
                <Key/>
              </el-icon>
            </template>
            <el-option v-for="item in roleStatusOptions" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item prop="startTime">
          <el-date-picker v-model="roleTableFormData.startTime" type="daterange" range-separator="至"
                          start-placeholder="起始日期" end-placeholder="终止日期" size="large"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch" size="large">搜索</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="role-edit-button">
      <el-button type="primary" icon="Plus" size="default" @click="createRole">新增</el-button>
      <el-button type="danger" icon="Delete" size="default" @click="deleteRole">批量删除</el-button>
    </div>


    <div class="role-table">
      <el-table :data="roleTableData" border stripe>
        <el-table-column type="selection" width="40" align="center"/>
        <el-table-column label="编号" key="roleId" prop="roleId" width="60" align="center"/>
        <el-table-column label="角色标识" key="roleKey" prop="roleKey" width="160" align="center"/>
        <el-table-column label="角色名称" key="roleName" prop="roleName" align="center"/>
        <el-table-column label="用户状态" key="status" prop="status" width="120" align="center">
          <template #default="{row}">
            <el-button type="warning" plain disabled size="default">{{
                row.status === '0' ? '正常' : '停用'
              }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" key="createTime" prop="createTime" width="160" align="center"/>
        <el-table-column label="创建人" key="createBy" prop="createBy" width="160" align="center"/>
        <el-table-column label="更新时间" key="updateTime" prop="updateTime" width="160" align="center"/>
        <el-table-column label="更新人" key="updateBy" prop="updateBy" width="160" align="center"/>
        <el-table-column label="操作" width="150" align="center">
          <template #default="{row}">
            <div v-if="row.userId != 1" class="table-operation">
              <el-button size="default" type="text" icon="Edit" @click="handleEdit(row)">修改</el-button>
              <el-button size="default" type="text" icon="Delete" @click="handleDelete(row)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<style scoped lang="scss">
.role-search-form {
  .el-form {
    display: flex;
    gap: 20px;
    flex-wrap: wrap;
    align-items: center;
  }

  .el-form-item {
    margin-bottom: 15px;
    flex-grow: 1;
    min-width: 240px;
  }

  .el-button {
    position: absolute;
    right: 0;
  }
}

.role-edit-button {
  margin-bottom: 15px;
  .el-button {
    margin-right: 10px;
  }
}

.role-table {
  width: 100%;
  margin-bottom: 20px;

  .table-operation {
    display: flex;
    justify-content: center;
    width: 100%;
  }
}

.table-checkbox {
  // 复选框的边框和背景区域
  :deep(.el-checkbox__inner) {
    border-color: #606266 !important;
  }

  // 修改复选框标签
  :deep(.el-checkbox__label) {
    color: #606266 !important;
  }

  // 选中状态的父容器
  :deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
    background-color: #409EFF;
    // 里面的对钩
    &::after {
      border-color: whitesmoke;
    }
  }

  // 聚焦状态
  :deep(.el-checkbox__input.is-focus .el-checkbox__inner) {
    border-color: #606266 !important;
  }
}

.role-dialog {
  .el-form {
    text-align: center;
  }
}
</style>
