<template>
  <div class="promotion-container">
    <!-- Header Section -->
    <div class="page-header">
      <div class="header-content">
        <div class="title-section">
          <h1 class="page-title">Quản lý khuyến mãi</h1>
          <p class="page-subtitle">Tạo và quản lý các chương trình khuyến mãi</p>
        </div>
        <el-button 
          v-if="isAdmin" 
          type="primary" 
          size="large"
          class="create-btn"
          @click="openDialog"
        >
          <el-icon class="btn-icon">
            <Plus />
          </el-icon>
          Tạo khuyến mãi
        </el-button>
      </div>
    </div>

    <!-- Filters Section -->
    <el-card class="filters-card" shadow="never">
      <div class="filters-content">
        <div class="filter-group">
          <div class="filter-item">
            <label class="filter-label">Tìm kiếm</label>
            <el-input
              v-model="search"
              placeholder="Tìm kiếm theo mã hoặc tên..."
              clearable
              class="filter-input"
            >
              <template #prefix>
                <el-icon class="search-icon">
                  <Search />
                </el-icon>
              </template>
            </el-input>
          </div>
          
          <div class="filter-item">
            <label class="filter-label">Trạng thái</label>
            <el-select
              v-model="trangThaiFilter"
              placeholder="Chọn trạng thái"
              clearable
              class="filter-select"
            >
              <el-option label="Tất cả" :value="null" />
              <el-option label="Chưa bắt đầu" value="NOT_STARTED" />
              <el-option label="Đang hoạt động" value="ACTIVE" />
              <el-option label="Đã hết hạn" value="EXPIRED" />
            </el-select>
          </div>
          
          <div class="filter-item">
            <label class="filter-label">Ngày bắt đầu</label>
            <el-date-picker
              v-model="ngayBatDauFilter"
              type="date"
              placeholder="Chọn ngày bắt đầu"
              value-format="YYYY-MM-DD"
              clearable
              class="filter-date"
            />
          </div>
          
          <div class="filter-item">
            <label class="filter-label">Ngày kết thúc</label>
            <el-date-picker
              v-model="ngayKetThucFilter"
              type="date"
              placeholder="Chọn ngày kết thúc"
              value-format="YYYY-MM-DD"
              clearable
              class="filter-date"
            />
          </div>
          
          <div class="filter-actions">
            <el-button @click="clear" class="reset-btn">
              <el-icon>
                <Refresh />
              </el-icon>
              Làm mới
            </el-button>
          </div>
        </div>
      </div>
    </el-card>

    <!-- Data Table -->
    <el-card class="table-card" shadow="never">
      <el-table
        v-loading="isLoading"
        :data="phieuGiamGias"
        class="promotion-table"
        :row-class-name="getRowClassName"
        empty-text="Không có dữ liệu"
      >
        <el-table-column label="STT" width="75" align="center">
          <template #default="scope">
            <span class="row-index">{{ scope.$index + 1 + currentPage * pageSize }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="maGiamGia" label="Mã khuyến mãi" min-width="145">
          <template #default="scope">
            <div class="code-cell">
              <el-tag type="info" size="small" class="code-tag">
                {{ scope.row.maGiamGia }}
              </el-tag>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="tenKhuyenMai" label="Tên khuyến mãi" min-width="200">
          <template #default="scope">
            <div class="name-cell">
              <span class="promotion-name">{{ scope.row.tenKhuyenMai }}</span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column label="Giá trị giảm" width="140" align="center">
          <template #default="scope">
            <div class="value-cell">
              <span v-if="scope.row.loaiKhuyenMai === 'Phần trăm'" class="discount-percent">
                {{ scope.row.giaTriKhuyenMai }}%
              </span>
              <span v-else class="discount-amount">
                {{ formatCurrency(scope.row.giaTriKhuyenMai) }}
              </span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column label="Thời gian" width="145">
          <template #default="scope">
            <div class="date-range">
              <div class="date-item">
                <el-icon class="date-icon"><Calendar /></el-icon>
                <span>{{ formatDate(scope.row.ngayBatDau) }}</span>
              </div>
              <div class="date-separator">→</div>
              <div class="date-item">
                <el-icon class="date-icon"><Calendar /></el-icon>
                <span>{{ formatDate(scope.row.ngayKetThuc) }}</span>
              </div>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="soLuong" label="Số lượng" width="110" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.soLuong > 0 ? 'success' : 'danger'" size="small">
              {{ scope.row.soLuong }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="Trạng thái" width="140" align="center">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.trangThaiPhieuGiamGia)" size="small">
              {{ convertTrangThai(scope.row.trangThaiPhieuGiamGia) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="Loại" width="130" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.congKhai ? 'success' : 'warning'" size="small">
              {{ scope.row.congKhai ? 'Công khai' : 'Riêng tư' }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column v-if="isAdmin" label="Thao tác" width="120" align="center" fixed="right">
          <template #default="scope">
            <div class="action-buttons">
              <el-tooltip content="Chỉnh sửa" placement="top">
                <el-button
                  size="small"
                  type="primary"
                  :icon="Edit"
                  circle
                  @click="viewUpdate(scope.row.id)"
                  class="action-btn edit-btn"
                />
              </el-tooltip>
              <el-tooltip content="Xóa" placement="top">
                <el-button
                  size="small"
                  type="danger"
                  :icon="Delete"
                  circle
                  @click="handleDeletePhieuGiamGia(scope.row.id)"
                  class="action-btn delete-btn" style="margin-left: 0;"
                />
              </el-tooltip>
            </div>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- Pagination -->
      <div class="pagination-wrapper">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :current-page="currentPage + 1"
          :page-size="pageSize"
          :page-sizes="[5, 10, 20, 50]"
          :total="total"
          @current-change="(p) => loadPhieuGiamGia(p - 1)"
          @size-change="handleSizeChange"
          class="custom-pagination"
        />
      </div>
    </el-card>

    <!-- Create/Edit Dialog -->
    <el-dialog
      v-model="phieuGiamGiaDialogVisible"
      width="800px"
      style="margin-top: 30px;"
      class="promotion-dialog"
      :close-on-click-modal="false"
    >
      <div class="dialog-content">
        <el-form :model="formData" label-position="top" class="promotion-form">
          <div class="form-grid">
            <!-- Basic Information -->
            <div class="form-section">
              <h3 class="section-title">Thông tin cơ bản</h3>
              <div class="form-row">
                <el-form-item label="Mã giảm giá" class="form-item">
                  <el-input
                    v-model="formData.maGiamGia"
                    placeholder="Mã tự động"
                    :class="{ 'error': errors.maGiamGia }"
                  />
                  <span v-if="errors.maGiamGia" class="error-text">{{ errors.maGiamGia }}</span>
                </el-form-item>
                
                <el-form-item label="Tên khuyến mãi *" class="form-item">
                  <el-input
                    v-model="formData.tenKhuyenMai"
                    placeholder="Nhập tên khuyến mãi"
                    :class="{ 'error': errors.tenKhuyenMai }"
                  />
                  <span v-if="errors.tenKhuyenMai" class="error-text">{{ errors.tenKhuyenMai }}</span>
                </el-form-item>
              </div>
              
              <div class="form-row">
                <el-form-item label="Loại khuyến mãi" class="form-item">
                  <el-select v-model="formData.loaiKhuyenMai" placeholder="Chọn loại">
                    <el-option value="Phần trăm" label="Phần trăm (%)" />
                    <el-option value="Cố định" label="Số tiền cố định" />
                  </el-select>
                </el-form-item>
                
                <el-form-item label="Giá trị khuyến mãi *" class="form-item">
                  <el-input-number
                    v-model="formData.giaTriKhuyenMai"
                    :min="0"
                    :max="formData.loaiKhuyenMai === 'Phần trăm' ? 100 : undefined"
                    placeholder="0"
                    class="full-width"
                    :class="{ 'error': errors.giaTriKhuyenMai }"
                  />
                  <span v-if="errors.giaTriKhuyenMai" class="error-text">{{ errors.giaTriKhuyenMai }}</span>
                </el-form-item>
              </div>
            </div>

            <!-- Conditions -->
            <div class="form-section">
              <h3 class="section-title">Điều kiện áp dụng</h3>
              <div class="form-row">
                <el-form-item label="Giá trị đơn hàng tối thiểu" class="form-item">
                  <el-input-number
                    v-model="formData.giaTriDonHangToiThieu"
                    :min="0"
                    placeholder="0"
                    class="full-width"
                    :class="{ 'error': errors.giaTriDonHangToiThieu }"
                  />
                  <span v-if="errors.giaTriDonHangToiThieu" class="error-text">{{ errors.giaTriDonHangToiThieu }}</span>
                </el-form-item>
                
                <el-form-item
                  v-if="formData.loaiKhuyenMai === 'Phần trăm'"
                  label="Giá trị khuyến mãi tối đa"
                  class="form-item"
                >
                  <el-input-number
                    v-model="formData.giaTriKhuyenMaiToiDa"
                    :min="0"
                    placeholder="0"
                    class="full-width"
                    :class="{ 'error': errors.giaTriKhuyenMaiToiDa }"
                  />
                  <span v-if="errors.giaTriKhuyenMaiToiDa" class="error-text">{{ errors.giaTriKhuyenMaiToiDa }}</span>
                </el-form-item>
              </div>
            </div>

            <!-- Time Settings -->
            <div class="form-section">
              <h3 class="section-title">Thời gian hiệu lực</h3>
              <div class="form-row">
                <el-form-item label="Ngày bắt đầu *" class="form-item">
                  <el-date-picker
                    v-model="formData.ngayBatDau"
                    type="date"
                    placeholder="Chọn ngày bắt đầu"
                    value-format="YYYY-MM-DD"
                    class="full-width"
                    :class="{ 'error': errors.ngayBatDau }"
                  />
                  <span v-if="errors.ngayBatDau" class="error-text">{{ errors.ngayBatDau }}</span>
                </el-form-item>
                
                <el-form-item label="Ngày kết thúc *" class="form-item">
                  <el-date-picker
                    v-model="formData.ngayKetThuc"
                    type="date"
                    placeholder="Chọn ngày kết thúc"
                    value-format="YYYY-MM-DD"
                    class="full-width"
                    :class="{ 'error': errors.ngayKetThuc }"
                  />
                  <span v-if="errors.ngayKetThuc" class="error-text">{{ errors.ngayKetThuc }}</span>
                </el-form-item>
              </div>
            </div>

            <!-- Additional Settings -->
            <div class="form-section">
              <h3 class="section-title">Cài đặt bổ sung</h3>
              <div class="form-row">
                <el-form-item label="Số lượng *" class="form-item">
                  <el-input-number
                    v-model="formData.soLuong"
                    :min="1"
                    placeholder="1"
                    class="full-width"
                    :class="{ 'error': errors.soLuong }"
                  />
                  <span v-if="errors.soLuong" class="error-text">{{ errors.soLuong }}</span>
                </el-form-item>
                
                <el-form-item label="Số điểm cần đổi" class="form-item">
                  <el-input-number
                    v-model="formData.soDiemCanDeDoi"
                    :min="0"
                    placeholder="0"
                    class="full-width"
                    :class="{ 'error': errors.soDiemCanDeDoi }"
                  />
                  <span v-if="errors.soDiemCanDeDoi" class="error-text">{{ errors.soDiemCanDeDoi }}</span>
                </el-form-item>
              </div>
              
              <div class="form-row">
                <el-form-item label="Loại phiếu" class="form-item">
                  <el-select v-model="formData.congKhai" placeholder="Chọn loại">
                    <el-option :value="true" label="Công khai" />
                    <el-option :value="false" label="Riêng tư" />
                  </el-select>
                </el-form-item>
                
                <el-form-item v-if="!formData.congKhai" label="Hạng tối thiểu" class="form-item">
                  <el-select v-model="formData.hangToiThieu" placeholder="Chọn hạng">
                    <el-option value="MEMBER" label="Thành viên" />
                    <el-option value="SILVER" label="Bạc" />
                    <el-option value="GOLD" label="Vàng" />
                    <el-option value="DIAMOND" label="Kim cương" />
                  </el-select>
                </el-form-item>
              </div>
              
              <el-form-item label="Điều kiện áp dụng *" class="form-item full-width">
                <el-input
                  v-model="formData.dieuKienApDung"
                  type="textarea"
                  :rows="3"
                  placeholder="Nhập điều kiện áp dụng khuyến mãi..."
                  :class="{ 'error': errors.dieuKienApDung }"
                />
                <span v-if="errors.dieuKienApDung" class="error-text">{{ errors.dieuKienApDung }}</span>
              </el-form-item>
              
              <el-form-item label="Trạng thái phát hành" class="form-item">
                <el-switch
                  v-model="formData.trangThaiPhatHanh"
                  :active-value="switchConfig.activeValue"
                  :inactive-value="switchConfig.inactiveValue"
                  :active-text="switchConfig.activeText"
                  :inactive-text="switchConfig.inactiveText"
                  class="status-switch"
                />
              </el-form-item>
            </div>
          </div>
        </el-form>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="phieuGiamGiaDialogVisible = false" size="large" class="cancel-btn">
            Hủy
          </el-button>
          <el-button type="primary" @click="savePhieuGiamGia" size="large" class="save-btn" :loading="isLoading">
            {{ formData.id ? 'Cập nhật' : 'Tạo mới' }}
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, reactive, computed } from "vue";
import { getAll, detail, add, update, deletePhieuGiamGia } from "@/Service/Adminservice/PhieuGiamGia/PhieuGiamGiaAdminService";
import { ElMessage, ElMessageBox } from "element-plus";
import { Delete, Plus, Edit, Search, Refresh, Calendar } from '@element-plus/icons-vue';
import store from "@/Service/LoginService/Store";

// Reactive data
const isLoading = ref(false);
const phieuGiamGias = ref([]);
const currentPage = ref(0);
const totalPages = ref(0);
const total = ref(0);
const pageSize = ref(5);
const search = ref("");
const trangThaiFilter = ref(null);
const ngayBatDauFilter = ref(null);
const ngayKetThucFilter = ref(null);
const phieuGiamGiaDialogVisible = ref(false);

const formData = reactive({
  id: null,
  maGiamGia: "",
  tenKhuyenMai: "",
  loaiKhuyenMai: "Phần trăm",
  giaTriKhuyenMai: 0,
  giaTriDonHangToiThieu: 0,
  giaTriKhuyenMaiToiDa: 0,
  ngayBatDau: "",
  ngayKetThuc: "",
  hangToiThieu: "MEMBER",
  soDiemCanDeDoi: 0,
  soLuong: 0,
  dieuKienApDung: "",
  congKhai: true,
  trangThaiPhieuGiamGia: "ACTIVE",
  trangThaiPhatHanh: "NOT_ISSUED"
});

const errors = reactive({
  maGiamGia: "",
  tenKhuyenMai: "",
  giaTriKhuyenMai: "",
  giaTriDonHangToiThieu: "",
  giaTriKhuyenMaiToiDa: "",
  ngayBatDau: "",
  ngayKetThuc: "",
  soDiemCanDeDoi: "",
  soLuong: "",
  dieuKienApDung: "",
});

// Methods
const resetForm = () => {
  formData.id = null;
  formData.maGiamGia = "";
  formData.tenKhuyenMai = "";
  formData.loaiKhuyenMai = "Phần trăm";
  formData.giaTriKhuyenMai = 0;
  formData.giaTriDonHangToiThieu = 0;
  formData.giaTriKhuyenMaiToiDa = 0;
  formData.ngayBatDau = "";
  formData.ngayKetThuc = "";
  formData.hangToiThieu = "MEMBER";
  formData.soDiemCanDeDoi = 0;
  formData.soLuong = 0;
  formData.dieuKienApDung = "";
  formData.trangThaiPhieuGiamGia = "ACTIVE";
  formData.trangThaiPhatHanh = "NOT_ISSUED";
  formData.congKhai = true;
};

const resetErrors = () => {
  Object.keys(errors).forEach(key => {
    errors[key] = "";
  });
};

const validateForm = () => {
  resetErrors();
  let isValid = true;

  if (formData.maGiamGia && formData.maGiamGia.trim().length > 0) {
    if (formData.maGiamGia.length < 3) {
      errors.maGiamGia = "Mã giảm giá phải có ít nhất 3 ký tự";
      isValid = false;
    } else if (formData.maGiamGia.length > 10) {
      errors.maGiamGia = "Mã giảm giá không được vượt quá 10 ký tự";
      isValid = false;
    }
  }

  if (!formData.tenKhuyenMai.trim()) {
    errors.tenKhuyenMai = "Tên khuyến mại là bắt buộc";
    isValid = false;
  } else if (formData.tenKhuyenMai.length < 3) {
    errors.tenKhuyenMai = "Tên khuyến mại phải có ít nhất 3 ký tự";
    isValid = false;
  } else if (formData.tenKhuyenMai.length > 255) {
    errors.tenKhuyenMai = "Tên khuyến mại không được vượt quá 255 ký tự";
    isValid = false;
  }

  if (formData.giaTriKhuyenMai <= 0) {
    errors.giaTriKhuyenMai = "Giá trị khuyến mãi phải lớn hơn 0";
    isValid = false;
  } else if (formData.loaiKhuyenMai === "Phần trăm" && formData.giaTriKhuyenMai > 100) {
    errors.giaTriKhuyenMai = "Giá trị khuyến mãi không được vượt quá 100%";
    isValid = false;
  }

  if (!formData.ngayBatDau) {
    errors.ngayBatDau = "Ngày bắt đầu là bắt buộc";
    isValid = false;
  }

  if (!formData.ngayKetThuc) {
    errors.ngayKetThuc = "Ngày kết thúc là bắt buộc";
    isValid = false;
  } else if (formData.ngayBatDau && formData.ngayKetThuc) {
    const startDate = new Date(formData.ngayBatDau);
    const endDate = new Date(formData.ngayKetThuc);
    if (endDate < startDate) {
      errors.ngayKetThuc = "Ngày kết thúc phải sau hoặc bằng ngày bắt đầu";
      isValid = false;
    }
  }

  if (formData.giaTriKhuyenMaiToiDa <= 10000.00) {
    errors.giaTriKhuyenMaiToiDa = "Giá trị khuyến mãi tối đa không được ít hơn 10000.00";
    isValid = false;
  }

  if (formData.soLuong <= 0) {
    errors.soLuong = "Số lượng phải lớn hơn 0";
    isValid = false;
  }

  if (!formData.dieuKienApDung.trim()) {
    errors.dieuKienApDung = "Điều kiện áp dụng là bắt buộc";
    isValid = false;
  }

  return isValid;
};

const loadPhieuGiamGia = async (page) => {
  try {
    isLoading.value = true;
    const response = await getAll({
      page: page,
      size: pageSize.value,
      search: search.value || null,
      trangThai: trangThaiFilter.value || null,
      ngayBatDau: ngayBatDauFilter.value || null,
      ngayKetThuc: ngayKetThucFilter.value || null,
    });
    phieuGiamGias.value = response.content;
    totalPages.value = response.totalPages;
    total.value = response.totalElements;
    currentPage.value = page;
    console.log(response)
  } catch (err) {
    console.error(err.message || "Lỗi lấy danh sách phiếu giảm giá");
    ElMessage.error(err.message || "Lỗi lấy danh sách phiếu giảm giá");
  } finally {
    isLoading.value = false;
  }
};

const openDialog = () => {
  resetForm();
  resetErrors();
  phieuGiamGiaDialogVisible.value = true;
};

const savePhieuGiamGia = async () => {
  if (!validateForm()) {
    ElMessage.error("Vui lòng kiểm tra các lỗi trong form");
    return;
  }
  
  try {
    isLoading.value = true;
    const payload = {
      ...formData,
      giaTriKhuyenMai: formData.giaTriKhuyenMai.toString(),
      giaTriDonHangToiThieu: formData.giaTriDonHangToiThieu.toString(),
      giaTriKhuyenMaiToiDa: formData.giaTriKhuyenMaiToiDa.toString(),
      soDiemCanDeDoi: formData.soDiemCanDeDoi.toString(),
      hangToiThieu: formData.congKhai ? null : formData.hangToiThieu,
    };

    if (formData.id) {
      await update(formData.id, payload);
      ElMessage.success("Cập nhật khuyến mãi thành công!");
    } else {
      await add(payload);
      ElMessage.success("Tạo khuyến mãi thành công!");
    }
    
    phieuGiamGiaDialogVisible.value = false;
    loadPhieuGiamGia(currentPage.value);
  } catch (error) {
    console.error(error);
    ElMessage.error(error.message || "Đã xảy ra lỗi không xác định");
  } finally {
    isLoading.value = false;
  }
};

const handleDeletePhieuGiamGia = async (id) => {
  try {
    await ElMessageBox.confirm("Bạn có chắc chắn muốn xóa khuyến mãi này?", 'Xác nhận', {
      confirmButtonText: 'Xóa',
      cancelButtonText: 'Hủy',
      type: 'warning'
    });
    
    isLoading.value = true;
    await deletePhieuGiamGia(id);
    ElMessage.success("Xóa khuyến mãi thành công!");
    loadPhieuGiamGia(currentPage.value);
  } catch (error) {
    if (error === 'cancel') {
      ElMessage.info('Đã hủy xóa');
    } else {
      console.error(error.message || "Lỗi khi xóa khuyến mãi");
      ElMessage.error(error.message || "Lỗi khi xóa khuyến mãi");
    }
  } finally {
    isLoading.value = false;
  }
};

const viewUpdate = async (id) => {
  try {
    const response = await detail(id);
    Object.assign(formData, response);
    phieuGiamGiaDialogVisible.value = true;
  } catch (error) {
    console.error(error.message || "Lỗi khi tải chi tiết khuyến mãi");
    ElMessage.error(error.message || "Lỗi khi tải chi tiết khuyến mãi");
  }
  resetErrors();
};

const clear = () => {
  currentPage.value = 0;
  search.value = "";
  trangThaiFilter.value = null;
  ngayBatDauFilter.value = null;
  ngayKetThucFilter.value = null;
  loadPhieuGiamGia(0);
};

const handleSizeChange = (newSize) => {
  pageSize.value = newSize;
  loadPhieuGiamGia(0);
};

// Utility functions
const trangThaiLabels = {
  NOT_STARTED: "Chưa bắt đầu",
  ACTIVE: "Đang hoạt động",
  EXPIRED: "Đã hết hạn",
};

const convertTrangThai = (trangThai) => {
  return trangThaiLabels[trangThai] || "Không xác định";
};

const getStatusType = (status) => {
  const statusTypes = {
    NOT_STARTED: 'info',
    ACTIVE: 'success',
    EXPIRED: 'danger'
  };
  return statusTypes[status] || 'info';
};

const getRowClassName = ({ row, rowIndex }) => {
  return rowIndex % 2 === 0 ? 'even-row' : 'odd-row';
};

const getTrangThaiSwitchConfig = (currentStatus) => {
  return {
    activeValue: "ISSUED",
    inactiveValue: currentStatus === "ISSUED" ? "STOP_ISSUED" : "NOT_ISSUED",
    activeText: "Phát hành",
    inactiveText: "",
  };
};

const switchConfig = computed(() => {
  return getTrangThaiSwitchConfig(formData.trangThaiPhatHanh);
});

const formatDate = (dateStr) => {
  if (!dateStr) return "";
  const date = new Date(dateStr);
  return `${date.getDate().toString().padStart(2, "0")}/${(date.getMonth() + 1).toString().padStart(2, "0")}/${date.getFullYear()}`;
};

const formatCurrency = (value) => {
  if (value == null) return "0 ₫";
  return new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(value);
};

// Computed properties
const isAdmin = computed(() => {
  const roles = store.state.roles;
  return Array.isArray(roles) && roles
    .map((role) => (typeof role === "string" ? role : role.authority))
    .includes("ROLE_ADMIN");
});

// Watchers
watch([search, trangThaiFilter, ngayBatDauFilter, ngayKetThucFilter], () => {
  loadPhieuGiamGia(0);
});

// Lifecycle
onMounted(() => {
  loadPhieuGiamGia(currentPage.value);
});
</script>

<style scoped>
.promotion-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 24px;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

/* Header Styles */
.page-header {
  background: white;
  border-radius: 16px;
  padding: 32px;
  margin-bottom: 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 24px;
}

.title-section {
  flex: 1;
}

.page-title {
  font-size: 32px;
  font-weight: 700;
  color: #1a202c;
  margin: 0 0 8px 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.page-subtitle {
  font-size: 16px;
  color: #64748b;
  margin: 0;
}

.create-btn {
  height: 48px;
  padding: 0 24px;
  border-radius: 12px;
  font-weight: 600;
  font-size: 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
  transition: all 0.3s ease;
}

.create-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.6);
}

.btn-icon {
  margin-right: 8px;
}

/* Filters Card */
.filters-card {
  margin-bottom: 24px;
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.filters-card :deep(.el-card__body) {
  padding: 24px;
}

.filters-content {
  width: 100%;
}

.filter-group {
  display: grid;
  grid-template-columns: 2fr 1.5fr 1.5fr 1.5fr auto;
  gap: 20px;
  align-items: end;
}

.filter-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.filter-label {
  font-size: 14px;
  font-weight: 500;
  color: #374151;
}

.filter-input :deep(.el-input__wrapper) {
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  transition: all 0.3s ease;
}

.filter-input :deep(.el-input__wrapper:hover) {
  border-color: #cbd5e1;
}

.filter-input :deep(.el-input__wrapper.is-focus) {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.search-icon {
  color: #9ca3af;
}

.reset-btn {
  padding: 0 16px;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  background: white;
  color: #6b7280;
  transition: all 0.3s ease;
}

.reset-btn:hover {
  border-color: #cbd5e1;
  background: #f9fafb;
}

/* Table Card */
.table-card {
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.table-card :deep(.el-card__body) {
  padding: 0;
}

.promotion-table {
  border-radius: 16px;
  overflow: hidden;
}

.promotion-table :deep(.el-table__header) {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
}

.promotion-table :deep(.el-table__header th) {
  background: transparent;
  color: #374151;
  font-weight: 600;
  font-size: 14px;
  border-bottom: 2px solid #e2e8f0;
  padding: 16px 12px;
}

.promotion-table :deep(.el-table__body tr) {
  transition: all 0.3s ease;
}

.promotion-table :deep(.el-table__body tr:hover) {
  background: #f8fafc;
}

.promotion-table :deep(.even-row) {
  background: #fafbfc;
}

.promotion-table :deep(.odd-row) {
  background: white;
}

.promotion-table :deep(.el-table__body td) {
  padding: 16px 12px;
  border-bottom: 1px solid #f1f5f9;
}

/* Table Cell Styles */
.row-index {
  font-weight: 600;
  color: #6b7280;
}

.code-cell {
  display: flex;
  align-items: center;
}

.code-tag {
  font-family: 'Monaco', 'Menlo', monospace;
  font-weight: 600;
}

.name-cell {
  display: flex;
  align-items: center;
}

.promotion-name {
  font-weight: 500;
  color: #1f2937;
}

.value-cell {
  text-align: center;
}

.discount-percent {
  font-weight: 700;
  color: #059669;
  font-size: 16px;
}

.discount-amount {
  font-weight: 600;
  color: #dc2626;
}

.date-range {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.date-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #6b7280;
}

.date-icon {
  font-size: 12px;
  color: #9ca3af;
}

.date-separator {
  text-align: center;
  color: #9ca3af;
  font-size: 12px;
}

.action-buttons {
  display: flex;
  gap: 8px;
  justify-content: center;
}

.action-btn {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.edit-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
}

.delete-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.4);
}

/* Pagination */
.pagination-wrapper {
  padding: 24px;
  display: flex;
  justify-content: center;
  background: #fafbfc;
  border-top: 1px solid #f1f5f9;
}

.custom-pagination :deep(.el-pagination) {
  gap: 8px;
}

.custom-pagination :deep(.el-pager li) {
  border-radius: 8px;
  margin: 0 2px;
  transition: all 0.3s ease;
}

.custom-pagination :deep(.el-pager li:hover) {
  background: #667eea;
  color: white;
}

.custom-pagination :deep(.el-pager li.is-active) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

/* Dialog Styles */
.promotion-dialog :deep(.el-dialog) {
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}

.promotion-dialog :deep(.el-dialog__header) {
  padding: 24px 24px 0;
  border-bottom: none;
}

.promotion-dialog :deep(.el-dialog__title) {
  font-size: 24px;
  font-weight: 700;
  color: #1a202c;
}

.dialog-content {
  padding: 0 24px;
  max-height: 70vh;
  overflow-y: auto;
}

.promotion-form {
  padding: 24px 0;
}

.form-grid {
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.form-section {
  background: #f8fafc;
  border-radius: 12px;
  padding: 24px;
  border: 1px solid #e2e8f0;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #374151;
  margin: 0 0 20px 0;
  padding-bottom: 12px;
  border-bottom: 2px solid #e2e8f0;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 20px;
}

.form-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-item.full-width {
  grid-column: 1 / -1;
}

.form-item :deep(.el-form-item__label) {
  font-weight: 500;
  color: #374151;
  margin-bottom: 8px;
}

.form-item :deep(.el-input__wrapper),
.form-item :deep(.el-select .el-input__wrapper),
.form-item :deep(.el-date-editor.el-input),
.form-item :deep(.el-textarea__inner) {
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  transition: all 0.3s ease;
}

.form-item :deep(.el-input__wrapper:hover),
.form-item :deep(.el-select .el-input__wrapper:hover),
.form-item :deep(.el-date-editor.el-input:hover),
.form-item :deep(.el-textarea__inner:hover) {
  border-color: #cbd5e1;
}

.form-item :deep(.el-input__wrapper.is-focus),
.form-item :deep(.el-select .el-input__wrapper.is-focus),
.form-item :deep(.el-date-editor.el-input.is-focus),
.form-item :deep(.el-textarea__inner:focus) {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-item.error :deep(.el-input__wrapper),
.form-item.error :deep(.el-select .el-input__wrapper),
.form-item.error :deep(.el-date-editor.el-input),
.form-item.error :deep(.el-textarea__inner) {
  border-color: #ef4444;
}

.error-text {
  font-size: 12px;
  color: #ef4444;
  margin-top: 4px;
}

.full-width {
  width: 100%;
}

.status-switch :deep(.el-switch__label) {
  font-weight: 500;
}

/* Dialog Footer */
.dialog-footer {
  padding: 24px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  border-top: 1px solid #f1f5f9;
}

.cancel-btn {
  height: 44px;
  padding: 0 24px;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  background: white;
  color: #6b7280;
  font-weight: 500;
}

.save-btn {
  height: 44px;
  padding: 0 24px;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  font-weight: 600;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.save-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.6);
}

/* Responsive Design */
@media (max-width: 1200px) {
  .filter-group {
    grid-template-columns: 1fr;
    gap: 16px;
  }
  
  .form-row {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .promotion-container {
    padding: 16px;
  }
  
  .page-header {
    padding: 24px;
  }
  
  .header-content {
    flex-direction: column;
    align-items: stretch;
    gap: 16px;
  }
  
  .page-title {
    font-size: 24px;
  }
  
  .promotion-table :deep(.el-table__header th),
  .promotion-table :deep(.el-table__body td) {
    padding: 12px 8px;
  }
  
  .action-buttons {
    flex-direction: column;
    gap: 4px;
  }
  
  .dialog-content {
    max-height: 60vh;
  }
  
  .form-section {
    padding: 16px;
  }
  
  .dialog-footer {
    flex-direction: column;
    gap: 8px;
  }
  
  .cancel-btn,
  .save-btn {
    width: 100%;
  }
}

@media (max-width: 480px) {
  .promotion-table :deep(.el-table__header th),
  .promotion-table :deep(.el-table__body td) {
    padding: 8px 4px;
    font-size: 12px;
  }
  
  .date-range {
    font-size: 10px;
  }
  
  .action-btn {
    width: 28px;
    height: 28px;
  }
}
</style>
