<template>
  <div class="container mt-4">

    <el-row :gutter="20" class="mb-4" align="middle">
      <!-- Thanh tìm kiếm căn trái -->
      <el-col :span="8">
        <el-input
          v-model="searchKeyword"
          placeholder="Tìm kiếm theo tên, loại màn hình, tần số quét..."
          clearable
          prefix-icon="Search"
          @clear="clearSearch"
        />
      </el-col>

      <!-- Các nút căn phải -->
      <el-col :span="16" class="text-right" align="right">
        <el-button type="primary" @click="handleCreate">Tạo mới</el-button>
        <el-button type="primary" @click="handleRefresh">Làm mới</el-button>
      </el-col>
    </el-row>

    <h2>Danh sách màn hình</h2>
    <div class="table-responsive mb-4" style="margin-top: 20px">
      <el-table :data="tableManHinh" border style="width: 100%">
        <el-table-column
          type="index"
          :index="indexMethod"
          label="STT"
          width="80"
        />
        <!-- <el-table-column prop="id" label="ID" /> -->
        <el-table-column prop="tenManHinh" label="Tên màn hình" />
        <el-table-column prop="kichThuoc" label="Kích thước" />
        <el-table-column prop="loaiManHinh" label="Loại màn hình" />
        <el-table-column prop="doPhanGiai" label="Độ phân giải" />
        <el-table-column prop="tanSoQuet" label="Tần số quét" />
        <el-table-column prop="doSang" label="Độ sáng" />
        <el-table-column prop="chatLieuKinh" label="Chất liệu kính" />
        <el-table-column label="Thao tác" width="180">
          <template #default="{ row }">
            <div class="action-buttons-horizontal">
              <el-button
                type="primary"
                :icon="Edit"
                circle
                @click="handleEdit(row)"
              />

              <el-button
                type="danger"
                :icon="Delete"
                circle
                @click="handleDelete(row.id)"
              />
              <!-- 
                            <router-link :to="`/admin/products/detail/${row.id}`">
                                <el-button type="info" :icon="View" circle />
                            </router-link> -->
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="pagination-fixed">
      <div class="d-flex justify-content-center align-items-center gap-3">
        <el-pagination
          background
          layout="prev, pager, next"
          :page-size="pageSize"
          :current-page="currentPage"
          :total="totalItems"
          :pager-count="7"
          prev-text="< Trước"
          next-text="Sau >"
          @current-change="handlePageChange"
        />
      </div>
    </div>

    <el-dialog
      v-model="dialogVisible"
      :title="isEditMode ? 'Chỉnh sửa màn hình' : 'Thêm mới màn hình'"
      width="900px"
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <el-form
        :model="formData"
        ref="formRef"
        :rules="rules"
        label-width="140px"
        label-position="left"
        class="hdh-form"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Tên màn hình" prop="tenManHinh">
              <el-input
                v-model="formData.tenManHinh"
                placeholder="Nhập tên màn hình (ví dụ: AMOLED, Retina, Dynamic Island...)"
                autocomplete="on"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="Kích thước" prop="kichThuoc">
              <el-input
                v-model="formData.kichThuoc"
                placeholder="Nhập kích thước (ví dụ: 6.5 inch)"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Loại màn hình" prop="loaiManHinh">
              <el-input
                v-model="formData.loaiManHinh"
                placeholder="Nhập loại màn hình (ví dụ: OLED, IPS, LCD)"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="Độ phân giải" prop="doPhanGiai">
              <el-input
                v-model="formData.doPhanGiai"
                placeholder="Nhập độ phân giải (ví dụ: 1080 x 2400 pixels)"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Tần số quét" prop="tanSoQuet">
              <el-input
                v-model="formData.tanSoQuet"
                placeholder="Nhập tần số quét (ví dụ: 120Hz)"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="Độ sáng" prop="doSang">
              <el-input
                v-model="formData.doSang"
                placeholder="Nhập độ sáng (ví dụ: 1000 nits)"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col>
            <el-form-item label="Chất liệu kính" prop="chatLieuKinh">
              <el-input
                v-model="formData.chatLieuKinh"
                placeholder="Nhập chất liệu kính (ví dụ: Gorilla Glass Victus)"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row justify="end" style="margin-top: 30px">
          <el-button @click="handleClose" style="margin-right: 10px"
            >Hủy</el-button
          >
          <el-button type="primary" @click="submitForm">{{
            isEditMode ? "Cập nhật" : "Lưu"
          }}</el-button>
        </el-row>
      </el-form>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, reactive } from "vue";
import {
  deleteManHinh,
  getAllManHinhPage,
  postManHinh,
  putManHinh,
} from "@/Service/Adminservice/Products/ProductAdminService";
import { Edit, Delete, View } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";

const tableManHinh = ref([]);
const currentPage = ref(1);
const totalPages = ref(1);
const totalItems = ref(0);
const pageSize = 5;
const searchKeyword = ref("");
const dialogVisible = ref(false);
const isEditMode = ref(false);
const formRef = ref(null);
const keyword = ref("");
const errors = reactive({});

