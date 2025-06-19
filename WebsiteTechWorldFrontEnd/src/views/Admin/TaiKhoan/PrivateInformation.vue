<template>
  <div class="profile-container">
    <h2 class="profile-title">Thông Tin Cá Nhân</h2>
    <div class="profile-card">
      <div class="profile-item">
        <label>Họ và Tên:</label>
        <p>{{ user.tenNhanVien || 'Chưa cập nhật' }}</p>
      </div>
      <div class="profile-item">
        <label>Email:</label>
        <p>{{ user.email || 'Chưa cập nhật' }}</p>
      </div>
      <div class="profile-item">
        <label>Số Điện Thoại:</label>
        <p>{{ user.sdt || 'Chưa cập nhật' }}</p>
      </div>
      <div class="profile-item">
        <label>Địa Chỉ:</label>
        <p>{{ user.diaChi || 'Chưa cập nhật' }}</p>
      </div>
      <div class="profile-item">
        <label>Ngày Sinh:</label>
        <p>{{ user.ngaySinh || 'Chưa cập nhật' }}</p>
      </div>
      <div class="profile-item">
        <label>Giới Tính:</label>
        <p>{{ user.gioiTinh || 'Chưa cập nhật' }}</p>
      </div>
      <button class="edit-button" @click="toggleEditMode">
        {{ isEditing ? 'Hủy' : 'Chỉnh Sửa' }}
      </button>
      <div v-if="isEditing" class="edit-form">
        <input v-model="form.tenNhanVien" placeholder="Họ và Tên" />
        <input v-model="form.email" placeholder="Email" />
        <input v-model="form.sdt" placeholder="Số Điện Thoại" />
        <input v-model="form.diaChi" placeholder="Địa Chỉ" />
        <input v-model="form.ngaySinh" placeholder="Ngày Sinh (YYYY-MM-DD)" />
        <select v-model="form.gioiTinh">
          <option value="" disabled>Chọn giới tính</option>
          <option value="NAM">Nam</option>
          <option value="NU">Nữ</option>
          <option value="KHAC">Khác</option>
        </select>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, watch } from 'vue';
import { useStore } from 'vuex';

// Lấy store
const store = useStore();

// Lấy thông tin user từ store
const user = computed(() => store.getters.user || {});

// State cho form chỉnh sửa
const isEditing = ref(false);
const form = ref({
  tenNhanVien: '',
  email: '',
  sdt: '',
  diaChi: '',
  ngaySinh: '',
  gioiTinh: '',
});

// Đồng bộ form với user khi bắt đầu chỉnh sửa
watch(
  () => isEditing.value,
  (newValue) => {
    if (newValue) {
      form.value = { ...user.value };
    }
  }
);

// Hàm toggle chế độ chỉnh sửa
const toggleEditMode = () => {
  isEditing.value = !isEditing.value;
};
</script>

<style scoped>
.profile-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.profile-title {
  text-align: center;
  color: #333;
}

.profile-card {
  background: #f9f9f9;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.profile-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
}

.profile-item label {
  font-weight: bold;
  color: #555;
}

.profile-item p {
  margin: 0;
  color: #333;
}

.edit-button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  width: 100%;
  margin-top: 20px;
}

.edit-button:hover {
  background-color: #0056b3;
}

.edit-form {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.edit-form input,
.edit-form select {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}
</style>