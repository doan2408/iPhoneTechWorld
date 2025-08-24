<template>
  <div class="container mt-4">
    <div class="form-header">
      <h2>Cập nhật sản phẩm</h2>
      <div class="header-divider"></div>
    </div>

    <el-form :model="sanPhamModel" ref="sanPhamForm" label-width="120px" :rules="rules" class="professional-form">
      <!-- Thông tin sản phẩm chính -->
      <div class="form-section">
        <h3 class="section-title">Thông tin cơ bản</h3>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Tên sản phẩm" prop="tenSanPham">
              <el-input v-model="sanPhamModel.tenSanPham" placeholder="Nhập tên sản phẩm"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Thương hiệu" prop="thuongHieu">
              <el-input v-model="sanPhamModel.thuongHieu" placeholder="Nhập thương hiệu"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Nhà cung cấp" prop="idNhaCungCap">
              <el-select v-model="sanPhamModel.idNhaCungCaps" multiple placeholder="Chọn nhà cung cấp"
                style="width: 100%">
                <el-option v-for="ncc in nhaCungCaps" :key="ncc.id" :label="ncc.tenNhaCungCap"
                  :value="ncc.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Trạng thái" prop="trangThaiSanPham">
              <el-select v-model="sanPhamModel.trangThaiSanPham" placeholder="Chọn trạng thái" style="width: 100%">
                <el-option v-for="tt in danhSachTrangThaiSanPham" :key="tt.value" :label="tt.label"
                  :value="tt.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Model sản phẩm" prop="idModelSanPham">
              <el-select v-model="sanPhamModel.idModelSanPham" placeholder="Chọn model sản phẩm" style="width: 100%">
                <el-option v-for="model in modelSanPhams" :key="model.id" :label="model.tenModel"
                  :value="model.idModelSanPham"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </div>

      <!-- Danh sách sản phẩm chi tiết -->
      <div class="form-section">
        <h3 class="section-title">Danh sách biến thể sản phẩm</h3>
        <div class="table-container">
          <el-table :data="sanPhamModel.sanPhamChiTiets" class="professional-table" border @row-click="selectChiTiet" :row-class-name="getRowClassName">
            <el-table-column type="index" label="STT" width="80" :index="indexMethod" />
            <el-table-column label="Mã sản phẩm chi tiết" sortable prop="maSanPhamChiTiet" width="180">
              <template #default="scope">
                <span class="table-cell-content">{{ scope.row.maSanPhamChiTiet }}</span>
              </template>
            </el-table-column>
            <el-table-column label="Màu sắc" prop="idMau">
              <template #default="{ row }">
                <span class="table-cell-content">{{
                  getMauSacLabel(row.idMau)
                }}</span>
              </template>
            </el-table-column>
            <el-table-column label="ROM" prop="idRom">
              <template #default="{ row }">
                <span class="table-cell-content">{{
                  getRomLabel(row.idRom)
                }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="soLuong" label="Số lượng">
              <template #default="{ row }">
                <el-tag type="info" size="small">{{ row.soLuong }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="giaBan" label="Giá bán">
              <template #default="{ row }">
                <span class="price-cell">{{
                  new Intl.NumberFormat("vi-VN", {
                    style: "currency",
                    currency: "VND",
                  }).format(row.giaBan)
                }}</span>
              </template>
            </el-table-column>
            <el-table-column label="Hình ảnh" width="120">
              <template #default="{ row }">
                <div class="table-images">
                  <div v-if="row.hinhAnhs && row.hinhAnhs.length > 0" class="image-preview-container">
                    <img :src="row.hinhAnhs[0].url" :alt="row.hinhAnhs[0].name" class="table-image"
                      @click="previewTableImage(row.hinhAnhs)" />
                    <span v-if="row.hinhAnhs.length > 1" class="image-count">+{{ row.hinhAnhs.length - 1 }}</span>
                  </div>
                  <div v-else class="no-image">
                    <i class="el-icon-picture-outline"></i>
                    <span>Chưa có ảnh</span>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="Hành động" width="100" align="center">
              <template #default="{ $index }">
                <el-button type="danger" size="small" :icon="Delete" circle @click.stop="removeChiTiet($index)" />
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>

      <!-- Form chỉnh sửa chi tiết sản phẩm được chọn -->
      <div v-if="selectedChiTiet !== null" class="form-section edit-section">
        <h4 class="edit-title">
          <i class="el-icon-edit"></i>
          Chỉnh sửa biến thể {{ selectedChiTiet + 1 }}
        </h4>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Màu SPCT" :prop="`sanPhamChiTiets.${selectedChiTiet}.maSanPhamChiTiet`">
              <el-input v-model="sanPhamModel.sanPhamChiTiets[selectedChiTiet].maSanPhamChiTiet" disabled
                style="width: 100%">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Màu sắc" :prop="`sanPhamChiTiets.${selectedChiTiet}.idMau`"
              :rules="[{ required: true, message: 'Vui lòng chọn màu sắc' }]">
              <el-select v-model="sanPhamModel.sanPhamChiTiets[selectedChiTiet].idMau" placeholder="Chọn màu sắc"
                style="width: 100%">
                <el-option v-for="mau in maus" :key="mau.id" :label="mau.tenMau" :value="mau.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="ROM" :prop="`sanPhamChiTiets.${selectedChiTiet}.idRom`"
              :rules="[{ required: true, message: 'Vui lòng chọn ROM' }]">
              <el-select v-model="sanPhamModel.sanPhamChiTiets[selectedChiTiet].idRom" placeholder="Chọn ROM"
                style="width: 100%">
                <el-option v-for="rom in roms" :key="rom.id" :label="rom.dungLuong" :value="rom.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Giá bán" :prop="`sanPhamChiTiets.${selectedChiTiet}.giaBan`" :rules="[
              {
                required: true,
                message: 'Vui lòng nhập giá bán',
                type: 'number',
              },
            ]">
              <el-input-number v-model="sanPhamModel.sanPhamChiTiets[selectedChiTiet].giaBan" :min="1000" :precision="0"
                :step="1000000" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="IMEI" :prop="`sanPhamChiTiets.${selectedChiTiet}.imeisInput`"
          :rules="[{ required: true, message: 'Vui lòng nhập IMEI' }]">
          <el-input type="textarea" v-model="sanPhamModel.sanPhamChiTiets[selectedChiTiet].imeisInput"
            placeholder="Nhập danh sách IMEI, phân tách bởi dấu phẩy (có thể kết hợp với file Excel)"
            @input="capNhatSoLuong(selectedChiTiet)"></el-input>
          <div class="imei-upload-container">
            <el-upload :ref="`upload-${selectedChiTiet}`" :file-list="imeiFileList[selectedChiTiet] || []"
              :auto-upload="false"
              :on-change="(file, fileList) => handleImeiFileChange(file, selectedChiTiet, fileList)"
              :on-remove="(file, fileList) => handleImeiFileRemove(file, selectedChiTiet, fileList)"
              :on-exceed="() => toast.warning('Bạn chỉ được chọn 1 file. Vui lòng xóa file khác trước khi tiếp tục.')"
              :limit="1" accept=".xlsx,.xls,.txt" list-type="text" class="custom-upload">
              <template #trigger>
                <el-button type="default" plain class="choose-file-btn">
                  <i class="el-icon-upload"></i> Chọn file Excel
                </el-button>
              </template>

              <template #default>
                <span v-if="imeiFileList[selectedChiTiet] && imeiFileList[selectedChiTiet].length > 0"
                  class="file-name">
                  {{ imeiFileList[selectedChiTiet][0].name }}
                </span>
                <span v-else class="file-placeholder">Chưa chọn file</span>
              </template>
            </el-upload>

            <el-button type="primary" :disabled="!(
              imeiFileList[selectedChiTiet] &&
              imeiFileList[selectedChiTiet].length > 0
            )
              " @click="confirmUploadImeiFile(selectedChiTiet)" class="upload-btn">
              <i class="el-icon-upload"></i>
              {{
                sanPhamModel.sanPhamChiTiets[selectedChiTiet].imeisInput
                  ? "Thêm từ Excel"
                  : "Upload"
              }}
            </el-button>
            <el-button type="warning" plain @click="confirmClearImeiInput(selectedChiTiet)" class="clear-btn">
              <i class="el-icon-delete"></i> Xóa tất cả IMEI
            </el-button>
          </div>
          <div class="imei-info">
            <span class="imei-count">Tổng số IMEI:
              {{ sanPhamModel.sanPhamChiTiets[selectedChiTiet].soLuong }}</span>
            <span v-if="sanPhamModel.sanPhamChiTiets[selectedChiTiet].hasExcelData" class="excel-indicator">
              <i class="el-icon-document"></i> (Có dữ liệu từ Excel)
            </span>
            <span v-if="
              sanPhamModel.sanPhamChiTiets[selectedChiTiet].invalidImeis
                ?.length
            " class="imei-error">
              ({{
                sanPhamModel.sanPhamChiTiets[selectedChiTiet].invalidImeis
                  .length
              }}
              IMEI không hợp lệ:
              {{
                sanPhamModel.sanPhamChiTiets[selectedChiTiet].invalidImeis.join(
                  ", "
                )
              }})
            </span>
            <span v-if="
              sanPhamModel.sanPhamChiTiets[selectedChiTiet].duplicateImeis
                ?.length
            " class="imei-error">
              ({{
                sanPhamModel.sanPhamChiTiets[selectedChiTiet].duplicateImeis
                  .length
              }}
              IMEI trùng lặp:
              {{
                sanPhamModel.sanPhamChiTiets[
                  selectedChiTiet
                ].duplicateImeis.join(", ")
              }})
            </span>
          </div>
        </el-form-item>

        <!-- Hình ảnh sản phẩm -->
        <el-form-item label="Hình ảnh sản phẩm">
          <div class="image-upload-section">
            <!-- Current Images Display -->
            <div v-if="
              sanPhamModel.sanPhamChiTiets[selectedChiTiet].hinhAnhs.length >
              0
            " class="current-images">
              <h5 class="images-subtitle">
                Hình ảnh hiện tại ({{
                  sanPhamModel.sanPhamChiTiets[selectedChiTiet].hinhAnhs.length
                }}/5)
              </h5>
              <div class="images-grid">
                <div v-for="(image, imgIndex) in sanPhamModel.sanPhamChiTiets[
                  selectedChiTiet
                ].hinhAnhs" :key="imgIndex" class="image-item">
                  <div class="image-wrapper">
                    <img :src="image.url" :alt="image.name" class="preview-image"
                      @click="previewSingleImage(image.url, imgIndex)" />
                    <div class="image-overlay">
                      <el-button type="primary" size="small" :icon="View" circle class="overlay-btn view-btn"
                        @click="previewSingleImage(image.url, imgIndex)" />
                      <el-button type="danger" size="small" :icon="Delete" circle class="overlay-btn delete-btn"
                        @click="removeImage(selectedChiTiet, imgIndex)" />
                    </div>
                    <div class="image-info">
                      <span class="image-name">{{
                        truncateFileName(image.name)
                      }}</span>
                      <span class="image-size">{{ getImageSize(image) }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- Upload Area -->
            <div class="upload-area">
              <el-upload wool-upload ref="imageUpload" :file-list="[]" :on-change="(file, fileList) =>
                handleFileChange(file, fileList, selectedChiTiet)
                " :on-exceed="handleExceed" :before-upload="beforeImageUpload" :auto-upload="false"
                accept="image/jpeg,image/png,image/webp,image/jpg" :limit="5" multiple drag class="image-uploader">
                <div class="upload-content">
                  <i class="el-icon-upload upload-icon"></i>
                  <div class="upload-text">
                    <p class="upload-title">
                      Kéo thả hoặc <em>click để tải ảnh</em>
                    </p>
                    <p class="upload-subtitle">
                      Hỗ trợ: JPG, PNG, WEBP (tối đa 5 ảnh, mỗi ảnh < 5MB) </p>
                  </div>
                </div>
              </el-upload>

              <!-- Upload Progress -->
              <div v-if="uploadProgress.show" class="upload-progress">
                <el-progress :percentage="uploadProgress.percent" :status="uploadProgress.status" :stroke-width="8">
                  <template #default="{ percentage }">
                    <span class="progress-text">{{ uploadProgress.text }} {{ percentage }}%</span>
                  </template>
                </el-progress>
              </div>
            </div>
          </div>
        </el-form-item>
      </div>

      <div v-else class="form-section">
        <el-alert title="Vui lòng chọn một biến thể để chỉnh sửa" type="info" show-icon class="selection-alert" />
      </div>

      <!-- Nút hành động -->
      <div class="form-actions">
        <el-button type="success" size="large" :loading="loading.submit" @click="submitForm"
          class="action-btn primary-btn">
          <i class="el-icon-check"></i>
          Cập nhật sản phẩm
        </el-button>
        <el-button type="default" size="large" @click="$router.push('/admin/products')"
          class="action-btn secondary-btn">
          <i class="el-icon-close"></i>
          Hủy
        </el-button>
      </div>
    </el-form>

    <el-alert v-if="error" :title="error" type="error" show-icon class="error-alert" />

    <!-- Image Preview Dialog -->
    <el-dialog v-model="imagePreview.visible" title="Xem trước hình ảnh" width="80%" :before-close="closeImagePreview"
      class="image-preview-dialog">
      <div class="preview-container">
        <div class="preview-main">
          <img :src="imagePreview.currentImage" :alt="imagePreview.currentName" class="preview-main-image" />
        </div>
        <div v-if="imagePreview.images.length > 1" class="preview-thumbnails">
          <div v-for="(img, index) in imagePreview.images" :key="index" class="thumbnail-item"
            :class="{ active: index === imagePreview.currentIndex }" @click="changePreviewImage(index)">
            <img :src="img.url" :alt="img.name" class="thumbnail-image" />
          </div>
        </div>
      </div>
      <template #footer>
        <div class="preview-footer">
          <span class="image-counter">
            {{ imagePreview.currentIndex + 1 }} /
            {{ imagePreview.images.length }}
          </span>
          <div class="preview-actions">
            <el-button @click="prevImage" :disabled="imagePreview.currentIndex === 0">
              <i class="el-icon-arrow-left"></i> Trước
            </el-button>
            <el-button @click="nextImage" :disabled="imagePreview.currentIndex === imagePreview.images.length - 1
              ">
              Sau <i class="el-icon-arrow-right"></i>
            </el-button>
            <el-button type="primary" @click="closeImagePreview">Đóng</el-button>
          </div>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage, ElMessageBox, ElLoading } from "element-plus";
