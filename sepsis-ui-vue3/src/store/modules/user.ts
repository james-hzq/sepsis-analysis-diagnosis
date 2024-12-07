/** 定义了一个 Pinia store，用于处理用户的身份验证、授权、用户信息获取以及用户登出等功能。*/

import { ref } from "vue"
import store from "@/store"
import { defineStore } from "pinia"
import { useTagsViewStore } from "./tags-view"
import { useSettingsStore } from "./settings"
import { getToken, removeToken, setToken } from "@/utils/cache/cookies"
import { resetRouter } from "@/router"
import {systemLoginApi, loginUserInfoApi, logoutApi} from "@/api/login"
import {type SystemLoginRequestData, type OAuth2LoginCallbackData} from "@/api/login/types/login"
import routeSettings from "@/config/route"

/**
 * @return
 * @author hzq
 * @date 2024/11/7 17:21
 * @apiNote 定义一个名为 "user" 的 Pinia store，管理用户相关的状态
 **/
export const useUserStore = defineStore("user", () => {
  // 声明响应式变量，用于保存 token、用户角色、用户名等
  const token = ref<string>(getToken() || "")
  const roles = ref<string[]>([])
  const username = ref<string>("")

  // 获取 tags-view 和 settings store
  const tagsViewStore = useTagsViewStore()
  const settingsStore = useSettingsStore()

  /**
   * @author hzq
   * @date 2024/11/7 15:50
   * @apiNote 系统用户名密码登录
   **/
  const systemLogin = async (systemLoginRequestData: SystemLoginRequestData) => {
    const { data } = await systemLoginApi(systemLoginRequestData)
    setToken(data)
    token.value = data
  }

  /**
   * @author hzq
   * @date 2024/11/7 15:51
   * @apiNote OAuth2授权登录
   **/
  const oauth2Login = async (oAuth2LoginCallbackData : OAuth2LoginCallbackData)=> {
    setToken(oAuth2LoginCallbackData.accessToken)
    token.value = oAuth2LoginCallbackData.accessToken
  }

  /**
   * @author hzq
   * @date 2024/11/7 15:52
   * @apiNote 获取登录用户详情
   **/
  const getLoginUserInfo = async () => {
    const { data } = await loginUserInfoApi()
    username.value = data.username
    // 验证返回的 roles 是否为一个非空数组，否则塞入一个没有任何作用的默认角色，防止路由守卫逻辑进入无限循环
    roles.value = data.roles?.length > 0 ? data.roles : routeSettings.defaultRoles
  }

  /**
   * @author hzq
   * @date 2024/11/7 17:21
   * @apiNote 模拟角色变化
   **/
  const changeRoles = async (role: string) => {
    const newToken = "token-" + role
    token.value = newToken
    setToken(newToken)
    // 用刷新页面代替重新登录
    window.location.reload()
  }

  /**
   * @author hzq
   * @date 2024/11/7 15:55
   * @apiNote 登出
   **/
  const logout = async () => {
    const { data } = await logoutApi()
    removeToken()
    username.value = ""
    token.value = ""
    roles.value = []
    resetRouter()
    _resetTagsView()
  }

  /**
   * @author hzq
   * @date 2024/11/7 15:54
   * @apiNote 重置 token 和 roles
   **/
  const resetToken = () => {
    removeToken()
    token.value = ""
    roles.value = []
  }

  /**
   * @author hzq
   * @date 2024/11/7 15:54
   * @apiNote 重置 Visited Views 和 Cached Views，重置已访问的视图和缓存视图的方法
   **/
  const _resetTagsView = () => {
    if (!settingsStore.cacheTagsView) {
      tagsViewStore.delAllVisitedViews()
      tagsViewStore.delAllCachedViews()
    }
  }

  // 返回 store 中的状态和方法
  return { token, roles, username, systemLogin, oauth2Login, getLoginUserInfo, changeRoles, logout, resetToken }
})

/**
 * @author hzq
 * @date 2024/11/7 17:22
 * @apiNote 在 setup 外使用 useUserStore
 **/
export function useUserStoreHook() {
  return useUserStore(store)
}
