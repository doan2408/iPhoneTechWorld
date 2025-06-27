<template>
  <div class="container">
    <h2 style="margin-bottom: 20px; font-weight: 600;">Quản lý model sản phẩm</h2>

    <div class="filter-section" style="
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-wrap: wrap;
    row-gap: 12px;
    column-gap: 16px;
    padding: 16px;
    background: #f9f9f9;
    border-radius: 8px;
    box-shadow: 0 2px 6px rgba(0,0,0,0.05);
    margin-bottom: 20px;
  ">
      <!-- Bộ lọc bên trái -->
      <div style="display: flex; align-items: center; flex-wrap: wrap; gap: 12px; min-width: 300px;">
        <el-input v-model="searchQuery" placeholder="Tìm kiếm mã/tên model" clearable style="width: 220px;" />
        <el-select v-model="filterStatus" placeholder="Lọc trạng thái" clearable style="width: 180px;">
          <el-option label="Active" value="ACTIVE" />
          <el-option label="Out of Stock" value="OUT_OF_STOCK" />
          <el-option label="Inactive" value="INACTIVE" />
        </el-select>
        <el-button type="primary" @click="fetchDanhMuc">Tìm</el-button>
      </div>

      <!-- Hành động bên phải -->
      <div style="display: flex; align-items: center; gap: 12px;">
        <el-button type="primary" @click="openAddDialog">Thêm model mới</el-button>
        <router-link to="/admin/products">
          <el-button type="success">Quay lại</el-button>
        </router-link>
      </div>
    </div>

    <el-table :data="modelSanPhams" style="margin-top: 20px" v-loading="loading" stripe border>
      <el-table-column label="STT" type="index" :index="indexMethod" width="80"></el-table-column>
      <el-table-column prop="maModelSanPham" label="Mã model" key="maModelSanPham"></el-table-column>
      <el-table-column prop="tenModel" label="Tên model" key="tenModel" width="200"></el-table-column>
      <el-table-column label="Loại" key="loai">
        <template #default="{ row }">
          {{ loaiLabel(row.idLoai) }}
        </template>
      </el-table-column>
      <el-table-column label="RAM" key="ram">
        <template #default="{ row }">
          {{ ramLabel(row.idRam) }}
        </template>
      </el-table-column>
      <el-table-column label="Xuất xứ" key="xuatXu">
        <template #default="{ row }">
          {{ xuatXuLabel(row.idXuatXu) }}
        </template>
      </el-table-column>
      <el-table-column label="Ngày ra mắt" key="namRaMat">
        <template #default="{ row }">
          {{ formatNamRaMat(row.namRaMat) }}
        </template>
      </el-table-column>
      <el-table-column prop="trangThai" label="Trạng thái" key="trangThai"></el-table-column>
      <el-table-column label="Hành động" width="200" key="actions">
        <template #default="{ row }">
          <div class="action-buttons-horizontal">
            <el-button type="info" @click="openDetailDialog(row)">Xem</el-button>
            <el-button type="primary" @click="openEditDialog(row)">Sửa</el-button>
            <el-button type="danger" @click="deleteModel(row.idModelSanPham)">Xóa</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-fixed">
      <div class="d-flex justify-content-center align-items-center gap-3">
        <el-pagination background layout="prev, pager, next" :page-size="pageSize" :current-page="currentPage"
          :total="totalItems" :pager-count="7" prev-text="< Trước" next-text="Sau >"
          @current-change="handlePageChange" />
      </div>
    </div>

    <!-- Dialog xem chi tiết -->
    <el-dialog title="Chi tiết model" v-model="detailDialogVisible" width="50%">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="Mã model">{{ detailModel.maModelSanPham || 'Không rõ' }}</el-descriptions-item>
        <el-descriptions-item label="Tên model">{{ detailModel.tenModel || 'Không rõ' }}</el-descriptions-item>
        <el-descriptions-item label="Loại">{{ loaiLabel(detailModel.idLoai) }}</el-descriptions-item>
        <el-descriptions-item label="RAM">{{ ramLabel(detailModel.idRam) }}</el-descriptions-item>
        <el-descriptions-item label="CPU">{{ cpuLabel(detailModel.idCpu) }}</el-descriptions-item>
        <el-descriptions-item label="Màn hình">{{ manHinhLabel(detailModel.idManHinh) }}</el-descriptions-item>
        <el-descriptions-item label="Hệ điều hành">{{ heDieuHanhLabel(detailModel.idHeDieuHanh)
        }}</el-descriptions-item>
        <el-descriptions-item label="Pin">{{ pinLabel(detailModel.idPin) }}</el-descriptions-item>
        <el-descriptions-item label="Camera trước">{{ cameraTruocLabel(detailModel.idCameraTruoc)
        }}</el-descriptions-item>
        <el-descriptions-item label="Camera sau">{{ cameraSauLabel(detailModel.idCameraSau) }}</el-descriptions-item>
        <el-descriptions-item label="Xuất xứ">{{ xuatXuLabel(detailModel.idXuatXu) }}</el-descriptions-item>
        <el-descriptions-item label="Ngày ra mắt">{{ formatNamRaMat(detailModel.namRaMat) }}</el-descriptions-item>
        <el-descriptions-item label="Mô tả" :span="2">{{ detailModel.moTa || 'Không có mô tả' }}</el-descriptions-item>
        <el-descriptions-item label="Trạng thái">{{ detailModel.trangThai || 'Không rõ' }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="detailDialogVisible = false">Đóng</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- Dialog thêm/sửa model -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="50%" :before-close="handleCloseDialog">
      <el-form :model="modelForm" ref="modelFormRef" label-width="120px" :rules="modelRules">
        <el-form-item label="Tên model" prop="tenModel">
          <el-input v-model="modelForm.tenModel" />
        </el-form-item>
        <el-form-item label="RAM" prop="idRam">
          <el-select v-model="modelForm.idRam" placeholder="Chọn RAM">
            <el-option v-for="ram in rams" :key="ram.idRam" :label="ram.dungLuongRam" :value="ram.idRam" />
          </el-select>
        </el-form-item>
        <el-form-item label="CPU" prop="idCpu">
          <el-select v-model="modelForm.idCpu" placeholder="Chọn CPU">
            <el-option v-for="cpu in cpus" :key="cpu.idCpu" :label="cpu.chipXuLy" :value="cpu.idCpu" />
          </el-select>
        </el-form-item>
        <el-form-item label="Màn hình" prop="idManHinh">
          <el-select v-model="modelForm.idManHinh" placeholder="Chọn màn hình">
            <el-option v-for="mh in manHinhs" :key="mh.idManHinh" :label="mh.kichThuoc" :value="mh.idManHinh" />
          </el-select>
        </el-form-item>
        <el-form-item label="Hệ điều hành" prop="idHeDieuHanh">
          <el-select v-model="modelForm.idHeDieuHanh" placeholder="Chọn hệ điều hành">
            <el-option v-for="hdh in heDieuHanhs" :key="hdh.idHeDieuHanh" :label="hdh.phienBan"
              :value="hdh.idHeDieuHanh" />
          </el-select>
        </el-form-item>
        <el-form-item label="Pin" prop="idPin">
          <el-select v-model="modelForm.idPin" placeholder="Chọn pin">
            <el-option v-for="pin in pins" :key="pin.idPin" :label="pin.phienBan" :value="pin.idPin" />
          </el-select>
        </el-form-item>
        <el-form-item label="Camera trước" prop="idCameraTruoc">
          <el-select v-model="modelForm.idCameraTruoc" placeholder="Chọn camera trước">
            <el-option v-for="cam in cameraTruocs" :key="cam.idCamera" :label="cam.doPhanGiai" :value="cam.idCamera" />
          </el-select>
        </el-form-item>
        <el-form-item label="Camera sau" prop="idCameraSau">
          <el-select v-model="modelForm.idCameraSau" placeholder="Chọn camera sau">
            <el-option v-for="cam in cameraSaus" :key="cam.idCamera" :label="cam.doPhanGiai" :value="cam.idCamera" />
          </el-select>
        </el-form-item>
        <el-form-item label="Xuất xứ" prop="idXuatXu">
          <el-select v-model="modelForm.idXuatXu" placeholder="Chọn xuất xứ">
            <el-option v-for="xx in xuatXus" :key="xx.idXuatXu" :label="xx.maXuatXu" :value="xx.idXuatXu" />
          </el-select>
        </el-form-item>
        <el-form-item label="Loại" prop="idLoai">
          <el-select v-model="modelForm.idLoai" placeholder="Chọn loại">
            <el-option v-for="loai in loais" :key="loai.idLoai" :label="loai.tenLoai" :value="loai.idLoai" />
          </el-select>
        </el-form-item>
        <el-form-item label="Ngày ra mắt" prop="namRaMat">
          <el-date-picker v-model="modelForm.namRaMat" type="date" value-format="YYYY-MM-DD" format="DD/MM/YYYY"
            placeholder="Chọn ngày ra mắt" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="Mô tả">
          <el-input type="textarea" v-model="modelForm.moTa" />
        </el-form-item>
        <el-form-item label="Trạng thái" prop="trangThai">
          <el-select v-model="modelForm.trangThai" placeholder="Chọn trạng thái">
            <el-option label="Active" value="ACTIVE" />
            <el-option label="Out of Stock" value="OUT_OF_STOCK" />
            <el-option label="Inactive" value="INACTIVE" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">Hủy</el-button>
          <el-button type="primary" @click="submitModelForm" :loading="submitting"
            :disabled="!canSubmitForm">Lưu</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { computed, onMounted, reactive, ref, watch } from 'vue';
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
  getAllPageModelSanPham,
  putModelSanPham,
  postModelSanPham,
  deleteModelSanPham,
  finByIdModelSanPham
} from '../../../Service/Adminservice/Products/ProductAdminService';

