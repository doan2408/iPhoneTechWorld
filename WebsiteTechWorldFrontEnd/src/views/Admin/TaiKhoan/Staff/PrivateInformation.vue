<template>
  <div class="profile-container">
    <h2 class="profile-title">Thông Tin Cá Nhân</h2>

    <!-- Hiển thị loading -->
    <div v-if="isLoading" class="loading">Đang tải...</div>

    <div class="profile-card">
      <!-- Hiển thị thông tin khi không chỉnh sửa -->
      <div v-if="!isEditing">
        <div class="profile-item">
          <label>Tài khoản:</label>
          <p>{{ userDetail.taiKhoan || "Chưa cập nhật" }}</p>
        </div>

        <div class="profile-item">
          <label>Họ và Tên:</label>
          <p>{{ userDetail.tenNhanVien || "Chưa cập nhật" }}</p>
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
          <label>Địa Chỉ:</label>
          <p>{{ userDetail.diaChi || "Chưa cập nhật" }}</p>
        </div>
        <div class="profile-item">
          <label>Ngày Sinh:</label>
          <p>{{ userDetail.namSinh || "Chưa cập nhật" }}</p>
        </div>
        <div class="profile-item">
          <label>Giới Tính:</label>
          <p>
            {{ userDetail.gioiTinh === true ? "Nam" : "Nữ" }}
          </p>
        </div>
      </div>

      <!-- Form chỉnh sửa -->
      <div v-if="isEditing" class="edit-form">
        <div class="form-group">
          <div v-if="errors.tenNhanVien" class="text-danger mb-1">
            {{ errors.tenNhanVien }}
          </div>
          <label>Họ và Tên:</label>
          <input
            v-model="form.tenNhanVien"
            placeholder="Nhập họ và tên"
            class="form-input"
          />
        </div>

        <div class="form-group">
          <div v-if="errors.email" class="text-danger mb-1">
            {{ errors.email }}
          </div>
          <label>Email:</label>
          <input
            v-model="form.email"
            type="email"
            placeholder="Nhập email"
            class="form-input"
          />
        </div>

        <div class="form-group">
          <div v-if="errors.sdt" class="text-danger mb-1">{{ errors.sdt }}</div>
          <label>Số Điện Thoại:</label>
          <input
            v-model="form.sdt"
            placeholder="Nhập số điện thoại"
            class="form-input"
          />
        </div>

        <div class="form-group">
          <div v-if="errors.diaChi" class="text-danger mb-1">
            {{ errors.diaChi }}
          </div>
          <label>Địa Chỉ:</label>
          <input
            v-model="form.diaChi"
            placeholder="Nhập địa chỉ"
            class="form-input"
          />
        </div>

        <div class="form-group">
          <div v-if="errors.namSinh" class="text-danger mb-1">
            {{ errors.namSinh }}
          </div>
          <label>Ngày Sinh:</label>
          <input v-model="form.namSinh" type="date" class="form-input" />
        </div>

        <div class="form-group">
          <div v-if="errors.gioiTinh" class="text-danger mb-1">
            {{ errors.gioiTinh }}
          </div>
          <label>Giới Tính:</label>
          <select v-model="form.gioiTinh" class="form-input">
            <option value="" disabled>Chọn giới tính</option>
            <option value="true">Nam</option>
            <option value="false">Nữ</option>
          </select>
        </div>
      </div>

      <!-- Buttons -->
      <div class="button-group">
        <button
          class="edit-button"
          @click="toggleEditMode"
          :disabled="isLoading"
        >
          {{ isEditing ? "Hủy" : "Chỉnh Sửa" }}
        </button>

        <button
          v-if="isEditing"
          class="save-button"
          @click="saveProfile"
          :disabled="isLoading"
        >
          Lưu Thay Đổi
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {
  detailStaff,
  updateStaff,
} from "@/Service/Adminservice/TaiKhoan/NhanVienServices";
import { ElMessage } from "element-plus";
import { computed, ref, watch, onMounted } from "vue";
import { useStore } from "vuex";

// Lấy store
const store = useStore();

// Lấy thông tin user từ store
const user = computed(() => store.getters.user || null);

// State cho thông tin user đầy đủ từ API
const userDetail = ref({});

// State cho form chỉnh sửa
const isEditing = ref(false);
const isLoading = ref(false);
const errors = ref({});

// Khởi tạo form
const form = ref({
  tenNhanVien: "",
  email: "",
  sdt: "",
  diaChi: "",
  ngaySinh: "",
  gioiTinh: "",
});

// Load thông tin user hiện tại
const loadCurrentUserProfile = async () => {
  try {
    isLoading.value = true;
    errors.value = {};

    const idUser = user.value?.id;
    if (!idUser) {
      throw new Error("Không tìm thấy ID người dùng");
    }

    const response = await detailStaff(idUser);
    userDetail.value = { ...response };

    // Đồng bộ form với userDetail khi tải xong
    form.value = { ...userDetail.value };
  } catch (err) {
    errors.value = err.message || "Lỗi khi tải thông tin người dùng";
    console.error("Error loading user profile:", err);
  } finally {
    isLoading.value = false;
  }
};

// Auto load user profile khi component được mount
onMounted(() => {
  if (user.value?.id) {
    loadCurrentUserProfile();
  }
});

// Đồng bộ form với userDetail khi bắt đầu chỉnh sửa
watch(
  () => isEditing.value,
  (newValue) => {
    if (newValue) {
      // Điền dữ liệu từ userDetail vào form khi bắt đầu chỉnh sửa
      form.value = { ...userDetail.value };
    }
  }
);

// Hàm toggle chế độ chỉnh sửa
const toggleEditMode = () => {
  isEditing.value = !isEditing.value;
  Object.keys(errors.value).forEach((key) => delete errors.value[key]);
  if (!isEditing.value) {
    // Reset form khi hủy chỉnh sửa
    form.value = { ...userDetail.value };
  }
};

// Hàm lưu thông tin (giả lập, vì bạn sẽ xử lý qua service riêng)
const saveProfile = async () => {
  Object.keys(errors.value).forEach((key) => delete errors.value[key]);
  try {
    isLoading.value = true;

    console.log("Saving current user profile:", form.value);

    const idUser = user.value?.id;
    const response = await updateStaff(idUser, form.value);
    loadCurrentUserProfile();
    ElMessage.success("Update thành công");

    // Tắt chế độ chỉnh sửa
    isEditing.value = false;
  } catch (err) {
    if (Array.isArray(err)) {
      // err là mảng lỗi [{field, message}, ...]
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
