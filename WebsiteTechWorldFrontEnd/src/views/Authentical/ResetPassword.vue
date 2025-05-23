<script setup>
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { resetPassword } from '@/Service/LoginService/ForgotPass';

const route = useRoute();
const router = useRouter();

const email = route.query.email;
const newPassword = ref('');
const confirmPassword = ref('');
const message = ref('');
const error = ref('');

const onSubmit = async () => {
  message.value = '';
  error.value = '';

  if (newPassword.value !== confirmPassword.value) {
    error.value = 'Mật khẩu không khớp.';
    return;
  }

  try {
    console.log(email, newPassword.value)
    const data = await resetPassword(email, newPassword.value);
    message.value = data;
    setTimeout(() => {
      router.push('/login');
    }, 1500);
  } catch (err) {
    error.value = err.message || 'Có lỗi xảy ra.';
  }
};
</script>

<template>
  <div>
    <h3>Đặt lại mật khẩu</h3>
    <form @submit.prevent="onSubmit">
      <input
        type="password"
        v-model="newPassword"
        placeholder="Mật khẩu mới"
        required
      />
      <input
        type="password"
        v-model="confirmPassword"
        placeholder="Xác nhận mật khẩu"
        required
      />
      <button type="submit">Xác nhận</button>
    </form>
    <p v-if="message" style="color:green">{{ message }}</p>
    <p v-if="error" style="color:red">{{ error }}</p>
  </div>
</template>

<style scoped>
div {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  font-family: Arial, sans-serif;
}

h3 {
  text-align: center;
  color: #333;
  margin-bottom: 20px;
  font-size: 1.5em;
}

form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

input[type="password"] {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 1em;
  outline: none;
  transition: border-color 0.3s ease;
}

input[type="password"]:focus {
  border-color: #007bff;
  box-shadow: 0 0 5px rgba(0, 123, 255, 0.3);
}

button {
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1em;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #0056b3;
}

button:active {
  background-color: #004085;
}

p {
  text-align: center;
  margin-top: 15px;
  font-size: 0.9em;
}
</style>
