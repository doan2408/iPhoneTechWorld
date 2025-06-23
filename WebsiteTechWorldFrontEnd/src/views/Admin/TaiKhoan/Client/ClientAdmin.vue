<script setup>
import { onMounted, reactive, ref, watch, nextTick } from "vue";
import {
  getAllClient,
  addClient,
  updateClient,
  detailClient,
} from "@/Service/Adminservice/TaiKhoan/KhachHangServices";
import { ElMessage, ElIcon } from "element-plus";
import { Edit, Plus, Search, Loading } from "@element-plus/icons-vue";

const clientList = ref([]);
const isLoading = ref(false);
const error = ref("");
const currentPage = ref(0);
const totalPages = ref(0);
const searchKeyword = ref("");
const searchTimeout = ref(null);

// Modal state
const showModal = ref(false);
const isEditMode = ref(false);
const editingClientId = ref(null);
const isSubmitting = ref(false);

// Client form data
const clientRequest = ref({
  tenKhachHang: "",
  taiKhoan: "",
  matKhau: "",
  email: "",
  sdt: "",
  trangThai: "ACTIVE",
  gioiTinh: true,
  ngaySinh: "",
});

const errors = reactive({});
const formRef = ref(null);
const formKey = ref(0);

// Validation rules
const rules = ref({
  tenKhachHang: [
    {
      required: true,
      message: "Vui lòng nhập tên khách hàng",
      trigger: "blur",
    },
  ],
});

// Error handling utilities
const setError = (field, message) => {
  errors[field] = message;
};

const clearError = (field) => {
  if (errors.hasOwnProperty(field)) {
    delete errors[field];
  }
};

const clearAllErrors = () => {
  const keys = [...Object.keys(errors)];
  keys.forEach((key) => {
    clearError(key);
  });

  // Force reactivity update
  nextTick(() => {
    if (formRef.value) {
      formRef.value.clearValidate();
    }
  });
};

// Load client
const loadClient = async (page = 0, keyword = null) => {
  try {
    isLoading.value = true;
    const response = await getAllClient(page, keyword);
    clientList.value = response.content;
    currentPage.value = page;
    totalPages.value = response.totalPages;
  } catch (err) {
    error.value =
      err.message || "An error was thrown while loading the clients";
  } finally {
    isLoading.value = false;
  }
};

// Search client
const performSearch = () => {
  if (searchTimeout.value) {
    clearTimeout(searchTimeout.value);
  }
  searchTimeout.value = setTimeout(() => {
    currentPage.value = 0;
    loadClient(0, searchKeyword.value || null);
  }, 500);
};

const clearSearch = () => {
  searchKeyword.value = "";
};

watch(searchKeyword, () => {
  performSearch();
});

// Pagination
const previousPage = () => {
  if (currentPage.value > 0) {
    currentPage.value -= 1;
  } else {
    currentPage.value = totalPages.value - 1;
  }
  loadClient(currentPage.value, searchKeyword.value || null);
};

const nextPage = () => {
  if (currentPage.value < totalPages.value - 1) {
    currentPage.value += 1;
  } else {
    currentPage.value = 0;
  }
  loadClient(currentPage.value, searchKeyword.value || null);
};

const firstPage = () => {
  const firstPage = 0;
  currentPage.value = firstPage;
  loadClient(currentPage.value, searchKeyword.value || null);
};

const latePage = () => {
  const latePage = totalPages.value - 1;
  currentPage.value = latePage;
  loadClient(currentPage.value, searchKeyword.value || null);
};

// Modal functions
const openAddModal = () => {
  showModal.value = true;
  isEditMode.value = false;
  editingClientId.value = null;
  resetForm();
  clearAllErrors();
  formKey.value += 1; // Force re-render form

  // Reset Element Plus form validation
  nextTick(() => {
    if (formRef.value) {
      formRef.value.resetFields();
      formRef.value.clearValidate();
    }
  });
};

const openEditModal = async (clientId) => {
  showModal.value = true;
  isEditMode.value = true;
  editingClientId.value = clientId;
  clearAllErrors();
  formKey.value += 1; // Force re-render form

  // Reset validation trước khi load data
  nextTick(() => {
    if (formRef.value) {
      formRef.value.resetFields();
      formRef.value.clearValidate();
    }
  });

  try {
    isLoading.value = true;
    const response = await detailClient(clientId);
    Object.assign(clientRequest.value, response);
    clientRequest.value.matKhau = ""; // Đặt lại mật khẩu rỗng khi chỉnh sửa
  } catch (err) {
    ElMessage.error(err.message || "Lỗi khi tải thông tin khách hàng");
    showModal.value = false;
  } finally {
    isLoading.value = false;
  }
};

