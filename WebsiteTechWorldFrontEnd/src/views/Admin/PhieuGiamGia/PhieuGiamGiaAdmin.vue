<script setup>
import { ref, onMounted, watch, reactive } from "vue";
import { getAll, detail, add, update, deletePhieuGiamGia, getAllKhachHang } from "@/Service/Adminservice/PhieuGiamGia/PhieuGiamGiaAdminService";
import { ElMessage } from "element-plus";

const phieuGiamGias = ref([]);
const isLoading = ref(false);
const phieuGiamGiaDialogVisible = ref(false);
const dialog = ref(null);
const khachHangList = ref([]);
const filteredKhachHangList = ref([]);
const searchKhachHang = ref("");

const currentPage = ref(0);
const totalPages = ref(0);
const pageSize = ref(5);

const search = ref("");
const trangThaiFilter = ref(null);
const ngayBatDauFilter = ref(null);
const ngayKetThucFilter = ref(null);

const formData = reactive({
  id: null,
  maGiamGia: "#####",
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
  trangThai: "ACTIVE",
  isGlobal: true,
  khachHangIds: [],
  khachHangMoi: false,
  khachHangCu: false,
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
  khachHangIds: "",
});

const resetForm = () => {
  formData.id = null;
  formData.maGiamGia = "#####";
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
  formData.trangThai = "ACTIVE";
  formData.isGlobal = true;
  formData.khachHangIds = [];
  formData.khachHangMoi = false;
  formData.khachHangCu = false;
  searchKhachHang.value = "";
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
  errors.khachHangIds = "";
};

const validateForm = () => {
  resetErrors();
  let isValid = true;

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

  if (!formData.isGlobal && !formData.khachHangIds.length && !formData.khachHangMoi && !formData.khachHangCu) {
    errors.khachHangIds = "Phải chọn ít nhất một khách hàng hoặc nhóm khách hàng";
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
  } catch (err) {
    console.error(err.message || "Lỗi lấy danh sách phiếu giảm giá");
    ElMessage.error(err.message || "Lỗi lấy danh sách phiếu giảm giá");
  } finally {
    isLoading.value = false;
  }
};

const loadKhachHang = async () => {
  try {
    const response = await getAllKhachHang();
    khachHangList.value = response.content;
    filteredKhachHangList.value = response.content;
    if (!khachHangList.value.length) {
      ElMessage.warning("Không có khách hàng nào trong hệ thống!");
    }
  } catch (err) {
    console.error(err.message || "Lỗi lấy danh sách khách hàng");
    ElMessage.error(err.message || "Lỗi lấy danh sách khách hàng");
  }
};

const filterKhachHang = () => {
  if (!searchKhachHang.value) {
    filteredKhachHangList.value = khachHangList.value;
  } else {
    filteredKhachHangList.value = khachHangList.value.filter((khachHang) =>
      khachHang.tenKhachHang.toLowerCase().includes(searchKhachHang.value.toLowerCase())
    );
  }
};

const createPhieuGiamGia = () => {
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
    };

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
  if (!confirm("Bạn có chắc chắn muốn xóa phiếu giảm giá này?")) return;
  try {
    isLoading.value = true;
    await deletePhieuGiamGia(id);
    ElMessage.success("Xóa phiếu giảm giá thành công!");
    loadPhieuGiamGia(currentPage.value);
  } catch (error) {
    console.error(error.message || "Lỗi khi xóa phiếu giảm giá");
    ElMessage.error(error.message || "Lỗi khi xóa phiếu giảm giá");
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
      khachHangIds: response.khachHangIds || [],
      khachHangMoi: response.khachHangMoi || false,
      khachHangCu: response.khachHangCu || false,
    });
    phieuGiamGiaDialogVisible.value = true;
    filterKhachHang();
  } catch (error) {
    console.error(error.message || "Lỗi khi tải chi tiết phiếu giảm giá");
    ElMessage.error(error.message || "Lỗi khi tải chi tiết phiếu giảm giá");
  }
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

watch(() => formData.isGlobal, (newIsGlobal) => {
  if (newIsGlobal) {
    formData.khachHangIds = [];
    formData.khachHangMoi = false;
    formData.khachHangCu = false;
    searchKhachHang.value = "";
    filterKhachHang();
  }
});

watch(searchKhachHang, () => {
  filterKhachHang();
});

onMounted(() => {
  loadPhieuGiamGia(currentPage.value);
  loadKhachHang();
});
</script>

