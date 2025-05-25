<template>
  <h1 style="text-align: center; font-size: 32px; margin-bottom: 20px;">Thêm sản phẩm</h1>
  <el-form :model="sanPham" label-width="300px">
    <el-form-item label="Tên sản phẩm">
      <el-input v-model="sanPham.tenSanPham" placeholder="Nhập tên sản phẩm" />
    </el-form-item>

    <el-form-item label="Thương hiệu" class="thuong-hieu-item">
      <el-input v-model="sanPham.thuongHieu" placeholder="Nhập tên thương hiệu" />
    </el-form-item>

    <el-form-item label="Nhà cung cấp">
      <el-select v-model="sanPham.idNhaCungCap" placeholder="Chọn nhà cung cấp" multiple>
        <el-option v-for="ncc in nhaCungCaps" :key="ncc.id" :label="ncc.tenNhaCungCap" :value="ncc.id" />
      </el-select>
    </el-form-item>

    <h3>Chi tiết sản phẩm</h3>
    <div v-for="chiTiet in sanPham.sanPhamChiTiets" :key="chiTiet.id">

      <el-form-item label="Màu sắc">
        <el-select v-model="chiTiet.idMau" placeholder="Chọn màu sắc" multiple>
          <el-option v-for="mau in mauSacs" :key="mau.id" :label="mau.tenMau" :value="mau.id" />
        </el-select>
      </el-form-item>

      <el-form-item label="Ram">
        <el-select v-model="chiTiet.idRam" placeholder="Chọn ram" multiple>
          <el-option v-for="ram in rams" :key="ram.id" :label="ram.dungLuong" :value="ram.id" />
        </el-select>
      </el-form-item>

      <el-form-item label="Rom">
        <el-select v-model="chiTiet.idRom" placeholder="Chọn rom" multiple>
          <el-option v-for="rom in roms" :key="rom.id" :label="rom.dungLuong" :value="rom.id" />
        </el-select>
      </el-form-item>

      <el-form-item label="Kích thước màn hình">
        <el-select v-model="chiTiet.idManHinh" placeholder="Chọn màn hình" multiple>
          <el-option v-for="manhinh in manHinhs" :key="manhinh.id" :label="manhinh.kichThuoc" :value="manhinh.id" />
        </el-select>
      </el-form-item>

      <el-form-item label="Hệ điều hành">
        <el-select v-model="chiTiet.idHeDieuHanh" placeholder="Chọn hệ điều hành" multiple>
          <el-option v-for="hdh in heDieuHanhs" :key="hdh.id" :label="hdh.phienBan" :value="hdh.id" />
        </el-select>
      </el-form-item>

      <el-form-item label="Pin">
        <el-select v-model="chiTiet.idPin" placeholder="Chọn pin" multiple>
          <el-option v-for="pin in pins" :key="pin.id" :label="pin.congSuatSac" :value="pin.id" />
        </el-select>
      </el-form-item>

      <el-form-item label="Cpu">
        <el-select v-model="chiTiet.idCpu" placeholder="Chọn cpu" multiple>
          <el-option v-for="cpu in cpus" :key="cpu.idCpu" :label="cpu.xungNhip" :value="cpu.id"/>
        </el-select>
      </el-form-item>

      <el-form-item label="Camera trước">
        <el-select v-model="chiTiet.idCameraTruoc" placeholder="Chọn camera trước" multiple>
          <el-option v-for="cameraTruoc in cameraTruocs" :key="cameraTruoc.id" :label="cameraTruoc.doPhanGiai" :value="cameraTruoc.id"/>
        </el-select>
      </el-form-item>

      <el-form-item label="Camera sau">
        <el-select v-model="chiTiet.idCameraSau" placeholder="Chọn camera sau" multiple>
          <el-option v-for="cameraSau in cameraSaus" :key="cameraSau.id" :label="cameraSau.doPhanGiai" :value="cameraSau.id"/>
        </el-select>
      </el-form-item>

      <el-form-item label="Xuất xứ">
        <el-select v-model="chiTiet.idXuatXu" placeholder="Chọn xuất xứ" multiple>
          <el-option v-for="xx in xuatXus" :key="xx.id" :label="xx.maXuatXu" :value="xx.id"/>
        </el-select>
      </el-form-item>

      <el-form-item label="Loại">
        <el-select v-model="chiTiet.idLoai" placeholder="Chọn loại" multiple>
          <el-option v-for="loai in loais" :key="loai.id" :label="loai.tenLoai" :value="loai.id"/>
        </el-select>
      </el-form-item>

    </div>

    <div style="display: flex; justify-content: center; margin-top: 20px;">
      <el-button type="success" @click="saveForm">Lưu sản phẩm</el-button>
    </div>

  </el-form>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue';
import axios from 'axios';
import { getAllCameraSau, getAllCameraTruoc, getAllCpu, getAllHeDieuHanh, getAllLoai, getAllManHinh, getAllMauSac, getAllNhaCungCap, getAllPin, getAllRam, getAllRom, getAllXuatXu } from '@/Service/Adminservice/Products/ProductAdminService';

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


const sanPham = reactive({
  tenSanPham: '',
  thuongHieu: '',
  idNhaCungCap: [],
  sanPhamChiTiets: []
});

const addChiTiet = () => {
  sanPham.sanPhamChiTiets.push({
    id: null,
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


onMounted(async () => {
  nhaCungCaps.value = await getAllNhaCungCap();
  mauSacs.value = await getAllMauSac();
  rams.value = await getAllRam();
  roms.value = await getAllRom();
  heDieuHanhs.value = await getAllHeDieuHanh();
  manHinhs.value = await getAllManHinh();
  pins.value = await getAllPin();
  cpus.value = await getAllCpu();
  cameraTruocs.value = await getAllCameraTruoc();
  cameraSaus.value = await getAllCameraSau();
  xuatXus.value = await getAllXuatXu();
  loais.value = await getAllLoai();

  console.log(nhaCungCaps.value);
  // Khởi tạo ít nhất 1 chi tiết để hiện form
  if (sanPham.sanPhamChiTiets.length === 0) {
    addChiTiet();
  }
})

const saveForm = () => {
  console.log('Dữ liệu gửi đi:', JSON.stringify(sanPham, null, 2));
}


</script>


<style scoped>
.el-form {
  max-width: 1350px;
  margin-left: 220px;
  background-color: #b5abab;
  padding: 24px;
  box-shadow: 0 4px 10px rgba(226, 209, 209, 0.1);
}
</style>