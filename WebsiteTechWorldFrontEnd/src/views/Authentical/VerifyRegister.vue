<script setup>
import { onMounted, ref, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import LoginService from '@/Service/LoginService/SignUp'

const router = useRouter()
const route = useRoute()

const email = route.query.email || '' // Nhận email từ đường dẫn (?email=abc@gmail.com)
const code = ref('')
const loading = ref(false)
const error = ref('')
const success = ref(false)

const timeLeft = ref(300) // Timer starts at 300 seconds (5 phút)
const timerExpired = ref(false)
let timer = null

onMounted(() => {
  timer = setInterval(() => {
    if (timeLeft.value > 0) {
      timeLeft.value -= 1
    } else {
      timerExpired.value = true
      clearInterval(timer)
    }
  }, 1000) // Sửa từ 5000ms thành 1000ms (1 giây)
})

// Hàm format thời gian thành Mp SS (ví dụ: 5p30, 4p59)
const formatTime = (seconds) => {
  const minutes = Math.floor(seconds / 60)
  const remainingSeconds = seconds % 60
  return `${minutes} phút ${remainingSeconds.toString().padStart(2, '0')}`
}

const handleVerify = async () => {
  if (timerExpired.value) {
    error.value = 'Mã xác thực đã hết hạn. Vui lòng yêu cầu mã mới.'
    return
  }
  error.value = ''
  loading.value = true

  try {
    const response = await LoginService.completeRegistration(email, code.value)
    success.value = true

    setTimeout(() => {
      router.push({
        path: '/login',
      })
    }, 1000)
  } catch (err) {
    error.value = err.message || 'Mã không đúng hoặc đã hết hạn.'
  } finally {
    loading.value = false
  }
}

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
  }
})
</script>

<template>
  <div class="verify-code-container">
    <h2>Xác minh mã xác thực</h2>
    <p>
      Nhập mã gồm 8 chữ số đã gửi đến email: <b>{{ email }} <i class="bi bi-person-check-fill"></i></b>
    </p>
    <p class="timer" v-bind:class="{ expired: timerExpired }">
      {{
        timerExpired ? "Mã đã hết hạn" : `Thời gian còn lại: ${formatTime(timeLeft)}`
      }}
    </p>

    <form @submit.prevent="handleVerify">
      <input
        v-model.trim="code"
        type="text"
        maxlength="8"
        placeholder="Nhập mã xác thực"
        required
      />
      <button type="submit" :disabled="loading">
        {{ loading ? "Đang kiểm tra..." : "Xác minh" }}
      </button>
    </form>

    <p v-if="error" class="error">{{ error }}</p>
    <p v-if="success" class="success">✅ Mã hợp lệ! Đang chuyển hướng...</p>
  </div>
</template>

<style scoped>
.verify-code-container {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  font-family: Arial, sans-serif;
}

h2 {
  text-align: center;
  color: #333;
  margin-bottom: 15px;
  font-size: 1.5em;
}

p {
  text-align: center;
  margin: 10px 0;
  font-size: 0.9em;
}

.timer {
  color: #ff4d4f;
  font-weight: bold;
}

.timer.expired {
  color: #d90429;
  background-color: #ffe6e6; /* Nền đỏ nhạt để nhấn mạnh */
  padding: 5px;
  border-radius: 4px;
}

form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

input[type="text"] {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 1em;
  outline: none;
  transition: border-color 0.3s ease;
}

input[type="text"]:focus {
  border-color: #007bff;
  box-shadow: 0 0 5px rgba(0, 123, 255, 0.3);
}

input:disabled {
  background-color: #e0e0e0;
  cursor: not-allowed;
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

button:hover:not(:disabled) {
  background-color: #0056b3;
}

button:active:not(:disabled) {
  background-color: #004085;
}

button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.error {
  color: red;
  text-align: center;
  margin-top: 15px;
}

.success {
  color: green;
  text-align: center;
  margin-top: 15px;
}

/* Mobile Optimization */
@media (max-width: 600px) {
  .verify-code-container {
    margin: 20px;
    padding: 15px;
  }

  h2 {
    font-size: 1.3em;
  }

  input[type="text"],
  button {
    font-size: 0.9em;
  }

  .timer {
    font-size: 0.85em;
  }
}
</style>