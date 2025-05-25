<template>
  <div class="container mt-4">
    <div class="row">
      <div class="col-auto" style="width: 50%; max-width: 400px;">
        <el-input v-model="searchQuery" placeholder="Tìm kiếm" clearable />
      </div>
      <div class="col-auto">
        <el-button type="primary" @click="handleSearch">Tìm kiếm</el-button>
      </div>
      <div class="col-auto">
        <el-button type="primary" @click="handleRefresh">Làm mới</el-button>
      </div>
    </div>

    <div class="row" style="justify-content: flex-start;">
      <div class="col-auto" style="padding-left: 90%;">
        <el-button type="primary" @click="handleCreate">Tạo mới</el-button>
      </div>

      <el-table :data="tableData" border style="width: 100%">
        <el-table-column type="index" label="#" width="50" />
        <el-table-column prop="first" label="First" />
        <el-table-column prop="last" label="Last" />
        <el-table-column prop="handle" label="Handle" />
      </el-table>

      <div class="pagination">
        <el-button type="primary" size="small" :disabled="currentPage === 1" @click="handlePrevPage">
          Trang trước
        </el-button>
        <span>Trang {{ currentPage }} / {{ totalPages }}</span>
        <el-button type="primary" size="small" :disabled="currentPage === totalPages" @click="handleNextPage">
          Trang sau
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';


// Dữ liệu bảng
const tableData = ref([
  { first: 'Mark', last: 'Otto', handle: '@mdo' },
  { first: 'Jacob', last: 'Thornton', handle: '@fat' },
  { first: 'John', last: 'Doe', handle: '@social' },
]);

// Dữ liệu phân trang
const currentPage = ref(1);
const totalPages = ref(1); // Giả sử chỉ có 1 trang, bạn có thể cập nhật logic phân trang nếu cần
const searchQuery = ref('');

// Xử lý tìm kiếm
const handleSearch = () => {
  console.log('Tìm kiếm:', searchQuery.value);
  // Thêm logic tìm kiếm nếu cần
};

// Xử lý làm mới
const handleRefresh = () => {
  searchQuery.value = '';
  console.log('Làm mới');
  // Thêm logic làm mới nếu cần
};

// Xử lý tạo mới
const handleCreate = () => {
  console.log('Tạo mới');
  // Thêm logic tạo mới nếu cần
};

// Xử lý phân trang
const handlePrevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
  }
};

const handleNextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
  }
};
</script>

<style scoped>
body {
  font-family: Arial, sans-serif;
  background: #fafafa;
  margin: 0;
  padding: 0;
}

.container {
  max-width: 1300px;
  margin-left: 220px;
  background-color: #b5abab;
  padding: 24px;
  box-shadow: 0 4px 10px rgba(226, 209, 209, 0.1);
}

.row {
  display: flex;
  justify-content: center;
  align-items: center;
  background: #fff;
  padding: 20px 40px;
  border-top: 1px solid #eee;
  margin-top: 30px;
}

.col-auto {
  margin-right: 10px;
}

.pagination {
  display: flex;
  width: 30%;
  align-items: center;
  justify-content: space-evenly;
  margin-top: 20px;
}
</style>