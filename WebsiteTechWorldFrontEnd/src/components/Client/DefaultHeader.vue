<script lang="ts" setup>
import { ref, onMounted, computed } from "vue";
import { useRouter, useRoute } from "vue-router";
import LoginService from "@/Service/LoginService/Login.js";
import { CartService } from "@/Service/ClientService/GioHang/CartService";

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
        <li>
          <router-link to="/shopping-cart" class="cart-link">
            <i class="fa fa-shopping-cart" style="margin-right: 5px;"></i>
            <span class="badge">{{ CartService.cartCount }}</span>
            Giỏ hàng
          </router-link>
        </li>


        <!-- Chỉ hiển thị nút Đăng xuất nếu người dùng đã đăng nhập
        <li v-if="isLoggedIn">
          <a href="#" @click.prevent="handleLogout">Đăng xuất</a>
        </li> -->

        <!-- Chỉ hiển thị nút Đăng nhập nếu người dùng chưa đăng nhập và không ở trang đăng nhập -->
        <!-- <li v-if="isLoggedIn">
          <a href="#" @click.prevent="handleLogout">Đăng nhập</a>
        </li> -->
        <li>
          <a href="#" @click.prevent="goToLogin" :class="{ 'disabled-link': isLoginPage }" :disabled="isLoginPage">
            Đăng nhập
          </a>
        </li>
      </ul>
    </nav>
  </header>
</template>

<style scoped>
/* === HEADER STYLES - Improved Version === */
.client-header {
  background: linear-gradient(135deg, #1a2954 0%, #2274c7 50%, #1a405e 100%);
  box-shadow: 0 4px 20px rgba(34, 116, 199, 0.3);
  color: white;
  padding: 12px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: sticky;
  top: 0;
  z-index: 1000;
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  position: relative;
}

/* Hiệu ứng tia lấp lánh theo chuột - Improved */
.client-header::before {
  content: "";
  position: absolute;
  top: var(--mouse-y, 50%);
  left: var(--mouse-x, 50%);
  width: 250px;
  height: 250px;
  background: radial-gradient(circle,
      rgba(135, 206, 235, 0.2) 0%,
      rgba(34, 116, 199, 0.1) 30%,
      transparent 70%);
  border-radius: 50%;
  opacity: 0;
  transition: opacity 0.4s ease;
  pointer-events: none;
  transform: translate(-50%, -50%);
  z-index: -1;
}

.client-header:hover::before {
  opacity: 1;
}

/* Logo Styling */
.client-header .logo {
  display: flex;
  align-items: center;
  z-index: 2;
}

.client-header .logo a {
  color: white;
  font-size: 28px;
  font-weight: 800;
  text-decoration: none;
  font-family: 'SF Pro Display', -apple-system, BlinkMacSystemFont, sans-serif;
  letter-spacing: -0.5px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
  transition: all 0.3s ease;
}

.client-header .logo a:hover {
  transform: translateY(-1px);
  text-shadow: 0 4px 8px rgba(0, 0, 0, 0.4);
}

/* Navigation Styling */
nav {
  z-index: 2;
}

nav ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 12px;
}

nav ul li {
  position: relative;
  margin-right: 0; /* Remove margin, use gap instead */
}

nav ul li a {
  color: rgba(255, 255, 255, 0.95);
  text-decoration: none;
  font-size: 14px;
  font-weight: 600;
  padding: 10px 16px;
  border-radius: 25px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 6px;
  background: rgba(135, 206, 235, 0.15);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(135, 206, 235, 0.3);
  position: relative;
  overflow: hidden;
}

nav ul li a::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, 
    transparent, 
    rgba(135, 206, 235, 0.3), 
    transparent
  );
  transition: left 0.6s ease;
  z-index: -1;
}

nav ul li a:hover::before {
  left: 100%;
}

