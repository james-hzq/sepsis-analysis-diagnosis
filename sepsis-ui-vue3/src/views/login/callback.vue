<template>
  <div class="callback">
    <h2>正在处理 OAuth2 登录...</h2>
  </div>
</template>

<script lang="ts">
import { onMounted, ref } from 'vue';
import {useRouter} from "vue-router";
import { useUserStore } from "@/store/modules/user"

const router = useRouter();
const accessToken = ref<string | null>(null);
const refreshToken = ref<string | null>(null);

/** 从 三方登录的重定向回调 URL 中提取参数*/
const getAccessTokenFromCallbackUrl = (): string | null => {
  const urlParams = new URLSearchParams(window.location.search);
  return urlParams.get('access_token');
};

/** 处理三方登录的回调 */
const handleCallback = async (): Promise<void> => {
  accessToken.value = getAccessTokenFromCallbackUrl();

  if (!accessToken.value || accessToken.value.trim() === "") {

  }
};

onMounted(() => {
  handleCallback();
});

</script>

<style scoped>
.callback {
  text-align: center;
  padding: 50px;
  font-size: 1.2em;
}
</style>
