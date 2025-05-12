<script setup>
import { ref, onMounted } from "vue";
import { getAllSanPham } from "@/Service/Adminservice/ProductAdminService";

// Khai báo biến phản ứng
const sanPhamList = ref([]);
const isLoading = ref(false);
const error = ref("");

// Hàm tải sản phẩm
const loadSanPham = async () => {
  try {
    isLoading.value = true;
    const data = await getAllSanPham();
    sanPhamList.value = Array.isArray(data) ? data : []; // Đảm bảo data là mảng
  } catch (err) {
    error.value = err.message || "Lỗi khi tải sản phẩm";
  } finally {
    isLoading.value = false;
  }
};

onMounted(() => {
  loadSanPham();
});
</script>

<!-- src/components/Product.vue -->
<template>
  <div class="product-container">
    <h1>Danh sách sản phẩm</h1>
    <div v-if="isLoading">Đang tải...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <ul v-else-if="sanPhamList.length > 0">
      <li v-for="(sanPham, index) in sanPhamList" :key="sanPham.id || index">
        {{ sanPham.tenSanPham }} - {{ sanPham.thuongHieu }} (Số lượng:
        {{ sanPham.soLuongTonKho }})
      </li>
    </ul>
    <p v-else>Không có sản phẩm nào.</p>
  </div>
</template>



<style scoped>
.product-container {
  max-width: 800px;
  margin: 2rem auto;
  padding: 2rem;
  text-align: center;
}
ul {
  list-style: none;
  padding: 0;
}
li {
  padding: 1rem;
  border-bottom: 1px solid #ddd;
  font-size: 1.1rem;
}
.error {
  color: red;
  font-size: 1rem;
}
</style>
