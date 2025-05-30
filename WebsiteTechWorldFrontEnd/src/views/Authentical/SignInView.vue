<script setup>
import { ref, watch } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useStore } from "vuex";

const tai_khoan = ref("");
const mat_khau = ref("");
const confirm_mat_khau = ref("");
const error = ref("");
const isLoading = ref(false);
const router = useRouter();
const route = useRoute(); // Khai báo useRoute để lấy route.query
const store = useStore();
const emit = defineEmits(["switchToRegister"]);

const handleLogin = async () => {
  if (!tai_khoan.value.trim() || !mat_khau.value.trim()) {
    error.value = "Vui lòng nhập tài khoản và mật khẩu";
    return;
  }
  try {
    isLoading.value = true;
    await store.dispatch("login", {
      tai_khoan: tai_khoan.value,
      mat_khau: mat_khau.value,
    });

    // 👇 Lấy đường dẫn muốn quay về (nếu có)
    let redirectPath = route.query.redirect || getDefaultRedirect();

    // Nếu redirect rỗng hoặc là '/', thì dùng trang mặc định
    if (!redirectPath || redirectPath === "/") {
      redirectPath = getDefaultRedirect();
    }

    // Nếu người dùng là customer và redirectPath không bắt đầu với "/client", thêm "/client" vào trước
    if (store.getters.isCustomer && !redirectPath.startsWith("/client")) {
      redirectPath = `/client${redirectPath}`;
    } else if (!store.getters.isCustomer) {
      // Nếu không phải là customer, điều hướng về trang mặc định (getDefaultRedirect)
      redirectPath = getDefaultRedirect();
    }

    console.log("path: ", redirectPath);

    // Sau khi đăng nhập thành công, điều hướng tới trang trước khi đăng nhập (nếu có) hoặc trang mặc định
    router.push(redirectPath);
  } catch (err) {
    error.value = err.message || "Đăng nhập thất bại";
  } finally {
    isLoading.value = false;
  }
};

// Hàm phụ để xác định trang mặc định nếu không có redirect
function getDefaultRedirect() {
  if (store.getters.isAdmin) return "/admin/products";
  if (store.getters.isStaff) return "/staff/products";
  if (store.getters.isCustomer) return "/client/home";
  return "/"; // fallback
}


const isLogin = ref(true); // true: đăng nhập, false: đăng ký

watch([tai_khoan, mat_khau], () => {
  error.value = "";
});
</script>

<template>
  <div class="login-page">
    <div class="login-container">
      <h2>{{ isLogin ? "Đăng nhập" : "Đăng ký" }}</h2>

      <form @submit.prevent="handleLogin">
    <div>
      <label>Tài khoản:</label>
      <input v-model="tai_khoan" type="text" placeholder="Nhập tài khoản" required />
    </div>
    <div>
      <label>Mật khẩu:</label>
      <input v-model="mat_khau" type="password" placeholder="Nhập mật khẩu" required />
    </div>
    <div class="forgot-password-wrapper">
      <router-link to="/forgot-password" class="forgot-password-link">Quên mật khẩu?</router-link>
    </div>

    <button type="submit" :disabled="isLoading">
      {{ isLoading ? "Đang xử lý..." : "Đăng nhập" }}
    </button>

    <p v-if="error" style="color: red">{{ error }}</p>

    <p class="switch-mode">
      Chưa có tài khoản?
      <span @click="emit('switchToRegister')" class="switch-link">Tạo tài khoản</span>
    </p>
  </form>
    </div>
  </div>
</template>

<style scoped>
/* Tạo các biến cho màu sắc và kiểu dáng */
:root {
  --primary-color: #1e90ff; /* Màu chính (xanh da trời) */
  --secondary-color: #6c757d; /* Màu phụ */
  --text-color: #ffffff; /* Màu chữ trắng */
  --error-color: #ff4d4f; /* Màu lỗi (đỏ) */
  --border-radius: 20px; /* Tăng độ cong cho mềm mại */
  --shadow: 0 15px 35px rgba(0, 0, 0, 0.2); /* Đổ bóng mềm mại */
  --glass-bg: rgba(255, 255, 255, 0.15); /* Nền mờ cho login */
  --glass-border: 1px solid rgba(255, 255, 255, 0.2); /* Biên mờ */
}

/* Toàn bộ trang login */
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 91.4vh;
  background-image: url("src/components/images/loginBackground.jpg"); /* Chèn ảnh nền ở đây */
  background-size: cover; /* Phủ ảnh toàn bộ */
  background-position: center; /* Canh giữa ảnh */
  background-repeat: no-repeat; /* Không lặp lại ảnh */
}

/* Tiêu đề login - làm đẹp hơn */
.login-container h2 {
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

/* Animation gradient cho chữ */
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

/* Gạch chân đẹp cho tiêu đề */
.login-container h2::after {
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

/* Cấu trúc chính của login container */
.login-container {
  max-width: 400px; /* Giữ nguyên kích thước */
  width: 100%;
  padding: 2.5rem; /* Giữ nguyên padding */
  background: var(--glass-bg); /* Nền mờ */
  backdrop-filter: blur(15px); /* Tăng blur cho mềm mại hơn */
  border: var(--glass-border);
  border-radius: 25px; /* Tăng border-radius cho tròn trịa */
  box-shadow: var(--shadow); /* Đổ bóng cho container */
  text-align: center;
  animation: slideIn 0.6s ease-out; /* Hiệu ứng slide-in khi vào trang */
}

/* Hiệu ứng slide-in khi hiển thị */
@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(30px); /* Lúc đầu bị đẩy xuống */
  }
  to {
    opacity: 1;
    transform: translateY(0); /* Cuối cùng trở về vị trí ban đầu */
  }
}

