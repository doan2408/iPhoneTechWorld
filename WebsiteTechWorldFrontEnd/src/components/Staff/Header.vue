<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import LoginService from '@/Service/LoginService/Login.js'

// Biến lưu trạng thái đăng nhập
const isLoggedIn = ref<boolean>(false)
const router = useRouter()
const user = ref<{fullName : String } | null>(null); // JSON trả về phải có trường tương ứng (fullName)

// Kiểm tra trạng thái đăng nhập khi trang được tải
onMounted(async () => {
  try {
    const currentUser = await LoginService.getCurrentUser();  // Lấy thông tin người dùng
    //user.value chỉ chấp nhận một đối tượng có duy nhất một trường fullName: string, hoặc null
    user.value = {
      fullName : currentUser.fullName  // Lưu thông tin người dùng vào biến user
    }
    isLoggedIn.value = true
  } catch (err) {
    isLoggedIn.value = false
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
</script>

<template>
  <div class="staff-sidebar">
    <div>
      <div class="user-info" v-if="isLoggedIn">
        <p class="username">👤 {{ user?.fullName }}</p>
      </div>

      <nav>
        <ul>
          <li><router-link to="/staff/dashboard">Dashboard</router-link></li>
          <li><router-link to="/staff/products">Xem sản phẩm sản phẩm</router-link></li>
          <li><router-link to="/staff/users">Thông tin khách hàng</router-link></li>
          <li><router-link to="/staff/orders">Quản lý đơn hàng</router-link></li>
          <li><router-link to="/staff/promotions">Khuyến mãi</router-link></li>
          <li><router-link to="/staff/reports">Báo cáo</router-link></li>
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
.staff-sidebar {
  width: 220px;
  height: 100vh;
  background-color: #2c3e50;
  color: white;
  position: fixed;
  top: 0;
  left: 0;
  display: flex;
  flex-direction: column;
  justify-content: space-between; /* Chỉ dùng cho logout ở cuối */
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

</style>
