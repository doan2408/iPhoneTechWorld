<script setup>
import { onMounted, reactive, ref, watch } from "vue";
import { getAllStaff } from "@/Service/Adminservice/TaiKhoan/NhanVienServices";
import { addStaff } from "@/Service/Adminservice/TaiKhoan/NhanVienServices";
import { updateStaff } from "@/Service/Adminservice/TaiKhoan/NhanVienServices";
import { detailStaff } from "@/Service/Adminservice/TaiKhoan/NhanVienServices";
import { ElMessage } from "element-plus";
import { Edit, Plus, Search, Loading } from "@element-plus/icons-vue"; // Thêm import Edit, Plus, Search, Loading

const staffList = ref([]);
const isLoading = ref(false);
const currentPage = ref(0);
const totalPages = ref(0);
const searchKeyword = ref("");
const searchTimeout = ref(null);

// Modal state
const showModal = ref(false);
const isSubmitting = ref(false);
const isEditMode = ref(false);
const editingStaffId = ref(null);

// Staff form data
const staffRequest = ref({
  tenNhanVien: "",
  taiKhoan: "",
  matKhau: "",
  email: "",
  sdt: "",
  diaChi: "",
  trangThai: "ENABLE",
  chucVu: "STAFF",
  gioiTinh: true,
  namSinh: "",
});

const errors = reactive({});

//load staff
const loadStaff = async (page = 0, keyword = null) => {
  try {
    isLoading.value = true;
    const response = await getAllStaff(page, keyword);
    staffList.value = response.content;
    currentPage.value = page;
    totalPages.value = response.totalPages;
  } catch (err) {
    err.value = err.message || "An error was thrown while loading the staff";
  } finally {
    isLoading.value = false;
  }
};

//search staff
const performSearch = () => {
  //clear old timeout if has
  if (searchTimeout.value) {
    clearTimeout(searchTimeout.value);
  }

  //create new timeout -- delay 500ms after user stop typing
  searchTimeout.value = setTimeout(() => {
    currentPage.value = 0; //reset from first page when searching
    loadStaff(0, searchKeyword.value || null);
  }, 500);
};

const clearSearch = () => {
  searchKeyword.value = "";
};

watch(searchKeyword, () => {
  performSearch();
});

//previous page
const previousPage = () => {
  if (currentPage.value > 0) {
    currentPage.value -= 1;
  } else {
    currentPage.value = totalPages.value - 1;
  }
  loadStaff(currentPage.value, searchKeyword.value || null);
};

//next page
const nextPage = () => {
  if (currentPage.value < totalPages.value - 1) {
    currentPage.value += 1;
  } else {
    currentPage.value = 0;
  }
  loadStaff(currentPage.value, searchKeyword.value || null);
};

const firstPage = () => {
  const firstPage = 0;
  currentPage.value = firstPage;
  loadStaff(currentPage.value, searchKeyword.value || null);
};

const lastPage = () => {
  // Renamed from latePage to lastPage for clarity
  const lastPage = totalPages.value - 1;
  currentPage.value = lastPage;
  loadStaff(currentPage.value, searchKeyword.value || null);
};

// Modal functions
const openAddModal = () => {
  showModal.value = true;
  isEditMode.value = false;
  editingStaffId.value = null;
  resetForm();
  resetErrors();
};

const openEditModal = async (staffId) => {
  showModal.value = true;
  isEditMode.value = true;
  editingStaffId.value = staffId;
  resetErrors();

  try {
    isLoading.value = true;
    const response = await detailStaff(staffId);
    Object.assign(staffRequest.value, response);
  } catch (err) {
    ElMessage.error(err.message || "Lỗi khi tải thông tin nhân viên");
    showModal.value = false;
  } finally {
    isLoading.value = false;
  }
};

const closeModal = () => {
  showModal.value = false;
  resetForm();
  resetErrors();
  isEditMode.value = false;
  editingStaffId.value = null;
};

