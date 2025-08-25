<template>
  <div class="container mt-4">
    <el-row :gutter="20" class="mb-4" align="middle">
      <!-- Thanh tìm kiếm căn trái -->
      <el-col :span="8">
        <el-input v-model="searchKeyword" placeholder="Tìm kiếm theo tên, loại màn hình, tần số quét..." clearable
          prefix-icon="Search" @clear="clearSearch" />
      </el-col>

      <!-- Các nút căn phải -->
      <el-col :span="16" class="text-right" align="right">
        <el-button type="primary" @click="handleCreate">Tạo mới</el-button>
        <el-button type="primary" @click="handleRefresh">Làm mới</el-button>
      </el-col>
    </el-row>

    <h2>Danh sách cpu</h2>
    <div class="table-responsive mb-4" style="margin-top: 20px;">
      <el-table :data="tableCpu" border style="width: 100%">
        <el-table-column type="index" :index="indexMethod" label="STT" width="80" />
        <!-- <el-table-column prop="id" label="ID" /> -->
        <el-table-column prop="chipXuLy" label="Chip xử lý" />
        <el-table-column prop="hangSanXuat" label="Hãng sản xuất" />
        <el-table-column prop="soNhan" label="Số nhân" />
        <el-table-column prop="xungNhip" label="Xung nhịp" />
        <el-table-column prop="congNgheSanXuat" label="Công nghệ sản xuất" />
        <el-table-column prop="boNhoDem" label="Bộ nhớ đệm" />
        <el-table-column prop="tieuThuDienNang" label="Tiêu thụ điện năng" />
        <el-table-column prop="namRaMat" label="Năm ra mắt" />
        <el-table-column label="Thao tác" width="180">
          <template #default="{ row }">
            <div class="action-buttons-horizontal">
              <el-button size="small" type="primary" :icon="icons.Edit" circle @click="openDetail(row)" />
              <el-button size="small" type="danger" :icon="icons.Delete" circle @click="handleDelete(row.id)" />
              <!-- <router-link :to="`/admin/products/detail/${row.id}`">
                <el-button size="small" type="info" :icon="icons.View" circle />
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

    <el-dialog v-model="dialogVisible" :title="isEditMode ? 'Chỉnh sửa cpu' : 'Thêm mới cpu'"
      :close-on-click-modal="false" :destroy-on-close="true" width="1200px">
      <el-form :model="formData" ref="formRef" :rules="rules" label-width="150px">
        <el-row :gutter="20">
          <!-- Chip xử lý -->

          <el-col :span="12">
            <el-form-item label="Chip xử lý" prop="chipXuLy">
              <el-input v-model="formData.chipXuLy" placeholder="Nhập tên chip xử lý (ví dụ: Apple A17 Pro)" />
            </el-form-item>
          </el-col>

          <!-- Hãng sản xuất -->
          <el-col :span="12">
            <el-form-item label="Hãng sản xuất" prop="hangSanXuat">
              <el-input v-model="formData.hangSanXuat" placeholder="Nhập tên hãng sản xuất (ví dụ: Intel, AMD...)" />
            </el-form-item>
          </el-col>

          <!-- Số nhân -->
          <el-col :span="12">
            <el-form-item label="Số nhân" prop="soNhan">
              <el-input v-model="formData.soNhan" placeholder="Nhập số lượng nhân (ví dụ: 8)" />
            </el-form-item>
          </el-col>

          <!-- Xung nhịp -->
          <el-col :span="12">
            <el-form-item label="Xung nhịp" prop="xungNhip">
              <el-input v-model="formData.xungNhip" placeholder="Nhập xung nhịp (ví dụ: 3.2GHz)" />
            </el-form-item>
          </el-col>

          <!-- Công nghệ sản xuất -->
          <el-col :span="12">
            <el-form-item label="Công nghệ sản xuất" prop="congNgheSanXuat">
              <el-input v-model="formData.congNgheSanXuat" placeholder="Nhập công nghệ sản xuất (ví dụ: 7nm, 5nm...)" />
            </el-form-item>
          </el-col>

          <!-- Bộ nhớ đệm -->
          <el-col :span="12">
            <el-form-item label="Bộ nhớ đệm" prop="boNhoDem">
              <el-input v-model="formData.boNhoDem" placeholder="Nhập bộ nhớ đệm (ví dụ: 16MB)" />
            </el-form-item>
          </el-col>

          <!-- Tiêu thụ điện năng -->
          <el-col :span="12">
            <el-form-item label="Tiêu thụ điện năng" prop="tieuThuDienNang">
              <el-input v-model="formData.tieuThuDienNang" placeholder="Nhập mức tiêu thụ điện (ví dụ: 65W)" />
            </el-form-item>
          </el-col>

          <!-- Năm ra mắt -->
          <el-col :span="12">
            <el-form-item label="Năm ra mắt" prop="namRaMat">
              <el-date-picker v-model="formData.namRaMat" type="date" value-format="YYYY-MM-DD"
                placeholder="Chọn ngày ra mắt sản phẩm" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- Nút hành động -->
        <div style="text-align: right; margin-top: 20px;">
          <el-button @click="handleClose">Hủy</el-button>
          <el-button type="primary" @click="submitForm">{{ isEditMode ? 'Cập nhật' : 'Lưu' }}</el-button>
        </div>
      </el-form>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch, markRaw } from 'vue'
