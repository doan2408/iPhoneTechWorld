<script setup>
import { ref, onMounted, reactive, computed } from "vue";
import { getAdressesClient } from "@/Service/Adminservice/TaiKhoan/KhachHangServices";
import {
  addAddress,
  getAdress,
  updateAddress,
} from "@/Service/Adminservice/TaiKhoan/DiaChiServices";
import { useRoute, useRouter } from "vue-router";
import { add } from "@/Service/Adminservice/PhieuGiamGia/PhieuGiamGiaAdminService";
import { ElMessage } from "element-plus";
import { ArrowLeft, Plus, Edit, Delete } from "@element-plus/icons-vue";

const addresses = ref([]);
const error = ref(null);
const selectedAddressId = ref(null); // Theo dõi địa chỉ được click để hiện nút
const route = useRoute();
const router = useRouter();

// Modal states
const isModalVisible = ref(false);
const isEditMode = ref(false);
const loading = ref(false);

//biến lưu dữ liệu ban đầu
const initialFormData = ref({});

// Form data
const formData = reactive({
  id: null,
  tenNguoiNhan: "",
  sdtNguoiNhan: "",
  soNha: "",
  tenDuong: "",
  xaPhuong: "",
  quanHuyen: "",
  tinhThanhPho: "",
  diaChiChinh: false,
});

// Form validation rules
const rules = {
  tenNguoiNhan: [
    {
      required: true,
      message: "Vui lòng nhập tên người nhận",
      trigger: "blur",
    },
  ],
  sdtNguoiNhan: [
    { required: true, message: "Vui lòng nhập số điện thoại", trigger: "blur" },
    {
      pattern: /^[0-9]{10,11}$/,
      message: "Số điện thoại không hợp lệ",
      trigger: "blur",
    },
  ],
  soNha: [{ required: true, message: "Vui lòng nhập số nhà", trigger: "blur" }],
  tenDuong: [
    { required: true, message: "Vui lòng nhập tên đường", trigger: "blur" },
  ],
  xaPhuong: [
    { required: true, message: "Vui lòng nhập xã/phường", trigger: "blur" },
  ],
  quanHuyen: [
    { required: true, message: "Vui lòng nhập quận/huyện", trigger: "blur" },
  ],
  tinhThanhPho: [
    {
      required: true,
      message: "Vui lòng nhập tỉnh/thành phố",
      trigger: "blur",
    },
  ],
};

const formRef = ref();

const loadAddresses = async () => {
  try {
    const idKhachHang = route.params.idClient;
    console.log(idKhachHang);
    const data = await getAdressesClient(idKhachHang);
    addresses.value = data;
  } catch (e) {
    error.value = e;
  }
};

// Reset form data
const resetForm = () => {
  Object.assign(formData, {
    id: null,
    tenNguoiNhan: "",
    sdtNguoiNhan: "",
    soNha: "",
    tenDuong: "",
    xaPhuong: "",
    quanHuyen: "",
    tinhThanhPho: "",
    diaChiChinh: false,
  });
  formRef.value?.clearValidate();
};

// Open modal for adding new address
const openAddModal = () => {
  if (addresses.value.length >= 5) {
    ElMessage.warning("Mỗi khách hàng chỉ được phép có tối đa 5 địa chỉ");
    return;
  }
  isEditMode.value = false;
  resetForm();
  isModalVisible.value = true;
};

const originalPrimaryStatus = ref(false);

// Open modal for editing address
const openEditModal = (address) => {
  isEditMode.value = true;
  Object.assign(formData, { ...address });
  originalPrimaryStatus.value = address.diaChiChinh;
  initialFormData.value = JSON.parse(JSON.stringify(formData)); // Deep copy
  isModalVisible.value = true;
};

const isFormChanged = computed(() => {
  return JSON.stringify(formData) !== JSON.stringify(initialFormData.value);
});

// Close modal
const closeModal = () => {
  isModalVisible.value = false;
  resetForm();
};

// Handle form submission
const handleSubmit = async () => {
  if (!formRef.value) return;

  try {
    await formRef.value.validate();
    loading.value = true;

    // Prepare data for API
    // Update existing address - sử dụng ID của địa chỉ
    const addressData = {
      id: formData.id, // ID của địa chỉ để update
      tenNguoiNhan: formData.tenNguoiNhan,
      sdtNguoiNhan: formData.sdtNguoiNhan,
      soNha: formData.soNha,
      tenDuong: formData.tenDuong,
      xaPhuong: formData.xaPhuong,
      quanHuyen: formData.quanHuyen,
      tinhThanhPho: formData.tinhThanhPho,
      diaChiChinh: formData.diaChiChinh,
      idKhachHang: route.params.idClient // ID khách hàng từ params
    };

    if (isEditMode.value) {
      const response = await updateAddress(addressData.id, addressData);
      console.log(response);
      console.log("Updating address:", addressData);
      ElMessage.success("Cập nhật địa chỉ thành công!");
    } else {
      const response = await addAddress(addressData);
      console.log(response);
      console.log("Adding new address:", addressData);
      ElMessage.success("Thêm địa chỉ thành công!");
    }

    // Reload addresses and close modal
    await loadAddresses();
    closeModal();
  } catch (error) {
    console.error("Form validation failed:", error);
    if (Array.isArray(error)) {
      err.forEach(({ field, message }) => {
        errors[field] = message;
      });
    } else {
      ElMessage.error("Cập nhật địa chỉ thất bại");
    }
  } finally {
    loading.value = false;
  }
};

