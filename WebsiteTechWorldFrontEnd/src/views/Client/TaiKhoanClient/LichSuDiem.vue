<script setup>
import { ref, onMounted } from "vue";
import { getLichSuDiem } from "@/Service/ClientService/TichDiem/TichDiemServices";
import { Back } from "@element-plus/icons-vue";

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
  date.setHours(date.getHours());
  const pad = (n) => (n < 10 ? "0" + n : n);
  const day = pad(date.getDate());
  const month = pad(date.getMonth() + 1);
  const year = date.getFullYear();
  const hour = pad(date.getHours());
  const minute = pad(date.getMinutes());
  const second = pad(date.getSeconds());
  return `${day}/${month}/${year} ${hour}:${minute}:${second}`;
};

onMounted(fetchData);
</script>

<template>
  <div class="container">
    <!-- Header -->
    <div class="header">
      <div class="title-section">
        <h2 class="page-title">Lịch sử tích điểm</h2>
      </div>
      <router-link to="/client/doiDiem">
        <el-button type="info" plain :icon="Back" class="back-btn">
          Quay lại
        </el-button>
      </router-link>
    </div>

    <!-- Filter -->
    <div class="filter-wrapper">
      <el-form :inline="true" class="filter-form">
        <el-form-item label="Từ ngày">
          <el-date-picker
            v-model="filters.fromDate"
            type="datetime"
            placeholder="Chọn ngày bắt đầu"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DDTHH:mm:ssZ"
            class="date-input"
          />
        </el-form-item>
        <el-form-item label="Đến ngày">
          <el-date-picker
            v-model="filters.toDate"
            type="datetime"
            placeholder="Chọn ngày kết thúc"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DDTHH:mm:ssZ"
            class="date-input"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchData" class="action-btn">
            Tìm kiếm
          </el-button>
          <el-button @click="resetFilters" class="action-btn">
            Reset
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- Table -->
    <div class="table-wrapper">
      <el-table 
        :data="lichSuDiemList" 
        border 
        stripe 
        style="width: 100%"
        class="data-table"
      >
        <el-table-column label="STT" type="index" width="60" align="center">
          <template #default="{ $index }">
            {{ $index + 1 + filters.page * filters.size }}
          </template>
        </el-table-column>
        
        <el-table-column label="Loại điểm" width="100" align="center">
          <template #default="{ row }">
            <span :class="row.loaiDiem === 'CONG' ? 'green-text' : 'red-text'">
              {{ row.loaiDiem === "CONG" ? "+++" : "---" }}
            </span>
          </template>
        </el-table-column>
        
        <el-table-column prop="soDiem" label="Số điểm" width="100" align="center" />
        
        <el-table-column prop="ghiChu" label="Ghi chú" min-width="200" />
        
        <el-table-column label="Thời gian" width="160" align="center">
          <template #default="{ row }">
            {{ formatDateTime(row.thoiGian) }}
          </template>
        </el-table-column>
        
        <el-table-column label="Hạn sử dụng" width="160" align="center">
          <template #default="{ row }">
            {{ formatDateTime(row.hanSuDung) || 'Không giới hạn' }}
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- Pagination -->
    <div class="pagination-wrapper">
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
.container {
  background: #f8fafc;
  min-height: 100vh;
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
}

/* Header */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #49a9c1 100%);
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  margin-bottom: 24px;
  position: relative;
}

.title-section {
  flex: 1;
}