<template>
  <div class="container">
    <div class="header-content">
      <div class="title-section">
        <h1 class="dashboard-title">Quản lý khuyến mãi</h1>
      </div>
      <div class="header-actions">
        <button @click="createPhieuGiamGia" class="btn btn-outline-primary">
          <svg class="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
          </svg>Tạo khuyến mãi mới
        </button>
      </div>
    </div>

    <!-- Bộ lọc -->
    <div class="search-card">
      <div class="search-content">
        <div class="row g-3">
          <div class="col-md-4">
            <input v-model="search" type="text" placeholder="Tìm kiếm theo mã hoặc tên..." class="form-control" />
          </div>
          <div class="col-md-2">
            <select v-model="trangThaiFilter" class="form-select">
              <option :value="null">-- Tất cả trạng thái --</option>
              <option value="NOT_STARTED">Chưa bắt đầu</option>
              <option value="ACTIVE">Đang hoạt động</option>
              <option value="EXPIRED">Đã hết hạn</option>
            </select>
          </div>
          <div class="col-md-2">
            <input v-model="ngayBatDauFilter" type="date" class="form-control" placeholder="Ngày bắt đầu" />
          </div>
          <div class="col-md-2">
            <input v-model="ngayKetThucFilter" type="date" class="form-control" placeholder="Ngày kết thúc" />
          </div>
          <div class="col-md-2">
            <button @click="clear" class="btn btn-outline-secondary" style="margin: 0 auto;">Làm mới</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Bảng phiếu giảm giá -->
    <div class="table-container">
      <div v-if="isLoading" class="text-center">
        <div class="spinner-border" role="status">
          <span class="visually-hidden">Đang tải...</span>
        </div>
      </div>
      <table v-else class="table table-fixed">
        <thead>
          <tr>
            <th class="col-1">STT</th>
            <th class="col-2">Mã</th>
            <th class="col-3">Tên</th>
            <th class="col-4">Giá trị giảm</th>
            <th class="col-5">Ngày bắt đầu</th>
            <th class="col-6">Ngày kết thúc</th>
            <th class="col-7">Số lượng</th>
            <th class="col-8">Trạng thái</th>
            <th class="col-9">Loại</th>
            <th class="col-10">Thao tác</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="phieuGiamGias.length === 0">
            <td colspan="10" class="text-center">Không có phiếu giảm giá nào.</td>
          </tr>
          <tr v-else v-for="(phieuGiamGia, index) in phieuGiamGias" :key="index">
            <template v-if="phieuGiamGia">
              <td class="col-1">{{ index + 1 + currentPage * pageSize }}</td>
              <td class="col-2">{{ phieuGiamGia.maGiamGia }}</td>
              <td class="col-3" :title="phieuGiamGia.tenKhuyenMai">{{ phieuGiamGia.tenKhuyenMai }}</td>
              <td class="col-4">
                {{ phieuGiamGia.loaiKhuyenMai === 'Phần trăm' ? phieuGiamGia.giaTriKhuyenMai + '%' :
                  formatCurrency(phieuGiamGia.giaTriKhuyenMai) }}
              </td>
              <td class="col-5">{{ formatDate(phieuGiamGia.ngayBatDau) }}</td>
              <td class="col-6">{{ formatDate(phieuGiamGia.ngayKetThuc) }}</td>
              <td class="col-7">{{ phieuGiamGia.soLuong }}</td>
              <td class="col-8">{{ convertTrangThai(phieuGiamGia.trangThai) }}</td>
              <td class="col-9">{{ phieuGiamGia.isGlobal ? 'Công khai' : 'Riêng tư' }}</td>
              <td class="col-10">
                <button class="btn btn-outline-primary btn-sm" title="Chỉnh sửa" @click="viewUpdate(phieuGiamGia.id)">
                  <i class="bi bi-pencil-square"></i>
                </button>
                <button class="btn btn-outline-danger btn-sm" style="margin-left: 5%;"
                  @click="handleDeletePhieuGiamGia(phieuGiamGia.id)">
                  <i class="bi bi-trash"></i>
                </button>
              </td>
            </template>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Phân trang -->
    <div class="pagination">
      <button @click="previousPage" :disabled="currentPage === 0" class="btn btn-outline-primary">Trang trước</button>
      <span style="margin: auto 10px;">Trang {{ currentPage + 1 }} / {{ totalPages }}</span>
      <button @click="nextPage" :disabled="currentPage >= totalPages - 1" class="btn btn-outline-primary">Trang
        sau</button>
    </div>

    <!-- Dialog thêm/sửa -->
    <dialog ref="dialog" class="dialog p-4 rounded shadow-lg" style="width: 800px; margin: auto;">
      <form :model="formData" @submit.prevent="savePhieuGiamGia">
        <h4 class="mb-4">{{ formData.id ? 'Chỉnh sửa Phiếu Giảm Giá' : 'Thêm Phiếu Giảm Giá' }}</h4>
        <div v-if="formData" class="row g-3">
          <div class="col-md-6">
            <label for="maGiamGia" class="form-label">Mã giảm giá</label>
            <input id="maGiamGia" v-model="formData.maGiamGia" type="text" class="form-control" disabled />
          </div>
          <div class="col-md-6">
            <label for="tenKhuyenMai" class="form-label">Tên khuyến mại <span class="text-danger">*</span></label>
            <input id="tenKhuyenMai" v-model="formData.tenKhuyenMai" type="text" class="form-control" />
            <div v-if="errors.tenKhuyenMai" class="text-danger mt-1">{{ errors.tenKhuyenMai }}</div>
          </div>
          <div class="col-md-6">
            <label for="loaiKhuyenMai" class="form-label">Loại khuyến mãi</label>
            <select id="loaiKhuyenMai" v-model="formData.loaiKhuyenMai" class="form-select">
              <option value="Phần trăm">Phần trăm (%)</option>
              <option value="Cố định">VNĐ</option>
            </select>
          </div>
          <div class="col-md-6">
            <label for="giaTriKhuyenMai" class="form-label">Giá trị khuyến mãi <span
                class="text-danger">*</span></label>
            <input id="giaTriKhuyenMai" v-model.number="formData.giaTriKhuyenMai" type="number" class="form-control"
              min="0" />
            <div v-if="errors.giaTriKhuyenMai" class="text-danger mt-1">{{ errors.giaTriKhuyenMai }}</div>
          </div>
          <div class="col-md-6">
            <label for="giaTriDonHangToiThieu" class="form-label">Giá trị đơn hàng tối thiểu</label>
            <input id="giaTriDonHangToiThieu" v-model.number="formData.giaTriDonHangToiThieu" type="number"
              class="form-control" min="0" />
            <div v-if="errors.giaTriDonHangToiThieu" class="text-danger mt-1">{{ errors.giaTriDonHangToiThieu }}</div>
          </div>
          <div class="col-md-6">
            <label for="giaTriKhuyenMaiToiDa" v-if="formData.loaiKhuyenMai == 'Cố định'" class="form-label">Giá trị khuyến mãi tối đa</label>
            <input id="giaTriKhuyenMaiToiDa" v-if="formData.loaiKhuyenMai == 'Cố định'" v-model.number="formData.giaTriKhuyenMaiToiDa" type="number"
              class="form-control" min="0" />
            <div v-if="errors.giaTriKhuyenMaiToiDa" class="text-danger mt-1">{{ errors.giaTriKhuyenMaiToiDa }}</div>
          </div>
          <div class="col-md-6">
            <label for="ngayBatDau" class="form-label">Ngày bắt đầu <span class="text-danger">*</span></label>
            <input id="ngayBatDau" v-model="formData.ngayBatDau" type="date" class="form-control" />
            <div v-if="errors.ngayBatDau" class="text-danger mt-1">{{ errors.ngayBatDau }}</div>
          </div>
          <div class="col-md-6">
            <label for="ngayKetThuc" class="form-label">Ngày kết thúc <span class="text-danger">*</span></label>
            <input id="ngayKetThuc" v-model="formData.ngayKetThuc" type="date" class="form-control" />
            <div v-if="errors.ngayKetThuc" class="text-danger mt-1">{{ errors.ngayKetThuc }}</div>
          </div>
          <div class="col-md-6">
            <label for="hangToiThieu" class="form-label">Hạng tối thiểu</label>
            <select id="hangToiThieu" v-model="formData.hangToiThieu" class="form-select">
              <option value="MEMBER">Thành viên</option>
              <option value="SILVER">Bạc</option>
              <option value="GOLD">Vàng</option>
              <option value="DIAMOND">Kim cương</option>
            </select>
          </div>
          <div class="col-md-6">
            <label for="soDiemCanDeDoi" class="form-label">Số điểm cần để đổi</label>
            <input id="soDiemCanDeDoi" v-model.number="formData.soDiemCanDeDoi" type="number" class="form-control"
              min="0" />
            <div v-if="errors.soDiemCanDeDoi" class="text-danger mt-1">{{ errors.soDiemCanDeDoi }}</div>
          </div>
          <div class="col-md-6">
            <label for="soLuong" class="form-label">Số lượng <span class="text-danger">*</span></label>
            <input id="soLuong" v-model.number="formData.soLuong" type="number" class="form-control" min="0" />
            <div v-if="errors.soLuong" class="text-danger mt-1">{{ errors.soLuong }}</div>
          </div>
          <div class="col-md-6">
            <label for="isGlobal" class="form-label">Loại phiếu</label>
            <select id="isGlobal" v-model="formData.isGlobal" class="form-select">
              <option :value="true">Công khai</option>
              <option :value="false">Riêng tư</option>
            </select>
          </div>
          <div v-if="!formData.isGlobal" class="col-md-6">
            <label class="form-label">Nhóm khách hàng</label>
            <div class="form-check">
              <input id="khachHangMoi" v-model="formData.khachHangMoi" type="checkbox" class="form-check-input" />
              <label for="khachHangMoi" class="form-check-label">Khách hàng mới</label>
            </div>
            <div class="form-check">
              <input id="khachHangCu" v-model="formData.khachHangCu" type="checkbox" class="form-check-input" />
              <label for="khachHangCu" class="form-check-label">Khách hàng cũ</label>
            </div>
          </div>
          <div v-if="!formData.isGlobal" class="col-md-6">
            <label for="khachHangIds" class="form-label">Khách hàng áp dụng <small>(Giữ Ctrl/Cmd để chọn
                nhiều)</small></label>
            <input v-model="searchKhachHang" type="text" class="form-control mb-2"
              placeholder="Tìm kiếm khách hàng..." />
            <select id="khachHangIds" v-model="formData.khachHangIds" multiple class="form-select" size="5">
              <option v-for="khachHang in filteredKhachHangList" :key="khachHang.id" :value="khachHang.id">
                #{{ khachHang.id }} - {{ khachHang.tenKhachHang }}
              </option>
            </select>
            <div v-if="errors.khachHangIds" class="text-danger mt-1">{{ errors.khachHangIds }}</div>
          </div>
          <div class="col-md-12">
            <label for="dieuKienApDung" class="form-label">Mô tả điều kiện áp dụng <span
                class="text-danger">*</span></label>
            <input id="dieuKienApDung" v-model="formData.dieuKienApDung" type="text" class="form-control" />
            <div v-if="errors.dieuKienApDung" class="text-danger mt-1">{{ errors.dieuKienApDung }}</div>
          </div>
        </div>
        <div class="mt-4 d-flex justify-content-end gap-2">
          <button type="button" @click="phieuGiamGiaDialogVisible = false" class="btn btn-secondary">Hủy</button>
          <button type="submit" class="btn btn-primary">Lưu</button>
        </div>
      </form>
    </dialog>
  </div>
