<template>
  <div class="profile-container">
    <h2 class="profile-title">Thông Tin Cá Nhân</h2>

    <div v-if="isLoading" class="loading">Đang tải...</div>

    <div class="profile-card">
      <div v-if="!isEditing">
        <div class="profile-item">
          <label>Tài khoản:</label>
          <p>{{ userDetail.taiKhoan || "Chưa cập nhật" }}</p>
        </div>

        <div class="profile-item">
          <label>Họ và Tên:</label>
          <p>{{ userDetail.tenKhachHang || "Chưa cập nhật" }}</p>
        </div>
        <div class="profile-item">
          <label>Email:</label>
          <p>{{ userDetail.email || "Chưa cập nhật" }}</p>
        </div>
        <div class="profile-item">
          <label>Số Điện Thoại:</label>
          <p>{{ userDetail.sdt || "Chưa cập nhật" }}</p>
        </div>
        <div class="profile-item">
          <label>Ngày Sinh:</label>
          <p>{{ userDetail.ngaySinh || "Chưa cập nhật" }}</p>
        </div>
        <div class="profile-item">
          <label>Giới Tính:</label>
          <p>{{ userDetail.gioiTinh === true ? "Nam" : "Nữ" }}</p>
        </div>
      </div>

      <div v-if="isEditing" class="edit-form">
        <div class="form-group">
          <div v-if="errors.tenKhachHang" class="text-danger mb-1">{{ errors.tenKhachHang }}</div>
          <label>Họ và Tên:</label>
          <input v-model="form.tenKhachHang" placeholder="Nhập họ và tên" class="form-input" />
        </div>

        <div class="form-group">
          <div v-if="errors.email" class="text-danger mb-1">{{ errors.email }}</div>
          <label>Email:</label>
          <input v-model="form.email" type="email" placeholder="Nhập email" class="form-input" />
        </div>

        <div class="form-group">
          <div v-if="errors.sdt" class="text-danger mb-1">{{ errors.sdt }}</div>
          <label>Số Điện Thoại:</label>
          <input v-model="form.sdt" placeholder="Nhập số điện thoại" class="form-input" />
        </div>

        <div class="form-group">
          <label>Mật khẩu:</label>
          <el-button type="primary" plain size="small" @click="openPwdDialog">Cập nhật mật khẩu</el-button>
        </div>

        <div class="form-group">
          <div v-if="errors.ngaySinh" class="text-danger mb-1">{{ errors.ngaySinh }}</div>
          <label>Ngày Sinh:</label>
          <input v-model="form.ngaySinh" type="date" class="form-input" />
        </div>

        <div class="form-group">
          <div v-if="errors.gioiTinh" class="text-danger mb-1">{{ errors.gioiTinh }}</div>
          <label>Giới Tính:</label>
          <select v-model="form.gioiTinh" class="form-input">
            <option value="" disabled>Chọn giới tính</option>
            <option value="true">Nam</option>
            <option value="false">Nữ</option>
          </select>
        </div>
      </div>

      <div class="button-group">
        <button class="edit-button" @click="toggleEditMode" :disabled="isLoading">
          {{ isEditing ? "Hủy" : "Chỉnh Sửa" }}
        </button>

        <button v-if="isEditing" class="save-button" @click="saveProfile" :disabled="isLoading">
          Lưu Thay Đổi
        </button>
      </div>
    </div>

    <!-- Modal đổi mật khẩu -->
    <el-dialog v-model="showPwdDialog" title="Cập nhật mật khẩu" width="400px">
      <div class="form-group">
        <label>Mật khẩu cũ:</label>
        <el-input v-model="passwordForm.oldPwd" type="password" />
      </div>
      <div class="form-group">
        <label>Mật khẩu mới:</label>
        <el-input v-model="passwordForm.newPwd" type="password" />
      </div>
      <template #footer>
        <el-button @click="showPwdDialog = false">Hủy</el-button>
        <el-button type="primary" @click="confirmPasswordChange">Xác nhận</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { detailClient, updateInfor } from "@/Service/ClientService/TaiKhoan/ClientServices";
import { ElMessage, ElMessageBox } from "element-plus";
import { computed, ref, watch, onMounted } from "vue";
import { useStore } from "vuex";

const store = useStore();
const user = computed(() => store.getters.user || null);
const userDetail = ref({});
const isEditing = ref(false);
const isLoading = ref(false);
const errors = ref({});
const showPwdDialog = ref(false);

const form = ref({
  tenKhachHang: "",
  email: "",
  sdt: "",
  matKhau: "",
  ngaySinh: "",
  gioiTinh: "",
});

const passwordForm = ref({
  oldPwd: "",
  newPwd: "",
});

const loadCurrentUserProfile = async () => {
  try {
    isLoading.value = true;
    errors.value = {};
    const idUser = user.value?.id;
    if (!idUser) throw new Error("Không tìm thấy ID người dùng");
    const response = await detailClient(idUser);
    delete response.matKhau;
    userDetail.value = { ...response };
    form.value = { ...userDetail.value };
  } catch (err) {
    errors.value = err.message || "Lỗi khi tải thông tin người dùng";
    console.error("Error loading user profile:", err);
  } finally {
    isLoading.value = false;
  }
};

onMounted(() => {
  if (user.value?.id) loadCurrentUserProfile();
});

watch(
  () => isEditing.value,
  (newValue) => {
    if (newValue) form.value = { ...userDetail.value };
  }
);

