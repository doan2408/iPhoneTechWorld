<template>
  <div class="container mt-4">
    <h2>Cập nhật sản phẩm</h2>
    <el-form :model="sanPhamModel" ref="sanPhamForm" label-width="120px" :rules="rules">
      <!-- Thông tin sản phẩm chính -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Tên sản phẩm" prop="tenSanPham">
            <el-input v-model="sanPhamModel.tenSanPham" placeholder="Nhập tên sản phẩm"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Thương hiệu" prop="thuongHieu">
            <el-input v-model="sanPhamModel.thuongHieu" placeholder="Nhập thương hiệu"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Nhà cung cấp" prop="idNhaCungCap">
            <el-select v-model="sanPhamModel.idNhaCungCap" placeholder="Chọn nhà cung cấp" style="width: 100%">
              <el-option v-for="ncc in nhaCungCaps" :key="ncc.id" :label="ncc.tenNhaCungCap"
                :value="ncc.id"></el-option>
            </el-select>
          </el-form-item>
          
        </el-col>
        <el-col :span="12">
          <el-form-item label="Trạng thái" prop="trangThaiSanPham">
            <el-select v-model="sanPhamModel.trangThaiSanPham" placeholder="Chọn trạng thái" style="width: 100%">
              <el-option v-for="tt in danhSachTrangThaiSanPham" :key="tt.value" :label="tt.label"
                :value="tt.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Danh sách sản phẩm chi tiết -->
      <h3>Danh sách biến thể sản phẩm</h3>
      <el-table :data="sanPhamModel.sanPhamChiTiets" style="width: 100%" border @row-click="selectChiTiet">
        <el-table-column type="index" label="STT" width="80" :index="indexMethod" />
        <el-table-column label="Màu sắc" prop="idMau">
          <template #default="{ row }">
            {{ getMauSacLabel(row.idMau) }}
          </template>
        </el-table-column>
        <!-- <el-table-column label="RAM" prop="idRam">
          <template #default="{ row }">
            {{ getRamLabel(row.idRam) }}
          </template>
        </el-table-column> -->
        <el-table-column label="ROM" prop="idRom">
          <template #default="{ row }">
            {{ getRomLabel(row.idRom) }}
          </template>
        </el-table-column>
        <el-table-column prop="soLuong" label="Số lượng" />
        <el-table-column prop="giaBan" label="Giá bán" />
        <el-table-column label="Hành động" width="100">
          <template #default="{ $index }">
            <el-button type="danger" size="small" :icon="Delete" circle @click.stop="removeChiTiet($index)" />
          </template>
        </el-table-column>
      </el-table>

      <!-- Form chỉnh sửa chi tiết sản phẩm được chọn -->
      <div v-if="selectedChiTiet !== null" class="mt-4">
        <h4>Chỉnh sửa biến thể {{ selectedChiTiet + 1 }}</h4>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Màu sắc" :prop="`sanPhamChiTiets.${selectedChiTiet}.idMau`"
              :rules="[{ required: true, message: 'Vui lòng chọn màu sắc' }]">
              <el-select v-model="sanPhamModel.sanPhamChiTiets[selectedChiTiet].idMau" placeholder="Chọn màu sắc"
                style="width: 100%">
                <el-option v-for="mau in maus" :key="mau.id" :label="mau.tenMau" :value="mau.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="8">
            <el-form-item label="RAM" :prop="`sanPhamChiTiets.${selectedChiTiet}.idRam`"
              :rules="[{ required: true, message: 'Vui lòng chọn RAM' }]">
              <el-select v-model="sanPhamModel.sanPhamChiTiets[selectedChiTiet].idRam" placeholder="Chọn RAM"
                style="width: 100%">
                <el-option v-for="ram in rams" :key="ram.id" :label="ram.dungLuong" :value="ram.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col> -->
          <el-col :span="8">
            <el-form-item label="ROM" :prop="`sanPhamChiTiets.${selectedChiTiet}.idRom`"
              :rules="[{ required: true, message: 'Vui lòng chọn ROM' }]">
              <el-select v-model="sanPhamModel.sanPhamChiTiets[selectedChiTiet].idRom" placeholder="Chọn ROM"
                style="width: 100%">
                <el-option v-for="rom in roms" :key="rom.id" :label="rom.dungLuong" :value="rom.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <!-- <el-col :span="8">
            <el-form-item label="Màn hình" :prop="`sanPhamChiTiets.${selectedChiTiet}.idManHinh`"
              :rules="[{ required: true, message: 'Vui lòng chọn màn hình' }]">
              <el-select v-model="sanPhamModel.sanPhamChiTiets[selectedChiTiet].idManHinh" placeholder="Chọn màn hình"
                style="width: 100%">
                <el-option v-for="mh in manHinhs" :key="mh.id" :label="mh.kichThuoc" :value="mh.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Hệ điều hành" :prop="`sanPhamChiTiets.${selectedChiTiet}.idHeDieuHanh`"
              :rules="[{ required: true, message: 'Vui lòng chọn hệ điều hành' }]">
              <el-select v-model="sanPhamModel.sanPhamChiTiets[selectedChiTiet].idHeDieuHanh"
                placeholder="Chọn hệ điều hành" style="width: 100%">
                <el-option v-for="hdh in heDieuHanhs" :key="hdh.id" :label="hdh.phienBan" :value="hdh.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Pin" :prop="`sanPhamChiTiets.${selectedChiTiet}.idPin`"
              :rules="[{ required: true, message: 'Vui lòng chọn pin' }]">
              <el-select v-model="sanPhamModel.sanPhamChiTiets[selectedChiTiet].idPin" placeholder="Chọn pin"
                style="width: 100%">
                <el-option v-for="pin in pins" :key="pin.id" :label="pin.congSuatSac" :value="pin.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="CPU" :prop="`sanPhamChiTiets.${selectedChiTiet}.idCpu`"
              :rules="[{ required: true, message: 'Vui lòng chọn CPU' }]">
              <el-select v-model="sanPhamModel.sanPhamChiTiets[selectedChiTiet].idCpu" placeholder="Chọn CPU"
                style="width: 100%">
                <el-option v-for="cpu in cpus" :key="cpu.id" :label="cpu.xungNhip" :value="cpu.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Camera trước" :prop="`sanPhamChiTiets.${selectedChiTiet}.idCameraTruoc`"
              :rules="[{ required: true, message: 'Vui lòng chọn camera trước' }]">
              <el-select v-model="sanPhamModel.sanPhamChiTiets[selectedChiTiet].idCameraTruoc"
                placeholder="Chọn camera trước" style="width: 100%">
                <el-option v-for="cam in cameraTruocs" :key="cam.id" :label="cam.doPhanGiai"
                  :value="cam.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Camera sau" :prop="`sanPhamChiTiets.${selectedChiTiet}.idCameraSau`"
              :rules="[{ required: true, message: 'Vui lòng chọn camera sau' }]">
              <el-select v-model="sanPhamModel.sanPhamChiTiets[selectedChiTiet].idCameraSau"
                placeholder="Chọn camera sau" style="width: 100%">
                <el-option v-for="cam in cameraSaus" :key="cam.id" :label="cam.doPhanGiai" :value="cam.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Xuất xứ" :prop="`sanPhamChiTiets.${selectedChiTiet}.idXuatXu`"
              :rules="[{ required: true, message: 'Vui lòng chọn xuất xứ' }]">
              <el-select v-model="sanPhamModel.sanPhamChiTiets[selectedChiTiet].idXuatXu" placeholder="Chọn xuất xứ"
                style="width: 100%">
                <el-option v-for="xx in xuatXus" :key="xx.id" :label="xx.maXuatXu" :value="xx.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Loại" :prop="`sanPhamChiTiets.${selectedChiTiet}.idLoai`"
              :rules="[{ required: true, message: 'Vui lòng chọn loại' }]">
              <el-select v-model="sanPhamModel.sanPhamChiTiets[selectedChiTiet].idLoai" placeholder="Chọn loại"
                style="width: 100%">
                <el-option v-for="loai in loais" :key="loai.id" :label="loai.tenLoai" :value="loai.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col> -->
          <el-col :span="8">
            <el-form-item label="Giá bán" :prop="`sanPhamChiTiets.${selectedChiTiet}.giaBan`"
              :rules="[{ required: true, message: 'Vui lòng nhập giá bán', type: 'number' }]">
              <el-input-number v-model="sanPhamModel.sanPhamChiTiets[selectedChiTiet].giaBan" :min="0" :precision="2"
                style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="IMEI" :prop="`sanPhamChiTiets.${selectedChiTiet}.imeisInput`"
          :rules="[{ required: true, message: 'Vui lòng nhập IMEI' }]">
          <el-input type="textarea" v-model="sanPhamModel.sanPhamChiTiets[selectedChiTiet].imeisInput"
            placeholder="Nhập danh sách IMEI, phân tách bởi dấu phẩy"
            @input="capNhatSoLuong(selectedChiTiet)"></el-input>
          <el-upload :auto-upload="false" :on-change="(file) => handleImeiFileChange(file, selectedChiTiet)"
            accept=".txt,.csv" style="margin-top: 8px;">
            <el-button type="primary">Tải lên file IMEI</el-button>
          </el-upload>
          <div style="margin-top: 8px;">
            Số lượng IMEI hợp lệ: {{ sanPhamModel.sanPhamChiTiets[selectedChiTiet].soLuong }}
            <span v-if="sanPhamModel.sanPhamChiTiets[selectedChiTiet].invalidImeis?.length" style="color: red;">
              ({{ sanPhamModel.sanPhamChiTiets[selectedChiTiet].invalidImeis.length }} IMEI không hợp lệ: {{
                sanPhamModel.sanPhamChiTiets[selectedChiTiet].invalidImeis.join(', ') }})
            </span>
          </div>
        </el-form-item>

        <el-form-item label="Hình ảnh">
          <el-upload :file-list="sanPhamModel.sanPhamChiTiets[selectedChiTiet].hinhAnhs"
            :on-change="(file, fileList) => handleFileChange(file, fileList, selectedChiTiet)"
            :on-remove="(file, fileList) => handleFileRemove(file, fileList, selectedChiTiet)" :auto-upload="false"
            accept="image/*" list-type="picture" :limit="5"
            :on-exceed="() => ElMessage.warning('Chỉ được tải lên tối đa 5 ảnh!')">
            <el-button type="primary">Tải lên hình ảnh</el-button>
          </el-upload>
        </el-form-item>
      </div>
      <div v-else class="mt-4">
        <el-alert title="Vui lòng chọn một biến thể để chỉnh sửa" type="info" show-icon />
      </div>

      <!-- Nút hành động -->
      <el-row :gutter="20" class="mt-4">
        <el-col :span="24" class="text-center">
          <el-button type="success" @click="submitForm">Cập nhật sản phẩm</el-button>
          <el-button type="default" @click="$router.push('/admin/products')">Hủy</el-button>
        </el-col>
      </el-row>
    </el-form>

    <el-alert v-if="error" :title="error" type="error" show-icon class="mt-3" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { Delete } from '@element-plus/icons-vue';
