<template>
  <div class="container">
    <div class="header-content">
      <h3><b>Quản lý khuyến mãi</b></h3>
      <el-button type="primary" style="font-size: 16px; padding: 18px 20px;" @click="openDialog" v-if="isAdmin">
        <el-icon style="margin-right: 5px;">
          <Plus />
        </el-icon>Tạo khuyến mãi
      </el-button>
    </div>

    <!-- Bộ lọc -->
    <el-card class="box-card" style="margin-top: 16px;">
      <el-row :gutter="20">
        <el-col :span="6" style="max-width: 30%;">
          <el-input v-model="search" placeholder="Tìm kiếm theo mã hoặc tên" clearable />
        </el-col>
        <el-col :span="5" style="max-width: 30%;">
          <el-select v-model="trangThaiFilter" placeholder="Trạng thái" clearable>
            <el-option label="Tất cả" :value="null"></el-option>
            <el-option label="Chưa bắt đầu" value="NOT_STARTED"></el-option>
            <el-option label="Đang hoạt động" value="ACTIVE"></el-option>
            <el-option label="Đã hết hạn" value="EXPIRED"></el-option>
          </el-select>
        </el-col>
        <el-col :span="5" style="max-width: 30%;">
          <el-date-picker v-model="ngayBatDauFilter" type="date" placeholder="Ngày bắt đầu" value-format="YYYY-MM-DD"
            clearable />
        </el-col>
        <el-col :span="5" style="max-width: 30%;">
          <el-date-picker v-model="ngayKetThucFilter" type="date" placeholder="Ngày kết thúc" value-format="YYYY-MM-DD"
            clearable />
        </el-col>
        <el-col :span="3" style="max-width: 30%;">
          <el-button @click="clear">Làm mới</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- Bảng -->
    <el-table v-loading="isLoading" :data="phieuGiamGias" style="width: 100%; margin-top: 16px;" border>
      <el-table-column label="STT" width="60">
        <template #default="scope">
          {{ scope.$index + 1 + currentPage * pageSize }}
        </template>
      </el-table-column>
      <el-table-column prop="maGiamGia" label="Mã" width="120" />
      <el-table-column prop="tenKhuyenMai" label="Tên" />
      <el-table-column label="Giá trị giảm" width="140">
        <template #default="scope">
          <span v-if="scope.row.loaiKhuyenMai === 'Phần trăm'">
            {{ scope.row.giaTriKhuyenMai }}%
          </span>
          <span v-else>
            {{ formatCurrency(scope.row.giaTriKhuyenMai) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="Bắt đầu" width="120">
        <template #default="scope">
          {{ formatDate(scope.row.ngayBatDau) }}
        </template>
      </el-table-column>
      <el-table-column label="Kết thúc" width="120">
        <template #default="scope">
          {{ formatDate(scope.row.ngayKetThuc) }}
        </template>
      </el-table-column>
      <el-table-column prop="soLuong" label="Số lượng" width="100" />
      <el-table-column prop="trangThaiPhieuGiamGia" label="Trạng thái" width="130">
        <template #default="scope">
          {{ convertTrangThai(scope.row.trangThaiPhieuGiamGia) }}
        </template>
      </el-table-column>
      <el-table-column label="Loại" width="100">
        <template #default="scope">
          {{ scope.row.congKhai ? 'Công khai' : 'Riêng tư' }}
        </template>
      </el-table-column>
      <el-table-column label="Thao tác" width="140"v-if="isAdmin">
        <template #default="scope">
          <el-button size="small" type="primary" @click="viewUpdate(scope.row.id)">
            <el-icon>
              <Edit />
            </el-icon>
          </el-button>
          <el-button size="small" type="danger" @click="handleDeletePhieuGiamGia(scope.row.id)">
            <el-icon>
              <Delete />
            </el-icon>
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Phân trang -->
    <div class="pagination" style="margin-top: 16px; text-align: center;">
      <el-pagination background layout="prev, pager, next" :current-page="currentPage + 1" :page-size="pageSize"
        :total="totalPages * pageSize" @current-change="(p) => loadPhieuGiamGia(p - 1)" />
    </div>

    <!-- Dialog -->
    <el-dialog v-model="phieuGiamGiaDialogVisible"
      :title="formData.id ? 'Chỉnh sửa phiếu giảm giá' : 'Thêm phiếu giảm giá'" width="640px" style="margin-top: 0;">
      <el-form :model="formData" label-width="200px">
        <el-form-item label="Mã giảm giá">
          <el-input v-model="formData.maGiamGia" />
        </el-form-item>
        <el-form-item label="Tên khuyến mãi" required :error="errors.tenKhuyenMai">
          <el-input v-model="formData.tenKhuyenMai" />
        </el-form-item>
        <el-form-item label="Loại khuyến mãi">
          <el-select v-model="formData.loaiKhuyenMai">
            <el-option value="Phần trăm" />
            <el-option value="Cố định" />
          </el-select>
        </el-form-item>
        <el-form-item label="Giá trị khuyến mãi" required :error="errors.giaTriKhuyenMai">
          <el-input-number v-model="formData.giaTriKhuyenMai" :min="0" />
        </el-form-item>
        <el-form-item label="Giá trị đơn hàng tối thiểu" :error="errors.giaTriDonHangToiThieu">
          <el-input-number v-model="formData.giaTriDonHangToiThieu" :min="0" />
        </el-form-item>
        <el-form-item label="Giá trị khuyến mãi tối đa" v-if="formData.loaiKhuyenMai === 'Phần trăm'"
          :error="errors.giaTriKhuyenMaiToiDa">
          <el-input-number v-model="formData.giaTriKhuyenMaiToiDa" :min="0" />
        </el-form-item>
        <el-form-item label="Ngày bắt đầu" required :error="errors.ngayBatDau">
          <el-date-picker v-model="formData.ngayBatDau" type="date" value-format="YYYY-MM-DD" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="Ngày kết thúc" required :error="errors.ngayKetThuc">
          <el-date-picker v-model="formData.ngayKetThuc" type="date" value-format="YYYY-MM-DD" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="Số lượng" required :error="errors.soLuong">
          <el-input-number v-model="formData.soLuong" :min="1" />
        </el-form-item>
        <el-form-item label="Số điểm cần đổi" :error="errors.soDiemCanDeDoi">
          <el-input-number v-model="formData.soDiemCanDeDoi" :min="0" />
        </el-form-item>
        <el-form-item label="Loại phiếu">
          <el-select v-model="formData.congKhai">
            <el-option :value="true" label="Công khai" />
            <el-option :value="false" label="Riêng tư" />
          </el-select>
        </el-form-item>
        <el-form-item label="Hạng tối thiểu" v-if="!formData.congKhai">
          <el-select v-model="formData.hangToiThieu">
            <el-option value="SILVER" label="Bạc" />
            <el-option value="GOLD" label="Vàng" />
            <el-option value="DIAMOND" label="Kim cương" />
          </el-select>
        </el-form-item>
        <el-form-item label="Điều kiện áp dụng" required :error="errors.dieuKienApDung">
          <el-input v-model="formData.dieuKienApDung" />
        </el-form-item>
        <el-form-item label="Trạng thái">
          <el-switch
  v-model="formData.trangThaiPhatHanh"
  active-text="Phát hành"
  inactive-text=""
  active-value="ISSUED"
  inactive-value="NOT_ISSUED"
/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="phieuGiamGiaDialogVisible = false">Hủy</el-button>
        <el-button type="primary" @click="savePhieuGiamGia">Lưu</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, reactive, computed } from "vue";
import { getAll, detail, add, update, deletePhieuGiamGia, getAllSanPham } from "@/Service/Adminservice/PhieuGiamGia/PhieuGiamGiaAdminService";
import { ElMessage, ElMessageBox } from "element-plus";
import { Delete, Plus } from '@element-plus/icons-vue'
import { Edit } from "lucide-vue-next";
import store from "@/Service/LoginService/Store";

const isLoading = ref(false);
const phieuGiamGias = ref([]);

const currentPage = ref(0);
const totalPages = ref(0);
const pageSize = ref(5);
const search = ref("");
const trangThaiFilter = ref(null);
const ngayBatDauFilter = ref(null);
const ngayKetThucFilter = ref(null);

const phieuGiamGiaDialogVisible = ref(false);
const dialog = ref(null);

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
  trangThaiPhatHanh: false
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
  formData.trangThaiPhatHanh = false;
  formData.congKhai = true;
};

