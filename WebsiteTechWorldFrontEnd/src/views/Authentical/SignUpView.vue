<script setup>
import router from "@/router";
import { onMounted, reactive, ref, watch } from "vue";
import { useStore } from "vuex";
// Import Icon từ Element Plus để đồng bộ giao diện
import {
  User,
  Lock,
  Message,
  Postcard,
  ArrowRight,
} from "@element-plus/icons-vue";

const khachHangRegister = ref({});
const confirm_mat_khau = ref("");
const errors = reactive({});
const isLoading = ref(false);
const store = useStore();
const emit = defineEmits(["switchToLogin"]);

const handleRegister = async () => {
  // Reset lỗi cũ
  Object.keys(errors).forEach((key) => delete errors[key]);

  const { taiKhoan, matKhau, tenKhachHang, email } = khachHangRegister.value;

  // Validate inputs
  if (!taiKhoan?.trim()) errors.taiKhoan = "Vui lòng nhập tài khoản";
  if (!matKhau?.trim()) errors.matKhau = "Vui lòng nhập mật khẩu";
  if (!tenKhachHang?.trim()) errors.tenKhachHang = "Vui lòng nhập họ tên";
  if (!email?.trim()) errors.email = "Vui lòng nhập email";
  if (matKhau !== confirm_mat_khau.value) {
    errors.confirm_mat_khau = "Mật khẩu xác nhận không khớp";
  }

  if (Object.keys(errors).length > 0) return;

  try {
    isLoading.value = true;
    console.log("Đăng ký với dữ liệu:", khachHangRegister.value);
    await store.dispatch("registerVerify", khachHangRegister.value);

    // Giả lập delay một chút cho mượt hiệu ứng
    setTimeout(() => {
      router.push({
        path: "/verify-register",
        query: { email: email },
      });
      emit("switchToVerify");
    }, 800);
  } catch (err) {
    console.log("Error:", err);
    if (Array.isArray(err)) {
      err.forEach(({ field, message }) => {
        errors[field] = message;
      });
    } else {
      errors.server = err.message || "Đăng ký thất bại";
    }
  } finally {
    isLoading.value = false;
  }
};

// Clear lỗi khi người dùng nhập lại
watch(
  khachHangRegister,
  () => {
    Object.keys(errors).forEach((key) => delete errors[key]);
  },
  { deep: true },
);

onMounted(() => {
  window.scrollTo({ top: 0, left: 0, behavior: "smooth" });
});
</script>

