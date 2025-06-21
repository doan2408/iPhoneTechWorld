<script lang="ts" setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import LoginService from '@/Service/LoginService/Login'
import { useStore } from 'vuex'

const store = useStore();
// Biến lưu trạng thái đăng nhập
const isLoggedIn = ref<boolean>(false)
const router = useRouter()
const user = ref<{fullName : String } | null>(null); // JSON trả về phải có trường tương ứng (fullName)
// Kiểm tra trạng thái đăng nhập khi trang được tải
onMounted(async () => {
  try {
    const currentUser = await LoginService.getCurrentUser();
    store.commit('setUser', currentUser);  // Lưu thông tin vào store
    store.commit('setRoles', currentUser.roles || []);  // Lấy thông tin người dùng
    //user.value chỉ chấp nhận một đối tượng có duy nhất một trường fullName: string, hoặc null
    user.value = {
      fullName : currentUser.fullName  // Lưu thông tin người dùng vào biến user
    }
    isLoggedIn.value = true;  // Đánh dấu người dùng đã đăng nhập
  } catch (err) {
    isLoggedIn.value = false;  // Người dùng chưa đăng nhập
  }
})


// Xử lý đăng xuất
const handleLogout = async () => {
  try {
    await LoginService.logout()  // Gọi API đăng xuất
    isLoggedIn.value = false
    router.push('/login')  // Điều hướng về trang đăng nhập
  } catch (err) {
    console.error('Lỗi đăng xuất:', err)
  }
}

const showProductMenu = ref(false)
function toggleProductMenu() {
  showProductMenu.value = !showProductMenu.value
}

const showUsers = ref(false)
function toggleUserstMenu() {
  showUsers.value = !showUsers.value
}

const showOrders = ref(false)
function toggleOrderMenu() {
  showOrders.value = !showOrders.value
}

</script>

<template>
  <div class="admin-sidebar">
    <div>
      <div class="logo">
          <img src="/src/components/images/LogoTechWorld-removebg-preview.png" alt="TechWorld">
      </div>
      <div class="user-info" v-if="isLoggedIn">
        <p class="username">👤 {{ user?.fullName }}</p>
      </div>

      <nav>
        <ul>
          <li><router-link to="/admin/dashboard">Dashboard</router-link></li>

          <li><router-link to="/admin/statistical" class="icon stats-icon">Thống Kê </router-link></li>
          <!-- Quản lý sản phẩm có submenu -->
          <li @click="toggleProductMenu" class="menu-toggle">
            Quản lý sản phẩm
            <i :class="showProductMenu ? 'bi bi-chevron-down' : 'bi bi-chevron-right'"></i>
          </li>
          <ul v-if="showProductMenu" class="submenu">

            <li><router-link to="/admin/products">Sản phẩm</router-link></li>
            <li><router-link to="/admin/xuatXu">Xuất xứ</router-link></li>
            <li><router-link to="/admin/rom">Rom</router-link></li>
            <li><router-link to="/admin/ram">Ram</router-link></li>
            <li><router-link to="/admin/pin">Pin</router-link></li>
            <li><router-link to="/admin/mauSac">Màu sắc</router-link></li>
            <li><router-link to="/admin/nhaCungCap">Nhà cung cấp</router-link></li>
            <li><router-link to="/admin/manHinh">Màn hình</router-link></li>
            <li><router-link to="/admin/loai">Loại</router-link></li>
            <li><router-link to="/admin/imei">Imei</router-link></li>
            <li><router-link to="/admin/heDieuHanh">Hệ điều hành</router-link></li>
            <li><router-link to="/admin/cpu">Cpu</router-link></li>
            <li><router-link to="/admin/cameraTruoc">Camera trước</router-link></li>
            <li><router-link to="/admin/cameraSau">Camera sau</router-link></li>
            <li><router-link to="/admin/products/add">Thêm sản phẩm</router-link></li>
            <li><router-link to="/admin/categories">Danh mục</router-link></li>
          </ul>


          <li @click="toggleUserstMenu" class="menu-toggle">
            Quản lý người dùng
            <i :class="showUsers ? 'bi bi-chevron-down' : 'bi bi-chevron-right'"></i>
          </li>
          <ul v-if="showUsers" class="submenu">
            <li><router-link to="/admin/staff">Quản lý nhân viên</router-link></li>
            <li><router-link to="/admin/client">Quản lý khách hàng</router-link></li>
          </ul>

          <li @click="toggleOrderMenu" class="menu-toggle">
            Quản lý đơn hàng
            <i :class="showOrders ? 'bi bi-chevron-down' : 'bi bi-chevron-right'"></i>
          </li>
          <ul v-if="showOrders" class="submenu">
            <li><router-link to="/admin/bill">Quản lý hóa đơn</router-link></li>
            <li><router-link to="/admin/shipping">Quản lý giao hàng</router-link></li>
            <li><router-link to="/admin/ban-hang">Bán hàng</router-link></li>
          </ul>

          <li><router-link to="/admin/promotions">Quản lý khuyến mãi</router-link></li>
          <li><router-link to="/admin/staff/infor">Thông tin cá nhân</router-link></li>

        </ul>
      </nav>
    </div>

    <!-- Nút đăng xuất luôn ở cuối -->
    <div class="logout-section" v-if="isLoggedIn">
      <a href="#" @click.prevent="handleLogout">Đăng xuất</a>
    </div>
    <div class="logout-section" v-if="!isLoggedIn">
      <router-link to="/login">Đăng nhập</router-link>
    </div>
  </div>