import { getSanPhamById, getAllNhaCungCapList, getAllMauSacList, getAllRamList, getAllRomList, getAllManHinhList, getAllHDHList, getAllPinList, getAllCpuList, getAllCameraTruocList, getAllCameraSauList, getAllXuatXuList, getAllLoaiList, putDataSanPham } from '@/Service/Adminservice/Products/ProductAdminService';
import axios from 'axios';
import { debounce } from 'lodash';

const route = useRoute();
const id = route.params.id;
const router = useRouter();
const sanPhamModel = reactive({
  tenSanPham: '',
  thuongHieu: '',
  idNhaCungCap: null,
  trangThaiSanPham: '',
  sanPhamChiTiets: []
});
const sanPhamForm = ref(null);
const error = ref('');
const selectedChiTiet = ref(null);
const nhaCungCaps = ref([]);
const maus = ref([]);
// const rams = ref([]);
const roms = ref([]);
// const manHinhs = ref([]);
// const heDieuHanhs = ref([]);
// const pins = ref([]);
// const cpus = ref([]);
// const cameraTruocs = ref([]);
// const cameraSaus = ref([]);
// const xuatXus = ref([]);
// const loais = ref([]);

const danhSachTrangThaiSanPham = [
  { label: 'Đang kinh doanh', value: 'ACTIVE' },
  { label: 'Ngừng kinh doanh', value: 'DISCONTINUED' },
  { label: 'Sắp ra mắt', value: 'COMING_SOON' },
  { label: 'Tạm ngừng bán', value: 'TEMPORARILY_UNAVAILABLE' },
  { label: 'Hết hàng', value: 'OUT_OF_STOCK' }
];

