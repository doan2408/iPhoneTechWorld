<script setup>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import {
  getAdress,
  updateAddress,
} from "@/Service/Adminservice/TaiKhoan/KhachHangServices";
import { ElNotification } from "element-plus";
import { h } from "vue";

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
const error = ref();

//thông báo
function showCustomNotification({
  messageText,
  type = "success",
  duration = 2000,
}) {
  ElNotification({
    title: "",
    message: h("div", [
      h("span", messageText),
      h(
        "div",
        {
          style: `
                    position: relative;
                    height: 4px;
                    background-color: #e0e0e0;
                    margin-top: 8px;
                    border-radius: 2px;
                    overflow: hidden;
                `,
        },
        [
          h("div", {
            style: `
                        position: absolute;
                        top: 0;
                        left: 0;
                        height: 100%;
                        background-color: ${
                          type === "success"
                            ? "#28a745"
                            : type === "error"
                            ? "#dc3545"
                            : "#007bff"
                        };
                        width: 100%;
                        animation: progressBar ${duration}ms linear forwards;
                    `,
          }),
        ]
      ),
    ]),
    duration: duration,
    type: type,
    position: "top-right",
  });
}

const getAdressesClient = async () => {
  try {
    const addressId = route.params.idAddress;
    const response = await getAdress(addressId);
    return (address.value = response);
  } catch (err) {
    error.value = err.message || "fail to get address";
  }
};

const updateHandle = async () => {
  try {
    const idUpdate = route.params.idAddress;
    const response = await updateAddress(idUpdate, address.value);
    console.log(response);

    // Thêm mới thành công
    showCustomNotification({
      messageText: "Thêm mới thành công!",
      type: "success",
      duration: 2000,
    });

    
  } catch (err) {
    error.value = err.message || "fail to update address";
    // Thêm thất bại
    showCustomNotification({
      messageText: "Thêm mới thất bại!",
      type: "error",
      duration: 3000,
    });
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
        <input
          id="tenNguoiNhan"
          v-model="address.tenNguoiNhan"
          type="text"
          placeholder="Nhập tên người nhận"
          required
        />
      </div>
      <div class="form-group">
        <label for="sdtNguoiNhan">Số điện thoại người nhận</label>
        <input
          id="sdtNguoiNhan"
          v-model="address.sdtNguoiNhan"
          type="text"
          placeholder="Nhập số điện thoại người nhận"
          required
        />
      </div>
      <div class="form-group">
        <label for="soNha">Số nhà</label>
        <input
          id="soNha"
          v-model="address.soNha"
          type="text"
          placeholder="Nhập số nhà"
          required
        />
      </div>
      <div class="form-group">
        <label for="tenDuong">Tên đường</label>
        <input
          id="tenDuong"
          v-model="address.tenDuong"
          type="text"
          placeholder="Nhập tên đường"
          required
        />
      </div>
      <div class="form-group">
        <label for="xaPhuong">Xã/Phường</label>
        <input
          id="xaPhuong"
          v-model="address.xaPhuong"
          type="text"
          placeholder="Nhập xã/phường"
          required
        />
      </div>
      <div class="form-group">
        <label for="quanHuyen">Quận/Huyện</label>
        <input
          id="quanHuyen"
          v-model="address.quanHuyen"
          type="text"
          placeholder="Nhập quận/huyện"
          required
        />
      </div>
      <div class="form-group">
        <label for="tinhThanhPho">Tỉnh/Thành phố</label>
        <input
          id="tinhThanhPho"
          v-model="address.tinhThanhPho"
          type="text"
          placeholder="Nhập tỉnh/thành phố"
          required
        />
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
</style>