import { Delete, View } from "@element-plus/icons-vue";
import {
  getSanPhamById,
  getAllNhaCungCapList,
  getAllMauSacList,
  getAllRomList,
  putDataSanPham,
  getAllModelSanPhamList,
} from "@/Service/Adminservice/Products/ProductAdminService";
import { debounce } from "lodash";
import api from "@/Service/LoginService/axiosInstance";
import * as XLSX from "xlsx";
import { useToast } from "vue-toastification";
const toast = useToast();

const route = useRoute();
const router = useRouter();
const id = ref(null); // Sử dụng ref để lưu id

const sanPhamModel = reactive({
  id: null,
  maSanPham: "",
  tenSanPham: "",
  thuongHieu: "",
  idNhaCungCap: null,
  trangThaiSanPham: "",
  idModelSanPham: null,
  sanPhamChiTiets: [],
});

const sanPhamForm = ref(null);
const error = ref("");
const selectedChiTiet = ref(null);
const nhaCungCaps = ref([]);
const maus = ref([]);
const roms = ref([]);
const modelSanPhams = ref([]);
const loading = reactive({
  submit: false,
});
const imeiFileList = reactive({});

const imagePreview = reactive({
  visible: false,
  images: [],
  currentIndex: 0,
  currentImage: "",
  currentName: "",
});