const resetErrors = () => {
  errors.maGiamGia = "";
  errors.tenKhuyenMai = "";
  errors.giaTriKhuyenMai = "";
  errors.giaTriDonHangToiThieu = "";
  errors.giaTriKhuyenMaiToiDa = "";
  errors.ngayBatDau = "";
  errors.ngayKetThuc = "";
  errors.soDiemCanDeDoi = "";
  errors.soLuong = "";
  errors.dieuKienApDung = "";
};

const validateForm = () => {
  resetErrors();
  let isValid = true;

  if (!formData.maGiamGia.trim()) {
    errors.maGiamGia = "Tên khuyến mại là bắt buộc";
    isValid = false;
  } else if (formData.maGiamGia.length < 3) {
    errors.maGiamGia = "Tên khuyến mại phải có ít nhất 3 ký tự";
    isValid = false;
  } else if (formData.maGiamGia.length > 50) {
    errors.maGiamGia = "Tên khuyến mại không được vượt quá 50 ký tự";
    isValid = false;
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

  if (formData.giaTriDonHangToiThieu < 0) {
    errors.giaTriDonHangToiThieu = "Giá trị đơn hàng tối thiểu không được âm";
    isValid = false;
  }

  if (formData.giaTriKhuyenMaiToiDa < 0) {
    errors.giaTriKhuyenMaiToiDa = "Giá trị khuyến mãi tối đa không được âm";
    isValid = false;
  } else if (
    formData.loaiKhuyenMai === "Cố định" &&
    formData.giaTriKhuyenMaiToiDa > 0 &&
    formData.giaTriKhuyenMaiToiDa < formData.giaTriKhuyenMai
  ) {
    errors.giaTriKhuyenMaiToiDa = "Giá trị khuyến mãi tối đa phải lớn hơn giá trị khuyến mãi";
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
      console.log(endDate, startDate)
      errors.ngayKetThuc = "Ngày kết thúc phải sau hoặc bằng ngày bắt đầu";
      isValid = false;
    }
  }

  if (formData.soDiemCanDeDoi < 0) {
    errors.soDiemCanDeDoi = "Số điểm cần để đổi không được âm";
    isValid = false;
  }

  if (formData.soLuong <= 0) {
    errors.soLuong = "Số lượng phải lớn hơn 0";
    isValid = false;
  }

  if (!formData.dieuKienApDung.trim()) {
    errors.dieuKienApDung = "Điều kiện áp dụng là bắt buộc";
    isValid = false;
  } else if (formData.dieuKienApDung.length < 3) {
    errors.dieuKienApDung = "Điều kiện áp dụng phải có ít nhất 3 ký tự";
    isValid = false;
  } else if (formData.dieuKienApDung.length > 255) {
    errors.dieuKienApDung = "Điều kiện áp dụng không được vượt quá 255 ký tự";
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
    currentPage.value = page;
    console.log(phieuGiamGias.value)
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
      hangToiThieu: formData.congKhai ? 'MEMBER' : formData.hangToiThieu,
      giaTriKhuyenMai: formData.giaTriKhuyenMai.toString(),
      giaTriDonHangToiThieu: formData.giaTriDonHangToiThieu.toString(),
      giaTriKhuyenMaiToiDa: formData.giaTriKhuyenMaiToiDa.toString(),
      soDiemCanDeDoi: formData.soDiemCanDeDoi.toString()
    };
    console.log(payload)
    if (formData.id) {
      await update(formData.id, payload);
      ElMessage.success("Cập nhật phiếu giảm giá thành công!");
    } else {
      await add(payload);
      ElMessage.success("Thêm phiếu giảm giá thành công!");
    }
    phieuGiamGiaDialogVisible.value = false;
    dialog.value?.close();
    loadPhieuGiamGia(currentPage.value);
  } catch (error) {
    console.error(error);
    resetErrors();
    if (error.response?.data?.errors) {
      const backendErrors = error.response.data.errors;
      Object.keys(backendErrors).forEach((key) => {
        if (errors[key] !== undefined) {
          errors[key] = backendErrors[key];
        }
      });
      ElMessage.error("Vui lòng kiểm tra các lỗi trong form");
    } else {
      ElMessage.error(error.message || "Đã xảy ra lỗi không xác định");
    }
  } finally {
    isLoading.value = false;
  }
};

