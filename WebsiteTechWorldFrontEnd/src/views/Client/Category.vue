<script setup>
import { ref, onMounted } from "vue";
import { getAllSanPham } from "@/Service/ClientService/ProductClientService";


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
  <div class="container mt-4">
    <h2>Category</h2>
    <table class="table table-striped">
      <thead>
        <tr>
          <th>ID</th>
          <th>Mã sản phẩm</th>
          <th>Tên sản phẩm</th>
          <th>Thương hiệu</th>
          <th>Số lượng tồn kho</th>
          <th>Nhà cung cấp</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="product in sanPhamList" :key="product.id">
          <td>{{ product.id }}</td>
          <td>{{ product.maSanPham }}</td>
          <td>{{ product.tenSanPham }}</td>
          <td>{{ product.thuongHieu }}</td>
          <td>{{ product.soLuongTonKho }}</td>
          <td>{{ product.tenNhaCungCap }}</td>
          <td>
            <RouterLink
              :to="`/products/${product.id}`"
              class="btn btn-primary btn-sm"
            >
              Chi tiết
            </RouterLink>
          </td>
        </tr>
      </tbody>
    </table>
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