</template>


<style scoped>
.admin-sidebar {
  width: 220px;
  height: 100vh;
  background: linear-gradient(135deg, #1e3a8a 0%, #3b82f6 30%, #1e40af 70%, #1e3a8a 100%);
  color: white;
  position: fixed;
  top: 0;
  left: 0;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 20px;
  box-shadow: 4px 0 15px rgba(59, 130, 246, 0.3);
  backdrop-filter: blur(10px);
  border-right: 1px solid rgba(147, 197, 253, 0.3);
  overflow-y: auto;
}

/* Hiệu ứng sao nhỏ cho sidebar */
.admin-sidebar::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: 
    radial-gradient(1px 1px at 20px 30px, rgba(255, 255, 255, 0.6), transparent),
    radial-gradient(1px 1px at 40px 70px, rgba(147, 197, 253, 0.5), transparent),
    radial-gradient(1px 1px at 90px 40px, rgba(255, 255, 255, 0.7), transparent),
    radial-gradient(1px 1px at 130px 80px, rgba(191, 219, 254, 0.6), transparent),
    radial-gradient(1px 1px at 160px 120px, rgba(255, 255, 255, 0.5), transparent);
  background-repeat: repeat;
  background-size: 200px 150px;
  animation: sidebarStars 6s ease-in-out infinite alternate;
  pointer-events: none;
  z-index: 0;
}

@keyframes sidebarStars {
  0% { opacity: 0.5; }
  100% { opacity: 0.8; }
}

.logo {
  text-align: center;
  margin-bottom: 10px;
  padding: 5px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 100px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  position: relative;
  z-index: 1;
  transition: all 0.3s ease;
}

.logo:hover {
  background: rgba(255, 255, 255, 0.15);
  transform: translateY(-2px);
  box-shadow: 0 5px 20px rgba(147, 197, 253, 0.3);
}

.logo img {
  max-width: 100%;
  height: auto;
  max-height: 70px;
  border-radius: 10px;
  transition: all 0.3s ease;
  filter: drop-shadow(0 2px 8px rgba(0, 0, 0, 0.2));
}

.logo img:hover {
  transform: scale(1.05);
  filter: drop-shadow(0 4px 12px rgba(147, 197, 253, 0.4));
}