const rules = {
  tenSanPham: [{ required: true, message: 'Vui lòng nhập tên sản phẩm' }],
  thuongHieu: [{ required: true, message: 'Vui lòng nhập thương hiệu' }],
  idNhaCungCap: [{ required: true, message: 'Vui lòng chọn nhà cung cấp' }],
  trangThaiSanPham: [{ required: true, message: 'Vui lòng chọn trạng thái' }]
};

const fetchSanPham = async (id) => {
  try {
    const response = await getSanPhamById(id);
    sanPhamModel.id = response.id;
    sanPhamModel.tenSanPham = response.tenSanPham;
    sanPhamModel.thuongHieu = response.thuongHieu;
    sanPhamModel.idNhaCungCap = response.idNhaCungCap;
    sanPhamModel.trangThaiSanPham = response.trangThaiSanPham;
    sanPhamModel.sanPhamChiTiets = response.sanPhamChiTiets.map(chiTiet => ({
      id: chiTiet.id,
      idMau: chiTiet.idMau,
      // idRam: chiTiet.idRam,
      idRom: chiTiet.idRom,
      // idManHinh: chiTiet.idManHinh,
      // idHeDieuHanh: chiTiet.idHeDieuHanh,
      // idPin: chiTiet.idPin,
      // idCpu: chiTiet.idCpu,
      // idCameraTruoc: chiTiet.idCameraTruoc,
      // idCameraSau: chiTiet.idCameraSau,
      // idXuatXu: chiTiet.idXuatXu,
      // idLoai: chiTiet.idLoai,
      soLuong: chiTiet.soLuong,
      giaBan: chiTiet.giaBan,
      imeisInput: chiTiet.imeis?.map(i => i.soImei).join(', ') || '',
      invalidImeis: [],
      hinhAnhs: chiTiet.hinhAnhs?.map(h => ({
        name: h.url.split('/').pop(),
        url: h.url,
        imagePublicId: h.imagePublicId
      })) || []

    }));
    console.log("dữ liệu detail: ", response);

    selectedChiTiet.value = sanPhamModel.sanPhamChiTiets.length > 0 ? 0 : null;
  } catch (err) {
    error.value = err.message || 'Lỗi khi tải chi tiết sản phẩm';
    ElMessage.error(error.value);
  }
};

