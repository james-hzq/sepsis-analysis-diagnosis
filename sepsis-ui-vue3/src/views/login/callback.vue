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
const getOAuth2LoginResponseData = async (): Promise<OAuth2LoginCallbackData | null> => {
  const urlParams = new URLSearchParams(window.location.search);
  const loginType = urlParams.get(LoginConstants.LOGIN_TYPE) || "";
  const accessToken = urlParams.get(LoginConstants.ACCESS_TOKEN) || "";
  const refreshToken = urlParams.get(LoginConstants.REFRESH_TOKEN) || "";

  return loginType ? {loginType, accessToken, refreshToken} : null;
};

/**
 * @author hzq
 * @date 2024/11/8 10:40
 * @apiNote 处理三方登录的回调
 **/
const handleOAuth2LoginCallback = async () => {
  oauth2LoginResponseData.value = await getOAuth2LoginResponseData()

  if (oauth2LoginResponseData.value?.loginType) {
    useUserStore()
      .oauth2Login(oauth2LoginResponseData.value)
      .then(() => {
        router.replace({ path: "/" });
      }).catch(error => {
        console.log("OAuth2登录失败:", error);
        oauth2LoginResponseData.value = null;
    })
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
