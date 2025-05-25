<script lang="ts" setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import LoginService from '@/Service/LoginService/Login.js'
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

</script>

<template>
  <div class="admin-sidebar">
    <div>
      <div class="user-info" v-if="isLoggedIn">
        <p class="username">👤 {{ user?.fullName }}</p>
      </div>

      <nav>
        <ul>
          <li><router-link to="/admin/dashboard">Dashboard</router-link></li>
          
          <!-- Quản lý sản phẩm có submenu -->
          <li @click="toggleProductMenu" class="menu-toggle">
            Quản lý sản phẩm
            <i :class="showProductMenu ? 'bi bi-chevron-down' : 'bi bi-chevron-right'"></i>
          </li>
          <ul v-if="showProductMenu" class="submenu">
            <li><router-link to="/admin/products">Danh sách sản phẩm</router-link></li>
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

         
          <li><router-link to="/admin/orders">Quản lý đơn hàng</router-link></li>
          <li><router-link to="/admin/promotions">Khuyến mãi</router-link></li>
          <li><router-link to="/admin/reports">Báo cáo</router-link></li>
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
/* Giữ nguyên CSS sidebar của bạn */
.admin-sidebar {
  width: 220px;
  height: 100vh;
  background-color: #04c9ff;
  color: white;
  position: fixed;
  top: 0;
  left: 0;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 20px;
}

.user-info {
  margin-bottom: 20px;
  border-bottom: 1px solid #7f8c8d;
  padding-bottom: 15px;
}

.username {
  font-weight: bold;
  margin-bottom: 8px;
}


nav ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

nav li {
  margin: 10px 0;
}

nav a {
  color: white;
  text-decoration: none;
}

nav a.router-link-exact-active {
  font-weight: bold;
  color: #1abc9c;
}

.logout-section {
  border-top: 1px solid #7f8c8d;
  padding-top: 15px;
}

.logout-section a,
.logout-section router-link {
  color: white;
  text-decoration: none;
}

.menu-toggle {
  cursor: pointer;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}

.submenu {
  padding-left: 15px;
  background-color: #13e780;
  margin-top: 5px;
  border-left: 3px solid #9dff00;
  border-radius: 10px;
}

.submenu li {
  margin: 8px 0;
}

.submenu a {
  color: #ecf0f1;
  text-decoration: none;
}

.submenu a.router-link-exact-active {
  font-weight: bold;
  color: #2054e4;
}

/* 🎯 Thêm CSS cho nội dung trang bên phải */
.content-container {
  margin-left: 220px; /* Đẩy sang phải đúng bằng sidebar */
  padding: 20px;
  width: calc(100% - 220px); /* Chiếm phần còn lại của màn hình */
  box-sizing: border-box; /* Đảm bảo padding không làm to nội dung */
}

.table {
  width: 100%; /* Table full chiều ngang trong container */
}

/* 🎯 Responsive: khi màn hình nhỏ */
@media screen and (max-width: 768px) {
  .admin-sidebar {
    width: 180px; /* Sidebar hẹp hơn */
  }
  .content-container {
    margin-left: 180px;
    width: calc(100% - 180px);
  }
}

@media screen and (max-width: 480px) {
  .admin-sidebar {
    position: relative; /* Sidebar chuyển sang không fixed */
    width: 100%;
    height: auto;
  }
  .content-container {
    margin-left: 0;
    width: 100%;
  }
}
</style>