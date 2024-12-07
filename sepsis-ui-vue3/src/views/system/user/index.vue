<script setup lang="ts">
import {computed, onMounted, reactive, ref} from "vue";
import { useUserStore } from "@/store/modules/user"
import {type UserTableData, type UserFormRequestData} from "@/api/system/user/types/user";
import {createUserApi, userTableApi} from "@/api/system/user";
import {FormInstance, type FormRules} from "element-plus";
import {List, User, Message, Key} from "@element-plus/icons-vue";
import Pagination from '@/components/Pagination/index.vue'

// 引入用户 Pinia Store
const userStore = useUserStore();
const loginUsername = computed(() => userStore.username);

// 用户角色选择器
const userRolesOptions = ['root', 'admin', 'user']

// 用户状态选择器
const userStatusOptions = [
  {value: '0', label: '正常'},
  {value: '1', label: '停用'}
]

// 用户表格查询表单
const userTableFormRef = ref<FormInstance | null>(null)
const total = ref(0)
const pageNum = ref(0)
const pageSize = ref(5)
const userTableFormData: UserFormRequestData = reactive({
  currUsername: loginUsername,
  userId: "",
  username: "",
  email: "",
  status: "",
  startTime: "",
  endTime: "",
  pageNum: pageNum.value,
  pageSize: pageSize.value
})

// 新增用户表单
const createDialogVisible = ref(false)
const createUserFormRef = ref<FormInstance | null>(null)
const createUserFormData: UserFormRequestData = reactive({
  currUsername: loginUsername,
  userId: null,
  username: "",
  password: "",
  roles: [],
  email: "",
  status: ""
})
const createUserFormRules: FormRules = {
  username: [
    { required: true, message: "请输入用户名", trigger: "blur" },
    { min: 1, max: 20, message: "长度在 1 到 20 个字符", trigger: "blur" }
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 5, max: 20, message: "长度在 5 到 20 个字符", trigger: "blur" }
  ],
  email: [
    { required: true, message: "请输入绑定邮箱", trigger: "blur" },
  ],
  roles: [
    { required: true, message: "请选择所属角色", trigger: "blur" },
  ],
  status: [
    { required: true, message: "请选择用户状态", trigger: "blur" },
  ]
}

// 用户表格数据
const userTableData = ref<UserTableData[]>([])

/**
 * 新增用户
 */
const createUser = () => {
  createDialogVisible.value = true
}

/**
 * 新增用户dialog取消
 */
const cancelCreateUser = () => {
  createDialogVisible.value = false
  resetCreateUserForm()
}

/**
 * 新增用户dialog确认
 */
const submitCreateUser = () => {
  createUserFormRef.value?.validate((valid: boolean) => {
    if (valid) {
      createUserApi(createUserFormData)
        .then(() => {
          getUserTable();
        })
        .finally(() => {
          createDialogVisible.value = false;
          resetCreateUserForm()
        })
    }
  })
}

/**
 * 新增用户表单重置
 */
const resetCreateUserForm = () => {
  Object.assign(createUserFormData, {
    currUsername: loginUsername,
    userId: "",
    username: "",
    password: "",
    roles: [],
    email: "",
    status: ""
  });
}

/**
 * 展示用户信息
 */
const getUserTable = () => {
  userTableApi(userTableFormData).then(res => {
    console.log(res.data)
    total.value = res.data.totalElements
    userTableData.value = res.data.content
  })
}

/**
 * 修改单个用户
 */
const handleEdit = () => {

}

/**
 * 删除单个用户
 */
const handleDelete = () => {

}

getUserTable();
</script>

