```vue
<template>
  <el-form :model="sanPham" ref="sanPhamForm" label-width="120px">
    <h2>Thêm sản phẩm</h2>
    <!-- Thông tin sản phẩm chính -->
    <el-form-item label="Tên sản phẩm" :error="errors.tenSanPham">
      <el-input v-model="sanPham.tenSanPham" @input="errors.tenSanPham = ''"></el-input>
    </el-form-item>

    <el-form-item label="Thương hiệu" :error="errors.thuongHieu">
      <el-input v-model="sanPham.thuongHieu" @input="errors.thuongHieu = ''"></el-input>
    </el-form-item>

    <el-form-item label="Nhà cung cấp" :error="errors.idNhaCungCap">
      <el-select v-model="sanPham.idNhaCungCap" placeholder="Chọn nhà cung cấp" @change="errors.idNhaCungCap = ''">
        <el-option v-for="ncc in nhaCungCaps" :key="ncc.id" :label="ncc.tenNhaCungCap" :value="ncc.id"></el-option>
      </el-select>
    </el-form-item>

    <el-form-item label="Trạng thái" :error="errors.trangThaiSanPham">
      <el-select v-model="sanPham.trangThaiSanPham" placeholder="Chọn trạng thái"
        @change="errors.trangThaiSanPham = ''">
        <el-option v-for="tt in danhSachTrangThaiSanPham" :key="tt.value" :label="tt.label"
          :value="tt.value"></el-option>
      </el-select>
    </el-form-item>

    <!-- Chọn thuộc tính để tạo biến thể -->
    <h3>Tạo biến thể sản phẩm</h3>
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="Màu sắc" :error="errors.selectedMaus">
          <el-select v-model="selectedMaus" multiple placeholder="Chọn màu sắc" @change="errors.selectedMaus = ''">
            <el-option v-for="mau in maus" :key="mau.id" :label="mau.tenMau" :value="mau.id"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="RAM" :error="errors.selectedRams">
          <el-select v-model="selectedRams" multiple placeholder="Chọn RAM" @change="errors.selectedRams = ''">
            <el-option v-for="ram in rams" :key="ram.id" :label="ram.dungLuong" :value="ram.id"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="ROM" :error="errors.selectedRoms">
          <el-select v-model="selectedRoms" multiple placeholder="Chọn ROM" @change="errors.selectedRoms = ''">
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
          <el-form-item label="Màn hình">
            <el-select v-model="sanPham.sanPhamChiTiets[selectedChiTiet].idManHinh" placeholder="Chọn màn hình">
              <el-option v-for="mh in manHinhs" :key="mh.id" :label="mh.kichThuoc" :value="mh.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Hệ điều hành">
            <el-select v-model="sanPham.sanPhamChiTiets[selectedChiTiet].idHeDieuHanh" placeholder="Chọn hệ điều hành">
              <el-option v-for="hdh in heDieuHanhs" :key="hdh.id" :label="hdh.phienBan" :value="hdh.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Pin">
            <el-select v-model="sanPham.sanPhamChiTiets[selectedChiTiet].idPin" placeholder="Chọn pin">
              <el-option v-for="pin in pins" :key="pin.id" :label="pin.congSuatSac" :value="pin.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="CPU">
            <el-select v-model="sanPham.sanPhamChiTiets[selectedChiTiet].idCpu" placeholder="Chọn CPU">
              <el-option v-for="cpu in cpus" :key="cpu.id" :label="cpu.xungNhip" :value="cpu.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Camera trước">
            <el-select v-model="sanPham.sanPhamChiTiets[selectedChiTiet].idCameraTruoc" placeholder="Chọn camera trước">
              <el-option v-for="cam in cameraTruocs" :key="cam.id" :label="cam.doPhanGiai" :value="cam.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Camera sau">
            <el-select v-model="sanPham.sanPhamChiTiets[selectedChiTiet].idCameraSau" placeholder="Chọn camera sau">
              <el-option v-for="cam in cameraSaus" :key="cam.id" :label="cam.doPhanGiai" :value="cam.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Xuất xứ">
            <el-select v-model="sanPham.sanPhamChiTiets[selectedChiTiet].idXuatXu" placeholder="Chọn xuất xứ">
              <el-option v-for="xx in xuatXus" :key="xx.id" :label="xx.maXuatXu" :value="xx.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Loại">
            <el-select v-model="sanPham.sanPhamChiTiets[selectedChiTiet].idLoai" placeholder="Chọn loại">
              <el-option v-for="loai in loais" :key="loai.id" :label="loai.tenLoai" :value="loai.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Giá bán">
            <el-input-number v-model="sanPham.sanPhamChiTiets[selectedChiTiet].giaBan" :min="0"
              :precision="2"></el-input-number>
          </el-form-item>
          <el-form-item label="Số lượng">
            <el-input-number v-model="sanPham.sanPhamChiTiets[selectedChiTiet].soLuong" :min="0"
              :disabled="true"></el-input-number>
          </el-form-item>
          <el-form-item label="IMEI">
            <el-input type="textarea" v-model="sanPham.sanPhamChiTiets[selectedChiTiet].imeisInput"
              placeholder="Nhập danh sách IMEI, phân tách bởi dấu phẩy"
              @input="capNhatSoLuong(selectedChiTiet)"></el-input>
            <el-upload :auto-upload="false" :on-change="(file) => handleImeiFileChange(file, selectedChiTiet)"
              accept=".txt,.csv" style="margin-top: 8px;">
              <el-button type="primary">Tải lên file IMEI</el-button>
            </el-upload>
            <div v-if="selectedChiTiet !== null" style="margin-top: 8px;">
              Số lượng IMEI: {{ sanPham.sanPhamChiTiets[selectedChiTiet].soLuong }}
            </div>
          </el-form-item>
          <el-form-item label="Hình ảnh">
            <el-upload :file-list="sanPham.sanPhamChiTiets[selectedChiTiet].hinhAnhs"
              :on-change="(file, fileList) => handleFileChange(file, fileList, selectedChiTiet)"
              :on-remove="(file, fileList) => handleFileRemove(file, fileList, selectedChiTiet)" :auto-upload="false"
              accept="image/jpeg,image/png" list-type="picture" :limit="5"
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
import { ElMessage, ElMessageBox } from 'element-plus';
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
    const errors = reactive({
      tenSanPham: '',
      thuongHieu: '',
      idNhaCungCap: '',
      trangThaiSanPham: '',
      selectedMaus: '',
      selectedRams: '',
      selectedRoms: ''
    });


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

        const requiredLists = [
          { name: 'Nhà cung cấp', list: nhaCungCaps.value },
          { name: 'Màu sắc', list: maus.value },
          { name: 'RAM', list: rams.value },
          { name: 'ROM', list: roms.value },
          { name: 'Màn hình', list: manHinhs.value },
          { name: 'Hệ điều hành', list: heDieuHanhs.value },
          { name: 'Pin', list: pins.value },
          { name: 'CPU', list: cpus.value },
          { name: 'Camera trước', list: cameraTruocs.value },
          { name: 'Camera sau', list: cameraSaus.value },
          { name: 'Xuất xứ', list: xuatXus.value },
          { name: 'Loại', list: loais.value }
        ];
        const emptyLists = requiredLists.filter(item => !item.list.length);
        if (emptyLists.length) {
          ElMessage.error(`Không thể tải danh mục: ${emptyLists.map(item => item.name).join(', ')}`);
        }
      } catch (error) {
        ElMessage.error('Lỗi khi lấy danh mục: ' + error.message);
      }
    };

    const generateVariants = () => {
      let hasError = false;

      if (!selectedMaus.value.length) {
        errors.selectedMaus = 'Vui lòng chọn ít nhất một màu sắc';
        hasError = true;
      }

      if (!selectedRams.value.length) {
        errors.selectedRams = 'Vui lòng chọn ít nhất một RAM';
        hasError = true;
      }

      if (!selectedRoms.value.length) {
        errors.selectedRoms = 'Vui lòng chọn ít nhất một ROM';
        hasError = true;
      }

      if (hasError) {
        ElMessage.error('Vui lòng chọn đầy đủ các thuộc tính để tạo biến thể.');
        return;
      }
      sanPham.sanPhamChiTiets = [];
      const existingCombinations = new Set();
      const newVariants = [];
      selectedMaus.value.forEach(mau => {
        selectedRams.value.forEach(ram => {
          selectedRoms.value.forEach(rom => {
            const combination = `${mau}-${ram}-${rom}`;
            if (!existingCombinations.has(combination)) {
              existingCombinations.add(combination);
              newVariants.push({
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
                imeisInput: ''
              });
            }
          });
        });
      });
      sanPham.sanPhamChiTiets = newVariants;
      selectedChiTiet.value = sanPham.sanPhamChiTiets.length > 0 ? 0 : null;
      console.log('Biến thể mới được tạo:', JSON.stringify(sanPham.sanPhamChiTiets, null, 2));
      ElMessage.success(`Đã tạo ${sanPham.sanPhamChiTiets.length} biến thể sản phẩm`);
    };

    const capNhatSoLuong = debounce((index) => {
      const imeis = sanPham.sanPhamChiTiets[index].imeisInput
        .split(',')
        .map(i => i.trim())
        .filter(i => i && i.length > 0);
      sanPham.sanPhamChiTiets[index].soLuong = imeis.length;
      console.log(`Cập nhật số lượng cho biến thể ${index}:`, imeis.length);
    }, 150);

    const handleImeiFileChange = (file, index) => {
      try {
        if (file.raw.size > 1024 * 1024) {
          throw new Error('File quá lớn, vui lòng chọn file dưới 1MB');
        }
        if (!['text/plain', 'text/csv'].includes(file.raw.type)) {
          throw new Error('Vui lòng chọn file .txt hoặc .csv');
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
      console.log('Biến thể sau khi xóa:', JSON.stringify(sanPham.sanPhamChiTiets, null, 2));
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
        if (!['image/jpeg', 'image/png'].includes(file.raw.type)) {
          throw new Error('Chỉ chấp nhận file JPEG hoặc PNG');
        }
        if (file.raw.size > 5 * 1024 * 1024) {
          throw new Error('Kích thước ảnh không được vượt quá 5MB');
        }
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
        // if (!sanPham.sanPhamChiTiets.length) {
        //   ElMessage.error('Vui lòng tạo ít nhất một biến thể sản phẩm.');
        //   return;
        // }

        const payload = {
          tenSanPham: sanPham.tenSanPham,
          thuongHieu: sanPham.thuongHieu,
          idNhaCungCap: sanPham.idNhaCungCap,
          trangThaiSanPham: sanPham.trangThaiSanPham !== '' ? sanPham.trangThaiSanPham : null,
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
        ElMessageBox.confirm('Sản phẩm đã được lưu thành công. Bạn có muốn làm mới form?', 'Xác nhận', {
          confirmButtonText: 'Có',
          cancelButtonText: 'Không'
        }).then(() => {
          sanPham.tenSanPham = '';
          sanPham.thuongHieu = '';
          sanPham.idNhaCungCap = '';
          sanPham.trangThaiSanPham = '';
          sanPham.sanPhamChiTiets = [];
          selectedMaus.value = [];
          selectedRams.value = [];
          selectedRoms.value = [];
          selectedChiTiet.value = null;
          ElMessage.success('Form đã được làm mới!');
          // Object.keys(errors).forEach(k => delete errors[k]);
          Object.keys(errors).forEach(k => errors[k] = '');
        });
      } catch (error) {
        console.error('Lỗi khi lưu sản phẩm:', {
          status: error.response?.status,
          responseData: error.response?.data,
          message: error.message,
          rawError: error
        });

        // 👉 TH1: Nếu server trả về lỗi xác thực từng field (validation error)
        if (
          error.response?.status === 400 &&
          typeof error.response?.data?.message === 'object'
        ) {
          const fieldErrors = error.response.data.message;

          // 👉 Gộp các lỗi thành 1 chuỗi để hiện lên toast
          const errorMessages = Object.values(fieldErrors).join('; ');

          // 👉 Hiện thông báo lỗi tổng
          ElMessage.error('Lỗi xác thực: ' + errorMessages);

          // 👉 Xoá lỗi cũ và gán lỗi mới vào biến `errors` để hiện dưới form
          Object.keys(errors).forEach(k => errors[k] = '');
          Object.assign(errors, fieldErrors);
        }

        // 👉 TH2: Nếu server trả về lỗi logic hoặc business (message là chuỗi)
        else if (typeof error.response?.data?.message === 'string') {
          ElMessage.error(error.response.data.message);
        }

        // 👉 TH3: Các lỗi khác không xác định (network, server lỗi 500,...)
        else {
          ElMessage.error(error.message || 'Lỗi không xác định');
        }

      }
    };


// đang xử lý lỗi error

    onMounted(() => {
      fetchDanhMuc();
    });

    return {
      sanPham,
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
      errors,
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
```