// const selectAddress = (id) => {
//   // Chỉ cho phép chọn địa chỉ phụ (không phải mặc định)
//   const address = addresses.value.find((addr) => addr.id === id);
//   if (!address.diaChiChinh) {
//     selectedAddressId.value = selectedAddressId.value === id ? null : id;
//   }
// };

// const setPrimary = async (address) => {
//   // Đặt địa chỉ này làm mặc định
//   addresses.value = addresses.value.map((addr) => ({
//     ...addr,
//     diaChiChinh: addr.id === address.id,
//   }));
//   // Ẩn nút sau khi đặt mặc định
//   selectedAddressId.value = null;
//   // TODO: Gọi API để cập nhật địa chỉ mặc định nếu có, ví dụ:
//   // await updatePrimaryAddressClient(address.id, true)
// };

const goToEdit = (addressId) => {
  const address = addresses.value.find((addr) => addr.id === addressId);
  if (address) {
    openEditModal(address);
  }
};

const goToAdd = () => {
  openAddModal();
};

const back = () => {
  router.push("/admin/client");
};

function handleTogglePrimary(value) {
  if (originalPrimaryStatus.value === true && value === false) {
    formData.diaChiChinh = true;
    ElMessage.error(
      "Không thể bỏ trạng thái địa chỉ chính. Mỗi khách hàng phải có ít nhất một địa chỉ chính."
    );
  }
}

onMounted(() => {
  loadAddresses();
});
</script>

