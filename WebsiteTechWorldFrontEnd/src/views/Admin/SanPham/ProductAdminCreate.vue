<template>
  <el-form :model="sanPham" ref="sanPhamForm" label-width="120px">
    <h2>Thêm sản phẩm</h2>
    <!-- Thông tin sản phẩm chính -->
    <el-form-item label="Tên sản phẩm" prop="tenSanPham"
      :rules="[{ required: true, message: 'Vui lòng nhập tên sản phẩm' }]">
      <el-input v-model="sanPham.tenSanPham"></el-input>
    </el-form-item>
    <el-form-item label="Thương hiệu" prop="thuongHieu"
      :rules="[{ required: true, message: 'Vui lòng nhập thương hiệu' }]">
      <el-input v-model="sanPham.thuongHieu"></el-input>
    </el-form-item>
    <el-form-item label="Nhà cung cấp" prop="idNhaCungCap"
      :rules="[{ required: true, message: 'Vui lòng chọn nhà cung cấp' }]">
      <el-select v-model="sanPham.idNhaCungCap" placeholder="Chọn nhà cung cấp">
        <el-option v-for="ncc in nhaCungCaps" :key="ncc.id" :label="ncc.tenNhaCungCap" :value="ncc.id"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="Trạng thái" prop="trangThaiSanPham"
      :rules="[{ required: true, message: 'Vui lòng chọn trạng thái' }]">
      <el-select v-model="sanPham.trangThaiSanPham" placeholder="Chọn trạng thái">
        <el-option v-for="tt in danhSachTrangThaiSanPham" :key="tt.value" :label="tt.label" :value="tt.value" />
      </el-select>
    </el-form-item>

    <!-- Quản lý chi tiết sản phẩm -->
    <h3>Chi tiết sản phẩm</h3>
    <el-row :gutter="20">
      <el-col :span="10">
        <el-card>
          <h4>Danh sách chi tiết</h4>
          <el-button type="primary" @click="addChiTiet">Thêm chi tiết mới</el-button>
          <el-table :data="sanPham.sanPhamChiTiets" style="margin-top: 10px" @row-click="selectChiTiet">
            <el-table-column label="Chi tiết" type="index" width="80"></el-table-column>
            <el-table-column label="Màu sắc">
              <template #default="{ row }">
                {{ getMauSacLabels(row.idMau) }}
              </template>
            </el-table-column>
            <el-table-column label="RAM">
              <template #default="{ row }">
                {{ getRamLabels(row.idRam) }}
              </template>
            </el-table-column>
            <el-table-column label="ROM">
              <template #default="{ row }">
                {{ getRomLabels(row.idRom) }}
              </template>
            </el-table-column>
            <el-table-column label="Hành động" width="100">
              <template #default="{ $index }">
                <el-button type="danger" size="small" @click.stop="removeChiTiet($index)">Xóa</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card v-if="selectedChiTiet !== null">
          <h4>Thông tin chi tiết {{ selectedChiTiet + 1 }}</h4>
          <el-form-item label="Màu sắc" :prop="`sanPhamChiTiets.${selectedChiTiet}.idMau`"
            :rules="[{ required: true, message: 'Vui lòng chọn màu sắc' }]">
            <el-select v-model="sanPham.sanPhamChiTiets[selectedChiTiet].idMau" placeholder="Chọn màu sắc">
              <el-option v-for="mau in maus" :key="mau.id" :label="mau.tenMau" :value="mau.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="RAM" :prop="`sanPhamChiTiets.${selectedChiTiet}.idRam`"
            :rules="[{ required: true, message: 'Vui lòng chọn RAM' }]">
            <el-select v-model="sanPham.sanPhamChiTiets[selectedChiTiet].idRam" placeholder="Chọn RAM">
              <el-option v-for="ram in rams" :key="ram.id" :label="ram.dungLuong" :value="ram.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="ROM" :prop="`sanPhamChiTiets.${selectedChiTiet}.idRom`"
            :rules="[{ required: true, message: 'Vui lòng chọn ROM' }]">
            <el-select v-model="sanPham.sanPhamChiTiets[selectedChiTiet].idRom" placeholder="Chọn ROM">
              <el-option v-for="rom in roms" :key="rom.id" :label="rom.dungLuong" :value="rom.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Màn hình" :prop="`sanPhamChiTiets.${selectedChiTiet}.idManHinh`"
            :rules="[{ required: true, message: 'Vui lòng chọn màn hình' }]">
            <el-select v-model="sanPham.sanPhamChiTiets[selectedChiTiet].idManHinh" placeholder="Chọn màn hình">
              <el-option v-for="mh in manHinhs" :key="mh.id" :label="mh.kichThuoc" :value="mh.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Hệ điều hành" :prop="`sanPhamChiTiets.${selectedChiTiet}.idHeDieuHanh`"
            :rules="[{ required: true, message: 'Vui lòng chọn hệ điều hành' }]">
            <el-select v-model="sanPham.sanPhamChiTiets[selectedChiTiet].idHeDieuHanh" placeholder="Chọn hệ điều hành">
              <el-option v-for="hdh in heDieuHanhs" :key="hdh.id" :label="hdh.phienBan" :value="hdh.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Pin" :prop="`sanPhamChiTiets.${selectedChiTiet}.idPin`"
            :rules="[{ required: true, message: 'Vui lòng chọn pin' }]">
            <el-select v-model="sanPham.sanPhamChiTiets[selectedChiTiet].idPin" placeholder="Chọn pin">
              <el-option v-for="pin in pins" :key="pin.id" :label="pin.congSuatSac" :value="pin.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="CPU" :prop="`sanPhamChiTiets.${selectedChiTiet}.idCpu`"
            :rules="[{ required: true, message: 'Vui lòng chọn CPU' }]">
            <el-select v-model="sanPham.sanPhamChiTiets[selectedChiTiet].idCpu" placeholder="Chọn CPU">
              <el-option v-for="cpu in cpus" :key="cpu.id" :label="cpu.xungNhip" :value="cpu.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Camera trước" :prop="`sanPhamChiTiets.${selectedChiTiet}.idCameraTruoc`"
            :rules="[{ required: true, message: 'Vui lòng chọn camera trước' }]">
            <el-select v-model="sanPham.sanPhamChiTiets[selectedChiTiet].idCameraTruoc" placeholder="Chọn camera trước">
              <el-option v-for="cam in cameraTruocs" :key="cam.id" :label="cam.doPhanGiai" :value="cam.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Camera sau" :prop="`sanPhamChiTiets.${selectedChiTiet}.idCameraSau`"
            :rules="[{ required: true, message: 'Vui lòng chọn camera sau' }]">
            <el-select v-model="sanPham.sanPhamChiTiets[selectedChiTiet].idCameraSau" placeholder="Chọn camera sau">
              <el-option v-for="cam in cameraSaus" :key="cam.id" :label="cam.doPhanGiai" :value="cam.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Xuất xứ" :prop="`sanPhamChiTiets.${selectedChiTiet}.idXuatXu`"
            :rules="[{ required: true, message: 'Vui lòng chọn xuất xứ' }]">
            <el-select v-model="sanPham.sanPhamChiTiets[selectedChiTiet].idXuatXu" placeholder="Chọn xuất xứ">
              <el-option v-for="xx in xuatXus" :key="xx.id" :label="xx.maXuatXu" :value="xx.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Loại" :prop="`sanPhamChiTiets.${selectedChiTiet}.idLoai`"
            :rules="[{ required: true, message: 'Vui lòng chọn loại' }]">
            <el-select v-model="sanPham.sanPhamChiTiets[selectedChiTiet].idLoai" placeholder="Chọn loại">
              <el-option v-for="loai in loais" :key="loai.id" :label="loai.tenLoai" :value="loai.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Số lượng">
            <el-input-number v-model="sanPham.sanPhamChiTiets[selectedChiTiet].soLuong" :min="0" :disabled="true" />
          </el-form-item>

          <el-form-item label="Giá bán" :prop="`sanPhamChiTiets.${selectedChiTiet}.giaBan`"
            :rules="[{ required: true, message: 'Vui lòng nhập giá bán', type: 'number' }]">
            <el-input-number v-model="sanPham.sanPhamChiTiets[selectedChiTiet].giaBan" :min="0"
              :precision="2"></el-input-number>
          </el-form-item>
          <el-form-item label="Hình ảnh">
            <el-upload :file-list="sanPham.sanPhamChiTiets[selectedChiTiet].hinhAnhs"
              :on-change="(file, fileList) => handleFileChange(file, fileList, selectedChiTiet)"
              :on-remove="(file, fileList) => handleFileRemove(file, fileList, selectedChiTiet)" :auto-upload="false"
              accept="image/*" list-type="picture">
              <el-button type="primary">Tải lên hình ảnh</el-button>
            </el-upload>
          </el-form-item>
          <el-form-item label="IMEI" :prop="`sanPhamChiTiets.${selectedChiTiet}.imeisInput`"
            :rules="[{ required: true, message: 'Vui lòng nhập IMEI' }]">
            <el-input type="textarea" v-model="sanPham.sanPhamChiTiets[selectedChiTiet].imeisInput"
              placeholder="Nhập danh sách IMEI, phân tách bởi dấu phẩy"
              @input="capNhatSoLuong(selectedChiTiet)"></el-input>
          </el-form-item>
        </el-card>
        <el-card v-else>
          <h4>Vui lòng chọn hoặc thêm chi tiết sản phẩm</h4>
        </el-card>
      </el-col>
    </el-row>

    <el-button type="success" @click="submitForm">Lưu sản phẩm</el-button>
  </el-form>
