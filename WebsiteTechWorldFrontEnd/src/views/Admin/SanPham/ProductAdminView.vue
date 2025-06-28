<template>
  <div class="container mt-4">
    <h2>Chi tiết sản phẩm</h2>
    <el-form :model="sanPhamModel" label-width="150px">
      <!-- Thông tin sản phẩm chính -->
      <h3>Thông tin chung</h3>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Mã sản phẩm">
            <el-input v-model="sanPhamModel.maSanPham" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Tên sản phẩm">
            <el-input v-model="sanPhamModel.tenSanPham" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Thương hiệu">
            <el-input v-model="sanPhamModel.thuongHieu" readonly></el-input>
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
          <el-form-item label="Nhà cung cấp">
            <el-input v-model="sanPhamModel.tenNhaCungCap" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Địa chỉ NCC">
            <el-input v-model="sanPhamModel.diaChi" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="SĐT NCC">
            <el-input v-model="sanPhamModel.sdt" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Email NCC">
            <el-input v-model="sanPhamModel.email" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Tên model">
            <el-input :value="sanPhamModel.modelSanPham?.tenModel || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Mã model">
            <el-input :value="sanPhamModel.modelSanPham?.maModelSanPham || ''" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Năm ra mắt">
            <el-input :value="sanPhamModel.modelSanPham?.namRaMat || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Mô tả">
            <el-input :value="sanPhamModel.modelSanPham?.moTa || ''" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Trạng thái model">
            <el-input :value="sanPhamModel.modelSanPham?.trangThai || ''" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Thông tin RAM -->
      <h3>Thông tin RAM</h3>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="Dung lượng RAM">
            <el-input :value="sanPhamModel.modelSanPham?.dungLuongRam || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Loại RAM">
            <el-input :value="sanPhamModel.modelSanPham?.loaiRam || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Tốc độ đọc/ghi">
            <el-input :value="sanPhamModel.modelSanPham?.tocDoDocGhiRam || ''" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="Nhà sản xuất RAM">
            <el-input :value="sanPhamModel.modelSanPham?.nhaSanXuatRam || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Năm ra mắt RAM">
            <el-input :value="sanPhamModel.modelSanPham?.namRaMatRam || ''" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Thông tin màn hình -->
      <h3>Thông tin màn hình</h3>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="Tên màn hình">
            <el-input :value="sanPhamModel.modelSanPham?.tenManHinh || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Kích thước">
            <el-input :value="sanPhamModel.modelSanPham?.kichThuoc || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Loại màn hình">
            <el-input :value="sanPhamModel.modelSanPham?.loaiManHinh || ''" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="Độ phân giải">
            <el-input :value="sanPhamModel.modelSanPham?.doPhanGiaiManHinh || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Tần số quét">
            <el-input :value="sanPhamModel.modelSanPham?.tanSoQuet || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Độ sáng">
            <el-input :value="sanPhamModel.modelSanPham?.doSang || ''" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="Chất liệu kính">
            <el-input :value="sanPhamModel.modelSanPham?.chatLieuKinh || ''" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Thông tin hệ điều hành -->
      <h3>Thông tin hệ điều hành</h3>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="Phiên bản HĐH">
            <el-input :value="sanPhamModel.modelSanPham?.phienBanHeDieuHanh || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Nhà phát triển">
            <el-input :value="sanPhamModel.modelSanPham?.nhaPhatTrien || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Giao diện người dùng">
            <el-input :value="sanPhamModel.modelSanPham?.giaoDienNguoiDung || ''" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Thông tin pin -->
      <h3>Thông tin pin</h3>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="Phiên bản pin">
            <el-input :value="sanPhamModel.modelSanPham?.phienBanPin || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Công suất sạc">
            <el-input :value="sanPhamModel.modelSanPham?.congSuatSac || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Thời gian sử dụng">
            <el-input :value="sanPhamModel.modelSanPham?.thoiGianSuDung || ''" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="Số lần sạc tối đa">
            <el-input :value="sanPhamModel.modelSanPham?.soLanSacToiDa || ''" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Thông tin CPU -->
      <h3>Thông tin CPU</h3>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="Hãng sản xuất">
            <el-input :value="sanPhamModel.modelSanPham?.hangSanXuat || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Số nhân">
            <el-input :value="sanPhamModel.modelSanPham?.soNhan || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Chip xử lý">
            <el-input :value="sanPhamModel.modelSanPham?.chipXuLy || ''" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="Xung nhịp">
            <el-input :value="sanPhamModel.modelSanPham?.xungNhip || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Công nghệ sản xuất">
            <el-input :value="sanPhamModel.modelSanPham?.congNgheSanXuat || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Bộ nhớ đệm">
            <el-input :value="sanPhamModel.modelSanPham?.boNhoDem || ''" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="Tiêu thụ điện năng">
            <el-input :value="sanPhamModel.modelSanPham?.tieuThuDienNang || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Năm ra mắt CPU">
            <el-input :value="sanPhamModel.modelSanPham?.namRaMatCpu || ''" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Thông tin camera trước -->
      <h3>Thông tin camera trước</h3>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="Loại camera">
            <el-input :value="sanPhamModel.modelSanPham?.loaiCameraTruoc || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Độ phân giải">
            <el-input :value="sanPhamModel.modelSanPham?.doPhanGiaiCameraTruoc || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Khẩu độ">
            <el-input :value="sanPhamModel.modelSanPham?.khauDoCameraTruoc || ''" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="Loại zoom">
            <el-input :value="sanPhamModel.modelSanPham?.loaiZoomCameraTruoc || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Chế độ chụp">
            <el-input :value="sanPhamModel.modelSanPham?.cheDoChupCameraTruoc || ''" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Thông tin camera sau -->
      <h3>Thông tin camera sau</h3>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="Loại camera">
            <el-input :value="sanPhamModel.modelSanPham?.loaiCameraSau || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Độ phân giải">
            <el-input :value="sanPhamModel.modelSanPham?.doPhanGiaiCameraSau || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Khẩu độ">
            <el-input :value="sanPhamModel.modelSanPham?.khauDoCameraSau || ''" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="Loại zoom">
            <el-input :value="sanPhamModel.modelSanPham?.loaiZoomCameraSau || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Chế độ chụp">
            <el-input :value="sanPhamModel.modelSanPham?.cheDoChupCameraSau || ''" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Thông tin xuất xứ và loại -->
      <h3>Thông tin xuất xứ và loại</h3>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="Mã xuất xứ">
            <el-input :value="sanPhamModel.modelSanPham?.maXuatXu || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Quốc gia">
            <el-input :value="sanPhamModel.modelSanPham?.tenQuocGia || ''" readonly></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Loại sản phẩm">
            <el-input :value="sanPhamModel.modelSanPham?.tenLoai || ''" readonly></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Danh sách sản phẩm chi tiết -->
      <h3>Danh sách biến thể sản phẩm</h3>
      <el-table :data="sanPhamModel.sanPhamChiTiets" style="width: 100%" border @row-click="selectChiTiet">
        <el-table-column type="index" label="STT" width="80" :index="indexMethod" />
        <el-table-column label="Mã SP chi tiết" prop="maSanPhamChiTiet" width="150" />
        <el-table-column label="Màu sắc" prop="tenMau" />
        <el-table-column label="ROM" prop="dungLuongRom" />
        <el-table-column prop="soLuongSPCT" label="Số lượng" width="120" />
        <el-table-column label="Giá bán" width="120">
          <template #default="{ row }">
            {{ formatPrice(row.giaBan) }}
          </template>
        </el-table-column>
      </el-table>

      <!-- Chi tiết biến thể sản phẩm được chọn -->
      <div v-if="selectedChiTiet !== null" class="mt-4">
        <h4>Chi tiết biến thể {{ selectedChiTiet + 1 }}</h4>

        <!-- Thông tin cơ bản của biến thể -->
        <h5>Thông tin cơ bản</h5>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Mã SP chi tiết">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet]?.maSanPhamChiTiet || ''" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Màu sắc">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet]?.tenMau || ''" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Mã màu">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet]?.maMau || ''" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- Thông tin ROM -->
        <h5>Thông tin ROM</h5>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Dung lượng ROM">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet]?.dungLuongRom || ''" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- IMEI và số lượng -->
        <h5>Thông tin IMEI</h5>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Giá bán">
              <el-input :value="formatPrice(sanPhamModel.sanPhamChiTiets[selectedChiTiet]?.giaBan)" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Số lượng">
              <el-input :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet]?.soLuongSPCT || ''" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="IMEI">
          <el-input type="textarea" :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet]?.imeisInput || ''" readonly></el-input>
          <div style="margin-top: 8px;">
            Số lượng IMEI: {{ sanPhamModel.sanPhamChiTiets[selectedChiTiet]?.soLuongSPCT || 0 }}
          </div>
        </el-form-item>

        <!-- Hình ảnh -->
        <h5>Hình ảnh sản phẩm</h5>
        <el-form-item label="Hình ảnh">
          <div class="image-preview">
            <el-image v-for="img in sanPhamModel.sanPhamChiTiets[selectedChiTiet]?.hinhAnhs || []" :key="img.url"
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
  getAllNhaCungCapList,
  getViewSanPham,
} from '@/Service/Adminservice/Products/ProductAdminService';