<template>
  <div class="app-container">
    <div class="user-search-form">
      <el-form ref="userTableFormRef" :model="userTableFormData">
        <el-form-item prop="userId">
          <el-input v-model.trim="userTableFormData.userId" placeholder="查询用户编号" clearable tabindex="1"
                    :prefix-icon="List" size="large"/>
        </el-form-item>
        <el-form-item prop="username">
          <el-input v-model.trim="userTableFormData.username" placeholder="查询用户名称" clearable tabindex="2"
                    :prefix-icon="User" size="large"/>
        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model.trim="userTableFormData.email" placeholder="查询绑定邮箱" clearable tabindex="3"
                    :prefix-icon="Message" size="large"/>
        </el-form-item>
        <el-form-item prop="status">
          <el-select v-model="userTableFormData.status" placeholder="查询用户状态" clearable tabindex="4" size="large">
            <template #prefix>
              <el-icon>
                <Key/>
              </el-icon>
            </template>
            <el-option v-for="item in userStatusOptions" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item prop="startTime">
          <el-date-picker v-model="userTableFormData.startTime" type="daterange" range-separator="至"
                          start-placeholder="起始日期" end-placeholder="终止日期" size="large"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch()" size="large">搜索</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="user-edit-button">
      <el-button type="primary" icon="Plus" size="default" @click="createUser()">新增</el-button>
      <el-button type="danger" icon="Delete" size="default" @click="">批量删除</el-button>
    </div>

    <div class="user-table">
      <el-table :data="userTableData" border stripe>
        <el-table-column type="selection" width="40" align="center"/>
        <el-table-column label="编号" key="userId" prop="userId" width="60" align="center"/>
        <el-table-column label="用户名称" key="username" prop="username" width="160" align="center"/>
        <el-table-column label="绑定邮箱" key="email" prop="email" align="center"/>
        <el-table-column label="所属角色" key="roles" prop="roles" width="245" align="center">
          <template #default="{row}">
            <el-checkbox-group disabled class="table-checkbox" v-model="row.roles" size="default">
              <el-checkbox v-for="role in userRolesOptions" :key="role" :label="role" :value="role">
                {{ role }}
              </el-checkbox>
            </el-checkbox-group>
          </template>
        </el-table-column>
        <el-table-column label="用户状态" key="status" prop="status" width="120" align="center">
          <template #default="{row}">
            <el-button type="primary" plain disabled size="default">{{
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
              <el-button size="default" type="text" icon="Edit" @click="handleEdit()">修改</el-button>
              <el-button size="default" type="text" icon="Delete" @click="handleDelete()">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div>
      <Pagination :total="total" :page-size="pageSize" :page-num="pageNum"/>
    </div>

    <!-- 新增用户的弹窗 -->
    <div class="user-dialog">
      <el-dialog title="新增用户" v-model="createDialogVisible" width="25%" center >
        <el-form ref="createUserFormRef" :model="createUserFormData" :rules="createUserFormRules" label-width="80px">
          <el-form-item label="用户名称" prop="username">
            <el-input placeholder="请输入用户名称" v-model="createUserFormData.username" type="text" clearable/>
          </el-form-item>
          <el-form-item label="用户密码" prop="password">
            <el-input placeholder="请输入用户密码" v-model="createUserFormData.password" type="password" clearable/>
          </el-form-item>
          <el-form-item label="绑定邮箱" prop="email">
            <el-input placeholder="请输入绑定邮箱" v-model="createUserFormData.email" type="text" clearable/>
          </el-form-item>
          <el-form-item label="所属角色" prop="roles">
            <el-checkbox-group class="table-checkbox" v-model="createUserFormData.roles" size="default">
              <el-checkbox v-for="role in userRolesOptions" :key="role" :label="role" :value="role" :disabled="role === 'root'">
                {{ role }}
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="用户状态" prop="status">
            <el-radio v-model="createUserFormData.status" label="0">正常</el-radio>
            <el-radio v-model="createUserFormData.status" label="1">禁用</el-radio>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button type="warning" plain size="default" @click="cancelCreateUser">取消</el-button>
            <el-button type="primary" plain size="default" @click="submitCreateUser">确定</el-button>
          </div>
        </template>
      </el-dialog>
    </div>

  </div>
</template>

<style scoped lang="scss">
.user-search-form {
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

.user-edit-button {
  margin-bottom: 15px;
  .el-button {
    margin-right: 10px;
  }
}

.user-table {
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

.user-dialog {
  .el-form {
    text-align: center;
  }
}
</style>