const handleDeletePhieuGiamGia = async (id) => {
  try {
    await ElMessageBox.confirm("Bạn có chắc chắn muốn xóa phiếu giảm giá này?", ' Xác nhận', {
      confirmButtonText: 'Xoá',
      cancelButtonText: 'Huỷ',
      type: 'warning'
    });
    isLoading.value = true;
    await deletePhieuGiamGia(id);
    ElMessage.success("Xóa phiếu giảm giá thành công!");
    loadPhieuGiamGia(currentPage.value);
  } catch (error) {
    if (error === 'cancel') {
      ElMessage.info('Đã huỷ xoá');
    } else {
      console.error(error.message || "Lỗi khi xóa phiếu giảm giá");
      ElMessage.error(error.message || "Lỗi khi xóa phiếu giảm giá");
    }
  } finally {
    isLoading.value = false;
  }
};

const nextPage = () => {
  if (currentPage.value < totalPages.value - 1) {
    loadPhieuGiamGia(currentPage.value + 1);
  }
};

const previousPage = () => {
  if (currentPage.value > 0) {
    loadPhieuGiamGia(currentPage.value - 1);
  }
};

const clear = () => {
  currentPage.value = 0;
  search.value = "";
  trangThaiFilter.value = null;
  ngayBatDauFilter.value = null;
  ngayKetThucFilter.value = null;
  loadPhieuGiamGia(0);
};