const route = useRoute();
const router = useRouter();
const id = route.params.id;
const sanPhamModel = reactive({
  id: null,
  maSanPham: '',
  tenSanPham: '',
  thuongHieu: '',
  trangThaiSanPham: '',
  idNhaCungCap: null,
  tenNhaCungCap: '',
  diaChi: '',
  sdt: '',
  email: '',
  modelSanPham: {},
  sanPhamChiTiets: [],
});
const error = ref('');
const selectedChiTiet = ref(null);
const nhaCungCaps = ref([]);

const danhSachTrangThaiSanPham = [
  { label: 'Đang kinh doanh', value: 'ACTIVE' },
  { label: 'Ngừng kinh doanh', value: 'DISCONTINUED' },
  { label: 'Sắp ra mắt', value: 'COMING_SOON' },
  { label: 'Tạm ngừng bán', value: 'TEMPORARILY_UNAVAILABLE' },
  { label: 'Hết hàng', value: 'OUT_OF_STOCK' },
];

const fetchSanPham = async (id) => {
  try {
    const response = await getViewSanPham(id);

    // Ánh xạ thông tin cơ bản
    sanPhamModel.id = response.id || null;
    sanPhamModel.maSanPham = response.maSanPham || '';
    sanPhamModel.tenSanPham = response.tenSanPham || '';
    sanPhamModel.thuongHieu = response.thuongHieu || '';
    sanPhamModel.trangThaiSanPham = response.trangThaiSanPham || '';

    // Ánh xạ thông tin nhà cung cấp
    if (response.nhaCungCapAdminResponse) {
      sanPhamModel.idNhaCungCap = response.nhaCungCapAdminResponse.id || null;
      sanPhamModel.tenNhaCungCap = response.nhaCungCapAdminResponse.tenNhaCungCap || '';
      sanPhamModel.diaChi = response.nhaCungCapAdminResponse.diaChi || '';
      sanPhamModel.sdt = response.nhaCungCapAdminResponse.sdt || '';
      sanPhamModel.email = response.nhaCungCapAdminResponse.email || '';
    } else {
      sanPhamModel.idNhaCungCap = null;
      sanPhamModel.tenNhaCungCap = '';
      sanPhamModel.diaChi = '';
      sanPhamModel.sdt = '';
      sanPhamModel.email = '';
    }

    // Ánh xạ thông tin model sản phẩm
    if (response.modelSanPhamAdminResponse) {
      sanPhamModel.modelSanPham = {
        idModelSanPham: response.modelSanPhamAdminResponse.idModelSanPham || null,
        maModelSanPham: response.modelSanPhamAdminResponse.maModelSanPham || '',
        tenModel: response.modelSanPhamAdminResponse.tenModel || '',
        namRaMat: response.modelSanPhamAdminResponse.namRaMat || '',
        moTa: response.modelSanPhamAdminResponse.moTa || '',
        trangThai: response.modelSanPhamAdminResponse.trangThai || '',
        dungLuongRam: response.modelSanPhamAdminResponse.dungLuongRam || '',
        loaiRam: response.modelSanPhamAdminResponse.loaiRam || '',
        tocDoDocGhiRam: response.modelSanPhamAdminResponse.tocDoDocGhiRam || '',
        nhaSanXuatRam: response.modelSanPhamAdminResponse.nhaSanXuatRam || '',
        namRaMatRam: response.modelSanPhamAdminResponse.namRaMatRam || '',
        tenManHinh: response.modelSanPhamAdminResponse.tenManHinh || '',
        kichThuoc: response.modelSanPhamAdminResponse.kichThuoc || '',
        loaiManHinh: response.modelSanPhamAdminResponse.loaiManHinh || '',
        doPhanGiaiManHinh: response.modelSanPhamAdminResponse.doPhanGiaiManHinh || '',
        tanSoQuet: response.modelSanPhamAdminResponse.tanSoQuet || '',
        doSang: response.modelSanPhamAdminResponse.doSang || '',
        chatLieuKinh: response.modelSanPhamAdminResponse.chatLieuKinh || '',
        phienBanHeDieuHanh: response.modelSanPhamAdminResponse.phienBanHeDieuHanh || '',
        nhaPhatTrien: response.modelSanPhamAdminResponse.nhaPhatTrien || '',
        giaoDienNguoiDung: response.modelSanPhamAdminResponse.giaoDienNguoiDung || '',
        phienBanPin: response.modelSanPhamAdminResponse.phienBanPin || '',
        congSuatSac: response.modelSanPhamAdminResponse.congSuatSac || '',
        thoiGianSuDung: response.modelSanPhamAdminResponse.thoiGianSuDung || '',
        soLanSacToiDa: response.modelSanPhamAdminResponse.soLanSacToiDa || '',
        hangSanXuat: response.modelSanPhamAdminResponse.hangSanXuat || '',
        soNhan: response.modelSanPhamAdminResponse.soNhan || '',
        chipXuLy: response.modelSanPhamAdminResponse.chipXuLy || '',
        xungNhip: response.modelSanPhamAdminResponse.xungNhip || '',
        congNgheSanXuat: response.modelSanPhamAdminResponse.congNgheSanXuat || '',
        boNhoDem: response.modelSanPhamAdminResponse.boNhoDem || '',
        tieuThuDienNang: response.modelSanPhamAdminResponse.tieuThuDienNang || '',
        namRaMatCpu: response.modelSanPhamAdminResponse.namRaMatCpu || '',
        loaiCameraTruoc: response.modelSanPhamAdminResponse.loaiCameraTruoc || '',
        doPhanGiaiCameraTruoc: response.modelSanPhamAdminResponse.doPhanGiaiCameraTruoc || '',
        khauDoCameraTruoc: response.modelSanPhamAdminResponse.khauDoCameraTruoc || '',
        loaiZoomCameraTruoc: response.modelSanPhamAdminResponse.loaiZoomCameraTruoc || '',
        cheDoChupCameraTruoc: response.modelSanPhamAdminResponse.cheDoChupCameraTruoc || '',
        loaiCameraSau: response.modelSanPhamAdminResponse.loaiCameraSau || '',
        doPhanGiaiCameraSau: response.modelSanPhamAdminResponse.doPhanGiaiCameraSau || '',
        khauDoCameraSau: response.modelSanPhamAdminResponse.khauDoCameraSau || '',
        loaiZoomCameraSau: response.modelSanPhamAdminResponse.loaiZoomCameraSau || '',
        cheDoChupCameraSau: response.modelSanPhamAdminResponse.cheDoChupCameraSau || '',
        maXuatXu: response.modelSanPhamAdminResponse.maXuatXu || '',
        tenQuocGia: response.modelSanPhamAdminResponse.tenQuocGia || '',
        tenLoai: response.modelSanPhamAdminResponse.tenLoai || '',
      };
    } else {
      sanPhamModel.modelSanPham = {};
    }

    // Ánh xạ sanPhamChiTiets
    sanPhamModel.sanPhamChiTiets = response.sanPhamChiTiets?.map((chiTiet) => ({
      id: chiTiet.id || null,
      maSanPhamChiTiet: chiTiet.maSanPhamChiTiet || '',
      soLuongSPCT: chiTiet.soLuongSPCT || 0,
      giaBan: chiTiet.giaBan || 0,
      tenMau: chiTiet.tenMau || '',
      maMau: chiTiet.maMau || '',
      dungLuongRom: chiTiet.dungLuongRom || '',
      imeisInput: chiTiet.imeis?.map((i) => i.soImei).join(', ') || '',
      hinhAnhs: chiTiet.hinhAnhs?.map((h) => ({
        name: h.url?.split('/').pop() || '',
        url: h.url || '',
        imagePublicId: h.imagePublicId || '',
      })) || [],
    })) || [];

    selectedChiTiet.value = sanPhamModel.sanPhamChiTiets.length > 0 ? 0 : null;
  } catch (err) {
    error.value = err.message || 'Lỗi khi tải chi tiết sản phẩm';
    ElMessage.error(error.value);
    console.error('❌ ~ Lỗi khi fetchSanPham:', err);
  }
};

