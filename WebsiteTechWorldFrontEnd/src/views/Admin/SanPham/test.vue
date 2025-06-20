```vue
<template>
  <el-form :model="sanPham" :rules="rules" ref="sanPhamForm" label-width="120px">
    <h2>Thêm sản phẩm</h2>
    <!-- Thông tin sản phẩm chính -->
    <el-form-item label="Tên sản phẩm" prop="tenSanPham">
      <el-input v-model="sanPham.tenSanPham"></el-input>
    </el-form-item>

    <el-form-item label="Thương hiệu" prop="thuongHieu">
      <el-input v-model="sanPham.thuongHieu"></el-input>
    </el-form-item>

    <el-form-item label="Nhà cung cấp" prop="idNhaCungCap">
      <el-select v-model="sanPham.idNhaCungCap" placeholder="Chọn nhà cung cấp">
        <el-option v-for="ncc in nhaCungCaps" :key="ncc.id" :label="ncc.tenNhaCungCap" :value="ncc.id"></el-option>
      </el-select>
    </el-form-item>

    <el-form-item label="Trạng thái" prop="trangThaiSanPham">
      <el-select v-model="sanPham.trangThaiSanPham" placeholder="Chọn trạng thái">
        <el-option v-for="tt in danhSachTrangThaiSanPham" :key="tt.value" :label="tt.label" :value="tt.value"></el-option>
      </el-select>
    </el-form-item>

    <!-- Chọn thuộc tính để tạo biến thể -->
    <h3>Tạo biến thể sản phẩm</h3>
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="Màu sắc">
          <el-select v-model="selectedMaus" multiple placeholder="Chọn màu sắc">
            <el-option v-for="mau in maus" :key="mau.id" :label="mau.tenMau" :value="mau.id"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="RAM">
          <el-select v-model="selectedRams" multiple placeholder="Chọn RAM">
            <el-option v-for="ram in rams" :key="ram.id" :label="ram.dungLuong" :value="ram.id"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="ROM">
          <el-select v-model="selectedRoms" multiple placeholder="Chọn ROM">
            <el-option v-for="rom in roms" :key="rom.id" :label="rom.dungLuong" :value="rom.id"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
    <el-button type="primary" @click="generateVariants">Tạo biến thể</el-button>

    <!-- Danh sách chi tiết sản phẩm -->
    <h3>Chi tiết sản phẩm</h3>
    <el-row :gutter="20">
      <el-col :span="10">
        <el-card>
          <h4>Danh sách chi tiết</h4>
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
          <el-form-item label="Giá bán" :prop="`sanPhamChiTiets.${selectedChiTiet}.giaBan`"
            :rules="[{ required: true, message: 'Vui lòng nhập giá bán', type: 'number' }]">
            <el-input-number v-model="sanPham.sanPhamChiTiets[selectedChiTiet].giaBan" :min="0" :precision="2"></el-input-number>
          </el-form-item>
          <el-form-item label="IMEI" :prop="`sanPhamChiTiets.${selectedChiTiet}.imeisInput`"
            :rules="[{ required: true, message: 'Vui lòng nhập IMEI' }]">
            <el-input type="textarea" v-model="sanPham.sanPhamChiTiets[selectedChiTiet].imeisInput"
              placeholder="Nhập danh sách IMEI, phân tách bởi dấu phẩy"
              @input="capNhatSoLuong(selectedChiTiet)"></el-input>
            <el-upload :auto-upload="false" :on-change="(file) => handleImeiFileChange(file, selectedChiTiet)"
              accept=".txt,.csv" style="margin-top: 8px;">
              <el-button type="primary">Tải lên file IMEI</el-button>
            </el-upload>
            <div v-if="selectedChiTiet !== null" style="margin-top: 8px;">
              Số lượng IMEI hợp lệ: {{ sanPham.sanPhamChiTiets[selectedChiTiet].soLuong }}
              <span v-if="sanPham.sanPhamChiTiets[selectedChiTiet].invalidImeis?.length" style="color: red;">
                ({{ sanPham.sanPhamChiTiets[selectedChiTiet].invalidImeis.length }} IMEI không hợp lệ: {{
                  sanPham.sanPhamChiTiets[selectedChiTiet].invalidImeis.join(', ') }})
              </span>
            </div>
          </el-form-item>
          <el-form-item label="Hình ảnh">
            <el-upload :file-list="sanPham.sanPhamChiTiets[selectedChiTiet].hinhAnhs"
              :on-change="(file, fileList) => handleFileChange(file, fileList, selectedChiTiet)"
              :on-remove="(file, fileList) => handleFileRemove(file, fileList, selectedChiTiet)" :auto-upload="false"
              accept="image/*" list-type="picture" :limit="5"
              :on-exceed="() => ElMessage.warning('Chỉ được tải lên tối đa 5 ảnh!')">
              <el-button type="primary">Tải lên hình ảnh</el-button>
            </el-upload>
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
import { ElMessage } from 'element-plus';
import { getAllCameraSauList, getAllCameraTruocList, getAllCpuList, getAllHDHList, getAllLoaiList, getAllManHinhList, getAllMauSacList, getAllNhaCungCapList, getAllPinList, getAllRamList, getAllRomList, getAllXuatXuList, postSanPham } from '@/Service/Adminservice/Products/ProductAdminService';
import { debounce } from 'chart.js/helpers';
import axios from 'axios';

export default {
  setup() {
    const sanPham = reactive({
      tenSanPham: '',
      thuongHieu: '',
      idNhaCungCap: '',
      trangThaiSanPham: '',
      sanPhamChiTiets: []
    });

    const rules = {
      tenSanPham: [{ required: true, message: 'Vui lòng nhập tên sản phẩm' }],
      thuongHieu: [{ required: true, message: 'Vui lòng nhập thương hiệu' }],
      idNhaCungCap: [{ required: true, message: 'Vui lòng chọn nhà cung cấp' }],
      trangThaiSanPham: [{ required: true, message: 'Vui lòng chọn trạng thái' }]
    };

    const sanPhamForm = ref(null);
    const selectedMaus = ref([]);
    const selectedRams = ref([]);
    const selectedRoms = ref([]);
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

    const danhSachTrangThaiSanPham = [
      { label: "Đang kinh doanh", value: "ACTIVE" },
      { label: "Ngừng kinh doanh", value: "DISCONTINUED" },
      { label: "Sắp ra mắt", value: "COMING_SOON" },
      { label: "Tạm ngừng bán", value: "TEMPORARILY_UNAVAILABLE" },
      { label: "Hết hàng", value: "OUT_OF_STOCK" }
    ];

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

    const generateVariants = () => {
      sanPham.sanPhamChiTiets = [];
      selectedMaus.value.forEach(mau => {
        selectedRams.value.forEach(ram => {
          selectedRoms.value.forEach(rom => {
            sanPham.sanPhamChiTiets.push({
              idMau: mau,
              idRam: ram,
              idRom: rom,
              idManHinh: manHinhs.value[0]?.id || null,
              idHeDieuHanh: heDieuHanhs.value[0]?.id || null,
              idPin: pins.value[0]?.id || null,
              idCpu: cpus.value[0]?.id || null,
              idCameraTruoc: cameraTruocs.value[0]?.id || null,
              idCameraSau: cameraSaus.value[0]?.id || null,
              idXuatXu: xuatXus.value[0]?.id || null,
              idLoai: loais.value[0]?.id || null,
              soLuong: 0,
              giaBan: 0,
              hinhAnhs: [],
              imeisInput: '',
              invalidImeis: []
            });
          });
        });
      });
      selectedChiTiet.value = sanPham.sanPhamChiTiets.length > 0 ? 0 : null;
      ElMessage.success(`Đã tạo ${sanPham.sanPhamChiTiets.length} biến thể sản phẩm`);
    };

    const validateIMEI = (imei) => /^\d{15}$/.test(imei);

    const capNhatSoLuong = debounce((index) => {
      const imeis = sanPham.sanPhamChiTiets[index].imeisInput
        .split(',')
        .map(i => i.trim())
        .filter(i => i);
      const validImeis = imeis.filter(validateIMEI);
      sanPham.sanPhamChiTiets[index].soLuong = validImeis.length;
      sanPham.sanPhamChiTiets[index].invalidImeis = imeis.filter(i => !validateIMEI(i));
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
          sanPham.sanPhamChiTiets[index].imeisInput = imeis.join(', ');
          capNhatSoLuong(index);
          ElMessage.success(`Đã tải lên ${imeis.length} IMEI từ file ${file.name}`);
        };
        reader.onerror = () => {
          throw new Error('Lỗi khi đọc file');
        };
        reader.readAsText(file.raw);
      } catch (error) {
        ElMessage.error('Lỗi khi xử lý file IMEI: ' + error.message);
      }
    };

    const removeChiTiet = (index) => {
      sanPham.sanPhamChiTiets.splice(index, 1);
      if (selectedChiTiet.value === index) {
        selectedChiTiet.value = sanPham.sanPhamChiTiets.length > 0 ? 0 : null;
      } else if (selectedChiTiet.value > index) {
        selectedChiTiet.value--;
      }
    };

    const selectChiTiet = (row, column, event) => {
      const index = sanPham.sanPhamChiTiets.indexOf(row);
      selectedChiTiet.value = index;
    };

    const getMauSacLabels = (idMau) => {
      if (!Array.isArray(idMau)) idMau = [idMau];
      return idMau.map(id => maus.value.find(m => String(m.id) === String(id))?.tenMau || '').join(', ');
    };

    const getRamLabels = (idRam) => {
      if (!Array.isArray(idRam)) idRam = [idRam];
      return idRam.map(id => rams.value.find(r => String(r.id) === String(id))?.dungLuong || '').join(', ');
    };

    const getRomLabels = (idRom) => {
      if (!Array.isArray(idRom)) idRom = [idRom];
      return idRom.map(id => roms.value.find(r => String(r.id) === String(id))?.dungLuong || '').join(', ');
    };

    const handleFileChange = async (file, fileList, index) => {
      try {
        const formData = new FormData();
        formData.append('file', file.raw);
        const response = await axios.post('http://localhost:8080/admin/hinhAnh/upload', formData, {
          headers: { 'Content-Type': 'multipart/form-data' }
        });
        sanPham.sanPhamChiTiets[index].hinhAnhs.push({
          name: file.name,
          url: response.data.url,
          imagePublicId: response.data.imagePublicId
        });
        ElMessage.success(`Tải ảnh ${file.name} thành công!`);
      } catch (error) {
        ElMessage.error('Lỗi khi tải ảnh: ' + (error.response?.data?.message || error.message));
      }
    };

    const handleFileRemove = (file, fileList, index) => {
      sanPham.sanPhamChiTiets[index].hinhAnhs = fileList.filter(f => f.url);
      ElMessage.success(`Đã xóa ảnh ${file.name}`);
    };

    const submitForm = async () => {
      try {
        await sanPhamForm.value.validate();
        const payload = {
          tenSanPham: sanPham.tenSanPham,
          thuongHieu: sanPham.thuongHieu,
          idNhaCungCap: sanPham.idNhaCungCap,
          trangThaiSanPham: sanPham.trangThaiSanPham,
          sanPhamChiTiets: sanPham.sanPhamChiTiets.map((chiTiet) => {
            const imeis = chiTiet.imeisInput
              .split(',')
              .map(i => i.trim())
              .filter(i => i);
            return {
              idMau: chiTiet.idMau,
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
              imeis: imeis.map(i => ({ soImei: i })),
              hinhAnhs: chiTiet.hinhAnhs.map(h => ({
                url: h.url,
                imagePublicId: h.imagePublicId
              }))
            };
          })
        };

        console.log(JSON.stringify(payload, null, 2));
        await postSanPham(payload);
        ElMessage.success('Lưu sản phẩm thành công!');

        // Reset form
        sanPham.tenSanPham = '';
        sanPham.thuongHieu = '';
        sanPham.idNhaCungCap = '';
        sanPham.trangThaiSanPham = '';
        sanPham.sanPhamChiTiets = [];
        selectedChiTiet.value = null;
      } catch (error) {
        ElMessage.error('Lỗi khi lưu sản phẩm: ' + (error.response?.data?.message || error.message));
      }
    };

    onMounted(() => {
      fetchDanhMuc();
    });

    return {
      sanPham,
      rules,
      sanPhamForm,
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
      selectedMaus,
      selectedRams,
      selectedRoms,
      danhSachTrangThaiSanPham,
      generateVariants,
      removeChiTiet,
      selectChiTiet,
      getMauSacLabels,
      getRamLabels,
      getRomLabels,
      handleFileChange,
      handleFileRemove,
      submitForm,
      capNhatSoLuong,
      handleImeiFileChange
    };
  }
};
</script>

<style scoped>
.el-table {
  cursor: pointer;
}
</style>