<template>
  <div class="auth-wrapper">
    <div class="auth-visual">
      <div class="visual-content">
        <div class="logo-area"><span class="logo-icon">🚀</span> TechWorld</div>
        <h1 class="visual-heading">
          Bắt đầu <br />
          <span class="highlight-text">Hành Trình Mới</span>
        </h1>
        <p class="visual-desc">
          Tạo tài khoản ngay hôm nay để nhận hàng ngàn ưu đãi độc quyền dành cho
          thành viên mới.
        </p>

        <div class="circle c1"></div>
        <div class="circle c2"></div>
      </div>
      <div class="visual-overlay"></div>
    </div>

    <div class="auth-form-container">
      <div class="form-wrapper">
        <div class="form-header">
          <h2>Đăng ký tài khoản ✨</h2>
          <p>Điền thông tin bên dưới để tham gia cùng chúng tôi</p>
        </div>

        <form @submit.prevent="handleRegister" class="register-form">
          <div class="form-row">
            <div class="input-group" :class="{ error: errors.tenKhachHang }">
              <label>Họ và tên</label>
              <div class="input-wrapper">
                <el-icon class="input-icon"><Postcard /></el-icon>
                <input
                  v-model="khachHangRegister.tenKhachHang"
                  type="text"
                  placeholder="Nguyễn Văn A"
                />
              </div>
              <span v-if="errors.tenKhachHang" class="error-msg">{{
                errors.tenKhachHang
              }}</span>
            </div>

            <div class="input-group" :class="{ error: errors.taiKhoan }">
              <label>Tài khoản</label>
              <div class="input-wrapper">
                <el-icon class="input-icon"><User /></el-icon>
                <input
                  v-model.trim="khachHangRegister.taiKhoan"
                  type="text"
                  placeholder="username123"
                  @input="
                    khachHangRegister.taiKhoan =
                      khachHangRegister.taiKhoan.replace(/\s/g, '')
                  "
                />
              </div>
              <span v-if="errors.taiKhoan" class="error-msg">{{
                errors.taiKhoan
              }}</span>
            </div>
          </div>

          <div
            class="input-group"
            :class="{ error: errors.email || errors.emailExist }"
          >
            <label>Email</label>
            <div class="input-wrapper">
              <el-icon class="input-icon"><Message /></el-icon>
              <input
                v-model="khachHangRegister.email"
                type="email"
                placeholder="example@email.com"
              />
            </div>
            <span v-if="errors.email" class="error-msg">{{
              errors.email
            }}</span>
            <span v-if="errors.emailExist" class="error-msg">{{
              errors.emailExist
            }}</span>
          </div>

          <div class="form-row">
            <div class="input-group" :class="{ error: errors.matKhau }">
              <label>Mật khẩu</label>
              <div class="input-wrapper">
                <el-icon class="input-icon"><Lock /></el-icon>
                <input
                  v-model="khachHangRegister.matKhau"
                  type="password"
                  placeholder="••••••••"
                />
              </div>
              <span v-if="errors.matKhau" class="error-msg">{{
                errors.matKhau
              }}</span>
            </div>

            <div
              class="input-group"
              :class="{ error: errors.confirm_mat_khau }"
            >
              <label>Xác nhận</label>
              <div class="input-wrapper">
                <el-icon class="input-icon"><Lock /></el-icon>
                <input
                  v-model="confirm_mat_khau"
                  type="password"
                  placeholder="••••••••"
                />
              </div>
              <span v-if="errors.confirm_mat_khau" class="error-msg">{{
                errors.confirm_mat_khau
              }}</span>
            </div>
          </div>

          <div v-if="errors.server" class="server-error">
            {{ errors.server }}
          </div>

          <button type="submit" :disabled="isLoading" class="btn-submit">
            <span v-if="!isLoading">Tạo tài khoản mới</span>
            <span v-else class="loader"></span>
            <el-icon v-if="!isLoading" class="btn-icon"><ArrowRight /></el-icon>
          </button>

          <div class="switch-auth">
            <p>Bạn đã có tài khoản?</p>
            <button
              type="button"
              @click="emit('switchToLogin')"
              class="login-link"
            >
              Đăng nhập ngay
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap");

.auth-wrapper {
  display: flex;
  min-height: 100vh;
  width: 100vw;
  background-color: #0f172a;
  font-family: "Plus Jakarta Sans", sans-serif;
  overflow: hidden;
}

/* --- PHẦN TRÁI (VISUAL) --- */
.auth-visual {
  flex: 1.2;
  position: relative;
  /* Dùng ảnh khác với trang login để phân biệt */
  background-image: url("https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?q=80&w=1964&auto=format&fit=crop");
  background-size: cover;
  background-position: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 6rem;
  overflow: hidden;
}

.visual-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    135deg,
    rgba(15, 23, 42, 0.9) 0%,
    rgba(88, 28, 135, 0.8) 100%
  ); /* Tông tím hơn chút */
  z-index: 1;
}

.visual-content {
  position: relative;
  z-index: 2;
  color: white;
  animation: fadeSlideRight 1s ease-out;
}

.logo-area {
  font-size: 1.5rem;
  font-weight: 700;
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 3rem;
  color: #a78bfa;
}

.visual-heading {
  font-size: 3.5rem;
  font-weight: 800;
  line-height: 1.2;
  margin-bottom: 1.5rem;
}