const uploadProgress = reactive({
  show: false,
  percent: 0,
  status: "",
  text: "Đang tải lên",
});

const danhSachTrangThaiSanPham = [
  { label: "Đang kinh doanh", value: "ACTIVE" },
  { label: "Ngừng kinh doanh", value: "DISCONTINUED" },
  // { label: "Sắp ra mắt", value: "COMING_SOON" },
  // { label: "Tạm ngừng bán", value: "TEMPORARILY_UNAVAILABLE" },
  // { label: "Hết hàng", value: "OUT_OF_STOCK" },
];

const rules = {
  tenSanPham: [
    { required: true, message: "Vui lòng nhập tên sản phẩm", trigger: "blur" },
    {
      min: 3,
      max: 50,
      message: "Tên sản phẩm phải từ 3 đến 50 ký tự",
      trigger: "blur",
    },
  ],
  thuongHieu: [
    { required: true, message: "Vui lòng nhập thương hiệu", trigger: "blur" },
    {
      min: 3,
      max: 50,
      message: "Thương hiệu phải từ 3 đến 50 ký tự",
      trigger: "blur",
    },
  ],
  idNhaCungCaps: [
    {
      required: true,
      message: "Vui lòng chọn nhà cung cấp",
      trigger: "change",
    },
  ],
  trangThaiSanPham: [
    { required: true, message: "Vui lòng chọn trạng thái", trigger: "change" },
  ],
  idModelSanPham: [
    {
      required: true,
      message: "Vui lòng chọn model sản phẩm",
      trigger: "change",
    },
  ],
};

const validateIMEI = (imei) => /^\d{15}$/.test(imei);

const previewTableImage = (images) => {
  imagePreview.images = images;
  imagePreview.currentIndex = 0;
  imagePreview.currentImage = images[0].url;
  imagePreview.currentName = images[0].name;
  imagePreview.visible = true;
};

const previewSingleImage = (url, index) => {
  const images = sanPhamModel.sanPhamChiTiets[selectedChiTiet.value].hinhAnhs;
  imagePreview.images = images;
  imagePreview.currentIndex = index;
  imagePreview.currentImage = url;
  imagePreview.currentName = images[index].name;
  imagePreview.visible = true;
};

const changePreviewImage = (index) => {
  imagePreview.currentIndex = index;
  imagePreview.currentImage = imagePreview.images[index].url;
  imagePreview.currentName = imagePreview.images[index].name;
};

const nextImage = () => {
  if (imagePreview.currentIndex < imagePreview.images.length - 1) {
    changePreviewImage(imagePreview.currentIndex + 1);
  }
};

const prevImage = () => {
  if (imagePreview.currentIndex > 0) {
    changePreviewImage(imagePreview.currentIndex - 1);
  }
};

const closeImagePreview = () => {
  imagePreview.visible = false;
  imagePreview.images = [];
  imagePreview.currentIndex = 0;
  imagePreview.currentImage = "";
  imagePreview.currentName = "";
};

const handleImeiFileRemove = (file, index, fileList) => {
  try {
    // Cập nhật fileList sau khi xóa
    imeiFileList[index] = fileList;
    toast.success("Đã xóa file thành công!");
  } catch (error) {
    toast.error("Đã xảy ra lỗi khi xóa file: " + (error.message || "Vui lòng thử lại."));
    console.error("Lỗi khi xóa file:", error);
  }
};


const removeImage = async (variantIndex, imageIndex) => {
  try {
    await ElMessageBox.confirm(
      "Bạn có chắc chắn muốn xóa hình ảnh này?",
      "Xác nhận xóa",
      {
        confirmButtonText: "Xóa",
        cancelButtonText: "Hủy",
        type: "warning",
      }
    );

    sanPhamModel.sanPhamChiTiets[variantIndex].hinhAnhs.splice(imageIndex, 1);
    toast.success("Đã xóa hình ảnh thành công!");
  } catch {
    // User cancelled
  }
};

const truncateFileName = (fileName) => {
  if (fileName.length > 15) {
    return fileName.substring(0, 12) + "...";
  }
  return fileName;
};

const getImageSize = (image) => {
  return "~250KB";
};

const beforeImageUpload = (file) => {
  const isValidType = [
    "image/jpeg",
    "image/png",
    "image/webp",
    "image/jpg",
  ].includes(file.type);
  const isLt5M = file.size / 1024 / 1024 < 5;

  if (!isValidType) {
    toast.error("Chỉ hỗ trợ định dạng JPG, PNG, WEBP!");
    return false;
  }
  if (!isLt5M) {
    toast.error("Kích thước ảnh phải nhỏ hơn 5MB!");
    return false;
  }
  return true;
};

const handleExceed = () => {
  toast.warning("Chỉ được tải lên tối đa 5 ảnh cho mỗi biến thể!");
};

