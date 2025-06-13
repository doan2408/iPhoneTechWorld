<script setup>
import { onMounted, reactive, ref } from "vue";
import { detailStaff } from "@/Service/Adminservice/TaiKhoan/NhanVienServices";
import { updateStaff } from "@/Service/Adminservice/TaiKhoan/NhanVienServices";
import { useRoute, useRouter } from "vue-router";
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


const staffRequest = reactive({
  tenNhanVien: "",
  taiKhoan: "",
  email: "",
  sdt: "",
  diaChi: "",
  trangThai: "",
  chucVu: "",
  gioiTinh: true,
  namSinh: "",
});

const isLoading = ref(false);
const errors = reactive({});
const route = useRoute();
const router = useRouter();

const id = route.params.id;

const loadStaffDetail = async (id) => {
  try {
    const response = await detailStaff(id);
    Object.assign(staffRequest, response);
  } catch (err) {
    errors = err.message || "Error while loading staff information";
  } finally {
    isLoading.value = false;
  }
};

const handldeUpdate = async () => {
  // reset lỗi cũ
  Object.keys(errors).forEach((key) => delete errors[key]);
  try {
    const id = route.params.id;
    const response = await updateStaff(id, staffRequest);

    // Thêm mới thành công
    showCustomNotification({
      messageText: "Thêm mới thành công!",
      type: "success",
      duration: 2000,
    });
    setTimeout(() => {
    router.push("/admin/staff")
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
  loadStaffDetail(id);
});
</script>

<template>
  <div class="staff-container">
    <h3>Update nhân viên</h3>
    <form @submit.prevent="handleAddStaff">
      <div class="row">
        <div class="col-md-6 mb-2">
          <div v-if="errors.tenNhanVien" class="text-danger mb-1">
            {{ errors.tenNhanVien }}
          </div>
          Tên Nhân Viên
          <input v-model="staffRequest.tenNhanVien" placeholder="Tên nhân viên" class="form-control" />
        </div>
        
        <div class="col-md-6 mb-2">
          <div v-if="errors.taiKhoan" class="text-danger mb-1">
            {{ errors.taiKhoan }}
          </div>
          Tài khoản
          <input v-model="staffRequest.taiKhoan" placeholder="Tên đăng nhập" class="form-control" />
        </div>

        <div class="col-md-6 mb-2">
          <div v-if="errors.email" class="text-danger mb-1">
            {{ errors.email }}
          </div>
          Email
          <input v-model="staffRequest.email" placeholder="Email" class="form-control" />
        </div>

        <div class="col-md-6 mb-2">
          <div v-if="errors.matKhau" class="text-danger mb-1">
            {{ errors.matKhau }}
          </div>
          Mật khẩu
          <input v-model="staffRequest.matKhau" type="password" placeholder="Mật khẩu" class="form-control" />
        </div>

        <div class="col-md-6 mb-2">
          <div v-if="errors.sdt" class="text-danger mb-1">
            {{ errors.sdt }}
          </div>
          Số điện thoại
          <input v-model="staffRequest.sdt" placeholder="Số điện thoại" class="form-control" />
        </div>

        <div class="col-md-6 mb-2">
          <div v-if="errors.diaChi" class="text-danger mb-1">
            {{ errors.diaChi }}
          </div>
          Địa chỉ
          <input v-model="staffRequest.diaChi" placeholder="Địa chỉ" class="form-control" />
        </div>

        <div class="col-md-6 mb-2">
          <div v-if="errors.trangThai" class="text-danger mb-1">
            {{ errors.trangThai }}
          </div>
          Trạng thái
          <select v-model="staffRequest.trangThai" class="form-select">
            <option value="ENABLE">Đang làm</option>
            <option value="DISABLE">Nghỉ</option>
          </select>
        </div>

        <div class="col-md-6 mb-2">
          <div v-if="errors.chucVu" class="text-danger mb-1">
            {{ errors.chucVu }}
          </div>
          Chức vụ
          <select v-model="staffRequest.chucVu" class="form-select">
            <option value="STAFF">Nhân Viên</option>
            <option value="ADMIN">Quản lý</option>
          </select>
        </div>

        <div class="col-md-6 mb-2">
          <div v-if="errors.gioiTinh" class="text-danger mb-1">
            {{ errors.gioiTinh }}
          </div>
          Giới tính
          <select v-model="staffRequest.gioiTinh" class="form-select">
            <option :value="true">Nam</option>
            <option :value="false">Nữ</option>
          </select>
        </div>

        <div class="col-md-6 mb-2">
          <div v-if="errors.namSinh" class="text-danger mb-1">
            {{ errors.namSinh }}
          </div>
          Năm sinh
          <input v-model="staffRequest.namSinh" placeholder="Năm sinh" type="date" class="form-control" />
        </div>
      </div>

      <button type="submit" @click="handldeUpdate" class="btn btn-success mt-2">
        Update thông tin
      </button>
    </form>
  </div>
</template>


<style scoped>
.staff-container {
  margin-left: 10px;
  padding: 20px;
  width: 99%;
  box-sizing: border-box;
}
</style>
