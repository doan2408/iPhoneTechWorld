<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import LoginService from '@/Service/LoginService/Login.js'

// Biến lưu trạng thái đăng nhập
const isLoggedIn = ref<boolean>(false)
const router = useRouter()
const user = ref<{fullName : String } | null>(null)

// Kiểm tra trạng thái đăng nhập khi trang được tải
onMounted(async () => {
  // Kiểm tra trạng thái đăng nhập từ localStorage trước
  const storedLoginStatus = localStorage.getItem('isLoggedIn');
  
  if (storedLoginStatus === 'true') {
    // Nếu có trong localStorage, cố gắng lấy thông tin người dùng
    try {
      const currentUser = await LoginService.getCurrentUser();  // Lấy thông tin người dùng từ API
      isLoggedIn.value = true;  // Người dùng đã đăng nhập
      user.value = currentUser

    } catch (err) {
      // Nếu có lỗi (người dùng đã đăng nhập nhưng phiên hết hạn chẳng hạn)
      isLoggedIn.value = false;  // Đánh dấu người dùng không đăng nhập
      localStorage.removeItem('isLoggedIn');  // Xóa trạng thái đăng nhập không hợp lệ
    }
  } else {
    // Nếu không có trạng thái đăng nhập trong localStorage
    isLoggedIn.value = false;  // Đánh dấu người dùng chưa đăng nhập
  }
});

// Xử lý đăng xuất
const handleLogout = async () => {
  try {
    await LoginService.logout();  // Gọi API đăng xuất
    isLoggedIn.value = false;
    localStorage.removeItem('isLoggedIn');  // Xóa trạng thái đăng nhập khỏi localStorage
    router.push('/login');  // Điều hướng về trang đăng nhập
  } catch (err) {
    console.error('Lỗi đăng xuất:', err)
  }
};
</script>



<template>
  <header class="client-header">
    <div class="logo">
      <router-link to="/home">Trang chủ</router-link>
    </div>
    <nav>
      <ul>
        <li><router-link to="/client/category">Danh mục</router-link></li>
        <li><router-link to="/client/users">Lọc Theo Giá</router-link></li>
        <li><router-link to="/client/products">Bạn muốn tìm gì</router-link></li>
        <li><router-link to="/client/products">Đơn mua của tôi</router-link></li>
        <li><router-link to="/client/orders">Tra cứu đơn hàng</router-link></li>
        <li><router-link to="/client/promotions">Giỏ hàng</router-link></li>
        <li class="username">👤 {{ user?.fullName }}</li>
        <!-- Chỉ hiển thị nút Đăng xuất nếu người dùng đã đăng nhập -->
        <li v-if="isLoggedIn">
          <a href="#" @click.prevent="handleLogout">Đăng xuất</a>
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
  background-color: #333;
  color: white;
  padding: 10px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
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
  text-decoration: underline;
}

</style>
