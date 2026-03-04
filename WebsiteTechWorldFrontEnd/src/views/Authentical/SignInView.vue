<script setup>
import { ElMessage } from "element-plus";
import { onMounted, reactive, ref, watch } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useStore } from "vuex";
import { User, Lock, ArrowRight } from "@element-plus/icons-vue"; // Import thêm Icon cho đẹp
import { cartService } from '@/service/ClientService/GioHang/GioHangClientService';
import headerState from "@/components/Client/modules/headerState";

const tai_khoan = ref("");
const mat_khau = ref("");
const errors = reactive({});
const isLoading = ref(false);
const router = useRouter();
const route = useRoute();
const store = useStore();
const emit = defineEmits(["switchToRegister"]);

const count = ref(0)

if (!store.hasModule('headerState')) {
  store.registerModule('headerState', headerState)
}

const guiLenHeader = () => {
  store.commit('headerState/setCartItemCount', count.value)
}

const handleLogin = async () => {
  if (!tai_khoan.value.trim() || !mat_khau.value.trim()) {
    ElMessage.error("Vui lòng nhập tài khoản và mật khẩu");
    return;
  }
  try {
    isLoading.value = true;
    await store.dispatch("login", {
      taiKhoan: tai_khoan.value,
      matKhau: mat_khau.value,
    });

    let redirectPath = route.query.redirect || getDefaultRedirect();
    if (!redirectPath || redirectPath === "/") {
      redirectPath = getDefaultRedirect();
    }

    if (store.getters.isCustomer && !redirectPath.startsWith("/client")) {
      redirectPath = `/client${redirectPath}`;
    } else if (!store.getters.isCustomer) {
      redirectPath = getDefaultRedirect();
    }

    router.push(redirectPath);
    
    const user = JSON.parse(localStorage.getItem("user"));
    try {
      count.value = await cartService.cartCount(user.id);
    } catch (error) {
      console.error('Lỗi khi tải giỏ hàng:', error);
    }
    guiLenHeader()

    ElMessage.success("Đăng nhập thành công !");
  } catch (err) {
    if (Array.isArray(err)) {
      err.forEach(({ field, message }) => {
        if (field === "trang_thai") {
          ElMessage.error(message);
        } else {
          errors[field] = message;
        }
      });
    }
  } finally {
    isLoading.value = false;
  }
};

function getDefaultRedirect() {
  if (store.getters.isAdmin) return "/admin/ban-hang";
  if (store.getters.isStaff) return "/admin/ban-hang";
  if (store.getters.isCustomer) return "/client/home";
  return "/";
}

watch([tai_khoan, mat_khau], () => {
  delete errors.tai_khoan;
  delete errors.mat_khau;
  delete errors.server;
});

onMounted(() => {
  window.scrollTo({ top: 0, left: 0, behavior: "smooth" });
})
</script>

<template>
  <div class="auth-wrapper">
    <div class="auth-visual">
      <div class="visual-content">
        <div class="logo-area">
          <span class="logo-icon">💠</span> TechWorld
        </div>
        <h1 class="visual-heading">
          Khám phá thế giới <br />
          <span class="highlight-text">Công Nghệ Đỉnh Cao</span>
        </h1>
        <p class="visual-desc">
          Đăng nhập để trải nghiệm mua sắm thiết bị công nghệ với ưu đãi tốt nhất dành riêng cho bạn.
        </p>
        
        <div class="circle c1"></div>
        <div class="circle c2"></div>
      </div>
      <div class="visual-overlay"></div>
    </div>

    <div class="auth-form-container">
      <div class="form-wrapper">
        <div class="form-header">
          <h2>Chào mừng trở lại! 👋</h2>
          <p>Vui lòng nhập thông tin để đăng nhập</p>
        </div>

        <form @submit.prevent="handleLogin" class="login-form">
          <div class="input-group" :class="{ error: errors.tai_khoan }">
            <label>Tài khoản</label>
            <div class="input-wrapper">
              <el-icon class="input-icon"><User /></el-icon>
              <input
                v-model.trim="tai_khoan"
                type="text"
                placeholder="Nhập username của bạn"
                @input="tai_khoan = tai_khoan.replace(/\s/g, '')"
              />
            </div>
            <span v-if="errors.tai_khoan" class="error-msg">{{ errors.tai_khoan }}</span>
          </div>

          <div class="input-group" :class="{ error: errors.mat_khau }">
            <label>Mật khẩu</label>
            <div class="input-wrapper">
              <el-icon class="input-icon"><Lock /></el-icon>
              <input
                v-model.trim="mat_khau"
                type="password"
                placeholder="Nhập mật khẩu"
              />
            </div>
            <span v-if="errors.mat_khau" class="error-msg">{{ errors.mat_khau }}</span>
          </div>

          <div v-if="errors.server" class="server-error">
            {{ errors.server }}
          </div>

          <div class="form-actions">
            <router-link to="/forgot-password" class="forgot-link">Quên mật khẩu?</router-link>
          </div>

          <button type="submit" :disabled="isLoading" class="btn-submit">
            <span v-if="!isLoading">Đăng nhập ngay</span>
            <span v-else class="loader"></span>
            <el-icon v-if="!isLoading" class="btn-icon"><ArrowRight /></el-icon>
          </button>

          <div class="switch-auth">
            Chưa có tài khoản?
            <span @click="emit('switchToRegister')" class="register-link">Đăng ký ngay</span>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap');