/* Tiêu đề login */
.login-container h2 {
  color: #1ed6ff;
  margin-bottom: 2rem; /* Giữ nguyên */
  font-size: 2rem; /* Giữ nguyên */
  font-weight: 700;
  letter-spacing: 1px;
  text-transform: uppercase; /* Chữ in hoa */
  text-shadow: 0 2px 10px rgba(30, 214, 255, 0.3); /* Bóng mềm mại */
}

/* Định dạng form */
form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem; /* Giữ nguyên */
}

/* Định dạng cho các trường input */
form div {
  text-align: left;
}

label {
  display: block;
  font-size: 1rem; /* Giữ nguyên */
  color: #1ed6ff;
  margin-bottom: 0.5rem; /* Giữ nguyên */
  font-weight: 500;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2); /* Hiệu ứng bóng cho chữ */
}

/* Định dạng cho input */
input {
  width: 100%;
  padding: 0.9rem; /* Giữ nguyên */
  background: rgba(255, 255, 255, 0.15);
  border: 2px solid rgba(255, 255, 255, 0.2); /* Tăng border để mềm mại hơn */
  border-radius: 18px; /* Tăng border-radius cho tròn trịa */
  font-size: 1rem; /* Giữ nguyên */
  color: var(--text-color);
  transition: all 0.3s ease; /* Hiệu ứng khi thay đổi */
}

input::placeholder {
  color: rgba(255, 255, 255, 0.7); /* Màu cho placeholder */
}

input:focus {
  outline: none;
  background: rgba(255, 255, 255, 0.25);
  border-color: var(--primary-color);
  box-shadow: 0 0 20px rgba(30, 144, 255, 0.4); /* Hiệu ứng khi focus vào input */
  transform: translateY(-2px); /* Hiệu ứng nâng nhẹ */
}

/* Định dạng cho nút submit */
button {
  background: linear-gradient(
    135deg,
    var(--primary-color),
    #04abed
  ); /* Hiệu ứng gradient */
  color: var(--text-color);
  padding: 1rem; /* Giữ nguyên */
  border: none;
  border-radius: 20px; /* Tăng border-radius cho tròn trịa */
  font-size: 1.1rem; /* Giữ nguyên */
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  box-shadow: 0 8px 20px rgba(30, 144, 255, 0.3); /* Bóng mềm mại */
}

/* Hiệu ứng hover cho button */
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

/* Hiển thị lỗi */
p[style*="color: red"] {
  color: var(--error-color);
  font-size: 0.95rem; /* Giữ nguyên */
  margin-top: 0.8rem; /* Giữ nguyên */
  text-align: left;
  font-weight: 500;
  padding: 0.8rem 1rem; /* Thêm padding cho đẹp */
  background: rgba(255, 77, 79, 0.15);
  border-radius: 15px; /* Tròn trịa */
  border: 1px solid rgba(255, 77, 79, 0.3);
}

/* Media query cho màn hình nhỏ */
@media (max-width: 480px) {
  .login-container {
    max-width: 90%; /* Giữ nguyên */
    margin: 1.5rem auto;
    padding: 2rem; /* Giữ nguyên */
    border-radius: 20px; /* Giữ tròn trịa trên mobile */
  }

  .login-container h2 {
    font-size: 1.6rem; /* Giữ nguyên */
  }

  input,
  button {
    font-size: 0.95rem; /* Giữ nguyên */
    padding: 0.8rem; /* Giữ nguyên */
    border-radius: 15px; /* Tròn trịa trên mobile */
  }

  label {
    font-size: 0.9rem; /* Giữ nguyên */
  }
}

.forgot-password-wrapper {
  text-align: right;
  margin-bottom: -0.8rem; /* Giữ nguyên */
}

.forgot-password-link {
  color: rgba(248, 3, 3, 0.85);
  text-decoration: underline;
  font-weight: bold;
  font-size: 0.9rem; /* Giữ nguyên */
  transition: color 0.3s ease;
  padding: 0.3rem 0.5rem; /* Thêm padding nhẹ */
  border-radius: 8px; /* Tròn trịa */
}

.forgot-password-link:hover {
  color: rgb(0, 154, 250);
  background: rgba(0, 154, 250, 0.1); /* Nền mờ khi hover */
}

.switch-mode {
  margin-top: 1.5rem; /* Giữ nguyên */
  padding: 0.8rem; /* Thêm padding */
  background: rgba(255, 255, 255, 0.1); /* Nền mờ */
  border-radius: 12px; /* Tròn trịa */
  backdrop-filter: blur(10px);
}

.switch-link {
  color: #00ff44;
  text-decoration: underline;
  cursor: pointer;
  font-weight: bold;
  transition: color 0.2s ease;
  padding: 0.2rem 0.4rem; /* Thêm padding nhẹ */
  border-radius: 6px; /* Tròn trịa */
}

.switch-link:hover {
  color: #1abc9c; /* Màu khi hover */
  text-decoration: underline;
  background: rgba(26, 188, 156, 0.1); /* Nền mờ khi hover */
}
</style>