const fetchDanhMuc = async () => {
  try {
    const responses = await Promise.all([
      getAllNhaCungCapList(),
      getAllMauSacList(),
      // getAllRamList(),
      getAllRomList(),
      // getAllManHinhList(),
      // getAllHDHList(),
      // getAllPinList(),
      // getAllCpuList(),
      // getAllCameraTruocList(),
      // getAllCameraSauList(),
      // getAllXuatXuList(),
      // getAllLoaiList()
    ]);
    nhaCungCaps.value = responses[0];
    maus.value = responses[1];
    // rams.value = responses[2];
    roms.value = responses[2];
    // manHinhs.value = responses[4];
    // heDieuHanhs.value = responses[5];
    // pins.value = responses[6];
    // cpus.value = responses[7];
    // cameraTruocs.value = responses[8];
    // cameraSaus.value = responses[9];
    // xuatXus.value = responses[10];
    // loais.value = responses[11];
  } catch (err) {
    error.value = err.message || 'Lỗi khi tải danh mục';
    ElMessage.error(error.value);
  }
};

const validateIMEI = (imei) => /^\d{15}$/.test(imei);

const capNhatSoLuong = debounce((index) => {
  const imeis = sanPhamModel.sanPhamChiTiets[index].imeisInput
    .split(',')
    .map(i => i.trim())
    .filter(i => i);
  const validImeis = imeis.filter(validateIMEI);
  sanPhamModel.sanPhamChiTiets[index].soLuong = validImeis.length;
  sanPhamModel.sanPhamChiTiets[index].invalidImeis = imeis.filter(i => !validateIMEI(i));
}, 300);