const formData = reactive({
  id: null,
  tenManHinh: "",
  kichThuoc: "",
  loaiManHinh: "",
  doPhanGiai: "",
  tanSoQuet: "",
  doSang: "",
  chatLieuKinh: "",
});

const rules = {
  tenManHinh: [
    {
      required: true,
      message: "Vui lòng nhập tên màn hình",
      trigger: "blur",
    },
    {
      max: 50,
      message: "Tên màn hình không được vượt quá 50 ký tự",
      trigger: "blur",
    },
  ],
  kichThuoc: [
    {
      required: true,
      message: "Không được để trống kích thước",
      trigger: "blur",
    },
    {
      max: 50,
      message: "Kích thước không được vượt quá 50 ký tự",
      trigger: "blur",
    },
  ],
  loaiManHinh: [
    {
      required: true,
      message: "Vui lòng chọn loại màn hình",
      trigger: "blur",
    },
    {
      max: 50,
      message: "Loại màn hình không được vượt quá 50 ký tự",
      trigger: "blur",
    },
  ],
  doPhanGiai: [
    {
      required: true,
      message: "Vui lòng nhập độ phân giải",
      trigger: "blur",
    },
    {
      max: 50,
      message: "Độ phân giải không được vượt quá 50 ký tự",
      trigger: "blur",
    },
  ],
  tanSoQuet: [
    {
      required: true,
      message: "Vui lòng nhập tần số quét",
      trigger: "blur",
    },
    {
      max: 50,
      message: "Tần số quét không được vượt quá 50 ký tự",
      trigger: "blur",
    },
  ],
  doSang: [
    {
      required: true,
      message: "Vui lòng nhập độ sáng",
      trigger: "blur",
    },
    {
      max: 50,
      message: "Độ sáng không được vượt quá 50 ký tự",
      trigger: "blur",
    },
  ],
  chatLieuKinh: [
    {
      required: true,
      message: "Vui lòng nhập chất liệu kính",
      trigger: "blur",
    },
    {
      max: 50,
      message: "Chất liệu kính không được vượt quá 50 ký tự",
      trigger: "blur",
    },
  ],
};

const loadData = async () => {
  try {
    const response = await getAllManHinhPage(
      currentPage.value - 1,
      pageSize,
      keyword.value.trim() || null
    );
    tableManHinh.value = response.content;
    totalPages.value = response.totalPages;
    totalItems.value = response.totalElements;
  } catch (error) {
    console.error("Không thể load dữ liệu:", error);
    ElMessage.error("Không thể tải dữ liệu");
  }
};

const clearSearch = () => {
  searchKeyword.value = "";
  keyword.value = "";
  currentPage.value = 1;
  loadData();
};

const handleRefresh = () => {
  clearSearch();
};

const handleCreate = () => {
  try {
    resetForm();
    dialogVisible.value = true;
    isEditMode.value = false;
  } catch (error) {
    console.error("Lỗi khi mở form tạo mới:", error);
    ElMessage.error("Không thể mở form tạo mới");
  }
};

const submitForm = async () => {
  if (!formRef.value) return;
  //reset lỗi cũ
  Object.keys(errors).forEach((key) => delete errors[key]);

  try {
    await formRef.value.validate();

    if (isEditMode.value) {
      await putManHinh(formData.id, formData);
      ElMessage.success("Cập nhật thành công!");
    } else {
      await postManHinh(formData);
      ElMessage.success("Thêm mới thành công!");
    }

    resetForm();
    dialogVisible.value = false;
    loadData();
  } catch (error) {
    console.error("Lỗi xử lý form:", error);
    if (Array.isArray(error)) {
      error.forEach(({ field, message }) => {
        ElMessage.error(errors[field] = message)
      });
    }
  }
};

const handleClose = () => {
  dialogVisible.value = false;
};

const handleEdit = (row) => {
  isEditMode.value = true;
  Object.assign(formData, row);
  dialogVisible.value = true;
};

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm("Bạn có chắc chắn muốn xóa không?", "Xác nhận", {
      confirmButtonText: "Xóa",
      cancelButtonText: "Hủy",
      type: "warning",
    });

    await deleteManHinh(id);
    ElMessage.success("Xóa thành công");
    loadData();
  } catch (error) {
    if (error !== "cancel") {
      console.error("Lỗi khi xóa:", error);
      ElMessage.error("Xóa thất bại");
    }
  }
};

const indexMethod = (index) => {
  return (currentPage.value - 1) * pageSize + index + 1;
};

const handlePageChange = (newPage) => {
  currentPage.value = newPage;
  loadData();
};

const resetForm = () => {
  formData.id = null;
  formData.tenManHinh = "";
  formData.kichThuoc = "";
  formData.loaiManHinh = "";
  formData.doPhanGiai = "";
  formData.tanSoQuet = "";
  formData.doSang = "";
  formData.chatLieuKinh = "";
};

onMounted(() => {
  loadData();
});

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
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
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