const fetchDanhMuc = async () => {
  try {
    const response = await getAllNhaCungCapList();
    nhaCungCaps.value = response || [];
  } catch (err) {
    error.value = err.message || 'Lỗi khi tải danh mục nhà cung cấp';
    ElMessage.error(error.value);
    console.error('❌ ~ Lỗi khi fetchDanhMuc:', err);
  }
};

const getNhaCungCapLabel = (id) => {
  const result = nhaCungCaps.value.find((ncc) => ncc.id === id);
  return result?.tenNhaCungCap || 'Không rõ';
};

const getTrangThaiLabel = (value) => {
  const result = danhSachTrangThaiSanPham.find((tt) => tt.value === value);
  return result?.label || 'Không rõ';
};

const formatPrice = (value) => {
  if (!value && value !== 0) return '';
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
};

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
/* Sử dụng biến màu của Element Plus để đảm bảo tính nhất quán */
:root {
  --el-color-primary: #409eff;
  --el-color-success: #67c23a;
  --el-color-warning: #e6a23c;
  --el-color-danger: #f56c6c;
  --el-color-info: #909399;
  --el-text-color-primary: #303133;
  --el-text-color-regular: #606266;
  --el-text-color-secondary: #909399;
  --el-border-color-base: #dcdfe6;
  --el-border-color-light: #e4e7ed;
  --el-bg-color: #ffffff;
  --el-bg-color-overlay: #f8f9fa;
}

