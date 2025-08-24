<template>
  <div class="warranty-container">
    <div class="page-header">
      <div class="header-content">
        <div class="title-section">
          <h1 class="page-title">Quản lý bảo hành</h1>
          <p class="page-subtitle">Tạo và quản lý các chương trình bảo hành</p>
        </div>
        <el-button
          v-if="isAdmin"
          type="primary"
          size="large"
          class="create-btn"
          @click="openCreateDialog()"
        >
          <el-icon class="btn-icon">
            <Plus />
          </el-icon>
          Thêm bảo hành
        </el-button>
      </div>
    </div>

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
              @input="fetchBaoHanh"
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
              @change="fetchBaoHanh"
            >
              <el-option label="Tất cả" value="" />
              <el-option label="Còn bảo hành" value="UNDER_WARRANTY" />
              <el-option label="Hết bảo hành" value="WARRANTY_EXPIRED" />
              <el-option label="Không bảo hành" value="WARRANTY_VOID" />
            </el-select>
          </div>
          <div class="filter-item">
            <label class="filter-label">Thời gian bắt đầu</label>
            <el-date-picker
              v-model="ngayBatDauFilter"
              type="date"
              placeholder="Chọn thời gian bắt đầu"
              value-format="YYYY-MM-DD"
              clearable
              class="filter-date"
              @change="fetchBaoHanh"
            />
          </div>
          <div class="filter-item">
            <label class="filter-label">Thời gian kết thúc</label>
            <el-date-picker
              v-model="ngayKetThucFilter"
              type="date"
              placeholder="Chọn thời gian kết thúc"
              value-format="YYYY-MM-DD"
              clearable
              class="filter-date"
              @change="fetchBaoHanh"
            />
          </div>
          <div class="filter-actions">
            <el-button @click="clearFilters" class="reset-btn">
              <el-icon>
                <Refresh />
              </el-icon>
              Làm mới
            </el-button>
          </div>
        </div>
      </div>
    </el-card>

    <el-card class="table-card" shadow="never">
      <el-table
        v-loading="loading"
        :data="baoHanhs"
        class="warranty-table"
        :row-class-name="getRowClassName"
        empty-text="Không có dữ liệu"
      >
        <el-table-column label="STT" width="75" align="center">
          <template #default="scope">
            <span class="row-index">{{
              scope.$index + 1 + (pagination.page - 1) * pagination.size
            }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="soImeiDaBan" label="Số IMEI" min-width="150">
          <template #default="scope">
            <div class="code-cell">
              <el-tag type="success" size="small" class="code-tag">
                {{ scope.row.soImeiDaBan }}
              </el-tag>
            </div>
          </template>
        </el-table-column>

        <el-table-column
          prop="tenLoaiBaoHanh"
          label="Loại bảo hành"
          min-width="170"
        >
          <template #default="scope">
            <div class="code-cell">
              <el-tag type="info" size="small" class="code-tag">
                {{ scope.row.tenLoaiBaoHanh }}
              </el-tag>
            </div>
          </template>
        </el-table-column>

        <el-table-column
          prop="maKhachHang"
          label="Mã khách hàng"
          min-width="120"
        >
          <template #default="scope">
            <div class="code-cell">
              <el-tag type="warning" size="small" class="code-tag">
                {{ scope.row.maKhachHang }}
              </el-tag>
            </div>
          </template>
        </el-table-column>

        <el-table-column
          prop="tenKhachHang"
          label="Tên khách hàng"
          min-width="150"
        >
          <template #default="scope">
            <div class="name-cell">
              <span class="warranty-name">{{ scope.row.tenKhachHang }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="Thời gian (tháng)" width="140" align="center">
          <template #default="scope">
            <div class="value-cell">
              <span class="warranty-time"
                >{{ scope.row.thoiGianThang }} tháng</span
              >
            </div>
          </template>
        </el-table-column>

        <el-table-column label="Thời gian bảo hành" width="200">
          <template #default="scope">
            <div class="date-range">
              <div class="date-item">
                <el-icon class="date-icon">
                  <Calendar />
                </el-icon>
                <span>{{ formatDate(scope.row.ngayBatDau) }}</span>
              </div>
              <div class="date-separator">→</div>
              <div class="date-item">
                <el-icon class="date-icon">
                  <Calendar />
                </el-icon>
                <span>{{ formatDate(scope.row.ngayKetThuc) }}</span>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="Trạng thái" width="150" align="center">
          <template #default="scope">
            <el-tag
              :type="getStatusType(scope.row.trangThaiBaoHanh)"
              size="small"
            >
              {{ convertTrangThaiBaoHanh(scope.row.trangThaiBaoHanh) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column
          label="Thao tác"
          width="200"
          align="center"
          fixed="right"
        >
          <template #default="scope">
            <div class="action-buttons">
              <el-tooltip content="Xem chi tiết" placement="top">
                <el-button
                  size="small"
                  type="info"
                  :icon="View"
                  circle
                  @click="openDetailDialog(scope.row)"
                  class="action-btn view-btn"
                />
              </el-tooltip>
              <el-tooltip content="Chỉnh sửa" placement="top">
                <el-button
                  v-if="isAdmin"
                  size="small"
                  type="primary"
                  :icon="Edit"
                  circle
                  @click="openEditDialog(scope.row)"
                  class="action-btn edit-btn"
                />
              </el-tooltip>
              <!-- <el-tooltip content="Xóa" placement="top">
                <el-button
                  size="small"
                  type="danger"
                  :icon="Delete"
                  circle
                  @click="xoaBaoHanh(scope.row)"
                  class="action-btn delete-btn"
                />
              </el-tooltip> -->
            </div>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :current-page="pagination.page"
          :page-size="pagination.size"
          :page-sizes="[5, 10, 20, 50]"
          :total="pagination.total"
          @current-change="updatePage"
          @size-change="updateSize"
          class="custom-pagination"
        />
      </div>
    </el-card>

    <!-- Create/Edit Dialog -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="700px"
      @close="resetForm"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="150px"
        label-position="top"
      >
        <div class="form-grid">
          <div class="form-row">
            <el-form-item
              label="Khách hàng"
              prop="idKhachHang"
              class="form-item"
            >
              <el-select
                v-model="form.idKhachHang"
                placeholder="Chọn khách hàng"
                filterable
                clearable
                style="width: 100%"
                @change="onCustomerChange"
              >
                <el-option
                  v-for="customer in khachHangs"
                  :key="customer.id"
                  :label="`${customer.maKhachHang} - ${customer.tenKhachHang} - ${customer.sdt}`"
                  :value="customer.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item
              label="IMEI đã bán"
              prop="idImeiDaBan"
              class="form-item"
            >
              <el-select
                v-model="form.idImeiDaBan"
                placeholder="Chọn IMEI"
                filterable
                clearable
                style="width: 100%"
                :disabled="!form.idKhachHang"
                @change="onImeiDaBanChange"
              >
                <el-option
                  v-for="imei in imeiDaBans"
                  :key="imei.id"
                  :label="`${imei.soImei}` + ' | ' + `${imei.tenModel}` + ' ' + `${imei.maXuatXu}`"
                  :value="imei.id"
                />
              </el-select>
            </el-form-item>
          </div>

          <div class="form-row">
            <el-form-item
              label="Loại bảo hành"
              prop="idLoaiBaoHanh"
              class="form-item"
            >
              <el-select
                v-model="form.idLoaiBaoHanh"
                placeholder="Chọn loại bảo hành"
                filterable
                clearable
                style="width: 100%"
                :disabled="!form.idImeiDaBan"
                @change="onWarrantyTypeChange"
              >
                <el-option
                  v-for="type in loaiBaoHanhs"
                  :key="type.id"
                  :label="`${type.tenLoaiBaoHanh} (${type.thoiGianThang} tháng) | ${type.daCo ==true ? 'Đã có' : 'Chưa có'}`"
                  :value="type.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item
              label="Trạng thái"
              prop="trangThaiBaoHanh"
              class="form-item"
            >
              <el-select
                v-model="form.trangThaiBaoHanh"
                placeholder="Chọn trạng thái"
                style="width: 100%"
              >
                <el-option label="Còn bảo hành" value="UNDER_WARRANTY" />
                <el-option label="Hết bảo hành" value="WARRANTY_EXPIRED" />
                <el-option label="Không bảo hành" value="WARRANTY_VOID" />
              </el-select>
            </el-form-item>
          </div>

          <div class="form-row">
            <el-form-item
              label="Ngày bắt đầu"
              prop="ngayBatDau"
              class="form-item"
            >
              <el-date-picker
                v-model="form.ngayBatDau"
                type="date"
                placeholder="Chọn ngày bắt đầu"
                style="width: 100%"
                value-format="YYYY-MM-DD"
                @change="calculateEndDate"
              />
            </el-form-item>
          </div>
        </div>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">Hủy</el-button>
          <el-button
            type="primary"
            @click="handleSubmit"
            :loading="submitLoading"
          >
            {{ isEditing ? "Cập nhật" : "Thêm mới" }}
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- Detail Dialog -->
    <el-dialog
      v-model="detailDialogVisible"
      title="Chi tiết bảo hành"
      width="600px"
    >
      <div v-if="selectedBaoHanh" class="detail-content">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Mã khách hàng">
            {{ selectedBaoHanh.maKhachHang }}
          </el-descriptions-item>
          <el-descriptions-item label="Tên khách hàng">
            {{ selectedBaoHanh.tenKhachHang }}
          </el-descriptions-item>
          <el-descriptions-item label="Số điện thoại khách hàng">
            {{ selectedBaoHanh.sdtKhachHang }}
          </el-descriptions-item>
          <el-descriptions-item label="Số IMEI">
            {{ selectedBaoHanh.soImeiDaBan }}
          </el-descriptions-item>
          <el-descriptions-item label="Loại bảo hành">
            {{ selectedBaoHanh.tenLoaiBaoHanh }}
          </el-descriptions-item>
          <el-descriptions-item label="Thời gian bảo hành">
            {{ selectedBaoHanh.thoiGianThang }} tháng
          </el-descriptions-item>
          <el-descriptions-item label="Trạng thái">
            <el-tag :type="getStatusType(selectedBaoHanh.trangThaiBaoHanh)">
              {{ convertTrangThaiBaoHanh(selectedBaoHanh.trangThaiBaoHanh) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="Ngày bắt đầu" :span="2">
            {{ formatDate(selectedBaoHanh.ngayBatDau) }}
          </el-descriptions-item>
          <el-descriptions-item label="Ngày kết thúc" :span="2">
            {{ formatDate(selectedBaoHanh.ngayKetThuc) }}
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="detailDialogVisible = false">Đóng</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch, nextTick, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  Plus,
  Edit,
  Delete,
  Search,
  Refresh,
  Calendar,
  View,
} from "@element-plus/icons-vue";
import {
  getAllBaoHanh,
  addWarranty,
  updateWarranty,
  imeiDaBanListByKhachHang,
} from "@/Service/Adminservice/BaoHanh/BaoHanhService";
import { clientList } from "@/Service/Adminservice/TaiKhoan/KhachHangServices";
import { loaiBaoHanhList } from "@/Service/Adminservice/BaoHanh/LoaiBaoHanhService";
import store from "@/Service/LoginService/Store";

// Reactive variables
const search = ref("");
const trangThaiFilter = ref("");
const ngayBatDauFilter = ref(null);
const ngayKetThucFilter = ref(null);
const loading = ref(false);
const baoHanhs = ref([]);
const selectedBaoHanh = ref(null);
const errors = reactive({});

const pagination = ref({
  page: 1,
  size: 10,
  total: 0,
});

// Dialog variables
const dialogVisible = ref(false);
const detailDialogVisible = ref(false);
const submitLoading = ref(false);
const isEditing = ref(false);
const formRef = ref(null);

// Form data - theo cấu trúc BaoHanhRequest
const form = ref({
  idBaoHanh: null,
  idKhachHang: null,
  idImeiDaBan: null,
  idLoaiBaoHanh: null,
  ngayBatDau: "",
  ngayKetThuc: "",
  trangThaiBaoHanh: "UNDER_WARRANTY",
});

// Dropdown data
const khachHangs = ref([]);
const imeiDaBans = ref([]);
const loaiBaoHanhs = ref([]);

// Validation rules
const rules = ref({
  idKhachHang: [
    {
      required: true,
      message: "Khách hàng không được để trống",
      trigger: "change",
    },
  ],
  idImeiDaBan: [
    {
      required: true,
      message: "IMEI đã bán không được để trống",
      trigger: "change",
    },
  ],
  idLoaiBaoHanh: [
    {
      required: true,
      message: "Loại bảo hành không được để trống",
      trigger: "change",
    },
  ],
  ngayBatDau: [
    {
      required: true,
      message: "Ngày bắt đầu không được để trống",
      trigger: "change",
    },
    {
      validator: (rule, value, callback) => {
        if (value && new Date(value) > new Date()) {
          callback(
            new Error("Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày hiện tại")
          );
        } else {
          callback();
        }
      },
      trigger: "change",
    },
  ],
  trangThaiBaoHanh: [
    {
      required: true,
      message: "Trạng thái bảo hành không được để trống",
      trigger: "change",
    },
  ],
});

// Computed
const isAdmin = computed(() => {
  const roles = store.state.roles;
  return (
    Array.isArray(roles) &&
    roles
      .map((role) => (typeof role === "string" ? role : role.authority))
      .includes("ROLE_ADMIN")
  );
});

const dialogTitle = computed(() => {
  return isEditing.value ? "Cập nhật bảo hành" : "Thêm bảo hành mới";
});

// Functions
const fetchBaoHanh = async () => {
  loading.value = true;
  try {
    console.log("Fetching warranty list with params:", {
      search: search.value,
      trangThai: trangThaiFilter.value,
      ngayBatDau: ngayBatDauFilter.value,
      page: pagination.value.page - 1,
      size: pagination.value.size,
    });

    const response = await getAllBaoHanh({
      search: search.value,
      trangThai: trangThaiFilter.value,
      ngayBatDau: ngayBatDauFilter.value,
      page: pagination.value.page - 1,
      size: pagination.value.size,
    });

    console.log("Fetch response:", response);

    baoHanhs.value = response.content || [];
    pagination.value.total = response.totalElements || 0;
  } catch (error) {
    console.error("Fetch error:", error);
    console.error("Fetch error details:", {
      response: error.response,
      data: error.response?.data,
      status: error.response?.status,
    });

    // Xử lý lỗi tương tự như handleSubmit
    if (error.response?.status === 400 && error.response.data) {
      let errorMessage = "Lỗi khi tải danh sách bảo hành: ";

      if (error.response.data && typeof error.response.data === "object") {
        // Xử lý format lỗi: {idImeiDaBan: [{message: "...", field: "idImeiDaBan"}]}
        Object.keys(error.response.data).forEach((key) => {
          if (Array.isArray(error.response.data[key])) {
            error.response.data[key].forEach((err) => {
              if (err.message) {
                errorMessage += `${key}: ${err.message}; `;
              }
            });
          }
        });

        console.warn("Validation error in fetch:", errorMessage);
        // Không hiển thị error cho user vì đây là internal fetch issue
      }
    } else {
      ElMessage.error(error?.message || "Lỗi khi lấy danh sách bảo hành");
    }
  } finally {
    loading.value = false;
  }
};

const loadDropdownData = async () => {
  try {
    // Load khách hàng
    const khachHangResponse = await clientList();
    khachHangs.value = khachHangResponse.content || khachHangResponse || [];

    // Load loại bảo hành
    // const loaiBaoHanhResponse = await loaiBaoHanhList(form.value.idImeiDaBan);
    // loaiBaoHanhResponse.content || loaiBaoHanhResponse || [];
  } catch (error) {
    console.error("Load dropdown error:", error);
    ElMessage.error("Lỗi khi tải dữ liệu dropdown");
  }
};

const onCustomerChange = async (customerId) => {
  // Luôn reset IMEI và loại bảo hành khi đổi khách hàng
  form.value.idImeiDaBan = null;
  form.value.idLoaiBaoHanh = null;
  loaiBaoHanhs.value = [];
  
  if (customerId) {
    try {
      const response = await imeiDaBanListByKhachHang(customerId);
      if (response && response.length > 0) {
        imeiDaBans.value = response;
      } else {
        imeiDaBans.value = [];
        ElMessage.error("Khách hàng chưa mua sản phẩm nào");
      }
    } catch (error) {
      console.error("Load IMEI error:", error);
      ElMessage.error("Lỗi khi tải danh sách IMEI");
      imeiDaBans.value = [];
    }
  } else {
    imeiDaBans.value = [];
  }
};

const onImeiDaBanChange = async (idImeiDaBan) => {
  // Luôn reset loại bảo hành khi đổi IMEI
  form.value.idLoaiBaoHanh = null;
  
  if (idImeiDaBan) {
    try {
      const loaiBaoHanhResponse = await loaiBaoHanhList(idImeiDaBan);
      if (loaiBaoHanhResponse && loaiBaoHanhResponse.length > 0) {
        loaiBaoHanhs.value = loaiBaoHanhResponse || loaiBaoHanhResponse;
      } else {
        loaiBaoHanhs.value = [];
        ElMessage.error("Sản phẩm này chưa có loại bảo hành");
      }
    } catch (err) {
      console.error("Load warranty types error:", err);
      ElMessage.error("Lỗi khi tải dữ liệu dropdown");
      loaiBaoHanhs.value = [];
    }
  } else {
    loaiBaoHanhs.value = [];
  }
};

const onWarrantyTypeChange = () => {
  calculateEndDate();
};

const calculateEndDate = () => {
  if (form.value.ngayBatDau && form.value.idLoaiBaoHanh) {
    const selectedType = loaiBaoHanhs.value.find(
      (type) => type.id === form.value.idLoaiBaoHanh
    );
    if (selectedType && selectedType.thoiGianThang) {
      const startDate = new Date(form.value.ngayBatDau);
      const endDate = new Date(startDate);
      endDate.setMonth(endDate.getMonth() + selectedType.thoiGianThang);

      const year = endDate.getFullYear();
      const month = String(endDate.getMonth() + 1).padStart(2, "0");
      const day = String(endDate.getDate()).padStart(2, "0");

      form.value.ngayKetThuc = `${year}-${month}-${day}`;
    }
  }
};

const openCreateDialog = async () => {
  isEditing.value = false;
  resetForm();
  await loadDropdownData();
  dialogVisible.value = true;
};

const openEditDialog = async (row) => {
  isEditing.value = true;
  dialogVisible.value = true;

  // Load dropdown data first
  await loadDropdownData();

  // Load IMEI for selected customer
  if (row.idKhachHang) {
    await onCustomerChange(row.idKhachHang);
  }

  if (row.idImeiDaBan) {
    await onImeiDaBanChange(row.idImeiDaBan);
  }

  // Wait for next tick to ensure all data is loaded
  await nextTick();

  // Then set form values
  form.value = {
    idBaoHanh: row.idBaoHanh,
    idKhachHang: row.idKhachHang,
    idImeiDaBan: row.idImeiDaBan,
    idLoaiBaoHanh: row.idLoaiBaoHanh,
    ngayBatDau: row.ngayBatDau,
    ngayKetThuc: row.ngayKetThuc,
    trangThaiBaoHanh: row.trangThaiBaoHanh,
  };

  // Clear form validation
  await nextTick();
  if (formRef.value) {
    formRef.value.clearValidate();
  }
};

const openDetailDialog = (row) => {
  selectedBaoHanh.value = row;
  detailDialogVisible.value = true;
};

const resetForm = () => {
  form.value = {
    idBaoHanh: null,
    idKhachHang: null,
    idImeiDaBan: null,
    idLoaiBaoHanh: null,
    ngayBatDau: "",
    ngayKetThuc: "",
    trangThaiBaoHanh: "UNDER_WARRANTY",
  };

  imeiDaBans.value = [];

  // Clear validation in next tick
  nextTick(() => {
    if (formRef.value) {
      formRef.value.clearValidate();
    }
  });
};

const handleSubmit = async () => {
  Object.keys(errors).forEach((key) => delete errors[key]); // clear error
  if (!formRef.value) return;

  // Check if dropdown data is loaded
  if (khachHangs.value.length === 0 || loaiBaoHanhs.value.length === 0) {
    ElMessage.error("Đang tải dữ liệu, vui lòng đợi...");
    return;
  }

  submitLoading.value = true;

  try {
    // Validate form
    const isValid = await formRef.value.validate().catch(() => false);
    if (!isValid) {
      submitLoading.value = false;
      return;
    }

    // Debug form values before creating payload
    console.log("Form values before submit:", {
      idKhachHang: form.value.idKhachHang,
      idImeiDaBan: form.value.idImeiDaBan,
      idLoaiBaoHanh: form.value.idLoaiBaoHanh,
      ngayBatDau: form.value.ngayBatDau,
      ngayKetThuc: form.value.ngayKetThuc,
      trangThaiBaoHanh: form.value.trangThaiBaoHanh,
    });

    // Validate required fields manually
    if (!form.value.idKhachHang) {
      ElMessage.error("Vui lòng chọn khách hàng");
      submitLoading.value = false;
      return;
    }
    if (!form.value.idImeiDaBan) {
      ElMessage.error("Vui lòng chọn IMEI đã bán");
      submitLoading.value = false;
      return;
    }
    if (!form.value.idLoaiBaoHanh) {
      ElMessage.error("Vui lòng chọn loại bảo hành");
      submitLoading.value = false;
      return;
    }
    if (!form.value.ngayBatDau) {
      ElMessage.error("Vui lòng chọn ngày bắt đầu");
      submitLoading.value = false;
      return;
    }
    if (!form.value.trangThaiBaoHanh) {
      ElMessage.error("Vui lòng chọn trạng thái bảo hành");
      submitLoading.value = false;
      return;
    }

    const payload = {
      idKhachHang: Number(form.value.idKhachHang),
      idImeiDaBan: Number(form.value.idImeiDaBan),
      idLoaiBaoHanh: Number(form.value.idLoaiBaoHanh),
      ngayBatDau: form.value.ngayBatDau, // YYYY-MM-DD
      ngayKetThuc: form.value.ngayKetThuc, // YYYY-MM-DD
      trangThaiBaoHanh: form.value.trangThaiBaoHanh,
    };

    console.log("Final payload to submit:", payload);

    if (isEditing.value) {
      // confirm update
      await ElMessageBox.confirm(
        "Bạn có chắc chắn muốn cập nhật bảo hành này?",
        "Xác nhận",
        {
          confirmButtonText: "Đồng ý",
          cancelButtonText: "Hủy",
          type: "warning",
        }
      );

      payload.idBaoHanh = Number(form.value.idBaoHanh);
      console.log("Updating warranty with id:", form.value.idBaoHanh);
      await updateWarranty(form.value.idBaoHanh, payload);
      ElMessage.success("Cập nhật bảo hành thành công");
    } else {
      // confirm add
      await ElMessageBox.confirm(
        "Bạn có chắc chắn muốn thêm bảo hành này?",
        "Xác nhận",
        {
          confirmButtonText: "Đồng ý",
          cancelButtonText: "Hủy",
          type: "warning",
        }
      );

      console.log("Creating new warranty...");
      await addWarranty(payload);
      ElMessage.success("Thêm bảo hành thành công");
    }

    dialogVisible.value = false;

    // Delay một chút trước khi fetch để đảm bảo backend đã xử lý xong
    setTimeout(() => {
      fetchBaoHanh();
    }, 500);
  } catch (error) {
    // nếu cancel confirm thì không báo gì cả
    if (error === "cancel" || error === "close") {
      return;
    }

    console.error("Submit error:", error);
    if (Array.isArray(error)) {
      error.forEach(({ field, message }) => {
        errors[field] = message;
        ElMessage.error(message);
      });
    }
    return; // Không refresh nếu có lỗi validation
  } finally {
    submitLoading.value = false;
  }
};

const clearFilters = () => {
  search.value = "";
  trangThaiFilter.value = "";
  ngayBatDauFilter.value = null;
  ngayKetThucFilter.value = null;
  pagination.value.page = 1;
  fetchBaoHanh();
};

const updatePage = (page) => {
  pagination.value.page = page;
  fetchBaoHanh();
};

const updateSize = (size) => {
  pagination.value.size = size;
  pagination.value.page = 1;
  fetchBaoHanh();
};

// Helper functions
const formatDate = (dateString) => {
  if (!dateString) return "";
  const date = new Date(dateString);
  return date.toLocaleDateString("vi-VN");
};

const convertTrangThaiBaoHanh = (trangThai) => {
  const trangThaiLabels = {
    UNDER_WARRANTY: "Còn bảo hành",
    WARRANTY_EXPIRED: "Hết bảo hành",
    WARRANTY_VOID: "Không bảo hành",
  };
  return trangThaiLabels[trangThai] || "Không xác định";
};

const getStatusType = (status) => {
  const statusTypes = {
    UNDER_WARRANTY: "success",
    WARRANTY_EXPIRED: "danger",
    WARRANTY_VOID: "info",
  };
  return statusTypes[status] || "info";
};

const getRowClassName = ({ rowIndex }) => {
  return rowIndex % 2 === 0 ? "even-row" : "odd-row";
};

// Watchers
watch(
  [search, trangThaiFilter, ngayBatDauFilter, ngayKetThucFilter],
  () => {
    pagination.value.page = 1;
  },
  { deep: true }
);

// Lifecycle
onMounted(() => {
  fetchBaoHanh();
});
</script>

<style scoped>
.warranty-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 24px;
  font-family: "Inter", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
    sans-serif;
}

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
  background: linear-gradient(135deg, #143adf 0%, #14a8cd 100%);
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
  background: linear-gradient(135deg, #143adf 0%, #14a8cd 100%);
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

.filter-input :deep(.el-input__wrapper),
.filter-select :deep(.el-input__wrapper),
.filter-date :deep(.el-input__wrapper) {
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  transition: all 0.3s ease;
}

.filter-input :deep(.el-input__wrapper:hover),
.filter-select :deep(.el-input__wrapper:hover),
.filter-date :deep(.el-input__wrapper:hover) {
  border-color: #cbd5e1;
}

.filter-input :deep(.el-input__wrapper.is-focus),
.filter-select :deep(.el-input__wrapper.is-focus),
.filter-date :deep(.el-input__wrapper.is-focus) {
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

.table-card {
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.table-card :deep(.el-card__body) {
  padding: 0;
}

.warranty-table {
  border-radius: 16px;
  overflow: hidden;
}

.warranty-table :deep(.el-table__header) {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
}

.warranty-table :deep(.el-table__header th) {
  background: transparent;
  color: #374151;
  font-weight: 600;
  font-size: 14px;
  border-bottom: 2px solid #e2e8f0;
  padding: 16px 12px;
}

.warranty-table :deep(.el-table__body tr) {
  transition: all 0.3s ease;
}

.warranty-table :deep(.el-table__body tr:hover) {
  background: #f8fafc;
}

.warranty-table :deep(.even-row) {
  background: #fafbfc;
}

.warranty-table :deep(.odd-row) {
  background: white;
}

.warranty-table :deep(.el-table__body td) {
  padding: 16px 12px;
  border-bottom: 1px solid #f1f5f9;
}

.row-index {
  font-weight: 600;
  color: #6b7280;
}

.code-cell {
  display: flex;
  align-items: center;
}

.code-tag {
  font-family: "Monaco", "Menlo", monospace;
  font-weight: 600;
}

.name-cell {
  display: flex;
  align-items: center;
}

.warranty-name {
  font-weight: 500;
  color: #1f2937;
}

.value-cell {
  text-align: center;
}

.warranty-percent {
  font-weight: 700;
  color: #059669;
  font-size: 16px;
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
  margin-left: 0;
  transition: all 0.3s ease;
}

.view-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(100, 116, 139, 0.4);
}

.edit-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
}

.delete:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.4);
}

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
  background: linear-gradient(135deg, #143adf 0%, #14a8cd 100%);
  color: white;
}

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
  .warranty-container {
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

  .warranty-table :deep(.el-table__header th),
  .warranty-table :deep(.el-table__body td) {
    padding: 12px 8px;
  }

  .action-buttons {
    flex-direction: column;
    gap: 4px;
  }
}

@media (max-width: 480px) {
  .warranty-table :deep(.el-table__header th),
  .warranty-table :deep(.el-table__body td) {
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
