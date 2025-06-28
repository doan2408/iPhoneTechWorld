<template>
  <el-form :model="sanPham" ref="sanPhamForm" label-width="120px">
    <h2>Thêm sản phẩm</h2>
    <!-- Thông tin sản phẩm chính -->
    <el-form-item label="Tên sản phẩm" :error="errors.tenSanPham">
      <el-input
        v-model="sanPham.tenSanPham"
        @input="errors.tenSanPham = ''"
      ></el-input>
    </el-form-item>

    <el-form-item label="Thương hiệu" :error="errors.thuongHieu">
      <el-input
        v-model="sanPham.thuongHieu"
        @input="errors.thuongHieu = ''"
      ></el-input>
    </el-form-item>

    <el-form-item label="Nhà cung cấp" :error="errors.idNhaCungCap">
      <el-select
        v-model="sanPham.idNhaCungCap"
        placeholder="Chọn nhà cung cấp"
        @change="errors.idNhaCungCap = ''"
      >
        <el-option
          v-for="ncc in nhaCungCaps"
          :key="ncc.id"
          :label="ncc.tenNhaCungCap"
          :value="ncc.id"
        ></el-option>
      </el-select>
    </el-form-item>

    <el-form-item label="Trạng thái" :error="errors.trangThaiSanPham">
      <el-select
        v-model="sanPham.trangThaiSanPham"
        placeholder="Chọn trạng thái"
        @change="errors.trangThaiSanPham = ''"
      >
        <el-option
          v-for="tt in danhSachTrangThaiSanPham"
          :key="tt.value"
          :label="tt.label"
          :value="tt.value"
        ></el-option>
      </el-select>
    </el-form-item>

    <!-- Chọn model_san_pham -->
    <h3>Chọn model sản phẩm</h3>
    <el-form-item label="Model sản phẩm" :error="errors.idModelSanPham">
      <el-select
        v-model="sanPham.idModelSanPham"
        placeholder="Chọn model sản phẩm"
        @change="onModelChange"
      >
        <el-option
          v-for="model in modelSanPhams"
          :key="model.idModelSanPham"
          :label="model.tenModel"
          :value="model.idModelSanPham"
        ></el-option>
      </el-select>
    </el-form-item>

    <!-- Chọn thuộc tính để tạo biến thể -->
    <h3>Tạo biến thể sản phẩm</h3>
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="Màu sắc" :error="errors.selectedMaus">
          <el-select
            v-model="selectedMaus"
            multiple
            placeholder="Chọn màu sắc"
            @change="errors.selectedMaus = ''"
          >
            <el-option
              v-for="mau in maus"
              :key="mau.id"
              :label="mau.tenMau"
              :value="mau.id"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="ROM" :error="errors.selectedRoms">
          <el-select
            v-model="selectedRoms"
            multiple
            placeholder="Chọn ROM"
            @change="errors.selectedRoms = ''"
          >
            <el-option
              v-for="rom in roms"
              :key="rom.id"
              :label="rom.dungLuong"
              :value="rom.id"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>

    <!-- Thêm ảnh theo màu sắc -->
    <h3>Ảnh theo màu sắc</h3>
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
            list-type="picture"
            :limit="5"
            :on-exceed="
              () =>
                ElMessage.warning('Chỉ được tải lên tối đa 5 ảnh cho mỗi màu!')
            "
          >
            <el-button type="primary"
              >Tải lên ảnh cho {{ getMauSacLabels(mau) }}</el-button
            >
          </el-upload>
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
          <el-table
            :data="sanPham.sanPhamChiTiets"
            style="margin-top: 10px"
            @row-click="selectChiTiet"
          >
            <el-table-column
              label="Chi tiết"
              type="index"
              width="60"
            ></el-table-column>
            <el-table-column label="Màu sắc">
              <template #default="{ row }">
                {{ getMauSacLabels(row.idMau) }}
              </template>
            </el-table-column>
            <el-table-column label="ROM">
              <template #default="{ row }">
                {{ getRomLabels(row.idRom) }}
              </template>
            </el-table-column>
            <el-table-column label="Ảnh">
              <template #default="{ row }">
                <el-image
                  v-for="hinh in row.hinhAnhs"
                  :key="hinh.imagePublicId"
                  :src="hinh.url"
                  style="width: 50px; height: 50px; margin-right: 5px"
                  fit="cover"
                />
              </template>
            </el-table-column>
            <el-table-column label="Hành động" width="100">
              <template #default="{ $index }">
                <el-button
                  type="danger"
                  size="small"
                  @click.stop="removeChiTiet($index)"
                  >Xóa</el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card v-if="selectedChiTiet !== null">
          <h4>Thông tin chi tiết {{ selectedChiTiet + 1 }}</h4>
          <el-form-item
            label="Giá bán"
            :error="errorsChiTiet[selectedChiTiet]?.giaBan || ''"
          >
            <el-input-number
              v-model="sanPham.sanPhamChiTiets[selectedChiTiet].giaBan"
              :precision="2"
              :min="1000"
              @change="errorsChiTiet[selectedChiTiet].giaBan = ''"
            ></el-input-number>
          </el-form-item>
          <el-form-item
            label="Số lượng"
            :error="errorsChiTiet[selectedChiTiet]?.soLuong || ''"
          >
            <el-input-number
              v-model="sanPham.sanPhamChiTiets[selectedChiTiet].soLuong"
              :min="0"
              :disabled="true"
            ></el-input-number>
          </el-form-item>
          <el-form-item label="IMEI">
            <el-input
              type="textarea"
              v-model="sanPham.sanPhamChiTiets[selectedChiTiet].imeisInput"
              placeholder="Nhập danh sách IMEI, phân tách bởi dấu phẩy"
              @input="capNhatSoLuong(selectedChiTiet)"
            ></el-input>
            <el-upload
              :auto-upload="false"
              :on-change="(file) => handleImeiFileChange(file, selectedChiTiet)"
              accept=".txt,.csv"
              style="margin-top: 8px"
            >
              <el-button type="primary">Tải lên file IMEI</el-button>
            </el-upload>
            <div
              v-if="selectedChiTiet !== null"
              class="imei-status"
              style="margin-top: 8px; display: flex; align-items: center"
            >
              <span
                >Số lượng IMEI:
                {{ sanPham.sanPhamChiTiets[selectedChiTiet].soLuong }}</span
              >
              <span
                v-if="errorsChiTiet[selectedChiTiet]?.imeisInput"
                :class="{
                  'valid-message':
                    errorsChiTiet[selectedChiTiet].imeisInput === 'Hợp lệ',
                  'error-message':
                    errorsChiTiet[selectedChiTiet].imeisInput !== 'Hợp lệ',
                }"
                style="margin-left: 20px"
              >
                {{ errorsChiTiet[selectedChiTiet].imeisInput }}
              </span>
            </div>
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
import { onMounted, reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  getAllMauSacList,
  getAllModelSanPhamList,
  getAllNhaCungCapList,
  getAllRomList,
  postSanPham,
} from "@/Service/Adminservice/Products/ProductAdminService";
import { debounce } from "chart.js/helpers";
import api from "@/Service/LoginService/axiosInstance";

