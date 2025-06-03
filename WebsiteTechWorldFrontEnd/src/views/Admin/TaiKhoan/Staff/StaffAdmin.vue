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
    <table class="table">
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
          <td>
            <span
              class="badge"
              :class="{
                'badge-active': staff.trangThai === 'ENABLE',
                'badge-inactive': staff.trangThai === 'DISABLE',
              }"
            >
              {{ staff.trangThai }}
            </span>
          </td>
          <td>
            <span
              class="badge"
              :class="{
                'badge-admin': staff.chucVu === 'ADMIN',
                'badge-staff': staff.chucVu === 'STAFF',
              }"
            >
              {{ staff.chucVu }}
            </span>
          </td>
          <td>{{ staff.gioiTinh ? "Nam" : "Nữ" }}</td>
          <td>{{ new Date(staff.namSinh).getFullYear() }}</td>
          <td>
            <RouterLink
              :to="`/admin/staff/${staff.id}`"
              class="btn btn-primary btn-sm"
              title="Chỉnh sửa"
            >
              <i class="bi bi-pencil-square"></i>
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

<style>
.staff-container {
  margin-left: 10px;
  padding: 30px;
  width: 99%;
  box-sizing: border-box;
  background: #f8f9fa;
  min-height: 100vh;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}

.staff-container h3 {
  margin-bottom: 25px;
  font-weight: 600;
  color: #495057;
  font-size: 24px;
  text-align: center;
}

.staff-container h2 {
  margin-bottom: 20px;
  font-weight: 600;
  color: #495057;
  font-size: 22px;
  text-align: center;
  margin-top: 30px;
}

/* Button styling - match với tone xanh trong ảnh */
.btn-success {
  background: #17a2b8;
  border: none;
  padding: 12px 30px;
  font-weight: 500;
  border-radius: 6px;
  cursor: pointer;
  width: 15%;
  margin-left: 85%;
  transition: all 0.2s ease;
  color: white;
  text-decoration: none;
  font-size: 14px;
}

.btn-success:hover {
  background: #138496;
}

/* Table styling - tối giản như trong ảnh */
.table {
  margin-top: 30px;
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border: 1px solid #dee2e6;
  width: 100%;
}

/* Table header - màu trắng như trong ảnh */
.table thead {
  background: white;
  border-bottom: 1px solid #dee2e6;
}

.table thead th {
  color: #495057 !important;
  font-weight: 500 !important;
  padding: 16px 12px !important;
  border: none !important;
  font-size: 14px !important;
  text-align: left !important;
  background: white !important;
  border-bottom: 1px solid #dee2e6 !important;
}

/* Bỏ striped, table trắng hết */
.table tbody tr {
  background: white !important;
  transition: background-color 0.2s ease;
}

.table tbody tr:hover {
  background-color: #f8f9fa !important;
}

.table th,
.table td {
  vertical-align: middle !important;
  padding: 12px;
  border-bottom: 1px solid #dee2e6;
  font-size: 14px;
}

.table tbody tr:last-child td {
  border-bottom: none;
}

/* Action buttons - tone xanh tối giản */
.btn-primary {
  background: white;
  border: 1px solid #dee2e6;
  padding: 6px 12px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
  transition: background-color 0.2s ease;
  margin-right: 5px;
  color: #007bff;
  text-decoration: none;
}

.btn-primary:hover {
  background: #f8f9fa;
  color: #007bff;
  text-decoration: none;
}

/* Pagination - tối giản */
.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.pagination button {
  padding: 8px 16px;
  border-radius: 4px;
  border: 1px solid #dee2e6;
  cursor: pointer;
  background: white;
  color: #495057;
  font-weight: 500;
  transition: all 0.2s ease;
}

.pagination button:hover {
  background: #e9ecef;
  border-color: #adb5bd;
}

.pagination span {
  font-weight: 500;
  color: #495057;
  font-size: 14px;
  padding: 8px 16px;
  background: #f8f9fa;
  border-radius: 4px;
  border: 1px solid #dee2e6;
}

/* Loading state */
.text-center p {
  font-size: 16px;
  color: #6c757d;
  font-weight: 500;
  padding: 40px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* HR styling */
hr {
  border: none;
  height: 1px;
  background: #dee2e6;
  margin: 20px 0;
}

/* Form controls */
.form-control,
.form-select {
  border-radius: 4px;
  border: 1px solid #ced4da;
  padding: 8px 12px;
  font-size: 14px;
  transition: border-color 0.2s ease;
  background: white;
}

.form-control:focus,
.form-select:focus {
  border-color: #80bdff;
  box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25);
  background: white;
  outline: none;
}

/* Badge styling theo mẫu trong ảnh */
.badge {
  padding: 6px 12px !important;
  border-radius: 4px !important;
  font-size: 11px !important;
  font-weight: bold 600 !important;
  text-transform: uppercase !important;
  display: inline-block !important;
  text-align: center !important;
  min-width: 80px !important;
  letter-spacing: 0.5px !important;
  
}

/* Hoạt động - giống HOÀN THÀNH (xanh lá) */
.badge-active {
  background-color: #e9f6f0 !important; /* Nền xanh lá nhạt */
  color: #1cae6a !important; /* Chữ xanh lá đậm */
}

/* Không hoạt động - giống ĐÃ HỦY (đỏ) */
.badge-inactive {
  background-color: #fff5f5 !important; /* Nền đỏ nhạt */
  color: #ed0e0e !important; /* Chữ đỏ đậm */
}


/* Hoạt động - giống HOÀN THÀNH (xanh lá) */
.badge-admin {
  background-color: #e5eefa !important; /* Nền xanh lá nhạt */
  color: #177ee4 !important; /* Chữ xanh lá đậm */
}

/* Không hoạt động - giống ĐÃ HỦY (đỏ) */
.badge-staff {
  background-color: #fcf2f8 !important; /* Nền đỏ nhạt */
  color: #ff0099 !important; /* Chữ đỏ đậm */
}
</style>
