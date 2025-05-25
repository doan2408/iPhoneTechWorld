<script setup>
import { ref } from 'vue';
import { sendForgotPasswordEmail } from '@/Service/LoginService/ForgotPass';
import { useRouter } from 'vue-router';


const router = useRouter();
const email = ref('');
const message = ref('');
const error = ref('');

const onSubmit = async () => {
  message.value = '';
  error.value = '';
  try {
    console.log(email.value)
    const data = await sendForgotPasswordEmail(email.value);
    message.value = data || 'Gửi mail thành công! Vui lòng kiểm tra email.';
    setTimeout(() => {
      router.push({
        path: "/verify-code",
        query: {email: email.value}
      })
    }, 1000)
  } catch (err) {
    console.log(err)
    error.value = err.message || 'Email không tồn tại';
  }
};
</script>

<template>
  <div>
    <h3>Quên mật khẩu</h3>
    <form @submit.prevent="onSubmit">
      <input v-model="email" type="email" placeholder="Nhập email" required />
      <button type="submit">Gửi</button>
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

input[type="email"] {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 1em;
  outline: none;
  transition: border-color 0.3s ease;
}

input[type="email"]:focus {
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

