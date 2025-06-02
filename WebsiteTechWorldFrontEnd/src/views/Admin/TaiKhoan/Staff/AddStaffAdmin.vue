<script setup>
import { addStaff } from "@/Service/Adminservice/TaiKhoan/NhanVienServices";
import { reactive, ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElNotification } from "element-plus"; 
import { h } from "vue";

const router = useRouter();

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

// add nhan vien phía admin
const staffRequest = ref({
  tenNhanVien: "", // Tên nhân viên
  trangThai: "ENABLE", //Mặc định là đang làm
  chucVu: "STAFF", // Mặc định là Nhân Viên
  gioiTinh: true, // Mặc định là Nam
});

const errors = reactive({});

const handleAddStaff = async () => {
  // reset lỗi cũ
  Object.keys(errors).forEach((key) => delete errors[key]);

  try {
    await addStaff(staffRequest.value);
    // reset form về mặc định
    staffRequest.value = {
      trangThai: "ENABLE",
      chucVu: "STAFF",
      gioiTinh: true,
    };
    // Thêm mới thành công
    showCustomNotification({
      messageText: "Thêm mới thành công!",
      type: "success",
      duration: 2000,
    });
    router.push(`/admin/staff`)
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
</script>

<template>
  <div class="staff-container">
  <!-- Form thêm nhân viên -->
  <h3>Thêm nhân viên</h3>
  <form @submit.prevent="handleAddStaff">
    <div class="row">
      <div class="col-md-6 mb-2">
        <div v-if="errors.tenNhanVien" class="text-danger mb-1">
          {{ errors.tenNhanVien }}
        </div>
        <input
          v-model="staffRequest.tenNhanVien"
          placeholder="Tên nhân viên"
          class="form-control"
        />
      </div>
      <div class="col-md-6 mb-2">
        <div v-if="errors.taiKhoan" class="text-danger mb-1">
          {{ errors.taiKhoan }}
        </div>
        <input
          v-model="staffRequest.taiKhoan"
          placeholder="Tên đăng nhập"
          class="form-control"
        />
      </div>
      <div class="col-md-6 mb-2">
        <div v-if="errors.matKhau" class="text-danger mb-1">
          {{ errors.matKhau }}
        </div>
        <input
          v-model="staffRequest.matKhau"
          placeholder="Mật khẩu"
          type="password"
          class="form-control"
        />
      </div>
      <div class="col-md-6 mb-2">
        <div v-if="errors.email" class="text-danger mb-1">
          {{ errors.email }}
        </div>
        <input
          v-model="staffRequest.email"
          placeholder="Email"
          class="form-control"
        />
      </div>
      <div class="col-md-6 mb-2">
        <div v-if="errors.sdt" class="text-danger mb-1">
          {{ errors.sdt }}
        </div>
        <input
          v-model="staffRequest.sdt"
          placeholder="Số điện thoại"
          class="form-control"
        />
      </div>
      <div class="col-md-6 mb-2">
        <div v-if="errors.diaChi" class="text-danger mb-1">
          {{ errors.diaChi }}
        </div>
        <input
          v-model="staffRequest.diaChi"
          placeholder="Địa chỉ"
          class="form-control"
        />
      </div>
      <div class="col-md-6 mb-2">
        <select
          v-model="staffRequest.trangThai"
          placeholder="Trạng thái"
          class="form-select"
        >
          <option value="ENABLE">Đang làm</option>
          <option value="DISABLE">Nghỉ</option>
        </select>
      </div>
      <div class="col-md-6 mb-2">
        <select
          v-model="staffRequest.chucVu"
          placeholder="Chức vụ"
          class="form-select"
        >
          <option value="STAFF">Nhân Viên</option>
          <option value="ADMIN">Quản lý</option>
        </select>
      </div>
      <div class="col-md-6 mb-2">
        <select v-model="staffRequest.gioiTinh" class="form-select">
          <option :value="true">Nam</option>
          <option :value="false">Nữ</option>
        </select>
      </div>
      <div class="col-md-6 mb-2">
        <div v-if="errors.namSinh" class="text-danger mb-1">
          {{ errors.namSinh }}
        </div>
        <input
          v-model="staffRequest.namSinh"
          placeholder="Năm sinh"
          type="date"
          class="form-control"
        />
      </div>
    </div>
    <button type="submit" class="btn btn-success mt-2">Thêm nhân viên</button>
  </form>
    </div>
</template>

<style scoped>
.staff-container {
  margin-left: 10px;
  padding: 30px;
  width: 99%;
  box-sizing: border-box;
  background: linear-gradient(135deg, #f5f7fa 0%, #e7e9ec 100%);
  min-height: 100vh;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.staff-container h3 {
  margin-bottom: 25px;
  font-weight: 700;
  color: #2c3e50;
  font-size: 24px;
  text-align: center;
  text-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.staff-container h2 {
  margin-bottom: 20px;
  font-weight: 600;
  color: #2c3e50;
  font-size: 22px;
  text-align: center;
  margin-top: 30px;
}

/* Form styling */
form {
  background: white;
  padding: 30px;
  border-radius: 15px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.1);
  margin-bottom: 30px;
}

.form-control, .form-select {
  border-radius: 10px;
  border: 2px solid #e1e8ed;
  padding: 12px 16px;
  font-size: 14px;
  transition: all 0.3s ease;
  background: #fafbfc;
}

.form-control:focus, .form-select:focus {
  border-color: #3498db;
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
  background: white;
  outline: none;
}

.form-control::placeholder {
  color: #95a5a6;
  font-weight: 400;
}

/* Error styling */
.text-danger {
  font-size: 12px;
  font-weight: 500;
  color: #e74c3c;
  margin-bottom: 5px;
}

/* Button styling */
.btn-success {
  background: linear-gradient(135deg, #27ae60 0%, #2ecc71 100%);
  border: none;
  padding: 12px 30px;
  font-weight: 600;
  border-radius: 25px;
  cursor: pointer;
  width: 15%;
  margin-left: 85%;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(39, 174, 96, 0.3);
  color: white;
  font-size: 14px;
}

.btn-success:hover {
  background: linear-gradient(135deg, #229954 0%, #27ae60 100%);
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(39, 174, 96, 0.4);
}
</style>
