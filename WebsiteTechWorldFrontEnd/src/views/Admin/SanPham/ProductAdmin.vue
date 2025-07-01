<template>
  <div class="container mt-4">
    <el-row :gutter="20" class="mb-4 justify-content-center">
      <el-col :span="6">
        <el-input v-model="searchQuery" placeholder="Tìm kiếm sản phẩm" clearable @input="handleSearch" />
      </el-col>
      <el-col :span="3">
        <el-button type="primary" size="default" class="w-100" @click="handleSearch">Tìm kiếm</el-button>
      </el-col>
    </el-row>

    <div class="mb-3" style="display: flex; justify-content: flex-end; gap: 12px;">
      <router-link to="/admin/products/create" class="el-link--success">
        <el-button type="success" size="default">Thêm sản phẩm mới</el-button>
      </router-link>

      <router-link to="/admin/products/create/modelSanPham" class="el-link--success">
        <el-button type="success" size="default">Thêm mẫu sản phẩm</el-button>
      </router-link>
    </div>

    <h2>Danh sách sản phẩm</h2>
    <el-table :data="sanPhamList" style="width: 100%" border>
      <el-table-column type="index" :index="indexMethod" label="STT" width="80" />
      <el-table-column label="Hình ảnh" width="120">
        <template #default="{ row }">
          <img :src="row.url || '/placeholder.jpg'" alt="Ảnh sản phẩm" style="width: 60px; height: auto;" />
        </template>
      </el-table-column>
      <el-table-column prop="maSanPham" label="Mã sản phẩm" />
      <el-table-column prop="tenSanPham" label="Tên sản phẩm" />
      <el-table-column prop="tenLoai" label="Tên loại" />
      <el-table-column prop="soLuong" label="Số lượng" />
      <el-table-column label="Trạng thái">
        <template #default="{ row }">
          {{ trangThaiSanPhamMap[row.trangThaiSanPham] || 'Không rõ' }}
        </template>
      </el-table-column>
      <!-- <el-table-column prop="giaBan" label="Giá bán" />/ -->
      <el-table-column label="Thao tác" width="180">
        <template #default="{ row }">
          <div class="action-buttons-horizontal">
            <router-link :to="`/admin/products/${row.id}`">
              <el-button size="small" type="primary" :icon="Edit" circle />
            </router-link>
            <el-button size="small" type="danger" :icon="Delete" circle @click="handleDelete(row.id)" />
            <router-link :to="`/admin/products/view/${row.id}`">
              <el-button size="small" type="info" :icon="View" circle />
            </router-link>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-fixed">
      <div class="d-flex justify-content-center align-items-center gap-3" style="margin-top: 30px;">
        <el-pagination background layout="prev, pager, next" :page-size="pageSize" :current-page="currentPage"
          :total="totalItems" :pager-count="7" prev-text="< Trước" next-text="Sau >"
          @current-change="handlePageChange" />
      </div>
    </div>

    <el-alert v-if="error" :title="error" type="error" show-icon class="mt-3" />
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import { Edit, Delete, View } from '@element-plus/icons-vue';
import { getAllSanPham } from '@/Service/Adminservice/Products/ProductAdminService';
import { ElMessage, ElMessageBox } from 'element-plus';
import { debounce } from 'lodash'; // Cần cài đặt: npm install lodash

// State
const sanPhamList = ref([]);
const isFetching = ref(false); // Ngăn gọi API lặp
const error = ref('');
const currentPage = ref(1);
const totalPages = ref(1);
const totalItems = ref(0);
const pageSize = 5;
const searchQuery = ref('');

const loadSanPham = async () => {
  if (isFetching.value) return; // Ngăn gọi API lặp
  try {
    isFetching.value = true;
    console.log('Đang tải sản phẩm, trang:', currentPage.value, 'Tìm kiếm:', searchQuery.value);
    const response = await getAllSanPham(currentPage.value - 1, pageSize, searchQuery.value);
    console.log('Dữ liệu API:', response);
    sanPhamList.value = response.content || [];
    totalPages.value = response.totalPages || 1;
    totalItems.value = response.totalElements || 0;
  } catch (err) {
    error.value = err.message || 'Lỗi khi tải sản phẩm';
    console.error('Lỗi API:', err);
    ElMessage.error(error.value);
  } finally {
    isFetching.value = false;
  }
};

