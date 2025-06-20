<template>
  <div class="container mt-4">
    <h2>Chi tiết sản phẩm</h2>
    <el-form :model="sanPhamModel" label-width="150px">
      <!-- Thông tin sản phẩm chính -->
      <h3>Thông tin chung</h3>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Tên sản phẩm">
            <el-input v-model="sanPhamModel.tenSanPham" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Thương hiệu">
            <el-input v-model="sanPhamModel.thuongHieu" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Nhà cung cấp">
            <el-input :value="getNhaCungCapLabel(sanPhamModel.idNhaCungCap)" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Trạng thái">
            <el-input :value="getTrangThaiLabel(sanPhamModel.trangThaiSanPham)" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Địa chỉ NCC">
            <el-input :value="sanPhamModel.diaChi" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="SĐT NCC">
            <el-input :value="sanPhamModel.sdt" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Email NCC">
            <el-input :value="sanPhamModel.email" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Danh sách sản phẩm chi tiết -->
      <h3>Danh sách biến thể sản phẩm</h3>
      <el-table :data="sanPhamModel.sanPhamChiTiets" style="width: 100%" border @row-click="selectChiTiet">
        <el-table-column type="index" label="STT" width="80" :index="indexMethod" />
        <el-table-column label="Mã SP chi tiết" prop="maSanPhamChiTiet" width="150" />
        <el-table-column label="Màu sắc" prop="idMau">
          <template #default="{ row }">
            {{ getMauSacLabel(row.idMau) }}
          </template>
        </el-table-column>
        <el-table-column label="RAM" prop="idRam">
          <template #default="{ row }">
            {{ getRamLabel(row.idRam) }}
          </template>
        </el-table-column>
        <el-table-column label="ROM" prop="idRom">
          <template #default="{ row }">
            {{ getRomLabel(row.idRom) }}
          </template>
        </el-table-column>
        <el-table-column prop="soLuongSPCT" label="Số lượng" width="120" />
        <el-table-column prop="giaBan" label="Giá bán" width="120" />
      </el-table>

      <!-- Chi tiết biến thể sản phẩm được chọn -->
      <div v-if="selectedChiTiet !== null" class="mt-4">
        <h4>Chi tiết biến thể {{ selectedChiTiet + 1 }}</h4>

        <!-- Thông tin cơ bản của biến thể -->
        <h5>Thông tin cơ bản</h5>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Mã SP chi tiết">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].maSanPhamChiTiet" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Màu sắc">
              <el-input :value="getMauSacLabel(sanPhamModel.sanPhamChiTiets[selectedChiTiet].idMau)"
                readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Mã màu">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].maMau" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- Thông tin RAM -->
        <h5>Thông tin RAM</h5>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Dung lượng RAM">
              <el-input :value="getRamLabel(sanPhamModel.sanPhamChiTiets[selectedChiTiet].idRam)" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Loại RAM">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].loaiRam" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Tốc độ đọc/ghi">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].tocDoDocGhiRam" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Nhà sản xuất RAM">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].nhaSanXuatRam" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Năm ra mắt RAM">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].namRaMatRam" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- Thông tin ROM -->
        <h5>Thông tin ROM</h5>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Dung lượng ROM">
              <el-input :value="getRomLabel(sanPhamModel.sanPhamChiTiets[selectedChiTiet].idRom)" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Loại ROM">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].loaiRom" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Tốc độ đọc/ghi">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].tocDoDocGhiRom" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Nhà sản xuất ROM">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].nhaSanXuatRom" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Năm ra mắt ROM">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].namRaMatRom" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- Thông tin màn hình -->
        <h5>Thông tin màn hình</h5>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Tên màn hình">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].tenManHinh" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Kích thước">
              <el-input :value="getManHinhLabel(sanPhamModel.sanPhamChiTiets[selectedChiTiet].idManHinh)"
                readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Loại màn hình">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].loaiManHinh" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Độ phân giải">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].doPhanGiaiManHinh" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Tần số quét">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].tanSoQuet" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Độ sáng">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].doSang" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Chất liệu kính">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].chatLieuKinh" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- Thông tin hệ điều hành -->
        <h5>Thông tin hệ điều hành</h5>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Phiên bản HĐH">
              <el-input :value="getHeDieuHanhLabel(sanPhamModel.sanPhamChiTiets[selectedChiTiet].idHeDieuHanh)"
                readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Nhà phát triển">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].nhaPhatTrien" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Giao diện người dùng">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].giaoDienNguoiDung" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- Thông tin pin -->
        <h5>Thông tin pin</h5>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Phiên bản pin">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].phienBanPin" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Công suất sạc">
              <el-input :value="getPinLabel(sanPhamModel.sanPhamChiTiets[selectedChiTiet].idPin)" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Thời gian sử dụng">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].thoiGianSuDung" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Số lần sạc tối đa">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].soLanSacToiDa" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- Thông tin CPU -->
        <h5>Thông tin CPU</h5>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Hãng sản xuất">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].hangSanXuat" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Số nhân">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].soNhan" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Số lượng CPU">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].soLuongCpu" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Xung nhịp">
              <el-input :value="getCpuLabel(sanPhamModel.sanPhamChiTiets[selectedChiTiet].idCpu)" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Công nghệ sản xuất">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].congNgheSanXuat" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Bộ nhớ đệm">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].boNhoDem" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Tiêu thụ điện năng">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].tieuThuDienNang" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Năm ra mắt">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].namRaMat" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- Thông tin camera trước -->
        <h5>Thông tin camera trước</h5>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Loại camera">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].loaiCameraTruoc" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Độ phân giải">
              <el-input :value="getCameraTruocLabel(sanPhamModel.sanPhamChiTiets[selectedChiTiet].idCameraTruoc)"
                readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Khẩu độ">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].khauDoCameraTruoc" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Loại zoom">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].loaiZoomCameraTruoc" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Chế độ chụp">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].cheDoChupCameraTruoc" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- Thông tin camera sau -->
        <h5>Thông tin camera sau</h5>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Loại camera">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].loaiCameraSau" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Độ phân giải">
              <el-input :value="getCameraSauLabel(sanPhamModel.sanPhamChiTiets[selectedChiTiet].idCameraSau)"
                readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Khẩu độ">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].khauDoCameraSau" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Loại zoom">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].loaiZoomCameraSau" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Chế độ chụp">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].cheDoChupCameraSau" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- Thông tin xuất xứ và loại -->
        <h5>Thông tin xuất xứ và loại</h5>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Mã xuất xứ">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].maXuatXu" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Quốc gia">
              <el-input :value="getXuatXuLabel(sanPhamModel.sanPhamChiTiets[selectedChiTiet].idXuatXu)"
                readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Loại sản phẩm">
              <el-input :value="getLoaiLabel(sanPhamModel.sanPhamChiTiets[selectedChiTiet].idLoai)" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- IMEI và số lượng -->
        <h5>Thông tin IMEI</h5>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Giá bán">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].giaBan" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Số lượng">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].soLuongSPCT" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="IMEI">
          <el-input type="textarea" :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet].imeisInput"
            readonly></el-input>
          <div style="margin-top: 8px;">
            Số lượng IMEI: {{ sanPhamModel.sanPhamChiTiets[selectedChiTiet].soLuongSPCT }}
          </div>
        </el-form-item>

        <!-- Hình ảnh -->
        <h5>Hình ảnh sản phẩm</h5>
        <el-form-item label="Hình ảnh">
          <div class="image-preview">
            <el-image v-for="img in sanPhamModel.sanPhamChiTiets[selectedChiTiet].hinhAnhs" :key="img.url"
              :src="img.url" :preview-src-list="[img.url]"
              style="width: 100px; height: 100px; margin-right: 8px; border-radius: 4px;" />
          </div>
        </el-form-item>
      </div>
      <div v-else class="mt-4">
        <el-alert title="Vui lòng chọn một biến thể để xem chi tiết" type="info" show-icon />
      </div>

      <!-- Nút quay lại -->
      <el-row :gutter="20" class="mt-4">
        <el-col :span="24" class="text-center">
          <el-button type="default" @click="$router.push('/admin/products')">Quay lại</el-button>
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
import {
  getSanPhamById,
  getAllNhaCungCapList,
  getAllMauSacList,
  getAllRamList,
  getAllRomList,
  getAllManHinhList,
  getAllHDHList,
  getAllPinList,
  getAllCpuList,
  getAllCameraTruocList,
  getAllCameraSauList,
  getAllXuatXuList,
  getAllLoaiList,
} from '@/Service/Adminservice/Products/ProductAdminService';