const handleFileChange = async (file, fileList, index) => {
  if (!beforeImageUpload(file.raw)) {
    return;
  }

  const currentImages = sanPhamModel.sanPhamChiTiets[index].hinhAnhs.length;
  if (currentImages >= 5) {
    toast.warning("Đã đạt giới hạn 5 ảnh cho biến thể này!");
    return;
  }

  try {
    uploadProgress.show = true;
    uploadProgress.percent = 0;
    uploadProgress.status = "";
    uploadProgress.text = "Đang tải lên";

    const formData = new FormData();
    formData.append("file", file.raw);

    const progressInterval = setInterval(() => {
      if (uploadProgress.percent < 90) {
        uploadProgress.percent += 10;
      }
    }, 200);

    const response = await api.post(
      "http://localhost:8080/admin/hinhAnh/upload",
      formData,
      {
        headers: { "Content-Type": "multipart/form-data" },
      }
    );

    clearInterval(progressInterval);
    uploadProgress.percent = 100;
    uploadProgress.status = "success";
    uploadProgress.text = "Tải lên thành công";

    sanPhamModel.sanPhamChiTiets[index].hinhAnhs.push({
      name: file.name,
      url: response.data.url,
      imagePublicId: response.data.imagePublicId,
    });

    toast.success(`Tải ảnh ${file.name} thành công!`);

    setTimeout(() => {
      uploadProgress.show = false;
    }, 1500);
  } catch (err) {
    uploadProgress.status = "exception";
    uploadProgress.text = "Tải lên thất bại";
    toast.error(
      "Lỗi khi tải ảnh: " + (err.response?.data?.message || err.message)
    );
    setTimeout(() => {
      uploadProgress.show = false;
    }, 2000);
  }
};

const handleImeiFileChange = (file, index, fileList) => {
  if (fileList.length > 1) {
    toast.warning("Bạn chỉ được chọn 1 file. Vui lòng xóa file khác trước khi tiếp tục.");
    return; // Ngăn không gán fileList vào imeiFileList
  }
  imeiFileList[index] = fileList;
  if (file.raw.size > 1024 * 1024) {
    toast.error("File quá lớn, vui lòng chọn file dưới 1MB.");
    imeiFileList[index] = [];
  } else if (
    ![
      "application/vnd.ms-excel",
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    ].includes(file.raw.type)
  ) {
    toast.error("Vui lòng chọn file có định dạng .xlsx hoặc .xls.");
    imeiFileList[index] = [];
  }
};

const confirmUploadImeiFile = (index) => {
  if (selectedChiTiet.value === null || selectedChiTiet.value !== index) {
    toast.error("Vui lòng chọn biến thể trước khi tải file IMEI!");
    return;
  }
  if (!imeiFileList[index] || imeiFileList[index].length === 0) {
    toast.error("Vui lòng chọn file trước khi upload!");
    return;
  }

  const existingCount = sanPhamModel.sanPhamChiTiets[index].imeisInput
    ? sanPhamModel.sanPhamChiTiets[index].imeisInput
      .split(",")
      .filter((im) => im.trim()).length
    : 0;

  const message =
    existingCount > 0
      ? `Bạn đã có ${existingCount} IMEI trong input. File "${imeiFileList[index][0].name}" sẽ được thêm vào danh sách hiện tại. Tiếp tục?`
      : `Bạn có chắc muốn upload file "${imeiFileList[index][0].name
      }" cho biến thể ${index + 1}?`;

  ElMessageBox.confirm(message, "Xác nhận", {
    confirmButtonText: "Upload",
    cancelButtonText: "Hủy",
    type: "warning",
  })
    .then(() => {
      uploadImeiFile(index);
    })
    .catch(() => {
      ElMessage.info("Đã hủy upload file.");
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
        const json = XLSX.utils.sheet_to_json(sheet, { header: 1 });
        const headers = json[0];

        if (headers && headers[0] !== "soImei") {
          throw new Error(
            'Cột đầu tiên phải có tiêu đề "soImei". Vui lòng sửa file Excel và thử lại.'
          );
        }

        // Lấy IMEI từ file Excel (loại bỏ các dòng trống)
        const newImeis = json
          .slice(1)
          .map((row) => String(row[0]).trim())
          .filter((imei) => imei && imei !== "undefined" && imei !== "null");

        console.log(`📋 IMEI từ file Excel: ${newImeis.length} mã`);

        // Lấy IMEI đã có từ input tay
        const existingImeisInput =
          sanPhamModel.sanPhamChiTiets[index].imeisInput || "";
        const existingImeis = existingImeisInput
          ? existingImeisInput
            .split(",")
            .map((im) => im.trim())
            .filter((im) => im)
          : [];

        console.log(`✏️ IMEI từ nhập tay: ${existingImeis.length} mã`);

        // Kết hợp IMEI từ cả hai nguồn
        const allImeis = [...existingImeis, ...newImeis];
        console.log(`🔗 Tổng IMEI sau khi kết hợp: ${allImeis.length} mã`);

        // Kiểm tra trùng lặp trong cùng biến thể (bao gồm cả IMEI cũ và mới)
        const uniqueImeis = [...new Set(allImeis)];
        const duplicateInSameVariant = allImeis.filter(
          (im, idx, arr) => arr.indexOf(im) !== idx
        );

        if (duplicateInSameVariant.length > 0) {
          throw new Error(
            `Có ${duplicateInSameVariant.length
            } IMEI trùng lặp: ${duplicateInSameVariant.slice(0, 3).join(", ")}${duplicateInSameVariant.length > 3 ? "..." : ""
            }`
          );
        }

        // Kiểm tra định dạng IMEI
        const invalidImeis = uniqueImeis.filter((imei) => !validateIMEI(imei));
        if (invalidImeis.length > 0) {
          throw new Error(
            `Có ${invalidImeis.length} giá trị không hợp lệ: ${invalidImeis
              .slice(0, 3)
              .join(", ")}${invalidImeis.length > 3 ? "..." : ""
            }. IMEI phải là số 15 chữ số.`
          );
        }

        // Kiểm tra trùng lặp với các biến thể khác
        const allOtherImeis = sanPhamModel.sanPhamChiTiets.flatMap(
          (chiTiet, i) =>
            i !== index
              ? (chiTiet.imeisInput || "")
                .split(",")
                .map((im) => im.trim())
                .filter((im) => im)
              : []
        );
        const duplicateImeis = uniqueImeis.filter((im) =>
          allOtherImeis.includes(im)
        );
        if (duplicateImeis.length > 0) {
          throw new Error(
            `Có ${duplicateImeis.length
            } IMEI trùng lặp với các biến thể khác: ${duplicateImeis
              .slice(0, 3)
              .join(", ")}${duplicateImeis.length > 3 ? "..." : ""}`
          );
        }

        // ✅ CẬP NHẬT: Cập nhật số lượng và danh sách IMEI
        const finalCount = uniqueImeis.length;
        sanPhamModel.sanPhamChiTiets[index].soLuong = finalCount;
        sanPhamModel.sanPhamChiTiets[index].imeisInput = uniqueImeis.join(", ");
        sanPhamModel.sanPhamChiTiets[index].invalidImeis = [];
        sanPhamModel.sanPhamChiTiets[index].duplicateImeis = [];

        // Đánh dấu là đã có dữ liệu từ Excel
        sanPhamModel.sanPhamChiTiets[index].hasExcelData = true;

        // Clear file list sau khi upload thành công
        imeiFileList[index] = [];

        // Thông báo thành công với thông tin chi tiết
        const addedCount = newImeis.length;
        const existingCount = existingImeis.length;

        ElMessage.success({
          message: `✅ Thành công!\n📁 Từ file: ${addedCount} IMEI\n✏️ Từ nhập tay: ${existingCount} IMEI\n📊 Tổng cộng: ${finalCount} IMEI cho biến thể ${index + 1
            }`,
          duration: 2000,
          showClose: true,
        });

        console.log(`✅ Upload thành công:`, {
          fromFile: addedCount,
          fromManual: existingCount,
          total: finalCount,
          variant: index + 1,
        });
      } catch (error) {
        console.error("❌ Lỗi khi xử lý file Excel:", error);
        sanPhamModel.sanPhamChiTiets[index].invalidImeis = [error.message];
        toast.error({
          message: `Lỗi khi đọc file Excel: ${error.message}`,
          duration: 2000,
        });
      } finally {
        loadingInstance.close();
      }
    };

    reader.onerror = () => {
      toast.error("Lỗi khi đọc file Excel.");
      loadingInstance.close();
    };

    reader.readAsArrayBuffer(file.raw);
  } catch (error) {
    toast.error("Lỗi khi xử lý file IMEI: " + error.message);
    loadingInstance.close();
  }
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
      ElMessage.info("Đã hủy xóa IMEI.");
    });
};

