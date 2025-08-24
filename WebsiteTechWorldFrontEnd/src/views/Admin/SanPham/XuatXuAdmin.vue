<template>
  <div class="container mt-4">

    <el-row :gutter="20" class="mb-4 justify-content-center">
      <el-col :span="6">
        <el-input v-model="searchQuery" placeholder="Tìm kiếm" clearable />
      </el-col>
      <el-col :span="3">
        <el-button type="primary" @click="handleSearch" class="w-100">Tìm kiếm</el-button>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mb-2" justify="end">
      <el-col :span="3">
        <el-button type="primary" @click="handleCreate" class="w-100">Tạo mới</el-button>
      </el-col>
      <el-col :span="3">
        <el-button type="primary" @click="handleRefresh" class="w-100">Làm mới</el-button>
      </el-col>
    </el-row>
    <h2>Danh sách xuất xứ</h2>
    <div class="table-responsive mb-4" style="margin-top: 20px;">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column type="index" :index="indexMethod" label="STT" width="80" />
        <el-table-column prop="maXuatXu" label="Mã xuất xứ" />
        <el-table-column prop="tenQuocGia" label="Tên quốc gia" />
        <el-table-column label="Thao tác" width="180">
          <template #default="{ row }">
            <div class="action-buttons-horizontal">

              <el-button size="small" type="primary" :icon="Edit" circle @click="openDetail(row)" />

              <el-button size="small" type="danger" :icon="Delete" circle @click="handleDelete(row.id)" />
              <!-- 
              <router-link :to="`/admin/products/detail/${row.id}`">
                <el-button size="small" type="info" :icon="View" circle />
              </router-link> -->
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="pagination-fixed">
      <div class="d-flex justify-content-center align-items-center gap-3">
        <el-pagination background layout="prev, pager, next" :page-size="pageSize" :current-page="currentPage"
          :total="totalItems" :pager-count="7" prev-text="< Trước" next-text="Sau >"
          @current-change="handlePageChange" />
      </div>
    </div>

    <el-dialog v-model="dialogVisible" :title="isEditMode ? 'Chỉnh sửa xuất xứ' : 'Thêm mới xuất xứ'" width="900px"
      :close-on-click-modal="false" :destroy-on-close="true">
      <el-form :model="formData" ref="formRef" label-width="140px" label-position="left" class="hdh-form">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Mã xuất xứ" prop="maXuatXu" :error="errors.maXuatXu">
              <el-input v-model="formData.maXuatXu" placeholder="Nhập mã xuất xứ..." autocomplete="on" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="Tên quốc gia" prop="tenQuocGia" :error="errors.tenQuocGia">
              <el-input v-model="formData.tenQuocGia" placeholder="Nhập tên quốc gia..." />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row justify="end" style="margin-top: 30px;">
          <el-button @click="handleClose" style="margin-right: 10px;">Hủy</el-button>
          <el-button type="primary" @click="submitForm">{{ isEditMode ? 'Cập nhật' : 'Lưu' }}</el-button>
        </el-row>
      </el-form>
    </el-dialog>

  </div>
</template>


<script setup>
import { ref, onMounted, watch, computed, reactive } from 'vue';
import { deleteXuatXu, getAllXuatXuPage, postXuatXu, putXuatXu } from '@/Service/Adminservice/Products/ProductAdminService';
import { Edit, Delete, View } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus';
import { useToast } from "vue-toastification";
const toast = useToast();

const tableData = ref([]);
const currentPage = ref(1);
const totalPages = ref(1);
const totalItems = ref(0);
const pageSize = 5; // Số bản ghi trên 1 trang
const searchQuery = ref('');
const dialogVisible = ref(false);
const isEditMode = ref(false);
const formRef = ref(null);

// const rules = {
//   maXuatXu: [{ required: true, message: 'Vui lòng nhập mã xuất xứ', trigger: 'blur' }],
//   tenQuocGia: [{ required: true, message: 'Vui lòng nhập tên quốc gia', trigger: 'blur' }],
// };