// Hàm tìm kiếm với debounce
const handleSearch = debounce(async () => {
  currentPage.value = 1; // Reset về trang 1 khi tìm kiếm
  await loadSanPham();
}, 300);

// Hàm xóa sản phẩm
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm(
      'Bạn có chắc chắn muốn xóa sản phẩm này?',
      'Xác nhận',
      {
        confirmButtonText: 'Xóa',
        cancelButtonText: 'Hủy',
        type: 'warning',
      }
    );
    // await deleteSanPham(id); // Bỏ comment khi có API xóa
    ElMessage.success('Xóa sản phẩm thành công');
    await loadSanPham(); // Tải lại danh sách sau khi xóa
  } catch (err) {
    console.log('Đã hủy xóa hoặc lỗi:', err);
    ElMessage.error('Lỗi khi xóa sản phẩm: ' + (err.message || 'Không rõ'));
  }
};

// Tính toán phân trang
const fromRecord = computed(() => {
  return totalItems.value === 0 ? 0 : (currentPage.value - 1) * pageSize + 1;
});

const toRecord = computed(() => {
  return Math.min(currentPage.value * pageSize, totalItems.value);
});

const indexMethod = (index) => {
  return (currentPage.value - 1) * pageSize + index + 1;
};

// Xử lý thay đổi trang
const handlePageChange = async (newPage) => {
  currentPage.value = newPage;
  await loadSanPham(); // Gọi loadSanPham khi chuyển trang
};

// Ánh xạ trạng thái sản phẩm
const trangThaiSanPhamMap = {
  ACTIVE: 'Đang kinh doanh',
  DISCONTINUED: 'Ngừng kinh doanh',
  COMING_SOON: 'Sắp ra mắt',
  TEMPORARILY_UNAVAILABLE: 'Tạm ngừng bán',
  OUT_OF_STOCK: 'Hết hàng',
};

// Theo dõi thay đổi tìm kiếm
watch(searchQuery, () => {
  handleSearch();
});

// Tải dữ liệu khi component được mount
onMounted(() => {
  loadSanPham();
});
</script>

<style scoped>
/* Giữ style gốc, nhưng bỏ transition để tránh nhảy */
* {
  box-sizing: border-box;
}

.container {
  min-height: 100vh;
  background: #f8f9fa;
  padding: 24px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  max-width: 1400px;
  margin: 0 auto;
}

.mt-4 {
  margin-top: 24px;
}

.mb-4 {
  margin-bottom: 32px;
}

.mb-3 {
  margin-bottom: 24px;
}

.w-100 {
  width: 100%;
}

:deep(.el-row) {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 24px;
  margin-bottom: 24px;
  border: 1px solid #e5e7eb;
}

:deep(.el-input__wrapper) {
  border: 1px solid #d1d5db;
  border-radius: 6px;
  background: white;
  transition: border-color 0.2s ease;
}

:deep(.el-input__wrapper:hover) {
  border-color: #9ca3af;
}

:deep(.el-input__wrapper.is-focus) {
  border-color: #3b82f6;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.1);
}

:deep(.el-input__inner) {
  font-size: 14px;
  color: #374151;
}

:deep(.el-button) {
  border-radius: 6px;
  font-weight: 500;
  transition: all 0.2s ease;
  border: 1px solid;
}

:deep(.el-button:hover) {
  transform: translateY(-1px);
}

:deep(.el-button--primary) {
  background: #3b82f6;
  border-color: #3b82f6;
  color: white;
}

:deep(.el-button--primary:hover) {
  background: #2563eb;
  border-color: #2563eb;
}

:deep(.el-button--success) {
  background: #10b981;
  border-color: #10b981;
  color: white;
}

:deep(.el-button--success:hover) {
  background: #059669;
  border-color: #059669;
}

:deep(.el-button--danger) {
  background: #ef4444;
  border-color: #ef4444;
  color: white;
}

:deep(.el-button--danger:hover) {
  background: #dc2626;
  border-color: #dc2626;
}