export default {
  setup() {
    const sanPham = reactive({
      tenSanPham: "",
      thuongHieu: "",
      idNhaCungCap: "",
      trangThaiSanPham: "",
      idModelSanPham: null,
      sanPhamChiTiets: [],
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
    const errors = reactive({
      tenSanPham: "",
      thuongHieu: "",
      idNhaCungCap: "",
      trangThaiSanPham: "",
      idModelSanPham: "",
      selectedMaus: "",
      selectedRoms: "",
    });
    const errorsChiTiet = reactive([]);
    const errorsMauHinhAnh = reactive({});

    const danhSachTrangThaiSanPham = [
      { label: "Đang kinh doanh", value: "ACTIVE" },
      { label: "Ngừng kinh doanh", value: "DISCONTINUED" },
      { label: "Sắp ra mắt", value: "COMING_SOON" },
      { label: "Tạm ngừng bán", value: "TEMPORARILY_UNAVAILABLE" },
      { label: "Hết hàng", value: "OUT_OF_STOCK" },
    ];

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

        const requiredLists = [
          { name: "Nhà cung cấp", list: nhaCungCaps.value },
          { name: "Màu sắc", list: maus.value },
          { name: "ROM", list: roms.value },
          { name: "Model sản phẩm", list: modelSanPhams.value },
        ];
        const emptyLists = requiredLists.filter((item) => !item.list.length);
        if (emptyLists.length) {
          ElMessage.error(
            `Không thể tải danh mục: ${emptyLists
              .map((item) => item.name)
              .join(", ")}`
          );
        }
      } catch (error) {
        ElMessage.error("Lỗi khi lấy danh mục: " + error.message);
      }
    };

    const onModelChange = () => {
      errors.idModelSanPham = "";
      const selectedModel = modelSanPhams.value.find(
        (m) => m.idModelSanPham === sanPham.idModelSanPham
      );
      if (selectedModel) {
        sanPham.tenSanPham = selectedModel.tenModel;
      }
    };

    const generateVariants = () => {
      let hasError = false;

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
        ElMessage.error(
          "Vui lòng chọn đầy đủ model, thuộc tính và ảnh cho từng màu."
        );
        return;
      }

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
              giaBan: existingVariant?.giaBan || 0,
              imeisInput: existingVariant?.imeisInput || "",
              hinhAnhs: hinhAnhTheoMau[mau] || [],
            });
            errorsChiTiet.push({
              giaBan: "",
              soLuong: "Số lượng phải lớn hơn 0",
              imeisInput: "Phải có ít nhất 1 IMEI",
            });
          }
        });
      });

      sanPham.sanPhamChiTiets = newVariants;

      sanPham.sanPhamChiTiets.forEach((_, index) => {
        capNhatSoLuong(index, true);
      });

      selectedChiTiet.value = sanPham.sanPhamChiTiets.length > 0 ? 0 : null;
      ElMessage.success(
        `Đã tạo ${sanPham.sanPhamChiTiets.length} biến thể sản phẩm`
      );

      if (errorsChiTiet.some((err) => Object.values(err).some((e) => e))) {
        const errorIndex = errorsChiTiet.findIndex((err) =>
          Object.values(err).some((e) => e)
        );
        selectedChiTiet.value = errorIndex !== -1 ? errorIndex : 0;
        ElMessage.warning(
          "Một số biến thể thiếu thông tin bắt buộc. Vui lòng kiểm tra!"
        );
      }
    };

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
            const invalidImeis = imeis.filter((i) => !/^\d{15}$/.test(i));
            errorsChiTiet[index].imeisInput =
              invalidImeis.length > 0
                ? `Có ${invalidImeis.length} IMEI không hợp lệ (phải là 15 chữ số)`
                : "";
            errorsChiTiet[index].soLuong = "";
          }
        } else {
          const invalidImeis = imeis.filter((i) => !/^\d{15}$/.test(i));
          errorsChiTiet[index].imeisInput =
            imeis.length === 0
              ? ""
              : invalidImeis.length > 0
              ? `Có ${invalidImeis.length} IMEI không hợp lệ (phải là 15 chữ số)`
              : "Hợp lệ";
          errorsChiTiet[index].soLuong =
            imeis.length > 0 ? "" : "Số lượng phải lớn hơn 0";
        }
      };

      if (validate) {
        update();
      } else {
        debounce(update, 150)();
      }
    };

    const handleImeiFileChange = (file, index) => {
      try {
        if (file.raw.size > 1024 * 1024) {
          throw new Error("File quá lớn, vui lòng chọn file dưới 1MB");
        }
        if (!["text/plain", "text/csv"].includes(file.raw.type)) {
          throw new Error("Vui lòng chọn file .txt hoặc .csv");
        }
        const reader = new FileReader();
        reader.onload = (e) => {
          const content = e.target.result;
          const imeis = content
            .split(/[\n,;\s]+/)
            .map((i) => i.trim())
            .filter((i) => i);
          sanPham.sanPhamChiTiets[index].imeisInput = imeis.join(", ");
          capNhatSoLuong(index);
          ElMessage.success(
            `Đã tải lên ${imeis.length} IMEI từ file ${file.name}`
          );
        };
        reader.onerror = () => {
          throw new Error("Lỗi khi đọc file");
        };
        reader.readAsText(file.raw);
      } catch (error) {
        ElMessage.error("Lỗi khi xử lý file IMEI: " + error.message);
      }
    };

    const handleMauFileChange = async (file, fileList, idMau) => {
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
          {
            headers: { "Content-Type": "multipart/form-data" },
          }
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
        errorsMauHinhAnh[idMau] =
          hinhAnhTheoMau[idMau].length > 0 ? "" : "Phải có ít nhất 1 hình ảnh";
        ElMessage.success(
          `Tải ảnh ${file.name} cho màu ${getMauSacLabels(idMau)} thành công!`
        );
      } catch (error) {
        errorsMauHinhAnh[idMau] =
          "Lỗi khi tải ảnh: " +
          (error.response?.data?.message || error.message);
        ElMessage.error(errorsMauHinhAnh[idMau]);
      }
    };

    const handleMauFileRemove = (file, fileList, idMau) => {
      hinhAnhTheoMau[idMau] = fileList.map((item) => ({
        name: item.name,
        url: item.url,
        imagePublicId: item.imagePublicId,
      }));
      errorsMauHinhAnh[idMau] =
        hinhAnhTheoMau[idMau].length > 0 ? "" : "Phải có ít nhất 1 hình ảnh";
      ElMessage.success(
        `Đã xóa ảnh ${file.name} của màu ${getMauSacLabels(idMau)}`
      );
    };

    const removeChiTiet = (index) => {
      sanPham.sanPhamChiTiets.splice(index, 1);
      errorsChiTiet.splice(index, 1);
      if (selectedChiTiet.value === index) {
        selectedChiTiet.value = sanPham.sanPhamChiTiets.length > 0 ? 0 : null;
      } else if (selectedChiTiet.value > index) {
        selectedChiTiet.value--;
      }
    };

    const selectChiTiet = (row, column, event) => {
      const index = sanPham.sanPhamChiTiets.indexOf(row);
      selectedChiTiet.value = index;
      if (errorsChiTiet[index]) {
        Object.keys(errorsChiTiet[index]).forEach(
          (k) => (errorsChiTiet[index][k] = "")
        );
      }
    };

    const getMauSacLabels = (idMau) => {
      if (!Array.isArray(idMau)) idMau = [idMau];
      return idMau
        .map(
          (id) =>
            maus.value.find((m) => String(m.id) === String(id))?.tenMau || ""
        )
        .join(", ");
    };

    const getRomLabels = (idRom) => {
      if (!Array.isArray(idRom)) idRom = [idRom];
      return idRom
        .map(
          (id) =>
            roms.value.find((r) => String(r.id) === String(id))?.dungLuong || ""
        )
        .join(", ");
    };

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
          errorsChiTiet[index].giaBan = "Giá bán phải lớn hơn 1000";
          hasError = true;
        }
        if (!chiTiet.soLuong) {
          errorsChiTiet[index].soLuong = "Số lượng phải lớn hơn 0";
          hasError = true;
        }
      });

      return !hasError;
    };

    const submitForm = async () => {
      try {
        if (!sanPham.sanPhamChiTiets.length) {
          ElMessage.error("Vui lòng tạo ít nhất một biến thể sản phẩm.");
          return;
        }

        if (!validateForm()) {
          const errorIndex = errorsChiTiet.findIndex((err) =>
            Object.values(err).some((e) => e)
          );
          if (errorIndex !== -1) {
            selectedChiTiet.value = errorIndex;
          }
          ElMessage.error("Vui lòng điền đầy đủ thông tin và sửa các lỗi.");
          return;
        }

        const payload = {
          tenSanPham: sanPham.tenSanPham,
          thuongHieu: sanPham.thuongHieu,
          idNhaCungCap: sanPham.idNhaCungCap,
          trangThaiSanPham: sanPham.trangThaiSanPham,
          idModelSanPham: sanPham.idModelSanPham,
          sanPhamChiTiets: sanPham.sanPhamChiTiets.map((chiTiet) => {
            const imeis = chiTiet.imeisInput
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

        console.log(JSON.stringify(payload, null, 2));
        await postSanPham(payload);
        ElMessageBox.confirm(
          "Sản phẩm đã được lưu thành công. Bạn có muốn làm mới form?",
          "Xác nhận",
          {
            confirmButtonText: "Có",
            cancelButtonText: "Không",
          }
        ).then(() => {
          sanPham.tenSanPham = "";
          sanPham.thuongHieu = "";
          sanPham.idNhaCungCap = "";
          sanPham.trangThaiSanPham = "";
          sanPham.idModelSanPham = null;
          sanPham.sanPhamChiTiets = [];
          selectedMaus.value = [];
          selectedRoms.value = [];
          selectedChiTiet.value = null;
          Object.keys(hinhAnhTheoMau).forEach(
            (key) => delete hinhAnhTheoMau[key]
          );
          Object.keys(errorsMauHinhAnh).forEach(
            (key) => delete errorsMauHinhAnh[key]
          );
          errorsChiTiet.length = 0;
          Object.keys(errors).forEach((k) => (errors[k] = ""));
          ElMessage.success("Form đã được làm mới!");
        });
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
          } else if (typeof errorData === "string") {
            errorMessages.push(errorData);
          } else {
            errorMessages.push("Lỗi xác thực không xác định");
          }

          const errorIndex = errorsChiTiet.findIndex((err) =>
            Object.values(err).some((e) => e)
          );
          if (errorIndex !== -1) {
            selectedChiTiet.value = errorIndex;
          }
          ElMessage.error(`Lỗi xác thực: ${errorMessages.join("; ")}`);
        } else {
          ElMessage.error(
            "Lỗi hệ thống: " + (error.message || "Vui lòng thử lại")
          );
        }
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
      generateVariants,
      removeChiTiet,
      selectChiTiet,
      getMauSacLabels,
      getRomLabels,
      handleMauFileChange,
      handleMauFileRemove,
      submitForm,
      capNhatSoLuong,
      handleImeiFileChange,
      onModelChange,
    };
  },
};
</script>

