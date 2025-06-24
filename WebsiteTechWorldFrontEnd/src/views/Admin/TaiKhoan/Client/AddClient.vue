<script setup>
import { onMounted, reactive, ref } from "vue";
import { addClient } from "@/Service/Adminservice/TaiKhoan/KhachHangServices";
import { ElMessage, ElNotification } from "element-plus";
import { h } from "vue";
import { useRouter } from "vue-router";

const errors = reactive({});
const router = useRouter()

// add khach hang phía admin
const clientRequest = ref({
  tenKhachHang: "", // Tên khách hàng
  trangThai: "ACTIVE", //Mặc định là đang làm
  gioiTinh: true, // Mặc định là Nam
});

const handleAddClient = async () => {
  try {
    //reset lỗi cũ
    Object.keys(errors).forEach((key) => delete errors[key]);

    const request = await addClient(clientRequest.value);
    clientRequest.value = {
      trangThai: "ACTIVE", //Mặc định là đang làm
      gioiTinh: true,
    };
    ElMessage.success("Thêm khách hàng thành công")
    router.push(`/admin/client`);
  } catch (err) {
    const errorData = err?.response?.data;
    if (Array.isArray(err)) {
      // err là mảng lỗi [{field, message}, ...]
      err.forEach(({ field, message }) => {
        errors[field] = message;
      });
    }
    else {
    console.error("Unexpected error format:", err);
    ElMessage.error("Thêm khách hàng thất bại")
  } 
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
          <div v-if="errors.tenKhachHang" class="text-danger mb-1">
            {{ errors.tenKhachHang }}
          </div>
          <input
            v-model="clientRequest.tenKhachHang"
            placeholder="Tên khách hàng"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
          <div v-if="errors.taiKhoan" class="text-danger mb-1">
            {{ errors.taiKhoan }}
          </div>
          <input
            v-model="clientRequest.taiKhoan"
            placeholder="Tên đăng nhập"
            class="form-control"
          />
        </div>
        <!-- <div class="col-md-6 mb-2">
          <div v-if="errors.matKhau" class="text-danger mb-1">
            {{ errors.matKhau }}
          </div>
          <input
            v-model="clientRequest.matKhau"
            placeholder="Mật khẩu"
            type="password"
            class="form-control"
          />
        </div> -->
        <div class="col-md-6 mb-2">
          <div v-if="errors.email" class="text-danger mb-1">
            {{ errors.email }}
          </div>
          <input
            v-model="clientRequest.email"
            placeholder="Email"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
          <div v-if="errors.sdt" class="text-danger mb-1">
            {{ errors.sdt }}
          </div>
          <input
            v-model="clientRequest.sdt"
            placeholder="Số điện thoại"
            class="form-control"
          />
        </div>
        <!-- <div class="col-md-6 mb-2">
          <input
            v-model="clientRequest.diaChi"
            placeholder="Địa chỉ"
            class="form-control"
          />
        </div> -->
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
