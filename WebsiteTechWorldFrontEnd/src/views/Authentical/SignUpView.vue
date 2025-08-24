<script setup>
import router from "@/router";
import { reactive, ref } from "vue";
import { useStore } from "vuex";

const khachHangRegister = ref({});
const confirm_mat_khau = ref("");
const errors = reactive({});
const isLoading = ref(false);
const store = useStore();
const emit = defineEmits(["switchToLogin"]);

const handleRegister = async () => {
  //remove old errors
  Object.keys(errors).forEach((key) => delete errors[key]);

  const { taiKhoan, matKhau, tenKhachHang, email } = khachHangRegister.value;

  // Kiểm tra input
  if (!taiKhoan?.trim()) errors.taiKhoan = "Vui lòng nhập tài khoản";
  if (!matKhau?.trim()) errors.matKhau = "Vui lòng nhập mật khẩu";
  if (!tenKhachHang?.trim()) errors.tenKhachHang = "Vui lòng nhập họ tên";
  if (!email?.trim()) errors.email = "Vui lòng nhập email";
  if (matKhau !== confirm_mat_khau.value) {
    errors.confirm_mat_khau = "Mật khẩu nhập lại không khớp";
  }

  if (Object.keys(errors).length > 0) return;

  try {
    isLoading.value = true;
    console.log("Đăng ký với dữ liệu:", khachHangRegister.value);
    await store.dispatch("registerVerify", khachHangRegister.value);
    setTimeout(() => {
      router.push({
        path: "/verify-register",
        query: { email: email }
      })
    }, 1000)
    emit('switchToVerify')
  } catch (err) {
    console.log("Error:", err);
    if (Array.isArray(err)) {
      err.forEach(({ field, message }) => {
        errors[field] = message; // Gán lỗi vào errors
      });
    } else {
      errors.server = err.message || "Register failed";
    }
  } finally {
    isLoading.value = false;
  }
};
</script>

<template>
  <div class="signup-page">
    <div class="signup-container">
      <div class="header-section">
        <h2>Đăng ký tài khoản</h2>
        <p class="subtitle">Tạo tài khoản để bắt đầu hành trình của bạn</p>
      </div>
      
      <form @submit.prevent="handleRegister" class="signup-form">
        <div class="form-row">
          <div class="form-group">
            <label>
              <i class="icon">👤</i>
              Tài khoản
            </label>
            <div class="input-wrapper">
              <input
                v-model.trim="khachHangRegister.taiKhoan"
                type="text"
                placeholder="Nhập tài khoản"
                :class="{ 'error': errors.taiKhoan }"
              />
              <div v-if="errors.taiKhoan" class="error-message">
                {{ errors.taiKhoan }}
              </div>
            </div>
          </div>
          
          <div class="form-group">
            <label>
              <i class="icon">📝</i>
              Họ tên
            </label>
            <div class="input-wrapper">
              <input
                v-model="khachHangRegister.tenKhachHang"
                type="text"
                placeholder="Nhập họ tên đầy đủ"
                :class="{ 'error': errors.tenKhachHang }"
              />
              <div v-if="errors.tenKhachHang" class="error-message">
                {{ errors.tenKhachHang }}
              </div>
            </div>
          </div>
        </div>

        <div class="form-group form-full-width">
          <label>
            <i class="icon">📧</i>
            Email
          </label>
          <div class="input-wrapper">
            <input
              v-model="khachHangRegister.email"
              type="email"
              placeholder="example@email.com"
              :class="{ 'error': errors.email }"
            />
            <div v-if="errors.emailExist" class="error-message">
              {{ errors.emailExist }}
            </div>
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label>
              <i class="icon">🔒</i>
              Mật khẩu
            </label>
            <div class="input-wrapper">
              <input
                v-model="khachHangRegister.matKhau"
                type="password"
                placeholder="Tối thiểu 8 ký tự"
                :class="{ 'error': errors.matKhau }"
              />
              <div v-if="errors.matKhau" class="error-message">
                {{ errors.matKhau }}
              </div>
            </div>
          </div>
          
          <div class="form-group">
            <label>
              <i class="icon">🔐</i>
              Xác nhận mật khẩu
            </label>
            <div class="input-wrapper">
              <input
                v-model="confirm_mat_khau"
                type="password"
                placeholder="Nhập lại mật khẩu"
                :class="{ 'error': errors.confirm_mat_khau }"
              />
              <div v-if="errors.confirm_mat_khau" class="error-message">
                {{ errors.confirm_mat_khau }}
              </div>
            </div>
          </div>
        </div>

        <button type="submit" :disabled="isLoading" class="submit-btn">
          <span v-if="isLoading" class="loading-spinner"></span>
          <i v-else class="icon">🚀</i>
          {{ isLoading ? "Đang xử lý..." : "Tạo tài khoản" }}
        </button>

        <div class="divider">
          <span>hoặc</span>
        </div>

        <div class="switch-mode">
          <p>Đã có tài khoản?</p>
          <button type="button" @click="emit('switchToLogin')" class="switch-link">
            Đăng nhập ngay
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
/* CSS Variables */
:root {
  --primary-gradient: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  --accent-gradient: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  --success-gradient: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  --glass-bg: rgba(255, 255, 255, 0.12);
  --glass-border: rgba(255, 255, 255, 0.18);
  --text-primary: #ffffff;
  --text-secondary: #ffffff;
  --error-color: #ff0000;
  --shadow-soft: 0 20px 60px rgba(0, 0, 0, 0.15);
  --shadow-glow: 0 0 40px rgba(102, 126, 234, 0.15);
}

