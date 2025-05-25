<!-- src/components/RegisterForm.vue -->
<script setup>
import { ref } from "vue";
import { useStore } from "vuex";


const khachHangRegister = ref({})
const confirm_mat_khau = ref("");
const error = ref("");
const isLoading = ref(false);
const store = useStore();
const emit = defineEmits(["switchToLogin"]);

const handleRegister = async () => {
  const { taiKhoan, matKhau, tenKhachHang, email} = khachHangRegister.value;
  if (
    !taiKhoan?.trim() ||
    !matKhau?.trim() ||
    !email?.trim()
  ) {
    error.value = "Vui lòng nhập đầy đủ thông tin";
    return;
  }

  if (matKhau !== confirm_mat_khau.value) {
    error.value = "Mật khẩu nhập lại không khớp";
    return;
  }

  try {
    isLoading.value = true;
    console.log("Đăng ký với dữ liệu:", khachHangRegister.value);
    await store.dispatch("register", khachHangRegister.value);
    emit("switchToLogin");
  } catch (err) {
    error.value = err.message || "Đăng ký thất bại";
  } finally {
    isLoading.value = false;
  }
};
</script>

<template>
  <div class="signup-page">
    <div class="signup-container">
      <h2>Đăng ký</h2>
      <form @submit.prevent="handleRegister">
        <div>
          <label>Tài khoản:</label>
          <input
            v-model="khachHangRegister.taiKhoan"
            type="text"
            placeholder="Nhập tài khoản"
            required
          />
        </div>
        <div>
          <label>Họ tên:</label>
          <input
            v-model="khachHangRegister.tenKhachHang"
            type="text"
            placeholder="Nhập họ tên"
            required
          />
        </div>
        <div>
          <label>Email:</label>
          <input
            v-model="khachHangRegister.email"
            type="email"
            placeholder="Nhập email"
            required
          />
        </div>
        <div>
          <label>Mật khẩu:</label>
          <input
            v-model="khachHangRegister.matKhau"
            type="password"
            placeholder="Nhập password"
            required
          />
        </div>
        <div>
          <label>Nhập lại mật khẩu:</label>
          <input
            v-model="confirm_mat_khau"
            type="password"
            placeholder="Xác nhận mật khẩu"
            required
          />
        </div>

        <button type="submit" :disabled="isLoading">
          {{ isLoading ? "Đang xử lý..." : "Đăng ký" }}
        </button>

        <p v-if="error" style="color: red">{{ error }}</p>

        <p class="switch-mode">
          Đã có tài khoản?
          <span @click="emit('switchToLogin')" class="switch-link"
            >Đăng nhập ngay</span
          >
        </p>
      </form>
    </div>
  </div>
</template>
<style scoped>
.signup-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("src/components/images/loginBackground.jpg");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

.signup-container {
  max-width: 400px;
  width: 100%;
  padding: 2.5rem;
  background: var(--glass-bg);
  backdrop-filter: blur(10px);
  border: var(--glass-border);
  border-radius: var(--border-radius);
  box-shadow: var(--shadow);
  text-align: center;
  animation: slideIn 0.6s ease-out;
}

.signup-container h2 {
  color: #1ed6ff;
  margin-bottom: 2rem;
  font-size: 2rem;
  font-weight: 700;
  letter-spacing: 1px;
  text-transform: uppercase;
}

form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

form div {
  text-align: left;
}

label {
  display: block;
  font-size: 1rem;
  color: #1ed6ff;
  margin-bottom: 0.5rem;
  font-weight: 500;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}

input {
  width: 100%;
  padding: 0.5rem 0.7rem;
  background: rgba(255, 255, 255, 0.15);
  border: var(--glass-border);
  border-radius: var(--border-radius);
  font-size: 0.9rem;
  color: var(--text-color);
  transition: all 0.3s ease;
}

input::placeholder {
  color: rgba(255, 255, 255, 0.7);
}

input:focus {
  outline: none;
  background: rgba(255, 255, 255, 0.25);
  border-color: var(--primary-color);
  box-shadow: 0 0 8px rgba(30, 144, 255, 0.4);
}

button {
  background: linear-gradient(135deg, var(--primary-color), #04abed);
  color: var(--text-color);
  padding: 1rem;
  border: none;
  border-radius: var(--border-radius);
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

button::before {
  content: "";
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.3),
    transparent
  );
  transition: 0.5s;
}

button:hover::before {
  left: 100%;
}

button:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(30, 144, 255, 0.3);
}

button:disabled {
  background: var(--secondary-color);
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

p[style*="color: red"] {
  color: var(--error-color);
  font-size: 0.95rem;
  margin-top: 0.8rem;
  text-align: left;
  font-weight: 500;
}

.switch-link {
  color: #3498db;
  text-decoration: underline;
  cursor: pointer;
  font-weight: bold;
  transition: color 0.2s ease;
}

.switch-link:hover {
  color: #1abc9c;
  text-decoration: underline;
}
</style>