.highlight-text {
  background: linear-gradient(to right, #a78bfa, #f472b6);
  -webkit-text-fill-color: transparent;
}

.visual-desc {
  font-size: 1.1rem;
  color: #e2e8f0;
  max-width: 500px;
  line-height: 1.6;
}

/* Hình tròn trang trí */
.circle {
  position: absolute;
  border-radius: 50%;
  z-index: 1;
  filter: blur(60px);
}
.c1 {
  width: 300px;
  height: 300px;
  background: rgba(167, 139, 250, 0.3);
  top: -50px;
  right: -50px;
  animation: float 7s infinite ease-in-out;
}
.c2 {
  width: 200px;
  height: 200px;
  background: rgba(244, 114, 182, 0.3);
  bottom: 50px;
  left: 50px;
  animation: float 9s infinite ease-in-out reverse;
}

/* --- PHẦN PHẢI (FORM) --- */
.auth-form-container {
  flex: 1;
  background: #0f172a;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  position: relative;
  overflow-y: auto; /* Cho phép cuộn nếu form dài */
}

.form-wrapper {
  width: 100%;
  max-width: 500px; /* Rộng hơn login xíu vì có 2 cột */
  animation: fadeSlideUp 0.8s ease-out;
}

.form-header {
  margin-bottom: 2rem;
  text-align: center;
}

.form-header h2 {
  color: #fff;
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
}

.form-header p {
  color: #94a3b8;
  font-size: 0.95rem;
}

.register-form {
  display: flex;
  flex-direction: column;
  gap: 1.2rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

/* Input Styles */
.input-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.input-group label {
  color: #cbd5e1;
  font-size: 0.9rem;
  font-weight: 500;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.input-wrapper input {
  width: 100%;
  background: #1e293b;
  border: 1px solid #334155;
  padding: 1rem 1rem 1rem 2.8rem;
  border-radius: 12px;
  color: #fff;
  font-size: 0.95rem;
  transition: all 0.3s ease;
}

.input-icon {
  position: absolute;
  left: 1rem;
  color: #94a3b8;
  font-size: 1.2rem;
  transition: all 0.3s ease;
}

.input-wrapper input:focus {
  outline: none;
  border-color: #a78bfa;
  box-shadow: 0 0 0 4px rgba(167, 139, 250, 0.1);
  background: #0f172a;
}

.input-wrapper input:focus + .input-icon {
  color: #a78bfa;
}

/* Error States */
.error input {
  border-color: #ef4444;
}
.error-msg {
  color: #ef4444;
  font-size: 0.8rem;
  margin-top: 2px;
}
.server-error {
  background: rgba(239, 68, 68, 0.1);
  color: #ef4444;
  padding: 0.8rem;
  border-radius: 8px;
  font-size: 0.9rem;
  text-align: center;
}

/* Button Submit */
.btn-submit {
  background: linear-gradient(
    135deg,
    #8b5cf6 0%,
    #d946ef 100%
  ); /* Gradient tím hồng khác login */
  color: white;
  padding: 1rem;
  border-radius: 12px;
  border: none;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  transition: all 0.3s ease;
  margin-top: 0.5rem;
}

.btn-submit:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 10px 25px -5px rgba(139, 92, 246, 0.4);
}

.btn-submit:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

/* Switch Auth */
.switch-auth {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #94a3b8;
  font-size: 0.95rem;
  margin-top: 0.5rem;
  background: rgba(255, 255, 255, 0.03);
  padding: 12px;
  border-radius: 12px;
}

.login-link {
  background: none;
  border: none;
  color: #a78bfa;
  font-weight: 700;
  cursor: pointer;
  font-size: 0.95rem;
  padding: 0;
  transition: 0.2s;
}

.login-link:hover {
  color: #c4b5fd;
  text-decoration: underline;
}

/* Loader */
.loader {
  width: 20px;
  height: 20px;
  border: 2px solid #fff;
  border-bottom-color: transparent;
  border-radius: 50%;
  animation: rotation 1s linear infinite;
}

@keyframes rotation {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

@keyframes float {
  0%,
  100% {
    transform: translate(0, 0);
  }
  50% {
    transform: translate(20px, -20px);
  }
}

@keyframes fadeSlideRight {
  from {
    opacity: 0;
    transform: translateX(-30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes fadeSlideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Responsive */
@media (max-width: 1024px) {
  .auth-visual {
    padding: 3rem;
  }
  .visual-heading {
    font-size: 2.5rem;
  }
}

@media (max-width: 768px) {
  .auth-wrapper {
    flex-direction: column;
    overflow-y: auto;
  }
  .auth-visual {
    display: none;
  }
  .auth-form-container {
    padding: 1.5rem;
    height: auto;
    min-height: 100vh;
  }
  .form-row {
    grid-template-columns: 1fr;
    gap: 1.2rem;
  } /* Mobile thì input thành 1 cột */
}
</style>
