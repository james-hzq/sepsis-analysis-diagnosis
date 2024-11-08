<script lang="ts" setup>
import LoginConstants from '@/constants/login-constant';
import { onMounted, ref } from 'vue';
import {useRouter} from "vue-router";
import { useUserStore } from "@/store/modules/user"
import {OAuth2LoginCallbackData} from "@/api/login/types/login";


const router = useRouter();
const oauth2LoginResponseData = ref<OAuth2LoginCallbackData | null>(null);

/**
 * @author hzq
 * @date 2024/11/8 10:30
 * @apiNote 从三方登录的重定向回调 URL 中提取参数
 **/
const getOAuth2LoginResponseData = (): OAuth2LoginCallbackData | null => {
  const urlParams = new URLSearchParams(window.location.search);
  const loginType = urlParams.get(LoginConstants.LOGIN_TYPE) || "";
  const accessToken = urlParams.get(LoginConstants.ACCESS_TOKEN) || "";
  const refreshToken = urlParams.get(LoginConstants.REFRESH_TOKEN) || "";

  // 清除 URL 参数，防止后续页面加载重复提取
  window.history.replaceState(null, '', window.location.pathname);
  return loginType ? {loginType, accessToken, refreshToken} : null;
};

/**
 * @author hzq
 * @date 2024/11/8 10:40
 * @apiNote 处理三方登录的回调
 **/
const handleOAuth2LoginCallback = () => {
  oauth2LoginResponseData.value = getOAuth2LoginResponseData()

  if (oauth2LoginResponseData.value) {
    useUserStore()
      .oauth2Login(oauth2LoginResponseData.value)
      .then(() => {
        console.log("执行到这里")
        useUserStore()
          .getOAuth2LoginUserInfo()
          .then(() => {
            router.push({ path: '/' });
          }).catch(error => {
          console.log("OAuth2登录失败:", error);
          oauth2LoginResponseData.value = null;
        })
      });
  }
};

onMounted(() => {
  handleOAuth2LoginCallback();
});

</script>

<template>
  <div class="callback">
    <h2>正在处理 OAuth2 登录...</h2>
  </div>
</template>

<style scoped>
.callback {
  text-align: center;
  padding: 50px;
  font-size: 1.2em;
}
</style>
