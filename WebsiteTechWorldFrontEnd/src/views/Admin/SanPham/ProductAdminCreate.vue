```html
<template>
  <el-form :model="sanPham" ref="sanPhamForm" label-width="140px" class="product-form">
    <!-- Thông tin sản phẩm chính -->
    <el-card shadow="hover" class="section-card">
      <template #header>
        <div class="card-header">
          <span>
            <Document class="el-icon" /> Thông tin sản phẩm
          </span>
        </div>
      </template>
      <el-form-item label="Model sản phẩm" prop="idModelSanPham" :error="errors.idModelSanPham">
        <el-select v-model="sanPham.idModelSanPham" placeholder="Chọn model sản phẩm" clearable filterable
          @change="onModelChange" style="width: 100%">
          <el-option v-for="model in modelSanPhams" :key="model.idModelSanPham" :label="model.tenModel"
            :value="model.idModelSanPham" />
        </el-select>
      </el-form-item>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Tên sản phẩm" prop="tenSanPham" :error="errors.tenSanPham">
            <el-input v-model="sanPham.tenSanPham" placeholder="Nhập tên sản phẩm" clearable
              @input="errors.tenSanPham = ''" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Thương hiệu" prop="thuongHieu" :error="errors.thuongHieu">
            <el-input v-model="sanPham.thuongHieu" placeholder="Nhập thương hiệu" clearable
              @input="errors.thuongHieu = ''" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Nhà cung cấp" prop="idNhaCungCap" :error="errors.idNhaCungCap">
            <el-select v-model="sanPham.idNhaCungCap" placeholder="Chọn nhà cung cấp" clearable filterable
              @change="errors.idNhaCungCap = ''" style="width: 100%">
              <el-option v-for="ncc in nhaCungCaps" :key="ncc.id" :label="ncc.tenNhaCungCap" :value="ncc.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Trạng thái" prop="trangThaiSanPham" :error="errors.trangThaiSanPham">
            <el-select v-model="sanPham.trangThaiSanPham" placeholder="Chọn trạng thái" clearable
              @change="errors.trangThaiSanPham = ''" style="width: 100%">
              <el-option v-for="tt in danhSachTrangThaiSanPham" :key="tt.value" :label="tt.label" :value="tt.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-card>

    <!-- Chọn thuộc tính để tạo biến thể -->
    <el-card shadow="hover" class="section-card">
      <template #header>
        <div class="card-header">
          <span>
            <Setting class="el-icon" /> Tạo biến thể sản phẩm
          </span>
        </div>
      </template>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Màu sắc" prop="selectedMaus" :error="errors.selectedMaus">
            <el-select v-model="selectedMaus" multiple placeholder="Chọn màu sắc" clearable
              @change="errors.selectedMaus = ''" style="width: 100%">
              <el-option v-for="mau in maus" :key="mau.id" :label="mau.tenMau" :value="mau.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="ROM" prop="selectedRoms" :error="errors.selectedRoms">
            <el-select v-model="selectedRoms" multiple placeholder="Chọn ROM" clearable
              @change="errors.selectedRoms = ''" style="width: 100%">
              <el-option v-for="rom in roms" :key="rom.id" :label="rom.dungLuong" :value="rom.id" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-card>

    <!-- Thêm ảnh theo màu sắc -->
    <el-card shadow="hover" class="section-card">
      <template #header>
        <div class="card-header">
          <span>
            <Picture class="el-icon" /> Ảnh theo màu sắc
          </span>
        </div>
      </template>
      <el-row :gutter="20">
        <el-col :span="24" v-for="mau in selectedMaus" :key="mau">
          <el-form-item :label="getMauSacLabels(mau)" :error="errorsMauHinhAnh[mau] || ''">
            <el-upload :file-list="hinhAnhTheoMau[mau] || []"
              :on-change="(file, fileList) => handleMauFileChange(file, fileList, mau)"
              :on-remove="(file, fileList) => handleMauFileRemove(file, fileList, mau)" :auto-upload="false"
              accept="image/jpeg,image/png,image/webp" list-type="picture-card" :limit="5" :on-exceed="handleExceed">
              <template #trigger>
                <el-button type="primary" plain>
                  <Upload class="el-icon" /> Tải ảnh
                </el-button>
              </template>
              <template #tip>
                <div class="el-upload__tip">
                  Tải tối đa 5 ảnh (JPEG, PNG, WEBP, tối đa 5MB mỗi ảnh)
                </div>
              </template>
            </el-upload>
          </el-form-item>
        </el-col>
      </el-row>
    </el-card>

    <!-- Nhập giá nhanh -->
    <el-card shadow="hover" class="section-card">
      <template #header>
        <div class="card-header">
          <span>
            <Money class="el-icon" /> Nhập giá nhanh
          </span>
        </div>
      </template>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Giá bán chung" prop="giaBanChung" :error="errors.giaBanChung">
            <el-input-number v-model="giaBanChung" :precision="0" :min="1000" :step="1000"
              placeholder="Nhập giá bán chung" style="width: 200px" @change="errors.giaBanChung = ''"
              :formatter="value => value ? `${value.toLocaleString('vi-VN')} VND` : ''"
              :parser="value => value.replace(/\D/g, '')" />
            <el-tooltip content="Áp dụng giá này cho tất cả biến thể" placement="top">
              <el-button type="primary" style="margin-left: 10px" @click="applyGiaBanChung">
                <Check class="el-icon" /> Áp dụng
              </el-button>
            </el-tooltip>
          </el-form-item>
        </el-col>
      </el-row>
      <el-button type="primary" :loading="loading.generate" @click="generateVariants">
        <Plus class="el-icon" /> Tạo biến thể
      </el-button>
    </el-card>

    <!-- Danh sách chi tiết sản phẩm -->
    <el-card shadow="hover" class="section-card">
      <template #header>
        <div class="card-header">
          <span>
            <Tickets class="el-icon" /> Chi tiết sản phẩm
          </span>
        </div>
      </template>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-table :data="sanPham.sanPhamChiTiets" style="margin-top: 10px" highlight-current-row
            @row-click="selectChiTiet" class="variant-table">
            <el-table-column label="STT" type="index" width="60" align="center" />
            <el-table-column label="Màu sắc" width="120">
              <template #default="{ row }">
                {{ getMauSacLabels(row.idMau) }}
              </template>
            </el-table-column>
            <el-table-column label="ROM" width="100">
              <template #default="{ row }">
                {{ getRomLabels(row.idRom) }}
              </template>
            </el-table-column>
            <el-table-column label="Giá bán" width="120">
              <template #default="{ row }">
                {{ row.giaBan ? row.giaBan.toLocaleString("vi-VN") + " VND" : "Chưa nhập giá" }}
              </template>
            </el-table-column>
            <el-table-column label="Ảnh" width="100">
              <template #default="{ row }">
                <el-image v-for="(hinh, index) in row.hinhAnhs.slice(0, 2)" :key="hinh.imagePublicId" :src="hinh.url"
                  :preview-src-list="[hinh.url]" style="width: 30px; height: 30px; margin-right: 3px" fit="cover" />
                <span v-if="row.hinhAnhs.length > 2">+{{ row.hinhAnhs.length - 2 }} ảnh</span>
                <span v-if="!row.hinhAnhs.length">Chưa có ảnh</span>
              </template>
            </el-table-column>
            <el-table-column label="Hành động" width="80" align="center" fixed="right">
              <template #default="{ $index }">
                <el-tooltip content="Xóa biến thể" placement="top">
                  <el-button type="danger" size="small" @click.stop="removeChiTiet($index)">
                    <Delete class="el-icon" />
                  </el-button>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="12">
          <el-card v-if="selectedChiTiet !== null" shadow="hover" class="variant-detail-card">
            <template #header>
              <div class="card-header">
                <span>Thông tin biến thể {{ selectedChiTiet + 1 }}
                  ({{ getMauSacLabels(sanPham.sanPhamChiTiets[selectedChiTiet].idMau) }}
                  - {{ getRomLabels(sanPham.sanPhamChiTiets[selectedChiTiet].idRom) }})</span>
              </div>
            </template>
            <el-form-item label="Giá bán" :error="errorsChiTiet[selectedChiTiet]?.giaBan || ''" class="error-container">
              <el-input-number v-model="sanPham.sanPhamChiTiets[selectedChiTiet].giaBan" :precision="0" :min="1000"
                :step="1000" placeholder="Nhập giá bán" style="width: 100%"
                @change="errorsChiTiet[selectedChiTiet].giaBan = ''"
                :formatter="value => value ? `${value.toLocaleString('vi-VN')} VND` : ''"
                :parser="value => value.replace(/\D/g, '')" />
            </el-form-item>
            <el-form-item label="Số lượng" :error="errorsChiTiet[selectedChiTiet]?.soLuong || ''"
              class="error-container">
              <el-input-number v-model="sanPham.sanPhamChiTiets[selectedChiTiet].soLuong" :min="0" :disabled="true"
                style="width: 100%" />
            </el-form-item>
            <el-form-item label="IMEI" :error="errorsChiTiet[selectedChiTiet]?.imeisInput || ''"
              class="error-container">
              <el-input type="textarea" v-model="sanPham.sanPhamChiTiets[selectedChiTiet].imeisInput"
                placeholder="Nhập danh sách IMEI (15 chữ số mỗi IMEI, phân tách bởi dấu phẩy)" :rows="4"
                :disabled="sanPham.sanPhamChiTiets[selectedChiTiet].isFileUploaded"
                @input="handleImeiInput(selectedChiTiet)" />
              <div class="action-buttons">
                <el-upload :auto-upload="false" :on-change="(file) => handleImeiFileChange(file, selectedChiTiet)"
                  accept=".xlsx,.xls" :disabled="sanPham.sanPhamChiTiets[selectedChiTiet].imeisInput.length > 0">
                  <el-button type="primary" plain
                    :disabled="sanPham.sanPhamChiTiets[selectedChiTiet].imeisInput.length > 0">
                    <Upload class="el-icon" /> Tải file IMEI
                  </el-button>
                </el-upload>
                <el-button type="warning" plain @click="clearImeiInput(selectedChiTiet)">
                  <Delete class="el-icon" /> Xóa IMEI
                </el-button>
              </div>
              <div class="imei-status">
                <span>Số lượng IMEI: {{ sanPham.sanPhamChiTiets[selectedChiTiet].soLuong }}</span>
                <el-tooltip v-if="errorsChiTiet[selectedChiTiet]?.imeisInput === 'Hợp lệ'"
                  content="Danh sách IMEI hợp lệ" placement="top">
                  <span class="valid-message">
                    <Check class="el-icon" /> Hợp lệ
                  </span>
                </el-tooltip>
              </div>
            </el-form-item>
          </el-card>
          <el-card v-else shadow="hover">
            <div class="empty-state">
              <InfoFilled class="el-icon" /> Vui lòng chọn hoặc thêm biến thể sản phẩm
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- Nút lưu sản phẩm -->
    <div class="form-actions">
      <el-button type="success" :loading="loading.submit" @click="submitForm">
        <Check class="el-icon" /> Lưu sản phẩm
      </el-button>
    </div>
  </el-form>
</template>

<script>
import { onMounted, reactive, ref } from "vue";
import { ElMessage, ElMessageBox, ElLoading } from "element-plus";
import {
  getAllMauSacList,
  getAllModelSanPhamList,
  getAllNhaCungCapList,
  getAllRomList,
  postSanPham,
} from "@/Service/Adminservice/Products/ProductAdminService";
import { debounce } from "chart.js/helpers";
import api from "@/Service/LoginService/axiosInstance";
import * as XLSX from "xlsx";
import { Document, Setting, Picture, Money, Upload, Check, Delete, Tickets, InfoFilled, Plus } from "@element-plus/icons-vue";
import { useRouter } from "vue-router";

export default {
  components: {
    Document,
    Setting,
    Picture,
    Money,
    Upload,
    Check,
    Delete,
    Tickets,
    InfoFilled,
    Plus
  },
  setup() {
    const router = useRouter(); // Khởi tạo router
    // Dữ liệu chính của sản phẩm
    const sanPham = reactive({
      tenSanPham: "",
      thuongHieu: "Apple",
      idNhaCungCap: "",
      trangThaiSanPham: "",
      idModelSanPham: null,
      sanPhamChiTiets: [],
    });

    // Trạng thái loading
    const loading = reactive({
      generate: false,
      submit: false,
    });

    // Tham chiếu đến form và các danh sách dữ liệu
    const sanPhamForm = ref(null);
    const selectedMaus = ref([]);
    const selectedRoms = ref([]);
    const nhaCungCaps = ref([]);
    const maus = ref([]);
    const roms = ref([]);
    const modelSanPhams = ref([]);
    const selectedChiTiet = ref(null);
    const hinhAnhTheoMau = reactive({});
    const errors = reactive({
      tenSanPham: "",
      thuongHieu: "",
      idNhaCungCap: "",
      trangThaiSanPham: "",
      idModelSanPham: "",
      selectedMaus: "",
      selectedRoms: "",
      giaBanChung: "",
    });
    const errorsChiTiet = reactive([]);
    const errorsMauHinhAnh = reactive({});
    const giaBanChung = ref(null);

    // Danh sách trạng thái sản phẩm
    const danhSachTrangThaiSanPham = [
      { label: "Đang kinh doanh", value: "ACTIVE" },
      // { label: "Ngừng kinh doanh", value: "DISCONTINUED" },
      { label: "Sắp ra mắt", value: "COMING_SOON" },
      // { label: "Tạm ngừng bán", value: "TEMPORARILY_UNAVAILABLE" },
      // { label: "Hết hàng", value: "OUT_OF_STOCK" },
    ];

    // Lấy dữ liệu danh mục từ API
    const fetchDanhMuc = async () => {
      const loadingInstance = ElLoading.service({
        text: "Đang tải danh mục...",
        background: "rgba(0, 0, 0, 0.7)",
      });
      try {
        const responses = await Promise.all([
          getAllNhaCungCapList(),
          getAllMauSacList(),
          getAllRomList(),
          getAllModelSanPhamList(),
        ]);

        nhaCungCaps.value = responses[0];
        maus.value = responses[1];
        roms.value = responses[2];
        modelSanPhams.value = responses[3];

        const requiredLists = [
          { name: "Nhà cung cấp", list: nhaCungCaps.value },
          { name: "Màu sắc", list: maus.value },
          { name: "ROM", list: roms.value },
          { name: "Model sản phẩm", list: modelSanPhams.value },
        ];
        const emptyLists = requiredLists.filter((item) => !item.list.length);
        if (emptyLists.length) {
          ElMessage.error(
            `Không thể tải danh mục: ${emptyLists.map((item) => item.name).join(", ")}`
          );
        }
      } catch (error) {
        ElMessage.error("Lỗi khi tải danh mục: " + error.message);
      } finally {
        loadingInstance.close();
      }
    };

    // Xử lý khi chọn model sản phẩm
    const onModelChange = () => {
      errors.idModelSanPham = "";
      const selectedModel = modelSanPhams.value.find(
        (m) => m.idModelSanPham === sanPham.idModelSanPham
      );
      if (selectedModel) {
        sanPham.tenSanPham = selectedModel.tenModel;
      }
    };

    // Xử lý khi vượt quá giới hạn ảnh
    const handleExceed = () => {
      ElMessage.warning("Chỉ được tải lên tối đa 5 ảnh cho mỗi màu!");
    };

    // Tạo các biến thể sản phẩm
    const generateVariants = async () => {
      loading.generate = true;
      let hasError = false;

      // Kiểm tra các trường bắt buộc để tạo biến thể
      if (!sanPham.idModelSanPham) {
        errors.idModelSanPham = "Vui lòng chọn model sản phẩm";
        hasError = true;
      }
      if (!selectedMaus.value.length) {
        errors.selectedMaus = "Vui lòng chọn ít nhất một màu sắc";
        hasError = true;
      }
      if (!selectedRoms.value.length) {
        errors.selectedRoms = "Vui lòng chọn ít nhất một ROM";
        hasError = true;
      }
      selectedMaus.value.forEach((mau) => {
        if (!hinhAnhTheoMau[mau] || !hinhAnhTheoMau[mau].length) {
          errorsMauHinhAnh[mau] = "Phải có ít nhất 1 hình ảnh cho màu này";
          hasError = true;
        }
      });

      if (hasError) {
        loading.generate = false;
        ElMessage.error("Vui lòng điền đầy đủ thông tin bắt buộc!");
        scrollToError();
        return;
      }

      // Làm mới hinhAnhTheoMau và errorsMauHinhAnh
      Object.keys(hinhAnhTheoMau).forEach((key) => {
        if (!selectedMaus.value.includes(Number(key))) {
          delete hinhAnhTheoMau[key];
        }
      });
      Object.keys(errorsMauHinhAnh).forEach((key) => {
        if (!selectedMaus.value.includes(Number(key))) {
          delete errorsMauHinhAnh[key];
        }
      });

      // Tạo biến thể mới
      const existingCombinations = new Set();
      const newVariants = [];
      errorsChiTiet.length = 0;

      selectedMaus.value.forEach((mau) => {
        selectedRoms.value.forEach((rom) => {
          const combination = `${mau}-${rom}`;
          if (!existingCombinations.has(combination)) {
            existingCombinations.add(combination);
            const existingVariant = sanPham.sanPhamChiTiets.find(
              (v) => v.idMau === mau && v.idRom === rom
            );
            newVariants.push({
              id: existingVariant?.id || null,
              idMau: mau,
              idRom: rom,
              soLuong: existingVariant?.soLuong || 0,
              giaBan: giaBanChung.value || existingVariant?.giaBan || null,
              imeisInput: existingVariant?.imeisInput || "",
              isFileUploaded: existingVariant?.isFileUploaded || false,
              imeis: existingVariant?.imeis || [], // Giữ lại danh sách IMEI
              hinhAnhs: hinhAnhTheoMau[mau] || [],
            });
            errorsChiTiet.push({
              giaBan: "",
              soLuong: "",
              imeisInput: "",
            });
          }
        });
      });

      sanPham.sanPhamChiTiets = newVariants;

      // Cập nhật số lượng dựa trên IMEI (không kiểm tra lỗi)
      sanPham.sanPhamChiTiets.forEach((_, index) => {
        capNhatSoLuong(index, false);
      });

      // Chọn biến thể đầu tiên
      selectedChiTiet.value = sanPham.sanPhamChiTiets.length > 0 ? 0 : null;

      // Cuộn đến bảng chi tiết
      setTimeout(() => {
        const table = document.querySelector(".variant-table");
        if (table) {
          table.scrollIntoView({ behavior: "smooth" });
        }
      }, 100);

      loading.generate = false;
      ElMessage.success(`Đã tạo ${sanPham.sanPhamChiTiets.length} biến thể sản phẩm`);
    };

    // Áp dụng giá bán chung
    const applyGiaBanChung = () => {
      if (!sanPham.sanPhamChiTiets.length) {
        ElMessage.error("Vui lòng tạo biến thể trước!");
        return;
      }
      if (!giaBanChung.value || giaBanChung.value < 1000) {
        errors.giaBanChung = "Giá bán chung phải lớn hơn hoặc bằng 1000";
        ElMessage.error("Vui lòng nhập giá bán chung hợp lệ!");
        return;
      }

      ElMessageBox.confirm(
        "Áp dụng giá này cho tất cả biến thể?",
        "Xác nhận",
        {
          confirmButtonText: "Áp dụng",
          cancelButtonText: "Hủy",
          type: "warning",
        }
      ).then(() => {
        sanPham.sanPhamChiTiets.forEach((chiTiet, index) => {
          chiTiet.giaBan = giaBanChung.value;
          errorsChiTiet[index].giaBan = "";
        });
        ElMessage.success("Đã áp dụng giá bán chung!");
      }).catch(() => {
        ElMessage.info("Đã hủy áp dụng giá chung.");
      });
    };

    // Cập nhật số lượng dựa trên IMEI
    const capNhatSoLuong = (index, validate = false) => {
      const update = () => {
        const imeis = sanPham.sanPhamChiTiets[index].imeisInput
          .split(",")
          .map((i) => i.trim())
          .filter((i) => i);
        sanPham.sanPhamChiTiets[index].soLuong = imeis.length;

        if (validate) {
          if (imeis.length === 0) {
            errorsChiTiet[index].imeisInput = "Phải có ít nhất 1 IMEI";
            errorsChiTiet[index].soLuong = "Số lượng phải lớn hơn 0";
          } else {
            const allImeis = sanPham.sanPhamChiTiets
              .flatMap((chiTiet, i) =>
                i !== index
                  ? chiTiet.imeisInput.split(",").map((im) => im.trim()).filter((im) => im)
                  : []
              );
            const duplicateImeis = imeis.filter((im) => allImeis.includes(im));
            if (duplicateImeis.length > 0) {
              errorsChiTiet[index].imeisInput = `${duplicateImeis.length} IMEI trùng lặp: ${duplicateImeis.slice(0, 3).join(", ")}${duplicateImeis.length > 3 ? "..." : ""}`;
              errorsChiTiet[index].soLuong = "";
            } else {
              const invalidImeis = imeis.filter((i) => !/^\d{15}$/.test(i));
              if (invalidImeis.length > 0) {
                errorsChiTiet[index].imeisInput = `${invalidImeis.length} IMEI không hợp lệ: ${invalidImeis.slice(0, 3).join(", ")}${invalidImeis.length > 3 ? "..." : ""}`;
                errorsChiTiet[index].soLuong = "";
              } else {
                errorsChiTiet[index].imeisInput = "Hợp lệ";
                errorsChiTiet[index].soLuong = "";
              }
            }
          }
        } else {
          errorsChiTiet[index].imeisInput = "";
          errorsChiTiet[index].soLuong = "";
        }
      };

      if (validate) {
        debounce(update, 150)();
      } else {
        update();
      }
    };

    // Xử lý khi nhập IMEI vào textarea
    const handleImeiInput = (index) => {
      sanPham.sanPhamChiTiets[index].isFileUploaded = false; // Đặt lại trạng thái file khi nhập tay
      capNhatSoLuong(index, true);
    };

    // Xóa danh sách IMEI
    const clearImeiInput = (index) => {
      ElMessageBox.confirm(
        "Bạn có chắc muốn xóa toàn bộ IMEI của biến thể này?",
        "Xác nhận",
        {
          confirmButtonText: "Xóa",
          cancelButtonText: "Hủy",
          type: "warning",
        }
      ).then(() => {
        sanPham.sanPhamChiTiets[index].imeisInput = "";
        sanPham.sanPhamChiTiets[index].soLuong = 0;
        sanPham.sanPhamChiTiets[index].isFileUploaded = false;
        sanPham.sanPhamChiTiets[index].imeis = [];
        errorsChiTiet[index].imeisInput = "";
        errorsChiTiet[index].soLuong = "";
        ElMessage.success("Đã xóa danh sách IMEI!");
      }).catch(() => {
        ElMessage.info("Đã hủy xóa IMEI.");
      });
    };

    // Xử lý tải file IMEI
    const handleImeiFileChange = async (file, index) => {
      const loadingInstance = ElLoading.service({
        text: "Đang đọc file Excel...",
        background: "rgba(0, 0, 0, 0.7)",
      });
      try {
        if (file.raw.size > 1024 * 1024) {
          throw new Error("File quá lớn, vui lòng chọn file dưới 1MB");
        }
        if (!["application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"].includes(file.raw.type)) {
          throw new Error("Vui lòng chọn file .xlsx hoặc .xls");
        }

        const reader = new FileReader();
        reader.onload = (e) => {
          try {
            const data = new Uint8Array(e.target.result);
            const workbook = XLSX.read(data, { type: "array" });
            const sheet = workbook.Sheets[workbook.SheetNames[0]];
            const json = XLSX.utils.sheet_to_json(sheet, { header: ["soImei"], skipHeader: true });

            const imeis = json
              .map((row) => String(row.soImei).trim())
              .filter((imei) => imei && /^\d{15}$/.test(imei));

            if (imeis.length === 0) {
              throw new Error("Không tìm thấy IMEI hợp lệ trong file");
            }


            // Lưu IMEI và đồng bộ vào imeisInput
            // sanPham.sanPhamChiTiets[index].imeisInput = imeis.join(", "); // Đồng bộ danh sách IMEI vào textarea
            sanPham.sanPhamChiTiets[index].soLuong = imeis.length;
            sanPham.sanPhamChiTiets[index].isFileUploaded = true;
            sanPham.sanPhamChiTiets[index].imeis = imeis; // Lưu danh sách IMEI riêng
            errorsChiTiet[index].imeisInput = "Hợp lệ";
            errorsChiTiet[index].soLuong = "";
            ElMessage.success(`Đã nhập ${imeis.length} IMEI từ file ${file.name}`);
          } catch (error) {
            ElMessage.error("Lỗi khi đọc file Excel: " + error.message);
          } finally {
            loadingInstance.close();
          }
        };
        reader.onerror = () => {
          ElMessage.error("Lỗi khi đọc file Excel");
          loadingInstance.close();
        };
        reader.readAsArrayBuffer(file.raw);
      } catch (error) {
        ElMessage.error("Lỗi khi xử lý file IMEI: " + error.message);
        loadingInstance.close();
      }
    };

    // Xử lý tải ảnh
    const handleMauFileChange = async (file, fileList, idMau) => {
      const loadingInstance = ElLoading.service({
        target: `.el-upload[mau-id="${idMau}"]`,
        text: "Đang tải ảnh...",
      });
      try {
        if (!["image/jpeg", "image/png", "image/webp"].includes(file.raw.type)) {
          throw new Error("Chỉ chấp nhận file JPEG, PNG hoặc WEBP");
        }
        if (file.raw.size > 5 * 1024 * 1024) {
          throw new Error("Kích thước ảnh không được vượt quá 5MB");
        }
        const formData = new FormData();
        formData.append("file", file.raw);
        const response = await api.post(
          "http://localhost:8080/admin/hinhAnh/upload",
          formData,
          { headers: { "Content-Type": "multipart/form-data" } }
        );
        if (!hinhAnhTheoMau[idMau]) {
          hinhAnhTheoMau[idMau] = [];
        }
        hinhAnhTheoMau[idMau] = fileList.map((item) => ({
          name: item.name,
          url: item.uid === file.uid ? response.data.url : item.url,
          imagePublicId:
            item.uid === file.uid ? response.data.imagePublicId : item.imagePublicId,
        }));
        errorsMauHinhAnh[idMau] =
          hinhAnhTheoMau[idMau].length > 0 ? "" : "Phải có ít nhất 1 hình ảnh";
        // Đồng bộ ảnh cho biến thể
        sanPham.sanPhamChiTiets.forEach((chiTiet) => {
          if (chiTiet.idMau === idMau) {
            chiTiet.hinhAnhs = hinhAnhTheoMau[idMau];
          }
        });
        ElMessage.success(`Tải ảnh ${file.name} thành công!`);
      } catch (error) {
        errorsMauHinhAnh[idMau] = "Lỗi khi tải ảnh: " + (error.response?.data?.message || error.message);
        ElMessage.error(errorsMauHinhAnh[idMau]);
      } finally {
        loadingInstance.close();
      }
    };

    // Xử lý xóa ảnh
    const handleMauFileRemove = (file, fileList, idMau) => {
      hinhAnhTheoMau[idMau] = fileList.map((item) => ({
        name: item.name,
        url: item.url,
        imagePublicId: item.imagePublicId,
      }));
      errorsMauHinhAnh[idMau] =
        hinhAnhTheoMau[idMau].length > 0 ? "" : "Phải có ít nhất 1 hình ảnh";
      sanPham.sanPhamChiTiets.forEach((chiTiet) => {
        if (chiTiet.idMau === idMau) {
          chiTiet.hinhAnhs = hinhAnhTheoMau[idMau];
        }
      });
      ElMessage.success(`Đã xóa ảnh ${file.name}`);
    };

    // Xóa biến thể
    const removeChiTiet = (index) => {
      ElMessageBox.confirm("Bạn có chắc muốn xóa biến thể này?", "Xác nhận", {
        confirmButtonText: "Xóa",
        cancelButtonText: "Hủy",
        type: "warning",
      }).then(() => {
        sanPham.sanPhamChiTiets.splice(index, 1);
        errorsChiTiet.splice(index, 1);
        if (selectedChiTiet.value === index) {
          selectedChiTiet.value = sanPham.sanPhamChiTiets.length > 0 ? 0 : null;
        } else if (selectedChiTiet.value > index) {
          selectedChiTiet.value--;
        }
        ElMessage.success("Đã xóa biến thể!");
      }).catch(() => {
        ElMessage.info("Đã hủy xóa biến thể.");
      });
    };

    // Chọn biến thể
    const selectChiTiet = (row, column, event) => {
      const index = sanPham.sanPhamChiTiets.indexOf(row);
      selectedChiTiet.value = index;
      if (errorsChiTiet[index]) {
        Object.keys(errorsChiTiet[index]).forEach((k) => (errorsChiTiet[index][k] = ""));
        capNhatSoLuong(index, true); // Kiểm tra lại IMEI khi chọn biến thể
      }
      // Đồng bộ imeisInput từ imeis nếu là file upload
      if (sanPham.sanPhamChiTiets[index].isFileUploaded && sanPham.sanPhamChiTiets[index].imeis) {
        sanPham.sanPhamChiTiets[index].imeisInput = sanPham.sanPhamChiTiets[index].imeis.join(", ");
      }
    };

    // Lấy nhãn màu sắc
    const getMauSacLabels = (idMau) => {
      if (!Array.isArray(idMau)) idMau = [idMau];
      return idMau
        .map((id) => maus.value.find((m) => String(m.id) === String(id))?.tenMau || "Không xác định")
        .join(", ");
    };

    // Lấy nhãn ROM
    const getRomLabels = (idRom) => {
      if (!Array.isArray(idRom)) idRom = [idRom];
      return idRom
        .map((id) => roms.value.find((r) => String(r.id) === String(id))?.dungLuong || "Không xác định")
        .join(", ");
    };

    // Cuộn đến phần có lỗi
    const scrollToError = () => {
      setTimeout(() => {
        const errorElement = document.querySelector(".el-form-item__error")?.closest(".el-form-item");
        if (errorElement) {
          errorElement.scrollIntoView({ behavior: "smooth", block: "center" });
        } else {
          const table = document.querySelector(".variant-table");
          if (table) {
            table.scrollIntoView({ behavior: "smooth" });
          }
        }
      }, 100);
    };

    // Xác thực form
    const validateForm = () => {
      let hasError = false;

      if (!sanPham.tenSanPham) {
        errors.tenSanPham = "Vui lòng nhập tên sản phẩm";
        hasError = true;
      }
      if (!sanPham.thuongHieu) {
        errors.thuongHieu = "Vui lòng nhập thương hiệu";
        hasError = true;
      }
      if (!sanPham.idNhaCungCap) {
        errors.idNhaCungCap = "Vui lòng chọn nhà cung cấp";
        hasError = true;
      }
      if (!sanPham.trangThaiSanPham) {
        errors.trangThaiSanPham = "Vui lòng chọn trạng thái";
        hasError = true;
      }
      if (!sanPham.idModelSanPham) {
        errors.idModelSanPham = "Vui lòng chọn model sản phẩm";
        hasError = true;
      }
      selectedMaus.value.forEach((mau) => {
        if (!hinhAnhTheoMau[mau] || !hinhAnhTheoMau[mau].length) {
          errorsMauHinhAnh[mau] = "Phải có ít nhất 1 hình ảnh cho màu này";
          hasError = true;
        }
      });

      sanPham.sanPhamChiTiets.forEach((chiTiet, index) => {
        if (!chiTiet.giaBan || chiTiet.giaBan < 1000) {
          errorsChiTiet[index].giaBan = `Giá bán phải ≥ 1000 VND`;
          hasError = true;
        }
        if (!chiTiet.soLuong) {
          errorsChiTiet[index].soLuong = "Số lượng phải lớn hơn 0";
          hasError = true;
        }
        const imeis = chiTiet.isFileUploaded
          ? chiTiet.imeis
          : chiTiet.imeisInput.split(",").map((i) => i.trim()).filter((i) => i);
        if (imeis.length === 0) {
          errorsChiTiet[index].imeisInput = "Phải có ít nhất 1 IMEI";
          hasError = true;
        } else {
          const allImeis = sanPham.sanPhamChiTiets
            .flatMap((ct, i) =>
              i !== index
                ? (ct.isFileUploaded
                  ? ct.imeis
                  : ct.imeisInput.split(",").map((im) => im.trim()).filter((im) => im))
                : []
            );
          const duplicateImeis = imeis.filter((im) => allImeis.includes(im));
          if (duplicateImeis.length > 0) {
            errorsChiTiet[index].imeisInput = `${duplicateImeis.length} IMEI trùng lặp: ${duplicateImeis.slice(0, 3).join(", ")}${duplicateImeis.length > 3 ? "..." : ""}`;
            hasError = true;
          } else {
            const invalidImeis = imeis.filter((i) => !/^\d{15}$/.test(i));
            if (invalidImeis.length > 0) {
              errorsChiTiet[index].imeisInput = `${invalidImeis.length} IMEI không hợp lệ: ${invalidImeis.slice(0, 3).join(", ")}${invalidImeis.length > 3 ? "..." : ""}`;
              hasError = true;
            }
          }
        }
      });

      return !hasError;
    };

    // Lưu sản phẩm
    const submitForm = async () => {
      loading.submit = true;
      try {
        if (!sanPham.sanPhamChiTiets.length) {
          ElMessage.error("Vui lòng tạo ít nhất một biến thể sản phẩm!");
          loading.submit = false;
          return;
        }

        if (!validateForm()) {
          const errorIndex = errorsChiTiet.findIndex((err) =>
            Object.values(err).some((e) => e)
          );
          if (errorIndex !== -1) {
            selectedChiTiet.value = errorIndex;
          }
          ElMessage.error("Vui lòng sửa các lỗi trong form!");
          scrollToError();
          loading.submit = false;
          return;
        }

        const payload = {
          tenSanPham: sanPham.tenSanPham,
          thuongHieu: sanPham.thuongHieu,
          idNhaCungCap: sanPham.idNhaCungCap,
          trangThaiSanPham: sanPham.trangThaiSanPham,
          idModelSanPham: sanPham.idModelSanPham,
          sanPhamChiTiets: sanPham.sanPhamChiTiets.map((chiTiet) => {
            const imeis = chiTiet.isFileUploaded
              ? chiTiet.imeis
              : chiTiet.imeisInput.split(",").map((i) => i.trim()).filter((i) => i);
            return {
              idMau: chiTiet.idMau,
              idRom: chiTiet.idRom,
              soLuong: chiTiet.soLuong,
              giaBan: chiTiet.giaBan,
              imeis: imeis.map((i) => ({ soImei: i })),
              hinhAnhs: chiTiet.hinhAnhs.map((h) => ({
                url: h.url,
                imagePublicId: h.imagePublicId,
              })),
            };
          }),
        };

        await postSanPham(payload);
        ElMessage.success("Sản phẩm đã được lưu thành công!");
        router.push({ name: "products" }); // Điều hướng đến danh sách sản phẩm
      } catch (error) {
        console.error("Lỗi khi lưu sản phẩm:", error);
        if (error.response?.status === 400) {
          const errorData = error.response.data.message;
          Object.keys(errors).forEach((k) => (errors[k] = ""));
          errorsChiTiet.forEach((err, index) => {
            Object.keys(err).forEach((k) => (err[k] = ""));
          });
          Object.keys(errorsMauHinhAnh).forEach((k) => (errorsMauHinhAnh[k] = ""));

          let errorMessages = [];

          if (Array.isArray(errorData)) {
            errorData.forEach((err) => {
              const field = err.field;
              const message = err.error;
              if (field.startsWith("sanPhamChiTiets[")) {
                const match = field.match(/sanPhamChiTiets\[(\d+)\]\.(.+)/);
                if (match) {
                  const index = parseInt(match[1]);
                  const subField = match[2] === "imeis" ? "imeisInput" : match[2];
                  if (index < errorsChiTiet.length) {
                    errorsChiTiet[index][subField] = message;
                    errorMessages.push(`Biến thể ${index + 1}: ${message}`);
                  }
                }
              } else {
                errors[field] = message;
                errorMessages.push(message);
              }
            });
          } else if (typeof errorData === "object") {
            Object.entries(errorData).forEach(([field, message]) => {
              if (field.startsWith("sanPhamChiTiets[")) {
                const match = field.match(/sanPhamChiTiets\[(\d+)\]\.(.+)/);
                if (match) {
                  const index = parseInt(match[1]);
                  const subField = match[2] === "imeis" ? "imeisInput" : match[2];
                  if (index < errorsChiTiet.length) {
                    errorsChiTiet[index][subField] = message;
                    errorMessages.push(`Biến thể ${index + 1}: ${message}`);
                  }
                }
              } else {
                errors[field] = message;
                errorMessages.push(message);
              }
            });
          } else {
            errorMessages.push(errorData || "Lỗi xác thực không xác định");
          }

          const errorIndex = errorsChiTiet.findIndex((err) =>
            Object.values(err).some((e) => e)
          );
          if (errorIndex !== -1) {
            selectedChiTiet.value = errorIndex;
          }
          ElMessage.error(`Lỗi xác thực: ${errorMessages.join("; ")}`);
          scrollToError();
        } else {
          ElMessage.error("Lỗi hệ thống: " + (error.message || "Vui lòng thử lại"));
        }
      } finally {
        loading.submit = false;
      }
    };

    // Tải danh mục khi component được gắn
    onMounted(() => {
      fetchDanhMuc();
    });

    return {
      sanPham,
      sanPhamForm,
      nhaCungCaps,
      maus,
      roms,
      modelSanPhams,
      selectedChiTiet,
      selectedMaus,
      selectedRoms,
      danhSachTrangThaiSanPham,
      errors,
      errorsChiTiet,
      errorsMauHinhAnh,
      hinhAnhTheoMau,
      giaBanChung,
      loading,
      generateVariants,
      applyGiaBanChung,
      removeChiTiet,
      selectChiTiet,
      getMauSacLabels,
      getRomLabels,
      handleMauFileChange,
      handleMauFileRemove,
      submitForm,
      capNhatSoLuong,
      handleImeiFileChange,
      handleImeiInput,
      onModelChange,
      handleExceed,
      clearImeiInput,
      router,
    };
  },
};
</script>

<style scoped>
.product-form {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
  background: var(--el-bg-color);
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.section-card {
  margin-bottom: 24px;
  border-radius: 8px;
  transition: all 0.3s;
}

.section-card:hover {
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
}

.card-header {
  font-size: 18px;
  font-weight: 600;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
  gap: 8px;
}

.card-header .el-icon {
  font-size: 20px;
  vertical-align: middle;
}

.el-form-item {
  margin-bottom: 24px;
  position: relative;
}

.el-form-item__label {
  font-weight: 500;
  color: var(--el-text-color-primary);
  font-size: 14px;
}

.el-form-item__error {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  font-weight: 500;
  color: var(--el-color-danger);
  background: var(--el-color-danger-light-9);
  padding: 4px 8px;
  border-radius: 4px;
  position: relative;
  top: 4px;
  z-index: 10;
  animation: pulse 0.5s ease-in-out 2;
}

.el-form-item__error::before {
  content: "\e79d";
  font-family: "element-icons" !important;
  font-size: 14px;
  color: var(--el-color-danger);
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }

  50% {
    transform: scale(1.05);
  }

  100% {
    transform: scale(1);
  }
}

.el-input,
.el-select,
.el-input-number {
  width: 100%;
}

.el-input__inner,
.el-select .el-input__inner,
.el-input-number .el-input__inner {
  border-radius: 6px;
  transition: all 0.3s;
}

.el-input__inner:focus,
.el-select .el-input__inner:focus,
.el-input-number .el-input__inner:focus {
  border-color: var(--el-color-primary);
  box-shadow: 0 0 0 2px var(--el-color-primary-light-8);
}

.el-button {
  border-radius: 6px;
  padding: 10px 20px;
  font-weight: 500;
  font-size: 14px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
}

.el-button--primary {
  background-color: #409EFF;
  color: #fff;
}

.el-button--primary:hover {
  background-color: #66B1FF;
  filter: brightness(110%);
}

.el-button--primary:active {
  background-color: #3a8ee6;
  transform: scale(0.98);
}

.el-button--success {
  background-color: #67C23A;
  color: #fff;
  padding: 12px 24px;
}

.el-button--success:hover {
  background-color: #85CE61;
  filter: brightness(110%);
}

.el-button--success:active {
  background-color: #5daf34;
  transform: scale(0.98);
}

.el-button--danger {
  background-color: #F56C6C;
  color: #fff;
}

.el-button--danger:hover {
  background-color: #F78989;
  filter: brightness(110%);
}

.el-button--danger:active {
  background-color: #dd6161;
  transform: scale(0.98);
}

.el-button--warning {
  background-color: #E6A23C;
  color: #fff;
}

.el-button--warning:hover {
  background-color: #EBB563;
  filter: brightness(110%);
}

.el-button--warning:active {
  background-color: #cf9236;
  transform: scale(0.98);
}

.el-button[plain] {
  background-color: transparent;
  border: 1px solid;
  color: inherit;
}

.el-button--primary[plain] {
  border-color: #409EFF;
  color: #409EFF;
}

.el-button--primary[plain]:hover {
  background-color: #ECF5FF;
  color: #66B1FF;
}

.el-button--warning[plain] {
  border-color: #E6A23C;
  color: #E6A23C;
}

.el-button--warning[plain]:hover {
  background-color: #FDF6EC;
  color: #EBB563;
}

.variant-table {
  border-radius: 8px;
  overflow: hidden;
}

.variant-table th {
  background: var(--el-fill-color-light);
  color: var(--el-text-color-primary);
  font-weight: 600;
}

.variant-table tr:hover {
  background: var(--el-fill-color-blank);
  cursor: pointer;
}

.variant-table .el-table__cell {
  padding: 0 5px;
}

.variant-table .el-image {
  width: 30px;
  height: 30px;
  margin-right: 3px;
}

.section-card .el-row {
  width: 100%;
  overflow-x: auto;
}

.el-upload__tip {
  font-size: 12px;
  color: var(--el-text-color-secondary);
  margin-top: 8px;
}

.el-upload--picture-card {
  border-radius: 6px;
}

.el-upload-list--picture-card .el-upload-list__item {
  border-radius: 6px;
  transition: all 0.3s;
}

.el-upload-list--picture-card .el-upload-list__item:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.variant-detail-card {
  position: relative;
  padding-bottom: 16px;
}

.action-buttons {
  margin-top: 12px;
  display: flex;
  gap: 12px;
  align-items: center;
  flex-wrap: nowrap;
}

.action-buttons .el-upload,
.action-buttons .el-button {
  flex: 0 0 auto;
  width: 150px;
  height: 36px;
  line-height: 36px;
  padding: 0;
  font-size: 13px;
}

.action-buttons .el-button {
  display: flex;
  align-items: center;
  justify-content: center;
}

.imei-status {
  font-size: 13px;
  color: var(--el-text-color-regular);
  margin-top: 12px;
  display: flex;
  align-items: center;
  gap: 16px;
}

.valid-message {
  color: var(--el-color-success);
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 4px;
}

.valid-message .el-icon {
  font-size: 14px;
}

.form-actions {
  text-align: right;
  margin-top: 24px;
}

.form-actions .el-button--success {
  width: 180px;
}

.empty-state {
  text-align: center;
  color: var(--el-text-color-secondary);
  font-size: 16px;
  padding: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

@media (max-width: 768px) {
  .product-form {
    padding: 16px;
  }

  .el-row {
    flex-direction: column;
  }

  .el-col {
    width: 100%;
    margin-bottom: 16px;
  }

  .el-button {
    width: 100%;
    font-size: 13px;
    padding: 8px 16px;
  }

  .variant-table {
    font-size: 12px;
  }

  .variant-table .el-table__cell {
    padding: 6px;
  }

  .variant-table .el-image {
    width: 24px;
    height: 24px;
  }

  .form-actions {
    text-align: center;
  }

  .form-actions .el-button--success {
    width: 100%;
    max-width: 300px;
  }

  .action-buttons {
    flex-direction: column;
    align-items: stretch;
    gap: 10px;
  }

  .action-buttons .el-upload,
  .action-buttons .el-button {
    width: 100%;
    max-width: 280px;
    margin: 0 auto;
    height: 40px;
    line-height: 40px;
  }

  .el-form-item__error {
    font-size: 12px;
    padding: 3px 6px;
  }
}

@media (max-width: 480px) {
  .el-form-item__label {
    font-size: 13px;
    line-height: 1.5;
  }

  .card-header {
    font-size: 16px;
  }

  .el-button {
    font-size: 12px;
    padding: 8px 12px;
  }

  .action-buttons .el-upload,
  .action-buttons .el-button {
    max-width: 240px;
    height: 36px;
    line-height: 36px;
  }

  .form-actions .el-button--success {
    max-width: 240px;
  }

  .el-form-item__error {
    font-size: 11px;
    padding: 2px 4px;
  }
}
</style>
```