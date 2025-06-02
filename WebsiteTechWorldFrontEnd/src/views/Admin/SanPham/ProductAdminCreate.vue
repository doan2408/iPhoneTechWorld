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

        <el-table-column label="Màu sắc">
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

        <el-table-column prop="soLuong" label="Số lượng" />
        <el-table-column prop="giaBan" label="Đơn giá">
          <template #default="{ row }">
            <el-input v-model="row.giaBan" placeholder="Nhập giá trị" />
          </template>
        </el-table-column>

        <el-table-column label="Thao tác" width="500">
          <template #default="{ row, $index }">
            <div style="display: flex; align-items: center; gap: 10px;">
              <input type="file" :ref="'fileInput_' + $index"
                style="padding: 6px 12px; border: 1px solid #ccc; border-radius: 4px; cursor: pointer;"
                @change="(event) => handleFileChange(event, row)" />

              <el-button type="danger" @click="uploadFile(row)">
                Upload
              </el-button>

              <el-button type="danger" @click="nhapHang(row)">
                Nhập
              </el-button>
            </div>
          </template>
        </el-table-column>

      </el-table>
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

const sanPham = reactive({
  id: null,
  tenSanPham: '',
  thuongHieu: '',
  idNhaCungCap: [],
  sanPhamChiTiets: []
});

const addChiTiet = () => {
  sanPham.sanPhamChiTiets.push({
    idSanPham: null,
    idMau: [],
    idRam: [],
    idRom: [],
    idManHinh: [],
    idHeDieuHanh: [],
    idPin: [],
    idCpu: [],
    idCameraTruoc: [],
    idCameraSau: [],
    idXuatXu: [],
    idLoai: [],
    hinhAnhs: [],
    imeis: [],
    soLuong: 0,
    giaBan: 0,
  });
};


const createSanPham = async () => {
  try {
    if (!sanPham.tenSanPham.trim()) {
      alert('Tên sản phẩm không được để trống');
      return;
    }
    const response = await axios.post(sanPham);
    console.log('Dữ liệu đã được lưu:', response.data);
    ElMessage.success('Tạo sản phẩm thành công!');
    sanPham.tenSanPham = '';
    sanPham.thuongHieu = '';
    sanPham.idNhaCungCap = [];
    sanPham.sanPhamChiTiets = [];
  } catch (error) {
    ElMessage.error('Tạo sản phẩm thất bại!');
    console.error(error);
  }
};

const addNCCDialog = ref(null);

function openDialog() {
  addNCCDialog.value?.open();
}

function handleNhaCungCapAdded(newNCC) {
  nhaCungCaps.value.push(newNCC);
}

const dialogVisible = ref(false);
const handleSelectClick = () => {
  dialogVisible.value = true;
};


const handleClick = () => {
  alert('Bạn cần implement thêm thương hiệu mới');
};



const hienThiBang = ref(false);

const saveForm = async () => {
  if (!sanPham.tenSanPham.trim()) {
    alert('Tên sản phẩm không được để trống');
    return;
  }
  if (sanPham.sanPhamChiTiets.length === 0) {
    alert('Bạn cần thêm ít nhất một cấu hình chi tiết');
    return;
  }

  try {
    // In dữ liệu ra console để kiểm tra
    console.log('Dữ liệu gửi đi:', JSON.stringify(sanPham, null, 2));

    // Gửi dữ liệu đến server (thay URL bằng endpoint thực tế của bạn)
    const response = await axios.post(sanPham);
    console.log('Dữ liệu đã được lưu:', response.data);

    // Hiển thị thông báo thành công
    alert('Lưu thành công!');
    hienThiBang.value = true;
  } catch (error) {
    console.error('Lỗi khi lưu dữ liệu:', error);
    alert('Lưu thất bại. Vui lòng thử lại.');
  }
};

watch(
  () => [sanPham.tenSanPham, sanPham.sanPhamChiTiets],
  ([ten, chiTiet]) => {
    const tenHopLe = ten.trim().length > 0;
    const chiTietHopLe = Array.isArray(chiTiet) && chiTiet.length > 0;
    hienThiBang.value = tenHopLe && chiTietHopLe;
  },
  { deep: true }
);

// Hàm lấy tên màu từ ID
const layTenMauSac = (id) => {
  const mau = mauSacs.value.find((m) => m.id === id);
  return mau ? mau.tenMau : 'Không rõ';
};

// Hàm lấy màu dạng hex
const layMauSac = (id) => {
  return mapMauSac.value.get(id) || '#000000';
};

// Tạo bản đồ mã màu
const mapMauSac = computed(() => {
  const map = new Map();
  for (const mau of mauSacs.value) {
    map.set(mau.id, mau.hexColor || '#000000');
  }
  return map;
});

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

// Ref để lưu danh sách phiên bản (nhóm theo màu sắc)
const danhSachPhienBan = ref([]);

// Theo dõi thay đổi của chi tiết sản phẩm để cập nhật phiên bản hiển thị
watch(
  () => sanPham.sanPhamChiTiets,
  (chiTiets) => {
    const groupByRamRom = {};

    chiTiets.forEach((chiTiet) => {
      const maus = chiTiet.idMau || [];
      const rams = chiTiet.idRam || [];
      const roms = chiTiet.idRom || [];

      if (maus.length === 0 || rams.length === 0 || roms.length === 0) {
        console.warn('Thiếu dữ liệu màu sắc, RAM hoặc ROM trong chi tiết:', chiTiet);
        return;
      }

      rams.forEach((idRam) => {
        roms.forEach((idRom) => {
          // Tạo key nhóm theo RAM-ROM
          const key = `${idRam}-${idRom}`;

          if (!groupByRamRom[key]) {
            groupByRamRom[key] = {
              idRam: idRam,
              idRom: idRom,
              tenPhienBan: `${layTenRam(idRam)} / ${layTenRom(idRom)}`, // tên nhóm theo RAM-ROM
              chiTiet: [],  // danh sách các màu cho tổ hợp RAM-ROM này
            };
          }

          maus.forEach((idMau) => {
            groupByRamRom[key].chiTiet.push({
              idMau: idMau,
              hexColor: layMauSac(idMau),
              tenMauSac: layTenMauSac(idMau),
              soLuong: chiTiet.soLuong,
              giaBan: chiTiet.giaBan,
              tenSanPham: sanPham.tenSanPham,
            });
          });
        });
      });
    });

    // Chuyển thành mảng để render
    danhSachPhienBan.value = Object.values(groupByRamRom);
  },
  { deep: true, immediate: true }
);


const handleFileChange = (file, row) => {
  console.log('File uploaded for row:', file, row);
};

const nhapHang = (row) => {
  console.log('Nhập hàng cho row:', row);
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