const resetForm = () => {
  staffRequest.value = {
    tenNhanVien: "",
    taiKhoan: "",
    matKhau: "",
    email: "",
    sdt: "",
    diaChi: "",
    trangThai: "ENABLE",
    chucVu: "STAFF",
    gioiTinh: true,
    namSinh: "",
  };
};

const resetErrors = () => {
  Object.keys(errors).forEach((key) => delete errors[key]);
};

// Handle submit
const handleSubmit = async () => {
  resetErrors();
  isSubmitting.value = true;

  try {
    if (isEditMode.value) {
      // Update staff
      await updateStaff(editingStaffId.value, staffRequest.value);
      ElMessage.success("Cập nhật nhân viên thành công");
    } else {
      // Add staff
      await addStaff(staffRequest.value);
      ElMessage.success("Thêm nhân viên thành công");
    }

    closeModal();
    loadStaff(currentPage.value, searchKeyword.value || null); // Reload current page
  } catch (err) {
    if (Array.isArray(err)) {
      err.forEach(({ field, message }) => {
        errors[field] = message;
      });
    } else {
      ElMessage.error(
        isEditMode.value
          ? "Cập nhật nhân viên thất bại"
          : "Thêm nhân viên thất bại"
      );
    }
  } finally {
    isSubmitting.value = false;
  }
};

const handlePageChange = async (newPage) => {
  currentPage.value = newPage - 1; // Điều chỉnh về index 0-based
  await loadStaff(currentPage.value, searchKeyword.value || null);
};

onMounted(() => {
  loadStaff();
});
</script>