const clearImeiInput = (index) => {
  sanPhamModel.sanPhamChiTiets[index].imeisInput = "";
  sanPhamModel.sanPhamChiTiets[index].soLuong = 0;
  sanPhamModel.sanPhamChiTiets[index].isFileUploaded = false;
  sanPhamModel.sanPhamChiTiets[index].hasExcelData = false; // Reset flag
  sanPhamModel.sanPhamChiTiets[index].invalidImeis = [];
  sanPhamModel.sanPhamChiTiets[index].duplicateImeis = [];
  imeiFileList[index] = [];

  nextTick(() => {
    const uploadComponent = document.querySelector(
      `.el-upload[ref="upload-${index}"]`
    );
    if (uploadComponent && uploadComponent.__vue__) {
      uploadComponent.__vue__.clearFiles();
    }
  });

  toast.success("Đã xóa toàn bộ danh sách IMEI!");
};

const fetchSanPham = async (productId) => {
  try {
    // console.log('Fetching product with ID:', productId);
    const response = await getSanPhamById(productId);
    sanPhamModel.id = response.id;
    sanPhamModel.maSanPham = response.maSanPham || "";
    sanPhamModel.tenSanPham = response.tenSanPham;
    sanPhamModel.thuongHieu = response.thuongHieu;
    sanPhamModel.idNhaCungCaps = response.idNhaCungCaps;
    sanPhamModel.trangThaiSanPham = response.trangThaiSanPham;
    sanPhamModel.idModelSanPham = response.idModelSanPham;
    sanPhamModel.sanPhamChiTiets = response.sanPhamChiTiets.map((chiTiet) => ({
      id: chiTiet.id,
      idMau: chiTiet.idMau,
      idRom: chiTiet.idRom,
      soLuong: chiTiet.soLuong,
      giaBan: chiTiet.giaBan,
      maSanPhamChiTiet: chiTiet.maSanPhamChiTiet,
      imeisInput: chiTiet.imeis?.map((i) => i.soImei).join(", ") || "",
      isFileUploaded: false,
      hasExcelData: false,
      invalidImeis: [],
      duplicateImeis: [],
      hinhAnhs:
        chiTiet.hinhAnhs?.map((h) => ({
          name: h.url.split("/").pop(),
          url: h.url,
          imagePublicId: h.imagePublicId,
        })) || [],
    }));
    selectedChiTiet.value = sanPhamModel.sanPhamChiTiets.length > 0 ? 0 : null;
  } catch (err) {
    error.value =
      err.response?.data?.message || "Lỗi khi tải chi tiết sản phẩm";
    console.error("Error fetching product:", err);
    ElMessage.error(error.value);
  }
};

const fetchDanhMuc = async () => {
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
  } catch (err) {
    error.value = err.response?.data?.message || "Lỗi khi tải danh mục";
    console.error("Error fetching danh muc:", err);
    ElMessage.error(error.value);
  }
};

const capNhatSoLuong = debounce((index) => {
  if (
    index === null ||
    index === undefined ||
    !sanPhamModel.sanPhamChiTiets[index]
  ) {
    toast.error("Vui lòng chọn một biến thể trước khi nhập IMEI");
    return;
  }

  const imeisInput = sanPhamModel.sanPhamChiTiets[index].imeisInput || "";

  // Reset flag khi người dùng nhập tay (không phải từ Excel)
  if (!sanPhamModel.sanPhamChiTiets[index].hasExcelData) {
    sanPhamModel.sanPhamChiTiets[index].isFileUploaded = false;
  }

  const imeis = imeisInput
    .split(",")
    .map((i) => i.trim())
    .filter((i) => i);

  // Loại bỏ trùng lặp trong cùng danh sách
  const uniqueImeis = [...new Set(imeis)];
  const duplicateInSameVariant = imeis.filter(
    (im, idx, arr) => arr.indexOf(im) !== idx
  );

  // Kiểm tra định dạng IMEI
  const validImeis = uniqueImeis.filter(validateIMEI);
  const invalidImeis = uniqueImeis.filter((i) => !validateIMEI(i));

  // Kiểm tra trùng lặp với các biến thể khác
  const allOtherImeis = sanPhamModel.sanPhamChiTiets.flatMap((chiTiet, i) =>
    i !== index && chiTiet.imeisInput
      ? chiTiet.imeisInput
        .split(",")
        .map((im) => im.trim())
        .filter((im) => im)
      : []
  );
  const duplicateImeis = validImeis.filter((im) => allOtherImeis.includes(im));

  // ✅ CẬP NHẬT: Cập nhật số lượng chính xác
  const finalValidCount = validImeis.filter(
    (im) => !duplicateImeis.includes(im)
  ).length;
  sanPhamModel.sanPhamChiTiets[index].soLuong = finalValidCount;
  sanPhamModel.sanPhamChiTiets[index].invalidImeis = invalidImeis;
  sanPhamModel.sanPhamChiTiets[index].duplicateImeis = [
    ...new Set([...duplicateInSameVariant, ...duplicateImeis]),
  ];

  // Hiển thị thông báo lỗi nếu có
  if (duplicateInSameVariant.length > 0) {
    toast.error(
      `Biến thể ${index + 1}: Có ${duplicateInSameVariant.length
      } IMEI trùng lặp trong cùng biến thể`
    );
  }
  if (duplicateImeis.length > 0) {
    toast.error(
      `Biến thể ${index + 1}: Có ${duplicateImeis.length
      } IMEI trùng lặp với các biến thể khác`
    );
  }
  // if (invalidImeis.length > 0) {
  //   ElMessage.error(
  //     `Biến thể ${index + 1}: Có ${invalidImeis.length} IMEI không hợp lệ`
  //   );
  // }

  console.log(`📊 Cập nhật số lượng biến thể ${index + 1}:`, {
    totalInput: imeis.length,
    uniqueImeis: uniqueImeis.length,
    validImeis: validImeis.length,
    finalCount: finalValidCount,
    duplicateInSame: duplicateInSameVariant.length,
    duplicateWithOthers: duplicateImeis.length,
    invalid: invalidImeis.length,
  });
}, 100);

