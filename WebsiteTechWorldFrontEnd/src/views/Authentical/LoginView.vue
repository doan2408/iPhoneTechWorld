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

// const handleLogin = async () => {
//   if (!tai_khoan.value.trim() || !mat_khau.value.trim()) {
//     error.value = "Vui lòng nhập tài khoản và mật khẩu";
//     return;
//   }
//   try {
//     isLoading.value = true;
//     await store.dispatch("login", {
//       tai_khoan: tai_khoan.value,
//       mat_khau: mat_khau.value,
//     });
//     if (store.getters.isAdmin) {
//       router.push("/admin/products");
//     } else if (store.getters.isStaff) {
//       router.push("/staff/products");
//     } else if (store.getters.isCustomer) {
//       router.push("/client/home");
//     }
//   } catch (err) {
//     error.value = err.message || "Đăng nhập thất bại";
//   } finally {
//     isLoading.value = false;
//   }
// };

const isLogin = ref(true); // true: đăng nhập, false: đăng ký

watch([tai_khoan, mat_khau], () => {
  error.value = "";
});
</script>

<template>
  <div class="login-page">
    <div class="login-container">
      <h2>{{ isLogin ? "Đăng nhập" : "Đăng ký" }}</h2>

      <form @submit.prevent="isLogin ? handleLogin() : handleRegister()">
        <div>
          <label>Tài khoản:</label>
          <input
            v-model="tai_khoan"
            type="text"
            placeholder="Nhập tài khoản"
            required
          />
        </div>

        <div>
          <label>Mật khẩu:</label>
          <input
            v-model="mat_khau"
            type="password"
            placeholder="Nhập mật khẩu"
            required
          />

          <div class="forgot-password-wrapper" v-if="isLogin">
            <router-link to="/forgot-password" class="forgot-password-link">
              Quên mật khẩu?
            </router-link>
          </div>
        </div>

        <!-- Nếu là đăng ký thì có thêm ô nhập lại mật khẩu hoặc email -->
        <div v-if="!isLogin">
          <div>
            <label>Email:</label>
            <input
              v-model="email"
              type="email"
              placeholder="Nhập email"
              required
            />
          </div>
          <div>
            <label>Nhập lại mật khẩu:</label>
            <input
              v-model="confirm_mat_khau"
              type="password"
              placeholder="Nhập lại mật khẩu"
            />
          </div>
        </div>

        <button type="submit" :disabled="isLoading">
          {{ isLoading ? "Đang xử lý..." : isLogin ? "Đăng nhập" : "Đăng ký" }}
        </button>

        <p v-if="error" style="color: red">{{ error }}</p>

        <p class="switch-mode">
          {{ isLogin ? "Chưa có tài khoản?" : "Đã có tài khoản?" }}
          <span @click="isLogin = !isLogin" class="switch-link">
            {{ isLogin ? "Tạo tài khoản" : "Đăng nhập ngay" }}
          </span>
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
  --border-radius: 12px; /* Độ cong của các góc */
  --shadow: 0 8px 24px rgba(0, 0, 0, 0.15); /* Đổ bóng nhẹ */
  --glass-bg: rgba(255, 255, 255, 0.1); /* Nền mờ cho login */
  --glass-border: 1px solid rgba(255, 255, 255, 0.1); /* Biên mờ */
}

/* Toàn bộ trang login */
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 92vh;
  background-image: url("/src/components/images/04e266e3cc304802aa7c5ea14c4a7207~tplv-photomode-image.jpeg"); /* Chèn ảnh nền ở đây */
  background-size: cover; /* Phủ ảnh toàn bộ */
  background-position: center; /* Canh giữa ảnh */
  background-repeat: no-repeat; /* Không lặp lại ảnh */
}

/* Cấu trúc chính của login container */
.login-container {
  max-width: 400px; /* Đặt chiều rộng cố định cho form */
  width: 100%;
  padding: 2.5rem;
  background: var(--glass-bg); /* Nền mờ */
  backdrop-filter: blur(10px); /* Làm mờ nền phía sau */
  border: var(--glass-border);
  border-radius: var(--border-radius);
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
  color: var(--text-color);
  margin-bottom: 2rem;
  font-size: 2rem;
  font-weight: 700;
  letter-spacing: 1px;
  text-transform: uppercase; /* Chữ in hoa */
}

/* Định dạng form */
form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

/* Định dạng cho các trường input */
form div {
  text-align: left;
}

label {
  display: block;
  font-size: 1rem;
  color: var(--text-color);
  margin-bottom: 0.5rem;
  font-weight: 500;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2); /* Hiệu ứng bóng cho chữ */
}

/* Định dạng cho input */
input {
  width: 100%;
  padding: 0.9rem;
  background: rgba(255, 255, 255, 0.15);
  border: var(--glass-border);
  border-radius: var(--border-radius);
  font-size: 1rem;
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
  box-shadow: 0 0 8px rgba(30, 144, 255, 0.4); /* Hiệu ứng khi focus vào input */
}

/* Định dạng cho nút submit */
button {
  background: linear-gradient(
    135deg,
    var(--primary-color),
    #04abed
  ); /* Hiệu ứng gradient */
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
  box-shadow: 0 6px 12px rgba(30, 144, 255, 0.3);
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
  font-size: 0.95rem;
  margin-top: 0.8rem;
  text-align: left;
  font-weight: 500;
}

/* Media query cho màn hình nhỏ */
@media (max-width: 480px) {
  .login-container {
    max-width: 90%;
    margin: 1.5rem auto;
    padding: 2rem;
  }

  .login-container h2 {
    font-size: 1.6rem;
  }

  input,
  button {
    font-size: 0.95rem;
    padding: 0.8rem;
  }

  label {
    font-size: 0.9rem;
  }
}

.forgot-password-wrapper {
  text-align: right;
  margin-bottom: -0.8rem;
}

.forgot-password-link {
  color: rgba(248, 3, 3, 0.85);
  text-decoration: underline;
  font-weight: bold;
  font-size: 0.9rem;
  transition: color 0.3s ease;
}

.forgot-password-link:hover {
  color: rgb(0, 154, 250);
}

.switch-link {
  color: #3498db;
  text-decoration: underline;
  cursor: pointer;
  font-weight: bold;
  transition: color 0.2s ease;
}

.switch-link:hover {
  color: #1abc9c; /* Màu khi hover */
  text-decoration: underline;
}
</style>
