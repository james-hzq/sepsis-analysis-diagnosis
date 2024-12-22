<script setup lang="ts">
import {computed, reactive, ref} from "vue";
import { useUserStore } from "@/store/modules/user"
import {type UserTableData, type UserFormRequestData} from "@/api/system/user/types/user";
import {createUserApi, deleteUserApi, updateUserApi, userTableApi} from "@/api/system/user";
import {FormInstance, type FormRules} from "element-plus";
import {List, User, Message, Key} from "@element-plus/icons-vue";
import Pagination from '@/components/Pagination/index.vue'
import messageUtils from '@/utils/modal'

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

// 用户表格数据
const userTableData = ref<UserTableData[]>([])

// 用户表格查询表单
const userTableFormRef = ref<FormInstance | null>(null)
const total = ref(0)
const query: PageRequestData = reactive({
  pageNum: 1,
  pageSize: 10,
  orderBy: null,
  direction: null
})
const userTableFormData: UserFormRequestData = reactive({
  currUsername: loginUsername,
  userId: "",
  username: "",
  email: "",
  status: "",
  startTime: "",
  endTime: "",
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

// 修改用户表单
const updateDialogVisible = ref(false)
const updateUserFormRef = ref<FormInstance | null>(null)
const updateUserFormData: UserFormRequestData = reactive({
  currUsername: loginUsername,
  userId: "",
  username: "",
  password: "",
  roles: [],
  email: "",
  status: ""
})
const updateUserFormRules: FormRules = {
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

/**
 * 新增用户
 */
const createUser = () => {
  createDialogVisible.value = true
}

/**
 * 批量删除用户
 */
const deleteUser = () => {
  messageUtils.msgWarning("暂未开启批量删除用户功能")
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
 * 修改用户dialog取消
 */
const cancelUpdateUser = () => {
  updateDialogVisible.value = false
  resetUpdateUserForm()
}

/**
 * 修改用户dialog确认
 */
const submitUpdateUser = () => {
  updateUserFormRef.value?.validate((valid: boolean) => {
    if (valid) {
      updateUserApi(updateUserFormData)
        .then(() => {
          getUserTable();
        })
        .finally(() => {
          updateDialogVisible.value = false;
          resetUpdateUserForm()
        })
    }
  })
}

/**
 * 修改用户表单重置
 */
const resetUpdateUserForm = () => {
  Object.assign(updateUserFormData, {
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
  // 更新查询参数
  userTableApi(query, userTableFormData).then(res => {
    total.value = res.data.totalElements
    userTableData.value = res.data.content
  })
}

/**
 * 更新页码
 */
const handlePageChange = (val) => {
  query.pageNum = val;
  getUserTable();
}

/**
 * 更新每页大小
 */
const handleSizeChange = (val) => {
  query.pageSize = val;
  query.pageNum = 1
  getUserTable();
}

/**
 * 搜索按钮
 */
const handleSearch = () => {
  query.pageNum = 1;
  getUserTable();
}

/**
 * 修改单个用户
 */
const handleEdit = (row) => {
  updateUserFormData.userId = row.userId
  updateUserFormData.username = row.username
  updateUserFormData.password = row.password || ''
  updateUserFormData.roles = row.roles
  updateUserFormData.email = row.email
  updateUserFormData.status = row.status
  updateDialogVisible.value = true
}

/**
 * 删除单个用户
 */
const handleDelete = (row) => {
  const userId = row.userId
  messageUtils.confirm('是否确认删除编号为 ' + userId + ' 的用户?').then(() => {
    deleteUserApi(userId).then(() => {
      query.pageNum = 1;
      getUserTable()
      messageUtils.msgSuccess("删除成功");
    })
  })
}

getUserTable();

</script>

<template>
  <div class="app-container">
    <div class="user-search-form">
      <el-form ref="userTableFormRef" :model="userTableFormData">
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
          <el-button type="primary" icon="Search" @click="handleSearch" size="large">搜索</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="user-edit-button">
      <el-button type="primary" icon="Plus" size="default" @click="createUser">新增</el-button>
      <el-button type="danger" icon="Delete" size="default" @click="deleteUser">批量删除</el-button>
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
      <Pagination
        :total="total"
        :page-num="query.pageNum"
        :page-size="query.pageSize"
        @update:page="handlePageChange"
        @update:size="handleSizeChange"
       />
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
            <el-radio v-model="createUserFormData.status" value="0">正常</el-radio>
            <el-radio v-model="createUserFormData.status" value="1">禁用</el-radio>
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

    <!-- 修改用户的弹窗 -->
    <div class="user-dialog">
      <el-dialog title="修改用户" v-model="updateDialogVisible" width="25%" center >
        <el-form ref="updateUserFormRef" :model="updateUserFormData" :rules="updateUserFormRules" label-width="80px">
          <el-form-item label="用户编号" prop="userId">
            <el-input disabled placeholder="请输入用户编号" v-model="updateUserFormData.userId" type="text" clearable/>
          </el-form-item>
          <el-form-item label="用户名称" prop="username">
            <el-input placeholder="请输入用户名称" v-model="updateUserFormData.username" type="text" clearable/>
          </el-form-item>
          <el-form-item label="用户密码" prop="password">
            <el-input placeholder="请输入用户密码" v-model="updateUserFormData.password" type="password" clearable/>
          </el-form-item>
          <el-form-item label="绑定邮箱" prop="email">
            <el-input placeholder="请输入绑定邮箱" v-model="updateUserFormData.email" type="text" clearable/>
          </el-form-item>
          <el-form-item label="所属角色" prop="roles">
            <el-checkbox-group class="table-checkbox" v-model="updateUserFormData.roles" size="default">
              <el-checkbox v-for="role in userRolesOptions" :key="role" :label="role" :value="role" :disabled="role === 'root'">
                {{ role }}
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="用户状态" prop="status">
            <el-radio v-model="updateUserFormData.status" label="0">正常</el-radio>
            <el-radio v-model="updateUserFormData.status" label="1">禁用</el-radio>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button type="warning" plain size="default" @click="cancelUpdateUser">取消</el-button>
            <el-button type="primary" plain size="default" @click="submitUpdateUser">确定</el-button>
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