const selectChiTiet = (row, column, event) => {
  const index = sanPhamModel.sanPhamChiTiets.indexOf(row);
  selectedChiTiet.value = index;
  imeiFileList[index] = imeiFileList[index] || [];
  if (!sanPhamModel.sanPhamChiTiets[index].isFileUploaded) {
    imeiFileList[index] = [];
    nextTick(() => {
      const uploadComponent = document.querySelector(
        `.el-upload[ref="upload-${index}"]`
      );
      if (uploadComponent && uploadComponent.__vue__) {
        uploadComponent.__vue__.clearFiles();
      }
    });
  }
};

const getRowClassName = ({ rowIndex }) => {
  console.log('Row index:', rowIndex, 'Selected index:', selectedChiTiet.value);
  return selectedChiTiet.value === rowIndex ? 'current-row' : ''
}

const removeChiTiet = async (index) => {
  try {
    await ElMessageBox.confirm(
      "Bạn có chắc chắn muốn xoá chi tiết sản phẩm này?",
      "Xác nhận",
      {
        confirmButtonText: "Xoá",
        cancelButtonText: "Hủy",
        type: "warning",
      }
    );

    // Nếu nhấn "Xoá"
    sanPhamModel.sanPhamChiTiets.splice(index, 1);
    delete imeiFileList[index];

    if (selectedChiTiet.value === index) {
      selectedChiTiet.value =
        sanPhamModel.sanPhamChiTiets.length > 0 ? 0 : null;
    } else if (selectedChiTiet.value > index) {
      selectedChiTiet.value--;
    }

    toast.success("Xoá chi tiết thành công!");
  } catch {
    // Nếu nhấn "Hủy"
    toast.info("Đã hủy thao tác xoá");
  }
};
const getMauSacLabel = (idMau) =>
  maus.value.find((m) => m.id === idMau)?.tenMau || "Không rõ";
const getRomLabel = (idRom) =>
  roms.value.find((r) => r.id === idRom)?.dungLuong || "Không rõ";
const indexMethod = (index) => index + 1;

const validateForm = () => {
  let hasError = false;
  error.value = "";

  // Kiểm tra thông tin cơ bản
  if (!sanPhamModel.tenSanPham) {
    error.value = "Vui lòng nhập tên sản phẩm";
    hasError = true;
  } else if (!sanPhamModel.thuongHieu) {
    error.value = "Vui lòng nhập thương hiệu";
    hasError = true;
  } else if (!sanPhamModel.idNhaCungCaps) {
    error.value = "Vui lòng chọn nhà cung cấp";
    hasError = true;
  } else if (!sanPhamModel.trangThaiSanPham) {
    error.value = "Vui lòng chọn trạng thái";
    hasError = true;
  } else if (!sanPhamModel.idModelSanPham) {
    error.value = "Vui lòng chọn model sản phẩm";
    hasError = true;
  } else if (!sanPhamModel.sanPhamChiTiets.length) {
    error.value = "Vui lòng thêm ít nhất một biến thể sản phẩm";
    hasError = true;
  }

  // Kiểm tra chi tiết sản phẩm
  const allImeis = new Set();
  sanPhamModel.sanPhamChiTiets.forEach((chiTiet, index) => {
    if (!chiTiet.idMau) {
      error.value = `Biến thể ${index + 1}: Vui lòng chọn màu sắc`;
      hasError = true;
    } else if (!chiTiet.idRom) {
      error.value = `Biến thể ${index + 1}: Vui lòng chọn ROM`;
      hasError = true;
    } else if (!chiTiet.giaBan || chiTiet.giaBan < 1000) {
      error.value = `Biến thể ${index + 1
        }: Giá bán phải lớn hơn hoặc bằng 1000 VND`;
      hasError = true;
    }
    // else if (!chiTiet.soLuong) {
    //   error.value = `Biến thể ${index + 1}: Số lượng phải lớn hơn 0`;
    //   hasError = true;
    // } 

    // else if (chiTiet.hinhAnhs.length === 0) {
    //   error.value = `Biến thể ${index + 1}: Phải có ít nhất 1 hình ảnh`;
    //   hasError = true;
    // }

    const imeis = chiTiet.imeisInput
      .split(",")
      .map((i) => i.trim())
      .filter((i) => i);
    if (imeis.length === 0) {
      // error.value = `Biến thể ${index + 1}: Phải có ít nhất 1 IMEI`;
      // hasError = true;
    } else {
      // Kiểm tra IMEI không hợp lệ
      const invalidImeis = imeis.filter((i) => !validateIMEI(i));
      if (invalidImeis.length > 0) {
        error.value = `Biến thể ${index + 1}: Có ${invalidImeis.length
          } IMEI không hợp lệ: ${invalidImeis.slice(0, 3).join(", ")}${invalidImeis.length > 3 ? "..." : ""
          }`;
        hasError = true;
      }

      // Kiểm tra trùng lặp trong cùng biến thể
      const duplicateInSameVariant = imeis.filter(
        (im, idx, arr) => arr.indexOf(im) !== idx
      );
      if (duplicateInSameVariant.length > 0) {
        error.value = `Biến thể ${index + 1}: Có ${duplicateInSameVariant.length
          } IMEI trùng lặp trong cùng biến thể: ${duplicateInSameVariant
            .slice(0, 3)
            .join(", ")}${duplicateInSameVariant.length > 3 ? "..." : ""}`;
        hasError = true;
      }

      // Kiểm tra trùng lặp với các biến thể khác
      const duplicates = imeis.filter((im) => allImeis.has(im));
      if (duplicates.length > 0) {
        error.value = `Biến thể ${index + 1}: Có ${duplicates.length
          } IMEI trùng lặp với các biến thể khác: ${duplicates
            .slice(0, 3)
            .join(", ")}${duplicates.length > 3 ? "..." : ""}`;
        hasError = true;
      } else {
        imeis.forEach((im) => allImeis.add(im));
      }
    }
  });

  return !hasError;
};