const closeModal = () => {
  showModal.value = false;
  resetForm();
  clearAllErrors();
  isEditMode.value = false;
  editingClientId.value = null;

  // Reset Element Plus form validation
  if (formRef.value) {
    formRef.value.resetFields();
    formRef.value.clearValidate();
  }
};

const resetForm = () => {
  clientRequest.value = {
    tenKhachHang: "",
    taiKhoan: "",
    matKhau: "",
    email: "",
    sdt: "",
    trangThai: "ACTIVE",
    gioiTinh: true,
    ngaySinh: "",
  };
};

// Handle submit
const handleAddClient = async () => {
  clearAllErrors();
  isSubmitting.value = true;

  try {
    const isValid = await formRef.value.validate();
    if (isValid) {
      if (isEditMode.value) {
        const payload = { ...clientRequest.value };
        if (!payload.matKhau) {
          delete payload.matKhau; // Không gửi matKhau nếu rỗng
        }
        await updateClient(editingClientId.value, payload);
        ElMessage.success("Cập nhật khách hàng thành công");
      } else {
        await addClient(clientRequest.value);
        ElMessage.success("Thêm khách hàng thành công");
      }
      closeModal();
      loadClient(currentPage.value, searchKeyword.value || null);
    }
  } catch (err) {
    if (Array.isArray(err)) {
      err.forEach(({ field, message }) => {
        setError(field, message);
      });
    } else {
      ElMessage.error(
        isEditMode.value
          ? "Cập nhật khách hàng thất bại"
          : "Thêm khách hàng thất bại"
      );
    }
  } finally {
    isSubmitting.value = false;
  }
};

onMounted(() => {
  loadClient();
});
</script>

