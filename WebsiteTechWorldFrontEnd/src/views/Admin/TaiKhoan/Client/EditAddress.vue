<script setup>
import { ref, onMounted, reactive } from "vue";
import { useRoute, useRouter } from "vue-router";
import { getAdress, updateAddress } from "@/Service/Adminservice/TaiKhoan/DiaChiServices";
import { ElMessage, ElNotification } from "element-plus";

// Dữ liệu địa chỉ mẫu
const address = ref({
  id: "",
  tenNguoiNhan: "",
  sdtNguoiNhan: "",
  soNha: "",
  tenDuong: "",
  xaPhuong: "",
  quanHuyen: "",
  tinhThanhPho: "",
  diaChiChinh: false,
});

const route = useRoute();
const router = useRouter();
const errors = reactive({});

const originalPrimaryStatus = ref(false);

const getAdressesClient = async () => {
  Object.keys(errors).forEach((key) => delete errors[key]);

  try {
    const addressId = route.params.idAddress;
    const response = await getAdress(addressId);
    address.value = response;
    originalPrimaryStatus.value = response.diaChiChinh;
  } catch (err) {
    errors.general = err.message || "fail to get address";
  }
};

function handleTogglePrimary(event) {
  if (originalPrimaryStatus.value === true && address.value.diaChiChinh === false) {
    address.value.diaChiChinh = true;
    ElMessage.error("Không thể bỏ trạng thái địa chỉ chính. Mỗi khách hàng phải có ít nhất một địa chỉ chính.");
  }
}

const updateHandle = async () => {
  try {
    const idUpdate = route.params.idAddress;
    const response = await updateAddress(idUpdate, address.value);
    console.log(response);

    ElMessage.success("Cập nhật địa chỉ thành công");
    setTimeout(() => {
      router.push(`/admin/client/addresses/${route.params.idClient}`);
    }, 1000);
  } catch (err) {
    if (Array.isArray(err)) {
      err.forEach(({ field, message }) => {
        errors[field] = message;
      });
    } else {
      ElMessage.error("Cập nhật địa chỉ thất bại");
    }
  }
};

const cancelEdit = () => {
  router.push(`/admin/client/addresses/${route.params.idClient}`);
};

onMounted(() => {
  getAdressesClient();
});
</script>

<template>
  <div class="edit-address-view">
    <el-card shadow="always">
      <template #header>
        <div class="card-header">
          <h2>Chỉnh sửa địa chỉ</h2>
        </div>
      </template>

      <el-form
        :model="address"
        label-position="top"
        @submit.prevent="updateHandle"
        class="address-form"
      >
        <el-form-item
          label="Tên người nhận"
          :error="errors.tenNguoiNhan"
          :show-message="!!errors.tenNguoiNhan"
        >
          <el-input
            v-model="address.tenNguoiNhan"
            placeholder="Nhập tên người nhận"
            clearable
          />
        </el-form-item>

        <el-form-item
          label="Số điện thoại người nhận"
          :error="errors.sdtNguoiNhan"
          :show-message="!!errors.sdtNguoiNhan"
        >
          <el-input
            v-model="address.sdtNguoiNhan"
            placeholder="Nhập số điện thoại người nhận"
            clearable
          />
        </el-form-item>

        <el-form-item
          label="Số nhà"
          :error="errors.soNha"
          :show-message="!!errors.soNha"
        >
          <el-input
            v-model="address.soNha"
            placeholder="Nhập số nhà"
            clearable
          />
        </el-form-item>

        <el-form-item
          label="Tên đường"
          :error="errors.tenDuong"
          :show-message="!!errors.tenDuong"
        >
          <el-input
            v-model="address.tenDuong"
            placeholder="Nhập tên đường"
            clearable
          />
        </el-form-item>

        <el-form-item
          label="Xã/Phường"
          :error="errors.xaPhuong"
          :show-message="!!errors.xaPhuong"
        >
          <el-input
            v-model="address.xaPhuong"
            placeholder="Nhập xã/phường"
            clearable
          />
        </el-form-item>

        <el-form-item
          label="Quận/Huyện"
          :error="errors.quanHuyen"
          :show-message="!!errors.quanHuyen"
        >
          <el-input
            v-model="address.quanHuyen"
            placeholder="Nhập quận/huyện"
            clearable
          />
        </el-form-item>

        <el-form-item
          label="Tỉnh/Thành phố"
          :error="errors.tinhThanhPho"
          :show-message="!!errors.tinhThanhPho"
        >
          <el-input
            v-model="address.tinhThanhPho"
            placeholder="Nhập tỉnh/thành phố"
            clearable
          />
        </el-form-item>

        <el-form-item label="Địa chỉ chính">
          <div class="switch-container">
            <el-switch
              v-model="address.diaChiChinh"
              @change="handleTogglePrimary"
              active-color="#13ce66"
              inactive-color="#ff4949"
            />
            <span v-if="originalPrimaryStatus" class="note">
              (Địa chỉ chính không thể bỏ chọn)
            </span>
          </div>
        </el-form-item>

        <el-form-item>
          <div class="form-actions">
            <el-button type="primary" @click="updateHandle">
              Lưu
            </el-button>
            <el-button type="danger" @click="cancelEdit">Hủy</el-button>
          </div>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.edit-address-view {
  padding: 20px;
  max-width: 600px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

h2 {
  margin: 0;
  color: #2c3e50;
}

.address-form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.switch-container {
  display: flex;
  align-items: center;
  gap: 10px;
}

.note {
  font-size: 0.85em;
  color: #909399;
}

.form-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

/* Responsive */
@media screen and (max-width: 480px) {
  .edit-address-view {
    padding: 10px;
  }
}
</style>