.auth-wrapper {
  display: flex;
  min-height: 100vh;
  width: 100vw;
  background-color: #0f172a; /* Màu nền tối chủ đạo */
  font-family: 'Plus Jakarta Sans', sans-serif;
  overflow: hidden;
}

/* --- PHẦN TRÁI (VISUAL) --- */
.auth-visual {
  flex: 1.2;
  position: relative;
  background-image: url("https://images.unsplash.com/photo-1550745165-9bc0b252726f?q=80&w=2070&auto=format&fit=crop");
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
  top: 0; left: 0; width: 100%; height: 100%;
  background: linear-gradient(135deg, rgba(15, 23, 42, 0.9) 0%, rgba(30, 58, 138, 0.8) 100%);
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
  color: #38bdf8;
}

.visual-heading {
  font-size: 3.5rem;
  font-weight: 800;
  line-height: 1.2;
  margin-bottom: 1.5rem;
}

.highlight-text {
  background: linear-gradient(to right, #38bdf8, #818cf8);
  -webkit-text-fill-color: transparent;
}

.visual-desc {
  font-size: 1.1rem;
  color: #cbd5e1;
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
  width: 300px; height: 300px;
  background: rgba(56, 189, 248, 0.3);
  top: -50px; right: -50px;
  animation: float 6s infinite ease-in-out;
}
.c2 {
  width: 200px; height: 200px;
  background: rgba(129, 140, 248, 0.3);
  bottom: 50px; left: 50px;
  animation: float 8s infinite ease-in-out reverse;
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
}

.form-wrapper {
  width: 100%;
  max-width: 440px;
  animation: fadeSlideUp 0.8s ease-out;
}

.form-header {
  margin-bottom: 2.5rem;
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

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.2rem;
}

/* Input Styles */
.input-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.input-group label {
  color: #e2e8f0;
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
  font-size: 1rem;
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
  border-color: #38bdf8;
  box-shadow: 0 0 0 4px rgba(56, 189, 248, 0.1);
  background: #0f172a;
}

.input-wrapper input:focus + .input-icon {
  color: #38bdf8;
}

/* Error States */
.error input {
  border-color: #ef4444;
}
.error-msg {
  color: #ef4444;
  font-size: 0.85rem;
}
.server-error {
  background: rgba(239, 68, 68, 0.1);
  color: #ef4444;
  padding: 0.8rem;
  border-radius: 8px;
  font-size: 0.9rem;
  text-align: center;
}

/* Actions */
.form-actions {
  display: flex;
  justify-content: flex-end;
}

.forgot-link {
  color: #38bdf8;
  font-size: 0.9rem;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.2s;
}

.forgot-link:hover {
  color: #7dd3fc;
  text-decoration: underline;
}

/* Button Submit */
.btn-submit {
  background: linear-gradient(135deg, #0ea5e9 0%, #2563eb 100%);
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
  box-shadow: 0 10px 25px -5px rgba(37, 99, 235, 0.4);
}

.btn-submit:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

/* Switch Auth */
.switch-auth {
  text-align: center;
  color: #94a3b8;
  font-size: 0.95rem;
  margin-top: 1rem;
}

.register-link {
  color: #38bdf8;
  font-weight: 600;
  cursor: pointer;
  margin-left: 5px;
  transition: 0.2s;
}

.register-link:hover {
  color: #7dd3fc;
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

/* Keyframes */
@keyframes rotation {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@keyframes float {
  0%, 100% { transform: translate(0, 0); }
  50% { transform: translate(20px, -20px); }
}

@keyframes fadeSlideRight {
  from { opacity: 0; transform: translateX(-30px); }
  to { opacity: 1; transform: translateX(0); }
}

@keyframes fadeSlideUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
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
  }
  .auth-visual {
    display: none; /* Ẩn phần ảnh trên mobile để tập trung login */
  }
  .auth-form-container {
    padding: 1.5rem;
  }
}
</style>