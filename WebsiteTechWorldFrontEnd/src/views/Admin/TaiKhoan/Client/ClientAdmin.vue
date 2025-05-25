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
    err.value = err.message || "An error was thrown while loading the satff";
  } finally {
    isLoading.value = false;
  }
};

// add khach hang phía admin
const clientRequest = ref({
  tenKhachHang: "", // Tên khách hàng
  trangThai: "ENABLE", //Mặc định là đang làm
  gioiTinh: true, // Mặc định là Nam
});

const handleAddClient = async () => {
  try {
    const request = await addClient(clientRequest.value);
    console.log(clientRequest);
    clientRequest.value = {
      trangThai: "ENABLE", //Mặc định là đang làm
      chucVu: "STAFF", // Mặc định là khach hang
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
            v-model="clientRequest.tenNhanVien"
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
            <option value="ENABLE">Đang làm</option>
            <option value="DISABLE">Nghỉ</option>
          </select>
        </div>
        <div class="col-md-6 mb-2">
          <select
            v-model="clientRequest.chucVu"
            placeholder="Chức vụ"
            class="form-select"
          >
            <option value="STAFF">Nhân Viên</option>
            <option value="ADMIN">Quản lý</option>
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
      <button type="submit" class="btn btn-success mt-2">Thêm khách hàng</button>
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
          <th>Chức vụ</th>
          <th>Giới tính</th>
          <th>Năm sinh</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="client in clientList" :key="client.id">
          <td>{{ client.maKhachHang }}</td>
          <td>{{ client.tenNhanVien }}</td>
          <td>{{ client.taiKhoan }}</td>
          <td>{{ client.email }}</td>
          <td>{{ client.sdt }}</td>
          <td>{{ client.diaChi }}</td>
          <td>{{ client.trangThai }}</td>
          <td>{{ client.chucVu }}</td>
          <td>{{ client.gioiTinh ? "Nam" : "Nữ" }}</td>
          <td>{{ new Date(client.namSinh).getFullYear() }}</td>
          <td>
            <RouterLink
              :to="`/admin/client/${client.id}`"
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
.client-container {
  margin-left: 10px; /* đúng bằng chiều rộng sidebar */
  padding: 20px;
  width: 99%;
  box-sizing: border-box;
}


.client-container h3 {
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
