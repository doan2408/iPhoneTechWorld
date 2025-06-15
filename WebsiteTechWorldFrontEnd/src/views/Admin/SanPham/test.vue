<template>
  <h1 style="text-align: center; font-size: 32px; margin-bottom: 20px;">Thêm sản phẩm</h1>

  <el-form :model="sanPham" label-width="100px" label-position="top" class="form-container">

    <el-form-item label="Tên sản phẩm">
      <el-input v-model="sanPham.tenSanPham" placeholder="Nhập tên sản phẩm" />
    </el-form-item>

    <el-row :gutter="20" align="middle">

      <el-col :span="12">
        <el-form-item label="Thương hiệu">
          <div style="display: flex; align-items: center; gap: 8px; width: 100%;">
            <el-input v-model="sanPham.thuongHieu" placeholder="Nhập tên thương hiệu" />
            <el-button size="small" type="primary" @click="handleClick" style="height: 32px;">
              +
            </el-button>
          </div>
        </el-form-item>
      </el-col>


      <el-col :span="12">
        <el-form-item label="Nhà cung cấp">
          <div style="display: flex; align-items: center; gap: 8px; width: 100%;">
            <el-select v-model="sanPham.idNhaCungCap" placeholder="Chọn nhà cung cấp" style="flex: 1;">
              <el-option v-for="ncc in nhaCungCaps" :key="ncc.id" :label="ncc.tenNhaCungCap" :value="ncc.id" />
            </el-select>

            <el-button size="small" type="primary" @click="openDialog" style="height: 32px;">
              +
            </el-button>
          </div>
        </el-form-item>

        <DialogThemNhaCungCap ref="addNCCDialog" @saved="handleNhaCungCapAdded" />
      </el-col>
    </el-row>



    <h3>Chi tiết sản phẩm</h3>
    <div v-for="chiTiet in sanPham.sanPhamChiTiets" :key="chiTiet.id">

      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="Hệ điều hành">
            <div style="display: flex; align-items: center; gap: 8px; width: 100%;">
              <el-select v-model="chiTiet.idHeDieuHanh" placeholder="Chọn hệ điều hành">
                <el-option v-for="hdh in heDieuHanhs" :key="hdh.id" :label="hdh.phienBan" :value="hdh.id" />
              </el-select>
              <el-button size="small" type="primary" @click="handleSelectClick" style="height: 32px;">
                +
              </el-button>
            </div>
          </el-form-item>
        </el-col>

        <el-col :span="8">
          <el-form-item label="Kích thước màn hình">
            <div style="display: flex; align-items: center; gap: 8px; width: 100%;">
              <el-select v-model="chiTiet.idManHinh" placeholder="Chọn màn hình">
                <el-option v-for="manhinh in manHinhs" :key="manhinh.id" :label="manhinh.kichThuoc"
                  :value="manhinh.id" />
              </el-select>
              <el-button size="small" type="primary" @click="handleSelectClick" style="height: 32px;">
                +
              </el-button>
            </div>
          </el-form-item>
        </el-col>

        <el-col :span="8">
          <el-form-item label="Xuất xứ">
            <div style="display: flex; align-items: center; gap: 8px; width: 100%;">
              <el-select v-model="chiTiet.idXuatXu" placeholder="Chọn xuất xứ">
                <el-option v-for="xx in xuatXus" :key="xx.id" :label="xx.maXuatXu" :value="xx.id" />
              </el-select>
              <el-button size="small" type="primary" @click="handleSelectClick" style="height: 32px;">
                +
              </el-button>
            </div>
          </el-form-item>
        </el-col>
      </el-row>


      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="Camera trước">
            <div style="display: flex; align-items: center; gap: 8px; width: 100%;">
              <el-select v-model="chiTiet.idCameraTruoc" placeholder="Chọn camera trước" multiple>
                <el-option v-for="cameraTruoc in cameraTruocs" :key="cameraTruoc.id" :label="cameraTruoc.doPhanGiai"
                  :value="cameraTruoc.id" />
              </el-select>
              <el-button size="small" type="primary" @click="handleSelectClick" style="height: 32px;">
                +
              </el-button>
            </div>
          </el-form-item>
        </el-col>

        <el-col :span="8">
          <el-form-item label="Camera sau">
            <div style="display: flex; align-items: center; gap: 8px; width: 100%;">
              <el-select v-model="chiTiet.idCameraSau" placeholder="Chọn camera sau" multiple>
                <el-option v-for="cameraSau in cameraSaus" :key="cameraSau.id" :label="cameraSau.doPhanGiai"
                  :value="cameraSau.id" />
              </el-select>
              <el-button size="small" type="primary" @click="handleSelectClick" style="height: 32px;">
                +
              </el-button>
            </div>
          </el-form-item>
        </el-col>

        <el-col :span="8">
          <el-form-item label="Pin">
            <div style="display: flex; align-items: center; gap: 8px; width: 100%;">
              <el-select v-model="chiTiet.idPin" placeholder="Chọn pin">
                <el-option v-for="pin in pins" :key="pin.id" :label="pin.congSuatSac" :value="pin.id" />
              </el-select>
              <el-button size="small" type="primary" @click="handleSelectClick" style="height: 32px;">
                +
              </el-button>
            </div>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Cpu">
            <div style="display: flex; align-items: center; gap: 8px; width: 100%;">
              <el-select v-model="chiTiet.idCpu" placeholder="Chọn cpu">
                <el-option v-for="cpu in cpus" :key="cpu.idCpu" :label="cpu.xungNhip" :value="cpu.id" />
              </el-select>
              <el-button size="small" type="primary" @click="handleSelectClick" style="height: 32px;">
                +
              </el-button>
            </div>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="Loại">
            <div style="display: flex; align-items: center; gap: 8px; width: 100%;">
              <el-select v-model="chiTiet.idLoai" placeholder="Chọn loại">
                <el-option v-for="loai in loais" :key="loai.id" :label="loai.tenLoai" :value="loai.id" />
              </el-select>
              <el-button size="small" type="primary" @click="handleSelectClick" style="height: 32px;">
                +
              </el-button>
            </div>
          </el-form-item>
        </el-col>
      </el-row>


      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="Màu sắc">
            <div style="display: flex; align-items: center; gap: 8px; width: 100%;">
              <el-select v-model="chiTiet.idMau" placeholder="Chọn màu sắc" multiple>
                <el-option v-for="mau in mauSacs" :key="mau.id" :label="mau.tenMau" :value="mau.id" />
              </el-select>
              <el-button size="small" type="primary" @click="handleSelectClick" style="height: 32px;">
                +
              </el-button>
            </div>
          </el-form-item>
        </el-col>

        <el-col :span="8">
          <el-form-item label="Ram">
            <div style="display: flex; align-items: center; gap: 8px; width: 100%;">
              <el-select v-model="chiTiet.idRam" placeholder="Chọn ram" multiple>
                <el-option v-for="ram in rams" :key="ram.id" :label="ram.dungLuong" :value="ram.id" />
              </el-select>
              <el-button size="small" type="primary" @click="handleSelectClick" style="height: 32px;">
                +
              </el-button>
            </div>
          </el-form-item>
        </el-col>

        <el-col :span="8">
          <el-form-item label="Rom">
            <div style="display: flex; align-items: center; gap: 8px; width: 100%;">
              <el-select v-model="chiTiet.idRom" placeholder="Chọn rom" multiple>
                <el-option v-for="rom in roms" :key="rom.id" :label="rom.dungLuong" :value="rom.id" />
              </el-select>
              <el-button size="small" type="primary" @click="handleSelectClick" style="height: 32px;">
                +
              </el-button>
            </div>
          </el-form-item>
        </el-col>
      </el-row>
    </div>

    <div v-for="(phienBan, index) in danhSachPhienBan" :key="index" style="margin-bottom: 40px;">
      <h3 style="margin-bottom: 10px;">PHIÊN BẢN {{ phienBan.tenPhienBan }}</h3> <!-- Tên nhóm RAM-ROM -->

      <el-table :data="phienBan.chiTiet" border style="width: 100%">
        <el-table-column label="STT" width="60" header-align="center" align="center">
          <template #default="{ $index }">
            {{ index * phienBan.chiTiet.length + $index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="tenSanPham" label="Tên sản phẩm" width="150" />

        <el-table-column label="Màu sắc" width="120">
          <template #default="{ row }">
            <div :style="{
              width: '30px',
              height: '30px',
              backgroundColor: layMauSac(row.idMau),
              borderRadius: '4px',
              margin: '0 auto'
            }"></div>
            <div style="text-align:center; font-size: 12px;">{{ layTenMauSac(row.idMau) }}</div>
          </template>
        </el-table-column>

        <el-table-column prop="soLuong" label="Số lượng" width="90" />
        
        <el-table-column prop="giaBan" label="Đơn giá">
          <template #default="{ row }">
            <el-input v-model.number="row.giaBan" type="number" placeholder="Nhập giá trị" min="0" />
          </template>
        </el-table-column>

        <el-table-column label="Thao tác" width="560">
          <template #default="{ row, $index }">
            <div style="display: flex; align-items: center; gap: 10px;">
              <input type="file" :ref="'fileInput_' + $index" @change="(event) => handleFileChange(event, row)"
                style="padding: 6px 12px; border: 1px solid #ccc; border-radius: 4px; cursor: pointer;" />

              <el-button type="danger" @click="uploadFile(row)">
                Upload
              </el-button>

              <el-button type="danger" @click="moDialogNhapImei(row)">
                Nhập
              </el-button>

              <el-button type="danger" @click="xoaChiTiet(phienBan, row)">
                Xoá
              </el-button>

            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <NhapImeiDialog v-model="dialogVisible" @confirm="nhanImeiTuDialog" />
    <div v-if="daChonDayDu" style="margin-bottom: 40px;">

      <div style="margin-bottom: 40px;">
        <h2>Thêm ảnh</h2>

        <el-table :data="danhSachMauSac" border style="margin-top: 20px;">
          <el-table-column label="Tên màu sắc">
            <template #default="{ row }">
              <div>{{ row.tenMauSac }}</div>
            </template>
          </el-table-column>

          <el-table-column label="Màu">
            <template #default="{ row }">
              <div :style="{
                width: '30px',
                height: '30px',
                backgroundColor: row.hexColor,
                borderRadius: '4px',
                border: '1px solid #ccc'
              }">
              </div>
            </template>
          </el-table-column>

          <el-table-column label="Ảnh">
            <template #default="{ row }">
              <div v-if="row.anhUrl">
                <img :src="row.anhUrl" style="width: 50px;" />
              </div>
              <div v-else>Chưa có ảnh</div>
            </template>
          </el-table-column>


          <el-table-column label="Upload ảnh">
            <template #default="{ row }">
              <el-upload :http-request="customUpload" :show-file-list="false" :data="{ row }"
                :before-upload="(file) => handleBeforeUpload(file, row)">
                <el-button type="primary">Tải ảnh</el-button>
              </el-upload>
            </template>
          </el-table-column>

        </el-table>
      </div>

    </div>


    <div style="display: flex; justify-content: center; margin-top: 20px;">
      <el-button type="success" @click="createSanPham">Lưu sản phẩm</el-button>
    </div>

  </el-form>