const toggleEditMode = () => {
  isEditing.value = !isEditing.value;
  Object.keys(errors.value).forEach((key) => delete errors.value[key]);
  if (!isEditing.value) form.value = { ...userDetail.value };
};

const saveProfile = async () => {
  Object.keys(errors.value).forEach((key) => delete errors.value[key]);

  try {
    // Confirm trước khi update
    await ElMessageBox.confirm(
      "Bạn có chắc chắn muốn cập nhật thông tin cá nhân?",
      "Xác nhận",
      {
        confirmButtonText: "Đồng ý",
        cancelButtonText: "Hủy",
        type: "warning",
      }
    ).catch(() => {
      // Nếu bấm Hủy thì dừng hàm luôn, không chạy tiếp
      throw "cancel";
    });

    isLoading.value = true;

    const idUser = user.value?.id;
    if (!form.value.matKhau) delete form.value.matKhau;
    delete form.value.hangKhachHang;

    await updateInfor(idUser, form.value);
    await loadCurrentUserProfile();
    ElMessage.success("Update thành công");
    isEditing.value = false;
  } catch (err) {
    if (err === "cancel") {
      // Người dùng bấm Hủy confirm → thoát im lặng
      return;
    }

    if (Array.isArray(err)) {
      err.forEach(({ field, message }) => {
        errors.value[field] = message;
      });
    } else {
      ElMessage.error("Update thông tin thất bại");
    }
  } finally {
    isLoading.value = false;
  }
};

const openPwdDialog = () => {
  passwordForm.value.oldPwd = "";
  passwordForm.value.newPwd = "";
  showPwdDialog.value = true;
};

const confirmPasswordChange = async () => {
  if (!passwordForm.value.oldPwd || !passwordForm.value.newPwd) {
    ElMessage.warning("Vui lòng nhập đủ mật khẩu cũ và mới!");
    return;
  }

  try {
    // Confirm trước khi đổi mật khẩu
    await ElMessageBox.confirm(
      "Bạn có chắc chắn muốn đổi mật khẩu?",
      "Xác nhận",
      {
        confirmButtonText: "Đồng ý",
        cancelButtonText: "Hủy",
        type: "warning",
      }
    ).catch(() => {
      throw "cancel";
    });

    isLoading.value = true;
    const idUser = user.value?.id;

    const payload = {
      ...form.value,
      matKhau: passwordForm.value.newPwd,
      matKhauCu: passwordForm.value.oldPwd,
    };
    console.log("payload: ", payload);
    delete payload.hangKhachHang;

    await updateInfor(idUser, payload);
    await loadCurrentUserProfile();

    ElMessage.success("Đổi mật khẩu thành công");
    showPwdDialog.value = false;
    passwordForm.value.oldPwd = "";
    passwordForm.value.newPwd = "";
  } catch (err) {
    if (err === "cancel") {
      // Bấm Hủy confirm → không làm gì
      return;
    }
    ElMessage.error("Mật khẩu cũ không đúng");
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
.profile-container {
  max-width: 700px;
  margin: 40px auto;
  padding: 20px;
  font-family: 'Inter', system-ui, -apple-system, sans-serif;
  background: linear-gradient(135deg, #e0f7fa, #e8eaf6); /* Gradient background nhẹ nhàng */
  border-radius: 12px; /* Bo góc cho container */
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05); /* Bóng nhẹ cho container */
  min-height: calc(100vh - 80px); /* Đảm bảo chiều cao tối thiểu */
}

.profile-title {
  text-align: center;
  color: #1a1a1a;
  font-size: 2rem;
  font-weight: 600;
  margin-bottom: 30px;
  letter-spacing: -0.02em;
}

.profile-card {
  background: #ffffff;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.profile-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 25px rgba(0, 0, 0, 0.12);
}

.profile-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
}

.profile-item label {
  font-weight: 600;
  color: #2c3e50;
  font-size: 1rem;
}

.profile-item p {
  margin: 0;
  color: #34495e;
  font-size: 1rem;
}

.edit-form {
  margin-top: 25px;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 8px;
  font-size: 0.95rem;
}

.form-input {
  padding: 12px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

.form-input:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
}

.text-danger {
  color: #e74c3c;
  font-size: 0.85rem;
  margin-bottom: 8px;
}

.button-group {
  display: flex;
  gap: 15px;
  margin-top: 30px;
  justify-content: center;
}

.edit-button,
.save-button {
  background: linear-gradient(135deg, #007bff, #0056b3);
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 500;
  transition: background 0.3s ease, transform 0.2s ease;
}

.edit-button:hover,
.save-button:hover {
  background: linear-gradient(135deg, #0056b3, #003d80);
  transform: translateY(-2px);
}

.edit-button:disabled,
.save-button:disabled {
  background: #b0b0b0;
  cursor: not-allowed;
  transform: none;
}

.loading {
  text-align: center;
  color: #007bff;
  font-size: 1.1rem;
  font-weight: 500;
  margin: 30px 0;
  animation: pulse 1.5s infinite;
}

/* Hiệu ứng pulse cho loading */
@keyframes pulse {
  0% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
  100% {
    opacity: 1;
  }
}

/* Responsive design */
@media (max-width: 600px) {
  .profile-container {
    padding: 15px;
    margin: 20px auto;
  }

  .profile-card {
    padding: 20px;
  }

  .profile-title {
    font-size: 1.5rem;
  }

  .button-group {
    flex-direction: column;
  }

  .edit-button,
  .save-button {
    width: 100%;
  }
}
</style>
