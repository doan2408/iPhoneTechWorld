<script lang="ts" setup>
import { ref, onMounted, computed, watch, onBeforeUnmount } from "vue";
import { useRoute, useRouter } from "vue-router";
import LoginService from "@/Service/LoginService/Login.js";
import { useStore } from 'vuex'
import headerState from "./modules/headerState";

// Biến lưu trạng thái đăng nhập
const isLoggedIn = ref<boolean>(false);
const router = useRouter();
const route = useRoute();
const user = ref<{ id: number; fullName: string } | null>(null);
const isLoading = ref<boolean>(true); // Thêm loading state

const store = useStore()

if (!store.hasModule('headerState')) {
  store.registerModule('headerState', headerState);
}

const cartItemCount = computed(() => store.getters['headerState/getCartItemCount'])

// Hàm kiểm tra trạng thái đăng nhập
const checkLoginStatus = async () => {
  try {
    isLoading.value = true;
    
    // Kiểm tra localStorage trước
    const storedLoginStatus = localStorage.getItem("isLoggedIn");
    const storedUser = localStorage.getItem("user");
    
    if (storedLoginStatus === "true") {
      try {
        // Verify với server
        const currentUser = await LoginService.getCurrentUser();
        
        // Cập nhật state
        isLoggedIn.value = true;
        user.value = currentUser;
        
        // Cập nhật localStorage với thông tin mới nhất
        localStorage.setItem("isLoggedIn", "true");
        localStorage.setItem("user", JSON.stringify(currentUser));
        
      } catch (err) {
        console.error("Token expired or invalid:", err);
        // Clear invalid state
        await clearLoginState();
      }
    } else {
      // Thử kiểm tra với server trong trường hợp localStorage bị xóa nhưng token còn valid
      try {
        const currentUser = await LoginService.getCurrentUser();
        
        // Nếu server trả về user valid, cập nhật localStorage
        isLoggedIn.value = true;
        user.value = currentUser;
        localStorage.setItem("isLoggedIn", "true");
        localStorage.setItem("user", JSON.stringify(currentUser));
        
      } catch (err) {
        // User thực sự chưa đăng nhập
        await clearLoginState();
      }
    }
  } catch (error) {
    console.error("Error checking login status:", error);
    await clearLoginState();
  } finally {
    isLoading.value = false;
  }
};

// Hàm clear login state
const clearLoginState = async () => {
  isLoggedIn.value = false;
  user.value = null;
  localStorage.removeItem("isLoggedIn");
  localStorage.removeItem("user");
};

// Kiểm tra trạng thái đăng nhập khi trang được tải
onMounted(async () => {
  await checkLoginStatus();

  // Đăng ký sự kiện click bên ngoài để ẩn dropdown
  document.addEventListener("click", handleClickOutside);

  // Lắng nghe sự kiện storage change (khi user login/logout ở tab khác)
  window.addEventListener('storage', handleStorageChange);

  // Lắng nghe sự kiện focus window (khi user quay lại tab)
  window.addEventListener('focus', handleWindowFocus);

  console.log('Cart item count: ', cartItemCount.value);
});

// Cleanup khi component unmount
onBeforeUnmount(() => {
  document.removeEventListener("click", handleClickOutside);
  window.removeEventListener('storage', handleStorageChange);
  window.removeEventListener('focus', handleWindowFocus);
});

// Xử lý khi localStorage thay đổi ở tab khác
const handleStorageChange = (e: StorageEvent) => {
  if (e.key === 'isLoggedIn' || e.key === 'user') {
    checkLoginStatus();
  }
};

// Xử lý khi user focus lại window/tab
const handleWindowFocus = () => {
  // Kiểm tra lại trạng thái khi user quay lại tab
  // Có thể debounce để tránh gọi quá nhiều
  checkLoginStatus();
};

// Xử lý đăng xuất
const handleLogout = async () => {
  try {
    await LoginService.logout(); // Gọi API đăng xuất
    await clearLoginState();
    
    // Clear cart nếu cần
    store.dispatch('headerState/clearCart');
    
    router.push("/login"); // Điều hướng về trang đăng nhập
  } catch (err) {
    console.error("Lỗi đăng xuất:", err);
    // Vẫn clear state local ngay cả khi API lỗi
    await clearLoginState();
    router.push("/login");
  }
};

const showDropdown = ref(false);

// Toggle dropdown khi click vào tên người dùng
const toggleDropdown = () => {
  showDropdown.value = !showDropdown.value;
  console.log("showDropdown:", showDropdown.value);
};