/* Main container */
.signup-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-image: url("@/components/images/loginBackground.jpg");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  padding: 2rem;
  position: relative;
}

.signup-page::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(2px);
}

/* Enhanced signup container */
.signup-container {
  position: relative;
  z-index: 1;
  max-width: 650px;
  width: 100%;
  background: var(--glass-bg);
  backdrop-filter: blur(20px);
  border: 2px solid var(--glass-border);
  border-radius: 24px;
  padding: 3rem;
  box-shadow: var(--shadow-soft), var(--shadow-glow);
  animation: containerSlideIn 0.8s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

@keyframes containerSlideIn {
  from {
    opacity: 0;
    transform: translateY(40px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

/* Header section */
.header-section {
  text-align: center;
  margin-bottom: 2.5rem;
  position: relative;
}

.header-section h2 {
  font-size: 2.5rem;
  font-weight: 800;
  background: var(--primary-gradient);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 0.5rem;
  letter-spacing: -1px;
  animation: titlePulse 3s ease-in-out infinite;
}

@keyframes titlePulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.02); }
}

.subtitle {
  color: #ffffff;
  font-size: 1.1rem;
  margin: 0;
  opacity: 0;
  animation: subtitleFade 1s ease-out 0.3s forwards;
}

@keyframes subtitleFade {
  to { opacity: 1; }
}

/* Form layout */
.signup-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
}

/* Full width form group for email */
.form-full-width {
  width: 100%;
}

.input-wrapper {
  position: relative;
}

/* Enhanced labels with icons */
label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.95rem;
  font-weight: 600;
  color: #ffffff;
  margin-bottom: 0.8rem;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.icon {
  font-size: 1.2rem;
  opacity: 0.9;
}

/* Modern input styling */
input {
  width: 100%;
  padding: 1rem 1.2rem;
  background: rgba(255, 255, 255, 0.08);
  border: 2px solid rgba(255, 255, 255, 0.15);
  border-radius: 16px;
  font-size: 1rem;
  color: #ffffff;
  transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  backdrop-filter: blur(10px);
}

input::placeholder {
  color: rgba(255, 255, 255, 0.6);
  font-weight: 400;
}

input:focus {
  outline: none;
  background: rgba(255, 255, 255, 0.15);
  border-color: rgba(102, 126, 234, 0.8);
  box-shadow: 
    0 0 0 4px rgba(102, 126, 234, 0.1),
    0 8px 25px rgba(102, 126, 234, 0.15);
  transform: translateY(-2px);
}

input.error {
  border-color: var(--error-color);
  box-shadow: 0 0 0 4px rgba(255, 0, 0, 0.1);
}

/* Error messages */
.error-message {
  color: #ff0000;
  font-size: 0.85rem;
  font-weight: 500;
  margin-top: 0.5rem;
  padding: 0.5rem 0.8rem;
  background: rgba(255, 0, 0, 0.1);
  border-radius: 8px;
  border-left: 3px solid #ff0000;
  animation: errorSlide 0.3s ease-out;
}

@keyframes errorSlide {
  from {
    opacity: 0;
    transform: translateX(-10px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* Enhanced submit button */
.submit-btn {
  width: 100%;
  padding: 1.2rem;
  background: var(--primary-gradient);
  color: var(--text-primary);
  border: none;
  border-radius: 16px;
  font-size: 1.1rem;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  position: relative;
  overflow: hidden;
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
  margin-top: 1rem;
  text-transform: uppercase;
  letter-spacing: 1px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.submit-btn::before {
  content: "";
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.2),
    transparent
  );
  transition: 0.6s;
}

.submit-btn:hover::before {
  left: 100%;
}

.submit-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 15px 35px rgba(102, 126, 234, 0.4);
}

.submit-btn:disabled {
  background: rgba(255, 255, 255, 0.2);
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

/* Loading spinner */
.loading-spinner {
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top: 2px solid white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* Divider */
.divider {
  position: relative;
  text-align: center;
  margin: 1rem 0;
}

.divider::before {
  content: "";
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.3),
    transparent
  );
  z-index: 0;
}

.divider span {
  position: relative;
  background: rgba(255, 255, 255, 0.12);
  backdrop-filter: blur(20px);
  padding: 0.5rem 1.5rem;
  color: var(--text-secondary);
  font-size: 0.9rem;
  font-weight: 500;
  border-radius: 20px;
  z-index: 1;
}

/* Switch mode section */
.switch-mode {
  text-align: center;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  padding: 1.5rem;
}

.switch-mode p {
  color: #ffffff;
  margin: 0 0 1rem 0;
  font-size: 0.95rem;
}

.switch-link {
  background: var(--success-gradient);
  color: var(--text-primary);
  border: none;
  padding: 0.8rem 2rem;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  font-size: 0.9rem;
}

.switch-link:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(79, 172, 254, 0.3);
}

/* Responsive design */
@media (max-width: 768px) {
  .signup-container {
    margin: 1rem;
    padding: 2rem;
    max-width: none;
  }
  
  .form-row {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  
  .header-section h2 {
    font-size: 2rem;
  }
  
  .subtitle {
    font-size: 1rem;
  }
}

@media (max-width: 480px) {
  .signup-container {
    padding: 1.5rem;
  }
  
  .header-section h2 {
    font-size: 1.8rem;
  }
  
  input, .submit-btn {
    padding: 0.9rem;
    font-size: 0.95rem;
  }
}
</style>