const handleImeiFileChange = (file, index) => {
  try {
    if (file.raw.size > 1024 * 1024) {
      throw new Error('File quá lớn, vui lòng chọn file dưới 1MB');
    }
    const reader = new FileReader();
    reader.onload = (e) => {
      const content = e.target.result;
      const imeis = content
        .split(/[\n,;\s]+/)
        .map(i => i.trim())
        .filter(i => i);
      sanPhamModel.sanPhamChiTiets[index].imeisInput = imeis.join(', ');
      capNhatSoLuong(index);
      ElMessage.success(`Đã tải lên ${imeis.length} IMEI từ file ${file.name}`);
    };
    reader.onerror = () => {
      throw new Error('Lỗi khi đọc file');
    };
    reader.readAsText(file.raw);
  } catch (err) {
    ElMessage.error('Lỗi khi xử lý file IMEI: ' + err.message);
  }
};

const handleFileChange = async (file, fileList, index) => {
  try {
    const formData = new FormData();
    formData.append('file', file.raw);
    const response = await axios.post('http://localhost:8080/admin/hinhAnh/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    sanPhamModel.sanPhamChiTiets[index].hinhAnhs.push({
      name: file.name,
      url: response.data.url,
      imagePublicId: response.data.imagePublicId
    });
    ElMessage.success(`Tải ảnh ${file.name} thành công!`);
  } catch (err) {
    ElMessage.error('Lỗi khi tải ảnh: ' + (err.response?.data?.message || err.message));
  }
};

const handleFileRemove = (file, fileList, index) => {
  sanPhamModel.sanPhamChiTiets[index].hinhAnhs = fileList.filter(f => f.url);
  ElMessage.success(`Đã xóa ảnh ${file.name}`);
};

const selectChiTiet = (row, column, event) => {
  const index = sanPhamModel.sanPhamChiTiets.indexOf(row);
  selectedChiTiet.value = index;
};

const removeChiTiet = (index) => {
  sanPhamModel.sanPhamChiTiets.splice(index, 1);
  if (selectedChiTiet.value === index) {
    selectedChiTiet.value = sanPhamModel.sanPhamChiTiets.length > 0 ? 0 : null;
  } else if (selectedChiTiet.value > index) {
    selectedChiTiet.value--;
  }
};

const getMauSacLabel = (idMau) => maus.value.find(m => m.id === idMau)?.tenMau || 'Không rõ';
// const getRamLabel = (idRam) => rams.value.find(r => r.id === idRam)?.dungLuong || 'Không rõ';
const getRomLabel = (idRom) => roms.value.find(r => r.id === idRom)?.dungLuong || 'Không rõ';

