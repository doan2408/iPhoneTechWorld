<script lang="ts" setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import LoginService from "@/Service/LoginService/Login.js";

// Biến lưu trạng thái đăng nhập
const isLoggedIn = ref<boolean>(false);
const router = useRouter();
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

onMounted(() => {
  document.addEventListener("click", handleClickOutside);
});
</script>

<template>
  <header class="client-header">
    <div class="logo">
      <router-link to="/client/home">TechWorld</router-link>
    </div>
    <nav>
      <ul>
        <li><router-link to="/client/category">Danh mục</router-link></li>
        <li><router-link to="/client/users">Lọc Theo Giá</router-link></li>
        <li>
          <router-link to="/client/products">Bạn muốn tìm gì</router-link>
        </li>
        <li>
          <router-link to="/client/products">Đơn mua của tôi</router-link>
        </li>
        <li><router-link to="/client/orders">Tra cứu đơn hàng</router-link></li>
        <li><router-link to="/client/shopping-cart">Giỏ hàng</router-link></li>
        <li v-if="isLoggedIn" class="user-dropdown">
          <a href="#" @click.prevent.stop="toggleDropdown"
            > <i class="bi bi-person-circle"></i> {{ user?.fullName }} </a
          >

          <!-- Dropdown menu -->
          <div v-if="showDropdown" class="dropdown-menu">
            <router-link to="/client/profile">Thông tin cá nhân</router-link>
            <router-link to="/client/orders">Đơn mua</router-link>
            <router-link to="/client/orders">Địa chỉ</router-link>
            <a href="#" @click.prevent="handleLogout">Đăng xuất</a>
          </div>
        </li>


        <!-- Chỉ hiển thị nút Đăng nhập nếu người dùng chưa đăng nhập -->
        <li v-if="!isLoggedIn">
          <router-link to="/login">Đăng nhập</router-link>
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
  align-items: center; /* Đảm bảo canh giữa */
  position: relative; /* Đảm bảo dropdown nằm trong vùng nav */
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
  z-index: 999; /* Đảm bảo nó nằm trên các thành phần khác */
}

.dropdown-menu a {
  border-left: 3px solid #00d5ff ;

  display: block;
  padding: 8px 16px;
  color: #333;
  text-decoration: none;
}

.dropdown-menu a:hover {
  border-left: 3px solid #1eff00;
  background-color: #f0f0f0;
}
</style>
