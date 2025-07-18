<script lang="ts" setup>
import { ref, onMounted, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import LoginService from "@/Service/LoginService/Login.js";


// Biến lưu trạng thái đăng nhập
const isLoggedIn = ref<boolean>(false);
const router = useRouter();
const route = useRoute();
const user = ref<{ fullName: String } | null>(null);

// Kiểm tra trạng thái đăng nhập khi trang được tải
onMounted(async () => {
  // Kiểm tra trạng thái đăng nhập từ localStorage trước
  const storedLoginStatus = localStorage.getItem("isLoggedIn");

  if (storedLoginStatus === "true") {
    // Nếu có trong localStorage, cố gắng lấy thông tin người dùng
    try {
      const currentUser = await LoginService.getCurrentUser(); // Lấy thông tin người dùng từ API
      isLoggedIn.value = true; // Người dùng đã đăng nhập
      user.value = currentUser;
    } catch (err) {
      // Nếu có lỗi (người dùng đã đăng nhập nhưng phiên hết hạn chẳng hạn)
      isLoggedIn.value = false; // Đánh dấu người dùng không đăng nhập
      localStorage.removeItem("isLoggedIn"); // Xóa trạng thái đăng nhập không hợp lệ
    }
  } else {
    // Nếu không có trạng thái đăng nhập trong localStorage
    isLoggedIn.value = false; // Đánh dấu người dùng chưa đăng nhập
  }

  // Đăng ký sự kiện click bên ngoài để ẩn dropdown
  document.addEventListener("click", handleClickOutside);
});

// Xử lý đăng xuất
const handleLogout = async () => {
  try {
    await LoginService.logout(); // Gọi API đăng xuất
    isLoggedIn.value = false;
    localStorage.removeItem("isLoggedIn"); // Xóa trạng thái đăng nhập khỏi localStorage
    localStorage.removeItem("user"); 
    router.push("/login"); // Điều hướng về trang đăng nhập
  } catch (err) {
    console.error("Lỗi đăng xuất:", err);
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
  // Nếu đang ở trang login, không cần thay đổi gì
  if (isLoginPage.value) {
    return null; // Không thêm redirect vào URL
  }

  // Nếu không phải trang login, thêm redirect vào URL
  return `/login?redirect=${encodeURIComponent(route.fullPath)}`;
});

const goToLogin = () => {
  // Nếu không ở trang login, điều hướng đến trang login với redirect
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
          <router-link to="/client/products"
            ><i class="fa fa-box"></i> Đơn mua của tôi</router-link
          >
        </li>

        <li>
          <router-link to="/client/order-tracking-search"
            ><i class="fa fa-file-alt"></i> Tra cứu đơn hàng</router-link
          >
        </li>

        <li>
          <router-link to="/client/shopping-cart"
            ><i class="fa fa-shopping-cart"></i> Giỏ hàng</router-link
          >
        </li>

        <!-- Hiển thị dropdown nếu đã đăng nhập -->
        <li v-if="isLoggedIn" class="user-dropdown">
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
              <a href="#" @click.prevent="handleLogout">
                <i class="fa fa-sign-out-alt"></i> Đăng xuất
              </a>
            </div>
          </transition>
        </li>

        <!-- Chỉ hiển thị nút Đăng nhập nếu người dùng chưa đăng nhập -->
        <li v-if="!isLoggedIn">
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
</style>
