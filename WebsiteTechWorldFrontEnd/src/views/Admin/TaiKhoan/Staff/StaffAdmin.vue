<script setup>
import { onMounted, reactive, ref } from "vue";
import { getAllStaff } from "@/Service/Adminservice/TaiKhoan/NhanVienServices";
import { updateStaff } from "@/Service/Adminservice/TaiKhoan/NhanVienServices";

const staffList = ref([]);
const isLoading = ref(false);
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
    <div>
      <RouterLink :to="`/admin/staff/add`" class="btn-success">Thêm nhân viên</RouterLink>
    </div>
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
          <th>Thao tác</th>
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
              Sửa
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
  margin-left: 10px;
  padding: 30px;
  width: 99%;
  box-sizing: border-box;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
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
  text-decoration: none;
}

.btn-success:hover {
  background: linear-gradient(135deg, #229954 0%, #27ae60 100%);
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(39, 174, 96, 0.4);
}

/* Table styling */
.table {
  margin-top: 30px;
  background: white;
  border-radius: 15px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0,0,0,0.1);
  border: none;
  width: 100%;
}

/* Table header styling */
.table thead {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: table-header-group;
}

.table thead tr {
  display: table-row;
}

.table thead th {
  color: white !important;
  font-weight: 600 !important;
  padding: 18px 15px !important;
  border: none !important;
  font-size: 14px !important;
  text-transform: uppercase !important;
  letter-spacing: 0.5px !important;
  display: table-cell !important;
  vertical-align: middle !important;
  text-align: left !important;
  background: transparent !important;
}

.table tbody tr {
  transition: all 0.3s ease;
}

.table tbody tr:hover {
  background-color: #f8f9fa;
  transform: scale(1.01);
}

.table th,
.table td {
  vertical-align: middle !important;
  padding: 15px;
  border-bottom: 1px solid #ecf0f1;
}

.table tbody tr:last-child td {
  border-bottom: none;
}

/* Update button styling */
.btn-primary {
  background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
  border: none;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  transition: all 0.3s ease;
  box-shadow: 0 3px 10px rgba(52, 152, 219, 0.3);
  color: white;
  text-decoration: none;
}

.btn-primary:hover {
  background: linear-gradient(135deg, #2980b9 0%, #1f4e79 100%);
  transform: translateY(-1px);
  box-shadow: 0 5px 15px rgba(52, 152, 219, 0.4);
  text-decoration: none;
  color: white;
}

.btn-sm {
  padding: 6px 12px;
  font-size: 11px;
}

/* Pagination styling */
.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  background: white;
  padding: 20px;
  border-radius: 15px;
  box-shadow: 0 5px 20px rgba(0,0,0,0.1);
}

.pagination button {
  padding: 10px 20px;
  border-radius: 25px;
  border: none;
  cursor: pointer;
  background: linear-gradient(135deg, #74b9ff 0%, #0984e3 100%);
  color: white;
  font-weight: 600;
  transition: all 0.3s ease;
  box-shadow: 0 3px 10px rgba(116, 185, 255, 0.3);
}

.pagination button:hover {
  background: linear-gradient(135deg, #0984e3 0%, #0770c4 100%);
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(116, 185, 255, 0.4);
}

.pagination span {
  font-weight: 600;
  color: #2c3e50;
  font-size: 16px;
  padding: 10px 20px;
  background: #ecf0f1;
  border-radius: 20px;
}

/* Loading state */
.text-center p {
  font-size: 18px;
  color: #7f8c8d;
  font-weight: 500;
  padding: 40px;
  background: white;
  border-radius: 15px;
  box-shadow: 0 5px 20px rgba(0,0,0,0.1);
}

/* HR styling */
hr {
  border: none;
  height: 2px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  margin: 30px 0;
  border-radius: 2px;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .btn-success {
    width: 100%;
    margin-left: 0;
    margin-top: 15px;
  }
  
  .staff-container {
    padding: 15px;
  }
  
  .table {
    font-size: 12px;
  }
  
  .pagination {
    flex-direction: column;
    gap: 10px;
  }
  
  .btn-sm {
    padding: 4px 8px;
    font-size: 10px;
  }
}

/* Hover effect for action buttons */
.table tbody tr:hover .btn-primary {
  transform: scale(1.1);
}
</style>
