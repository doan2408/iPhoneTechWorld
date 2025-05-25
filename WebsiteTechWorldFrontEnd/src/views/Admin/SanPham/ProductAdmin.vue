<script setup>
import { ref, onMounted } from "vue";
import { getAllSanPham } from "@/Service/Adminservice/Products/ProductAdminService";

// Khai báo biến phản ứng
const sanPhamList = ref([]);
const isLoading = ref(false);
const error = ref("");

const currentPage = ref(0); // Trang hiện tại
const totalPages = ref(0); // Tổng số trang

// Hàm tải sản phẩm
// nếu người dùng không truyền page khi gọi hàm
//  → nó sẽ tự động dùng 0.
const loadSanPham = async (page = 0) => {
  try {
    isLoading.value = true;
    const response = await getAllSanPham(page);
    sanPhamList.value = response;
    totalPages.value = response.totalPages;
    currentPage.value = page;
  } catch (err) {
    error.value = err.message || "Lỗi khi tải sản phẩm";
  } finally {
    isLoading.value = false;
  }
};

// Chuyển đến trang kế tiếp
const nextPage = () => {
  if (currentPage.value < totalPages.value - 1) {
    loadSanPham(currentPage.value + 1); // Tải dữ liệu trang kế tiếp
  } else if (currentPage.value == totalPages.value - 1) {
    loadSanPham((currentPage.value = 0));
  }
};

// Chuyển về trang trước
const previousPage = () => {
  if (currentPage.value > 0) {
    loadSanPham(currentPage.value - 1); // Tải dữ liệu trang trước
  } else if (currentPage.value == 0) {
    loadSanPham((currentPage.value = totalPages.value - 1));
  }
};

onMounted(() => {
  loadSanPham();
});
</script>

<template>

  <div class="container mt-4">

    <div class="mb-3">
      <RouterLink to="/admin/products/create" class="btn btn-success">
        Thêm sản phẩm mới
      </RouterLink>
    </div>

    <!-- Hiển thị khi đang tải -->
    <div v-if="isLoading" class="text-center">
      <p>Đang tải dữ liệu...</p>
    </div>

    <h2>Danh sách sản phẩm</h2>
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
            <RouterLink :to="`/admin/products/${product.id}`" class="btn btn-primary btn-sm">
              Update
            </RouterLink>
          </td>
        </tr>
      </tbody>
    </table>
    <!-- Nút phân trang -->
    <div class="pagination">
      <button @click="previousPage">Trang trước</button>
      <span>Trang {{ currentPage + 1 }} / {{ totalPages }}</span>
      <button @click="nextPage">Trang sau</button>
    </div>
  </div>
</template>

<style scoped>
.container {
  margin-left: 215px;
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

