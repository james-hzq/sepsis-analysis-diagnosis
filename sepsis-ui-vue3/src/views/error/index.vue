<template>
  <div class="error-container">
    <h1>Oops! Something went wrong.</h1>
    <p>An unexpected error occurred. Please try again later.</p>
    <p>{{ errorMessage }}</p>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';

export default defineComponent({
  name: 'ErrorPage',
  setup() {
    const errorMessage = ref('Internal Server Error');

    return {
      errorMessage,
    };
  },
});
</script>

<style scoped>
.error-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  font-family: Arial, sans-serif;
  text-align: center;
}
</style>