/* Nếu không có ảnh, hiển thị placeholder */
.logo:empty::before {
  content: "TechWorld";
  display: block;
  font-size: 18px;
  font-weight: 800;
  color: #f1f5f9;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
  letter-spacing: 2px;
  background: linear-gradient(135deg, #ffffff 0%, #ffffff 50%, #ffffff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  padding: 15px 0;
}

.user-info {
  margin-bottom: 20px;
  border-bottom: 1px solid rgba(147, 197, 253, 0.4);
  padding-bottom: 15px;
  position: relative;
  z-index: 1;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 10px;
  padding: 15px;
  backdrop-filter: blur(10px);
}

.username {
  font-weight: 600;
  margin-bottom: 0px;
  color: #f1f5f9;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
  font-size: 14px; /* Giảm font size */
}

nav {
  position: relative;
  z-index: 1;
}

nav ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

nav li {
  margin: 6px 0; /* Giảm margin */
}

nav a {
  color: #f1f5f9;
  text-decoration: none;
  padding: 8px 12px; /* Giảm padding */
  border-radius: 8px;
  display: block;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  font-size: 13px; /* Giảm font size */
}

nav a:hover {
  background: rgba(147, 197, 253, 0.3);
  color: white;
  transform: translateX(5px);
  box-shadow: 0 3px 10px rgba(147, 197, 253, 0.4);
}

nav a.router-link-exact-active {
  font-weight: bold;
  background: linear-gradient(135deg, #2563eb 0%, #60a5fa 100%);
  color: white;
  box-shadow: 0 3px 15px rgba(96, 165, 250, 0.5);
}

.logout-section {
  border-top: 1px solid rgba(147, 197, 253, 0.4);
  padding-top: 15px;
  position: relative;
  z-index: 1;
}

.logout-section a,
.logout-section router-link {
  color: white;
  text-decoration: none;
  padding: 8px 12px; /* Giảm padding */
  border-radius: 8px;
  display: block;
  transition: all 0.3s ease;
  background: rgba(239, 68, 68, 0.8);
  text-align: center;
  font-weight: 600;
  font-size: 13px; /* Giảm font size */
}

.logout-section a:hover {
  background: rgba(220, 38, 38, 0.9);
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(239, 68, 68, 0.5);
}

.menu-toggle {
  cursor: pointer;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  padding: 8px 12px; /* Giảm padding */
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.15);
  border: 1px solid rgba(255, 255, 255, 0.25);
  transition: all 0.3s ease;
  color: #f1f5f9;
  font-size: 13px; /* Giảm font size */
}

.menu-toggle:hover {
  background: rgba(147, 197, 253, 0.25);
  transform: translateX(3px);
  box-shadow: 0 3px 10px rgba(147, 197, 253, 0.3);
}

.menu-toggle i {
  transition: transform 0.3s ease;
  color: #dbeafe;
  flex-shrink: 0;
  margin-left: 8px;
  font-size: 12px; /* Giảm font size cho icon */
}

.submenu {
  padding-left: 15px;
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.4) 0%, rgba(96, 165, 250, 0.3) 100%);
  margin-top: 5px;
  border-left: 3px solid #60a5fa;
  border-radius: 10px;
  padding: 8px 12px; /* Giảm padding */
  backdrop-filter: blur(5px);
  animation: slideDown 0.3s ease-out;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.submenu li {
  margin: 4px 0; /* Giảm margin */
}

.submenu a {
  color: #f1f5f9;
  text-decoration: none;
  padding: 6px 10px; /* Giảm padding */
  font-size: 12px; /* Giảm font size */
  background: rgba(255, 255, 255, 0.1);
}

.submenu a:hover {
  background: rgba(255, 255, 255, 0.2);
  color: white;
}

.submenu a.router-link-exact-active {
  font-weight: bold;
  background: rgba(96, 165, 250, 0.5);
  color: white;
}

/* Content container */
.content-container {
  margin-left: 220px;
  padding: 20px;
  width: calc(100% - 220px);
  box-sizing: border-box;
  min-height: 100vh;
  background: rgba(255, 255, 255, 0.02);
}

.table {
  width: 100%;
}

/* Responsive */
@media screen and (max-width: 768px) {
  .admin-sidebar {
    width: 180px;
  }
  .content-container {
    margin-left: 180px;
    width: calc(100% - 180px);
  }
  
  .menu-toggle {
    font-size: 12px;
    padding: 6px 10px;
  }
  
  .submenu a {
    font-size: 11px;
    padding: 5px 8px;
  }
}

@media screen and (max-width: 480px) {
  .admin-sidebar {
    position: relative;
    width: 100%;
    height: auto;
  }
  .content-container {
    margin-left: 0;
    width: 100%;
  }
}

/* Scrollbar styling */
.admin-sidebar::-webkit-scrollbar {
  width: 6px;
}

.admin-sidebar::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 3px;
}

.admin-sidebar::-webkit-scrollbar-thumb {
  background: rgba(147, 197, 253, 0.6);
  border-radius: 3px;
}

.admin-sidebar::-webkit-scrollbar-thumb:hover {
  background: rgba(147, 197, 253, 0.8);
}
/* Thông KêKê */
</style>