const route = useRoute();
const router = useRouter();
const id = route.params.id;
const sanPhamModel = reactive({
  tenSanPham: '',
  thuongHieu: '',
  idNhaCungCap: null,
  trangThaiSanPham: '',
  diaChi: '',
  sdt: '',
  email: '',
  sanPhamChiTiets: [],
});
const error = ref('');
const selectedChiTiet = ref(null);
const nhaCungCaps = ref([]);
const maus = ref([]);
const rams = ref([]);
const roms = ref([]);
const manHinhs = ref([]);
const heDieuHanhs = ref([]);
const pins = ref([]);
const cpus = ref([]);
const cameraTruocs = ref([]);
const cameraSaus = ref([]);
const xuatXus = ref([]);
const loais = ref([]);

const danhSachTrangThaiSanPham = [
  { label: 'Đang kinh doanh', value: 'ACTIVE' },
  { label: 'Ngừng kinh doanh', value: 'DISCONTINUED' },
  { label: 'Sắp ra mắt', value: 'COMING_SOON' },
  { label: 'Tạm ngừng bán', value: 'TEMPORARILY_UNAVAILABLE' },
  { label: 'Hết hàng', value: 'OUT_OF_STOCK' },
];

const fetchSanPham = async (id) => {
  try {
    const response = await getSanPhamById(id);
    sanPhamModel.id = response.id;
    sanPhamModel.tenSanPham = response.tenSanPham;
    sanPhamModel.thuongHieu = response.thuongHieu;
    sanPhamModel.idNhaCungCap = response.idNhaCungCap;
    sanPhamModel.trangThaiSanPham = response.trangThaiSanPham;
    sanPhamModel.diaChi = response.diaChi;
    sanPhamModel.sdt = response.sdt;
    sanPhamModel.email = response.email;
    sanPhamModel.sanPhamChiTiets = response.sanPhamChiTiets.map((chiTiet) => ({
      id: chiTiet.id,
      maSanPhamChiTiet: chiTiet.maSanPhamChiTiet,
      idMau: chiTiet.idMau,
      maMau: chiTiet.maMau,
      tenMau: chiTiet.tenMau,
      idRam: chiTiet.idRam,
      dungLuongRam: chiTiet.dungLuongRam,
      loaiRam: chiTiet.loaiRam,
      tocDoDocGhiRam: chiTiet.tocDoDocGhiRam,
      nhaSanXuatRam: chiTiet.nhaSanXuatRam,
      namRaMatRam: chiTiet.namRaMatRam,
      idRom: chiTiet.idRom,
      dungLuongRom: chiTiet.dungLuongRom,
      loaiRom: chiTiet.loaiRom,
      tocDoDocGhiRom: chiTiet.tocDoDocGhiRom,
      nhaSanXuatRom: chiTiet.nhaSanXuatRom,
      namRaMatRom: chiTiet.namRaMatRom,
      idManHinh: chiTiet.idManHinh,
      tenManHinh: chiTiet.tenManHinh,
      kichThuoc: chiTiet.kichThuoc,
      loaiManHinh: chiTiet.loaiManHinh,
      doPhanGiaiManHinh: chiTiet.doPhanGiaiManHinh,
      tanSoQuet: chiTiet.tanSoQuet,
      doSang: chiTiet.doSang,
      chatLieuKinh: chiTiet.chatLieuKinh,
      idHeDieuHanh: chiTiet.idHeDieuHanh,
      phienBanHeDieuHanh: chiTiet.phienBanHeDieuHanh,
      nhaPhatTrien: chiTiet.nhaPhatTrien,
      giaoDienNguoiDung: chiTiet.giaoDienNguoiDung,
      idPin: chiTiet.idPin,
      phienBanPin: chiTiet.phienBanPin,
      congSuatSac: chiTiet.congSuatSac,
      thoiGianSuDung: chiTiet.thoiGianSuDung,
      soLanSacToiDa: chiTiet.soLanSacToiDa,
      idCpu: chiTiet.idCpu,
      hangSanXuat: chiTiet.hangSanXuat,
      soNhan: chiTiet.soNhan,
      soLuongCpu: chiTiet.soLuongCpu,
      xungNhip: chiTiet.xungNhip,
      congNgheSanXuat: chiTiet.congNgheSanXuat,
      boNhoDem: chiTiet.boNhoDem,
      tieuThuDienNang: chiTiet.tieuThuDienNang,
      namRaMat: chiTiet.namRaMat,
      idCameraTruoc: chiTiet.idCameraTruoc,
      loaiCameraTruoc: chiTiet.loaiCameraTruoc,
      doPhanGiaiCameraTruoc: chiTiet.doPhanGiaiCameraTruoc,
      khauDoCameraTruoc: chiTiet.khauDoCameraTruoc,
      loaiZoomCameraTruoc: chiTiet.loaiZoomCameraTruoc,
      cheDoChupCameraTruoc: chiTiet.cheDoChupCameraTruoc,
      idCameraSau: chiTiet.idCameraSau,
      loaiCameraSau: chiTiet.loaiCameraSau,
      doPhanGiaiCameraSau: chiTiet.doPhanGiaiCameraSau,
      khauDoCameraSau: chiTiet.khauDoCameraSau,
      loaiZoomCameraSau: chiTiet.loaiZoomCameraSau,
      cheDoChupCameraSau: chiTiet.cheDoChupCameraSau,
      idXuatXu: chiTiet.idXuatXu,
      maXuatXu: chiTiet.maXuatXu,
      tenQuocGia: chiTiet.tenQuocGia,
      idLoai: chiTiet.idLoai,
      tenLoai: chiTiet.tenLoai,
      soLuongSPCT: chiTiet.soLuongSPCT,
      giaBan: chiTiet.giaBan,
      imeisInput: chiTiet.imeis?.map((i) => i.soImei).join(', ') || '',
      hinhAnhs: chiTiet.hinhAnhs?.map((h) => ({
        name: h.url.split('/').pop(),
        url: h.url,
        imagePublicId: h.imagePublicId,
      })) || [],
    }));
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
      getAllRamList(),
      getAllRomList(),
      getAllManHinhList(),
      getAllHDHList(),
      getAllPinList(),
      getAllCpuList(),
      getAllCameraTruocList(),
      getAllCameraSauList(),
      getAllXuatXuList(),
      getAllLoaiList(),
    ]);
    nhaCungCaps.value = responses[0];
    maus.value = responses[1];
    rams.value = responses[2];
    roms.value = responses[3];
    manHinhs.value = responses[4];
    heDieuHanhs.value = responses[5];
    pins.value = responses[6];
    cpus.value = responses[7];
    cameraTruocs.value = responses[8];
    cameraSaus.value = responses[9];
    xuatXus.value = responses[10];
    loais.value = responses[11];
  } catch (err) {
    error.value = err.message || 'Lỗi khi tải danh mục';
    ElMessage.error(error.value);
  }
};

