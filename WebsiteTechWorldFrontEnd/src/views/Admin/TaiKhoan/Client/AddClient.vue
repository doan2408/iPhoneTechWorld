<script setup>
import { onMounted, reactive, ref } from "vue";
import { addClient } from "@/Service/Adminservice/TaiKhoan/KhachHangServices";

// add khach hang phía admin
const clientRequest = ref({
  tenKhachHang: "", // Tên khách hàng
  trangThai: "ACTIVE", //Mặc định là đang làm
  gioiTinh: true, // Mặc định là Nam
});

const handleAddClient = async () => {
  try {
    const request = await addClient(clientRequest.value);
    clientRequest.value = {
      trangThai: "ACTIVE", //Mặc định là đang làm
      gioiTinh: true,
    }; //reset form
    await loadClient();
    alert("them khach hang thanh cong");
  } catch (err) {
    err.value = err.message || "An error was thrown while adding the client";
  }
};
</script>

<template>
  <div class="client-container">
    <!-- Form thêm khách hàng -->
    <h3>Thêm khách hàng</h3>
    <form @submit.prevent="handleAddClient">
      <div class="row">
        <div class="col-md-6 mb-2">
          <input
            v-model="clientRequest.tenKhachHang"
            placeholder="Tên khách hàng"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
          <input
            v-model="clientRequest.taiKhoan"
            placeholder="Tên đăng nhập"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
          <input
            v-model="clientRequest.matKhau"
            placeholder="Mật khẩu"
            type="password"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
          <input
            v-model="clientRequest.email"
            placeholder="Email"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
          <input
            v-model="clientRequest.sdt"
            placeholder="Số điện thoại"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
          <input
            v-model="clientRequest.diaChi"
            placeholder="Địa chỉ"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
          <select
            v-model="clientRequest.trangThai"
            placeholder="Trạng thái"
            class="form-select"
          >
            <option value="ACTIVE">Hoạt động</option>
            <option value="INACTIVE">Ngừng hoạt động</option>
          </select>
        </div>
        <div class="col-md-6 mb-2">
          <select v-model="clientRequest.gioiTinh" class="form-select">
            <option :value="true">Nam</option>
            <option :value="false">Nữ</option>
          </select>
        </div>
        <div class="col-md-6 mb-2">
          <input
            v-model="clientRequest.namSinh"
            placeholder="Năm sinh"
            type="date"
            class="form-control"
          />
        </div>
      </div>
      <button type="submit" class="btn btn-success mt-2">
        Thêm khách hàng
      </button>
    </form>
    </div>
</template>