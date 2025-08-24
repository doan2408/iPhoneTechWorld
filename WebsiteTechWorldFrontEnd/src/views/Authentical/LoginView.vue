<!-- src/pages/AuthPage.vue -->
<script setup>
import { ref } from "vue";
import LoginForm from "@/views/Authentical/SignInView.vue";
import RegisterForm from "@/views/Authentical/SignUpView.vue";
import VerifyRegister from "@/views/Authentical/VerifyRegister.vue";

const currentView = ref("login");

const switchToLogin = () => currentView.value = "login";
const switchToRegister = () => currentView.value = "register";
const switchToVerify = () => currentView.value = "verify";

</script>

<template>
  <div class="auth-page">
    <div class="auth-container">
      <LoginForm v-if="currentView === 'login'" @switchToRegister="switchToRegister" />
      <RegisterForm v-else-if="currentView === 'register'" 
                    @switchToLogin="switchToLogin" 
                    @switchToVerify="switchToVerify" />
      <VerifyRegister v-else />
    </div>
  </div>
</template>

<style scoped>

</style>