// Ẩn dropdown khi click bên ngoài
const handleClickOutside = (event: MouseEvent) => {
  const target = event.target as HTMLElement;
  if (!target.closest(".user-dropdown") || target.closest(".dropdown-menu")) {
    showDropdown.value = false;
  }
};

// Kiểm tra trang login hiện tại
const isLoginPage = computed(() => route.path === "/login");

// Kiểm tra trang hiện tại, nếu là /login thì không thêm redirect vào URL
const getLoginLink = computed(() => {
  if (isLoginPage.value) {
    return null;
  }
  return `/login?redirect=${encodeURIComponent(route.fullPath)}`;
});

const goToLogin = () => {
  if (getLoginLink.value) {
    router.push(getLoginLink.value);
  }
};
</script>

<template>
  <header class="client-header">
    <div class="logo">
      <router-link to="/client/home">TechWorld</router-link>
    </div>
    <nav>
      <ul>
        <li>
          <router-link to="/client/products"><i class="fa fa-box"></i> Đơn mua của tôi</router-link>
        </li>

        <li>
          <router-link to="/client/order-tracking-search"><i class="fa fa-file-alt"></i> Tra cứu đơn hàng</router-link>
        </li>

        <li>
          <router-link to="/client/shopping-cart" class="cart-link">
            <i class="fa fa-shopping-cart" style="margin-right: 5px;"></i>
            <span class="badge">{{ cartItemCount }}</span>
            Giỏ hàng
          </router-link>
        </li>

        <!-- Hiển thị loading khi đang kiểm tra -->
        <li v-if="isLoading" class="loading-state">
          <i class="fa fa-spinner fa-spin"></i>
        </li>

        <!-- Hiển thị dropdown nếu đã đăng nhập -->
        <li v-else-if="isLoggedIn" class="user-dropdown">
          <a href="#" @click.prevent.stop="toggleDropdown">
            <i class="bi bi-person-circle"></i>
            {{ user?.fullName }}
          </a>

          <!-- Dropdown menu -->
          <transition name="fade-slide">
            <div v-if="showDropdown" class="dropdown-menu">
              <router-link to="/client/profile">
                <i class="fa fa-user"></i> Thông tin
              </router-link>
              <router-link to="/client/my-order">
                <i class="fa fa-box"></i> Đơn mua
              </router-link>
              <router-link to="/client/addresses">
                <i class="fa fa-map-marker-alt"></i> Địa chỉ
              </router-link>
              <router-link to="/client/doiDiem">
                <i class="fa fa-exchange-alt"></i> Đổi điểm
              </router-link>
              <a href="#" @click.prevent="handleLogout">
                <i class="fa fa-sign-out-alt"></i> Đăng xuất
              </a>
            </div>
          </transition>
        </li>

        <!-- Chỉ hiển thị nút Đăng nhập nếu người dùng chưa đăng nhập -->
        <li v-else>
          <a href="#" @click.prevent="goToLogin" :class="{ 'disabled-link': isLoginPage }" :disabled="isLoginPage">
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
  background-color: #333;
  color: white;
  padding: 10px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* Hiệu ứng tia lấp lánh theo chuột */
.client-header::before {
  content: "";
  position: absolute;
  top: var(--mouse-y);
  left: var(--mouse-x);
  width: 200px;
  height: 200px;
  background: radial-gradient(circle,
      rgba(255, 255, 255, 0.3) 0%,
      rgba(135, 206, 235, 0.4) 30%,
      transparent 70%);
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
  align-items: center;
  position: relative;
  text-decoration: none;
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

.user-dropdown {
  position: relative;
  margin-right: 0px;
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  background-color: white;
  border: 1px solid #ccc;
  min-width: 165px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  display: block;
  z-index: 999;
}

.dropdown-menu a {
  border-left: 3px solid #00d5ff;
  display: block;
  padding: 8px 16px;
  color: #333;
  text-decoration: none;
}

.dropdown-menu a:hover {
  border-left: 3px solid #1eff00;
  background-color: #f0f0f0;
}

/* Hiệu ứng dropdown xuất hiện mượt */
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.3s ease;
}

.fade-slide-enter-from,
.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}


.cart-link {
  position: relative;
  display: inline-block;
}

.badge {
  position: absolute;
  top: -6px;
  right: 60px;
  background-color: red;
  color: white;
  font-size: 11px;
  padding: 2px 6px;
  border-radius: 50%;
  z-index: 1;
}
</style>