const formData = reactive({
  id: null,
  maXuatXu: '',
  tenQuocGia: '',
})

const resetForm = () => {
  formData.id = null;
  formData.maXuatXu = '';
  formData.tenQuocGia = '';
  errors.maXuatXu = '';
  errors.tenQuocGia = ''
}

const resetErrors = () => {
  errors.maXuatXu = '';
  errors.tenQuocGia = ''
}

const errors = reactive({
  maXuatXu: '',
  tenQuocGia: ''
})


const loadData = async () => {
  try {
    const response = await getAllXuatXuPage(currentPage.value - 1, pageSize, searchQuery.value.trim());
    tableData.value = response.content;
    totalPages.value = response.totalPages;
    totalItems.value = response.totalElements;
  } catch (error) {
    console.error('Không thể load dữ liệu:', error);
  }
};

const submitForm = async () => {
  if (!formRef.value) return;

  try {
    // Hiện confirm trước khi gọi API
    const confirmMsg = isEditMode.value
      ? 'Bạn có chắc chắn muốn cập nhật xuất xứ này không?'
      : 'Bạn có chắc chắn muốn thêm mới xuất xứ này không?';

    await ElMessageBox.confirm(
      confirmMsg,
      'Xác nhận',
      {
        confirmButtonText: 'Đồng ý',
        cancelButtonText: 'Hủy',
        type: 'warning',
      }
    );

    // Nếu người dùng ấn Đồng ý → gọi API
    if (isEditMode.value) {
      await putXuatXu(formData.id, formData);
      toast.success('Cập nhật xuất xứ thành công!');
    } else {
      await postXuatXu(formData);
      toast.success('Thêm mới xuất xứ thành công!');
    }

    resetForm();
    dialogVisible.value = false;
    loadData();

  } catch (error) {
    // Nếu người dùng ấn "Hủy" thì ElMessageBox sẽ throw ra, mình cần phân biệt với lỗi API
    if (error === 'cancel' || error === 'close') {
      toast.info('Đã hủy thao tác');
      return;
    }

    errors.maXuatXu = error.message?.maXuatXu || '';
    errors.tenQuocGia = error.message?.tenQuocGia || '';

    if (error.response) {
      const status = error.response.status;
      const data = error.response.data;

      if (status === 400) {
        const msg = data.message;

        if (typeof msg === 'string') {
          toast.error(msg);
        } else if (typeof msg === 'object') {
          Object.keys(errors).forEach(key => {
            errors[key] = msg[key] || '';
          });

          const fieldErrors = Object.values(errors).filter(m => m);
          if (fieldErrors.length > 0) {
            toast.error(fieldErrors.join('\n'));
          } else {
            toast.error('Dữ liệu không hợp lệ!');
          }
        } else {
          toast.error('Dữ liệu không hợp lệ!');
        }
      } else if (status === 409) {
        toast.error(data.message || 'Dữ liệu đã tồn tại');
      } else if (status >= 500) {
        toast.error('Server lỗi, vui lòng thử lại sau');
      } else {
        toast.error(data.message || 'Đã xảy ra lỗi không xác định');
      }
    } else if (error.request) {
      toast.error('Không nhận được phản hồi từ server');
    } else {
      toast.error('Lỗi gửi request: ' + error.message);
    }
  }
};


const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm(
      'Bạn có chắc chắn muốn xoá xuất xứ này không?',
      'Xác nhận xoá',
      {
        confirmButtonText: 'Xoá',
        cancelButtonText: 'Huỷ',
        type: 'warning',
      }
    );
    await deleteXuatXu(id);
    toast.success('Xoá thành công!');
    loadData(); // tải lại danh sách sau khi xoá
  } catch (error) {
    if (error === 'cancel' || error === 'close') {
      toast.info('Đã hủy thao tác');
      return;
    }
  }
}