</template>

<style scoped>
* {
  box-sizing: border-box;
}

.container {
  padding: 1.5rem;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.icon {
  width: 1em;
  height: 1em;
  margin-right: 0.5em;
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dashboard-title {
  font-size: 1.75rem;
  font-weight: 600;
  color: #333333;
  margin: 0;
}

.search-card {
  background-color: white;
  border-radius: 0.5rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  padding: 1.25rem;
  margin: 1.5rem 0;
}

.table-container {
  overflow-x: auto;
}

.table-fixed {
  table-layout: fixed;
  width: 100%;
  background-color: white;
  border-radius: 0.5rem;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.table-fixed th,
.table-fixed td {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  padding: 0.75rem;
  vertical-align: middle;
}

.table-fixed th.col-1,
.table-fixed td.col-1,
.table-fixed th.col-7,
.table-fixed td.col-7,
.table-fixed th.col-9,
.table-fixed td.col-9 {
  text-align: center;
}

.table-fixed th.col-1,
.table-fixed td.col-1 {
  width: 6%;
}

.table-fixed th.col-2,
.table-fixed td.col-2 {
  width: 6%;
}

.table-fixed th.col-3,
.table-fixed td.col-3 {
  width: 15%;
}

.table-fixed th.col-4,
.table-fixed td.col-4 {
  width: 10%;
}

.table-fixed th.col-5,
.table-fixed td.col-5 {
  width: 10%;
}

.table-fixed th.col-6,
.table-fixed td.col-6 {
  width: 11%;
}

.table-fixed th.col-7,
.table-fixed td.col-7 {
  width: 10%;
}

.table-fixed th.col-8,
.table-fixed td.col-8 {
  width: 12%;
}

.table-fixed th.col-9,
.table-fixed td.col-9 {
  width: 10%;
}

.table-fixed th.col-10,
.table-fixed td.col-10 {
  width: 10%;
}

thead {
  height: 60px;
  background-color: #f8f9fa;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  margin: 1.5rem auto;
  min-height: 40px;
  padding: 0.5rem;
  background-color: white;
  border-radius: 0.5rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.dialog {
  border: none;
  border-radius: 8px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
  background-color: white;
}

.form-select {
  padding: 8px;
  cursor: pointer;
}

.form-select[multiple] option {
  padding: 8px;
}

.form-select[multiple] option:checked {
  background-color: #007bff;
  color: white;
}
</style>