const submitForm = async () => {
  let loadingInstance = null;
  loading.submit = true;
  try {
    console.log("🟡 Bắt đầu submit form...");

    if (!id.value) {
      console.error("❌ ID sản phẩm không hợp lệ");
      throw new Error("ID sản phẩm không hợp lệ");
    }
    console.log("🧾 ID sản phẩm:", id.value);

    const formValid = await sanPhamForm.value.validate();
    console.log("✅ Form Element Plus hợp lệ:", formValid);
    if (!formValid) {
      ElMessage.error("Vui lòng kiểm tra lại dữ liệu nhập vào");
      return;
    }

    const logicValid = validateForm();
    console.log("✅ Form logic validate hợp lệ:", logicValid);
    if (!logicValid) {
      ElMessage.error(error.value);
      return;
    }

    const payload = {
      id: id.value,
      maSanPham: sanPhamModel.maSanPham,
      tenSanPham: sanPhamModel.tenSanPham,
      thuongHieu: sanPhamModel.thuongHieu,
      idNhaCungCaps: sanPhamModel.idNhaCungCaps,
      trangThaiSanPham: sanPhamModel.trangThaiSanPham,
      idModelSanPham: sanPhamModel.idModelSanPham,
      sanPhamChiTiets: sanPhamModel.sanPhamChiTiets.map((chiTiet, index) => {
        const imeis = chiTiet.imeisInput
          ? chiTiet.imeisInput
            .split(",")
            .map((i) => i.trim())
            .filter((i) => i)
          : [];
        console.log(`📦 Biến thể ${index + 1}:`, {
          id: chiTiet.id || null,
          idMau: chiTiet.idMau,
          idRom: chiTiet.idRom,
          soLuong: chiTiet.soLuong,
          imeiCount: imeis.length,
          imeis,
        });
        return {
          id: chiTiet.id || null,
          maSanPhamChiTiet: chiTiet.maSanPhamChiTiet,
          idMau: chiTiet.idMau,
          idRom: chiTiet.idRom,
          soLuong: chiTiet.soLuong,
          giaBan: chiTiet.giaBan,
          imeis: imeis.map((i) => ({ soImei: i, trangThaiImei: "AVAILABLE" })),
          hinhAnhs: chiTiet.hinhAnhs.map((h) => ({
            url: h.url,
            imagePublicId: h.imagePublicId,
          })),
        };
      }),
    };

    console.log("📤 Payload gửi lên:", JSON.stringify(payload, null, 2));

    await putDataSanPham(id.value, payload);

    console.log("✅ Cập nhật thành công");
    ElMessage.success("Cập nhật sản phẩm thành công!");
    router.push("/admin/products");
  } catch (err) {
    const errorMessage =
      err.response?.data?.message || err.message || "Lỗi khi cập nhật sản phẩm";
    error.value = errorMessage;
    console.error("❌ Submit error:", {
      error: errorMessage,
      response: err.response?.data,
      stack: err.stack,
    });
    if (
      err.response?.status === 400 &&
      errorMessage.includes("IMEI trùng lặp")
    ) {
      ElMessage.error("Không thể cập nhật: Có IMEI trùng lặp trong dữ liệu.");
    } else {
      ElMessage.error(errorMessage);
    }
  } finally {
    loading.submit = false;
    if (loadingInstance) {
      loadingInstance.close();
    }
    console.log("🔚 Kết thúc submit");
  }
};

onMounted(() => {
  const routeId = route.params.id;
  // console.log('Route params id:', routeId);
  if (!routeId || isNaN(parseInt(routeId))) {
    ElMessage.error("ID sản phẩm không hợp lệ");
    router.push("/admin/products");
    return;
  }
  id.value = parseInt(routeId);
  fetchSanPham(id.value);
  fetchDanhMuc();
});
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 32px;
  font-family: "Inter", "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  max-width: 1600px;
  margin: 0 auto;
}

.professional-table .el-table__body tr.current-row {
  background-color: #ebf8ff !important;
  border-left: 4px solid #667eea;
  transform: scale(1.01);
}

.professional-table .el-table__body tr.current-row:hover {
  background-color: #c0ced5 !important;
  transform: scale(1.01);
} 

.form-header {
  text-align: center;
  margin-bottom: 40px;
}

.form-header h2 {
  font-size: 32px;
  font-weight: 700;
  color: #1a202c;
  margin: 0 0 16px 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-divider {
  width: 80px;
  height: 4px;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  margin: 0 auto;
  border-radius: 2px;
}

.form-section {
  background: white;
  border-radius: 16px;
  padding: 32px;
  margin-bottom: 32px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
}

.form-section:hover {
  transform: translateY(-2px);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.12);
}

.section-title {
  font-size: 24px;
  font-weight: 600;
  color: #2d3748;
  margin: 0 0 24px 0;
  padding-bottom: 12px;
  border-bottom: 2px solid #e2e8f0;
  position: relative;
}

.section-title::after {
  content: "";
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 60px;
  height: 2px;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
}

.professional-form .el-form-item {
  margin-bottom: 24px;
}

.professional-form .el-form-item__label {
  font-weight: 600;
  color: #4a5568;
  font-size: 14px;
}

.professional-form .el-input__inner,
.professional-form .el-select .el-input__inner,
.professional-form .el-textarea__inner {
  border-radius: 8px;
  border: 2px solid #e2e8f0;
  transition: all 0.3s ease;
  font-size: 14px;
  padding: 12px 16px;
}

.professional-form .el-input__inner:focus,
.professional-form .el-select .el-input__inner:focus,
.professional-form .el-textarea__inner:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.table-container {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.08);
  border: 1px solid #e2e8f0;
}

.professional-table {
  background: white;
  border-radius: 0;
  border: none;
  font-size: 14px;
}

