<template>
  <div class="warranty-container">
    <div class="page-header">
      <div class="header-content">
        <div class="title-section">
          <h1 class="page-title">Quản lý loại bảo hành</h1>
          <p class="page-subtitle">Tạo và quản lý các loại bảo hành</p>
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
          Thêm loại bảo hành
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
              placeholder="Tìm kiếm theo tên bảo hành..."
              clearable
              class="filter-input"
              @input="fetchLoaiBaoHanh"
            >
              <template #prefix>
                <el-icon class="search-icon">
                  <Search />
                </el-icon>
              </template>
            </el-input>
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
        :data="loaiBaoHanhs"
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
        <el-table-column
          prop="tenLoaiBaoHanh"
          label="Tên loại bảo hành"
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
          prop="idModelSanPham"
          label="Tên Model sản phẩm"
          min-width="200"
        >
          <template #default="scope">
            <div class="code-cell">
              <el-tag type="info" size="small" class="code-tag">
                {{
                  `${scope.row.tenModelSanPham}` +
                    " " +
                    `${scope.row.maXuatXu}` || "Chưa có thông tin"
                }}
              </el-tag>
            </div>
          </template>
        </el-table-column>

        <el-table-column
          label="Thời gian bảo hành (tháng)"
          width="250"
          align="center"
        >
          <template #default="scope">
            <div class="value-cell">
              <span class="warranty-percent">{{
                scope.row.thoiGianThang
              }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="trangThai" label="Trạng thái" min-width="300">
          <template #default="scope">
            <div class="name-cell">
              <span class="warranty-name">{{ scope.row.trangThai == true ? "Đang hoạt động" : "Không hoạt động" }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="moTa" label="Mô tả" min-width="300">
          <template #default="scope">
            <div class="name-cell">
              <span class="warranty-name">{{ scope.row.moTa }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column
          v-if="isAdmin"
          label="Thao tác"
          width="150"
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
                  size="small"
                  type="primary"
                  :icon="Edit"
                  circle
                  @click="openEditDialog(scope.row)"
                  class="action-btn edit-btn"
                />
              </el-tooltip>
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

    <!-- Create Dialog -->
    <el-dialog
      v-model="showCreateDialog"
      :title="dialogTitle"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="createFormRef"
        :model="form"
        :rules="rules"
        label-width="160px"
        label-position="left"
      >
        <el-form-item
          label="Tên loại bảo hành"
          prop="tenLoaiBaoHanh"
          :error="errors.tenLoaiBaoHanh"
        >
          <el-input
            v-model="form.tenLoaiBaoHanh"
            placeholder="Nhập tên loại bảo hành"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>

        <el-form-item
          label="Model sản phẩm"
          prop="idModelSanPham"
          :error="errors.idModelSanPham"
        >
          <el-select
            v-model="form.idModelSanPham"
            placeholder="Chọn model sản phẩm"
            style="width: 100%"
            clearable
            filterable
            remote
            reserve-keyword
            :remote-method="handleModelSearch"
            :loading="modelSearchLoading"
            @focus="handleModelFocus"
            @clear="handleModelClear"
          >
            <el-option
              v-for="model in filteredModelSanPhams"
              :key="model.idModelSanPham"
              :label="`${model.tenModel}${
                model.maXuatXu ? ' - ' + model.maXuatXu : ''
              }`"
              :value="model.idModelSanPham"
            >
              <span class="model-option">
                <span class="model-name">{{ model.tenModel }}</span>
                <span v-if="model.maXuatXu" class="model-origin"
                  >- {{ model.maXuatXu }}</span
                >
              </span>
            </el-option>
            <el-option
              v-if="filteredModelSanPhams.length === 0 && modelSearchQuery"
              disabled
              label="Không tìm thấy model phù hợp"
              value=""
            />
          </el-select>
        </el-form-item>

        <el-form-item
          label="Trạng thái"
          prop="trangThai"
          :error="errors.tranGThai"
        >
          <el-select
            v-model="form.trangThai"
            placeholder="Chọn trạng thái"
            style="width: 100%"
            filterable
            clearable
          >
            <el-option label="Hoạt động" value="true" />
            <el-option label="Không hoạt động" value="false" />
          </el-select>
        </el-form-item>

        <el-form-item
          label="Thời gian bảo hành"
          prop="thoiGianThang"
          :error="errors.thoiGianThang"
        >
          <el-input-number
            v-model="form.thoiGianThang"
            :min="1"
            :max="120"
            placeholder="Nhập thời gian bảo hành"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="Mô tả" prop="moTa" :error="errors.moTa">
          <el-input
            v-model="form.moTa"
            type="textarea"
            :rows="4"
            placeholder="Nhập mô tả loại bảo hành"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancelCreate">Hủy</el-button>
          <el-button
            type="primary"
            @click="submitCreate"
            :loading="isSubmitting"
          >
            <el-icon class="btn-icon"><Plus /></el-icon>
            Thêm mới
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- Edit Dialog -->
    <el-dialog
      v-model="showEditDialog"
      :title="dialogTitle"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="editFormRef"
        :model="form"
        :rules="editRules"
        label-width="160px"
        label-position="left"
      >
        <el-form-item
          label="Tên loại bảo hành"
          prop="tenLoaiBaoHanh"
          :error="errors.tenLoaiBaoHanh"
        >
          <el-input
            v-model="form.tenLoaiBaoHanh"
            placeholder="Nhập tên loại bảo hành"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>

        <el-form-item
          label="Model sản phẩm"
          prop="idModelSanPham"
          :error="errors.idModelSanPham"
        >
          <el-select
            v-model="form.idModelSanPham"
            placeholder="Chọn model sản phẩm"
            style="width: 100%"
            clearable
            filterable
            remote
            reserve-keyword
            :remote-method="handleModelSearch"
            :loading="modelSearchLoading"
            @focus="handleModelFocus"
            @clear="handleModelClear"
          >
            <el-option
              v-for="model in filteredModelSanPhams"
              :key="model.idModelSanPham"
              :label="`${model.tenModel}${
                model.maXuatXu ? ' - ' + model.maXuatXu : ''
              }`"
              :value="model.idModelSanPham"
            >
              <span class="model-option">
                <span class="model-name">{{ model.tenModel }}</span>
                <span v-if="model.maXuatXu" class="model-origin"
                  >- {{ model.maXuatXu }}</span
                >
              </span>
            </el-option>
            <el-option
              v-if="filteredModelSanPhams.length === 0 && modelSearchQuery"
              disabled
              label="Không tìm thấy model phù hợp"
              value=""
            />
          </el-select>
        </el-form-item>

        <el-form-item
          label="Trạng thái"
          prop="trangThai"
          :error="errors.tranGThai"
        >
          <el-select
            v-model="form.trangThai"
            placeholder="Chọn trạng thái"
            style="width: 100%"
            filterable
            clearable
          >
            <el-option label="Hoạt động" value="true" />
            <el-option label="Không hoạt động" value="false" />
          </el-select>
        </el-form-item>

        <el-form-item
          label="Thời gian bảo hành"
          prop="thoiGianThang"
          :error="errors.thoiGianThang"
        >
          <el-input-number
            v-model="form.thoiGianThang"
            :min="1"
            :max="120"
            placeholder="Nhập thời gian bảo hành"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="Mô tả" prop="moTa" :error="errors.moTa">
          <el-input
            v-model="form.moTa"
            type="textarea"
            :rows="4"
            placeholder="Nhập mô tả loại bảo hành"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancelEdit">Hủy</el-button>
          <el-button type="primary" @click="submitEdit" :loading="isSubmitting">
            <el-icon class="btn-icon"><Edit /></el-icon>
            Cập nhật
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- Detail Dialog -->
    <el-dialog
      v-model="showDetailDialog"
      title="Chi tiết loại bảo hành"
      width="700px"
    >
      <div v-if="selectedBaoHanh" class="detail-content">
        <div class="detail-row">
          <label class="detail-label">Tên loại bảo hành:</label>
          <el-tag type="info" class="detail-tag">
            {{ selectedBaoHanh.tenLoaiBaoHanh }}
          </el-tag>
        </div>

        <div class="detail-row">
          <label class="detail-label">Model sản phẩm:</label>
          <span class="detail-value">{{
            selectedBaoHanh.tenModelSanPham || "Chưa có thông tin"
          }}</span>
        </div>

        <div class="detail-row">
          <label class="detail-label">Trạng thái:</label>
          <span class="detail-value">{{
            selectedBaoHanh.trangThai == true ? "Đang hoạt động" : "Không hoạt động" || "Chưa có thông tin"
          }}</span>
        </div>

        <div class="detail-row">
          <label class="detail-label">Thời gian bảo hành:</label>
          <span class="detail-value highlight"
            >{{ selectedBaoHanh.thoiGianThang }} tháng</span
          >
        </div>

        <div class="detail-row">
          <label class="detail-label">Mô tả:</label>
          <p class="detail-description">
            {{ selectedBaoHanh.moTa || "Không có mô tả" }}
          </p>
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="closeDetail">Đóng</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  Plus,
  Edit,
  Search,
  Refresh,
  Calendar,
  View,
} from "@element-plus/icons-vue";
import {
  getAllLoaiBaoHanh,
  getLoaiBaoHanhById,
  addWarrantyType,
  updateWarrantyType,
} from "@/Service/Adminservice/BaoHanh/LoaiBaoHanhService";
import { getAllModelSanPhamList } from "@/Service/Adminservice/Products/ProductAdminService";
import store from "@/Service/LoginService/Store";

const search = ref("");
const loading = ref(false);
const isSubmitting = ref(false);
const loaiBaoHanhs = ref([]);
const modelSanPhams = ref([]);
const filteredModelSanPhams = ref([]);
const selectedBaoHanh = ref(null);
const errors = ref({});
const modelSearchLoading = ref(false);
const modelSearchQuery = ref("");

const pagination = ref({
  page: 1,
  size: 5,
  total: 0,
});

// Dialog states
const showCreateDialog = ref(false);
const showEditDialog = ref(false);
const showDetailDialog = ref(false);
const dialogTitle = ref("");

// Form data
const form = ref({
  id: null,
  idModelSanPham: null,
  tenLoaiBaoHanh: "",
  thoiGianThang: null,
  moTa: "",
});
const createFormRef = ref(null);
const editFormRef = ref(null);

// Validation rules for create
const rules = ref({
  tenLoaiBaoHanh: [
    {
      required: true,
      message: "Vui lòng nhập tên loại bảo hành",
      trigger: "blur",
    },
    { min: 2, max: 100, message: "Tên phải từ 2-100 ký tự", trigger: "blur" },
  ],
  thoiGianThang: [
    {
      required: true,
      message: "Vui lòng nhập thời gian bảo hành",
      trigger: "blur",
    },
    {
      type: "number",
      min: 1,
      max: 12,
      message: "Thời gian từ 1-12 tháng",
      trigger: "blur",
    },
  ],
  trangThai : [
    {
      required: true,
      message: "Vui lòng chọn trạng thái",
      trigger: "blur"
    }

  ],
  moTa: [{ max: 500, message: "Mô tả không quá 500 ký tự", trigger: "blur" }],
  idModelSanPham: [
    {
      required: true,
      message: "Vui lòng chọn model sản phẩm",
      trigger: "blur",
    },
  ],
});

// Validation rules for edit
const editRules = ref({
  tenLoaiBaoHanh: [
    {
      required: true,
      message: "Vui lòng nhập tên loại bảo hành",
      trigger: "blur",
    },
    { min: 2, max: 100, message: "Tên phải từ 2-100 ký tự", trigger: "blur" },
  ],
  thoiGianThang: [
    {
      required: true,
      message: "Vui lòng nhập thời gian bảo hành",
      trigger: "blur",
    },
    {
      type: "number",
      min: 1,
      max: 12,
      message: "Thời gian từ 1-12 tháng",
      trigger: "blur",
    },
  ],
    trangThai : [
    {
      required: true,
      message: "Vui lòng chọn trạng thái",
      trigger: "blur"
    }

  ],
  moTa: [{ max: 500, message: "Mô tả không quá 500 ký tự", trigger: "blur" }],
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

// Error handling functions
const setError = (field, message) => {
  errors.value[field] = message;
};

const clearAllErrors = () => {
  Object.keys(errors.value).forEach((key) => {
    delete errors.value[key];
  });
};

// Model search functions
const handleModelSearch = (query) => {
  modelSearchQuery.value = query;
  if (query !== "") {
    modelSearchLoading.value = true;
    setTimeout(() => {
      filteredModelSanPhams.value = modelSanPhams.value.filter((model) => {
        const searchText = query.toLowerCase();
        const modelName = model.tenModel?.toLowerCase() || "";
        const origin = model.maXuatXu?.toLowerCase() || "";
        return modelName.includes(searchText) || origin.includes(searchText);
      });
      modelSearchLoading.value = false;
    }, 200); // Debounce 200ms
  } else {
    filteredModelSanPhams.value = [...modelSanPhams.value];
  }
};

const handleModelFocus = () => {
  if (filteredModelSanPhams.value.length === 0) {
    filteredModelSanPhams.value = [...modelSanPhams.value];
  }
};

const handleModelClear = () => {
  modelSearchQuery.value = "";
  filteredModelSanPhams.value = [...modelSanPhams.value];
};

// Fetch model sản phẩm
const fetchModelSanPhams = async () => {
  try {
    const response = await getAllModelSanPhamList();
    modelSanPhams.value = response;
    filteredModelSanPhams.value = [...response]; // Initialize filtered list
  } catch (error) {
    console.error("Lỗi khi lấy danh sách model sản phẩm:", error);
    ElMessage.error("Không thể lấy danh sách model sản phẩm");
    modelSanPhams.value = [];
    filteredModelSanPhams.value = [];
  }
};

// Reset form function
const resetForm = () => {
  form.value = {
    id: null,
    idModelSanPham: null,
    tenLoaiBaoHanh: "",
    thoiGianThang: null,
    moTa: "",
  };

  // Clear errors
  clearAllErrors();

  // Reset model search
  modelSearchQuery.value = "";
  filteredModelSanPhams.value = [...modelSanPhams.value];

  // Reset form validation
  if (createFormRef.value) {
    createFormRef.value.resetFields();
  }
  if (editFormRef.value) {
    editFormRef.value.resetFields();
  }
};

// Dialog functions
const openCreateDialog = () => {
  resetForm();
  dialogTitle.value = "Thêm loại bảo hành mới";
  showCreateDialog.value = true;
  // Reset filtered models when opening dialog
  filteredModelSanPhams.value = [...modelSanPhams.value];
};

const openEditDialog = async (row) => {
  try {
    loading.value = true;
    const response = await getLoaiBaoHanhById(row.id);

    form.value = {
      id: response.id,
      tenLoaiBaoHanh: response.tenLoaiBaoHanh,
      idModelSanPham: response.idModelSanPham,
      thoiGianThang: response.thoiGianThang,
      moTa: response.moTa || "",
      trangThai: response.trangThai == true ? "Đang hoạt động" : "Không hoạt động"
    };

    clearAllErrors();
    // Reset filtered models when opening edit dialog
    filteredModelSanPhams.value = [...modelSanPhams.value];
    dialogTitle.value = "Chỉnh sửa loại bảo hành";
    showEditDialog.value = true;
  } catch (error) {
    console.error("Error opening edit dialog:", error);
    ElMessage.error(
      error?.response?.data?.message || "Lỗi khi lấy thông tin loại bảo hành"
    );
  } finally {
    loading.value = false;
  }
};

const openDetailDialog = async (row) => {
  try {
    loading.value = true;
    const response = await getLoaiBaoHanhById(row.id);
    selectedBaoHanh.value = response;
    showDetailDialog.value = true;
  } catch (error) {
    console.error("Error opening detail dialog:", error);
    ElMessage.error(
      error?.response?.data?.message || "Lỗi khi lấy chi tiết loại bảo hành"
    );
  } finally {
    loading.value = false;
  }
};


const submitCreate = async () => {
  if (!createFormRef.value) return;

  clearAllErrors();
  isSubmitting.value = true;

  try {
    await createFormRef.value.validate(); // only proceed if valid
    console.log("Form data before submit: ", form.value);

    // Confirm trước khi thêm
    await ElMessageBox.confirm(
      "Bạn có chắc chắn muốn thêm loại bảo hành này?",
      "Xác nhận",
      {
        confirmButtonText: "Đồng ý",
        cancelButtonText: "Hủy",
        type: "warning",
      }
    );

    await addWarrantyType(form.value);
    ElMessage.success("Thêm loại bảo hành thành công");
    showCreateDialog.value = false;
    resetForm();
    await fetchLoaiBaoHanh();
  } catch (error) {
    // Nếu cancel confirm thì dừng, không báo lỗi gì cả
    if (error === "cancel" || error === "close") {
      return;
    }

    console.error("Error creating warranty type:", error);
    if (Array.isArray(error)) {
      let messages = [];
      error.forEach(({ field, message }) => {
        setError(field, message);
        messages.push(message);
      });
      ElMessage.error(messages.join(""));
    } else {
      ElMessage.error("Thêm loại bảo hành thất bại");
    }
  } finally {
    isSubmitting.value = false;
  }
};


const submitEdit = async () => {
  if (!editFormRef.value) return;

  clearAllErrors();
  isSubmitting.value = true;

  try {
    const isValid = await editFormRef.value.validate();
    if (!isValid) {
      return;
    }

    // Confirm trước khi cập nhật
    await ElMessageBox.confirm(
      "Bạn có chắc chắn muốn cập nhật loại bảo hành này?",
      "Xác nhận",
      {
        confirmButtonText: "Đồng ý",
        cancelButtonText: "Hủy",
        type: "warning",
      }
    );

    console.log("Form data before update:", form.value);

    await updateWarrantyType(form.value.id, form.value);
    ElMessage.success("Cập nhật loại bảo hành thành công");
    showEditDialog.value = false;
    resetForm();
    await fetchLoaiBaoHanh();
  } catch (error) {
    // Nếu cancel confirm thì không báo lỗi gì cả
    if (error === "cancel" || error === "close") {
      return;
    }

    console.error("Error updating warranty type:", error);
    if (Array.isArray(error)) {
      let messages = [];
      error.forEach(({ field, message }) => {
        setError(field, message);
        messages.push(message);
      });
      ElMessage.error(messages.join(""));
    } else {
      ElMessage.error("Cập nhật loại bảo hành thất bại");
    }
  } finally {
    isSubmitting.value = false;
  }
};


const cancelCreate = () => {
  showCreateDialog.value = false;
  resetForm();
};

const cancelEdit = () => {
  showEditDialog.value = false;
  resetForm();
};

const closeDetail = () => {
  showDetailDialog.value = false;
  selectedBaoHanh.value = null;
};

const fetchLoaiBaoHanh = async () => {
  loading.value = true;
  try {
    const response = await getAllLoaiBaoHanh({
      search: search.value,
      page: pagination.value.page - 1,
      size: pagination.value.size,
    });
    loaiBaoHanhs.value = response.content;
    pagination.value.total = response.totalElements;
  } catch (error) {
    console.error("Error fetching warranty types:", error);
    ElMessage.error(
      error?.response?.data?.message || "Lỗi khi lấy danh sách loại bảo hành"
    );
  } finally {
    loading.value = false;
  }
};

const clearFilters = () => {
  search.value = "";
  pagination.value.page = 1;
  fetchLoaiBaoHanh();
};

const updatePage = (page) => {
  pagination.value.page = page;
  fetchLoaiBaoHanh();
};

const updateSize = (size) => {
  pagination.value.size = size;
  pagination.value.page = 1;
  fetchLoaiBaoHanh();
};

const getRowClassName = ({ rowIndex }) => {
  return rowIndex % 2 === 0 ? "even-row" : "odd-row";
};

watch(search, () => {
  pagination.value.page = 1;
  fetchLoaiBaoHanh();
});

onMounted(() => {
  fetchLoaiBaoHanh();
  fetchModelSanPhams();
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
  box-shadow: 0 4px 15px rgba(38, 66, 195, 0.4);
  transition: all 0.3s ease;
}

.create-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(31, 55, 159, 0.6);
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
  font-weight: 900;
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

.detail-row {
  display: flex;
  align-items: center;
  margin-bottom: 12px; /* khoảng cách giữa các hàng */
}

.detail-label {
  width: 180px;
  font-weight: 600;
  color: #555;
}

.detail-value,
.detail-tag,
.detail-description {
  flex: 1; /* value chiếm hết phần còn lại */
}
.detail-description {
  margin: 0;
}
</style>