<template>
  <div class="address-view">
    <!-- Header với nút thêm địa chỉ -->
    <div class="header-section">
      <h2 v-if="addresses.length == 0">Khách hàng chưa cập nhật địa chỉ</h2>
      <h2 v-else>Địa chỉ giao hàng</h2>

      <el-button
        type="primary"
        @click="goToAdd"
        :disabled="addresses.length >= 5"
        class="add-address-btn"
      >
        <el-icon><Plus /></el-icon>
        Thêm địa chỉ ({{ addresses.length }}/5)
      </el-button>
    </div>

    <!-- Danh sách địa chỉ -->
    <div class="address-list">
      <el-card
        v-for="address in addresses"
        :key="address.id"
        class="address-card"
        :class="{ 'is-default': address.diaChiChinh }"
        shadow="hover"
        @click="!address.diaChiChinh && selectAddress(address.id)"
      >
        <div class="address-content">
          <div class="address-details">
            <el-descriptions :column="1" border>
              <!-- <el-descriptions-item label="ID:">
                <span>{{
                  address.tenNguoiNhan != null
                    ? address.id
                    : "Không có thông tin"
                }}</span>
              </el-descriptions-item> -->

              <el-descriptions-item label="Người nhận:">
                <span>{{
                  address.tenNguoiNhan != null
                    ? address.tenNguoiNhan
                    : "Không có thông tin"
                }}</span>
              </el-descriptions-item>
              <el-descriptions-item label="Số điện thoại:">
                <span>{{
                  address.sdtNguoiNhan != null
                    ? address.sdtNguoiNhan
                    : "Không có thông tin"
                }}</span>
              </el-descriptions-item>
              <el-descriptions-item label="Địa chỉ:">
                <span>
                  {{ address.soNha != null ? address.soNha : ""
                  }}{{ address.soNha ? ", " : ""
                  }}{{ address.tenDuong != null ? address.tenDuong : ""
                  }}{{ address.tenDuong ? ", " : "" }}
                  {{ address.xaPhuong != null ? address.xaPhuong : ""
                  }}{{ address.xaPhuong ? ", " : ""
                  }}{{ address.quanHuyen != null ? address.quanHuyen : ""
                  }}{{ address.quanHuyen ? ", " : "" }}
                  {{ address.tinhThanhPho != null ? address.tinhThanhPho : "" }}
                </span>
              </el-descriptions-item>
            </el-descriptions>
          </div>

          <div class="address-actions">
            <el-tag
              v-if="address.diaChiChinh"
              type="danger"
              effect="plain"
              class="default-tag"
            >
              Mặc định
            </el-tag>

            <div class="action-buttons">
              <el-button
                type="primary"
                size="small"
                circle
                @click.stop="goToEdit(address.id)"
                title="Chỉnh sửa"
              >
                <el-icon><Edit /></el-icon>
              </el-button>

              <el-button
                type="danger"
                size="small"
                circle
                @click.stop
                title="Xóa"
              >
                <el-icon><Delete /></el-icon>
              </el-button>

              <el-button
                v-if="selectedAddressId === address.id && !address.diaChiChinh"
                type="success"
                size="small"
                @click.stop="setPrimary(address)"
                class="set-primary-btn"
              >
                Đặt làm mặc địnhhh
              </el-button>
            </div>
          </div>
        </div>
      </el-card>

      <div class="footer-actions">
        <el-button @click="back" type="info">
          <el-icon><ArrowLeft /></el-icon>
          Quay lại
        </el-button>
      </div>
    </div>

    <!-- Modal Form -->
    <el-dialog
      v-model="isModalVisible"
      :title="isEditMode ? 'Chỉnh sửa địa chỉ' : 'Thêm địa chỉ mới'"
      width="600px"
      :before-close="closeModal"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="140px"
        label-position="left"
      >
        <el-form-item label="Tên người nhận" prop="tenNguoiNhan">
          <el-input
            v-model="formData.tenNguoiNhan"
            placeholder="Nhập tên người nhận"
            clearable
          />
        </el-form-item>

        <el-form-item label="Số điện thoại" prop="sdtNguoiNhan">
          <el-input
            v-model="formData.sdtNguoiNhan"
            placeholder="Nhập số điện thoại"
            clearable
            maxlength="11"
          />
        </el-form-item>

        <el-form-item label="Số nhà" prop="soNha">
          <el-input
            v-model="formData.soNha"
            placeholder="Nhập số nhà"
            clearable
          />
        </el-form-item>

        <el-form-item label="Tên đường" prop="tenDuong">
          <el-input
            v-model="formData.tenDuong"
            placeholder="Nhập tên đường"
            clearable
          />
        </el-form-item>

        <el-form-item label="Xã/Phường" prop="xaPhuong">
          <el-input
            v-model="formData.xaPhuong"
            placeholder="Nhập xã/phường"
            clearable
          />
        </el-form-item>

        <el-form-item label="Quận/Huyện" prop="quanHuyen">
          <el-input
            v-model="formData.quanHuyen"
            placeholder="Nhập quận/huyện"
            clearable
          />
        </el-form-item>

        <el-form-item label="Tỉnh/Thành phố" prop="tinhThanhPho">
          <el-input
            v-model="formData.tinhThanhPho"
            placeholder="Nhập tỉnh/thành phố"
            clearable
          />
        </el-form-item>

        <el-form-item label="Địa chỉ chính">
          <div class="switch-container">
            <el-switch
              v-model="formData.diaChiChinh"
              @change="handleTogglePrimary"
              active-color="#13ce66"
              inactive-color="#ff4949"
            />
            <span v-if="originalPrimaryStatus" class="note">
              (Địa chỉ chính không thể bỏ chọn)
            </span>
          </div>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="closeModal">Hủy</el-button>
          <el-button type="primary" @click="handleSubmit"
           :loading="loading"
           :disabled="isEditMode && !isFormChanged"
           >
            {{ isEditMode ? "Cập nhật" : "Thêm mới" }}
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.address-view {
  padding: 20px;
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-section h2 {
  margin: 0;
  color: #303133;
}

.add-address-btn {
  display: flex;
  align-items: center;
  gap: 8px;
}

.address-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.address-card {
  transition: all 0.3s ease;
}

.address-card:not(.is-default) {
  cursor: pointer;
}

.address-card:not(.is-default):hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.is-default {
  cursor: default;
  border: 2px solid #f56c6c;
}

.address-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 20px;
}

.address-details {
  flex: 1;
}

.address-actions {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 12px;
}

.default-tag {
  align-self: flex-end;
}

.action-buttons {
  display: flex;
  gap: 8px;
  align-items: center;
}

.set-primary-btn {
  animation: slideIn 0.3s ease forwards;
}

.footer-actions {
  display: flex;
  justify-content: flex-start;
  margin-top: 20px;
}

@keyframes slideIn {
  from {
    transform: translateX(50px);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

/* Responsive design */
@media (max-width: 768px) {
  .header-section {
    flex-direction: column;
    gap: 16px;
    align-items: stretch;
  }

  .address-content {
    flex-direction: column;
    gap: 16px;
  }

  .address-actions {
    align-items: stretch;
  }

  .action-buttons {
    justify-content: center;
  }
}

/* Modal styles */
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

:deep(.el-dialog) {
  margin-top: 5vh;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #606266;
}

:deep(.el-input__wrapper) {
  border-radius: 6px;
}

:deep(.el-switch) {
  --el-switch-on-color: #409eff;
}
</style>