const indexMethod = (index) => index + 1;

const submitForm = async () => {
  try {
    await sanPhamForm.value.validate();
    const payload = {
      id: sanPhamModel.id,
      tenSanPham: sanPhamModel.tenSanPham,
      thuongHieu: sanPhamModel.thuongHieu,
      idNhaCungCap: sanPhamModel.idNhaCungCap,
      trangThaiSanPham: sanPhamModel.trangThaiSanPham,
      sanPhamChiTiets: sanPhamModel.sanPhamChiTiets.map(chiTiet => {
        const imeis = chiTiet.imeisInput
          .split(',')
          .map(i => i.trim())
          .filter(i => i);
        return {
          id: chiTiet.id || null,
          idMau: chiTiet.idMau,
          // idRam: chiTiet.idRam,
          idRom: chiTiet.idRom,
          // idManHinh: chiTiet.idManHinh,
          // idHeDieuHanh: chiTiet.idHeDieuHanh,
          // idPin: chiTiet.idPin,
          // idCpu: chiTiet.idCpu,
          // idCameraTruoc: chiTiet.idCameraTruoc,
          // idCameraSau: chiTiet.idCameraSau,
          // idXuatXu: chiTiet.idXuatXu,
          // idLoai: chiTiet.idLoai,
          soLuong: chiTiet.soLuong,
          giaBan: chiTiet.giaBan,
          imeis: imeis.map(i => ({ soImei: i })),
          hinhAnhs: chiTiet.hinhAnhs.map(h => ({
            url: h.url,
            imagePublicId: h.imagePublicId
          }))
        };
      })
    };
    console.log('Route params:', route.params);
    console.log('Route params id:', id);

    await putDataSanPham(id, payload);
    ElMessage.success('Cập nhật sản phẩm thành công!');
    router.push('/admin/products');
  } catch (err) {
    error.value = err.message || 'Lỗi khi cập nhật sản phẩm';
    ElMessage.error(error.value);
  }
};

onMounted(async () => {
  await Promise.all([fetchDanhMuc(), fetchSanPham(route.params.id)]);
});
</script>

<style scoped>

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

h2 {
  font-size: 24px;
  font-weight: 600;
  margin: 0 0 24px 0;
  color: #1f2937;
  text-align: center;
  padding-bottom: 12px;
  border-bottom: 2px solid #e5e7eb;
}

h3 {
  font-size: 20px;
  font-weight: 600;
  margin: 24px 0 16px;
  color: #1f2937;
}

h4 {
  font-size: 18px;
  font-weight: 500;
  margin: 16px 0 12px;
  color: #374151;
}

.el-table {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border: 1px solid #e5e7eb;
}

.el-table__header th {
  background: #f9fafb;
  color: #374151;
  font-weight: 600;
  font-size: 14px;
}

.el-table td {
  padding: 12px 16px;
  font-size: 14px;
  color: #374151;
}

.el-button {
  border-radius: 6px;
  font-weight: 500;
}

.el-button--success {
  background: #10b981;
  border-color: #10b981;
  color: white;
}

.el-button--success:hover {
  background: #059669;
  border-color: #059669;
}

.el-button--danger {
  background: #ef4444;
  border-color: #ef4444;
  color: white;
}

.el-button--danger:hover {
  background: #dc2626;
  border-color: #dc2626;
}

.el-alert {
  border-radius: 8px;
  margin-top: 24px;
}

.text-center {
  text-align: center;
}

@media (max-width: 768px) {
  .container {
    padding: 16px;
  }

  h2 {
    font-size: 20px;
  }

  h3 {
    font-size: 18px;
  }

  h4 {
    font-size: 16px;
  }

  .el-table td {
    padding: 8px 12px;
    font-size: 13px;
  }
}
</style>