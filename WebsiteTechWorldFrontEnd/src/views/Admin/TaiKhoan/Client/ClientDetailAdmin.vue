<script setup>
import { onMounted, reactive, ref } from "vue";
import { detailClient } from "@/Service/Adminservice/TaiKhoan/KhachHangServices";
import { updateClient } from "@/Service/Adminservice/TaiKhoan/KhachHangServices";
import { useRoute, useRouter } from "vue-router";
import { ElNotification } from "element-plus";
import { h } from "vue";

const clientRequest = reactive({
  tenKhachHang: "",
  taiKhoan: "",
  email: "",
  sdt: "",
  trangThai: "",
  gioiTinh: true,
  namSinh: "",
});

const isLoading = ref(false);
const errors = reactive({});
const route = useRoute();
const router = useRouter();

const id = route.params.id;

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

const loadClientDetail = async (id) => {
  try {
    const response = await detailClient(id);
    Object.assign(clientRequest, response);
    console.log(clientRequest);
  } catch (err) {
    errors = err.message || "Error while loading client information";
  } finally {
    isLoading.value = false;
  }
};

const handldeUpdate = async () => {
  try {
    // reset lỗi cũ
    Object.keys(errors).forEach((key) => delete errors[key]);

    const id = route.params.id;
    const response = await updateClient(id, clientRequest);

    // Thêm mới thành công
    showCustomNotification({
      messageText: "Update thông tin thành công!",
      type: "success",
      duration: 2000,
    });
    setTimeout(() => {
      router.push("/admin/client");
    }, 1000);
  } catch (err) {
    if (Array.isArray(err)) {
      // err là mảng lỗi [{field, message}, ...]
      err.forEach(({ field, message }) => {
        errors[field] = message;
      });
    } else {
      showCustomNotification({
        messageText: "Có lỗi xảy ra!",
        type: "error",
        duration: 2000,
      });
    }
  }
};

onMounted(() => {
  loadClientDetail(id);
});
</script>

<template>
  <div class="client-container">
    <!-- Form thêm khách hàng -->
    <h3>Update khách hàng</h3>
    <form @submit.prevent="handleAddClient">
      <div class="row">
        <div class="col-md-6 mb-2">
          <div v-if="errors.tenKhachHang" class="text-danger mb-1">
            {{ errors.tenKhachHang }}
          </div>
          Tên Khách Hàng
          <input
            v-model="clientRequest.tenKhachHang"
            placeholder="Tên khách hàng"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
          Tài khoản
          <div v-if="errors.taiKhoan" class="text-danger mb-1">
            {{ errors.taiKhoan }}
          </div>
          <input
            v-model="clientRequest.taiKhoan"
            placeholder="Tên đăng nhập"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
          Email
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
          Số điện thoại
          <div v-if="errors.sdt" class="text-danger mb-1">
            {{ errors.sdt }}
          </div>
          <input
            v-model="clientRequest.sdt"
            placeholder="Số điện thoại"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
          Trạng thái
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
          Giới tính
          <select v-model="clientRequest.gioiTinh" class="form-select">
            <option :value="true">Nam</option>
            <option :value="false">Nữ</option>
          </select>
        </div>
        <div class="col-md-6 mb-2">
          Năm sinh
          <input
            v-model="clientRequest.ngaySinh"
            placeholder="Năm sinh"
            type="date"
            class="form-control"
          />
        </div>
      </div>
      <button type="submit" @click="handldeUpdate" class="btn btn-success mt-2">
        Update thông tin
      </button>
    </form>
  </div>
</template>

<style scoped>
.client-container {
  margin-left: 10px;
  padding: 20px;
  width: 99%;
  box-sizing: border-box;
}
</style>
