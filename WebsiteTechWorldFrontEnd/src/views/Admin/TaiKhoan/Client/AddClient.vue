<script setup>
import { onMounted, reactive, ref } from "vue";
import { addClient } from "@/Service/Adminservice/TaiKhoan/KhachHangServices";
import { ElNotification } from "element-plus"; 
import { h } from "vue";

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
    };
    // Thêm mới thành công
    showCustomNotification({
      messageText: "Thêm mới thành công!",
      type: "success",
      duration: 2000,
    });
    router.push(`/admin/client`)
  } catch (err) {
    showCustomNotification({
      messageText: "Thêm mới thất bại!",
      type: "error",
      duration: 2000,
    });
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