export default {
  name: 'ManageModelSanPham',
  setup() {
    const modelSanPhams = reactive([]);
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
    const dialogVisible = ref(false);
    const dialogTitle = ref('Thêm model mới');
    const modelFormRef = ref(null);
    const currentPage = ref(1);
    const totalPages = ref(1);
    const totalItems = ref(0);
    const pageSize = 5;
    const searchQuery = ref('');
    const filterStatus = ref('');
    const detailDialogVisible = ref(false);
    const detailModel = ref({});
    const baseModelName = ref('');

    const modelForm = ref({
      idModelSanPham: null,
      maModelSanPham: '',
      tenModel: '',
      idRam: null,
      idCpu: null,
      idManHinh: null,
      idHeDieuHanh: null,
      idPin: null,
      idCameraTruoc: null,
      idCameraSau: null,
      idXuatXu: null,
      idLoai: null,
      namRaMat: null,
      moTa: '',
      trangThai: 'ACTIVE'
    });

    const modelRules = reactive({
      tenModel: [{ required: true, message: 'Vui lòng nhập tên model', trigger: 'blur' }],
      idRam: [{ required: true, message: 'Vui lòng chọn RAM', trigger: 'change' }],
      idCpu: [{ required: true, message: 'Vui lòng chọn CPU', trigger: 'change' }],
      idManHinh: [{ required: true, message: 'Vui lòng chọn màn hình', trigger: 'change' }],
      idHeDieuHanh: [{ required: true, message: 'Vui lòng chọn hệ điều hành', trigger: 'change' }],
      idPin: [{ required: true, message: 'Vui lòng chọn pin', trigger: 'change' }],
      idCameraTruoc: [{ required: true, message: 'Vui lòng chọn camera trước', trigger: 'change' }],
      idCameraSau: [{ required: true, message: 'Vui lòng chọn camera sau', trigger: 'change' }],
      idXuatXu: [{ required: true, message: 'Vui lòng chọn xuất xứ', trigger: 'change' }],
      idLoai: [{ required: true, message: 'Vui lòng chọn loại', trigger: 'change' }],
      namRaMat: [{ required: true, message: 'Vui lòng chọn ngày ra mắt', trigger: 'change' }],
      trangThai: [{ required: true, message: 'Vui lòng chọn trạng thái', trigger: 'change' }],
      moTa: []
    });

    const canSubmitForm = computed(() => {
      return [
        rams.value,
        cpus.value,
        manHinhs.value,
        heDieuHanhs.value,
        pins.value,
        cameraTruocs.value,
        cameraSaus.value,
        xuatXus.value,
        loais.value
      ].every(list => list.length > 0);
    });

    const indexMethod = (index) => {
      return (currentPage.value - 1) * pageSize + index + 1;
    };

    const ramLabel = (idRam) => {
      if (!idRam || !rams.value.length) return 'Không rõ';
      const found = rams.value.find(r => r.idRam === idRam);
      return found?.dungLuongRam || 'Không rõ';
    };

    const cpuLabel = (idCpu) => {
      if (!idCpu || !cpus.value.length) return 'Không rõ';
      const found = cpus.value.find(c => c.idCpu === idCpu);
      return found?.chipXuLy || 'Không rõ';
    };

    const manHinhLabel = (idManHinh) => {
      if (!idManHinh || !manHinhs.value.length) return 'Không rõ';
      const found = manHinhs.value.find(m => m.idManHinh === idManHinh);
      return found?.kichThuoc || 'Không rõ';
    };

    const heDieuHanhLabel = (idHeDieuHanh) => {
      if (!idHeDieuHanh || !heDieuHanhs.value.length) return 'Không rõ';
      const found = heDieuHanhs.value.find(h => h.idHeDieuHanh === idHeDieuHanh);
      return found?.phienBan || 'Không rõ';
    };

    const pinLabel = (idPin) => {
      if (!idPin || !pins.value.length) return 'Không rõ';
      const found = pins.value.find(p => p.idPin === idPin);
      return found?.phienBan || 'Không rõ';
    };

    const cameraTruocLabel = (idCamera) => {
      if (!idCamera || !cameraTruocs.value.length) return 'Không rõ';
      const found = cameraTruocs.value.find(c => c.idCamera === idCamera);
      return found?.doPhanGiai || 'Không rõ';
    };

    const cameraSauLabel = (idCamera) => {
      if (!idCamera || !cameraSaus.value.length) return 'Không rõ';
      const found = cameraSaus.value.find(c => c.idCamera === idCamera);
      return found?.doPhanGiai || 'Không rõ';
    };

    const loaiLabel = (idLoai) => {
      if (!idLoai || !loais.value.length) return 'Không rõ';
      const found = loais.value.find(l => l.idLoai === idLoai);
      return found?.tenLoai || 'Không rõ';
    };

    const xuatXuLabel = (idXuatXu) => {
      if (!idXuatXu || !xuatXus.value.length) return 'Không rõ';
      const found = xuatXus.value.find(x => x.idXuatXu === idXuatXu);
      return found?.maXuatXu || 'Không rõ';
    };

    const formatNamRaMat = (namRaMat) => {
      if (!namRaMat) return 'Không rõ';
      try {
        const date = new Date(namRaMat);
        return `${date.getDate().toString().padStart(2, '0')}/${(date.getMonth() + 1).toString().padStart(2, '0')}/${date.getFullYear()}`;
      } catch {
        return 'Không rõ';
      }
    };

    // Tạo tên model từ tên gốc, loại và xuất xứ
    const generateTenModel = () => {
      const baseName = baseModelName.value?.trim() || '';
      const loai = loais.value.find(l => l.idLoai === modelForm.value.idLoai)?.tenLoai || '';
      const xuatXu = xuatXus.value.find(x => x.idXuatXu === modelForm.value.idXuatXu)?.maXuatXu || '';
      console.log('generateTenModel:', { baseName, loai, xuatXu });
      modelForm.value.tenModel = [baseName, loai, xuatXu].filter(Boolean).join(' ').trim();
      console.log('tenModel updated:', modelForm.value.tenModel);
    };

    // Theo dõi idLoai và idXuatXu để tự động cập nhật tenModel
    watch([() => modelForm.value.idLoai, () => modelForm.value.idXuatXu], () => {
      console.log('idLoai or idXuatXu changed:', {
        idLoai: modelForm.value.idLoai,
        idXuatXu: modelForm.value.idXuatXu
      });
      generateTenModel();
    });

    // Theo dõi tenModel để cập nhật baseModelName khi người dùng nhập/xóa
    watch(() => modelForm.value.tenModel, (newTenModel) => {
      console.log('tenModel changed:', newTenModel);
      // Chỉ lấy phần tên gốc, loại bỏ loai và xuatXu nếu có
      let baseName = newTenModel?.trim() || '';
      const loai = loais.value.find(l => l.idLoai === modelForm.value.idLoai)?.tenLoai || '';
      const xuatXu = xuatXus.value.find(x => x.idXuatXu === modelForm.value.idXuatXu)?.maXuatXu || '';
      if (loai) {
        baseName = baseName.replace(new RegExp(`\\b${loai}\\b`, 'i'), '').trim();
      }
      if (xuatXu) {
        baseName = baseName.replace(new RegExp(`\\b${xuatXu}\\b`, 'i'), '').trim();
      }
      baseModelName.value = baseName;
      console.log('baseModelName updated:', baseModelName.value);
    });

    const handleApiError = (error, defaultMessage) => {
      const status = error.response?.status;
      const messages = {
        400: 'Dữ liệu không hợp lệ, vui lòng kiểm tra lại.',
        404: 'Dữ liệu không tồn tại.',
        500: 'Lỗi máy chủ, vui lòng thử lại sau.'
      };
      ElMessage.error(messages[status] || `${defaultMessage}: ${error.message}`);
    };

    const fetchDanhMuc = async () => {
      loading.value = true;
      try {
        const page = Number(currentPage.value) - 1;
        const size = pageSize;
        const search = searchQuery.value || '';
        const status = filterStatus.value || '';
        const responses = await Promise.all([
          getAllRamList(),
          getAllCpuList(),
          getAllManHinhList(),
          getAllHDHList(),
          getAllPinList(),
          getAllCameraTruocList(),
          getAllCameraSauList(),
          getAllXuatXuList(),
          getAllLoaiList(),
          getAllPageModelSanPham(page, size, search, status)
        ]);

        rams.value = (responses[0] || []).map(ram => ({
          idRam: ram.id,
          dungLuongRam: ram.dungLuong || 'Không rõ'
        }));
        cpus.value = (responses[1] || []).map(cpu => ({
          idCpu: cpu.id,
          chipXuLy: cpu.chipXuLy || 'Không rõ'
        }));
        manHinhs.value = (responses[2] || []).map(mh => ({
          idManHinh: mh.id,
          kichThuoc: mh.kichThuoc || 'Không rõ'
        }));
        heDieuHanhs.value = (responses[3] || []).map(hdh => ({
          idHeDieuHanh: hdh.id,
          phienBan: hdh.phienBan || 'Không rõ'
        }));
        pins.value = (responses[4] || []).map(pin => ({
          idPin: pin.id,
          phienBan: pin.phienBan || 'Không rõ'
        }));
        cameraTruocs.value = (responses[5] || []).map(cam => ({
          idCamera: cam.id,
          doPhanGiai: cam.doPhanGiai || 'Không rõ'
        }));
        cameraSaus.value = (responses[6] || []).map(cam => ({
          idCamera: cam.id,
          doPhanGiai: cam.doPhanGiai || 'Không rõ'
        }));
        xuatXus.value = (responses[7] || []).map(xx => ({
          idXuatXu: xx.id,
          maXuatXu: xx.maXuatXu || 'Không rõ'
        }));
        loais.value = (responses[8] || []).map(loai => ({
          idLoai: loai.id,
          tenLoai: loai.tenLoai || 'Không rõ'
        }));
        modelSanPhams.length = 0;
        modelSanPhams.push(...(responses[9].content || []));
        totalItems.value = responses[9].totalElements || 0;
        totalPages.value = responses[9].totalPages || 1;

        console.log('Dữ liệu sau khi gán:', {
          modelSanPhams: modelSanPhams,
          rams: rams.value,
          cpus: cpus.value,
          manHinhs: manHinhs.value,
          heDieuHanhs: heDieuHanhs.value,
          pins: pins.value,
          cameraTruocs: cameraTruocs.value,
          cameraSaus: cameraSaus.value,
          xuatXus: xuatXus.value,
          loais: loais.value,
          totalItems: totalItems.value,
          totalPages: totalPages.value
        });

        if (!canSubmitForm.value) {
          ElMessage.warning('Một số danh mục rỗng, vui lòng kiểm tra dữ liệu.');
        }
      } catch (error) {
        handleApiError(error, 'Lỗi khi lấy danh mục');
      } finally {
        loading.value = false;
      }
    };

    const openAddDialog = async () => {
      dialogTitle.value = 'Thêm model mới';
      modelFormRef.value?.resetFields();
      modelForm.value = {
        idModelSanPham: null,
        maModelSanPham: '',
        tenModel: '',
        idRam: null,
        idCpu: null,
        idManHinh: null,
        idHeDieuHanh: null,
        idPin: null,
        idCameraTruoc: null,
        idCameraSau: null,
        idXuatXu: null,
        idLoai: null,
        namRaMat: null,
        moTa: '',
        trangThai: 'ACTIVE'
      };
      baseModelName.value = ''; // Reset baseModelName
      dialogVisible.value = true;
    };

    const openEditDialog = async (model) => {
      const response = await finByIdModelSanPham(model.idModelSanPham);
      dialogTitle.value = 'Sửa model';
      modelForm.value = {
        ...model,
        idCpu: response.idCpu || null,
        idManHinh: response.idManHinh || null,
        idHeDieuHanh: response.idHeDieuHanh || null,
        idPin: response.idPin || null,
        idCameraTruoc: response.idCameraTruoc || null,
        idCameraSau: response.idCameraSau || null,
        moTa: response.moTa || 'Không có mô tả'
      };
      dialogVisible.value = true;
    };

    const openDetailDialog = async (model) => {
      const response = await finByIdModelSanPham(model.idModelSanPham);
      console.log("Dữ liệu chi tiết model sản phẩm: ", response)
      console.log('Model trong openDetailDialog:', model);
      detailModel.value = {
        ...model,
        idCpu: response.idCpu || null,
        idManHinh: response.idManHinh || null,
        idHeDieuHanh: response.idHeDieuHanh || null,
        idPin: response.idPin || null,
        idCameraTruoc: response.idCameraTruoc || null,
        idCameraSau: response.idCameraSau || null,
        moTa: response.moTa || 'Không có mô tả'
      };
      // Giả sử tenModel đã bao gồm loai và xuatXu, lấy phần đầu làm baseModelName
      baseModelName.value = modelForm.value.tenModel.split(' ')[0] || modelForm.value.tenModel;
      generateTenModel(); // Cập nhật lại tenModel
      console.log('detailModel sau khi gán:', detailModel.value);
      detailDialogVisible.value = true;
    };

    const submitModelForm = () => {
      modelFormRef.value?.validate(async (valid) => {
        if (!valid) {
          ElMessage.error('Vui lòng điền đầy đủ thông tin.');
          return;
        }
        submitting.value = true;
        try {
          const payload = {
            tenModel: modelForm.value.tenModel,
            idRam: modelForm.value.idRam,
            idCpu: modelForm.value.idCpu,
            idManHinh: modelForm.value.idManHinh,
            idHeDieuHanh: modelForm.value.idHeDieuHanh,
            idPin: modelForm.value.idPin,
            idCameraTruoc: modelForm.value.idCameraTruoc,
            idCameraSau: modelForm.value.idCameraSau,
            idXuatXu: modelForm.value.idXuatXu,
            idLoai: modelForm.value.idLoai,
            namRaMat: modelForm.value.namRaMat || null,
            moTa: modelForm.value.moTa,
            trangThai: modelForm.value.trangThai
          };
          if (modelForm.value.idModelSanPham) {
            await putModelSanPham(modelForm.value.idModelSanPham, payload);
            ElMessage.success('Cập nhật model thành công!');
          } else {
            await postModelSanPham(payload);
            ElMessage.success('Thêm model thành công!');
          }
          dialogVisible.value = false;
          await fetchDanhMuc();
        } catch (error) {
          handleApiError(error, 'Lỗi khi lưu model');
        } finally {
          submitting.value = false;
        }
      });
    };

    const deleteModel = (id) => {
      ElMessageBox.confirm('Bạn có chắc chắn muốn xóa model này?', 'Xác nhận', {
        confirmButtonText: 'Có',
        cancelButtonText: 'Không',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteModelSanPham(id);
          ElMessage.success('Xóa model thành công!');
          await fetchDanhMuc();
        } catch (error) {
          handleApiError(error, 'Lỗi khi xóa model');
        }
      });
    };

    const handleCloseDialog = (done) => {
      ElMessageBox.confirm('Bạn có chắc muốn hủy? Các thay đổi sẽ không được lưu.', 'Xác nhận', {
        confirmButtonText: 'Có',
        cancelButtonText: 'Không',
        type: 'warning'
      }).then(() => {
        done();
      });
    };

    const handlePageChange = async (newPage) => {
      currentPage.value = newPage;
      await fetchDanhMuc();
    };

    watch([searchQuery, filterStatus], debounce(() => {
      currentPage.value = 1;
      fetchDanhMuc();
    }, 500));

    onMounted(fetchDanhMuc);

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
      dialogVisible,
      dialogTitle,
      modelForm,
      modelFormRef,
      modelRules,
      currentPage,
      totalPages,
      totalItems,
      pageSize,
      searchQuery,
      filterStatus,
      detailDialogVisible,
      detailModel,
      canSubmitForm,
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
      formatNamRaMat,
      fetchDanhMuc,
      openAddDialog,
      openEditDialog,
      openDetailDialog,
      submitModelForm,
      deleteModel,
      handleCloseDialog,
      handlePageChange
    };
  }
};
</script>

