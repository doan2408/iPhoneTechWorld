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

    <h2>Danh sách cpu</h2>
    <div class="table-responsive mb-4" style="margin-top: 20px;">
      <el-table :data="tableCpu" border style="width: 100%">
        <el-table-column type="index" :index="indexMethod" label="STT" width="80" />
        <!-- <el-table-column prop="id" label="ID" /> -->
        <el-table-column prop="hangSanXuat" label="Hãng sản xuất" />
        <el-table-column prop="soNhan" label="Số nhân" />
        <el-table-column prop="soLuong" label="Số lượng" />
        <el-table-column prop="xungNhip" label="Xung nhịp" />
        <el-table-column prop="congNgheSanXuat" label="Công nghệ sản xuất" />
        <el-table-column prop="boNhoDem" label="Bộ nhớ đệm" />
        <el-table-column prop="tieuThuDienNang" label="Tiêu thụ điện năng" />
        <el-table-column prop="namRaMat" label="Năm ra mắt" />
        <el-table-column label="Thao tác" width="180">
          <template #default="{ row }">
            <div class="action-buttons-horizontal">
              <el-button size="small" type="primary" :icon="icons.Edit" circle @click="openDetail(row, true)" />
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
      :close-on-click-modal="false" :destroy-on-close="true" width="800px">
      <el-form :model="formData" ref="formRef" :rules="rules" label-width="150px">
        <el-row :gutter="20">
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

          <!-- Số lượng -->
          <el-col :span="12">
            <el-form-item label="Số lượng" prop="soLuong">
              <el-input type="number" v-model.number="formData.soLuong" placeholder="Nhập số lượng linh kiện..." />
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

<script>
import { deleteCpu, getAllCpuPage, postCpu, putCpu } from '@/Service/Adminservice/Products/ProductAdminService';
import { Edit, Delete, View } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';
import { computed, markRaw } from 'vue';

export default {
  data() {
    return {
      dialogVisible: false,
      isEditMode: false,
      formData: {
        id: null,
        hangSanXuat: '',
        soNhan: '',
        soLuong: null,
        xungNhip: '',
        congNgheSanXuat: '',
        boNhoDem: '',
        tieuThuDienNang: '',
        namRaMat: null,
      },
      tableCpu: [],
      currentPage: 1,
      totalPages: 1,
      totalItems: 0,
      pageSize: 5,
      searchQuery: '',
      icons: {
        Edit: markRaw(Edit),
        Delete: markRaw(Delete),
        View: markRaw(View)
      },
      rules: {
        hangSanXuat: [{ required: true, message: 'Vui lòng nhập hãng sản xuất', trigger: 'blur' }],
        soNhan: [{ required: true, message: 'Vui lòng nhập số nhân', trigger: 'blur' }],
        soLuong: [{ required: true, message: 'Vui lòng nhập số lượng', trigger: 'blur' }],
        xungNhip: [{ required: true, message: 'Vui lòng nhập xung nhịp', trigger: 'blur' }],
        congNgheSanXuat: [{ required: true, message: 'Vui lòng nhập công nghệ sản xuất', trigger: 'blur' }],
        boNhoDem: [{ required: true, message: 'Vui lòng nhập bộ nhớ đệm', trigger: 'blur' }],
        tieuThuDienNang: [{ required: true, message: 'Vui lòng nhập tiêu thụ điện năng', trigger: 'blur' }],
        namRaMat: [{ required: true, message: 'Vui lòng chọn năng ra mắt', trigger: 'blur' }],
      },
    };
  },
  computed: {
    fromRecord() {
      return this.totalItems === 0 ? 0 : (this.currentPage - 1) * this.pageSize + 1;
    },
    toRecord() {
      return Math.min(this.currentPage * this.pageSize, this.totalItems);
    }
  },
  methods: {
    async loadData() {
      try {
        const response = await getAllCpuPage(this.currentPage - 1, this.pageSize, this.searchQuery.trim());
        this.tableCpu = response.content;
        this.totalPages = response.totalPages;
        this.totalItems = response.totalElements;
      } catch (error) {
        console.error('Không thể load dữ liệu:', error);
      }
    },
    resetForm() {
      this.formData = {
        id: null,
        hangSanXuat: '',
        soNhan: '',
        soLuong: null,
        xungNhip: '',
        congNgheSanXuat: '',
        boNhoDem: '',
        tieuThuDienNang: '',
        namRaMat: null,
      };
    },
    handleSearch() {
      this.currentPage = 1;
      this.loadData();
    },
    handleRefresh() {
      this.searchQuery = '';
      this.currentPage = 1;
      this.loadData();
    },
    handleCreate() {
      this.resetForm();
      this.dialogVisible = true;
    },
    handleClose() {
      this.dialogVisible = false;
    },
    async submitForm() {
      try {
        await this.$refs.formRef.validate();

        if (this.isEditMode) {
          await putCpu(this.formData.id, this.formData);
          this.$message.success('Cập nhật thành công!');
        } else {
          await postCpu(this.formData);
          this.$message.success('Thêm cpu thành công!');
          console.log('Dữ liệu data', this.formData);
        }

        this.resetForm();
        this.dialogVisible = false;
        this.loadData();
      } catch (err) {
        this.$message.error('Vui lòng điền đầy đủ và hợp lệ các trường!');
      }
    },
    openDetail(data, isEdit = false) {
      this.formData = { ...data };
      this.isEditMode = isEdit;
      this.dialogVisible = true;
    },
    indexMethod(index) {
      return (this.currentPage - 1) * this.pageSize + index + 1;
    },
    handlePageChange(newPage) {
      this.currentPage = newPage;
    },
    async handleDelete(id) {
      try {
        // Đợi người dùng xác nhận
        await this.$confirm('Bạn có chắc chắn muốn xoá mục này?', 'Xác nhận', {
          confirmButtonText: 'Xoá',
          cancelButtonText: 'Huỷ',
          type: 'warning'
        });

        // Nếu xác nhận thì thực hiện xoá
        await deleteCpu(id);
        this.$message.success('Xoá thành công');
        this.loadData();

      } catch (error) {
        // Nếu người dùng huỷ xác nhận, hoặc API xoá lỗi
        if (error === 'cancel') {
          this.$message.info('Đã huỷ xoá');
        } else {
          this.$message.error('Xoá thất bại');
        }
      }
    },
  },
  mounted() {
    this.loadData();
  },
  watch: {
    currentPage() {
      this.loadData();
    },
    searchQuery() {
      this.loadData();
    }
  }
};
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