/* Container chính */
.container {
  min-height: 100vh;
  background: var(--el-bg-color-overlay);
  padding: 32px;
  font-family: 'Inter', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  max-width: 1440px;
  margin: 0 auto;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

/* Tiêu đề chính */
h2 {
  font-size: 28px;
  font-weight: 700;
  margin: 0 0 32px;
  color: var(--el-text-color-primary);
  text-align: center;
  padding-bottom: 16px;
  border-bottom: 2px solid var(--el-border-color-light);
  letter-spacing: 0.5px;
}

/* Tiêu đề phụ cấp 1 */
h3 {
  font-size: 22px;
  font-weight: 600;
  margin: 32px 0 16px;
  color: var(--el-text-color-primary);
  position: relative;
  padding-left: 12px;
}

h3::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 4px;
  background: var(--el-color-primary);
  border-radius: 2px;
}

/* Tiêu đề phụ cấp 2 */
h4 {
  font-size: 18px;
  font-weight: 600;
  margin: 24px 0 12px;
  color: var(--el-text-color-regular);
}

/* Tiêu đề phụ cấp 3 */
h5 {
  font-size: 16px;
  font-weight: 500;
  margin: 16px 0 12px;
  color: var(--el-text-color-regular);
}

/* Form của Element Plus */
.el-form-item {
  margin-bottom: 20px;
}