<style scoped>
.el-form {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
  background: var(--el-bg-color);
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

h2 {
  font-size: 24px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin-bottom: 24px;
  text-align: center;
}

h3 {
  font-size: 18px;
  font-weight: 500;
  color: var(--el-text-color-regular);
  margin: 24px 0 16px;
  border-bottom: 1px solid var(--el-border-color-light);
  padding-bottom: 8px;
}

.el-form-item {
  margin-bottom: 24px;
}

.el-form-item__label {
  font-weight: 500;
  color: var(--el-text-color-primary);
}

.el-input,
.el-select {
  width: 100%;
}

.el-input__inner,
.el-select .el-input__inner {
  border-radius: 6px;
  transition: border-color 0.2s;
}

.el-input__inner:focus,
.el-select .el-input__inner:focus {
  border-color: var(--el-color-primary);
  box-shadow: 0 0 0 1px var(--el-color-primary);
}

.el-button {
  border-radius: 6px;
  padding: 10px 20px;
  font-weight: 500;
  transition: all 0.3s;
}

.el-button--primary {
  background-color: var(--el-color-primary);
  border-color: var(--el-color-primary);
}

.el-button--primary:hover {
  background-color: var(--el-color-primary-light-3);
  border-color: var(--el-color-primary-light-3);
}

.el-button--success {
  background-color: var(--el-color-success);
  border-color: var(--el-color-success);
}

.el-button--success:hover {
  background-color: var(--el-color-success-light-3);
  border-color: var(--el-color-success-light-3);
}

.el-button--danger {
  background-color: var(--el-color-danger);
  border-color: var(--el-color-danger);
}

.el-button--danger:hover {
  background-color: var(--el-color-danger-light-3);
  border-color: var(--el-color-danger-light-3);
}

.el-table {
  cursor: pointer;
  border-radius: 6px;
  overflow: hidden;
}

.el-table th {
  background-color: var(--el-fill-color-light);
  color: var(--el-text-color-primary);
  font-weight: 600;
}

.el-table tr:hover {
  background-color: var(--el-fill-color-blank);
}

.el-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 16px;
}

.el-card h4 {
  font-size: 16px;
  font-weight: 500;
  color: var(--el-text-color-primary);
  margin-bottom: 16px;
}

.el-upload {
  width: 100%;
}

.el-upload .el-button {
  width: 100%;
  text-align: center;
}

.el-upload-list--picture .el-upload-list__item {
  border-radius: 6px;
  transition: all 0.3s;
}

.el-upload-list--picture .el-upload-list__item:hover {
  transform: scale(1.02);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.imei-status {
  font-size: 14px;
  color: var(--el-text-color-regular);
  display: flex;
  align-items: center;
  margin-top: 8px;
}

.valid-message {
  color: var(--el-color-success);
  font-weight: 500;
}

.error-message {
  color: var(--el-color-danger);
  font-weight: 500;
}

.mau-hinh-anh {
  margin-bottom: 16px;
}

.mau-hinh-anh .el-form-item__label {
  font-weight: 500;
  color: var(--el-text-color-primary);
}

@media (max-width: 768px) {
  .el-form {
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
  }
}
</style>