<style scoped>
.container {
  padding: 24px;
}

.filter-section {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
}

.el-table {
  width: 100%;
  overflow-x: auto;
  margin-top: 20px;
}

.el-button+.el-button {
  margin-left: 10px;
}

.el-form-item {
  margin-bottom: 18px;
}

.dialog-footer {
  text-align: right;
}

.action-buttons-horizontal {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  flex-wrap: nowrap;
}

:deep(.el-table td .el-button) {
  width: 32px;
  height: 32px;
  padding: 0;
  border-radius: 6px;
  flex-shrink: 0;
}

:deep(.el-descriptions__label) {
  font-weight: 500;
  background: #f9fafb;
}

:deep(.el-descriptions__content) {
  background: white;
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

:deep(.el-alert) {
  border-radius: 8px;
  margin-top: 24px;
}

@media (max-width: 768px) {
  .container {
    padding: 16px;
  }

  h2 {
    font-size: 20px;
  }

  .filter-section {
    flex-direction: column;
    align-items: stretch;
  }

  .filter-section .el-input,
  .filter-section .el-select,
  .filter-section .el-button {
    width: 100%;
  }

  .el-table {
    display: block;
  }

  :deep(.el-table td) {
    padding: 8px 12px;
    font-size: 13px;
  }

  .action-buttons-horizontal {
    gap: 4px;
  }

  :deep(.el-table td .el-button) {
    width: 28px;
    height: 28px;
  }

  .el-dialog {
    width: 90%;
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