nav ul li a:hover {
  background: rgba(135, 206, 235, 0.25);
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(34, 116, 199, 0.3);
  border-color: rgba(135, 206, 235, 0.5);
  text-decoration: none;
}

nav ul li a i {
  font-size: 16px;
  opacity: 0.9;
  margin-right: 0; /* Remove inline margin */
}

/* Cart Link Special Styling - FIXED BADGE POSITION */
.cart-link {
  position: relative;
  background: rgba(135, 206, 235, 0.2) !important;
  border: 2px solid rgba(135, 206, 235, 0.4) !important;
  overflow: visible !important;
}

.cart-link:hover {
  background: rgba(135, 206, 235, 0.3) !important;
  border-color: rgba(135, 206, 235, 0.6) !important;
}

.cart-link i {
  margin-right: 6px; /* Proper spacing instead of inline style */
}

/* FIXED: Badge positioning */
.badge {
  position: absolute;
  top: -8px;
  right: -8px; /* Changed from right: 60px to proper position */
  background: linear-gradient(135deg, #dc3545, #c82333);
  color: white;
  font-size: 11px;
  font-weight: 700;
  padding: 3px 6px;
  border-radius: 50%;
  min-width: 16px;
  height: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(220, 53, 69, 0.5), 0 0 0 2px white;
  z-index: 10;
  animation: pulse-badge 2s infinite;
}

@keyframes pulse-badge {
  0%, 100% { 
    transform: scale(1); 
    box-shadow: 0 2px 8px rgba(220, 53, 69, 0.5), 0 0 0 2px white; 
  }
  50% { 
    transform: scale(1.05); 
    box-shadow: 0 4px 12px rgba(220, 53, 69, 0.7), 0 0 0 2px white; 
  }
}

/* User Dropdown - KEEP ORIGINAL FUNCTIONALITY */
.user-dropdown {
  position: relative;
  margin-right: 0;
  z-index: 5;
}

.user-dropdown > a {
  background: rgba(135, 206, 235, 0.2) !important;
  border: 2px solid rgba(135, 206, 235, 0.4) !important;
  font-weight: 700 !important;
  cursor: pointer;
}

.user-dropdown > a:hover {
  background: rgba(135, 206, 235, 0.3) !important;
  border-color: rgba(135, 206, 235, 0.6) !important;
}

/* Dropdown Menu - KEEP ORIGINAL STRUCTURE */
.dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  background-color: white;
  border: none;
  min-width: 200px;
  border-radius: 12px;
  box-shadow: 
    0 20px 40px rgba(0, 0, 0, 0.15),
    0 8px 24px rgba(0, 0, 0, 0.1),
    0 0 0 1px rgba(0, 0, 0, 0.05);
  display: block;
  z-index: 9999;
  overflow: hidden;
  backdrop-filter: blur(20px);
  margin-top: 8px;
}

/* Arrow for dropdown */
.dropdown-menu::before {
  content: '';
  position: absolute;
  top: -6px;
  right: 24px;
  width: 12px;
  height: 12px;
  background: white;
  border: 1px solid rgba(0, 0, 0, 0.05);
  border-bottom: none;
  border-right: none;
  transform: rotate(45deg);
  z-index: 1;
}

.dropdown-menu a {
  display: block;
  padding: 14px 18px;
  color: #2c3e50;
  text-decoration: none;
  font-weight: 500;
  font-size: 14px;
  border-left: 3px solid transparent;
  transition: all 0.25s ease;
  background: white;
  display: flex !important;
  align-items: center;
  gap: 12px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
}

.dropdown-menu a:last-child {
  border-bottom: none;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  color: #dc3545;
}

.dropdown-menu a:hover {
  background: linear-gradient(90deg, rgba(34, 116, 199, 0.08), rgba(135, 206, 235, 0.05));
  border-left-color: #2274c7;
  color: #2274c7;
  transform: translateX(2px);
}