const trangThaiLabels = {
  NOT_STARTED: "Chưa bắt đầu",
  ACTIVE: "Đang hoạt động",
  EXPIRED: "Đã hết hạn",
};

const convertTrangThai = (trangThai) => {
  return trangThaiLabels[trangThai] || "Không xác định";
};

const viewUpdate = async (id) => {
  try {
    const response = await detail(id);
    Object.assign(formData, {
      ...response,
      sanPhamIds: response.sanPhamIds || [],
      khachHangMoi: response.khachHangMoi || false,
      khachHangCu: response.khachHangCu || false,
    });
    phieuGiamGiaDialogVisible.value = true;
  } catch (error) {
    console.error(error.message || "Lỗi khi tải chi tiết phiếu giảm giá");
    ElMessage.error(error.message || "Lỗi khi tải chi tiết phiếu giảm giá");
  }
  resetErrors()
};

const formatDate = (dateStr) => {
  if (!dateStr) return "";
  const date = new Date(dateStr);
  return `${date.getDate().toString().padStart(2, "0")}/${(date.getMonth() + 1).toString().padStart(2, "0")}/${date.getFullYear()}`;
};

const formatCurrency = (value) => {
  if (value == null) return "0 ₫";
  return new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(value);
};

watch([search, trangThaiFilter, ngayBatDauFilter, ngayKetThucFilter], () => {
  loadPhieuGiamGia(0);
});

watch(phieuGiamGiaDialogVisible, (visible) => {
  if (visible) {
    dialog.value?.showModal();
  } else {
    dialog.value?.close();
  }
});

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

onMounted(() => {
  loadPhieuGiamGia(currentPage.value);
});
</script>

<style scoped>
.container {
  padding: 20px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  /* cho phép xuống hàng khi thu nhỏ */
  gap: 10px;
}

.pagination {
  margin-top: 20px;
}

/* Responsive cho bộ lọc */
@media (max-width: 900px) {
  .box-card .el-row {
    flex-direction: column !important;
  }

  .box-card .el-col {
    width: 100% !important;
    max-width: 100% !important;
    padding-left: 0 !important;
    padding-right: 0 !important;
    margin-bottom: 10px;
  }

  .pagination {
    text-align: center;
  }
}

/* Giữ nguyên chiều cao table */
/* .el-table {
  min-height: 300px;
} */
</style>