<template>
  <div class="staff-container">
    <!-- Add Staff Button -->
    <div>
      <el-button type="primary" @click="openAddModal" class="add-staff-btn">
        <el-icon><Plus /></el-icon>
        Thêm nhân viên
      </el-button>
    </div>

    <el-divider />

    <!-- Search Section -->
    <div class="search-section">
      <div class="search-container">
        <div class="search-input-group">
          <div class="search-input-wrapper">
            <el-input
              v-model="searchKeyword"
              placeholder="Tìm kiếm theo tên, email, số điện thoại, chức vụ..."
              prefix-icon="Search"
              clearable
              @clear="clearSearch"
              class="search-input"
            />
          </div>
          <div class="search-status" v-if="isLoading">
            <el-icon class="is-loading"><Loading /></el-icon>
            Đang tìm kiếm...
          </div>
        </div>
      </div>
    </div>

    <!-- Loading State -->
    <div v-if="isLoading" class="text-center">
      <el-icon :icon="Loading" />
      <p>Đang tải dữ liệu...</p>
    </div>

    <!-- Search Result Info -->
    <div v-if="searchKeyword && !isLoading" class="search-result-info">
      <el-alert
        :title="`Kết quả tìm kiếm cho: '${searchKeyword}' (${staffList.length} nhân viên)`"
        type="info"
        :closable="false"
        show-icon
      />
    </div>

    <!-- No Data State -->
    <div v-if="!isLoading && staffList.length === 0" class="no-data">
      <el-empty>
        <template #description>
          <span v-if="searchKeyword">
            Không tìm thấy nhân viên nào với từ khóa "{{ searchKeyword }}"
          </span>
          <span v-else>Chưa có nhân viên nào</span>
        </template>
      </el-empty>
    </div>

    <!-- Staff Table -->
    <div v-if="staffList.length > 0">
      <h2>Danh sách nhân viên</h2>
      <el-table :data="staffList" style="width: 100%" stripe>
        <el-table-column
          type="index"
          label="STT"
          width="60"
          :index="(index) => index + 1 + currentPage * 10"
        />
        <el-table-column prop="maNhanVien" label="Mã nhân viên" width="120" />
        <el-table-column prop="tenNhanVien" label="Tên nhân viên" width="150" />
        <el-table-column prop="taiKhoan" label="Tên đăng nhập" width="130" />
        <el-table-column prop="email" label="Email" width="180" />
        <el-table-column prop="sdt" label="Số điện thoại" width="120" />
        <el-table-column prop="diaChi" label="Địa chỉ" width="150">
          <template #default="scope">
            <span style="white-space: normal; word-break: break-word">
              {{ scope.row.diaChi || "Không có" }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="Trạng thái" width="100">
          <template #default="scope">
            <el-tag
              :type="scope.row.trangThai === 'ENABLE' ? 'success' : 'danger'"
              size="small"
            >
              {{ scope.row.trangThai === "ENABLE" ? "Đang làm" : "Nghỉ" }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Chức vụ" width="100">
          <template #default="scope">
            <el-tag
              :type="scope.row.chucVu === 'ADMIN' ? 'primary' : 'info'"
              size="small"
            >
              {{ scope.row.chucVu === "ADMIN" ? "Quản lý" : "Nhân viên" }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Giới tính" width="80">
          <template #default="scope">
            {{ scope.row.gioiTinh ? "Nam" : "Nữ" }}
          </template>
        </el-table-column>
        <el-table-column label="Năm sinh" width="100">
          <template #default="scope">
            {{ new Date(scope.row.namSinh).getFullYear() }}
          </template>
        </el-table-column>
        <el-table-column label="Thao tác" width="100" fixed="right">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              :icon="Edit"
              @click="openEditModal(scope.row.id)"
              title="Chỉnh sửa"
            />
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination -->
      <div class="pagination-fixed">
        <div
          class="d-flex justify-content-center align-items-center gap-3"
          style="margin-top: 30px"
        >
          <!-- Nút Đầu -->
          <el-button
            :disabled="currentPage === 0"
            @click="firstPage"
            type="default"
          >
            «
          </el-button>

          <!-- el-pagination -->
          <el-pagination
            background
            layout="prev, pager, next"
            :page-size="10"
            :current-page="currentPage + 1"
            :total="totalPages * 10"
            :pager-count="7"
            prev-text="‹"
            next-text="›"
            @current-change="handlePageChange"
          />

          <!-- Nút Cuối -->
          <el-button
            :disabled="currentPage === totalPages - 1"
            @click="lastPage"
            type="default"
          >
            »
          </el-button>
        </div>
      </div>
    </div>

    <!-- Staff Modal (Add/Edit) -->
    <el-dialog
      v-model="showModal"
      :title="
        isEditMode ? 'Cập nhật thông tin nhân viên' : 'Thêm nhân viên mới'
      "
      width="800px"
      :before-close="closeModal"
    >
      <el-form
        :model="staffRequest"
        label-width="140px"
        label-position="left"
        @submit.prevent="handleSubmit"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Tên nhân viên" :error="errors.tenNhanVien">
              <el-input
                v-model="staffRequest.tenNhanVien"
                placeholder="Nhập tên nhân viên"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="Tên đăng nhập" :error="errors.taiKhoan">
              <el-input
                v-model.trim="staffRequest.taiKhoan"
                placeholder="Nhập tên đăng nhập"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Mật khẩu" :error="errors.matKhau">
              <el-input
                v-model.trim="staffRequest.matKhau"
                type="password"
                :placeholder="
                  isEditMode
                    ? 'Chỉ đổi khi nhân viên nghỉ'
                    : 'Nhập mật khẩu'
                "
                show-password
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="Email" :error="errors.email">
              <el-input
                v-model.trim="staffRequest.email"
                placeholder="Nhập email"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Số điện thoại" :error="errors.sdt">
              <el-input
                v-model.trim="staffRequest.sdt"
                placeholder="Nhập số điện thoại"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="Địa chỉ" :error="errors.diaChi">
              <el-input
                v-model="staffRequest.diaChi"
                placeholder="Nhập địa chỉ"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Trạng thái">
              <el-select
                v-model="staffRequest.trangThai"
                placeholder="Chọn trạng thái"
              >
                <el-option label="Đang làm" value="ENABLE" />
                <el-option label="Nghỉ" value="DISABLE" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="Chức vụ">
              <el-select
                v-model="staffRequest.chucVu"
                placeholder="Chọn chức vụ"
              >
                <el-option label="Nhân viên" value="STAFF" />
                <!-- <el-option label="Quản lý" value="ADMIN" /> -->
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Giới tính">
              <el-select
                v-model="staffRequest.gioiTinh"
                placeholder="Chọn giới tính"
              >
                <el-option label="Nam" :value="true" />
                <el-option label="Nữ" :value="false" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="Năm sinh" :error="errors.namSinh">
              <el-date-picker
                v-model="staffRequest.namSinh"
                type="date"
                placeholder="Chọn năm sinh"
                format="DD/MM/YYYY"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="closeModal" :disabled="isSubmitting">
            Hủy
          </el-button>
          <el-button
            type="primary"
            @click="handleSubmit"
            :loading="isSubmitting"
          >
            {{ isEditMode ? "Cập nhật" : "Thêm nhân viên" }}
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.staff-container {
  padding: 24px;
  background: #f5f7fa;
  min-height: 100vh;
}

.add-staff-btn {
  margin-bottom: 16px;
}

.search-section {
  margin-bottom: 24px;
}

.search-container {
  display: flex;
  justify-content: center;
}

.search-input-group {
  width: 100%;
  max-width: 600px;
}

.search-input-wrapper {
  width: 100%;
}

.search-input {
  width: 100%;
}

.search-status {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 8px;
  color: #409eff;
  font-size: 14px;
  justify-content: center;
}

.search-result-info {
  margin-bottom: 16px;
}

.no-data {
  margin: 40px 0;
}

.text-center {
  text-align: center;
  padding: 40px;
}

h2 {
  margin: 24px 0 16px 0;
  color: #303133;
  font-weight: 600;
}

/* Pagination - Sử dụng style từ ProductAdmin.vue */
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

/* Dialog customizations */
:deep(.el-dialog) {
  border-radius: 8px;
}

:deep(.el-dialog__header) {
  padding: 20px 20px 10px 20px;
  border-bottom: 1px solid #ebeef5;
}

:deep(.el-dialog__body) {
  padding: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

/* Form customizations */
:deep(.el-form-item__label) {
  color: #606266;
  font-weight: 500;
}

:deep(.el-input__wrapper) {
  border-radius: 6px;
}

:deep(.el-select) {
  width: 100%;
}

/* Table customizations - Đảm bảo đồng đều kích thước */
:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table th) {
  background: #fafafa;
  color: #606266;
  font-weight: 600;
}

:deep(.el-table tr) {
  height: 48px !important; /* Đặt chiều cao cố định */
  transition: none !important; /* Loại bỏ hiệu ứng chuyển đổi */
}

:deep(.el-table td) {
  padding: 12px 16px;
  font-size: 14px;
  color: #374151;
  border-color: #f3f4f6;
  background: white !important;
  vertical-align: middle; /* Căn giữa nội dung */
}

:deep(.el-table .cell) {
  overflow: visible !important;
  text-overflow: unset !important;
  white-space: nowrap !important;
}

:deep(.el-button) {
  padding: 6px 12px;
  font-size: 14px;
}

/* Responsive */
@media (max-width: 768px) {
  .staff-container {
    padding: 16px;
  }

  :deep(.el-dialog) {
    width: 95% !important;
    margin: 5vh auto !important;
  }

  :deep(.el-table tr) {
    height: 40px !important; /* Giảm chiều cao trên mobile */
  }

  :deep(.el-table td) {
    padding: 8px 12px;
    font-size: 13px;
  }
}
</style>