<template>
  <div class="client-container">
    <!-- Add Client Button -->
    <div>
      <el-button type="primary" @click="openAddModal" class="add-client-btn">
        <el-icon><Plus /></el-icon>
        Thêm khách hàng
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
              placeholder="Tìm kiếm theo tên, email, số điện thoại, tên tài khoản..."
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
        :title="`Kết quả tìm kiếm cho: '${searchKeyword}' (${clientList.length} khách hàng)`"
        type="info"
        :closable="false"
        show-icon
      />
    </div>

    <!-- No Data State -->
    <div v-if="!isLoading && clientList.length === 0" class="no-data">
      <el-empty>
        <template #description>
          <span v-if="searchKeyword">
            Không tìm thấy khách hàng nào với từ khóa "{{ searchKeyword }}"
          </span>
          <span v-else>Chưa có khách hàng nào</span>
        </template>
      </el-empty>
    </div>

    <!-- Client Table -->
    <div v-if="clientList.length > 0">
      <h2>Danh sách khách hàng</h2>
      <el-table :data="clientList" style="width: 100%" stripe>
        <el-table-column
          type="index"
          label="STT"
          width="60"
          :index="(index) => index + 1 + currentPage * 10"
        />
        <el-table-column prop="maKhachHang" label="Mã khách hàng" width="120" />
        <el-table-column
          prop="tenKhachHang"
          label="Tên khách hàng"
          width="150"
        />
        <el-table-column prop="taiKhoan" label="Tên đăng nhập" width="130" />
        <el-table-column prop="email" label="Email" width="180" />
        <el-table-column label="Địa chỉ" width="250">
          <template #default="scope">
            <template
              v-if="
                scope.row.diaChi &&
                scope.row.diaChi.find((dc) => dc.diaChiChinh)
              "
            >
              {{ scope.row.diaChi.find((dc) => dc.diaChiChinh).soNha }},
              {{ scope.row.diaChi.find((dc) => dc.diaChiChinh).tenDuong }},
              {{ scope.row.diaChi.find((dc) => dc.diaChiChinh).xaPhuong }},
              {{ scope.row.diaChi.find((dc) => dc.diaChiChinh).quanHuyen }},
              {{ scope.row.diaChi.find((dc) => dc.diaChiChinh).tinhThanhPho }}
            </template>
            <template v-else>Không có</template>
          </template>
        </el-table-column>
        <el-table-column prop="sdt" label="Số điện thoại" width="120">
          <template #default="scope">
            {{ scope.row.sdt ? scope.row.sdt : "Không có" }}
          </template>
        </el-table-column>
        <el-table-column label="Trạng thái" width="100">
          <template #default="scope">
            <el-tag
              :type="scope.row.trangThai === 'ACTIVE' ? 'success' : 'danger'"
              size="small"
            >
              {{ scope.row.trangThai === "ACTIVE" ? "Hoạt động" : "Ngừng" }}
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
            {{
              scope.row.ngaySinh
                ? new Date(scope.row.ngaySinh).getFullYear()
                : "Không"
            }}
          </template>
        </el-table-column>
        <el-table-column label="Thao tác" width="150" fixed="right">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              :icon="Edit"
              @click="openEditModal(scope.row.id)"
              title="Chỉnh sửa"
            />
            <el-button
              type="warning"
              size="small"
              @click="$router.push(`/admin/client/addresses/${scope.row.id}`)"
              title="Xem địa chỉ"
            >
              <i class="bi bi-geo-alt"></i>
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination -->
      <div class="pagination">
        <button @click="firstPage">Trang đầu</button>
        <button @click="previousPage">Trang trước</button>
        <span>Trang {{ currentPage + 1 }} / {{ totalPages }}</span>
        <button @click="nextPage">Trang sau</button>
        <button @click="latePage">Trang cuối</button>
      </div>
    </div>

    <!-- Client Modal (Add/Edit) -->
    <el-dialog
      v-model="showModal"
      :title="
        isEditMode ? 'Cập nhật thông tin khách hàng' : 'Thêm khách hàng mới'
      "
      width="800px"
      :before-close="closeModal"
    >
      <el-form
        :model="clientRequest"
        ref="formRef"
        :key="formKey"
        label-width="140px"
        label-position="left"
        @submit.prevent="handleAddClient"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item
              label="Tên khách hàng"
              :error="errors.tenKhachHang"
            >
              <el-input
                v-model="clientRequest.tenKhachHang"
                placeholder="Nhập tên khách hàng"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item
              label="Tên đăng nhập"
              :error="errors.taiKhoan"
            >
              <el-input
                v-model.trim="clientRequest.taiKhoan"
                placeholder="Nhập tên đăng nhập"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Email"  :error="errors.email">
              <el-input
                v-model.trim="clientRequest.email"
                placeholder="Nhập email"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="Số điện thoại"  :error="errors.sdt">
              <el-input
                v-model.trim="clientRequest.sdt"
                placeholder="Nhập số điện thoại"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Trạng thái">
              <el-select
                v-model="clientRequest.trangThai"
                placeholder="Chọn trạng thái"
              >
                <el-option label="Hoạt động" value="ACTIVE" />
                <el-option label="Ngừng hoạt động" value="INACTIVE" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item
              label="Năm sinh"
              :error="errors.ngaySinh"
            >
              <el-date-picker
                v-model="clientRequest.ngaySinh"
                type="date"
                placeholder="Chọn năm sinh"
                format="DD/MM/YYYY"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Giới tính">
              <el-select
                v-model="clientRequest.gioiTinh"
                placeholder="Chọn giới tính"
              >
                <el-option label="Nam" :value="true" />
                <el-option label="Nữ" :value="false" />
              </el-select>
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
            @click="handleAddClient"
            :loading="isSubmitting"
          >
            {{ isEditMode ? "Cập nhật" : "Thêm khách hàng" }}
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.client-container {
  padding: 24px;
  background: #f5f7fa;
  min-height: 100vh;
}

.add-client-btn {
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

/* Pagination */
.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.pagination button {
  padding: 8px 16px;
  border-radius: 4px;
  border: 1px solid #dee2e6;
  cursor: pointer;
  background: white;
  color: #495057;
  font-weight: 500;
  transition: all 0.2s ease;
}

.pagination button:hover {
  background: #e9ecef;
  border-color: #adb5bd;
}

.pagination span {
  font-weight: 500;
  color: #495057;
  font-size: 14px;
  padding: 8px 16px;
  background: #f8f9fa;
  border-radius: 4px;
  border: 1px solid #dee2e6;
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

/* Table customizations */
:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table th) {
  background: #fafafa;
  color: #606266;
  font-weight: 600;
}

/* Responsive */
@media (max-width: 768px) {
  .client-container {
    padding: 16px;
  }

  :deep(.el-dialog) {
    width: 95% !important;
    margin: 5vh auto !important;
  }
}
</style>
