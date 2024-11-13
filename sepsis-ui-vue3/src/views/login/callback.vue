<script lang="ts" setup>
import LoginConstants from '@/constants/login-constant';
import {onMounted, ref} from 'vue';
import {useRouter} from "vue-router";
import {useUserStore} from "@/store/modules/user"
import {OAuth2LoginCallbackData} from "@/api/login/types/login";

const router = useRouter();

/**
 * @author hzq
 * @date 2024/11/8 10:40
 * @apiNote 处理三方登录的回调
 **/
const handleOAuth2LoginCallback = () => {

  const urlParams = new URLSearchParams(window.location.search);
  const loginType = urlParams.get(LoginConstants.LOGIN_TYPE) || "";
  const accessToken = urlParams.get(LoginConstants.ACCESS_TOKEN) || "";
  const refreshToken = urlParams.get(LoginConstants.REFRESH_TOKEN) || "";

  if (!loginType) {
    console.warn("Missing required OAuth parameters");
    return;
  }

  const oauth2LoginResponseData: OAuth2LoginCallbackData = {
    loginType,
    accessToken,
    refreshToken,
  };

  useUserStore()
    .oauth2Login(oauth2LoginResponseData)
    .then(() => {
      // // Then clear the URL params
      // window.history.replaceState(
      //   {},
      //   document.title,
      //   window.location.pathname
      // );
      router.push({path: '/'})
    }).catch(error => {
      router.replace({path: "/error"})
    });
};

handleOAuth2LoginCallback();

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
