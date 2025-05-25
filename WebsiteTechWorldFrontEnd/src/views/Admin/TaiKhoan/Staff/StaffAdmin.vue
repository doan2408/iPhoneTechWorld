<script setup>
import { onMounted, reactive, ref } from "vue";
import { getAllStaff } from "@/Service/Adminservice/TaiKhoan/NhanVienServices";
import { addStaff } from "@/Service/Adminservice/TaiKhoan/NhanVienServices";
import { updateStaff } from "@/Service/Adminservice/TaiKhoan/NhanVienServices";

const staffList = ref([]);
const isLoading = ref(false);
const error = ref("");

const currentPage = ref(0);
const totalPages = ref(0);

//load staff
// nếu người dùng không truyền page khi gọi hàm
//  → nó sẽ tự động dùng 0.
const loadStaff = async (page = 0) => {
  try {
    isLoading.value = true;
    const response = await getAllStaff(page);
    staffList.value = response.content;
    currentPage.value = page;
    totalPages.value = response.totalPages;
  } catch (err) {
    err.value = err.message || "An error was thrown while loading the satff";
  } finally {
    isLoading.value = false;
  }
};

// add nhan vien phía admin
const staffRequest = ref({
  tenNhanVien: "", // Tên nhân viên
  trangThai: "ENABLE", //Mặc định là đang làm
  chucVu: "STAFF", // Mặc định là Nhân Viên
  gioiTinh: true, // Mặc định là Nam
});

const handleAddStaff = async () => {
  try {
    const request = await addStaff(staffRequest.value);
    console.log(staffRequest);
    staffRequest.value = {
      trangThai: "ENABLE", //Mặc định là đang làm
      chucVu: "STAFF", // Mặc định là Nhân Viên
      gioiTinh: true,
    }; //reset form
    await loadStaff();
    alert("them nhan vien thanh cong");
  } catch (err) {
    err.value = err.message || "An error was thrown while adding the staff";
  }
};

//previous page
const previousPage = () => {
  if (currentPage.value > 0) {
    currentPage.value -= 1;
  } else {
    currentPage.value = totalPages.value - 1;
  }
  loadStaff(currentPage.value);
};

//next page
const nextPage = () => {
  if (currentPage.value < totalPages.value - 1) {
    currentPage.value += 1;
  } else {
    currentPage.value = 0;
  }
  loadStaff(currentPage.value);
};

onMounted(() => {
  loadStaff();
});
</script>

<template>
  <div class="staff-container">
    <!-- Form thêm nhân viên -->
    <h3>Thêm nhân viên</h3>
    <form @submit.prevent="handleAddStaff">
      <div class="row">
        <div class="col-md-6 mb-2">
          <input
            v-model="staffRequest.tenNhanVien"
            placeholder="Tên nhân viên"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
          <input
            v-model="staffRequest.taiKhoan"
            placeholder="Tên đăng nhập"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
          <input
            v-model="staffRequest.matKhau"
            placeholder="Mật khẩu"
            type="password"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
          <input
            v-model="staffRequest.email"
            placeholder="Email"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
          <input
            v-model="staffRequest.sdt"
            placeholder="Số điện thoại"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
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
    <hr />

    <!-- Hiển thị khi đang tải -->
    <div v-if="isLoading" class="text-center">
      <p>Đang tải dữ liệu...</p>
    </div>
    <h2>Danh sách nhân viên</h2>
    <table class="table table-striped">
      <thead>
        <tr>
          <th>Mã nhân viên</th>
          <th>Tên nhân viên</th>
          <th>Tên đăng nhập</th>
          <th>Email</th>
          <th>Địa chỉ</th>
          <th>Số điện thoại</th>
          <th>Trạng thái</th>
          <th>Chức vụ</th>
          <th>Giới tính</th>
          <th>Năm sinh</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="staff in staffList" :key="staff.id">
          <td>{{ staff.maNhanVien }}</td>
          <td>{{ staff.tenNhanVien }}</td>
          <td>{{ staff.taiKhoan }}</td>
          <td>{{ staff.email }}</td>
          <td>{{ staff.sdt }}</td>
          <td>{{ staff.diaChi }}</td>
          <td>{{ staff.trangThai }}</td>
          <td>{{ staff.chucVu }}</td>
          <td>{{ staff.gioiTinh ? "Nam" : "Nữ" }}</td>
          <td>{{ new Date(staff.namSinh).getFullYear() }}</td>
          <td>
            <RouterLink
              :to="`/admin/staff/${staff.id}`"
              class="btn btn-primary btn-sm"
            >
              Update
            </RouterLink>
          </td>
        </tr>
      </tbody>
    </table>
    <!-- Nút phân trang -->
    <div class="pagination">
      <button @click="previousPage">Trang trước</button>
      <span>Trang {{ currentPage + 1 }} / {{ totalPages }}</span>
      <button @click="nextPage">Trang sau</button>
    </div>
  </div>
</template>

<style scoped>
.staff-container {
  margin-left: 10px; /* đúng bằng chiều rộng sidebar */
  padding: 20px;
  width: 99%;
  box-sizing: border-box;
}


.staff-container h3 {
  margin-bottom: 20px;
  font-weight: 600;
  color: #222;
}

.form-control {
  border-radius: 6px;
  border: 1px solid #ccc;
  padding: 8px 12px;
  font-size: 14px;
}

.form-control:focus {
  border-color: #28a745;
  box-shadow: 0 0 6px rgba(40,167,69,0.3);
}

.btn-success {
  background-color: #28a745;
  border: none;
  padding: 10px 20px;
  font-weight: 600;
  border-radius: 10px;
  cursor: pointer;
  width: 15%;
  margin-left: 85%;
}

.btn-success:hover {
  background-color: #218838;
}

.table {
  margin-top: 30px;
}

.table th, .table td {
  vertical-align: middle !important;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  gap: 15px;
}

.pagination button {
  padding: 6px 12px;
  border-radius: 5px;
  border: 1px solid #ccc;
  cursor: pointer;
  background-color: #f8f9fa;
  transition: background-color 0.3s ease;
}

.pagination button:hover {
  background-color: #e2e6ea;
}
</style>