.dropdown-menu a:last-child:hover {
  background: linear-gradient(90deg, rgba(220, 53, 69, 0.08), rgba(200, 35, 51, 0.05));
  border-left-color: #dc3545;
  color: #dc3545;
}

.dropdown-menu a i {
  width: 16px;
  text-align: center;
  opacity: 0.7;
  font-size: 14px;
  transition: all 0.3s ease;
  margin-right: 0;
}

.dropdown-menu a:hover i {
  opacity: 1;
  transform: scale(1.1);
}

/* Loading State */
.loading-state {
  display: flex;
  align-items: center;
  padding: 10px 16px;
}

.loading-state i {
  color: rgba(255, 255, 255, 0.8);
  font-size: 16px;
}

/* KEEP ORIGINAL Animation */
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.3s ease;
}

.fade-slide-enter-from,
.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

.fade-slide-enter-to {
  opacity: 1;
  transform: translateY(0);
}

.fade-slide-leave-from {
  opacity: 1;
  transform: translateY(0);
}

/* Disabled Link */
.disabled-link {
  opacity: 0.5;
  cursor: not-allowed;
  pointer-events: none;
}

/* Responsive Design */
@media (max-width: 1024px) {
  .client-header {
    padding: 10px 20px;
  }

  nav ul {
    gap: 8px;
  }

  nav ul li a {
    padding: 8px 12px;
    font-size: 13px;
  }

  .client-header .logo a {
    font-size: 24px;
  }
}

@media (max-width: 768px) {
  .client-header {
    padding: 8px 16px;
    flex-wrap: wrap;
    gap: 12px;
  }

  .client-header .logo a {
    font-size: 20px;
  }

  nav ul {
    flex-wrap: wrap;
    gap: 6px;
  }

  nav ul li a {
    padding: 6px 10px;
    font-size: 12px;
  }

  nav ul li a i {
    font-size: 14px;
  }

  .dropdown-menu {
    min-width: 180px;
    right: -10px;
  }

  .dropdown-menu::before {
    right: 30px;
  }

  .badge {
    font-size: 10px;
    padding: 2px 5px;
    min-width: 14px;
    height: 14px;
    top: -6px;
    right: -6px;
  }
}

@media (max-width: 640px) {
  .client-header {
    flex-direction: column;
    align-items: stretch;
    padding: 12px 16px;
  }

  .client-header .logo {
    justify-content: center;
    margin-bottom: 8px;
  }

  nav ul {
    justify-content: center;
    flex-wrap: wrap;
  }

  nav ul li a {
    padding: 8px 12px;
    font-size: 13px;
  }

  .dropdown-menu {
    right: 50%;
    transform: translateX(50%);
    min-width: 180px;
  }

  .dropdown-menu::before {
    right: 50%;
    transform: translateX(50%) rotate(45deg);
  }
}

@media (max-width: 480px) {
  nav ul li a {
    padding: 6px 8px;
    font-size: 11px;
  }

  nav ul li a i {
    font-size: 12px;
  }

  /* Hide cart text on mobile */
  .cart-link {
    font-size: 0;
  }

  .cart-link i {
    font-size: 16px;
  }

  .badge {
    font-size: 9px;
    padding: 2px 4px;
    min-width: 12px;
    height: 12px;
    top: -4px;
    right: -4px;
  }

  .user-dropdown > a {
    max-width: 120px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

/* Focus states for accessibility */
nav ul li a:focus {
  outline: 2px solid rgba(135, 206, 235, 0.6);
  outline-offset: 2px;
}

.dropdown-menu a:focus {
  outline: 2px solid rgba(34, 116, 199, 0.5);
  outline-offset: -2px;
}

/* Reduced motion support */
@media (prefers-reduced-motion: reduce) {
  .client-header::before,
  nav ul li a::before,
  .fade-slide-enter-active,
  .fade-slide-leave-active,
  .badge {
    animation: none;
    transition: none;
  }
}
</style>