.page-title {
  color: white;
  font-size: 28px;
  font-weight: 700;
  margin: 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.back-btn {
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: white;
  padding: 12px 20px;
  border-radius: 25px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.back-btn:hover {
  background: rgba(255, 255, 255, 0.25);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

/* Filter */
.filter-wrapper {
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  margin-bottom: 24px;
  border: 1px solid #e2e8f0;
}

.filter-form {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  align-items: end;
}

.filter-form .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
}

.filter-form :deep(.el-form-item__label) {
  font-weight: 500;
  color: #4a5568;
  margin-bottom: 8px;
}

.date-input {
  width: 220px;
}

.date-input :deep(.el-input__inner) {
  border-radius: 8px;
  border: 2px solid #e2e8f0;
  padding: 12px 16px;
  font-size: 14px;
  transition: all 0.3s ease;
}

.date-input :deep(.el-input__inner:focus) {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.action-btn {
  padding: 12px 24px;
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.3s ease;
  margin-right: 12px;
}

.action-btn[type="primary"] {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
}

.action-btn[type="primary"]:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

.action-btn:not([type="primary"]) {
  background: #f7fafc;
  border: 2px solid #e2e8f0;
  color: #4a5568;
}

.action-btn:not([type="primary"]):hover {
  background: #edf2f7;
  border-color: #cbd5e0;
}

/* Table - Giữ nguyên style cơ bản */
.table-wrapper {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  overflow-x: auto;
}

.data-table {
  font-size: 14px;
}

.data-table :deep(.el-table__header th) {
  background: #fafafa;
  color: #333;
  font-weight: 600;
  padding: 12px 8px;
}

.data-table :deep(.el-table__body td) {
  padding: 12px 8px;
}

.data-table :deep(.el-table__row:hover) {
  background: #f8f9fa;
}

/* Text colors - giữ nguyên như cũ */
.green-text {
  color: rgb(4, 188, 4);
  font-weight: bold;
}

.red-text {
  color: rgb(255, 0, 0);
  font-weight: bold;
}

/* Pagination */
.pagination-wrapper {
  display: flex;
  justify-content: center;
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.pagination-wrapper :deep(.el-pagination) {
  display: flex;
  align-items: center;
  gap: 8px;
}

.pagination-wrapper :deep(.el-pager li) {
  border-radius: 8px;
  margin: 0 2px;
  transition: all 0.3s ease;
  background: white;
  border: 1px solid #e2e8f0;
}

.pagination-wrapper :deep(.el-pager li:hover) {
  background: #f8fafc;
  border-color: #667eea;
  transform: translateY(-1px);
}

.pagination-wrapper :deep(.el-pager li.is-active) {
  background: linear-gradient(135deg, #a8b3e3 0%, #4b91a2 100%);
  color: white;
  border-color: #667eea;
}

.pagination-wrapper :deep(.btn-prev),
.pagination-wrapper :deep(.btn-next) {
  border-radius: 8px;
  transition: all 0.3s ease;
  background: white;
  border: 1px solid #e2e8f0;
}

.pagination-wrapper :deep(.btn-prev:hover),
.pagination-wrapper :deep(.btn-next:hover) {
  background: #667eea;
  color: white;
  transform: translateY(-1px);
}

/* Responsive */
@media (max-width: 768px) {
  .container {
    padding: 16px;
  }
  
  .header {
    flex-direction: column;
    gap: 15px;
    align-items: stretch;
    text-align: center;
  }
  
  .page-title {
    font-size: 24px;
  }
  
  .filter-form {
    flex-direction: column;
    align-items: stretch;
  }
  
  .date-input {
    width: 100%;
  }
  
  .action-btn {
    width: 100%;
    margin-right: 0;
    margin-bottom: 10px;
  }
  
  .table-wrapper {
    padding: 15px;
  }
  
  .data-table :deep(.el-table__header th),
  .data-table :deep(.el-table__body td) {
    padding: 8px 6px;
    font-size: 13px;
  }
}

@media (max-width: 480px) {
  .container {
    padding: 12px;
  }
  
  .header,
  .filter-wrapper,
  .table-wrapper,
  .pagination-wrapper {
    padding: 15px;
  }
  
  .page-title {
    font-size: 20px;
  }
  
  .data-table :deep(.el-table__header th),
  .data-table :deep(.el-table__body td) {
    padding: 6px 4px;
    font-size: 12px;
  }
}
</style>
