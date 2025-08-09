<template>
  <!-- Header Section -->
  <div class="page-header">
    <div class="header-container">
      <div class="header-left">
        <div class="header-icon">
          <el-icon size="32" color="#409EFF">
            <Box />
          </el-icon>
        </div>
        <div class="header-info">
          <h1 class="header-title">Quản lý sản phẩm</h1>
          <p class="header-subtitle">Thêm sản phẩm mới vào hệ thống</p>
        </div>
      </div>
    </div>
  </div>

  <!-- Custom Notification -->
  <div
    v-if="notification.visible"
    class="custom-notification"
    :class="`notification-${notification.type}`"
  >
    {{ notification.message }}
  </div>

  <!-- Main Form -->
  <el-form
    :model="sanPham"
    ref="sanPhamForm"
    label-width="140px"
    class="product-form"
  >
    <!-- Thông tin sản phẩm chính -->
    <el-card shadow="hover" class="section-card">
      <template #header>
        <div class="card-header">
          <span> <Document class="el-icon" /> Thông tin sản phẩm </span>
        </div>
      </template>
      <el-form-item
        label="Model sản phẩm"
        prop="idModelSanPham"
        :error="errors.idModelSanPham"
      >
        <el-select
          v-model="sanPham.idModelSanPham"
          placeholder="Chọn model sản phẩm"
          clearable
          filterable
          @change="onModelChange"
          style="width: 100%"
        >
          <el-option
            v-for="model in modelSanPhams"
            :key="model.idModelSanPham"
            :label="`${model.tenModel} - ${model.maXuatXu}`"
            :value="model.idModelSanPham"
          />
        </el-select>
      </el-form-item>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            label="Tên sản phẩm"
            prop="tenSanPham"
            :error="errors.tenSanPham"
          >
            <el-input
              v-model="sanPham.tenSanPham"
              placeholder="Nhập tên sản phẩm"
              clearable
              @input="errors.tenSanPham = ''"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="Thương hiệu"
            prop="thuongHieu"
            :error="errors.thuongHieu"
          >
            <el-input
              v-model="sanPham.thuongHieu"
              placeholder="Nhập thương hiệu"
              clearable
              @input="errors.thuongHieu = ''"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            label="Nhà cung cấp"
            prop="idNhaCungCap"
            :error="errors.idNhaCungCap"
          >
            <el-select
              v-model="sanPham.idNhaCungCap"
              placeholder="Chọn nhà cung cấp"
              clearable
              filterable
              @change="errors.idNhaCungCap = ''"
              style="width: 100%"
            >
              <el-option
                v-for="ncc in nhaCungCaps"
                :key="ncc.id"
                :label="ncc.tenNhaCungCap"
                :value="ncc.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="Trạng thái"
            prop="trangThaiSanPham"
            :error="errors.trangThaiSanPham"
          >
            <el-select
              v-model="sanPham.trangThaiSanPham"
              placeholder="Chọn trạng thái"
              clearable
              @change="errors.trangThaiSanPham = ''"
              style="width: 100%"
            >
              <el-option
                v-for="tt in danhSachTrangThaiSanPham"
                :key="tt.value"
                :label="tt.label"
                :value="tt.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-card>

    <!-- Chọn thuộc tính để tạo biến thể -->
    <el-card shadow="hover" class="section-card">
      <template #header>
        <div class="card-header">
          <span> <Setting class="el-icon" /> Tạo biến thể sản phẩm </span>
        </div>
      </template>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            label="Màu sắc"
            prop="selectedMaus"
            :error="errors.selectedMaus"
          >
            <div style="display: flex; gap: 8px; width: 100%">
              <el-select
                v-model="selectedMaus"
                multiple
                placeholder="Chọn màu sắc"
                clearable
                @change="errors.selectedMaus = ''"
                style="width: 100%"
              >
                <el-option
                  v-for="mau in maus"
                  :key="mau.id"
                  :label="mau.tenMau"
                  :value="mau.id"
                />
              </el-select>

              <el-button
                type="success"
                circle
                @click="addMauSacDialogRef.open()"
              >
                <el-icon>
                  <Plus />
                </el-icon>
              </el-button>
            </div>
          </el-form-item>
        </el-col>
        <DialogThemMauSac ref="addMauSacDialogRef" @saved="handleMauSacSaved" />

        <el-col :span="12">
          <el-form-item
            label="ROM"
            prop="selectedRoms"
            :error="errors.selectedRoms"
          >
            <div style="display: flex; gap: 8px; width: 100%">
              <el-select
                v-model="selectedRoms"
                multiple
                placeholder="Chọn ROM"
                clearable
                @change="errors.selectedRoms = ''"
                style="flex: 1"
              >
                <el-option
                  v-for="rom in roms"
                  :key="rom.id"
                  :label="rom.dungLuong"
                  :value="rom.id"
                />
              </el-select>

              <el-button type="success" circle @click="addRomDialogRef.open()">
                <el-icon>
                  <Plus />
                </el-icon>
              </el-button>
            </div>
          </el-form-item>
        </el-col>

        <DialogThemRom ref="addRomDialogRef" @saved="handleDungLuongRomSaved" />
      </el-row>
    </el-card>

    <!-- Thêm ảnh theo màu sắc -->
    <el-card shadow="hover" class="section-card">
      <template #header>
        <div class="card-header">
          <span> <Picture class="el-icon" /> Ảnh theo màu sắc </span>
        </div>
      </template>
      <el-row :gutter="20">
        <el-col :span="24" v-for="mau in selectedMaus" :key="mau">
          <el-form-item
            :label="getMauSacLabels(mau)"
            :error="errorsMauHinhAnh[mau] || ''"
          >
            <el-upload
              :file-list="hinhAnhTheoMau[mau] || []"
              :on-change="
                (file, fileList) => handleMauFileChange(file, fileList, mau)
              "
              :on-remove="
                (file, fileList) => handleMauFileRemove(file, fileList, mau)
              "
              :auto-upload="false"
              accept="image/jpeg,image/png,image/webp"
              list-type="picture-card"
              :limit="5"
              :on-exceed="handleExceed"
            >
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
          <span> <Money class="el-icon" /> Nhập giá nhanh </span>
        </div>
      </template>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            label="Giá bán chung"
            prop="giaBanChung"
            :error="errors.giaBanChung"
          >
            <el-input
              v-model="giaBanChungFormatted"
              placeholder="Nhập giá bán chung"
              style="width: 200px"
              @input="handlePriceInput"
              @change="errors.giaBanChung = ''"
              @blur="validatePrice"
            >
              <template #suffix>
                <span style="color: #909399; font-size: 12px">VND</span>
              </template>
            </el-input>
            <el-button
              type="primary"
              style="margin-left: 10px"
              @click="confirmApplyGiaBanChung"
            >
              <Check class="el-icon" /> Áp dụng
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
      <el-button
        type="primary"
        :loading="loading.generate"
        @click="generateVariants"
      >
        <Plus class="el-icon" />
        {{ loading.generate ? "Đang tạo..." : "Tạo biến thể" }}
      </el-button>
    </el-card>

    <!-- Danh sách chi tiết sản phẩm -->
    <el-card shadow="hover" class="section-card">
      <template #header>
        <div class="card-header">
          <span> <Tickets class="el-icon" /> Chi tiết sản phẩm </span>
        </div>
      </template>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-table
            :data="sanPham.sanPhamChiTiets"
            style="margin-top: 10px"
            highlight-current-row
            @row-click="selectChiTiet"
            class="variant-table"
          >
            <el-table-column
              label="STT"
              type="index"
              width="60"
              align="center"
            />
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
                {{
                  row.giaBan
                    ? row.giaBan.toLocaleString("vi-VN") + " VND"
                    : "Chưa nhập giá"
                }}
              </template>
            </el-table-column>
            <el-table-column label="Ảnh" width="100">
              <template #default="{ row }">
                <el-image
                  v-for="(hinh, index) in row.hinhAnhs.slice(0, 2)"
                  :key="hinh.imagePublicId"
                  :src="hinh.url"
                  :preview-src-list="[hinh.url]"
                  style="width: 30px; height: 30px; margin-right: 3px"
                  fit="cover"
                />
                <span v-if="row.hinhAnhs.length > 2"
                  >+{{ row.hinhAnhs.length - 2 }} ảnh</span
                >
                <span v-if="!row.hinhAnhs.length">Chưa có ảnh</span>
              </template>
            </el-table-column>
            <el-table-column
              label="Hành động"
              width="80"
              align="center"
              fixed="right"
            >
              <template #default="{ $index }">
                <el-button
                  type="danger"
                  size="small"
                  @click.stop="confirmRemoveChiTiet($index)"
                >
                  <Delete class="el-icon" />
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="12">
          <el-card
            v-if="selectedChiTiet !== null"
            shadow="hover"
            class="variant-detail-card"
          >
            <template #header>
              <div class="card-header">
                <span
                  >Thông tin biến thể {{ selectedChiTiet + 1 }} ({{
                    getMauSacLabels(
                      sanPham.sanPhamChiTiets[selectedChiTiet].idMau
                    )
                  }}
                  -
                  {{
                    getRomLabels(
                      sanPham.sanPhamChiTiets[selectedChiTiet].idRom
                    )
                  }})</span
                >
              </div>
            </template>
            <el-form-item
              label="Giá bán"
              :error="errorsChiTiet[selectedChiTiet]?.giaBan || ''"
              class="error-container"
            >
              <el-input
                v-model="variantPriceFormatted[selectedChiTiet]"
                placeholder="Nhập giá bán"
                style="width: 100%"
                @input="
                  (value) => handleVariantPriceInput(value, selectedChiTiet)
                "
                @change="errorsChiTiet[selectedChiTiet].giaBan = ''"
              >
                <template #suffix>
                  <span style="color: #909399; font-size: 12px">VND</span>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item
              label="Số lượng"
              :error="errorsChiTiet[selectedChiTiet]?.soLuong || ''"
              class="error-container"
            >
              <el-input-number
                v-model="sanPham.sanPhamChiTiets[selectedChiTiet].soLuong"
                :min="0"
                :disabled="true"
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item
              label="IMEI"
              :error="errorsChiTiet[selectedChiTiet]?.imeisInput || ''"
              class="error-container"
            >
              <el-input
                type="textarea"
                v-model="sanPham.sanPhamChiTiets[selectedChiTiet].imeisInput"
                placeholder="Nhập danh sách IMEI (15 chữ số mỗi IMEI, phân tách bởi dấu phẩy)"
                :rows="4"
                :disabled="
                  sanPham.sanPhamChiTiets[selectedChiTiet].isFileUploaded
                "
                @input="handleImeiInput(selectedChiTiet)"
              />
              <div class="imei-upload-container">
                <el-upload
                  :ref="`upload-${selectedChiTiet}`"
                  :file-list="imeiFileList[selectedChiTiet] || []"
                  :auto-upload="false"
                  :on-change="
                    (file, fileList) =>
                      handleFileSelection(file, selectedChiTiet, fileList)
                  "
                  accept=".xlsx,.xls,"
                  :disabled="
                    sanPham.sanPhamChiTiets[selectedChiTiet]?.imeisInput
                      .length > 0
                  "
                  list-type="text"
                  class="custom-upload"
                >
                  <template #trigger>
                    <el-button type="default" plain class="choose-file-btn">
                      <Upload class="el-icon" /> Chọn file
                    </el-button>
                  </template>
                  <template #default>
                    <span
                      v-if="
                        imeiFileList[selectedChiTiet] &&
                        imeiFileList[selectedChiTiet].length > 0
                      "
                      class="file-name"
                    >
                      {{ imeiFileList[selectedChiTiet][0].name }}
                    </span>
                    <span v-else class="file-placeholder">Chưa chọn file</span>
                  </template>
                </el-upload>
                <el-button
                  type="primary"
                  :disabled="
                    !(
                      imeiFileList[selectedChiTiet] &&
                      imeiFileList[selectedChiTiet].length > 0
                    )
                  "
                  @click="confirmUploadImeiFile(selectedChiTiet)"
                  class="upload-btn"
                >
                  <Upload class="el-icon" /> Upload
                </el-button>
                <el-button
                  type="warning"
                  plain
                  @click="confirmClearImeiInput(selectedChiTiet)"
                  class="clear-btn"
                >
                  <Delete class="el-icon" /> Xóa IMEI
                </el-button>
              </div>
              <div class="imei-status">
                <span
                  >Số lượng IMEI:
                  {{ sanPham.sanPhamChiTiets[selectedChiTiet].soLuong }}</span
                >
                <el-tooltip
                  v-if="errorsChiTiet[selectedChiTiet]?.imeisInput === 'Hợp lệ'"
                  content="Danh sách IMEI hợp lệ"
                  placement="top"
                >
                  <span class="valid-message">
                    <Check class="el-icon" /> Hợp lệ
                  </span>
                </el-tooltip>
              </div>
            </el-form-item>
          </el-card>
          <el-card v-else shadow="hover">
            <div class="empty-state">
              <InfoFilled class="el-icon" /> Vui lòng chọn hoặc thêm biến thể
              sản phẩm
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- Nút lưu sản phẩm -->
    <div class="form-actions">
      <el-button
        type="success"
        :loading="loading.submit"
        @click="confirmSubmitForm"
      >
        <Check class="el-icon" /> Lưu sản phẩm
      </el-button>
    </div>
  </el-form>
