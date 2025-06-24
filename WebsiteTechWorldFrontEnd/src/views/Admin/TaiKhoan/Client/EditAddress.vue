<script setup>
import { ref, onMounted, reactive } from "vue";
import { useRoute, useRouter } from "vue-router";
import {
  getAdress,
  updateAddress,
} from "@/Service/Adminservice/TaiKhoan/KhachHangServices";
import { ElMessage, ElNotification } from "element-plus";
import { h } from "vue";
import { de } from "element-plus/es/locales.mjs";

// Dữ liệu địa chỉ mẫu (thay bằng API hoặc props thực tế)
const address = ref({
  id: "",
  tenNguoiNhan: "",
  sdtNguoiNhan: "",
  soNha: "",
  tenDuong: "",
  xaPhuong: "",
  quanHuyen: "",
  tinhThanhPho: "",
});

const route = useRoute();
const router = useRouter();
const errors = reactive({});

// Lưu trạng thái ban đầu của địa chỉ chính
const originalPrimaryStatus = ref(false);

const getAdressesClient = async () => {
  Object.keys(errors).forEach((key) => delete errors[key]);

  try {
    const addressId = route.params.idAddress;
    const response = await getAdress(addressId);
    address.value = response;
    
    // Lưu trạng thái ban đầu
    originalPrimaryStatus.value = response.diaChiChinh;

  } catch (err) {
    errors.general = err.message || "fail to get address";
  }
};

function handleTogglePrimary(event) {
  // Nếu địa chỉ hiện tại đang là địa chỉ chính (true) thì không cho chuyển về false
  if (originalPrimaryStatus.value === true && address.value.diaChiChinh === false) {
    // Khôi phục lại trạng thái true
    address.value.diaChiChinh = true;
    ElMessage.error("Không thể bỏ trạng thái địa chỉ chính. Mỗi khách hàng phải có ít nhất một địa chỉ chính.")
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
      ElMessage.error("Cập nhật địa chỉ thất bại")
    }
  }
};

const cancelEdit = () => {
  // Quay lại trang danh sách địa chỉ
  router.push(`/admin/client/addresses/${route.params.idClient}`);
};

onMounted(() => {
  getAdressesClient();
});
</script>

<template>
  <div class="edit-address-view">
    <h2>Chỉnh sửa địa chỉ</h2>
    <form class="address-form" @submit.prevent="updateHandle">
      <div class="form-group">
        <label for="tenNguoiNhan">Tên người nhận</label>
        <span v-if="errors.tenNguoiNhan" class="text-danger mb-1">{{
          errors.tenNguoiNhan
        }}</span>
        <input
          id="tenNguoiNhan"
          v-model="address.tenNguoiNhan"
          type="text"
          placeholder="Nhập tên người nhận"
        />
      </div>
      <div class="form-group">
        <label for="sdtNguoiNhan">Số điện thoại người nhận</label>
        <span v-if="errors.sdtNguoiNhan" class="text-danger mb-1">{{
          errors.sdtNguoiNhan
        }}</span>
        <input
          id="sdtNguoiNhan"
          v-model="address.sdtNguoiNhan"
          type="text"
          placeholder="Nhập số điện thoại người nhận"
        />
      </div>
      <div class="form-group">
        <label for="soNha">Số nhà</label>
        <span v-if="errors.soNha" class="text-danger mb-1">{{
          errors.soNha
        }}</span>
        <input
          id="soNha"
          v-model="address.soNha"
          type="text"
          placeholder="Nhập số nhà"
        />
      </div>
      <div class="form-group">
        <label for="tenDuong">Tên đường</label>
        <span v-if="errors.tenDuong" class="text-danger mb-1">{{
          errors.tenDuong
        }}</span>
        <input
          id="tenDuong"
          v-model="address.tenDuong"
          type="text"
          placeholder="Nhập tên đường"
        />
      </div>
      <div class="form-group">
        <label for="xaPhuong">Xã/Phường</label>
        <span v-if="errors.xaPhuong" class="text-danger mb-1">{{
          errors.xaPhuong
        }}</span>
        <input
          id="xaPhuong"
          v-model="address.xaPhuong"
          type="text"
          placeholder="Nhập xã/phường"
        />
      </div>
      <div class="form-group">
        <label for="quanHuyen">Quận/Huyện</label>
        <span v-if="errors.quanHuyen" class="text-danger mb-1">{{
          errors.quanHuyen
        }}</span>
        <input
          id="quanHuyen"
          v-model="address.quanHuyen"
          type="text"
          placeholder="Nhập quận/huyện"
        />
      </div>
      <div class="form-group">
        <label for="tinhThanhPho">Tỉnh/Thành phố</label>
        <span v-if="errors.tinhThanhPho" class="text-danger mb-1">{{
          errors.tinhThanhPho
        }}</span>
        <input
          id="tinhThanhPho"
          v-model="address.tinhThanhPho"
          type="text"
          placeholder="Nhập tỉnh/thành phố"
        />
      </div>
      <div class="form-group">
        <label for="diaChiChinh">Địa chỉ chính</label>
        <label class="switch">
          <input
            type="checkbox"
            id="diaChiChinh"
            v-model="address.diaChiChinh"
            @change="handleTogglePrimary"
          />
          <span class="slider"></span>
        </label>
        <span v-if="originalPrimaryStatus" class="note">
          (Địa chỉ chính không thể bỏ chọn)
        </span>
      </div>

      <div class="form-actions">
        <button type="submit" class="btn-save">Lưu</button>
        <button type="button" class="btn-cancel" @click="cancelEdit">
          Hủy
        </button>
      </div>
    </form>
  </div>
</template>

<style scoped>
.edit-address-view {
  padding: 20px;
  max-width: 600px;
  margin: 0 auto;
}

h2 {
  margin-bottom: 20px;
  color: #2c3e50;
}

.address-form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  font-weight: bold;
  margin-bottom: 5px;
  color: #34495e;
}

.form-group input {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  outline: none;
  transition: border-color 0.2s ease;
}

.form-group input:focus {
  border-color: #007bff;
}

.form-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.btn-save,
.btn-cancel {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.btn-save {
  background-color: #28a745;
  color: white;
}

.btn-save:hover {
  background-color: #218838;
}

.btn-cancel {
  background-color: #dc3545;
  color: white;
}

.btn-cancel:hover {
  background-color: #c82333;
}

/* Responsive */
@media screen and (max-width: 480px) {
  .edit-address-view {
    padding: 10px;
  }

  .form-group input {
    font-size: 14px;
  }

  .btn-save,
  .btn-cancel {
    padding: 6px 12px;
    font-size: 14px;
  }
}
.switch {
  position: relative;
  display: inline-block;
  width: 50px;
  height: 28px;
  margin-left: 10px;
}

.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  transition: 0.4s;
  border-radius: 28px;
}

.slider:before {
  position: absolute;
  content: "";
  height: 22px;
  width: 22px;
  left: 3px;
  bottom: 3px;
  background-color: white;
  transition: 0.4s;
  border-radius: 50%;
}

input:checked + .slider {
  background-color: #4caf50;
}

input:checked + .slider:before {
  transform: translateX(22px);
}

.note {
  font-size: 0.85em;
  color: gray;
  margin-left: 10px;
}
</style>