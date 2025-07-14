<script lang="ts" setup>
import { ref, onMounted, computed } from "vue";
import { useRouter, useRoute } from "vue-router";
import LoginService from "@/Service/LoginService/Login.js";

// Biến lưu trạng thái đăng nhập
const isLoggedIn = ref<boolean>(false);
const router = useRouter();
const route = useRoute();

// Kiểm tra trạng thái đăng nhập khi trang được tải
onMounted(async () => {
  // Kiểm tra trạng thái đăng nhập từ localStorage trước
  const storedLoginStatus = localStorage.getItem("isLoggedIn");

  if (storedLoginStatus === "true") {
    // Nếu có trong localStorage, cố gắng lấy thông tin người dùng
    try {
      await LoginService.getCurrentUser(); // Lấy thông tin người dùng từ API
      isLoggedIn.value = true; // Người dùng đã đăng nhập
    } catch (err) {
      // Nếu có lỗi (người dùng đã đăng nhập nhưng phiên hết hạn chẳng hạn)
      isLoggedIn.value = false; // Đánh dấu người dùng không đăng nhập
      localStorage.removeItem("isLoggedIn"); // Xóa trạng thái đăng nhập không hợp lệ
    }
  } else {
    // Nếu không có trạng thái đăng nhập trong localStorage
    isLoggedIn.value = false; // Đánh dấu người dùng chưa đăng nhập
  }
});

// Xử lý đăng xuất
const handleLogout = async () => {
  try {
    await LoginService.logout(); // Gọi API đăng xuất
    isLoggedIn.value = false;
    localStorage.removeItem("isLoggedIn"); // Xóa trạng thái đăng nhập khỏi localStorage
    router.push("/login"); // Điều hướng về trang đăng nhập
  } catch (err) {
    console.error("Lỗi đăng xuất:", err);
  }
};

// Kiểm tra trang login hiện tại
const isLoginPage = computed(() => route.path === '/login')

// Kiểm tra trang hiện tại, nếu là /login thì không thêm redirect vào URL
const getLoginLink = computed(() => {
  // Nếu đang ở trang login, không cần thay đổi gì
  if (isLoginPage.value) {
    return null  // Không thêm redirect vào URL
  }
  
  // Nếu không phải trang login, thêm redirect vào URL
  return `/login?redirect=${encodeURIComponent(route.fullPath)}`
})

const goToLogin = () => {
  // Nếu không ở trang login, điều hướng đến trang login với redirect
  if (getLoginLink.value) {
    router.push(getLoginLink.value)
  }
}
</script>

<template>
  <header class="client-header">
    <div class="logo">
      <router-link to="/">TechWorld</router-link>
    </div>
    <nav>
      <ul>
        <li><router-link to="/category"><i class="fa fa-box"></i> Danh mục</router-link></li>
        <li><router-link to="/orders"><i class="fa fa-file-alt"></i> Tra cứu đơn hàng</router-link></li>
        <li><router-link to="/shopping-cart"><i class="fa fa-shopping-cart"></i> Giỏ hàng</router-link></li>

        <!-- Chỉ hiển thị nút Đăng xuất nếu người dùng đã đăng nhập
        <li v-if="isLoggedIn">
          <a href="#" @click.prevent="handleLogout">Đăng xuất</a>
        </li> -->

        <!-- Chỉ hiển thị nút Đăng nhập nếu người dùng chưa đăng nhập và không ở trang đăng nhập -->
         <!-- <li v-if="isLoggedIn">
          <a href="#" @click.prevent="handleLogout">Đăng nhập</a>
        </li> -->
        <li>
          <a
            href="#"
            @click.prevent="goToLogin"
            :class="{ 'disabled-link': isLoginPage }"
            :disabled="isLoginPage"
          >
          Đăng nhập
          </a>
        </li>
      </ul>
    </nav>
  </header>
</template>

<style scoped>
.client-header {
  background: linear-gradient(135deg, #1a2954 0%, #2274c7 50%, #1a405e 100%);
  color: white;
  padding: 10px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  overflow: hidden;
  --mouse-x: 0px;
  --mouse-y: 0px;
}

/* Hiệu ứng tia lấp lánh theo chuột */
.client-header::before {
  content: '';
  position: absolute;
  top: var(--mouse-y);
  left: var(--mouse-x);
  width: 200px;
  height: 200px;
  background: radial-gradient(
    circle,
    rgba(255, 255, 255, 0.3) 0%,
    rgba(135, 206, 235, 0.4) 30%,
    transparent 70%
  );
  border-radius: 50%;
  opacity: 0;
  transition: opacity 0.3s ease;
  pointer-events: none;
  transform: translate(-50%, -50%);
}

.client-header:hover::before {
  opacity: 1;
}

.client-header .logo a {
  color: white;
  font-size: 24px;
  text-decoration: none;
}

nav ul {
  list-style-type: none;
  padding: 0;
  display: flex;
}

nav ul li {
  margin-right: 20px;
}

nav ul li a {
  color: white;
  text-decoration: none;
  font-size: 16px;
}

nav ul li a:hover {
  text-decoration: none;
}

/* Style cho link bị disabled */
.disabled-link {
  opacity: 0.6;
  cursor: not-allowed;
  pointer-events: none;
}
</style>