const getNhaCungCapLabel = (id) => nhaCungCaps.value.find((ncc) => ncc.id === id)?.tenNhaCungCap || 'Không rõ';
const getTrangThaiLabel = (value) =>
  danhSachTrangThaiSanPham.find((tt) => tt.value === value)?.label || 'Không rõ';
const getMauSacLabel = (id) => maus.value.find((m) => m.id === id)?.tenMau || 'Không rõ';
const getRamLabel = (id) => rams.value.find((r) => r.id === id)?.dungLuongRam || 'Không rõ';
const getRomLabel = (id) => roms.value.find((r) => r.id === id)?.dungLuongRom || 'Không rõ';
const getManHinhLabel = (id) => manHinhs.value.find((mh) => mh.id === id)?.kichThuoc || 'Không rõ';
const getHeDieuHanhLabel = (id) => heDieuHanhs.value.find((hdh) => hdh.id === id)?.phienBanHeDieuHanh || 'Không rõ';
const getPinLabel = (id) => pins.value.find((p) => p.id === id)?.congSuatSac || 'Không rõ';
const getCpuLabel = (id) => cpus.value.find((c) => c.id === id)?.xungNhip || 'Không rõ';
const getCameraTruocLabel = (id) => cameraTruocs.value.find((c) => c.id === id)?.doPhanGiaiCameraTruoc || 'Không rõ';
const getCameraSauLabel = (id) => cameraSaus.value.find((c) => c.id === id)?.doPhanGiaiCameraSau || 'Không rõ';
const getXuatXuLabel = (id) => xuatXus.value.find((xx) => xx.id === id)?.tenQuocGia || 'Không rõ';
const getLoaiLabel = (id) => loais.value.find((l) => l.id === id)?.tenLoai || 'Không rõ';

const selectChiTiet = (row, column, event) => {
  const index = sanPhamModel.sanPhamChiTiets.indexOf(row);
  selectedChiTiet.value = index;
};

const indexMethod = (index) => index + 1;

onMounted(async () => {
  await Promise.all([fetchDanhMuc(), fetchSanPham(id)]);
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

h5 {
  font-size: 16px;
  font-weight: 500;
  margin: 16px 0 12px;
  color: #4b5563;
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

.el-button--default {
  background: #f3f4f6;
  border-color: #d1d5db;
  color: #374151;
}

.el-button--default:hover {
  background: #e5e7eb;
  border-color: #9ca3af;
}

.el-alert {
  border-radius: 8px;
  margin-top: 24px;
}

.text-center {
  text-align: center;
}

.image-preview {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
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

  h5 {
    font-size: 14px;
  }

  .el-table td {
    padding: 8px 12px;
    font-size: 13px;
  }
}
</style>