</template>





<script setup>
import { onMounted, reactive, ref, watch, computed } from 'vue';
import axios from 'axios';
import { getAllCameraSauList, getAllCameraTruocList, getAllCpuList, getAllHDHList, getAllLoaiList, getAllManHinhList, getAllMauSacList, getAllNhaCungCapList, getAllPinList, getAllRamList, getAllRomList, getAllXuatXuList, postNhaCungCapList, postSanPham } from '@/Service/Adminservice/Products/ProductAdminService';
import DialogThemNhaCungCap from '@/components/Admin/dialogs/DialogThemNhaCungCap.vue';
import NhapImeiDialog from '@/components/Admin/dialogs/DialogThemIemi.vue';
import { ElMessage } from 'element-plus';


const nhaCungCaps = ref([]);
const mauSacs = ref([]);
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
const thuongHieus = ref([]);
const errorMessage = ref('');
const addNCCDialog = ref(null);
const hienThiBang = ref(false);
const dialogVisible = ref(false);
const currentRow = ref(null);
const anhTheoMau = reactive({});
const danhSachPhienBan = ref([]);

const customUpload = async (options) => {
  const { file } = options;

  if (!file) {
    ElMessage.error('Vui lòng chọn file ảnh');
    return;
  }

  const formData = new FormData();
  formData.append('file', file);

  try {
    const res = await axios.post('http://localhost:8080/admin/hinhAnh/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    const response = res.data;
    console.log('✅ Upload thành công:', response);
    ElMessage.success('Upload thành công!');

    // Nếu bạn muốn sử dụng URL trả về:
    // const imageUrl = response.url;
    // làm gì đó với imageUrl...

  } catch (err) {
    if (err.response) {
      console.error('Lỗi từ server:', err.response.data);
      const message = err.response.data?.message || 'Lỗi không xác định từ server';
      ElMessage.error(`Upload lỗi: ${message}`);

    } else if (err.request) {
      console.error('Không có phản hồi từ server:', err.request);
      ElMessage.error('Không thể kết nối đến server. Vui lòng kiểm tra lại địa chỉ hoặc khởi động backend.');

    } else {
      console.error('Lỗi không xác định:', err.message);
      ElMessage.error(`Lỗi không xác định: ${err.message}`);
    }
  }
};


const sanPham = reactive({
  id: null,
  tenSanPham: '',
  thuongHieu: '',
  idNhaCungCap: '',
  sanPhamChiTiets: []
});

const addChiTiet = () => {
  sanPham.sanPhamChiTiets.push({
    idSanPham: null,
    idMau: [],
    idRam: [],
    idRom: [],
    idManHinh: null,
    idHeDieuHanh: null,
    idPin: null,
    idCpu: null,
    idCameraTruoc: null,
    idCameraSau: [],
    idXuatXu: null,
    idLoai: null,
    hinhAnhs: [],
    imeis: [],
    soLuong: 0,
    giaBan: 0,
  });
};
const createSanPham = async () => {
  try {
    // 👉 Bước 0: Kiểm tra tên sản phẩm
    if (!sanPham.tenSanPham.trim()) {
      ElMessage.warning('Tên sản phẩm không được để trống');
      return;
    }

    // 👉 Bước 1: Gửi sản phẩm chính (KHÔNG GỬI CHI TIẾT)
    const { tenSanPham, thuongHieu, idNhaCungCap } = sanPham;
    console.log('📤 Gửi dữ liệu sản phẩm chính:', { tenSanPham, thuongHieu, idNhaCungCap });

    const response = await postSanPham({ tenSanPham, thuongHieu, idNhaCungCap });

    const idSanPhamMoi = response.data.id;
    sanPham.id = idSanPhamMoi;

    console.log('✅ Đã tạo sản phẩm thành công, ID mới là:', idSanPhamMoi);

    // 👉 Bước 2: Cập nhật chi tiết sản phẩm (thêm idSanPham, hình ảnh)
    sanPham.sanPhamChiTiets.forEach((ct, index) => {
      const mauId = Array.isArray(ct.idMau) && ct.idMau.length > 0 ? ct.idMau[0] : null;
      const url = mauId && anhTheoMau[mauId] ? anhTheoMau[mauId] : null;

      ct.idSanPham = idSanPhamMoi;

      // Gán hình ảnh theo màu (nếu có)
      ct.hinhAnhs = url ? [{ duongDan: url }] : [];

      console.log(`🧩 Chi tiết sản phẩm #${index + 1}:`);
      console.log('→ ID sản phẩm:', ct.idSanPham);
      console.log('→ ID Màu:', ct.idMau);
      console.log('→ Hình ảnh:', ct.hinhAnhs);
      console.log('→ Các trường khác:', {
        idRam: ct.idRam,
        idRom: ct.idRom,
        idCpu: ct.idCpu,
        idManHinh: ct.idManHinh,
        idPin: ct.idPin,
        idHeDieuHanh: ct.idHeDieuHanh,
        idCameraTruoc: ct.idCameraTruoc,
        idCameraSau: ct.idCameraSau,
        idLoai: ct.idLoai,
        idXuatXu: ct.idXuatXu,
        imeis: ct.imeis,
        soLuong: ct.soLuong,
        giaBan: ct.giaBan
      });
    });

    // 👉 Bước 3: Gửi danh sách chi tiết sản phẩm
    console.log('🚀 Gửi danh sách chi tiết sản phẩm:');
    console.log(JSON.stringify(sanPham.sanPhamChiTiets, null, 2));

    await postSanPhamChiTiets(sanPham.sanPhamChiTiets);

    // ✅ Thành công
    ElMessage.success('Tạo sản phẩm thành công!');
    console.log('🎉 Tạo sản phẩm hoàn tất! Dữ liệu đầy đủ:');
    console.log(JSON.stringify(sanPham, null, 2));
  } catch (error) {
    // ❌ Lỗi xảy ra
    console.error('❌ Lỗi khi tạo sản phẩm:', error);
    ElMessage.error('Tạo sản phẩm thất bại!');
  }
};




//open dialog

function openDialog() {
  addNCCDialog.value?.open();
}

function handleNhaCungCapAdded(newNCC) {
  nhaCungCaps.value.push(newNCC);
}

const handleSelectClick = () => {
  dialogVisible.value = true;
};

const handleClick = () => {
  alert('Bạn cần implement thêm thương hiệu mới');
};


// const saveForm = async () => {
//   if (!sanPham.tenSanPham.trim()) {
//     alert('Tên sản phẩm không được để trống');
//     return;
//   }
//   if (sanPham.sanPhamChiTiets.length === 0) {
//     alert('Bạn cần thêm ít nhất một cấu hình chi tiết');
//     return;
//   }
//   try {
//     console.log('Dữ liệu gửi đi:', JSON.stringify(sanPham, null, 2));
//     const response = await axios.post(sanPham);
//     console.log('Dữ liệu đã được lưu:', response.data);
//     alert('Lưu thành công!');
//     hienThiBang.value = true;
//   } catch (error) {
//     console.error('Lỗi khi lưu dữ liệu:', error);
//     alert('Lưu thất bại. Vui lòng thử lại.');
//   }
// };


function xoaChiTiet(phienBan, chiTiet) {
  const idRam = phienBan.idRam;
  const idRom = phienBan.idRom;
  const idMau = chiTiet.idMau;

  // Tìm index bản ghi chi tiết tương ứng
  const index = sanPham.sanPhamChiTiets.findIndex(item => {
    return item.idRam.includes(idRam) &&
      item.idRom.includes(idRom) &&
      item.idMau.includes(idMau);
  });

  if (index !== -1) {
    const item = sanPham.sanPhamChiTiets[index];

    // Xóa màu trong mảng idMau
    item.idMau = item.idMau.filter(m => m !== idMau);
  }
}

// hiển thị bảng màu
watch(
  () => [sanPham.tenSanPham, sanPham.sanPhamChiTiets],
  ([ten, chiTiet]) => {
    const tenHopLe = ten.trim().length > 0;
    const chiTietHopLe = Array.isArray(chiTiet) && chiTiet.length > 0;
    hienThiBang.value = tenHopLe && chiTietHopLe;
  },
  { deep: true }
);


// Tạo bản đồ mã màu
const mapMauSac = computed(() => {
  const map = new Map();
  for (const mau of mauSacs.value) {
    map.set(mau.id, mau.hexColor || '#000000');
  }
  return map;
});

// Hàm lấy tên màu từ ID
const layTenMauSac = (id) => {
  const mau = mauSacs.value.find((m) => m.id === id);
  return mau ? mau.tenMau : 'Không rõ';
};

// Hàm lấy màu dạng hex
const layMauSac = (id) => {
  return mapMauSac.value.get(id) || '#000000';
};

// Hàm lấy tên RAM từ ID
const layTenRam = (id) => {
  const ram = rams.value.find((r) => r.id === id);
  return ram ? ram.dungLuong : 'Không rõ';
};

// Hàm lấy tên ROM từ ID
const layTenRom = (id) => {
  const rom = roms.value.find((r) => r.id === id);
  return rom ? rom.dungLuong : 'Không rõ';
};



// Khi sanPham hoặc chi tiết sản phẩm thay đổi, tính lại
watch(
  () => sanPham.sanPhamChiTiets,
  (newVal) => {
    const danhSachCu = danhSachPhienBan.value; // giữ bản cũ
    danhSachPhienBan.value = tinhToanPhienBan(sanPham, danhSachCu);
  },
  { immediate: true, deep: true }
);


// Hàm xử lý tính danh sách phiên bản
function tinhToanPhienBan(sanPham, danhSachCu = []) {
  const groupByRamRom = {};
  const chiTiets = sanPham.sanPhamChiTiets || [];

  const validChiTiets = chiTiets.filter((chiTiet) =>
    chiTiet.idMau?.length && chiTiet.idRam?.length && chiTiet.idRom?.length
  );

  validChiTiets.forEach((chiTiet) => {
    const maus = chiTiet.idMau;
    const rams = chiTiet.idRam;
    const roms = chiTiet.idRom;

    rams.forEach((idRam) => {
      roms.forEach((idRom) => {
        const key = `${idRam}-${idRom}`;
        if (!groupByRamRom[key]) {
          groupByRamRom[key] = {
            idRam: idRam,
            idRom: idRom,
            tenPhienBan: `${layTenRam(idRam)} / ${layTenRom(idRom)}`,
            chiTiet: [],
          };
        }

        maus.forEach((idMau) => {
          // 🔁 Tìm thông tin cũ (nếu có)
          const cu = danhSachCu.find(p =>
            p.idRam === idRam &&
            p.idRom === idRom &&
            p.chiTiet?.some(ct => ct.idMau === idMau)
          );

          const chiTietCu = cu?.chiTiet?.find(ct => ct.idMau === idMau);

          groupByRamRom[key].chiTiet.push({
            idMau: idMau,
            hexColor: layMauSac(idMau),
            tenMauSac: layTenMauSac(idMau),
            tenSanPham: sanPham.tenSanPham,
            // 👇 Giữ lại giá trị cũ nếu có
            soLuong: chiTietCu?.soLuong ?? chiTiet.soLuong ?? 0,
            giaBan: chiTietCu?.giaBan ?? chiTiet.giaBan ?? 0,
          });
        });
      });
    });
  });

  return Object.values(groupByRamRom);
}


// xử lý bảng màu load ảnh

const danhSachMauSac = computed(() => {
  const danhSach = new Map();

  (sanPham.sanPhamChiTiets || []).forEach((chiTiet) => {
    (chiTiet.idMau || []).forEach((idMau) => {
      if (!danhSach.has(idMau)) {
        danhSach.set(idMau, {
          idMau,
          tenMauSac: layTenMauSac(idMau),
          hexColor: layMauSac(idMau),
        });
      }
    });
  });

  return Array.from(danhSach.values());
});

const daChonDayDu = computed(() => {
  if (!danhSachPhienBan.value.length) return false;

  for (const phienBan of danhSachPhienBan.value) {
    if (!phienBan.chiTiet || !phienBan.chiTiet.length) return false;

    for (const chiTiet of phienBan.chiTiet) {
      if (
        chiTiet.idMau == null ||
        chiTiet.giaBan == null ||
        chiTiet.soLuong == null ||
        !chiTiet.tenSanPham?.trim()
      ) {
        console.log("Thiếu thông tin:", chiTiet);
        return false;
      }
    }
  }

  return true;
});


// upload ảnh

const handleUploadSuccess = (response, file, chiTietIndex) => {
  try {
    if (response.url && response.public_id) {
      // Thêm ảnh vào hinhAnhs của chi tiết sản phẩm tương ứng
      sanPham.sanPhamChiTiets[chiTietIndex].hinhAnhs.push({
        url: response.url,
        imagePublicId: response.public_id
      });
      ElMessage.success("Upload ảnh thành công!");
    } else {
      ElMessage.warning("Thiếu URL hoặc public_id từ phản hồi backend");
    }
  } catch (error) {
    console.error("Lỗi khi xử lý upload:", error);
    ElMessage.error("Lỗi trong quá trình upload ảnh!");
  }
};


const handleBeforeUpload = (file, row) => {
  const reader = new FileReader();
  reader.onload = (e) => {
    row.anhUrl = e.target.result; // Hiển thị ảnh base64 preview tạm thời
  };
  reader.readAsDataURL(file);
  return true; // Cho phép tiếp tục upload
};



// xử lý imei
const moDialogNhapImei = (row) => {
  if (row.listImei && row.listImei.length > 0) {
    alert("Đã upload file IMEI, không được nhập thủ công nữa.");
    return;
  }
  currentRow.value = row;
  dialogVisible.value = true;
};


const nhanImeiTuDialog = (imeis) => {
  const row = currentRow.value;
  if (!row) return;

  if (!row.allImei) {
    row.allImei = [];
  }

  const imeiMoi = imeis.filter(i => !row.allImei.includes(i));
  row.allImei.push(...imeiMoi);
  row.soLuong = row.allImei.length;

  alert(`Đã nhập ${imeiMoi.length} IMEI mới. Tổng cộng: ${row.allImei.length}`);
};


const handleFileChange = (event, row) => {
  row.fileUpload = event.target.files[0]; // chỉ lưu file
};

const uploadFile = (row) => {
  const file = row.fileUpload;
  if (!file) {
    alert("Vui lòng chọn file trước.");
    return;
  }

  const reader = new FileReader();

  reader.onload = (e) => {
    const text = e.target.result;
    const imeis = text
      .trim()
      .split(',')
      .map(s => s.trim())
      .filter(s => s.length > 0);

    row.soLuong = imeis.length;
    row.listImei = imeis; // Lưu lại danh sách IMEI đã upload
    alert(`Đã upload ${imeis.length} IMEI.`);
  };

  reader.onerror = () => {
    alert("Không thể đọc file.");
  };

  reader.readAsText(file);
};




onMounted(async () => {
  try {
    nhaCungCaps.value = await getAllNhaCungCapList();
    xuatXus.value = await getAllXuatXuList();
    roms.value = await getAllRomList();
    mauSacs.value = await getAllMauSacList();
    rams.value = await getAllRamList();
    heDieuHanhs.value = await getAllHDHList();
    manHinhs.value = await getAllManHinhList();
    pins.value = await getAllPinList();
    cpus.value = await getAllCpuList();
    cameraTruocs.value = await getAllCameraTruocList();
    cameraSaus.value = await getAllCameraSauList();
    loais.value = await getAllLoaiList();

    if (sanPham.sanPhamChiTiets.length === 0) {
      addChiTiet();
    }
  } catch (error) {
    console.error('Lỗi khi lấy dữ liệu từ API:', error);
    errorMessage.value = 'Có lỗi xảy ra khi lấy dữ liệu. Vui lòng thử lại sau.';
  }
});



</script>

<style scoped>
.form-container {
  max-width: 1350px;
  background-color: #f5f5f5;
  padding: 24px;
  border-radius: 8px;
}

.el-form {
  background-color: #b5abab;
  padding: 24px;
  box-shadow: 0 4px 10px rgba(226, 209, 209, 0.1);
}
</style>