<template>
  <div class="model-management">
    <!-- Page Header -->
    <div class="page-header">
      <div class="page-title">
        <h1>Quản lý Model Sản Phẩm</h1>
        <p class="page-subtitle">Quản lý thông tin chi tiết các model sản phẩm</p>
      </div>
    </div>

    <!-- Form thêm/sửa model -->
    <el-card class="form-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <el-icon class="header-icon">
            <Plus />
          </el-icon>
          <span class="header-title">{{ formTitle }}</span>
        </div>
      </template>

      <el-form :model="modelForm" ref="modelFormRef" :rules="formRules" label-position="top" class="model-form"
        id="form">
        <!-- Thông tin cơ bản -->
        <div class="form-section">
          <div class="section-header">
            <el-icon class="section-icon">
              <InfoFilled />
            </el-icon>
            <h3 class="section-title">Thông tin cơ bản</h3>
          </div>
          <div class="form-grid">
            <el-form-item label="Tên model" prop="tenModel" :error="errors.tenModel">
              <el-input v-model="modelForm.tenModel" placeholder="Nhập tên model" @input="clearFieldError('tenModel')"
                clearable size="large" :class="{ 'updated-input': tenModelUpdated }" />
            </el-form-item>
            <el-form-item label="Loại" prop="idLoai" :error="errors.idLoai">
              <div style="display: flex; gap: 8px; width: 100%;">
                <el-select v-model="modelForm.idLoai" placeholder="Chọn loại" @change="clearFieldError('idLoai')"
                  clearable :loading="loading" size="large" style="width: 100%">
                  <el-option v-for="loai in loais" :key="loai.idLoai" :label="loai.tenLoai" :value="loai.idLoai" />
                </el-select>
                <el-button type="success" size="large" circle @click="addLoaiDialogRef.open()">
                  <el-icon>
                    <Plus />
                  </el-icon>
                </el-button>
              </div>
            </el-form-item>
            <DialogThemLoai ref="addLoaiDialogRef" @saved="handleLoaiSaved" />

            <el-form-item label="Xuất xứ" prop="idXuatXu" :error="errors.idXuatXu">
              <div style="display: flex; gap: 8px; width: 100%;">
                <el-select v-model="modelForm.idXuatXu" placeholder="Chọn xuất xứ" @change="clearFieldError('idXuatXu')"
                  clearable :loading="loading" size="large" style="width: 100%">
                  <el-option v-for="xx in xuatXus" :key="xx.idXuatXu" :label="xx.maXuatXu" :value="xx.idXuatXu" />
                </el-select>
                <el-button type="success" size="large" circle @click="addXuatXuDialogRef.open()">
                  <el-icon>
                    <Plus />
                  </el-icon>
                </el-button>
              </div>
            </el-form-item>
            <DialogThemXuatXu ref="addXuatXuDialogRef" @saved="handleXuatXuSaved" />
          </div>
        </div>

        <!-- Cấu hình phần cứng -->
        <div class="form-section">
          <div class="section-header">
            <el-icon class="section-icon">
              <Setting />
            </el-icon>
            <h3 class="section-title">Cấu hình phần cứng</h3>
          </div>
          <div class="form-grid">
            <el-form-item label="RAM" prop="idRam" :error="errors.idRam">
              <div style="display: flex; gap: 8px; width: 100%;">
                <el-select v-model="modelForm.idRam" placeholder="Chọn RAM" @change="clearFieldError('idRam')" clearable
                  :loading="loading" size="large" style="width: 100%">
                  <el-option v-for="ram in rams" :key="ram.idRam" :label="ram.dungLuongRam" :value="ram.idRam" />
                </el-select>
                <el-button type="success" size="large" circle @click="addDungLuongDialogRef.open()">
                  <el-icon>
                    <Plus />
                  </el-icon>
                </el-button>
              </div>
            </el-form-item>
            <DialogThemRam ref="addDungLuongDialogRef" @saved="handleDungLuongSaved" />

            <el-form-item label="CPU" prop="idCpu" :error="errors.idCpu">
              <div style="display: flex; gap: 8px; width: 100%;">
                <el-select v-model="modelForm.idCpu" placeholder="Chọn CPU" @change="clearFieldError('idCpu')" clearable
                  :loading="loading" size="large" style="width: 100%">
                  <el-option v-for="cpu in cpus" :key="cpu.idCpu" :label="cpu.chipXuLy" :value="cpu.idCpu" />
                </el-select>
                <el-button type="success" size="large" circle @click="addCpuDialogRef.open()">
                  <el-icon>
                    <Plus />
                  </el-icon>
                </el-button>
              </div>
            </el-form-item>
            <DiaLogThemCpu ref="addCpuDialogRef" @saved="handleCpuSaved" />

            <el-form-item label="Màn hình" prop="idManHinh" :error="errors.idManHinh">
              <div style="display: flex; gap: 8px; width: 100%;">
                <el-select v-model="modelForm.idManHinh" placeholder="Chọn màn hình"
                  @change="clearFieldError('idManHinh')" clearable :loading="loading" size="large" style="width: 100%">
                  <el-option v-for="mh in manHinhs" :key="mh.idManHinh" :label="mh.kichThuoc" :value="mh.idManHinh" />
                </el-select>
                <el-button type="success" size="large" circle @click="addManHinhDialogRef.open()">
                  <el-icon>
                    <Plus />
                  </el-icon>
                </el-button>
              </div>
            </el-form-item>
            <DialogThemManHinh ref="addManHinhDialogRef" @saved="handleManHinhSaved" />

            <el-form-item label="Hệ điều hành" prop="idHeDieuHanh" :error="errors.idHeDieuHanh">
              <div style="display: flex; gap: 8px; width: 100%;">
                <el-select v-model="modelForm.idHeDieuHanh" placeholder="Chọn hệ điều hành"
                  @change="clearFieldError('idHeDieuHanh')" clearable :loading="loading" size="large"
                  style="width: 100%">
                  <el-option v-for="hdh in heDieuHanhs" :key="hdh.idHeDieuHanh" :label="hdh.phienBan"
                    :value="hdh.idHeDieuHanh" />
                </el-select>
                <el-button type="success" size="large" circle @click="addHDHDialogRef.open()">
                  <el-icon>
                    <Plus />
                  </el-icon>
                </el-button>
              </div>
            </el-form-item>
            <DialogThemHDH ref="addHDHDialogRef" @saved="handleHDHSaved" />

            <el-form-item label="Pin" prop="idPin" :error="errors.idPin">
              <div style="display: flex; gap: 8px; width: 100%;">
                <el-select v-model="modelForm.idPin" placeholder="Chọn pin" @change="clearFieldError('idPin')" clearable
                  :loading="loading" size="large" style="width: 100%">
                  <el-option v-for="pin in pins" :key="pin.idPin" :label="pin.phienBan" :value="pin.idPin" />
                </el-select>
                <el-button type="success" size="large" circle @click="addPinDialogRef.open()">
                  <el-icon>
                    <Plus />
                  </el-icon>
                </el-button>
              </div>
            </el-form-item>
            <DialogThemPin ref="addPinDialogRef" @saved="handlePinSaved" />
          </div>
        </div>

        <!-- Camera -->
        <div class="form-section">
          <div class="section-header">
            <el-icon class="section-icon">
              <Camera />
            </el-icon>
            <h3 class="section-title">Camera</h3>
          </div>
          <div class="form-grid">
            <el-form-item label="Camera trước" prop="idCameraTruoc" :error="errors.idCameraTruoc">
              <div style="display: flex; gap: 8px; width: 100%;">
                <el-select v-model="modelForm.idCameraTruoc" placeholder="Chọn camera trước"
                  @change="clearFieldError('idCameraTruoc')" clearable :loading="loading" size="large"
                  style="width: 100%">
                  <el-option v-for="cam in cameraTruocs" :key="cam.idCamera"
                    :label="`${cam.doPhanGiai} - ${cam.khauDo} - ${cam.loaiCamera}`" :value="cam.idCamera" />
                </el-select>
                <el-button type="success" size="large" circle @click="addCameraTruocDialogRef.open()">
                  <el-icon>
                    <Plus />
                  </el-icon>
                </el-button>
              </div>
            </el-form-item>
            <DialogThemCameraTruoc ref="addCameraTruocDialogRef" @saved="handleCameraTruocSaved" />

            <el-form-item label="Camera sau" prop="idCameraSau" :error="errors.idCameraSau">
              <div style="display: flex; gap: 8px; width: 100%;">
                <el-select v-model="modelForm.idCameraSau" placeholder="Chọn camera sau"
                  @change="clearFieldError('idCameraSau')" :loading="loading" size="large" style="width: 100%" multiple>
                  <el-option v-for="cam in cameraSaus" :key="cam.idCamera" :label="`${cam.doPhanGiai} - ${cam.khauDo} - ${cam.loaiCamera}`"
                    :value="cam.idCamera" />
                </el-select>
                <el-button type="success" size="large" circle @click="addCameraSauDialogRef.open()">
                  <el-icon>
                    <Plus />
                  </el-icon>
                </el-button>
              </div>
            </el-form-item>

            <DialogThemCameraSau ref="addCameraSauDialogRef" @saved="handleCameraSauSaved" />
          </div>
        </div>

        <!-- Thông tin bổ sung -->
        <div class="form-section">
          <div class="section-header">
            <el-icon class="section-icon">
              <DocumentAdd />
            </el-icon>
            <h3 class="section-title">Thông tin bổ sung</h3>
          </div>
          <div class="form-grid">
            <el-form-item label="Trạng thái" prop="trangThaiSanPhamModel" :error="errors.trangThaiSanPhamModel">
              <el-select v-model="modelForm.trangThaiSanPhamModel" placeholder="Chọn trạng thái"
                @change="clearFieldError('trangThaiSanPhamModel')" clearable size="large" style="width: 100%">
                <el-option v-for="trangThai in statuses" :key="trangThai.value" :label="trangThai.label"
                  :value="trangThai.value" />
              </el-select>
            </el-form-item>
            <el-form-item label="Ngày ra mắt" prop="namRaMat" :error="errors.namRaMat">
              <el-date-picker v-model="modelForm.namRaMat" @change="clearFieldError('namRaMat')" type="date"
                value-format="YYYY-MM-DD" format="DD/MM/YYYY" placeholder="Chọn ngày ra mắt" size="large"
                style="width: 100%" />
            </el-form-item>
          </div>
        </div>

        <div class="form-actions">
          <el-button size="large" @click="resetForm">
            <el-icon>
              <RefreshLeft />
            </el-icon>
            Đặt lại
          </el-button>
          <el-button type="primary" size="large" @click="submitModelForm" :loading="submitting">
            <el-icon>
              <Check />
            </el-icon>
            {{ formMode === 'add' ? 'Thêm mới' : 'Cập nhật' }}
          </el-button>
        </div>
      </el-form>
    </el-card>

    <!-- Bộ lọc -->
    <el-card class="filter-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <el-icon class="header-icon">
            <Search />
          </el-icon>
          <span class="header-title">Bộ lọc tìm kiếm</span>
        </div>
      </template>

      <div class="filter-content">
        <div class="filter-row">
          <el-input v-model="filters.searchQuery" placeholder="Tìm kiếm model..." clearable size="large"
            class="search-input">
            <template #prefix>
              <el-icon>
                <Search />
              </el-icon>
            </template>
          </el-input>

          <el-select v-model="filters.idLoai" placeholder="Lọc theo loại" clearable size="large" class="filter-select">
            <el-option v-for="loai in loais" :key="loai.idLoai" :label="loai.tenLoai" :value="loai.idLoai" />
          </el-select>

          <el-select v-model="filters.idRam" placeholder="Lọc theo RAM" clearable size="large" class="filter-select">
            <el-option v-for="ram in rams" :key="ram.idRam" :label="ram.dungLuongRam" :value="ram.idRam" />
          </el-select>

          <el-select v-model="filters.idXuatXu" placeholder="Lọc theo xuất xứ" clearable size="large"
            class="filter-select">
            <el-option v-for="xx in xuatXus" :key="xx.idXuatXu" :label="xx.maXuatXu" :value="xx.idXuatXu" />
          </el-select>
        </div>

        <div class="filter-actions">
          <el-button type="primary" size="large" @click="fetchDanhMuc">
            <el-icon>
              <Search />
            </el-icon>
            Tìm kiếm
          </el-button>
          <el-button size="large" @click="resetFilters">
            <el-icon>
              <RefreshLeft />
            </el-icon>
            Xóa bộ lọc
          </el-button>
          <router-link to="/admin/products">
            <el-button type="success" size="large">
              <el-icon>
                <ArrowLeft />
              </el-icon>
              Quay lại
            </el-button>
          </router-link>
        </div>
      </div>
    </el-card>

    <!-- Bảng dữ liệu -->
    <el-card class="table-card" shadow="hover">
      <template #header>
        <div class="table-header">
          <div class="table-title">
            <el-icon class="header-icon">
              <List />
            </el-icon>
            <span>Danh sách Model</span>
          </div>
          <div class="table-info">
            <el-tag type="info" size="large">
              Tổng: {{ totalItems }} model
            </el-tag>
          </div>
        </div>
      </template>

      <el-table :data="modelSanPhams" v-loading="loading" stripe class="data-table" :header-cell-style="{
        backgroundColor: '#f8fafc',
        color: '#374151',
        fontWeight: '600',
        fontSize: '14px'
      }">
        <el-table-column label="STT" type="index" :index="indexMethod" width="80" align="center" />
        <el-table-column prop="maModelSanPham" label="Mã model" width="150" show-overflow-tooltip />
        <el-table-column prop="tenModel" label="Tên model" width="200" show-overflow-tooltip />
        <el-table-column label="Loại" width="150">
          <template #default="{ row }">
            <el-tag type="primary" effect="light">
              {{ loaiLabel(row.idLoai) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="RAM" width="120">
          <template #default="{ row }">
            <el-tag type="warning" effect="light">
              {{ ramLabel(row.idRam) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Xuất xứ" width="120">
          <template #default="{ row }">
            <el-tag type="info" effect="light">
              {{ xuatXuLabel(row.idXuatXu) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Trạng thái" width="150">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.trangThaiSanPhamModel)" effect="dark" size="large">
              {{ statusLabel(row.trangThaiSanPhamModel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Ngày ra mắt" width="150">
          <template #default="{ row }">
            <span class="date-text">
              {{ formatNamRaMat(row.namRaMat) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="Hành động" width="200" align="center" fixed="right">
          <template #default="{ row }">
            <div class="action-buttons">
              <el-tooltip content="Xem chi tiết" placement="top">
                <el-button size="small" type="info" @click="viewModel(row)" circle class="action-btn">
                  <el-icon>
                    <View />
                  </el-icon>
                </el-button>
              </el-tooltip>

              <el-tooltip content="Chỉnh sửa" placement="top">
                <el-button size="small" type="primary" @click="editModel(row)" circle class="action-btn">
                  <el-icon>
                    <Edit />
                  </el-icon>
                </el-button>
              </el-tooltip>

              <el-tooltip content="Xóa" placement="top">
                <el-button size="small" type="danger" @click="deleteModel(row.idModelSanPham, row)" circle
                  class="action-btn">
                  <el-icon>
                    <Delete />
                  </el-icon>
                </el-button>
              </el-tooltip>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- Phân trang -->
      <div class="pagination-fixed">
        <div class="d-flex justify-content-center align-items-center gap-3">
          <el-pagination background layout="prev, pager, next" :page-size="pageSize" :current-page="currentPage"
            :total="totalItems" :pager-count="7" prev-text="< Trước" next-text="Sau >"
            @current-change="handlePageChange" />
        </div>
      </div>
    </el-card>

    <!-- Dialog chi tiết model -->
    <el-dialog :title="`Chi tiết model: ${viewModelData.tenModel || 'Không rõ'}`" v-model="dialogVisible" width="60%"
      :before-close="() => dialogVisible = false" style="margin-top: 10px;" class="detail-dialog">
      <div class="dialog-content">
        <div class="detail-grid">
          <div class="detail-item">
            <div class="detail-label">Mã model</div>
            <div class="detail-value">{{ viewModelData.maModelSanPham || 'Không rõ' }}</div>
          </div>

          <div class="detail-item">
            <div class="detail-label">Tên model</div>
            <div class="detail-value">{{ viewModelData.tenModel || 'Không rõ' }}</div>
          </div>

          <div class="detail-item">
            <div class="detail-label">RAM</div>
            <div class="detail-value">
              <el-tag type="warning">{{ ramLabel(viewModelData.idRam) }}</el-tag>
            </div>
          </div>

          <div class="detail-item">
            <div class="detail-label">CPU</div>
            <div class="detail-value">
              <el-tag type="success">{{ cpuLabel(viewModelData.idCpu) }}</el-tag>
            </div>
          </div>

          <div class="detail-item">
            <div class="detail-label">Màn hình</div>
            <div class="detail-value">
              <el-tag type="info">{{ manHinhLabel(viewModelData.idManHinh) }}</el-tag>
            </div>
          </div>

          <div class="detail-item">
            <div class="detail-label">Hệ điều hành</div>
            <div class="detail-value">
              <el-tag type="primary">{{ heDieuHanhLabel(viewModelData.idHeDieuHanh) }}</el-tag>
            </div>
          </div>

          <div class="detail-item">
            <div class="detail-label">Pin</div>
            <div class="detail-value">
              <el-tag>{{ pinLabel(viewModelData.idPin) }}</el-tag>
            </div>
          </div>

          <div class="detail-item">
            <div class="detail-label">Camera trước</div>
            <div class="detail-value">
              <el-tag type="warning">{{ cameraTruocLabel(viewModelData.idCameraTruoc) }}</el-tag>
            </div>
          </div>

          <div class="detail-item">
            <div class="detail-label">Camera sau</div>
            <div class="detail-value">
              <el-tag type="info">{{ cameraSauLabel(viewModelData.idCameraSau) }}</el-tag>
            </div>
          </div>

          <div class="detail-item">
            <div class="detail-label">Xuất xứ</div>
            <div class="detail-value">
              <el-tag type="info">{{ xuatXuLabel(viewModelData.idXuatXu) }}</el-tag>
            </div>
          </div>

          <div class="detail-item">
            <div class="detail-label">Loại</div>
            <div class="detail-value">
              <el-tag type="primary">{{ loaiLabel(viewModelData.idLoai) }}</el-tag>
            </div>
          </div>

          <div class="detail-item detail-full">
            <div class="detail-label">Trạng thái</div>
            <div class="detail-value">
              <el-tag :type="statusTagType(viewModelData.trangThaiSanPhamModel)" effect="dark" size="large">
                {{ statusLabel(viewModelData.trangThaiSanPhamModel) }}
              </el-tag>
            </div>
          </div>

          <div class="detail-item detail-full">
            <div class="detail-label">Ngày ra mắt</div>
            <div class="detail-value date-text">
              {{ formatNamRaMat(viewModelData.namRaMat) }}
            </div>
          </div>
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button size="large" @click="dialogVisible = false">
            <el-icon>
              <Close />
            </el-icon>
            Đóng
          </el-button>
          <el-button type="primary" size="large" @click="switchToEditMode">
            <el-icon>
              <Edit />
            </el-icon>
            Chuyển sang sửa
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, computed, watch, onMounted } from 'vue';
import {
  Edit, View, Delete, Plus, InfoFilled, Setting, Camera,
  DocumentAdd, Search, RefreshLeft, Check, ArrowLeft,
  List, Close
} from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { debounce } from 'lodash';
import {
  getAllRamList,
  getAllCpuList,
  getAllManHinhList,
  getAllHDHList,
  getAllPinList,
  getAllCameraTruocList,
  getAllCameraSauList,
  getAllXuatXuList,
  getAllLoaiList,
  filterModelSanPham,
  putModelSanPham,
  postModelSanPham,
  finByIdModelSanPham,
  deleteModelSanPham
} from '../../../Service/Adminservice/Products/ProductAdminService';
import DialogThemLoai from '@/components/Admin/dialogs/DialogThemLoai.vue';
import DialogThemXuatXu from '@/components/Admin/dialogs/DialogThemXuatXu.vue';
import DialogThemRam from '@/components/Admin/dialogs/DialogThemRam.vue';
import DiaLogThemCpu from '@/components/Admin/dialogs/DiaLogThemCpu.vue';
import DialogThemManHinh from '@/components/Admin/dialogs/DialogThemManHinh.vue';
import DialogThemHDH from '@/components/Admin/dialogs/DialogThemHDH.vue';
import DialogThemPin from '@/components/Admin/dialogs/DialogThemPin.vue';
import DialogThemCameraSau from '@/components/Admin/dialogs/DialogThemCameraSau.vue';
import DialogThemCameraTruoc from '@/components/Admin/dialogs/DialogThemCameraTruoc.vue';
import { useToast } from 'vue-toastification';

export default {
  name: 'ModelManagement',
  components: {
    Edit, View, Delete, Plus, InfoFilled, Setting, Camera,
    DocumentAdd, Search, RefreshLeft, Check, ArrowLeft,
    List, Close, DialogThemLoai,
    DialogThemXuatXu,
    DialogThemRam,
    DiaLogThemCpu,
    DialogThemManHinh,
    DialogThemHDH,
    DialogThemPin,
    DialogThemCameraSau,
    DialogThemCameraTruoc
  },
  setup() {
    const modelSanPhams = ref([]);
    const loading = ref(false);
    const submitting = ref(false);
    const rams = ref([]);
    const cpus = ref([]);
    const manHinhs = ref([]);
    const heDieuHanhs = ref([]);
    const pins = ref([]);
    const cameraTruocs = ref([]);
    const cameraSaus = ref([]);
    const xuatXus = ref([]);
    const loais = ref([]);
    const errors = reactive({});
    const addLoaiDialogRef = ref(null);
    const addXuatXuDialogRef = ref(null);
    const addDungLuongDialogRef = ref(null);
    const addCpuDialogRef = ref(null);
    const addManHinhDialogRef = ref(null);
    const addHDHDialogRef = ref(null);
    const addPinDialogRef = ref(null);
    const addCameraSauDialogRef = ref(null);
    const addCameraTruocDialogRef = ref(null);
    const toast = useToast();

    const statuses = ref([
      { value: 'ACTIVE', label: 'Đang hoạt động', type: 'success' },
      { value: 'DISCONTINUED', label: 'Ngừng sản xuất', type: 'warning' },
      { value: 'UPCOMING', label: 'Chờ ra mắt', type: 'info' },
    ]);
    const tenModelUpdated = ref(false);

    const formMode = ref('add');
    const modelFormRef = ref(null);
    const currentPage = ref(1);
    const totalItems = ref(0);
    const pageSize = 5;
    const dialogVisible = ref(false);
    const viewModelData = ref({});

    const filters = reactive({
      searchQuery: '',
      idLoai: null,
      idRam: null,
      idXuatXu: null
    });

    const modelForm = ref({
      idModelSanPham: null,
      tenModel: '',
      idRam: null,
      idCpu: null,
      idManHinh: null,
      idHeDieuHanh: null,
      idPin: null,
      idCameraTruoc: null,
      idCameraSau: [],
      idXuatXu: null,
      idLoai: null,
      trangThaiSanPhamModel: 'ACTIVE',
      namRaMat: null
    });

    const formRules = ref({
      tenModel: [{ required: true, message: 'Vui lòng nhập tên model', trigger: 'blur' }],
      idLoai: [{ required: true, message: 'Vui lòng chọn loại', trigger: 'change' }],
      idXuatXu: [{ required: true, message: 'Vui lòng chọn xuất xứ', trigger: 'change' }],
      idRam: [{ required: true, message: 'Vui lòng chọn RAM', trigger: 'change' }],
      idCpu: [{ required: true, message: 'Vui lòng chọn CPU', trigger: 'change' }],
      idManHinh: [{ required: true, message: 'Vui lòng chọn màn hình', trigger: 'change' }],
      idHeDieuHanh: [{ required: true, message: 'Vui lòng chọn hệ điều hành', trigger: 'change' }],
      idPin: [{ required: true, message: 'Vui lòng chọn pin', trigger: 'change' }],
      idCameraTruoc: [{ required: true, message: 'Vui lòng chọn camera trước', trigger: 'change' }],
      idCameraSau: [
        { type: 'array', required: true, message: 'Vui lòng chọn ít nhất một camera sau', trigger: 'change' }
      ]
    });

    const formTitle = computed(() => formMode.value === 'add' ? 'Thêm Model Mới' : 'Sửa Model');

    const indexMethod = (index) => (currentPage.value - 1) * pageSize + index + 1;

    const ramLabel = (idRam) => rams.value.find(r => r.idRam === idRam)?.dungLuongRam || 'Không rõ';
    const cpuLabel = (idCpu) => cpus.value.find(c => c.idCpu === idCpu)?.chipXuLy || 'Không rõ';
    const manHinhLabel = (idManHinh) => manHinhs.value.find(m => m.idManHinh === idManHinh)?.kichThuoc || 'Không rõ';
    const heDieuHanhLabel = (idHeDieuHanh) => heDieuHanhs.value.find(h => h.idHeDieuHanh === idHeDieuHanh)?.phienBan || 'Không rõ';
    const pinLabel = (idPin) => pins.value.find(p => p.idPin === idPin)?.phienBan || 'Không rõ';
    const cameraTruocLabel = (idCamera) => {
      const camera = cameraTruocs.value.find(c => c.idCamera === idCamera);
      return camera ? `${camera.doPhanGiai} - ${camera.khauDo || 'Không rõ'}` : 'Không rõ';
    };
    const cameraSauLabel = (idCameraSau) => {
      if (!idCameraSau || !Array.isArray(idCameraSau)) return 'Không rõ';
      return idCameraSau
        .map(id => {
          const camera = cameraSaus.value.find(c => c.idCamera === id);
          return camera ? `${camera.doPhanGiai} - ${camera.khauDo || 'Không rõ'}` : 'Không rõ';
        })
        .join(', ');
    };
    const loaiLabel = (idLoai) => loais.value.find(l => l.idLoai === idLoai)?.tenLoai || 'Không rõ';
    const xuatXuLabel = (idXuatXu) => xuatXus.value.find(x => x.idXuatXu === idXuatXu)?.maXuatXu || 'Không rõ';

    const statusTagType = (trangThai) => {
      const status = statuses.value.find(s => s.value === trangThai);
      if (!status) {
        console.warn(`Invalid status value: ${trangThai}`);
        return 'info';
      }
      return status.type;
    };

    const statusLabel = (trangThai) => {
      const status = statuses.value.find(s => s.value === trangThai);
      if (!status) {
        return 'Không rõ';
      }
      return status.label;
    };

    const formatNamRaMat = (namRaMat) => {
      if (!namRaMat || isNaN(Date.parse(namRaMat))) return 'Không rõ';
      const date = new Date(namRaMat);
      return `${date.getDate().toString().padStart(2, '0')}/${(date.getMonth() + 1).toString().padStart(2, '0')}/${date.getFullYear()}`;
    };

    watch(() => modelForm.value.idLoai, (newIdLoai, oldIdLoai) => {
      if (newIdLoai) {
        const selectedLoai = loais.value.find(l => l.idLoai === newIdLoai);
        if (selectedLoai) {
          const tenLoai = selectedLoai.tenLoai || '';
          let currentTenModel = modelForm.value.tenModel || '';
          if (oldIdLoai) {
            const oldLoai = loais.value.find(l => l.idLoai === oldIdLoai);
            if (oldLoai && currentTenModel.endsWith(' ' + oldLoai.tenLoai)) {
              currentTenModel = currentTenModel.slice(0, -(oldLoai.tenLoai.length + 1));
            }
          }
          if (!currentTenModel.endsWith(' ' + tenLoai)) {
            if (currentTenModel) {
              modelForm.value.tenModel = `${currentTenModel.trim()} ${tenLoai}`;
            } else {
              modelForm.value.tenModel = tenLoai;
            }
            tenModelUpdated.value = true;
            setTimeout(() => {
              tenModelUpdated.value = false;
            }, 2000);
          }
        }
      }
    });

    const handleLoaiSaved = (savedLoai) => {
      loais.value.push({ idLoai: savedLoai.id, tenLoai: savedLoai.tenLoai });
      modelForm.value.idLoai = savedLoai.id;
    };
    

    const handleXuatXuSaved = (savedXuatXu) => {
      xuatXus.value.push({ idXuatXu: savedXuatXu.id, maXuatXu: savedXuatXu.maXuatXu });
      modelForm.value.idXuatXu = savedXuatXu.id;
    };

    const handleDungLuongSaved = (savedDungLuong) => {
      rams.value.push({ idRam: savedDungLuong.id, dungLuongRam: savedDungLuong.dungLuong });
      modelForm.value.idRam = savedDungLuong.id;
    };

    const handleCpuSaved = (savedCpu) => {
      cpus.value.push({ idCpu: savedCpu.id, chipXuLy: savedCpu.chipXuLy });
      modelForm.value.idCpu = savedCpu.id;
    };

    const handleManHinhSaved = (savedManHinh) => {
      manHinhs.value.push({ idManHinh: savedManHinh.id, kichThuoc: savedManHinh.kichThuoc });
      modelForm.value.idManHinh = savedManHinh.id;
    };

    const handleHDHSaved = (savedHDH) => {
      heDieuHanhs.value.push({ idHeDieuHanh: savedHDH.id, phienBan: savedHDH.phienBan });
      modelForm.value.idHeDieuHanh = savedHDH.id;
    };

    const handlePinSaved = (savedPin) => {
      pins.value.push({ idPin: savedPin.id, phienBan: savedPin.phienBan });
      modelForm.value.idPin = savedPin.id;
    };

    const handleCameraSauSaved = (savedCameraSau) => {
      cameraSaus.value.push({
        idCamera: savedCameraSau.id,
        loaiCamera: savedCameraSau.loaiCamera,
        khauDo: savedCameraSau.khauDo,
        doPhanGiai: savedCameraSau.doPhanGiai
      });

      modelForm.value.idCameraSau = [savedCameraSau.id];

    };

    const handleCameraTruocSaved = (savedCameraTruoc) => {
      // Thêm camera trước mới vào danh sách cameraTruocs
      cameraTruocs.value.push({
        idCamera: savedCameraTruoc.id,
        loaiCamera: savedCameraTruoc.loaiCamera,
        khauDo: savedCameraTruoc.khauDo,
        doPhanGiai: savedCameraTruoc.doPhanGiai
      });

      // Cập nhật idCameraTruoc trong modelForm
      modelForm.value.idCameraTruoc = savedCameraTruoc.id;
    };

    const fetchDanhMuc = async () => {
      loading.value = true;
      try {
        const results = await Promise.allSettled([
          getAllRamList(),
          getAllCpuList(),
          getAllManHinhList(),
          getAllHDHList(),
          getAllPinList(),
          getAllCameraTruocList(),
          getAllCameraSauList(),
          getAllXuatXuList(),
          getAllLoaiList(),
          filterModelSanPham(currentPage.value - 1, pageSize, filters.searchQuery, filters.idLoai, filters.idRam, filters.idXuatXu)
        ]);

        const failedRequests = results.filter(r => r.status === 'rejected');
        if (failedRequests.length > 0) {
          toast.warning('Một số dữ liệu không tải được, vui lòng thử lại!');
          console.error('API thất bại:', failedRequests);
        }

        rams.value = results[0].status === 'fulfilled' && Array.isArray(results[0].value)
          ? results[0].value.map(ram => ({ idRam: ram.id, dungLuongRam: ram.dungLuong || '' }))
          : [];
        cpus.value = results[1].status === 'fulfilled' && Array.isArray(results[1].value)
          ? results[1].value.map(cpu => ({ idCpu: cpu.id, chipXuLy: cpu.chipXuLy || '' }))
          : [];
        manHinhs.value = results[2].status === 'fulfilled' && Array.isArray(results[2].value)
          ? results[2].value.map(mh => ({ idManHinh: mh.id, kichThuoc: mh.kichThuoc || '' }))
          : [];
        heDieuHanhs.value = results[3].status === 'fulfilled' && Array.isArray(results[3].value)
          ? results[3].value.map(hdh => ({ idHeDieuHanh: hdh.id, phienBan: hdh.phienBan || '' }))
          : [];
        pins.value = results[4].status === 'fulfilled' && Array.isArray(results[4].value)
          ? results[4].value.map(pin => ({ idPin: pin.id, phienBan: pin.phienBan || '' }))
          : [];
        cameraTruocs.value = results[5].status === 'fulfilled' && Array.isArray(results[5].value)
          ? results[5].value.map(cam => ({ idCamera: cam.id, doPhanGiai: cam.doPhanGiai || '', khauDo: cam.khauDo || '' , loaiCamera: cam.loaiCamera || ''}))
          : [];
        cameraSaus.value = results[6].status === 'fulfilled' && Array.isArray(results[6].value)
          ? results[6].value.map(cam => ({ idCamera: cam.id, doPhanGiai: cam.doPhanGiai || '', khauDo: cam.khauDo || '' , loaiCamera: cam.loaiCamera || ''}))
          : [];
        xuatXus.value = results[7].status === 'fulfilled' && Array.isArray(results[7].value)
          ? results[7].value.map(xx => ({ idXuatXu: xx.id, maXuatXu: xx.maXuatXu || '' }))
          : [];
        loais.value = results[8].status === 'fulfilled' && Array.isArray(results[8].value)
          ? results[8].value.map(loai => ({ idLoai: loai.id, tenLoai: loai.tenLoai || '' }))
          : [];

        modelSanPhams.value = results[9].status === 'fulfilled' && Array.isArray(results[9].value.content)
          ? results[9].value.content.map(model => {
            const validStatuses = ['ACTIVE', 'DISCONTINUED', 'UPCOMING'];
            const trangThai = validStatuses.includes(model.trangThaiSanPhamModel)
              ? model.trangThaiSanPhamModel
              : 'ACTIVE';
            if (!validStatuses.includes(model.trangThaiSanPhamModel)) {
              console.warn(`Invalid trangThaiSanPhamModel: ${model.trangThaiSanPhamModel} for model ${model.maModelSanPham}`);
            }
            return {
              ...model,
              tenModel: model.tenModel || '',
              maModelSanPham: model.maModelSanPham || 'Không rõ',
              trangThaiSanPhamModel: trangThai,
              idCameraSau: Array.isArray(model.cameraSaus) ? model.cameraSaus.map(cam => cam?.id).filter(id => id != null) : []
            };
          })
          : [];

        totalItems.value = results[9].status === 'fulfilled' ? results[9].value.totalElements || 0 : 0;

        if (!modelSanPhams.value.length) {
          ElMessage.info('Không tìm thấy model.');
        }
      } finally {
        loading.value = false;
      }
    };

    const resetFilters = () => {
      Object.assign(filters, { searchQuery: '', idLoai: null, idRam: null, idXuatXu: null });
      currentPage.value = 1;
      fetchDanhMuc();
    };

    const resetForm = () => {
      const hasChanges = Object.values(modelForm.value).some(value => value !== null && value !== '' && !(Array.isArray(value) && value.length === 0));
      if (hasChanges) {
        ElMessageBox.confirm('Bạn có chắc muốn đặt lại form?', 'Xác nhận', {
          confirmButtonText: 'Có',
          cancelButtonText: 'Không'
        }).then(() => {
          formMode.value = 'add';
          modelFormRef.value?.resetFields();
          modelForm.value = {
            idModelSanPham: null,
            tenModel: '',
            idRam: null,
            idCpu: null,
            idManHinh: null,
            idHeDieuHanh: null,
            idPin: null,
            idCameraTruoc: null,
            idCameraSau: [],
            idXuatXu: null,
            idLoai: null,
            trangThaiSanPhamModel: 'ACTIVE',
            namRaMat: null
          };
          tenModelUpdated.value = false;
          toast.success('Form đã được đặt lại.');
        });
      } else {
        formMode.value = 'add';
        modelFormRef.value?.resetFields();
        modelForm.value = {
          idModelSanPham: null,
          tenModel: '',
          idRam: null,
          idCpu: null,
          idManHinh: null,
          idHeDieuHanh: null,
          idPin: null,
          idCameraTruoc: null,
          idCameraSau: [],
          idXuatXu: null,
          idLoai: null,
          trangThaiSanPhamModel: 'ACTIVE',
          namRaMat: null
        };
        tenModelUpdated.value = false;
      }
    };

    const viewModel = async (model) => {
      try {
        const response = await finByIdModelSanPham(model.idModelSanPham);
        if (!response) {
          toast.error('Không tìm thấy dữ liệu model!');
          return;
        }
        const validStatuses = ['ACTIVE', 'DISCONTINUED', 'UPCOMING'];
        const trangThai = validStatuses.includes(response.trangThaiSanPhamModel)
          ? response.trangThaiSanPhamModel
          : 'ACTIVE';
        if (!validStatuses.includes(response.trangThaiSanPhamModel)) {
          console.warn(`Invalid trangThaiSanPhamModel in viewModel: ${response.trangThaiSanPhamModel}`);
        }
        viewModelData.value = {
          ...response,
          tenModel: response.tenModel || '',
          maModelSanPham: response.maModelSanPham || 'Không rõ',
          trangThaiSanPhamModel: trangThai,
          idCameraSau: Array.isArray(response.cameraSaus) ? response.cameraSaus.map(cam => cam?.id).filter(id => id != null) : []
        };
        dialogVisible.value = true;
      } catch (error) {
        console.error('Lỗi khi lấy chi tiết model:', error);
        if (error.response?.status === 404) {
          toast.error('Model không tồn tại!');
        } else if (error.response?.status === 500) {
          toast.error('Lỗi máy chủ, vui lòng thử lại sau!');
        } else {
          toast.error('Không thể lấy chi tiết model, vui lòng thử lại!');
        }
      }
    };

    const editModel = async (model) => {
      try {
        const response = await finByIdModelSanPham(model.idModelSanPham);
        if (!response) {
          toast.error('Không tìm thấy dữ liệu model!');
          return;
        }
        const validStatuses = ['ACTIVE', 'DISCONTINUED', 'UPCOMING'];
        const trangThai = validStatuses.includes(response.trangThaiSanPhamModel)
          ? response.trangThaiSanPhamModel
          : 'ACTIVE';
        if (!validStatuses.includes(response.trangThaiSanPhamModel)) {
          console.warn(`Invalid trangThaiSanPhamModel in editModel: ${response.trangThaiSanPhamModel}`);
        }
        formMode.value = 'edit';
        modelForm.value = {
          ...response,
          idModelSanPham: response.idModelSanPham,
          tenModel: response.tenModel || '',
          idRam: response.idRam,
          idCpu: response.idCpu,
          idManHinh: response.idManHinh,
          idHeDieuHanh: response.idHeDieuHanh,
          idPin: response.idPin,
          idCameraTruoc: response.idCameraTruoc,
          idCameraSau: Array.isArray(response.cameraSaus) ? response.cameraSaus.map(cam => cam?.id).filter(id => id != null) : [],
          idXuatXu: response.idXuatXu,
          idLoai: response.idLoai,
          trangThaiSanPhamModel: trangThai,
          namRaMat: response.namRaMat
        };
        document.getElementById('form').scrollIntoView();

      } catch (error) {
        console.error('Lỗi khi lấy chi tiết model để chỉnh sửa:', error);
        toast.error('Không thể lấy dữ liệu model để chỉnh sửa!');
      }
    };

    const switchToEditMode = () => {
      formMode.value = 'edit';
      modelForm.value = { ...viewModelData.value };
      dialogVisible.value = false;
    };

    function clearFieldError(fieldName) {
      if (errors[fieldName]) {
        delete errors[fieldName];
      }
    }

    const submitModelForm = async () => {
      if (!modelFormRef.value) return;

      await modelFormRef.value.validate(async (valid) => {
        if (!valid) {
          toast.error('Vui lòng kiểm tra lại các trường thông tin!');
          return;
        }

        submitting.value = true;

        try {
          const cameraSaus = modelForm.value.idCameraSau.map((id, index) => ({
            id: id,
            isChinh: index === 0
          }));

          const payload = {
            tenModel: modelForm.value.tenModel ? modelForm.value.tenModel.trim() : '',
            idRam: modelForm.value.idRam,
            idCpu: modelForm.value.idCpu,
            idManHinh: modelForm.value.idManHinh,
            idHeDieuHanh: modelForm.value.idHeDieuHanh,
            idPin: modelForm.value.idPin,
            idCameraTruoc: modelForm.value.idCameraTruoc,
            cameraSaus: cameraSaus,
            idXuatXu: modelForm.value.idXuatXu,
            idLoai: modelForm.value.idLoai,
            trangThaiSanPhamModel: modelForm.value.trangThaiSanPhamModel,
            namRaMat: modelForm.value.namRaMat
          };

          if (formMode.value === 'edit') {
            await putModelSanPham(modelForm.value.idModelSanPham, payload);
            const index = modelSanPhams.value.findIndex(
              (m) => m.idModelSanPham === modelForm.value.idModelSanPham
            );
            if (index !== -1) {
              modelSanPhams.value[index] = {
                ...modelSanPhams.value[index],
                ...payload,
                idCameraSau: payload.cameraSaus.map(cam => cam.id)
              };
            }
            toast.success('Cập nhật model thành công!');
          } else {
            const newForm = await postModelSanPham(payload);
            if (newForm && typeof newForm === 'object') {
              modelSanPhams.value.unshift({
                ...newForm,
                tenModel: newForm.tenModel || '',
                maModelSanPham: newForm.maModelSanPham || 'Không rõ',
                trangThaiSanPhamModel: newForm.trangThaiSanPhamModel || 'ACTIVE',
                idCameraSau: newForm.cameraSaus ? newForm.cameraSaus.map(cam => cam.id) : []
              });
              totalItems.value += 1;
              toast.success('Thêm model thành công!');
            } else {
              toast.error('Dữ liệu trả về từ API không hợp lệ!');
            }
          }

          resetForm();
        } catch (error) {
          console.error('❌ Bắt lỗi từ backend:', error);
          const errorData = error.response?.data;
          Object.keys(errors).forEach((key) => delete errors[key]);

          const errorMessages = {
            'model.duplicate.config': 'Cấu hình model đã tồn tại, vui lòng kiểm tra lại!',
            'camera_sau.not_found': 'Camera sau không tồn tại!',
            'model.ten_model.required': 'Tên model là bắt buộc!',
            'model.required_fields_missing': 'Vui lòng điền đầy đủ các trường bắt buộc!'
          };

          if (error.response?.status === 400 && errorData) {
            if (typeof errorData.message === 'object' && errorData.message !== null) {
              Object.assign(errors, errorData.message);
            } else if (typeof errorData.message === 'string') {
              toast.error(errorMessages[errorData.message] || errorData.message || 'Đã xảy ra lỗi nghiệp vụ!');
            } else {
              toast.error('Lỗi không xác định từ server!');
            }
          } else if (error.response?.status === 500) {
            toast.error('Lỗi máy chủ, vui lòng thử lại sau!');
          } else if (!error.response) {
            toast.error('Không thể kết nối đến máy chủ!');
          } else {
            toast.error('Có lỗi hệ thống xảy ra!');
          }
        } finally {
          submitting.value = false;
        }
      });
    };

    const deleteModel = (id, model) => {
      ElMessageBox.confirm('Bạn có chắc chắn muốn xóa model này?', 'Xác nhận', {
        confirmButtonText: 'Có',
        cancelButtonText: 'Không'
      }).then(async () => {
        await deleteModelSanPham(id);
        modelSanPhams.value = modelSanPhams.value.filter(m => m.idModelSanPham !== id);
        totalItems.value -= 1;
        toast.success('Xóa model thành công!');
        if (!modelSanPhams.value.length && currentPage.value > 1) {
          currentPage.value -= 1;
          await fetchDanhMuc();
        }
      });
    };

    const handlePageChange = (newPage) => {
      currentPage.value = newPage;
      fetchDanhMuc();
    };

    watch(() => [filters.searchQuery, filters.idLoai, filters.idRam, filters.idXuatXu], debounce(() => {
      currentPage.value = 1;
      fetchDanhMuc();
    }, 500));

    onMounted(() => fetchDanhMuc());

    return {
      modelSanPhams,
      loading,
      submitting,
      rams,
      cpus,
      manHinhs,
      heDieuHanhs,
      pins,
      cameraTruocs,
      cameraSaus,
      xuatXus,
      loais,
      statuses,
      formMode,
      formTitle,
      modelForm,
      modelFormRef,
      formRules,
      currentPage,
      totalItems,
      pageSize,
      filters,
      dialogVisible,
      viewModelData,
      tenModelUpdated,
      errors,
      addLoaiDialogRef,
      addXuatXuDialogRef,
      addDungLuongDialogRef,
      addCpuDialogRef,
      addManHinhDialogRef,
      addHDHDialogRef,
      addPinDialogRef,
      addCameraTruocDialogRef,
      addCameraSauDialogRef,
      toast,
      handlePinSaved,
      handleCameraTruocSaved,
      handleCameraSauSaved,
      handleHDHSaved,
      handleManHinhSaved,
      handleCpuSaved,
      handleDungLuongSaved,
      handleLoaiSaved,
      handleXuatXuSaved,
      clearFieldError,
      indexMethod,
      ramLabel,
      cpuLabel,
      manHinhLabel,
      heDieuHanhLabel,
      pinLabel,
      cameraTruocLabel,
      cameraSauLabel,
      loaiLabel,
      xuatXuLabel,
      statusLabel,
      statusTagType,
      formatNamRaMat,
      fetchDanhMuc,
      resetFilters,
      resetForm,
      viewModel,
      editModel,
      switchToEditMode,
      submitModelForm,
      deleteModel,
      handlePageChange
    };
  },
};
</script>

<style scoped>
.updated-input {
  border: 2px solid #67C23A !important;
  transition: border 0.3s;
}

.model-management {
  max-width: 1400px;
  margin: 0 auto;
  padding: 24px;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  min-height: 100vh;
}

.page-header {
  margin-bottom: 32px;
}

.page-title h1 {
  font-size: 32px;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 8px 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.page-subtitle {
  font-size: 16px;
  color: #6b7280;
  margin: 0;
}

.form-card,
.filter-card,
.table-card {
  margin-bottom: 24px;
  border-radius: 16px;
  border: none;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.form-card:hover,
.filter-card:hover,
.table-card:hover {
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 12px;
  font-weight: 600;
  font-size: 18px;
  color: #374151;
}

.header-icon {
  font-size: 20px;
  color: #667eea;
}

.header-title {
  color: #1f2937;
}

.model-form {
  padding: 8px 0;
}

.form-section {
  margin-bottom: 32px;
  padding: 24px;
  background: #f8fafc;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}

.section-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 2px solid #e2e8f0;
}

.section-icon {
  font-size: 18px;
  color: #667eea;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #374151;
  margin: 0;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  margin-top: 32px;
  padding-top: 24px;
  border-top: 2px solid #e2e8f0;
}

.filter-content {
  padding: 8px 0;
}

.filter-row {
  display: grid;
  grid-template-columns: 2fr repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 20px;
}

.search-input {
  min-width: 300px;
}

.filter-select {
  min-width: 200px;
}

.filter-actions {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
}

.table-title {
  display: flex;
  align-items: center;
  gap: 12px;
  font-weight: 600;
  font-size: 18px;
  color: #374151;
}

.table-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.data-table {
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid #e2e8f0;
}

.data-table :deep(.el-table__header) {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
}

.data-table :deep(.el-table__row:hover) {
  background-color: #f0f9ff;
}

.date-text {
  font-family: 'Monaco', 'Menlo', monospace;
  font-size: 13px;
  color: #6b7280;
  background: #f3f4f6;
  padding: 4px 8px;
  border-radius: 6px;
}

.action-buttons {
  display: flex;
  gap: 8px;
  justify-content: center;
  flex-wrap: wrap;
}

.action-btn {
  transition: all 0.2s ease;
}

.action-btn:hover {
  transform: scale(1.1);
}

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

.detail-dialog :deep(.el-dialog) {
  border-radius: 16px;
  overflow: hidden;
}

.detail-dialog :deep(.el-dialog__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 20px 24px;
}

.detail-dialog :deep(.el-dialog__title) {
  color: white;
  font-weight: 600;
}

.dialog-content {
  padding: 24px;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.detail-item {
  padding: 16px;
  background: #f8fafc;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
  transition: all 0.2s ease;
}

.detail-item:hover {
  background: #f0f9ff;
  border-color: #3b82f6;
}

.detail-item.detail-full {
  grid-column: span 2;
}

.detail-label {
  font-weight: 600;
  color: #374151;
  margin-bottom: 8px;
  font-size: 14px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.detail-value {
  color: #1f2937;
  font-size: 15px;
  font-weight: 500;
}

.dialog-footer {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  padding: 20px 24px;
  background: #f8fafc;
  border-top: 1px solid #e2e8f0;
}

@media (max-width: 1200px) {
  .form-grid {
    grid-template-columns: 1fr;
  }

  .filter-row {
    grid-template-columns: 1fr;
  }

  .detail-grid {
    grid-template-columns: 1fr;
  }

  .detail-item.detail-full {
    grid-column: span 1;
  }
}

@media (max-width: 768px) {
  .model-management {
    padding: 16px;
  }

  .page-title h1 {
    font-size: 24px;
  }

  .form-section {
    padding: 16px;
  }

  .form-actions {
    flex-direction: column;
  }

  .filter-actions {
    flex-direction: column;
  }

  .table-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .action-buttons {
    flex-direction: row;
    gap: 6px;
  }

  .dialog-footer {
    flex-direction: column;
  }
}

:deep(.el-card__header) {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-bottom: 2px solid #e2e8f0;
  font-weight: 600;
}

:deep(.el-form-item__label) {
  font-weight: 600;
  color: #374151;
}

:deep(.el-input__wrapper) {
  border-radius: 8px;
  transition: all 0.2s ease;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #667eea;
}

:deep(.el-select .el-input__wrapper) {
  border-radius: 8px;
}

:deep(.el-button) {
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.2s ease;
}

:deep(.el-button:hover) {
  transform: translateY(-1px);
}

:deep(.el-tag) {
  border-radius: 6px;
  font-weight: 500;
}

:deep(.el-pagination) {
  font-weight: 500;
}

:deep(.el-pagination .el-pager li) {
  border-radius: 6px;
  margin: 0 2px;
}

:deep(.el-table th) {
  background: #f8fafc !important;
  color: #374151 !important;
  font-weight: 600 !important;
}

:deep(.el-form-item__error) {
  color: red;
  font-size: 13px;
  line-height: 1.2;
  margin-top: 4px;
  display: block;
}
</style>