</template>

<script>
import { onMounted, reactive, ref, nextTick, computed, watch } from "vue";
import { ElMessageBox, ElLoading } from "element-plus";
import {
  getAllMauSacList,
  getAllModelSanPhamList,
  getAllNhaCungCapList,
  getAllRomList,
  getAllXuatXuList,
  postSanPham,
} from "@/Service/Adminservice/Products/ProductAdminService";
import { debounce } from "chart.js/helpers";
import api from "@/Service/LoginService/axiosInstance";
import * as XLSX from "xlsx";
import {
  Document,
  Setting,
  Picture,
  Money,
  Upload,
  Check,
  Delete,
  Tickets,
  InfoFilled,
  Plus,
  Box,
} from "@element-plus/icons-vue";
import { useRouter } from "vue-router";
import DialogThemMauSac from "@/components/Admin/dialogs/DialogThemMauSac.vue";
import DialogThemRom from "@/components/Admin/dialogs/DialogThemRom.vue";

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
    Plus,
    Box,
    DialogThemMauSac,
    DialogThemRom,
  },
  setup() {
    const router = useRouter();
    const sanPham = reactive({
      tenSanPham: "",
      thuongHieu: "Apple",
      idNhaCungCap: "",
      trangThaiSanPham: "",
      idModelSanPham: null,
      sanPhamChiTiets: [],
      idMau: null,
    });

    const loading = reactive({
      generate: false,
      submit: false,
    });

    const sanPhamForm = ref(null);
    const selectedMaus = ref([]);
    const selectedRoms = ref([]);
    const nhaCungCaps = ref([]);
    const maus = ref([]);
    const roms = ref([]);
    const modelSanPhams = ref([]);
    const selectedChiTiet = ref(null);
    const hinhAnhTheoMau = reactive({});
    const imeiFileList = reactive({}); // Quản lý file-list cho từng biến thể
    const addMauSacDialogRef = ref(null);
    const addRomDialogRef = ref(null);

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

    const handleMauSacSaved = (savedMauSac) => {
      maus.value.push({
        idMau: savedMauSac.id,
        tenMau: savedMauSac.tenMau,
        maMau: savedMauSac.maMau,
      });
      selectedMaus.value.push(savedMauSac.idMau);
    };

    const handleDungLuongRomSaved = (savedDungLuong) => {
      roms.value.push({
        idRom: savedDungLuong.id,
        dungLuong: savedDungLuong.dungLuong,
      });
      selectedRoms.value.push(savedDungLuong.idRom);
    };

    // Thêm trạng thái cho thông báo tùy chỉnh
    const notification = reactive({
      visible: false,
      message: "",
      type: "success", // Có thể là 'success', 'error', 'warning', 'info'
      timeout: null,
    });

    const danhSachTrangThaiSanPham = [
      { label: "Đang kinh doanh", value: "ACTIVE" },
      { label: "Sắp ra mắt", value: "COMING_SOON" },
    ];

    // Hàm hiển thị thông báo tùy chỉnh
    const showNotification = (message, type = "success", duration = 3000) => {
      notification.message = message;
      notification.type = type;
      notification.visible = true;
      clearTimeout(notification.timeout);
      notification.timeout = setTimeout(() => {
        notification.visible = false;
      }, duration);
    };

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
          showNotification(
            `Không thể tải danh mục: ${emptyLists
              .map((item) => item.name)
              .join(", ")}`,
            "error",
            3000
          );
        }
      } catch (error) {
        showNotification(
          "Lỗi khi tải danh mục: " + error.message,
          "error",
          3000
        );
      } finally {
        loadingInstance.close();
      }
    };

    const onModelChange = () => {
      errors.idModelSanPham = "";
      errors.tenSanPham = "";
      const selectedModel = modelSanPhams.value.find(
        (m) => m.idModelSanPham === sanPham.idModelSanPham
      );
      if (selectedModel) {
        sanPham.tenSanPham = selectedModel.tenModel;
      } else {
        sanPham.tenSanPham = "";
      }
      nextTick(() => {
        scrollToError(); // Đảm bảo cuộn đến phần lỗi nếu còn
      });
    };

    const handleExceed = () => {
      showNotification(
        "Chỉ được tải lên tối đa 5 ảnh cho mỗi màu!",
        "warning",
        3000
      );
    };

    const generateVariants = async () => {
  loading.generate = true;
  let hasError = false;
  const errorMessages = [];
  console.log("Starting generateVariants", {
    sanPham,
    selectedMaus: selectedMaus.value,
    selectedRoms: selectedRoms.value,
    hinhAnhTheoMau,
  });

  // TẠM THỜI COMMENT TẤT CẢ VALIDATION ĐỂ TEST
  // if (!validateForm()) {
  //   Object.keys(errors).forEach((key) => {
  //     if (errors[key]) {
  //       errorMessages.push(errors[key]);
  //     }
  //   });
  //   hasError = true;
  // }

  if (!selectedMaus.value.length) {
    errors.selectedMaus = "Vui lòng chọn ít nhất một màu sắc";
    errorMessages.push("Chọn ít nhất một màu sắc");
    hasError = true;
  }
  if (!selectedRoms.value.length) {
    errors.selectedRoms = "Vui lòng chọn ít nhất một ROM";
    errorMessages.push("Chọn ít nhất một ROM");
    hasError = true;
  }

  // selectedMaus.value.forEach((mau) => {
  //   if (!hinhAnhTheoMau[mau] || !hinhAnhTheoMau[mau].length) {
  //     errorsMauHinhAnh[mau] = "Phải có ít nhất 1 hình ảnh cho màu này";
  //     errorMessages.push(`Màu ${getMauSacLabels(mau)}: Phải có ít nhất 1 hình ảnh`);
  //     hasError = true;
  //   }
  // });

  if (hasError) {
    loading.generate = false;
    await nextTick();
    showNotification(errorMessages.join("; "), "error", 5000);
    scrollToError();
    return;
  }

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
          imeis: existingVariant?.imeis || [],
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

  const duplicateCombinations = [];
  for (const variant of newVariants) {
    try {
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), 5000);
      const res = await api.get(
        "/admin/sanPhamChiTiet/check-duplicate-variant",
        {
          params: {
            idSp: sanPham.idModelSanPham,
            idMau: variant.idMau,
            idRom: variant.idRom,
            idLoai: sanPham.idModelSanPham,
          },
          signal: controller.signal,
        }
      );
      clearTimeout(timeoutId);
      if (res.data.exists) {
        duplicateCombinations.push(
          `${getMauSacLabels(variant.idMau)} - ${getRomLabels(
            variant.idRom
          )}`
        );
      }
    } catch (err) {
      console.error("Lỗi kiểm tra biến thể từ backend:", err);
      if (err.name === "AbortError") {
        showNotification(
          "Yêu cầu kiểm tra biến thể quá thời gian, vui lòng thử lại.",
          "error",
          3000
        );
      } else {
        showNotification(
          "Lỗi kiểm tra trùng biến thể từ hệ thống: " + err.message,
          "error",
          3000
        );
      }
      loading.generate = false;
      return;
    }
  }

  if (duplicateCombinations.length > 0) {
    showNotification(
      `Không thể tạo các biến thể đã tồn tại: ${duplicateCombinations.join(
        ", "
      )}`,
      "error",
      1000
    );
    loading.generate = false;
    return;
  }

  sanPham.sanPhamChiTiets = newVariants;

  // Sync formatted values cho các biến thể mới tạo
  sanPham.sanPhamChiTiets.forEach((variant, index) => {
    if (variant.giaBan) {
      variantPriceFormatted[index] = variant.giaBan.toLocaleString("vi-VN");
    } else {
      variantPriceFormatted[index] = "";
    }
  });

  // BỎ PHẦN VALIDATION GIÁ BÁN VÀ IMEI CHO TỪNG BIẾN THỂ
  // Chỉ cập nhật số lượng mà không validate
  sanPham.sanPhamChiTiets.forEach((_, index) => {
    capNhatSoLuong(index, true);
    // Bỏ phần validation IMEI ở đây
  });

  selectedChiTiet.value = sanPham.sanPhamChiTiets.length > 0 ? 0 : null;
  setTimeout(() => {
    const table = document.querySelector(".variant-table");
    if (table) {
      table.scrollIntoView({ behavior: "smooth" });
    }
  }, 100);

  loading.generate = false;
  showNotification(
    `Đã tạo ${sanPham.sanPhamChiTiets.length} biến thể sản phẩm`,
    "success",
    3000
  );
};

    const variantPriceFormatted = reactive({});

    // Hàm xử lý input cho giá bán biến thể
    const handleVariantPriceInput = (value, index) => {
      const numericValue = value.replace(/[^\d]/g, "");

      if (numericValue === "") {
        variantPriceFormatted[index] = "";
        sanPham.sanPhamChiTiets[index].giaBan = null;
        return;
      }

      const numberValue = parseInt(numericValue, 10);
      variantPriceFormatted[index] = numberValue.toLocaleString("vi-VN");
      sanPham.sanPhamChiTiets[index].giaBan = numberValue;

      if (errorsChiTiet[index]) {
        errorsChiTiet[index].giaBan = "";
      }
    };

    const giaBanChungFormatted = ref("");

    // Hàm xử lý input cho el-input
    const handlePriceInput = (value) => {
      // Chỉ cho phép nhập số
      const numericValue = value.replace(/[^\d]/g, "");

      if (numericValue === "") {
        giaBanChungFormatted.value = "";
        giaBanChung.value = null;
        return;
      }

      const numberValue = parseInt(numericValue, 10);

      // Format với dấu chấm phân cách
      giaBanChungFormatted.value = numberValue.toLocaleString("vi-VN");
      giaBanChung.value = numberValue;

      // Xóa lỗi khi người dùng nhập
      errors.giaBanChung = "";
    };

    const validatePrice = () => {
      if (giaBanChung.value && giaBanChung.value < 1000) {
        errors.giaBanChung = "Giá bán phải lớn hơn hoặc bằng 1.000 VND";
      } else if (giaBanChung.value && giaBanChung.value > 100000000) {
        errors.giaBanChung = "Giá bán không được vượt quá 100.000.000 VND";
      } else {
        errors.giaBanChung = "";
      }
    };

    // Watch để đồng bộ giữa giaBanChung và giaBanChungFormatted
    watch(giaBanChung, (newValue) => {
      if (newValue === null || newValue === undefined || newValue === "") {
        giaBanChungFormatted.value = "";
      } else {
        giaBanChungFormatted.value = newValue.toLocaleString("vi-VN");
      }
    });

    // Cập nhật confirmApplyGiaBanChung để sử dụng với input tùy chỉnh
    const confirmApplyGiaBanChung = () => {
      if (!sanPham.sanPhamChiTiets.length) {
        showNotification("Vui lòng tạo biến thể trước!", "error", 3000);
        return;
      }

      // Validate giá trị
      if (!giaBanChung.value || giaBanChung.value < 1000) {
        errors.giaBanChung = "Giá bán chung phải lớn hơn hoặc bằng 1.000 VND";
        showNotification("Vui lòng nhập giá bán chung hợp lệ!", "error", 3000);
        return;
      }

      if (giaBanChung.value > 100000000) {
        errors.giaBanChung = "Giá bán không được vượt quá 100.000.000 VND";
        showNotification("Giá bán quá cao!", "error", 3000);
        return;
      }

      ElMessageBox.confirm(
        `Áp dụng giá ${giaBanChung.value.toLocaleString(
          "vi-VN"
        )} VND cho tất cả biến thể?`,
        "Xác nhận",
        {
          confirmButtonText: "Áp dụng",
          cancelButtonText: "Hủy",
          type: "warning",
        }
      )
        .then(() => {
          applyGiaBanChung();
        })
        .catch(() => {
          showNotification("Đã hủy áp dụng giá chung.", "info", 3000);
        });
    };

    const applyGiaBanChung = () => {
      sanPham.sanPhamChiTiets.forEach((chiTiet, index) => {
        chiTiet.giaBan = giaBanChung.value;
        errorsChiTiet[index].giaBan = "";

        // Sync formatted value cho input giá bán biến thể
        variantPriceFormatted[index] =
          giaBanChung.value.toLocaleString("vi-VN");
      });
      showNotification("Đã áp dụng giá bán chung!", "success", 3000);
    };

    const capNhatSoLuong = (index, validate = false) => {
      const update = () => {
        const imeis = sanPham.sanPhamChiTiets[index].isFileUploaded
          ? sanPham.sanPhamChiTiets[index].imeis
          : sanPham.sanPhamChiTiets[index].imeisInput
              .split(",")
              .map((i) => i.trim())
              .filter((i) => i);
        sanPham.sanPhamChiTiets[index].soLuong = imeis.length;
        if (validate) {
          if (imeis.length === 0) {
            errorsChiTiet[index].imeisInput = "Phải có ít nhất 1 IMEI";
            errorsChiTiet[index].soLuong = "Số lượng phải lớn hơn 0";
          } else {
            const allImeis = sanPham.sanPhamChiTiets.flatMap((chiTiet, i) =>
              i !== index
                ? chiTiet.isFileUploaded
                  ? chiTiet.imeis
                  : chiTiet.imeisInput
                      .split(",")
                      .map((im) => im.trim())
                      .filter((im) => im)
                : []
            );
            const duplicateImeis = imeis.filter((im) => allImeis.includes(im));
            if (duplicateImeis.length > 0) {
              errorsChiTiet[index].imeisInput = `${
                duplicateImeis.length
              } IMEI trùng lặp: ${duplicateImeis.slice(0, 3).join(", ")}${
                duplicateImeis.length > 3 ? "..." : ""
              }`;
              errorsChiTiet[index].soLuong = "";
            } else {
              const invalidImeis = imeis.filter((i) => !/^\d{15}$/.test(i));
              if (invalidImeis.length > 0) {
                errorsChiTiet[index].imeisInput = `${
                  invalidImeis.length
                } IMEI không hợp lệ: ${invalidImeis.slice(0, 3).join(", ")}${
                  invalidImeis.length > 3 ? "..." : ""
                }`;
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

    const handleImeiInput = (index) => {
      sanPham.sanPhamChiTiets[index].isFileUploaded = false;
      capNhatSoLuong(index, true);
      imeiFileList[index] = []; // Làm mới file-list khi nhập thủ công
    };

    const clearUploadFiles = (index) => {
      imeiFileList[index] = []; // Cập nhật file-list thủ công
      nextTick(() => {
        const uploadComponent = document.querySelector(
          `.el-upload[ref="upload-${index}"]`
        );
        if (uploadComponent && uploadComponent.__vue__) {
          uploadComponent.__vue__.clearFiles(); // Đảm bảo làm mới
        }
      });
    };

    const confirmClearImeiInput = (index) => {
      ElMessageBox.confirm(
        "Bạn có chắc muốn xóa toàn bộ IMEI của biến thể này?",
        "Xác nhận",
        {
          confirmButtonText: "Xóa",
          cancelButtonText: "Hủy",
          type: "warning",
        }
      )
        .then(() => {
          clearImeiInput(index);
        })
        .catch(() => {
          showNotification("Đã hủy xóa IMEI.", "info", 3000);
        });
    };

    const clearImeiInput = (index) => {
      sanPham.sanPhamChiTiets[index].imeisInput = "";
      sanPham.sanPhamChiTiets[index].soLuong = 0;
      sanPham.sanPhamChiTiets[index].isFileUploaded = false;
      sanPham.sanPhamChiTiets[index].imeis = [];
      errorsChiTiet[index].imeisInput = "";
      errorsChiTiet[index].soLuong = "";
      clearUploadFiles(index); // Xóa danh sách file
      showNotification("Đã xóa danh sách IMEI!", "success", 3000);
    };

    const handleFileSelection = (file, index, fileList) => {
      imeiFileList[index] = fileList; // Chỉ cập nhật file-list khi chọn file
      if (file.raw.size > 1024 * 1024) {
        showNotification(
          "File quá lớn, vui lòng chọn file dưới 1MB",
          "error",
          3000
        );
        imeiFileList[index] = [];
      } else if (
        ![
          "application/vnd.ms-excel",
          "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        ].includes(file.raw.type)
      ) {
        showNotification("Vui lòng chọn file .xlsx hoặc .xls", "error", 3000);
        imeiFileList[index] = [];
      }
    };

    const confirmUploadImeiFile = (index) => {
      if (selectedChiTiet.value === null || selectedChiTiet.value !== index) {
        showNotification(
          "Vui lòng chọn biến thể trước khi tải file IMEI!",
          "error",
          3000
        );
        return;
      }
      if (!imeiFileList[index] || imeiFileList[index].length === 0) {
        showNotification("Vui lòng chọn file trước khi upload!", "error", 3000);
        return;
      }
      ElMessageBox.confirm(
        `Bạn có chắc muốn upload file "${
          imeiFileList[index][0].name
        }" cho biến thể ${index + 1}?`,
        "Xác nhận",
        {
          confirmButtonText: "Upload",
          cancelButtonText: "Hủy",
          type: "warning",
        }
      )
        .then(() => {
          uploadImeiFile(index);
        })
        .catch(() => {
          showNotification("Đã hủy upload file.", "info", 3000);
        });
    };

    const uploadImeiFile = async (index) => {
      const file = imeiFileList[index][0];
      const loadingInstance = ElLoading.service({
        text: "Đang đọc file Excel...",
        background: "rgba(0, 0, 0, 0.7)",
      });
      try {
        const reader = new FileReader();
        reader.onload = (e) => {
          try {
            const data = new Uint8Array(e.target.result);
            const workbook = XLSX.read(data, { type: "array" });
            const sheet = workbook.Sheets[workbook.SheetNames[0]];
            const json = XLSX.utils.sheet_to_json(sheet, { header: 1 }); // Lấy dữ liệu thô
            const headers = json[0]; // Dòng đầu tiên là tiêu đề
            if (headers && headers[0] !== "soImei") {
              throw new Error(
                "Cột đầu tiên phải có tiêu đề 'soImei'. Vui lòng sửa file Excel và thử lại."
              );
            }
            const allImeis = json
              .slice(1)
              .map((row) => String(row[0]).trim())
              .filter((imei) => imei);
            const invalidImeis = allImeis.filter(
              (imei) => !/^\d{15}$/.test(imei)
            );
            if (invalidImeis.length > 0) {
              throw new Error(
                `Có ${invalidImeis.length} giá trị không hợp lệ: ${invalidImeis
                  .slice(0, 3)
                  .join(", ")}${
                  invalidImeis.length > 3 ? "..." : ""
                }. IMEI phải là số 15 chữ số. Vui lòng kiểm tra và sửa file.`
              );
            }
            const imeis = allImeis.filter((imei) => /^\d{15}$/.test(imei));
            if (imeis.length === 0) {
              throw new Error(
                "Không tìm thấy IMEI hợp lệ trong file. Vui lòng đảm bảo cột 'soImei' chứa số 15 chữ số và không có hàng trống."
              );
            }
            const allExistingImeis = sanPham.sanPhamChiTiets.flatMap(
              (chiTiet, i) =>
                i !== index
                  ? chiTiet.isFileUploaded
                    ? chiTiet.imeis
                    : chiTiet.imeisInput
                        .split(",")
                        .map((im) => im.trim())
                        .filter((im) => im)
                  : []
            );
            const duplicateImeis = imeis.filter((im) =>
              allExistingImeis.includes(im)
            );
            if (duplicateImeis.length > 0) {
              throw new Error(
                `${duplicateImeis.length} IMEI trùng lặp: ${duplicateImeis
                  .slice(0, 3)
                  .join(", ")}${
                  duplicateImeis.length > 3 ? "..." : ""
                }. Vui lòng kiểm tra và loại bỏ IMEI trùng.`
              );
            }
            sanPham.sanPhamChiTiets[index].soLuong = imeis.length;
            sanPham.sanPhamChiTiets[index].isFileUploaded = true;
            sanPham.sanPhamChiTiets[index].imeis = imeis;
            sanPham.sanPhamChiTiets[index].imeisInput = imeis.join(", ");
            errorsChiTiet[index].imeisInput = "Hợp lệ";
            errorsChiTiet[index].soLuong = "";
            imeiFileList[index] = []; // Xóa file-list sau khi upload thành công
            showNotification(
              `Đã nhập ${imeis.length} IMEI từ file ${file.name} cho biến thể ${
                index + 1
              }`,
              "success",
              3000
            );
          } catch (error) {
            errorsChiTiet[index].imeisInput = error.message;
            showNotification(
              `Lỗi khi đọc file Excel: ${error.message} Vui lòng kiểm tra định dạng file (cột 'soImei', số 15 chữ số) và thử lại.`,
              "error",
              7000
            );
          } finally {
            loadingInstance.close();
          }
        };
        reader.onerror = () => {
          errorsChiTiet[index].imeisInput =
            "Lỗi khi đọc file Excel. Vui lòng chọn file hợp lệ (.xlsx hoặc .xls) và thử lại.";
          showNotification(
            "Lỗi khi đọc file Excel. Vui lòng chọn file hợp lệ (.xlsx hoặc .xls) và thử lại.",
            "error",
            3000
          );
          loadingInstance.close();
        };
        reader.readAsArrayBuffer(file.raw);
      } catch (error) {
        errorsChiTiet[index].imeisInput =
          "Lỗi khi xử lý file IMEI: " +
          error.message +
          ". Vui lòng thử lại với file đúng định dạng.";
        showNotification(
          "Lỗi khi xử lý file IMEI: " +
            error.message +
            ". Vui lòng thử lại với file đúng định dạng.",
          "error",
          3000
        );
        loadingInstance.close();
      }
    };

    const handleMauFileChange = async (file, fileList, idMau) => {
      const loadingInstance = ElLoading.service({
        target: `.el-upload[mau-id="${idMau}"]`,
        text: "Đang tải ảnh...",
      });
      try {
        if (
          !["image/jpeg", "image/png", "image/webp"].includes(file.raw.type)
        ) {
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
            item.uid === file.uid
              ? response.data.imagePublicId
              : item.imagePublicId,
        }));
        // errorsMauHinhAnh[idMau] =
        //   hinhAnhTheoMau[idMau].length > 0 ? "" : "Phải có ít nhất 1 hình ảnh";
        // sanPham.sanPhamChiTiets.forEach((chiTiet) => {
        //   if (chiTiet.idMau === idMau) {
        //     chiTiet.hinhAnhs = hinhAnhTheoMau[idMau];
        //   }
        // });
        showNotification(`Tải ảnh ${file.name} thành công!`, "success", 3000);
      } catch (error) {
        errorsMauHinhAnh[idMau] =
          "Lỗi khi tải ảnh: " +
          (error.response?.data?.message || error.message);
        showNotification(errorsMauHinhAnh[idMau], "error", 3000);
      } finally {
        loadingInstance.close();
      }
    };

    const handleMauFileRemove = (file, fileList, idMau) => {
      hinhAnhTheoMau[idMau] = fileList.map((item) => ({
        name: item.name,
        url: item.url,
        imagePublicId: item.imagePublicId,
      }));
      // errorsMauHinhAnh[idMau] =
      //   hinhAnhTheoMau[idMau].length > 0 ? "" : "Phải có ít nhất 1 hình ảnh";
      // sanPham.sanPhamChiTiets.forEach((chiTiet) => {
      //   if (chiTiet.idMau === idMau) {
      //     chiTiet.hinhAnhs = hinhAnhTheoMau[idMau];
      //   }
      // });
      showNotification(`Đã xóa ảnh ${file.name}`, "success", 3000);
    };

    const confirmRemoveChiTiet = (index) => {
      ElMessageBox.confirm("Bạn có chắc muốn xóa biến thể này?", "Xác nhận", {
        confirmButtonText: "Xóa",
        cancelButtonText: "Hủy",
        type: "warning",
      })
        .then(() => {
          removeChiTiet(index);
        })
        .catch(() => {
          showNotification("Đã hủy xóa biến thể.", "info", 3000);
        });
    };

    const removeChiTiet = (index) => {
      sanPham.sanPhamChiTiets.splice(index, 1);
      errorsChiTiet.splice(index, 1);
      delete imeiFileList[index]; // Xóa file-list của biến thể bị xóa
      if (selectedChiTiet.value === index) {
        selectedChiTiet.value = sanPham.sanPhamChiTiets.length > 0 ? 0 : null;
      } else if (selectedChiTiet.value > index) {
        selectedChiTiet.value--;
      }
      showNotification("Đã xóa biến thể!", "success", 3000);
    };

    const selectChiTiet = (row, column, event) => {
      const index = sanPham.sanPhamChiTiets.indexOf(row);
      selectedChiTiet.value = index;
      if (errorsChiTiet[index]) {
        Object.keys(errorsChiTiet[index]).forEach(
          (k) => (errorsChiTiet[index][k] = "")
        );
        capNhatSoLuong(index, true);
      }
      if (
        sanPham.sanPhamChiTiets[index].isFileUploaded &&
        sanPham.sanPhamChiTiets[index].imeis
      ) {
        sanPham.sanPhamChiTiets[index].imeisInput =
          sanPham.sanPhamChiTiets[index].imeis.join(", ");
      } else if (!sanPham.sanPhamChiTiets[index].imeisInput) {
        sanPham.sanPhamChiTiets[index].imeisInput = "";
        sanPham.sanPhamChiTiets[index].isFileUploaded = false;
        sanPham.sanPhamChiTiets[index].imeis = [];
      }
      imeiFileList[index] = imeiFileList[index] || []; // Đảm bảo file-list được khởi tạo
      if (!sanPham.sanPhamChiTiets[index].isFileUploaded) {
        imeiFileList[index] = []; // Làm mới file-list khi chọn biến thể
      }
      nextTick(() => {
        clearUploadFiles(index);
      });
    };

    const getMauSacLabels = (idMau) => {
      if (!Array.isArray(idMau)) idMau = [idMau];
      return idMau
        .map(
          (id) =>
            maus.value.find((m) => String(m.id) === String(id))?.tenMau ||
            "Không xác định"
        )
        .join(", ");
    };

    const getRomLabels = (idRom) => {
      if (!Array.isArray(idRom)) idRom = [idRom];
      return idRom
        .map(
          (id) =>
            roms.value.find((r) => String(r.id) === String(id))?.dungLuong ||
            "Không xác định"
        )
        .join(", ");
    };

    const scrollToError = () => {
      setTimeout(() => {
        let errorElement = document
          .querySelector(".el-form-item__error")
          ?.closest(".el-form-item");
        if (!errorElement) {
          const firstMauError = Object.keys(errorsMauHinhAnh).find(
            (mau) => errorsMauHinhAnh[mau]
          );
          if (firstMauError) {
            errorElement = document
              .querySelector(`[mau-id="${firstMauError}"]`)
              ?.closest(".el-form-item");
          }
        }
        if (!errorElement) {
          const errorIndex = errorsChiTiet.findIndex((err) =>
            Object.values(err).some((e) => e)
          );
          if (errorIndex !== -1) {
            errorElement = document.querySelector(".variant-table");
            selectedChiTiet.value = errorIndex;
          }
        }
        if (errorElement) {
          errorElement.scrollIntoView({ behavior: "smooth", block: "center" });
        }
      }, 100);
    };

    const validateForm = () => {
      let hasError = false;
      if (!sanPham.idModelSanPham || !sanPham.tenSanPham) {
        errors.tenSanPham =
          "Vui lòng chọn model sản phẩm hoặc nhập tên sản phẩm";
        hasError = true;
      } else {
        errors.tenSanPham = "";
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
      // selectedMaus.value.forEach((mau) => {
      //   if (!hinhAnhTheoMau[mau] || !hinhAnhTheoMau[mau].length) {
      //     errorsMauHinhAnh[mau] = "Phải có ít nhất 1 hình ảnh cho màu này";
      //     hasError = true;
      //   }
      // });
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
          : chiTiet.imeisInput
              .split(",")
              .map((i) => i.trim())
              .filter((i) => i);
        if (imeis.length === 0) {
          errorsChiTiet[index].imeisInput = "Phải có ít nhất 1 IMEI";
          hasError = true;
        } else {
          const allImeis = sanPham.sanPhamChiTiets.flatMap((ct, i) =>
            i !== index
              ? ct.isFileUploaded
                ? ct.imeis
                : ct.imeisInput
                    .split(",")
                    .map((im) => im.trim())
                    .filter((im) => im)
              : []
          );
          const duplicateImeis = imeis.filter((im) => allImeis.includes(im));
          if (duplicateImeis.length > 0) {
            errorsChiTiet[index].imeisInput = `${
              duplicateImeis.length
            } IMEI trùng lặp: ${duplicateImeis.slice(0, 3).join(", ")}${
              duplicateImeis.length > 3 ? "..." : ""
            }`;
            hasError = true;
          } else {
            const invalidImeis = imeis.filter((i) => !/^\d{15}$/.test(i));
            if (invalidImeis.length > 0) {
              errorsChiTiet[index].imeisInput = `${
                invalidImeis.length
              } IMEI không hợp lệ: ${invalidImeis.slice(0, 3).join(", ")}${
                invalidImeis.length > 3 ? "..." : ""
              }`;
              hasError = true;
            }
          }
        }
      });
      return !hasError;
    };

    const confirmSubmitForm = async () => {
      loading.submit = true;
      if (!sanPham.sanPhamChiTiets.length) {
        showNotification(
          "Vui lòng tạo ít nhất một biến thể sản phẩm!",
          "error",
          3000
        );
        loading.submit = false;
        return;
      }
      ElMessageBox.confirm("Bạn có chắc muốn lưu sản phẩm này?", "Xác nhận", {
        confirmButtonText: "Lưu",
        cancelButtonText: "Hủy",
        type: "warning",
      })
        .then(() => {
          submitForm();
        })
        .catch(() => {
          loading.submit = false;
          showNotification("Đã hủy lưu sản phẩm.", "info", 3000);
        });
    };

    const submitForm = async () => {
      try {
        if (!validateForm()) {
          const errorIndex = errorsChiTiet.findIndex((err) =>
            Object.values(err).some((e) => e)
          );
          if (errorIndex !== -1) {
            selectedChiTiet.value = errorIndex;
          }
          showNotification("Vui lòng nhập đầy đủ thông tin", "error", 5000);
          scrollToError();
          loading.submit = false;
          return;
        }
        const payload = {
          tenSanPham: sanPham.tenSanPham,
          thuongHieu: sanPham.thuongHieu,
          idNhaCungCaps: [sanPham.idNhaCungCap],
          trangThaiSanPham: sanPham.trangThaiSanPham,
          idModelSanPham: sanPham.idModelSanPham,
          sanPhamChiTiets: sanPham.sanPhamChiTiets.map((chiTiet) => {
            const imeis = chiTiet.isFileUploaded
              ? chiTiet.imeis
              : chiTiet.imeisInput
                  .split(",")
                  .map((i) => i.trim())
                  .filter((i) => i);
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
        showNotification("Sản phẩm đã được lưu thành công!", "success", 3000);
        router.push({ name: "products" });
      } catch (error) {
        console.error("Lỗi khi lưu sản phẩm:", error);
        if (error.response?.status === 400) {
          const errorData = error.response.data.message;
          Object.keys(errors).forEach((k) => (errors[k] = ""));
          errorsChiTiet.forEach((err, index) => {
            Object.keys(err).forEach((k) => (err[k] = ""));
          });
          Object.keys(errorsMauHinhAnh).forEach(
            (k) => (errorsMauHinhAnh[k] = "")
          );
          let errorMessages = [];
          if (Array.isArray(errorData)) {
            errorData.forEach((err) => {
              const field = err.field;
              const message = err.error;
              if (field.startsWith("sanPhamChiTiets[")) {
                const match = field.match(/sanPhamChiTiets\[(\d+)\]\.(.+)/);
                if (match) {
                  const index = parseInt(match[1]);
                  const subField =
                    match[2] === "imeis" ? "imeisInput" : match[2];
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
                  const subField =
                    match[2] === "imeis" ? "imeisInput" : match[2];
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
          showNotification(
            `Lỗi xác thực: ${errorMessages.join("; ")}`,
            "error",
            5000
          );
          scrollToError();
        } else {
          showNotification(
            "Lỗi hệ thống: " + (error.message || "Vui lòng thử lại"),
            "error",
            3000
          );
        }
      } finally {
        loading.submit = false;
      }
    };

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
      DialogThemMauSac,
      DialogThemRom,
      addMauSacDialogRef,
      addRomDialogRef,
      variantPriceFormatted,
      handleVariantPriceInput,
      giaBanChungFormatted,
      handlePriceInput,
      validatePrice,
      handleMauSacSaved,
      handleDungLuongRomSaved,
      generateVariants,
      confirmApplyGiaBanChung,
      removeChiTiet,
      confirmRemoveChiTiet,
      selectChiTiet,
      getMauSacLabels,
      getRomLabels,
      handleMauFileChange,
      handleMauFileRemove,
      submitForm,
      confirmSubmitForm,
      capNhatSoLuong,
      handleImeiInput,
      onModelChange,
      handleExceed,
      clearImeiInput,
      confirmClearImeiInput,
      clearUploadFiles,
      imeiFileList,
      handleFileSelection,
      uploadImeiFile,
      confirmUploadImeiFile,
      router,
      notification, // Trả về thông báo để sử dụng trong template
      showNotification, // Hàm để gọi thông báo
    };
  },
};
</script>

<style scoped>
.page-header {
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  margin-bottom: 24px;
}

.header-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 16px 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  background: #f0f9ff;
  border-radius: 12px;
}

.header-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.header-title {
  font-size: 24px;
  font-weight: 700;
  color: #1f2937;
  margin: 0;
  line-height: 1.2;
}

.header-subtitle {
  font-size: 14px;
  color: #6b7280;
  margin: 0;
  line-height: 1.4;
}

/* Custom Notification Styles */
.custom-notification {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 12px 20px;
  border-radius: 6px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  z-index: 1000;
  animation: fadeIn 0.3s ease-in, fadeOut 0.3s ease-out 2.7s;
  animation-fill-mode: forwards;
  max-width: 300px;
  word-wrap: break-word;
}

.notification-success {
  background-color: #f0f9eb;
  color: #67c23a;
  border-left: 4px solid #67c23a;
}

.notification-error {
  background-color: #fef0f0;
  color: #f56c6c;
  border-left: 4px solid #f56c6c;
}

.notification-warning {
  background-color: #fdf6ec;
  color: #e6a23c;
  border-left: 4px solid #e6a23c;
}

.notification-info {
  background-color: #edf2fc;
  color: #409eff;
  border-left: 4px solid #409eff;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateX(100%);
  }

  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes fadeOut {
  from {
    opacity: 1;
    transform: translateX(0);
  }

  to {
    opacity: 0;
    transform: translateX(100%);
  }
}

/* Main Form Styles */
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
  background-color: #409eff;
  color: #fff;
}

.el-button--primary:hover {
  background-color: #66b1ff;
  filter: brightness(110%);
}

.el-button--primary:active {
  background-color: #3a8ee6;
  transform: scale(0.98);
}

.el-button--success {
  background-color: #67c23a;
  color: #fff;
  padding: 12px 24px;
}

.el-button--success:hover {
  background-color: #85ce61;
  filter: brightness(110%);
}

.el-button--success:active {
  background-color: #5daf34;
  transform: scale(0.98);
}

.el-button--danger {
  background-color: #f56c6c;
  color: #fff;
}

.el-button--danger:hover {
  background-color: #f78989;
  filter: brightness(110%);
}

.el-button--danger:active {
  background-color: #dd6161;
  transform: scale(0.98);
}

.el-button--warning {
  background-color: #e6a23c;
  color: #fff;
}

.el-button--warning:hover {
  background-color: #ebb563;
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
  border-color: #409eff;
  color: #409eff;
}

.el-button--primary[plain]:hover {
  background-color: #ecf5ff;
  color: #66b1ff;
}

.el-button--warning[plain] {
  border-color: #e6a23c;
  color: #e6a23c;
}

.el-button--warning[plain]:hover {
  background-color: #fdf6ec;
  color: #ebb563;
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

/* Custom styles for IMEI upload */
.imei-upload-container {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-top: 12px;
  flex-wrap: wrap;
}

.custom-upload {
  display: flex;
  align-items: center;
  width: auto;
}

.choose-file-btn {
  padding: 8px 16px;
  font-size: 13px;
}

.file-name {
  margin-left: 10px;
  color: var(--el-text-color-regular);
  font-size: 13px;
}

.file-placeholder {
  margin-left: 10px;
  color: var(--el-text-color-secondary);
  font-size: 13px;
}

.upload-btn,
.clear-btn {
  padding: 8px 16px;
  font-size: 13px;
  height: 36px;
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

/* Responsive Styles */
@media (max-width: 768px) {
  .header-container {
    flex-direction: column;
    gap: 16px;
    align-items: flex-start;
  }

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

  .imei-upload-container {
    flex-direction: column;
    align-items: stretch;
    gap: 10px;
  }

  .upload-btn,
  .clear-btn {
    width: 100%;
    max-width: 280px;
    margin: 0 auto;
  }
}

@media (max-width: 480px) {
  .header-title {
    font-size: 20px;
  }

  .header-subtitle {
    font-size: 13px;
  }

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

  .upload-btn,
  .clear-btn {
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