import { deleteCpu, getAllCpuPage, postCpu, putCpu } from '@/Service/Adminservice/Products/ProductAdminService'
import { Edit, Delete, View } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useToast } from "vue-toastification";
const toast = useToast();

// Router
const router = useRouter()


// Reactive data
const dialogVisible = ref(false)
const isEditMode = ref(false)
const formRef = ref(null)

const formData = reactive({
  id: null,
  hangSanXuat: "Apple",
  soNhan: '',
  chipXuLy: '',
  xungNhip: '',
  congNgheSanXuat: '',
  boNhoDem: '',
  tieuThuDienNang: '',
  namRaMat: null,
})

const tableCpu = ref([])
const currentPage = ref(1)
const totalPages = ref(1)
const totalItems = ref(0)
const pageSize = ref(5)
const searchKeyword = ref('')
const keyword = ref("")
const errors = reactive({})

const icons = {
  Edit: markRaw(Edit),
  Delete: markRaw(Delete),
  View: markRaw(View)
}

const rules = {
  hangSanXuat: [
    { required: true, message: 'Vui lòng nhập hãng sản xuất', trigger: 'blur' },
    { max: 50, message: "không được phép quá 50 kí tự" }
  ],
  soNhan: [
    { required: true, message: 'Vui lòng nhập số nhân', trigger: 'blur' },
    { max: 50, message: "không được phép quá 50 kí tự" }
  ],
  chipXuLy: [
    { required: true, message: 'Vui lòng nhập tên chip xử lý', trigger: 'blur' },
    { max: 50, message: "không được phép quá 50 kí tự" }
  ],
  xungNhip: [
    { required: true, message: 'Vui lòng nhập xung nhịp', trigger: 'blur' },
    { max: 50, message: "không được phép quá 50 kí tự" }
  ],
  congNgheSanXuat: [
    { required: true, message: 'Vui lòng nhập công nghệ sản xuất', trigger: 'blur' },
    { max: 50, message: "không được phép quá 50 kí tự" }
  ],
  boNhoDem: [
    { required: true, message: 'Vui lòng nhập bộ nhớ đệm', trigger: 'blur' },
    { max: 50, message: "không được phép quá 50 kí tự" }
  ],
  tieuThuDienNang: [
    { required: true, message: 'Vui lòng nhập tiêu thụ điện năng', trigger: 'blur' },
    { max: 50, message: "không được phép quá 50 kí tự" }
  ],
  namRaMat: [
    { required: true, message: 'Vui lòng chọn năng ra mắt', trigger: 'blur' },
    { max: 50, message: "không được phép quá 50 kí tự" }
  ],
}

// Computed properties
const fromRecord = computed(() => {
  return totalItems.value === 0 ? 0 : (currentPage.value - 1) * pageSize.value + 1
})

const toRecord = computed(() => {
  return Math.min(currentPage.value * pageSize.value, totalItems.value)
})

// Methods
const loadData = async () => {
  try {
    const response = await getAllCpuPage(currentPage.value - 1, pageSize.value, keyword.value.trim() || null)
    tableCpu.value = response.content
    totalPages.value = response.totalPages
    totalItems.value = response.totalElements
  } catch (error) {
    console.error('Không thể load dữ liệu:', error)
  }
}