.el-form-item__label {
  color: var(--el-text-color-regular);
  font-weight: 500;
  font-size: 14px;
}

.el-input {
  --el-input-bg-color: var(--el-bg-color);
  --el-input-border-color: var(--el-border-color-base);
  --el-input-text-color: var(--el-text-color-primary);
}

.el-input__inner {
  border-radius: 6px;
  transition: all 0.3s ease;
}

.el-input.is-disabled .el-input__inner {
  background: #f5f7fa;
  color: var(--el-text-color-regular);
  cursor: not-allowed;
}

/* Bảng biến thể sản phẩm */
.el-table {
  background: var(--el-bg-color);
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  border: 1px solid var(--el-border-color-light);
  margin-top: 16px;
}

.el-table__header th {
  background: #fafafa;
  color: var(--el-text-color-primary);
  font-weight: 600;
  font-size: 14px;
  padding: 12px;
}

.el-table td {
  padding: 12px;
  font-size: 14px;
  color: var(--el-text-color-regular);
  transition: background 0.2s ease;
}

.el-table tr:hover td {
  background: #f5f7fa;
}

/* Nút */
.el-button {
  border-radius: 6px;
  font-weight: 500;
  padding: 12px 24px;
  transition: all 0.3s ease;
}

.el-button--default {
  background: var(--el-bg-color);
  border-color: var(--el-border-color-base);
  color: var(--el-text-color-regular);
}

