<script setup>
import { ref, onMounted } from "vue";
import { getLichSuDiem } from "@/Service/ClientService/TichDiem/TichDiemServices";

const filters = ref({
  fromDate: null,
  toDate: null,
  page: 0,
  size: 10,
});

const lichSuDiemList = ref([]);
const totalElements = ref(0);

const fetchData = async () => {
  try {
    const res = await getLichSuDiem({
      ...filters.value,
    });
    lichSuDiemList.value = res.content;
    totalElements.value = res.totalElements;
  } catch (err) {
    console.error("Lỗi khi lấy dữ liệu:", err);
  }
};

const resetFilters = () => {
  filters.value.fromDate = null;
  filters.value.toDate = null;
  filters.value.page = 0;
  fetchData();
};

const handlePageChange = (pageNumber) => {
  filters.value.page = pageNumber - 1;
  fetchData();
};

const formatDateTime = (value) => {
  if (!value) return "";

  const date = new Date(value);

  // Nếu backend trả về UTC
  date.setHours(date.getHours());

  const pad = (n) => (n < 10 ? "0" + n : n);

  const day = pad(date.getDate());
  const month = pad(date.getMonth() + 1); // tháng bắt đầu từ 0
  const year = date.getFullYear();

  const hour = pad(date.getHours());
  const minute = pad(date.getMinutes());
  const second = pad(date.getSeconds());

  return `${day}/${month}/${year} ${hour}:${minute}:${second}`;
};

onMounted(fetchData);
</script>
<template>
  <div class="p-4">
    <h2 class="text-xl font-semibold mb-4">Lịch sử tích điểm</h2>

    <!-- Bộ lọc ngày -->
    <el-form :inline="true" class="mb-4">
      <el-form-item label="Từ ngày">
        <el-date-picker
          v-model="filters.fromDate"
          type="datetime"
          placeholder="Chọn ngày bắt đầu"
          format="YYYY-MM-DD HH:mm:ss"
          value-format="YYYY-MM-DDTHH:mm:ssZ"
        />
      </el-form-item>

      <el-form-item label="Đến ngày">
        <el-date-picker
          v-model="filters.toDate"
          type="datetime"
          placeholder="Chọn ngày kết thúc"
          format="YYYY-MM-DD HH:mm:ss"
          value-format="YYYY-MM-DDTHH:mm:ssZ"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="fetchData">Tìm kiếm</el-button>
        <el-button @click="resetFilters">Reset</el-button>
      </el-form-item>
    </el-form>

    <!-- Bảng lịch sử điểm -->
    <el-table :data="lichSuDiemList" border stripe style="width: 100%">
      <el-table-column label="STT" type="index" width="60" />
      <el-table-column prop="soDiem" label="Số điểm" />
      <el-table-column prop="loaiDiem" label="Loại điểm" />
      <el-table-column prop="ghiChu" label="Ghi chú" />
      <el-table-column
        label="Thời gian"
        :formatter="(row) => formatDateTime(row.thoiGian)"
      />
      <el-table-column
        label="Hạn sử dụng"
        :formatter="
          (row) =>
            formatDateTime(row.hanSuDung) ? formatDateTime(row.hanSuDung) : ''
        "
      />
    </el-table>

    <!-- Phân trang -->
    <div class="mt-4 text-center">
      <el-pagination
        background
        layout="prev, pager, next"
        :total="totalElements"
        :page-size="filters.size"
        :current-page="filters.page + 1"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<style scoped>
/* Khung tổng thể */
div {
  background-color: #fff;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
}

/* Tiêu đề */
h2 {
  font-size: 22px;
  font-weight: 600;
  color: #333;
  margin-bottom: 20px;
}

/* Form filter */
.el-form {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-bottom: 20px;
}

.el-form-item {
  margin-right: 12px;
}

/* Bảng */
.el-table {
  font-size: 14px;
}

/* Căn giữa phân trang */
.el-pagination {
  justify-content: center;
  margin-top: 24px;
}
</style>