</template>

<script>
import { onMounted, reactive, ref } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';
import { getAllCameraSauList, getAllCameraTruocList, getAllCpuList, getAllHDHList, getAllLoaiList, getAllManHinhList, getAllMauSacList, getAllNhaCungCapList, getAllPinList, getAllRamList, getAllRomList, getAllXuatXuList, postSanPham } from '@/Service/Adminservice/Products/ProductAdminService';

export default {
  setup() {
    const sanPham = reactive({
      tenSanPham: '',
      thuongHieu: '',
      idNhaCungCap: '',
      trangThaiSanPham: '',
      sanPhamChiTiets: []
    });

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
    const selectedChiTiet = ref(null);

    // Lấy danh sách danh mục từ API
    const fetchDanhMuc = async () => {
      console.log('✅ Hàm fetchDanhMuc đã được gọi');
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
          getAllLoaiList()
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
      } catch (error) {
        ElMessage.error('Lỗi khi lấy danh mục: ' + error.message);
      }
    };

    // Thêm chi tiết sản phẩm
    const addChiTiet = () => {
      sanPham.sanPhamChiTiets.push({
        idMau: null,
        idRam: null,
        idRom: null,
        idManHinh: null,
        idHeDieuHanh: null,
        idPin: null,
        idCpu: null,
        idCameraTruoc: null,
        idCameraSau: null,
        idXuatXu: null,
        idLoai: null,
        soLuong: 0,
        giaBan: 0,
        hinhAnhs: [],
        imeisInput: ''
      });
      selectedChiTiet.value = sanPham.sanPhamChiTiets.length - 1; // Chọn chi tiết mới thêm
    };

    const danhSachTrangThaiSanPham = [
      { label: "Đang kinh doanh", value: "ACTIVE" },
      { label: "Ngừng kinh doanh", value: "DISCONTINUED" },
      { label: "Sắp ra mắt", value: "COMING_SOON" },
      { label: "Tạm ngừng bán", value: "TEMPORARILY_UNAVAILABLE" },
      { label: "Hết hàng", value: "OUT_OF_STOCK" }
    ];


    // Trong phần setup, thêm phương thức capNhatSoLuong
    const capNhatSoLuong = (index) => {
      const imeisInput = sanPham.sanPhamChiTiets[index].imeisInput;
      // Tách chuỗi IMEI bằng dấu phẩy, làm sạch và lọc bỏ mục rỗng
      const imeisArray = imeisInput
        .split(',')
        .map(item => item.trim())
        .filter(item => item !== '');
      // Cập nhật số lượng
      sanPham.sanPhamChiTiets[index].soLuong = imeisArray.length;
    };

    // Xóa chi tiết sản phẩm
    const removeChiTiet = (index) => {
      sanPham.sanPhamChiTiets.splice(index, 1);
      if (selectedChiTiet.value === index) {
        selectedChiTiet.value = sanPham.sanPhamChiTiets.length > 0 ? 0 : null;
      } else if (selectedChiTiet.value > index) {
        selectedChiTiet.value--;
      }
    };

    // Chọn chi tiết sản phẩm
    const selectChiTiet = (row, column, event) => {
      const index = sanPham.sanPhamChiTiets.indexOf(row);
      selectedChiTiet.value = index;
    };

    // Lấy nhãn màu sắc
    const getMauSacLabels = (idMau) => {
      if (!Array.isArray(idMau)) idMau = [idMau];
      const labels = idMau.map(id => {
        const found = maus.value.find(m => String(m.id) === String(id));
        return found?.tenMau || '';
      }).join(', ');
      return labels;
    };

    // Lấy nhãn RAM
    const getRamLabels = (idRam) => {
      if (!Array.isArray(idRam)) idRam = [idRam];
      const labels = idRam.map(id => {
        const found = rams.value.find(r => String(r.id) === String(id));
        return found?.dungLuong || '';
      }).join(', ');

      return labels;
    };

    const getRomLabels = (idRom) => {
      if (!Array.isArray(idRom)) idRom = [idRom];
      const labels = idRom.map(id => {
        const found = roms.value.find(r => String(r.id) === String(id));
        return found?.dungLuong || '';
      }).join(', ');
      return labels;
    };


    const handleFileChange = async (file, fileList, index) => {
      try {
        if (!file.raw) throw new Error('File không hợp lệ');

        const formData = new FormData();
        formData.append('file', file.raw);

        const response = await axios.post('http://localhost:8080/admin/hinhAnh/upload', formData, {
          headers: { 'Content-Type': 'multipart/form-data' }
        });

        if (!response.data.url) throw new Error('Không nhận được URL ảnh từ server');

        file.url = response.data.url; // Gán trực tiếp URL vào file
        ElMessage.success(`Tải ảnh ${file.name} thành công!`);
      } catch (error) {
        ElMessage.error('Lỗi khi tải ảnh: ' + (error.response?.data?.message || error.message));
      }
    };


    const handleFileRemove = (file, fileList, index) => {
      sanPham.sanPhamChiTiets[index].hinhAnhs = fileList;
    };

    // Gửi form
    const submitForm = async () => {
      try {
        // Chuẩn bị bản sao để xử lý JSON đúng định dạng
        const payload = {
          tenSanPham: sanPham.tenSanPham,
          thuongHieu: sanPham.thuongHieu,
          idNhaCungCap: sanPham.idNhaCungCap,
          trangThaiSanPham: sanPham.trangThaiSanPham,
          sanPhamChiTiets: sanPham.sanPhamChiTiets.map((chiTiet) => ({
            idMau: chiTiet.idMau,         // hoặc là chuỗi hoặc số nếu không dùng mảng nữa
            idRam: chiTiet.idRam,
            idRom: chiTiet.idRom,
            idManHinh: chiTiet.idManHinh,
            idHeDieuHanh: chiTiet.idHeDieuHanh,
            idPin: chiTiet.idPin,
            idCpu: chiTiet.idCpu,
            idCameraTruoc: chiTiet.idCameraTruoc,
            idCameraSau: chiTiet.idCameraSau,
            idXuatXu: chiTiet.idXuatXu,
            idLoai: chiTiet.idLoai,
            soLuong: chiTiet.soLuong,
            giaBan: chiTiet.giaBan,
            imeis: chiTiet.imeisInput
              .split(',')
              .map(i => i.trim())
              .filter(i => i)
              .map(i => ({ soImei: i })),
            hinhAnhs: chiTiet.hinhAnhs.map((h) => h.url) // hoặc chuỗi base64
          }))
        };

        await postSanPham(payload); // <-- gửi JSON

        ElMessage.success('Lưu sản phẩm thành công!');

        // Reset form
        sanPham.tenSanPham = '';
        sanPham.thuongHieu = '';
        sanPham.idNhaCungCap = '';
        sanPham.trangThaiSanPham = '';
        sanPham.sanPhamChiTiets = [];
        selectedChiTiet.value = null;
      } catch (error) {
        ElMessage.error('Lỗi khi lưu sản phẩm: ' + error.message);
      }
    };


    onMounted(() => {
      fetchDanhMuc();
    });

    return {
      sanPham,
      nhaCungCaps,
      maus,
      rams,
      roms,
      manHinhs,
      heDieuHanhs,
      pins,
      cpus,
      cameraTruocs,
      cameraSaus,
      xuatXus,
      loais,
      selectedChiTiet,
      danhSachTrangThaiSanPham,
      addChiTiet,
      removeChiTiet,
      selectChiTiet,
      getMauSacLabels,
      getRamLabels,
      getRomLabels,
      handleFileChange,
      handleFileRemove,
      submitForm,
      capNhatSoLuong
    };
  }
};
</script>

<style scoped>
.chi-tiet-container {
  margin-bottom: 20px;
}

.el-table {
  cursor: pointer;
}
</style>