.el-button--default:hover {
  background: var(--el-border-color-light);
  border-color: var(--el-border-color-base);
  color: var(--el-text-color-primary);
}

/* Alert */
.el-alert {
  border-radius: 8px;
  margin-top: 24px;
  padding: 12px;
}

/* Hình ảnh preview */
.image-preview {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 8px;
}

.el-image {
  border: 1px solid var(--el-border-color-light);
  border-radius: 6px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.el-image:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* Text center */
.text-center {
  text-align: center;
}

/* Margin top */
.mt-4 {
  margin-top: 24px;
}

/* Responsive */
@media (max-width: 768px) {
  .container {
    padding: 16px;
  }

  h2 {
    font-size: 22px;
    margin-bottom: 24px;
  }

  h3 {
    font-size: 18px;
    margin: 24px 0 12px;
  }

  h4 {
    font-size: 16px;
  }

  h5 {
    font-size: 14px;
  }

  .el-form-item {
    margin-bottom: 16px;
  }

  .el-form-item__label {
    font-size: 13px;
  }

  .el-table {
    font-size: 13px;
  }

  .el-table__header th,
  .el-table td {
    padding: 8px;
  }

  .el-button {
    padding: 10px 20px;
  }

  .el-col {
    margin-bottom: 12px;
  }
}

@media (max-width: 576px) {
  .container {
    padding: 12px;
  }

  h2 {
    font-size: 20px;
  }

  h3 {
    font-size: 16px;
  }

  .el-form-item__label {
    font-size: 12px;
    line-height: 1.5;
  }

  .el-input__inner {
    font-size: 12px;
  }

  .image-preview .el-image {
    width: 80px;
    height: 80px;
  }
}
</style>