<script setup>
import { onMounted, reactive, ref, watch } from "vue";
import { getAllClient } from "@/Service/Adminservice/TaiKhoan/KhachHangServices";
import { updateClient } from "@/Service/Adminservice/TaiKhoan/KhachHangServices";

const clientList = ref([]);
const isLoading = ref(false);
const error = ref("");

const currentPage = ref(0);
const totalPages = ref(0);

const searchKeyword = ref("");
const searchTimeout = ref(null); // status of searching

//load client
// nếu người dùng không truyền page khi gọi hàm
//  → nó sẽ tự động dùng 0.
const loadClient = async (page = 0, keyword = null) => {
  try {
    isLoading.value = true;
    const response = await getAllClient(page, keyword);
    clientList.value = response.content;
    currentPage.value = page;
    totalPages.value = response.totalPages;
  } catch (err) {
    error.value = err.message || "An error was thrown while loading the satff";
  } finally {
    isLoading.value = false;
  }
};

//search client
const performSearch = () => {
  //clear old timeout if has
  if (searchTimeout.value) {
    clearTimeout(searchTimeout.value);
  }

  //create new timeout --delay 500ms after user stop typing
  searchTimeout.value = setTimeout(() => {
    currentPage.value = 0;
    loadClient(0, searchKeyword.value || null);
  }, 500);
};

const clearSearch = () => {
  searchKeyword.value = "";
};

watch(searchKeyword, () => {
  performSearch();
});

//previous page
const previousPage = () => {
  if (currentPage.value > 0) {
    currentPage.value -= 1;
  } else {
    currentPage.value = totalPages.value - 1;
  }
  loadClient(currentPage.value, searchKeyword.value || null);
};

//next page
const nextPage = () => {
  if (currentPage.value < totalPages.value - 1) {
    currentPage.value += 1;
  } else {
    currentPage.value = 0;
  }
  loadClient(currentPage.value, searchKeyword.value || null);
};
const firstPage = () => {
  const firstPage = 0;
  currentPage.value = firstPage;
};

const latePage = () => {
  const latePage = totalPages.value - 1;
  currentPage.value = latePage;
};

onMounted(() => {
  loadClient();
});
</script>

<template>
  <div class="client-container">
    <div>
      <RouterLink :to="`/admin/client/add`" class="btn-success"
        >Thêm khách hàng</RouterLink
      >
    </div>
    <hr />

    <!-- Thanh tìm kiếm -->
    <div class="search-section">
      <div class="search-container">
        <div class="search-input-group">
          <div class="search-input-wrapper">
            <i class="bi bi-search search-icon"></i>
            <input
              v-model="searchKeyword"
              type="text"
              class="form-control search-input"
              placeholder="Tìm kiếm theo tên, email, số điện thoại, tên tài khoản..."
            />
            <button
              @click="clearSearch"
              class="btn-clear-inline"
              v-if="searchKeyword"
              title="Xóa tìm kiếm"
            >
              <i class="bi bi-x-circle"></i>
            </button>
          </div>
          <div class="search-status" v-if="isLoading">
            <i class="bi bi-arrow-repeat spin"></i>
            Đang tìm kiếm...
          </div>
        </div>
      </div>
    </div>

    <!-- Hiển thị khi đang tải -->
    <div v-if="isLoading" class="text-center">
      <p>Đang tải dữ liệu...</p>
    </div>

    <!-- Hiển thị kết quả tìm kiếm -->
    <div v-if="searchKeyword && !isLoading" class="search-result-info">
      <p>
        <i class="bi bi-info-circle"></i>
        Kết quả tìm kiếm cho: "<strong>{{ searchKeyword }}</strong
        >" ({{ clientList.length }} khách hàng)
      </p>
    </div>

    <!-- Hiển thị khi không có dữ liệu -->
    <div v-if="!isLoading && clientList.length === 0" class="no-data">
      <p v-if="searchKeyword">
        <i class="bi bi-search"></i>
        Không tìm thấy khách hàng nào với từ khóa "{{ searchKeyword }}"
      </p>
      <p v-else>
        <i class="bi bi-inbox"></i>
        Chưa có khách hàng nào
      </p>
    </div>

    <h2>Danh sách khách hàng</h2>
    <table class="table">
      <thead>
        <tr>
          <th>STT</th>
          <th>Mã khách hàng</th>
          <th>Tên khách hàng</th>
          <th>Tên đăng nhập</th>
          <th>Email</th>
          <th>Địa chỉ</th>
          <th>Số điện thoại</th>
          <th>Trạng thái</th>
          <th>Giới tính</th>
          <th>Năm sinh</th>
          <th colspan="2" style="text-align: center">Thao tác</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(client, index) in clientList" :key="client.id">
          <td>{{ index + 1 + (currentPage * 10) }}</td>
          <td>{{ client.maKhachHang }}</td>
          <td>{{ client.tenKhachHang }}</td>
          <td>{{ client.taiKhoan }}</td>
          <td>{{ client.email }}</td>
          <td>
            <template
              v-if="client.diaChi && client.diaChi.find((dc) => dc.diaChiChinh)"
            >
              {{ client.diaChi.find((dc) => dc.diaChiChinh).soNha }},
              {{ client.diaChi.find((dc) => dc.diaChiChinh).tenDuong }},
              {{ client.diaChi.find((dc) => dc.diaChiChinh).xaPhuong }},
              {{ client.diaChi.find((dc) => dc.diaChiChinh).quanHuyen }},
              {{ client.diaChi.find((dc) => dc.diaChiChinh).tinhThanhPho }}
            </template>
            <template v-else>Không có</template>
          </td>

          <td>
            {{ client.sdt ? client.sdt : "Không có" }}
          </td>

          <!-- Cột Trạng thái -->
          <td>
            <span
              class="badge"
              :class="{
                'badge-active': client.trangThai === 'ACTIVE',
                'badge-inactive': client.trangThai === 'INACTIVE',
              }"
            >
              {{ client.trangThai }}
            </span>
          </td>
          <td>{{ client.gioiTinh ? "Nam" : "Nữ" }}</td>
          <td>
            {{
              client.ngaySinh
                ? new Date(client.ngaySinh).getFullYear()
                : "Không"
            }}
          </td>
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
  background: #f8f9fa;
  min-height: 100vh;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}