.professional-table .el-table__header-wrapper {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.professional-table .el-table__header th {
  background: transparent !important;
  color: white !important;
  font-weight: 600;
  font-size: 14px;
  padding: 16px 12px;
  border-bottom: none;
  text-align: center;
}

.professional-table .el-table__header th.is-leaf {
  border-right: 1px solid rgba(255, 255, 255, 0.2);
}

.professional-table .el-table__header th:last-child {
  border-right: none;
}

.professional-table .el-table__body tr {
  transition: all 0.3s ease;
  cursor: pointer;
}

.professional-table .el-table__body tr:hover {
  background-color: #f7fafc !important;
  transform: scale(1.01);
}

.professional-table .el-table__body tr.current-row {
  background-color: #ebf8ff !important;
  border-left: 4px solid #667eea;
}

.professional-table .el-table__body td {
  padding: 16px 12px;
  border-bottom: 1px solid #f1f5f9;
  color: #4a5568;
  font-size: 14px;
  text-align: center;
  vertical-align: middle;
}

.table-cell-content {
  font-weight: 500;
  color: #2d3748;
}

.price-cell {
  font-weight: 600;
  color: #38a169;
  font-size: 15px;
}

.table-images {
  display: flex;
  justify-content: center;
  align-items: center;
}

.image-preview-container {
  position: relative;
  display: inline-block;
}

.table-image {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 8px;
  cursor: pointer;
  border: 2px solid #e2e8f0;
  transition: all 0.3s ease;
}

.table-image:hover {
  border-color: #667eea;
  transform: scale(1.1);
}

.image-count {
  position: absolute;
  top: -8px;
  right: -8px;
  background: #667eea;
  color: white;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 10px;
  font-weight: 600;
}

.no-image {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #a0aec0;
  font-size: 12px;
}

.no-image i {
  font-size: 24px;
  margin-bottom: 4px;
}

.edit-section {
  background: linear-gradient(-espdeg, #f0f9ff 0%, #e0f2fe 100%);
  border: 2px solid #0ea5e9;
}

.edit-title {
  font-size: 20px;
  font-weight: 600;
  color: #0c4a6e;
  margin: 0 0 24px 0;
  display: flex;
  align-items: center;
  gap: 8px;
}

.edit-title i {
  color: #0ea5e9;
}

.image-upload-section {
  width: 100%;
}

/* .current-images {
  Marcell-bottom: 24px;
} */

.images-subtitle {
  font-size: 16px;
  font-weight: 600;
  color: #2d3748;
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.images-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
  margin-bottom: 20px;
}

.image-item {
  position: relative;
  border-radius: 12px;
  overflow: hidden;
  background: white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.image-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.image-wrapper {
  position: relative;
}

.preview-image {
  width: 100%;
  height: 241px;
  object-fit: contain; /* luôn giữ nguyên tỷ lệ và hiển thị đủ ảnh */
  border-radius: 8px;  /* bo góc */
  background: #000;    /* nền để lấp phần thừa */
  cursor: pointer;
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.image-item:hover .image-overlay {
  opacity: 1;
}

.overlay-btn {
  width: 36px;
  height: 36px;
  border-radius: 50%;
}

.view-btn {
  background: #667eea;
  border-color: #667eea;
}

.delete-btn {
  background: #ef4444;
  border-color: #ef4444;
}

.image-info {
  padding: 12px;
  background: #f8fafc;
  border-top: 1px solid #e2e8f0;
}

.image-name {
  display: block;
  font-weight: 500;
  color: #2d3748;
  font-size: 13px;
  margin-bottom: 4px;
}

.image-size {
  font-size: 11px;
  color: #718096;
}

.upload-area {
  margin-top: 20px;
}

.image-uploader {
  width: 100%;
}

.image-uploader .el-upload {
  width: 100%;
}

.image-uploader .el-upload-dragger {
  width: 100%;
  height: 200px;
  border: 2px dashed #d1d5db;
  border-radius: 12px;
  background: #fafbfc;
  transition: all 0.3s ease;
}

.image-uploader .el-upload-dragger:hover {
  border-color: #667eea;
  background: #f0f9ff;
}

.upload-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  padding: 20px;
}

.upload-icon {
  font-size: 48px;
  color: #9ca3af;
  margin-bottom: 16px;
}

.upload-text {
  text-align: center;
}

.upload-title {
  font-size: 16px;
  color: #374151;
  margin: 0 0 8px 0;
}

.upload-title em {
  color: #667eea;
  font-style: normal;
  font-weight: 600;
}

.upload-subtitle {
  font-size: 13px;
  color: #6b7280;
  margin: 0;
}

.upload-progress {
  margin-top: 16px;
  padding: 16px;
  background: #f8fafc;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
}

.progress-text {
  font-size: 14px;
  color: #4a5568;
  font-weight: 500;
}

.image-preview-dialog .el-dialog__body {
  padding: 0;
}

.preview-container {
  display: flex;
  flex-direction: column;
  max-height: 70vh;
}

.preview-main {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #000;
  min-height: 400px;
}

.preview-main-image {
  max-width: 100%;
  max-height: 60vh;
  object-fit: contain;
}

.preview-thumbnails {
  display: flex;
  gap: 8px;
  padding: 16px;
  background: #f8fafc;
  overflow-x: auto;
  border-top: 1px solid #e2e8f0;
}

.thumbnail-item {
  flex-shrink: 0;
  width: 60px;
  height: 60px;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  border: 2px solid transparent;
  transition: all 0.3s ease;
}

.thumbnail-item.active {
  border-color: #667eea;
}

.thumbnail-item:hover {
  border-color: #a5b4fc;
}

.thumbnail-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.preview-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background: #f8fafc;
  border-top: 1px solid #e2e8f0;
}

.image-counter {
  font-size: 14px;
  color: #6b7280;
  font-weight: 500;
}

.preview-actions {
  display: flex;
  gap: 8px;
}

.imei-info {
  margin-top: 12px;
  /* padding: 12px; */
  background: #f8fafc;
  border-radius: 8px;
  border-left: 4px solid #4299e1;
}

.imei-count {
  font-weight: 600;
  color: #2d3748;
}

.imei-error {
  color: #e53e3e;
  font-weight: 500;
  display: block;
  margin-top: 4px;
}

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
  border-radius: 8px;
}

.file-name {
  margin-left: 10px;
  color: #4a5568;
  font-size: 13px;
}

.file-placeholder {
  margin-left: 10px;
  color: #6b7280;
  font-size: 13px;
}

.upload-btn,
.clear-btn {
  margin-right: 12px;
  padding: 8px 16px;
  font-size: 13px;
  height: 36px;
  border-radius: 8px;
}

.upload-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-color: transparent;
  color: white;
}

.upload-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.clear-btn {
  border-color: #e2e8f0;
  color: #e53e3e;
}

.clear-btn:hover {
  background: #fef2f2;
  border-color: #e53e3e;
}

.form-actions {
  text-align: center;
  padding: 32px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.08);
  margin-top: 32px;
}

.action-btn {
  border-radius: 12px;
  font-weight: 600;
  font-size: 16px;
  padding: 14px 32px;
  margin: 0 12px;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.primary-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-color: transparent;
  color: white;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.primary-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.6);
}

.secondary-btn {
  background: white;
  border-color: #e2e8f0;
  color: #4a5568;
}

.secondary-btn:hover {
  background: #f7fafc;
  border-color: #cbd5e0;
  transform: translateY(-1px);
}

.selection-alert {
  border-radius: 12px;
  border: none;
  background: linear-gradient(135deg, #fef5e7 0%, #fed7aa 100%);
  color: #9a3412;
}

.error-alert {
  border-radius: 12px;
  margin-top: 32px;
  border: none;
  background: linear-gradient(135deg, #fef2f2 0%, #fecaca 100%);
}

.el-tag {
  border-radius: 20px;
  font-weight: 500;
  padding: 4px 12px;
}

@media (max-width: 1200px) {
  .container {
    padding: 24px;
  }

  .form-section {
    padding: 24px;
  }

  .images-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  }
}

@media (max-width: 768px) {
  .container {
    padding: 16px;
  }

  .form-header h2 {
    font-size: 24px;
  }

  .section-title {
    font-size: 20px;
  }

  .form-section {
    padding: 20px;
    margin-bottom: 20px;
  }

  .professional-table .el-table__body td,
  .professional-table .el-table__header th {
    padding: 12px 8px;
    font-size: 13px;
  }

  .action-btn {
    font-size: 14px;
    padding: 12px 24px;
    margin: 8px;
  }

  .images-grid {
    grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
    gap: 12px;
  }

  .preview-image {
    height: 100px;
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
  .form-header h2 {
    font-size: 20px;
  }

  .section-title {
    font-size: 18px;
  }

  .professional-table .el-table__body td,
  .professional-table .el-table__header th {
    padding: 10px 6px;
    font-size: 12px;
  }

  .action-btn {
    display: block;
    width: 100%;
    margin: 8px 0;
  }

  .images-grid {
    grid-template-columns: 1fr 1fr;
  }

  .table-image {
    width: 40px;
    height: 40px;
  }

  .upload-btn,
  .clear-btn {
    max-width: 240px;
    height: 36px;
    line-height: 36px;
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.form-section {
  animation: fadeInUp 0.6s ease-out;
}

.form-section:nth-child(2) {
  animation-delay: 0.1s;
}

.form-section:nth-child(3) {
  animation-delay: 0.2s;
}

.form-section:nth-child(4) {
  animation-delay: 0.3s;
}

::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f5f9;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: linear-gradient(135deg, #5a67d8 0%, #6b46c1 100%);
}
</style>