const resetForm = () => {
  Object.assign(formData, {
    id: null,
    hangSanXuat: "Apple",
    soNhan: '',
    chipXuLy: '',
    xungNhip: '',
    congNgheSanXuat: '',
    boNhoDem: '',
    tieuThuDienNang: '',
    namRaMat: null,
  })
}

const handleRefresh = () => {
  clearSearch();
}

const clearSearch = () => {
  searchKeyword.value = "";
  keyword.value = "";
  currentPage.value = 1;
  loadData();
}

const handleCreate = () => {
  resetForm()
  isEditMode.value = false
  dialogVisible.value = true
}

const handleClose = () => {
  dialogVisible.value = false
}

const submitForm = async () => {
  try {

    // Chuẩn bị dữ liệu gửi đi
    const submitData = {
      ...formData,
    };
    console.log("Submitting data:", submitData);

    if (isEditMode.value) {
      await putCpu(formData.id, submitData);
      toast.success("Cập nhật thành công!");
    } else {
      await postCpu(submitData);
      toast.success("Thêm CPU thành công!");
    }

    resetForm();
    dialogVisible.value = false;
    loadData();
  } catch (err) {
    console.error("❌ Lỗi xử lý form:", err);
    console.log("err.response:", err.response);
    console.log("err.response.data:", err.response?.data);

    // Xử lý lỗi validate frontend
    if (err && err.fields) {
      Object.values(err.fields).forEach(fieldErrors => {
        fieldErrors.forEach(error => toast.error(error.message));
      });
      return;
    }

    // Xử lý lỗi từ API
    if (err.response && err.response.data) {
      const { message, errors } = err.response.data;
      if (Array.isArray(message)) {
        message.forEach(error => toast.error(error.message));
      } else if (typeof message === 'object' && message !== null) {
        Object.values(message).forEach(msg => toast.error(msg));
      } else if (typeof errors === 'object' && errors !== null) {
        Object.values(errors).forEach(msg => toast.error(msg));
      } else {
        toast.error(message || errors || "Lỗi không xác định!");
      }
      return;
    }

    // Xử lý lỗi mạng
    if (!err.response) {
      toast.error("Lỗi kết nối server. Vui lòng thử lại sau!");
      return;
    }

    // Xử lý lỗi chung
    const action = isEditMode.value ? "cập nhật" : "thêm";
    const errorMessage = err?.message || String(err) || "Lỗi không xác định!";
    toast.error(`Lỗi khi ${action} CPU: ${errorMessage}`);
  }
};

const openDetail = (row) => {
  isEditMode.value = true;
  Object.assign(formData, row)
  dialogVisible.value = true
}

const indexMethod = (index) => {
  return (currentPage.value - 1) * pageSize.value + index + 1
}

const handlePageChange = (newPage) => {
  currentPage.value = newPage
}

const handleDelete = async (id) => {
  try {
    // Đợi người dùng xác nhận
    await ElMessageBox.confirm('Bạn có chắc chắn muốn xoá mục này?', 'Xác nhận', {
      confirmButtonText: 'Xoá',
      cancelButtonText: 'Huỷ',
      type: 'warning'
    })

    // Nếu xác nhận thì thực hiện xoá
    await deleteCpu(id)
    ElMessage.success('Xoá thành công')
    loadData()

  } catch (error) {
    // Nếu người dùng huỷ xác nhận, hoặc API xoá lỗi
    if (error === 'cancel') {
      ElMessage.info('Đã huỷ xoá')
    } else {
      ElMessage.error('Xoá thất bại')
    }
  }
}

// Lifecycle
onMounted(() => {
  loadData()
})

// Watchers
watch(currentPage, () => {
  loadData()
})

watch(searchKeyword, () => {
  keyword.value = searchKeyword.value.trim();
  currentPage.value = 1;
  loadData();
});
</script>

<style scoped>
::v-deep(.el-pagination button) {
  font-size: 16px;
  padding: 8px 16px;
}

::v-deep(.el-pagination .el-pager li) {
  font-size: 16px;
  padding: 6px 10px;
}



.pagination-fixed {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-top: 24px;
  border: 1px solid #e5e7eb;
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
