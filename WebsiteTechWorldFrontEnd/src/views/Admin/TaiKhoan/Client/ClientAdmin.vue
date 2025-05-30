<script setup>
import { onMounted, reactive, ref } from "vue";
import { getAllClient } from "@/Service/Adminservice/TaiKhoan/KhachHangServices";
import { addClient } from "@/Service/Adminservice/TaiKhoan/KhachHangServices";
import { updateClient } from "@/Service/Adminservice/TaiKhoan/KhachHangServices";

const clientList = ref([]);
const isLoading = ref(false);
const error = ref("");

const currentPage = ref(0);
const totalPages = ref(0);

//load client
// nếu người dùng không truyền page khi gọi hàm
//  → nó sẽ tự động dùng 0.
const loadClient = async (page = 0) => {
  try {
    isLoading.value = true;
    const response = await getAllClient(page);
    clientList.value = response.content;
    currentPage.value = page;
    totalPages.value = response.totalPages;
  } catch (err) {
    error.value = err.message || "An error was thrown while loading the satff";
  } finally {
    isLoading.value = false;
  }
};

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
    }; //reset form
    await loadClient();
    alert("them khach hang thanh cong");
  } catch (err) {
    err.value = err.message || "An error was thrown while adding the client";
  }
};

//previous page
const previousPage = () => {
  if (currentPage.value > 0) {
    currentPage.value -= 1;
  } else {
    currentPage.value = totalPages.value - 1;
  }
  loadClient(currentPage.value);
};

//next page
const nextPage = () => {
  if (currentPage.value < totalPages.value - 1) {
    currentPage.value += 1;
  } else {
    currentPage.value = 0;
  }
  loadClient(currentPage.value);
};

onMounted(() => {
  loadClient();
});
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
    <hr />

    <!-- Hiển thị khi đang tải -->
    <div v-if="isLoading" class="text-center">
      <p>Đang tải dữ liệu...</p>
    </div>
    <h2>Danh sách khách hàng</h2>
    <table class="table table-striped">
      <thead>
        <tr>
          <th>Mã khách hàng</th>
          <th>Tên khách hàng</th>
          <th>Tên đăng nhập</th>
          <th>Email</th>
          <th>Địa chỉ</th>
          <th>Số điện thoại</th>
          <th>Trạng thái</th>
          <th>Giới tính</th>
          <th>Năm sinh</th>
          <th colspan="2" style="text-align: center;">Thao tác</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="client in clientList" :key="client.id">
          <td>{{ client.maKhachHang }}</td>
          <td>{{ client.tenKhachHang }}</td>
          <td>{{ client.taiKhoan }}</td>
          <td>{{ client.email }}</td>
          <td>
            {{ client.diaChi.find((dc) => dc.diaChiChinh === true)?.soNha }},
            {{ client.diaChi.find((dc) => dc.diaChiChinh)?.tenDuong }},
            {{ client.diaChi.find((dc) => dc.diaChiChinh)?.xaPhuong }},
            {{ client.diaChi.find((dc) => dc.diaChiChinh)?.quanHuyen }},
            {{ client.diaChi.find((dc) => dc.diaChiChinh)?.tinhThanhPho }}
          </td>
          <td>{{ client.sdt }}</td>
          <td>{{ client.trangThai }}</td>
          <td>{{ client.gioiTinh ? "Nam" : "Nữ" }}</td>
          <td>{{ new Date(client.ngaySinh).getFullYear() }}</td>
          <td>
            <RouterLink
              :to="`/admin/client/${client.id}`"
              class="btn btn-primary btn-sm"
              title="Chỉnh sửa"
            >
              <i class="bi bi-pencil-square"></i>
            </RouterLink>
          </td>
          <td>
            <RouterLink
              :to="`/admin/client/addresses/${client.id}`"
              class="btn btn-warning btn-sm"
              title="Xem địa chỉ"
            >
              <i class="bi bi-geo-alt"></i>  
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
.client-container {
  margin-left: 10px;
  padding: 30px;
  width: 99%;
  box-sizing: border-box;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  min-height: 100vh;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.client-container h3 {
  margin-bottom: 25px;
  font-weight: 700;
  color: #2c3e50;
  font-size: 24px;
  text-align: center;
  text-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.client-container h2 {
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

/* Table header styling - FIXED */
.table thead {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: table-header-group; /* Ensure header is displayed */
}

.table thead tr {
  display: table-row; /* Ensure header row is displayed */
}

.table thead th {
  color: white !important; /* Force white text */
  font-weight: 600 !important;
  padding: 18px 15px !important;
  border: none !important;
  font-size: 14px !important;
  text-transform: uppercase !important;
  letter-spacing: 0.5px !important;
  display: table-cell !important; /* Ensure header cells are displayed */
  vertical-align: middle !important;
  text-align: left !important;
  background: transparent !important; /* Let the thead background show */
}

/* Ensure the text in headers is visible */
.table thead th:nth-child(1) { content: "Mã khách hàng"; }
.table thead th:nth-child(2) { content: "Tên khách hàng"; }
.table thead th:nth-child(3) { content: "Tên đăng nhập"; }
.table thead th:nth-child(4) { content: "Email"; }
.table thead th:nth-child(5) { content: "Địa chỉ"; }
.table thead th:nth-child(6) { content: "Số điện thoại"; }
.table thead th:nth-child(7) { content: "Trạng thái"; }
.table thead th:nth-child(8) { content: "Giới tính"; }
.table thead th:nth-child(9) { content: "Năm sinh"; }
.table thead th:nth-child(10) { content: "Thao tác"; text-align: center !important; }
.table thead th:nth-child(11) { content: "Thao tác"; text-align: center !important; }

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

/* Action buttons styling */
.btn-primary {
  background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
  border: none;
  padding: 8px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  transition: all 0.3s ease;
  box-shadow: 0 3px 10px rgba(52, 152, 219, 0.3);
  margin-right: 5px;
  color: white;
}

.btn-primary:hover {
  background: linear-gradient(135deg, #2980b9 0%, #1f4e79 100%);
  transform: translateY(-1px);
  box-shadow: 0 5px 15px rgba(52, 152, 219, 0.4);
}

.btn-warning {
  background: linear-gradient(135deg, #f39c12 0%, #e67e22 100%);
  border: none;
  padding: 8px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  transition: all 0.3s ease;
  box-shadow: 0 3px 10px rgba(243, 156, 18, 0.3);
  color: white;
}

.btn-warning:hover {
  background: linear-gradient(135deg, #e67e22 0%, #d35400 100%);
  transform: translateY(-1px);
  box-shadow: 0 5px 15px rgba(243, 156, 18, 0.4);
}

.btn-sm {
  padding: 6px 10px;
  font-size: 11px;
}

/* Icon styling */
.bi {
  font-size: 14px;
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

/* Address cell styling */
.table td:nth-child(5) {
  max-width: 200px;
  word-wrap: break-word;
  font-size: 13px;
  line-height: 1.4;
}

/* Action buttons container */
.table td:nth-last-child(-n+2) {
  text-align: center;
  padding: 10px 5px;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .btn-success {
    width: 100%;
    margin-left: 0;
    margin-top: 15px;
  }
  
  .client-container {
    padding: 15px;
  }
  
  .table {
    font-size: 12px;
  }
  
  .table td:nth-child(5) {
    max-width: 150px;
    font-size: 11px;
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
.table tbody tr:hover .btn-primary,
.table tbody tr:hover .btn-warning {
  transform: scale(1.1);
}
</style>
