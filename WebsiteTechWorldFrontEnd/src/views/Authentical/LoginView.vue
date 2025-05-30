<!-- src/pages/AuthPage.vue -->
<script setup>
import { ref } from "vue";
import LoginForm from "@/views/Authentical/SignInView.vue";
import RegisterForm from "@/views/Authentical/SignUpView.vue";

const isLogin = ref(true);

const switchToLogin = () => (isLogin.value = true);
const switchToRegister = () => (isLogin.value = false);
</script>

<template>
  <div class="auth-page">
    <div class="auth-container">
      <LoginForm v-if="isLogin" @switchToRegister="switchToRegister" />
      <RegisterForm v-else @switchToLogin="switchToLogin" />
    </div>
  </div>
</template>

<style scoped>

</style>
