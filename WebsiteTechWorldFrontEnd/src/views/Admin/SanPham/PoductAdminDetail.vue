<script setup>
import { ref, onMounted, watch } from "vue";
import { detailSanPham, getAllSanPham } from "@/Service/Adminservice/Products/ProductAdminService";
import { useRoute,useRouter } from "vue-router";

// Khai báo biến phản ứng
const isLoading = ref(false);
const error = ref("");

const route = useRoute();
const router = useRoute();
const id = route.params.id;
const sanPham = ref(null);

// Hàm tải sản phẩm
const loadDetail = async (id) => {
  try {
    isLoading.value = true;
    const response = await detailSanPham(id);
    sanPham.value = response;
    console.log('product: ', sanPham.value)
  } catch (err) {
    error.value = err.message || "Lỗi khi tải sản phẩm";
  } finally {
    isLoading.value = false;
  }
};


onMounted(() => {
  loadDetail(id);
});

// Watch để theo dõi khi id thay đổi, nếu thay đổi thì gọi lại hàm loadDetail
watch(() => route.params.id, (newId) => {
  loadDetail(newId);
});
</script>

<template>
  <div class="container mt-4">
    <h2>Chi tiết sản phẩm</h2>
    <form @submit.prevent="updateProduct" v-if="sanPham">
      <div class="mb-3">
        <label for="id" class="form-label">ID</label>
        <input type="text" id="id" class="form-control" v-model="sanPham.id" disabled />
      </div>
      <div class="mb-3">
        <label for="maSanPham" class="form-label">Mã sản phẩm</label>
        <input type="text" id="maSanPham" class="form-control" v-model="sanPham.maSanPham" />
      </div>
      <div class="mb-3">
        <label for="tenSanPham" class="form-label">Tên sản phẩm</label>
        <input type="text" id="tenSanPham" class="form-control" v-model="sanPham.tenSanPham" />
      </div>
      <div class="mb-3">
        <label for="thuongHieu" class="form-label">Thương hiệu</label>
        <input type="text" id="thuongHieu" class="form-control" v-model="sanPham.thuongHieu" />
      </div>
      <div class="mb-3">
        <label for="soLuongTonKho" class="form-label">Số lượng tồn kho</label>
        <input type="number" id="soLuongTonKho" class="form-control" v-model="sanPham.soLuongTonKho" />
      </div>
      <div class="mb-3">
        <label for="tenNhaCungCap" class="form-label">Nhà cung cấp</label>
        <input type="text" id="tenNhaCungCap" class="form-control" v-model="sanPham.tenNhaCungCap" />
      </div>
      <button type="submit" class="btn btn-primary">Cập nhật</button>
    </form>
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
