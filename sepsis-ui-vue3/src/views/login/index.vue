<script lang="ts" setup>
import { reactive, ref } from "vue"
import { useRouter } from "vue-router"
import { useUserStore } from "@/store/modules/user"
import {ElMessage, type FormInstance, type FormRules} from "element-plus"
import { User, Lock } from "@element-plus/icons-vue"
import { type SystemLoginRequestData } from "@/api/login/types/login"
import Owl from "./components/Owl.vue"
import { useFocus } from "./hooks/useFocus"

const router = useRouter()
const { isFocus, handleBlur, handleFocus } = useFocus()

// 登录表单元素的引用
const loginFormRef = ref<FormInstance | null>(null)

// 系统登录按钮 Loading
const systemLoginLoading = ref(false)
const systemRegisterLoading = ref(false)

// 登录表单数据
const loginFormData: SystemLoginRequestData = reactive({
  username: "hzq",
  password: "318777541",
})

// 登录表单校验规则
const loginFormRules: FormRules = {
  username: [
    { required: true, message: "请输入用户名", trigger: "blur" },
    { min: 1, max: 20, message: "长度在 1 到 20 个字符", trigger: "blur" }
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 5, max: 20, message: "长度在 5 到 20 个字符", trigger: "blur" }
  ]
}

/**
 * @author hzq
 * @date 2024/11/28 14:28
 * @apiNote 系统用户登录逻辑
 **/
const handleSystemLogin = () => {
  loginFormRef.value?.validate((valid: boolean) => {
    if (valid) {
      systemLoginLoading.value = true
      useUserStore()
        .systemLogin(loginFormData)
        .then(() => {
          router.push({ path: "/" })
        })
        .catch(() => {
          loginFormData.password = ""
        })
        .finally(() => {
          systemLoginLoading.value = false
        })
    }
  })
}

/**
 * @author hzq
 * @date 2024/11/28 14:28
 * @apiNote 系统用户注册逻辑
 **/
const handleSystemRegister = () => {
  ElMessage.warning("注册通道暂时不对外开放，若注册账户，请联系管理员，在平台内部注册")
}

/**
 * @author hzq
 * @date 2024/11/28 14:28
 * @apiNote 第三方登录逻辑
 **/
const handleGithubLogin = (registrationId: String) => {
  window.location.href = `http://localhost:9100/oauth2/authorization/${registrationId}`
}

</script>

<template>
  <div class="login-container">
    <!-- 添加猫头鹰组件 -->
    <Owl :close-eyes="isFocus" />
    <div class="login-card">
      <!-- 标题 -->
      <div class="title">
        <img src="@/assets/login/login-title.png" alt="登录标题"/>
      </div>
      <!-- 登录 -->
      <div class="content">
        <el-form ref="loginFormRef" :model="loginFormData" :rules="loginFormRules">
          <el-form-item prop="username">
            <el-input v-model.trim="loginFormData.username" placeholder="用户名" type="text" tabindex="1" :prefix-icon="User" size="large"/>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model.trim="loginFormData.password" placeholder="密码" type="password" tabindex="2" :prefix-icon="Lock" size="large" show-password @blur="handleBlur" @focus="handleFocus"/>
          </el-form-item>
          <el-button :loading="systemLoginLoading" type="primary" size="large" @click.prevent="handleSystemLogin">登 录</el-button>
          <el-button :loading="systemRegisterLoading" type="primary" size="large" @click.prevent="handleSystemRegister">注 册</el-button>
          <el-divider>
            <span class="third-party-login-divider-span">其他登录方式</span>
          </el-divider>
          <div class="third-party-login">
            <img src="@/icons/svg/github.svg" alt="GitHub" class="third-party-login-icon" @click="handleGithubLogin('github')"/>
            <img src="@/icons/svg/google.svg" alt="Google" class="third-party-login-icon" @click="handleGithubLogin('google')"/>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
// 设置登录容器的样式
.login-container {
  // 使用弹性盒子布局
  display: flex;
  // 主轴方向为垂直方向
  flex-direction: column;
  // 在主轴上居中对齐
  justify-content: center;
  // 与主轴垂直的方向（水平方向）居中
  align-items: center;
  // 宽度100%
  width: 100%;
  // 最小高度100%
  min-height: 100%;
  // 设置背景图样式
  background: url('@/assets/login/login-background.png') no-repeat center center;
  // 背景图全覆盖
  background-size: cover;
  // 设置登录卡片的样式
  .login-card {
    // 设置宽度
    width: 480px;
    // 最大宽度为父容器宽度的90
    max-width: 90%;
    // 圆角半径为20px
    border-radius: 20px;
    // 添加阴影效果
    box-shadow: 0 0 10px #dcdfe6;
    // 背景颜色为变量定义的颜色
    background-color: var(--el-bg-color);
    // 内容溢出隐藏
    overflow: hidden;
    // 设置标题样式
    .title {
      // 使用弹性盒子布局
      display: flex;
      // 在主轴上居中对
      justify-content: center;
      // 在交叉轴上居中对齐
      align-items: center;
      // 高度为150p
      height: 150px;
      // 设置图片样式
      img {
        // 高度100%
        height: 100%;
      }
    }
    // 设置内容区域的样式
    .content {
      // 上、右、下、左 内边距
      padding: 20px 50px 50px 50px;
      // 选择所有深度为.el-input-group__append的元素
      :deep(.el-input-group__append) {
        // 内边距为0
        padding: 0;
        // 内容溢出隐藏
        overflow: hidden;
        // 设置.el-image的样式
        .el-image {
          // 设置宽度
          width: 100px;
          // 设置高度
          height: 40px;
          // 去掉左边框
          border-left: 0;
          // 禁止用户选择
          user-select: none;
          // 鼠标样式为指针
          cursor: pointer;
          // 文本居中
          text-align: center;
        }
      }
      // 设置按钮样式
      .el-button {
        // 宽度100%
        width: 100%;
        // 上、右、下、左 外边距
        margin: 10px 0 0 0;
        // 文本居中
        text-align: center;
      }
      .third-party-login-divider-span {
        color: #6c6e72;
      }
      // 设置第三方登录样式
      .third-party-login {
        // 使用 flex 布局
        display: flex;
        // 主轴方向为水平方向
        flex-direction: row;
        // 居中对齐
        justify-content: center;
        // 垂直居中
        align-items: center;
        // 图标之间的间距
        gap: 16px;
        // 文本对齐方式为居中对齐
        text-align: center;
        // 鼠标样式为指针
        cursor: pointer;
      }
      .third-party-login-icon {
        // 设置宽度
        width: 22px;
        // 设置高度
        height: 22px;
      }
      // 鼠标悬停时放大效果
      .third-party-login-icon:hover {
        transform: scale(1.3);
      }
    }
  }
}
</style>
