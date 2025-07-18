<!-- src/components/RegisterForm.vue -->
<script setup>
import { reactive, ref } from "vue";
import { useStore } from "vuex";


const khachHangRegister = ref({})
const confirm_mat_khau = ref("");
const errors = reactive({});
const isLoading = ref(false);
const store = useStore();
const emit = defineEmits(["switchToLogin"]);

const handleRegister = async () => {
  //remove old errors
  Object.keys(errors).forEach(key => delete errors[key]);

  const { taiKhoan, matKhau, tenKhachHang, email} = khachHangRegister.value;

  // Kiểm tra input
  if (!taiKhoan?.trim()) errors.taiKhoan = "Vui lòng nhập tài khoản";
  if (!matKhau?.trim()) errors.matKhau = "Vui lòng nhập mật khẩu";
  if (!tenKhachHang?.trim()) errors.tenKhachHang = "Vui lòng nhập họ tên";
  if (!email?.trim()) errors.email = "Vui lòng nhập email";
  if (matKhau !== confirm_mat_khau.value) {
    errors.confirm_mat_khau = "Mật khẩu nhập lại không khớp";
  }
  
  if(Object.keys(errors).length > 0) return;

  try {
    isLoading.value = true;
    console.log("Đăng ký với dữ liệu:", khachHangRegister.value);
    await store.dispatch("register", khachHangRegister.value);
    emit("switchToLogin");
  } catch (err) {
    console.log("Error:", err);
    if (Array.isArray(err)) {
      err.forEach(({ field, message }) => {
        errors[field] = message; // Gán lỗi vào errors
      });
    } 
    else {
      errors.server = err.message || "Register failed"
    }
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
          <div v-if="errors.taiKhoan" class="text-danger mb-1">
            {{ errors.taiKhoan }}
          </div>
          <label>Tài khoản:</label>
          <input
            v-model.trim="khachHangRegister.taiKhoan"
            type="text"
            placeholder="Nhập tài khoản"
            
          />
        </div>
        <div>
          <div v-if="errors.tenKhachHang" class="text-danger mb-1">
            {{ errors.tenKhachHang }}
          </div>
          <label>Họ tên:</label>
          <input
            v-model="khachHangRegister.tenKhachHang"
            type="text"
            placeholder="Nhập họ tên"
          />
        </div>
        <div>
          <div v-if="errors.email" class="text-danger mb-1">
            {{ errors.email }}
          </div>
          <label>Email:</label>
          <input
            v-model="khachHangRegister.email"
            type="email"
            placeholder="Nhập email"
          />
        </div>
        <div>
          <div v-if="errors.matKhau" class="text-danger mb-1">
            {{ errors.matKhau }}
          </div>
          <label>Mật khẩu:</label>
          <input
            v-model="khachHangRegister.matKhau"
            type="password"
            placeholder="Nhập password"
          />
        </div>
        <div>
          <label>Nhập lại mật khẩu:</label>
          <input
            v-model="confirm_mat_khau"
            type="password"
            placeholder="Xác nhận mật khẩu"
          />
          <div v-if="errors.confirm_mat_khau" class="text-danger mb-1">
            {{ errors.confirm_mat_khau }}
          </div>
        </div>

        <button type="submit" :disabled="isLoading">
          {{ isLoading ? "Đang xử lý..." : "Đăng ký" }}
        </button>


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
/* CSS Variables */
:root {
  --primary-color: #1e90ff;
  --secondary-color: #6c757d;
  --text-color: #ffffff;
  --error-color: #ff4d4f;
  --border-radius: 20px;
  --shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
  --glass-bg: rgba(255, 255, 255, 0.15);
  --glass-border: 1px solid rgba(255, 255, 255, 0.2);
}

/* Main container */
.signup-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 91.4vh;
  background-image: url("src/components/images/loginBackground.jpg");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  padding: 2rem 1rem;
}

/* Register container with glassmorphism */
.signup-container {
  max-width: 420px;
  width: 100%;
  padding: 2.5rem;
  background: var(--glass-bg);
  backdrop-filter: blur(15px);
  border: var(--glass-border);
  border-radius: 25px;
  box-shadow: var(--shadow);
  text-align: center;
  animation: slideIn 0.6s ease-out;
}

/* Slide-in animation */
@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Animated title */
.signup-container h2 {
  background: linear-gradient(135deg, #1ed6ff 0%, #00bfff 50%, #87ceeb 100%);
  background-size: 200% 200%;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 2rem;
  font-size: 2rem;
  font-weight: 800;
  letter-spacing: 2px;
  text-transform: uppercase;
  animation: gradientShift 3s ease infinite;
  position: relative;
  text-shadow: 0 4px 15px rgba(30, 214, 255, 0.4);
  filter: drop-shadow(0 2px 8px rgba(30, 214, 255, 0.3));
}

/* Gradient animation */
@keyframes gradientShift {
  0%, 100% { 
    background-position: 0% 50%; 
    transform: scale(1);
  }
  50% { 
    background-position: 100% 50%;
    transform: scale(1.02);
  }
}

/* Underline effect for title */
.signup-container h2::after {
  content: '';
  position: absolute;
  bottom: -8px;
  left: 50%;
  transform: translateX(-50%);
  width: 80px;
  height: 3px;
  background: linear-gradient(90deg, transparent, #1ed6ff, #00bfff, #1ed6ff, transparent);
  border-radius: 3px;
  animation: underlineGlow 2s ease-in-out infinite alternate;
}

@keyframes underlineGlow {
  0% { 
    box-shadow: 0 0 5px rgba(30, 214, 255, 0.5);
    opacity: 0.8;
  }
  100% { 
    box-shadow: 0 0 15px rgba(30, 214, 255, 0.8);
    opacity: 1;
  }
}

/* Form styling */
form {
  display: flex;
  flex-direction: column;
  gap: 1.2rem;
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

/* Input styling */
input {
  width: 100%;
  padding: 0.9rem;
  background: rgba(255, 255, 255, 0.15);
  border: 2px solid rgba(255, 255, 255, 0.2);
  border-radius: 18px;
  font-size: 1rem;
  color: white;
  transition: all 0.3s ease;
}

input::placeholder {
  color: rgba(255, 255, 255, 0.7);
}

input:focus {
  outline: none;
  background: rgba(255, 255, 255, 0.25);
  border-color: var(--primary-color);
  box-shadow: 0 0 20px rgba(30, 144, 255, 0.4);
  transform: translateY(-2px);
}

/* Submit button */
button {
  background: linear-gradient(135deg, var(--primary-color), #04abed);
  color: var(--text-color);
  padding: 1rem;
  border: none;
  border-radius: 20px;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  box-shadow: 0 8px 20px rgba(30, 144, 255, 0.3);
  margin-top: 0.5rem;
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
  box-shadow: 0 12px 25px rgba(30, 144, 255, 0.4);
}

button:disabled {
  background: var(--secondary-color);
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

/* Error message */
p[style*="color: red"] {
  color: var(--error-color);
  font-size: 0.95rem;
  margin-top: 0.8rem;
  text-align: left;
  font-weight: 500;
  padding: 0.8rem 1rem;
  background: rgba(255, 77, 79, 0.15);
  border-radius: 15px;
  border: 1px solid rgba(255, 77, 79, 0.3);
}

/* Switch mode section */
.switch-mode {
  margin-top: 1.5rem;
  padding: 0.8rem;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  backdrop-filter: blur(10px);
  color: var(--text-color);
}

.switch-link {
  color: #00ff44;
  text-decoration: underline;
  cursor: pointer;
  font-weight: bold;
  transition: color 0.2s ease;
  padding: 0.2rem 0.4rem;
  border-radius: 6px;
}

.switch-link:hover {
  color: #1abc9c;
  text-decoration: underline;
  background: rgba(26, 188, 156, 0.1);
}

/* Responsive design */
@media (max-width: 480px) {
  .signup-container {
    max-width: 90%;
    margin: 1.5rem auto;
    padding: 2rem;
    border-radius: 20px;
  }

  .signup-container h2 {
    font-size: 1.6rem;
  }

  input,
  button {
    font-size: 0.95rem;
    padding: 0.8rem;
    border-radius: 15px;
  }

  label {
    font-size: 0.9rem;
  }
}
</style>