.client-container h3 {
  margin-bottom: 25px;
  font-weight: 600;
  color: #495057;
  font-size: 24px;
  text-align: center;
}

.client-container h2 {
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
  background: #007bff;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
  transition: background-color 0.2s ease;
  margin-right: 5px;
  color: white;
}

.btn-primary:hover {
  background: #0056b3;
}

.btn-warning {
  background: #ffc107;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
  transition: background-color 0.2s ease;
  color: #212529;
}

.btn-warning:hover {
  background: #e0a800;
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

/* Address cell styling */
.table td:nth-child(5) {
  max-width: 200px;
  word-wrap: break-word;
  font-size: 13px;
  line-height: 1.4;
}

/* Action buttons container */
.table td:nth-last-child(-n + 2) {
  text-align: center;
  padding: 10px 5px;
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

/* Button styling - viền xám, chỉ icon có màu */
.btn-primary,
.btn-warning {
  background: white;
  border: 1px solid #dee2e6;
  padding: 6px 10px;
  border-radius: 4px;
}

.btn-primary:hover,
.btn-warning:hover {
  background: #f8f9fa;
  border: 1px solid #dee2e6;
}

/* Icon có màu riêng */
.btn-primary .bi {
  color: #007bff;
}

.btn-warning .bi {
  color: #ffc107;
}

.btn-primary:hover .bi {
  color: #007bff;
}

.btn-warning:hover .bi {
  color: #ffc107;
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
  background-color: #f7e7e7 !important; /* Nền đỏ nhạt */
  color: #ed0e0e !important; /* Chữ đỏ đậm */
}

.search-input-group {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 600px;
}

.search-input-wrapper {
  position: relative;
  width: 100%;
  display: flex;
  align-items: center;
}

.search-icon {
  position: absolute;
  left: 16px;
  color: #6c757d;
  font-size: 16px;
  z-index: 2;
}

.search-input {
  flex: 1;
  padding: 12px 16px 12px 45px;
  border: 2px solid #dee2e6;
  border-radius: 25px;
  font-size: 14px;
  transition: all 0.2s ease;
  background: #f8f9fa;
  padding-right: 50px;
}

.search-input:focus {
  border-color: #17a2b8;
  box-shadow: 0 0 0 3px rgba(23, 162, 184, 0.1);
  outline: none;
  background: white;
}

.btn-clear-inline {
  position: absolute;
  right: 12px;
  background: none;
  border: none;
  color: #6c757d;
  cursor: pointer;
  padding: 4px;
  border-radius: 50%;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
}

.btn-clear-inline:hover {
  color: #dc3545;
  background: rgba(220, 53, 69, 0.1);
}

.search-status {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #17a2b8;
  font-size: 14px;
  font-weight: 500;
}

.spin {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.search-result-info {
  background: white;
  padding: 15px 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  border-left: 4px solid #17a2b8;
}

.search-result-info p {
  margin: 0;
  color: #495057;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.search-result-info i {
  color: #17a2b8;
}

.no-data {
  background: white;
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
  margin: 20px 0;
}

.no-data p {
  margin: 0;
  color: #6c757d;
  font-size: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.no-data i {
  font-size: 20px;
}
</style>
