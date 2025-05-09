<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import LoginService from '@/Service/LoginService/Login.js'

// Biến lưu trạng thái đăng nhập
const isLoggedIn = ref<boolean>(false)
const router = useRouter()

// Kiểm tra trạng thái đăng nhập khi trang được tải
onMounted(async () => {
  try {
    await LoginService.getCurrentUser()  // Kiểm tra người dùng đã đăng nhập chưa
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
  <header class="admin-header">
    <div class="logo">
      <router-link to="/">Admin Dashboard</router-link>
    </div>
    <nav>
      <ul>
        <li><router-link to="/admin/dashboard">Dashboard</router-link></li>
        <li><router-link to="/admin/users">Quản lý người dùng</router-link></li>
        <li><router-link to="/admin/products">Quản lý sản phẩm</router-link></li>
        <li><router-link to="/admin/orders">Quản lý đơn hàng</router-link></li>
        <li><router-link to="/admin/promotions">Khuyến mãi</router-link></li>
        <li><router-link to="/admin/reports">Báo cáo</router-link></li>
        <li v-if="isLoggedIn">
          <a href="#" @click.prevent="handleLogout">Đăng xuất</a>
        </li>
        <li v-if="!isLoggedIn">
          <router-link to="/login">Đăng nhập</router-link>
        </li>
      </ul>
    </nav>
  </header>
</template>

<style scoped>
.admin-header {
  background-color: #333;
  color: white;
  padding: 10px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.admin-header .logo a {
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
  text-decoration: underline;
}
</style>
