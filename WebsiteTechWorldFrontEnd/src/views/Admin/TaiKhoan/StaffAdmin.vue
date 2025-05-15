<script setup>
import { onMounted, ref } from 'vue';
import { getAllStaff } from '@/Service/Adminservice/TaiKhoan/NhanVienServices';


const staffList = ref([]);
const isLoading = ref(false);
const error = ref("");

const currentPage = ref(0);
const totalPages = ref(0);


//load staff 
// nếu người dùng không truyền page khi gọi hàm
//  → nó sẽ tự động dùng 0.
const loadStaff = async(page =0) => {
    try {
        isLoading.value = true;
        const response = await getAllStaff(page);
        staffList.value = response.content;
        currentPage.value = page;
        totalPages.value = response.totalPages;
    } catch( err) {
        err.value = err.message || "An error was thrown while loading the satff"
    } finally {
        isLoading.value = false;
    }
};


//previous page
const previousPage = () => {
    if(currentPage.value > 0) {
        currentPage.value -= 1;
    }
    else {
        currentPage.value = totalPages.value -1
    }
    loadStaff(currentPage.value)
}

//next page
const nextPage = () => {
    if(currentPage.value < totalPages.value -1) {
        currentPage.value += 1;
    }
    else {
        currentPage.value = 0
    }
    loadStaff(currentPage.value)
}

onMounted(() => {
    loadStaff()
})
</script>

<template>
  <div class="container mt-4">
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
          <td>{{ staff.gioiTinh ? 'Nam' : 'Nữ' }}</td>
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
.container {
  margin-left: 215px;
}

ul {
  list-style: none;
  padding: 0;
}
li {
  padding: 1rem;
  border-bottom: 1px solid #ddd;
  font-size: 1.1rem;
}
.error {
  color: red;
  font-size: 1rem;
}</style>