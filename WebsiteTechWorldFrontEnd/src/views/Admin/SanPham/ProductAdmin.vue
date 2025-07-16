```vue
<template>
  <div class="page-wrapper">
    <!-- Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <el-icon class="header-icon">
            <Goods />
          </el-icon>
          <div class="header-text">
            <h1 class="header-title">Quản lý sản phẩm</h1>
            <p class="header-subtitle">Danh sách và quản lý tất cả sản phẩm</p>
          </div>
        </div>
        <div class="header-right">
          <el-tag type="success" size="large" v-if="totalItems > 0">
            {{ totalItems }} sản phẩm
          </el-tag>
        </div>
      </div>
    </div>

    <div class="container mt-4">
      <!-- Tiêu đề cho bộ lọc -->
      <h2 class="filter-title">
        <el-icon class="title-icon">
          <Filter />
        </el-icon>
        Bộ lọc sản phẩm
      </h2>

      <el-row :gutter="20" class="mb-4 justify-content-center">
        <el-col :span="6">
          <el-input v-model="searchQuery" placeholder="Tìm kiếm sản phẩm" clearable @input="handleSearch"
            class="search-input">
            <template #prefix>
              <el-icon class="search-icon">
                <Search />
              </el-icon>
            </template>
          </el-input>
        </el-col>
        <el-col :span="6">
          <el-select v-model="filters.idLoai" placeholder="Lọc theo loại" clearable @change="handleSearch"
            class="filter-select">
            <el-option v-for="loai in loais" :key="loai.id" :label="loai.tenLoai" :value="loai.id" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select v-model="filters.trangThai" placeholder="Lọc theo trạng thái" clearable @change="handleSearch"
            class="filter-select">
            <el-option v-for="trangThaiSP in trangThaiSanPhams" :key="trangThaiSP.value" :label="trangThaiSP.label"
              :value="trangThaiSP.value" />
          </el-select>
        </el-col>
        <el-col :span="3">
          <el-button type="primary" size="default" class="w-100 search-btn" @click="handleSearch" :loading="isFetching">
            <el-icon>
              <Search />
            </el-icon>
            Tìm kiếm
          </el-button>
        </el-col>
        <el-col :span="3">
          <el-button type="default" size="default" class="w-100 reset-btn" @click="handleReset" :disabled="isFetching">
            <el-icon>
              <Refresh />
            </el-icon>
            Đặt lại
          </el-button>
        </el-col>
      </el-row>
      <!-- v-if="isAdmin" -->

      <div class="mb-3 action-section" >
        <router-link to="/admin/products/create" class="el-link--success">
          <el-button type="success" size="default" class="action-btn">
            <el-icon>
              <Plus />
            </el-icon>
            Thêm sản phẩm mới
          </el-button>
        </router-link>
        <router-link to="/admin/products/create/modelSanPham" class="el-link--success">
          <el-button type="success" size="default" plain class="action-btn">
            <el-icon>
              <DocumentAdd />
            </el-icon>
            Thêm mẫu sản phẩm
          </el-button>
        </router-link>
      </div>

      <h2 class="page-title">
        <el-icon class="title-icon">
          <Goods />
        </el-icon>
        Danh sách sản phẩm
      </h2>

      <el-table :data="sanPhamList" style="width: 100%" border stripe :loading="isFetching" class="beautiful-table"
        empty-text="Không có dữ liệu">
        <el-table-column type="index" :index="indexMethod" label="STT" width="80" align="center" />

        <el-table-column label="Hình ảnh" width="120" align="center">
          <template #default="{ row }">
            <div class="image-container">
              <el-image :src="row.url || '/placeholder.svg?height=60&width=60'" alt="Ảnh sản phẩm" class="product-image"
                fit="cover" :preview-src-list="[row.url || '/placeholder.svg?height=60&width=60']" preview-teleported>
                <template #error>
                  <div class="image-error">
                    <el-icon>
                      <Picture />
                    </el-icon>
                  </div>
                </template>
              </el-image>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="maSanPham" label="Mã sản phẩm">
          <template #default="{ row }">
            <el-tag type="info" effect="light" class="code-tag">
              {{ row.maSanPham }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="tenSanPham" label="Tên sản phẩm" show-overflow-tooltip>
          <template #default="{ row }">
            <span class="product-name">{{ row.tenSanPham }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="tenLoai" label="Tên loại">
          <template #default="{ row }">
            <el-tag size="small" effect="light" class="category-tag">
              {{ row.tenLoai }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="soLuong" label="Số lượng" align="center">
          <template #default="{ row }">
            <el-tag :type="row.soLuong > 10 ? 'success' : row.soLuong > 0 ? 'warning' : 'danger'" effect="light"
              class="quantity-tag">
              {{ row.soLuong }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="Trạng thái" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.trangThaiSanPham)" effect="light" class="status-tag">
              <el-icon class="status-icon">
                <component :is="getStatusIcon(row.trangThaiSanPham)" />
              </el-icon>
              {{ trangThaiSanPhamMap[row.trangThaiSanPham] || 'Không rõ' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="Thao tác" width="180" align="center">
          <template #default="{ row }">
            <div class="action-buttons-horizontal">
              <router-link :to="`/admin/products/view/${row.id}`">
                <el-tooltip content="Xem chi tiết" placement="top">
                  <el-button size="small" type="info" :icon="View" class="action-btn-square" />
                </el-tooltip>
              </router-link>
              <router-link :to="`/admin/products/${row.id}`">
                <el-tooltip content="Chỉnh sửa" placement="top">
                  <el-button size="small" type="primary" :icon="Edit" class="action-btn-square" />
                </el-tooltip>
              </router-link>
              <el-tooltip content="Xóa sản phẩm" placement="top">
                <el-button size="small" type="danger" :icon="Delete" @click="handleDelete(row.id)"
                  class="action-btn-square" />
              </el-tooltip>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-fixed">
        <!-- <div class="pagination-info">
          <el-tag type="info" effect="light" class="info-tag">
            Hiển thị {{ fromRecord }} - {{ toRecord }} trong tổng số {{ totalItems }} sản phẩm
          </el-tag>
        </div> -->
        <div class="d-flex justify-content-center align-items-center gap-3">
          <el-pagination background layout="prev, pager, next" :page-size="pageSize" :current-page="currentPage"
            :total="totalItems" :pager-count="7" prev-text="< Trước" next-text="Sau >"
            @current-change="handlePageChange" class="simple-pagination" />
        </div>
      </div>

      <el-alert v-if="error" :title="error" type="error" show-icon class="mt-3 error-alert" closable />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch, reactive } from 'vue';
import { Edit, Delete, View, Search, Plus, DocumentAdd, Goods, Picture, CircleCheck, Warning, Clock, CircleClose, Minus, Filter, Refresh } from '@element-plus/icons-vue';
import { getAllSanPham, getAllLoaiList, getTrangThaiSanPham, deleteSanPham } from '@/Service/Adminservice/Products/ProductAdminService';
import { ElMessage, ElMessageBox } from 'element-plus';
import { debounce } from 'lodash';
import store from '@/Service/LoginService/Store';
// import { add, debounce } from 'lodash'; // Cần cài đặt: npm install lodash
// import store from '@/Service/LoginService/Store';


// State
const sanPhamList = ref([]);
const isFetching = ref(false);
const error = ref('');
const currentPage = ref(1);
const totalPages = ref(1);
const totalItems = ref(0);
const pageSize = 5;
const searchQuery = ref('');
const loais = ref([]);
const trangThaiSanPhams = ref([]);
const filters = reactive({
  idLoai: null,
  trangThai: ''
});


// Tải danh sách loại sản phẩm
const loadLoaiSanPham = async () => {
  try {
    const response = await getAllLoaiList();
    loais.value = response || [];
  } catch (err) {
    error.value = err.message || 'Lỗi khi tải loại sản phẩm';
    ElMessage.error(error.value);
  }
};

const loadTrangThaiSanPham = async () => {
  try {
    const response = await getTrangThaiSanPham();
    trangThaiSanPhams.value = response || [];
    console.log("111111111111", response);
  } catch (err) {
    error.value = err.message || 'Lỗi khi tải trạng thái sản phẩm';
    ElMessage.error(error.value);
  }
};

// Tải danh sách sản phẩm
const loadSanPham = async () => {
  if (isFetching.value) return;

  try {
    isFetching.value = true;
    console.log('Đang tải sản phẩm, trang:', currentPage.value, 'Tìm kiếm:', searchQuery.value, 'Loại:', filters.idLoai, 'Trạng thái:', filters.trangThai);

    const response = await getAllSanPham(currentPage.value - 1, pageSize, searchQuery.value, filters.idLoai, filters.trangThai);

    console.log('Đang tải sản phẩm, trang22222:', currentPage.value, 'Tìm kiếm:', searchQuery.value, 'Loại:', filters.idLoai, 'Trạng thái:', filters.trangThai);

    console.log('Dữ liệu trả về từ API:', response);

    sanPhamList.value = response.content || [];
    totalPages.value = response.totalPages || 1;
    totalItems.value = response.totalElements || 0;
  } catch (err) {
    error.value = err.message || 'Lỗi khi tải sản phẩm';
    ElMessage.error(error.value);
  } finally {
    isFetching.value = false;
  }
};

// Hàm tìm kiếm với debounce
const handleSearch = debounce(async () => {
  currentPage.value = 1;
  await loadSanPham();
}, 300);

// Hàm đặt lại bộ lọc
const handleReset = async () => {
  searchQuery.value = '';
  filters.idLoai = null;
  filters.trangThai = '';
  currentPage.value = 1;
  await loadSanPham();
};

// Hàm xóa sản phẩm
const handleDelete = async (id) => {
  console.log('ID sản phẩm:', id);
  if (!id) {
    ElMessage.error('ID sản phẩm không hợp lệ');
    return;
  }
  try {
    await ElMessageBox.confirm(
      'Bạn có chắc chắn muốn tạm ngừng bán sản phẩm này?',
      'Xác nhận',
      {
        confirmButtonText: 'Xóa',
        cancelButtonText: 'Hủy',
        type: 'warning',
      }
    );
    await deleteSanPham(id);
    ElMessage.success('Sản phẩm tạm ngừng bán !!!');
    await loadSanPham();
  } catch (err) {
    console.error('Lỗi khi xóa:', err);
    if (err !== 'cancel') {
      ElMessage.error('Lỗi khi xóa sản phẩm: ' + (err.message || 'Không rõ'));
    }
  }
};
// Get status type and icon
const getStatusType = (status) => {
  const statusTypes = {
    ACTIVE: 'success',
    DISCONTINUED: 'danger',
    COMING_SOON: 'warning',
    TEMPORARILY_UNAVAILABLE: 'info',
    OUT_OF_STOCK: 'danger'
  };
  return statusTypes[status] || 'info';
};

const getStatusIcon = (status) => {
  const statusIcons = {
    ACTIVE: CircleCheck,
    DISCONTINUED: CircleClose,
    COMING_SOON: Clock,
    TEMPORARILY_UNAVAILABLE: Warning,
    OUT_OF_STOCK: Minus
  };
  return statusIcons[status] || Warning;
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

const handlePageChange = (newPage) => {
  currentPage.value = newPage;
  loadSanPham();
};


// Ánh xạ trạng thái sản phẩm
const trangThaiSanPhamMap = {
  ACTIVE: 'Đang kinh doanh',
  DISCONTINUED: 'Ngừng kinh doanh',
  COMING_SOON: 'Sắp ra mắt',
  TEMPORARILY_UNAVAILABLE: 'Tạm ngừng bán',
  OUT_OF_STOCK: 'Hết hàng',
};

const isAdmin = computed(() => {
  const roles = store.state.roles;
  return (
    Array.isArray(roles) &&
    roles
      .map((role) => (typeof role === "string" ? role : role.authority))
      .includes("ROLE_ADMIN")
  );
});

const isStaff = computed(() => {
  const roles = store.state.roles;
  return (
    Array.isArray(roles) &&
    roles
      .map((role) => (typeof role === "string" ? role : role.authority))
      .includes("ROLE_STAFF")
  );
});
// Theo dõi thay đổi tìm kiếm
watch(searchQuery, () => {
  handleSearch();
});

// Tải dữ liệu khi component được mount
onMounted(async () => {
  await Promise.all([loadTrangThaiSanPham(), loadLoaiSanPham(), loadSanPham()]);
});
</script>

<style scoped>
/* Page Wrapper */
.page-wrapper {
  min-height: 100vh;
  background-color: #f8f9fa;
}

/* Header Styling */
.page-header {
  background: white;
  border-bottom: 1px solid #e9ecef;
  padding: 20px 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-icon {
  font-size: 40px;
  color: #409eff;
  background: #ecf5ff;
  padding: 12px;
  border-radius: 12px;
}

.header-text {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.header-title {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.header-subtitle {
  margin: 0;
  font-size: 14px;
  color: #909399;
}

.header-right {
  display: flex;
  align-items: center;
}

/* Container và Layout */
.container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 24px;
  font-family: 'Inter', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
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

/* Filter Title */
.filter-title {
  font-size: 20px;
  font-weight: 600;
  margin: 0 0 16px 0;
  color: #303133;
  display: flex;
  align-items: center;
  gap: 8px;
}

/* Filter Row Styling */
:deep(.el-row) {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 24px;
  margin-bottom: 24px;
  border: 1px solid #e9ecef;
}

/* Input và Select Styling */
.search-input :deep(.el-input__wrapper) {
  border-radius: 6px;
  border: 1px solid #dcdfe6;
  background: white;
  transition: all 0.3s ease;
}

.search-input :deep(.el-input__wrapper:hover) {
  border-color: #409eff;
}

.search-input :deep(.el-input__wrapper.is-focus) {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.search-icon {
  color: #909399;
}

.filter-select :deep(.el-input__wrapper) {
  border-radius: 6px;
  border: 1px solid #dcdfe6;
  background: white;
  transition: all 0.3s ease;
}

.filter-select :deep(.el-input__wrapper:hover) {
  border-color: #409eff;
}

/* Button Styling */
.search-btn,
.reset-btn {
  border-radius: 6px;
  font-weight: 500;
  transition: all 0.3s ease;
}

/* Action Section */
.action-section {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 20px 24px;
  border: 1px solid #e9ecef;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.action-btn {
  border-radius: 6px;
  font-weight: 500;
  transition: all 0.3s ease;
  padding: 10px 20px;
}

/* Page Title */
.page-title {
  font-size: 20px;
  font-weight: 600;
  margin: 24px 0;
  color: #303133;
  text-align: center;
  padding: 16px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  border: 1px solid #e9ecef;
}

.title-icon {
  font-size: 24px;
  color: #409eff;
}

/* Table Styling - Chỉ làm đậm chữ */
.beautiful-table {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border: 1px solid #e9ecef;
}

:deep(.el-table__header th) {
  background-color: #f5f7fa !important;
  color: #606266 !important;
  font-weight: 700 !important;
  border-bottom: 1px solid #ebeef5;
}

:deep(.el-table__body tr:hover) {
  background-color: #f5f7fa !important;
}

:deep(.el-table td) {
  padding: 12px;
  font-size: 14px;
  color: #303133 !important;
  border-color: #ebeef5;
  font-weight: 600 !important;
}

/* Image Container */
.image-container {
  display: flex;
  justify-content: center;
  align-items: center;
}

.product-image {
  width: 60px;
  height: 60px;
  border-radius: 6px;
  border: 1px solid #dcdfe6;
  transition: all 0.3s ease;
  cursor: pointer;
}

.product-image:hover {
  transform: scale(1.05);
  border-color: #409eff;
}

.image-error {
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  border-radius: 6px;
  color: #c0c4cc;
}

/* Tags Styling */
.code-tag {
  border-radius: 4px;
  font-weight: 700 !important;
  color: #1f2937 !important;
  font-size: 13px !important;
}

.category-tag {
  border-radius: 4px;
  font-weight: 700 !important;
  color: #166a51 !important;
  font-size: 14px !important;
}

.quantity-tag {
  border-radius: 4px;
  font-weight: 700 !important;
  color: #0b2f24 !important;
  min-width: 40px;
  font-size: 14px !important;
}

.status-tag {
  border-radius: 4px;
  font-weight: 700 !important;
  display: flex;
  align-items: center;
  color: #0b2f24 !important;
  gap: 4px;
  font-size: 12px !important;
}

.product-name {
  font-weight: 700 !important;
  color: #1f2937 !important;
  font-size: 14px !important;
}

/* Action Buttons - Square with rounded corners */
.action-buttons-horizontal {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  flex-wrap: nowrap;
}

.action-btn-square {
  width: 32px;
  height: 32px;
  border-radius: 6px;
  transition: all 0.3s ease;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.action-btn-square:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

/* Pagination */
.pagination-fixed {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-top: 24px;
  border: 1px solid #e9ecef;
}

.pagination-info {
  text-align: center;
  margin-bottom: 16px;
}

.info-tag {
  font-size: 14px;
  padding: 6px 12px;
  border-radius: 4px;
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

.simple-pagination :deep(.el-pagination button) {
  background: white;
  border: 1px solid #dcdfe6;
  color: #606266;
  font-size: 14px;
  border-radius: 4px;
  margin: 0 2px;
  padding: 6px 12px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.simple-pagination :deep(.el-pagination button:hover) {
  background: #409eff;
  color: white;
  border-color: #409eff;
}

.simple-pagination :deep(.el-pager li) {
  background: white;
  border: 1px solid #dcdfe6;
  color: #606266;
  font-size: 14px;
  border-radius: 4px;
  margin: 0 2px;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 500;
  transition: all 0.3s ease;
}

.simple-pagination :deep(.el-pager li:hover) {
  background: #409eff;
  color: white;
  border-color: #409eff;
}

.simple-pagination :deep(.el-pager li.is-active) {
  background: #409eff;
  border-color: #409eff;
  color: white;
}

/* Error Alert */
.error-alert {
  border-radius: 6px;
  margin-top: 24px;
}

/* Responsive Design */
@media (max-width: 768px) {
  .header-content {
    padding: 0 16px;
    flex-direction: column;
    gap: 16px;
    text-align: center;
  }

  .header-left {
    flex-direction: column;
    gap: 12px;
  }

  .container {
    padding: 16px;
  }

  .filter-title {
    font-size: 18px;
  }

  .page-title {
    font-size: 18px;
    flex-direction: column;
    gap: 8px;
  }

  .action-section {
    flex-direction: column;
    gap: 12px;
  }

  .action-btn {
    width: 100%;
  }

  :deep(.el-table td) {
    padding: 8px;
    font-size: 13px;
  }

  .product-image {
    width: 50px;
    height: 50px;
  }

  .action-buttons-horizontal {
    gap: 6px;
  }

  .action-btn-square {
    width: 28px;
    height: 28px;
  }

  .pagination-fixed {
    padding: 16px;
  }
}

/* Link Styling */
:deep(.el-link) {
  text-decoration: none;
}

:deep(.el-link:hover) {
  text-decoration: none;
}
</style>
```