const fromRecord = computed(() => {
  return totalItems.value === 0 ? 0 : (currentPage.value - 1) * pageSize + 1;
});
const toRecord = computed(() => {
  return Math.min(currentPage.value * pageSize, totalItems.value);
});

onMounted(() => {
  loadData();
});

watch([currentPage, searchQuery], () => {
  loadData();
});

watch(
  () => formData.maXuatXu,
  (newValue) => {
    if (newValue) {
      errors.maXuatXu = ''; // Xóa lỗi khi người dùng nhập mã xuất xứ
    }
  }
);

watch(
  () => formData.tenQuocGia,
  (newValue) => {
    if (newValue) {
      errors.tenQuocGia = ''; // Xóa lỗi khi người dùng nhập tên quốc gia
    }
  }
);

const handleSearch = () => {
  currentPage.value = 1;
};

const handleRefresh = () => {
  searchQuery.value = '';
  currentPage.value = 1;
};

const handleCreate = () => {
  resetForm();
  dialogVisible.value = true;
  isEditMode.value = false;
}

const handleClose = () => {
  dialogVisible.value = false;
  resetForm()
}

const openDetail = (row) => {
  isEditMode.value = true;
  Object.assign(formData, row);
  dialogVisible.value = true;
  resetErrors()
};

const indexMethod = (index) => {
  return (currentPage.value - 1) * pageSize + index + 1;
};

const handlePageChange = (newPage) => {
  currentPage.value = newPage;
};
</script>


<style scoped>
.pagination-fixed {
  /* Thuộc tính bình thường, không fixed */
  margin-top: 20px;
  background: white;
  border-radius: 8px;
  padding: 16px 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  border: 1px solid #e5e7eb;
  display: flex;
  justify-content: center;
  align-items: center;
}



::v-deep(.el-pagination button) {
  font-size: 16px;
  padding: 8px 16px;
}

::v-deep(.el-pagination .el-pager li) {
  font-size: 16px;
  padding: 6px 10px;
}



* {
  box-sizing: border-box;
}

/* Dashboard Container */
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

/* ===== SEARCH SECTION ===== */
:deep(.el-row) {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 24px;
  margin-bottom: 24px;
  border: 1px solid #e5e7eb;
}

/* Input Styling */
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

/* ===== BUTTON STYLES ===== */
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

/* Add Product Section */
.mb-3 {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 20px 24px;
  border: 1px solid #e5e7eb;
}

/* ===== TITLE STYLING ===== */
h2 {
  font-size: 24px;
  font-weight: 600;
  margin: 0 0 24px 0;
  color: #1f2937;
  text-align: center;
  padding-bottom: 12px;
  border-bottom: 2px solid #e5e7eb;
}

/* ===== SIMPLE TABLE STYLING - NO STRIPE ===== */
:deep(.el-table) {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  border: 1px solid #e5e7eb;
}

:deep(.el-table__header th) {
  background: #f9fafb !important;
  color: #374151 !important;
  font-weight: 600;
  font-size: 14px;
  border-bottom: 1px solid #e5e7eb !important;
  padding: 12px 16px;
}

/* Remove all striped and hover effects */
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

/* Image Styling */
:deep(.el-table td img) {
  width: 50px !important;
  height: 50px !important;
  object-fit: cover;
  border-radius: 6px;
  border: 1px solid #e5e7eb;
}

/* ===== HORIZONTAL ACTION BUTTONS ===== */
.action-buttons-horizontal {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  flex-wrap: nowrap;
}

/* Action Buttons in Table */
:deep(.el-table td .el-button) {
  width: 32px;
  height: 32px;
  padding: 0;
  border-radius: 6px;
  flex-shrink: 0;
}

/* ===== PAGINATION SECTION ===== */
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

/* ===== SKELETON LOADING ===== */
:deep(.el-skeleton) {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 24px;
  border: 1px solid #e5e7eb;
}

/* ===== ALERT ===== */
:deep(.el-alert) {
  border-radius: 8px;
  margin-top: 24px;
}

/* ===== RESPONSIVE DESIGN ===== */
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
</style>