:deep(.el-button--info) {
  background: #6b7280;
  border-color: #6b7280;
  color: white;
}

:deep(.el-button--info:hover) {
  background: #4b5563;
  border-color: #4b5563;
}

.mb-3 {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 20px 24px;
  border: 1px solid #e5e7eb;
}

h2 {
  font-size: 24px;
  font-weight: 600;
  margin: 0 0 24px 0;
  color: #1f2937;
  text-align: center;
  padding-bottom: 12px;
  border-bottom: 2px solid #e5e7eb;
}

:deep(.el-table) {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  border: 1px solid #e5e7eb;
  /* Bỏ transition để tránh nhảy */
}

:deep(.el-table__header th) {
  background: #f9fafb !important;
  color: #374151 !important;
  font-weight: 600;
  font-size: 14px;
  border-bottom: 1px solid #e5e7eb !important;
  padding: 12px 16px;
}

:deep(.el-table__body tr) {
  background: white !important;
}

:deep(.el-table__body tr:hover) {
  background: white !important;
}

:deep(.el-table__body tr.el-table__row--striped) {
  background: white !important;
}

:deep(.el-table__body tr.el-table__row--striped:hover) {
  background: white !important;
}

:deep(.el-table td) {
  padding: 12px 16px;
  font-size: 14px;
  color: #374151;
  border-color: #f3f4f6;
  background: white !important;
}

:deep(.el-table td img) {
  width: 50px !important;
  height: 50px !important;
  object-fit: cover;
  border-radius: 6px;
  border: 1px solid #e5e7eb;
}

.action-buttons-horizontal {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  flex-wrap: nowrap;
}

:deep(.el-table td .el-button) {
  width: 32px;
  height: 32px;
  padding: 0;
  border-radius: 6px;
  flex-shrink: 0;
}

.pagination-fixed {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-top: 24px;
  border: 1px solid #e5e7eb;
}

.d-flex {
  display: flex;
}

.justify-content-center {
  justify-content: center;
}

.align-items-center {
  align-items: center;
}

.gap-3 {
  gap: 12px;
}

:deep(.el-pagination button) {
  background: white;
  border: 1px solid #d1d5db;
  color: #374151;
  font-size: 14px;
  border-radius: 6px;
  margin: 0 2px;
  padding: 6px 12px;
}

:deep(.el-pagination button:hover) {
  background: #f3f4f6;
  border-color: #9ca3af;
}

:deep(.el-pagination .el-pager li) {
  background: white;
  border: 1px solid #d1d5db;
  color: #374151;
  font-size: 14px;
  border-radius: 6px;
  margin: 0 2px;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
}

:deep(.el-pagination .el-pager li:hover) {
  background: #f3f4f6;
  border-color: #9ca3af;
}

:deep(.el-pagination .el-pager li.is-active) {
  background: #3b82f6;
  border-color: #3b82f6;
  color: white;
}

:deep(.el-alert) {
  border-radius: 8px;
  margin-top: 24px;
}

@media (max-width: 768px) {
  .container {
    padding: 16px;
  }

  h2 {
    font-size: 20px;
  }

  :deep(.el-row) {
    padding: 16px;
  }

  :deep(.el-table td) {
    padding: 8px 12px;
    font-size: 13px;
  }

  :deep(.el-table td img) {
    width: 40px !important;
    height: 40px !important;
  }

  .action-buttons-horizontal {
    gap: 4px;
  }

  :deep(.el-table td .el-button) {
    width: 28px;
    height: 28px;
  }

  .pagination-fixed {
    padding: 16px;
  }

  :deep(.el-pagination button) {
    padding: 4px 8px;
    font-size: 12px;
  }

  :deep(.el-pagination .el-pager li) {
    width: 32px;
    height: 32px;
    font-size: 12px;
  }
}

ul {
  list-style: none;
  padding: 0;
}

li {
  padding: 1rem;
  border-bottom: 1px solid #e5e7eb;
  font-size: 1.1rem;
}

.error {
  color: #dc2626;
  font-size: 1rem;
}

:deep(.el-link) {
  text-decoration: none;
}

:deep(.el-link:hover) {
  text-decoration: none;
}
</style>