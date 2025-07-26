<template>
  <div class="container mx-auto p-6">
    <el-card class="shadow-xl bg-white">
      <div class="flex justify-between items-center mb-6">
        <h2 class="text-2xl font-bold text-gray-900">Danh sách đánh giá sản phẩm</h2>
        <el-select v-model="filterStatus" placeholder="Lọc trạng thái" @change="loadDanhGia" class="w-48">
          <el-option label="Tất cả" value="" />
          <el-option label="Chờ duyệt" value="PENDING_APPROVAL" />
          <el-option label="Đã duyệt" value="APPROVED" />
          <el-option label="Từ chối" value="REFUSE" />
          <el-option label="Ẩn" value="HIDE" />
        </el-select>
      </div>

      <el-table
        v-loading="loading"
        :data="dataReview"
        style="width: 100%"
        border
        class="rounded-lg bg-gray-50"
        :row-class-name="tableRowClassName"
      >
        <el-table-column type="index" label="STT" width="60" :index="indexMethod" align="center" />
        <el-table-column prop="tenKhachHang" label="Khách hàng" width="150" />
        <el-table-column prop="maSanPhamChiTiet" label="Mã sản phẩm" width="120" align="center" />
        <el-table-column prop="tenSanPham" label="Sản phẩm" width="160" />
        <el-table-column prop="tenMau" label="Màu sắc" width="120" align="center" />
        <el-table-column prop="dungLuongRam" label="RAM" width="100" align="center" />
        <el-table-column prop="dungLuongRom" label="ROM" width="100" align="center" />
        <el-table-column label="Sao" width="200" align="center">
          <template #default="{ row }">
            <el-rate v-model="row.soSao" disabled :max="5" class="text-yellow-400" />
          </template>
        </el-table-column>
        <el-table-column prop="noiDung" label="Nội dung" width="400" />
        <el-table-column prop="ngayDanhGia" label="Ngày đánh giá" width="180" align="center" />
        <el-table-column label="Ảnh" width="200" align="center">
          <template #default="{ row }">
            <el-image
              v-for="(img, index) in row.anhUrls"
              :key="index"
              :src="img"
              class="w-14 h-14 mr-2 rounded-md border border-gray-200 hover:border-blue-400 transition"
              fit="cover"
              :preview-src-list="row.anhUrls"
              :initial-index="index"
            />
          </template>
        </el-table-column>
        <el-table-column label="Video" width="200" align="center">
          <template #default="{ row }">
            <video
              v-for="(vid, idx) in row.videoUrls"
              :key="idx"
              :src="vid"
              class="w-16 h-12 mr-2 rounded-md border border-gray-200 hover:border-blue-400 transition"
              controls
            ></video>
          </template>
        </el-table-column>
        <el-table-column prop="noiDungPhanHoi" label="Phản hồi" width="300" />
        <el-table-column label="Trạng thái" width="150" align="center">
          <template #default="{ row }">
            <span
              :class="{
                'text-green-600 font-semibold': row.trangThaiDanhGia === 'APPROVED',
                'text-yellow-600 font-semibold': row.trangThaiDanhGia === 'PENDING_APPROVAL',
                'text-red-600 font-semibold': row.trangThaiDanhGia === 'REFUSE',
                'text-gray-600 font-semibold': row.trangThaiDanhGia === 'HIDE',
              }"
            >
              {{ mapStatusToVietnamese(row.trangThaiDanhGia) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="Hành động" width="350" align="center">
          <template #default="{ row }">
            <el-button
              type="success"
              size="small"
              :disabled="row.trangThaiDanhGia === 'APPROVED'"
              @click="handleApprove(row.idDanhGia)"
              class="mr-2 hover:bg-green-600 transition"
            >
              Phê duyệt
            </el-button>
            <el-button
              type="warning"
              size="small"
              :disabled="row.trangThaiDanhGia === 'REFUSE'"
              @click="handleReject(row.idDanhGia)"
              class="mr-2 hover:bg-yellow-600 transition"
            >
              Từ chối
            </el-button>
            <el-button
              type="info"
              size="small"
              @click="openReplyDialog(row)"
              class="mr-2 hover:bg-blue-600 transition"
            >
              Phản hồi
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click="handleDelete(row.idDanhGia)"
              class="hover:bg-red-600 transition"
            >
              Xóa
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper mt-6 flex justify-center">
        <el-pagination
          background
          layout="prev, pager, next, total"
          :current-page="currentPage"
          :page-size="pageSize"
          :total="totalItems"
          @current-change="handlePageChange"
          class="bg-white p-2 rounded-lg shadow-sm"
        />
      </div>
    </el-card>

    <!-- Dialog phản hồi -->
    <el-dialog
      title="Phản hồi đánh giá"
      v-model="dialogReplyVisible"
      width="35%"
      :before-close="handleCloseReplyDialog"
      class="rounded-lg"
    >
      <el-form :model="replyForm" :rules="replyRules" ref="replyForm" label-position="top" class="p-4">
        <el-form-item label="Nội dung phản hồi" prop="noiDungPhanHoi">
          <el-input
            type="textarea"
            v-model="replyForm.noiDungPhanHoi"
            placeholder="Nhập nội dung phản hồi"
            :rows="4"
            class="w-full"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer flex justify-end gap-3 p-4">
          <el-button @click="dialogReplyVisible = false" class="hover:bg-gray-100 transition">Hủy</el-button>
          <el-button type="primary" @click="submitReplyForm" class="hover:bg-blue-600 transition">Gửi</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { DanhGiaSanPhamAdminService } from '@/Service/Adminservice/DanhGiaSanPhamAdminService/DanhGiaSanPhamAdminService';

const dataReview = ref([]);
const loading = ref(false);
const currentPage = ref(1);
const totalPages = ref(1);
const totalItems = ref(0);
const pageSize = ref(5);
const filterStatus = ref('');
const dialogReplyVisible = ref(false);
const replyForm = ref({
  idDanhGia: null,
  noiDungPhanHoi: '',
});
const replyRules = ref({
  noiDungPhanHoi: [{ required: true, message: 'Vui lòng nhập nội dung phản hồi', trigger: 'blur' }],
});
const replyFormRef = ref(null);

// Ánh xạ trạng thái sang tiếng Việt
const mapStatusToVietnamese = (status) => {
  switch (status) {
    case 'PENDING_APPROVAL':
      return 'Chờ duyệt';
    case 'APPROVED':
      return 'Đã duyệt';
    case 'REFUSE':
      return 'Từ chối';
    case 'HIDE':
      return 'Ẩn';
    default:
      return status;
  }
};

// Tùy chỉnh lớp CSS cho hàng bảng
const tableRowClassName = ({ row }) => {
  return row.trangThaiDanhGia === 'APPROVED'
    ? 'bg-green-50'
    : row.trangThaiDanhGia === 'PENDING_APPROVAL'
    ? 'bg-yellow-50'
    : row.trangThaiDanhGia === 'REFUSE'
    ? 'bg-red-50'
    : 'bg-gray-50';
};

// Hàm tính toán STT
const indexMethod = (index) => {
  return index + 1 + (currentPage.value - 1) * pageSize.value;
};

// Tải danh sách đánh giá
const loadDanhGia = async () => {
  loading.value = true;
  try {
    const response = await DanhGiaSanPhamAdminService.layTatCaDanhGiaAdmin(
      currentPage.value - 1,
      pageSize.value,
      filterStatus.value
    );
    dataReview.value = response.content.map(item => ({
      ...item,
      anhUrls: Array.isArray(item.anhUrls) ? item.anhUrls : item.anhUrls ? item.anhUrls.split(',') : [],
      videoUrls: Array.isArray(item.videoUrls) ? item.videoUrls : item.videoUrls ? item.videoUrls.split(',') : [],
      soSao: Number(item.soSao),
      noiDungPhanHoi: item.noiDungPhanHoi || '',
    }));
    totalPages.value = response.totalPages;
    totalItems.value = response.totalElements;
  } catch (error) {
    ElMessage.error('Không thể tải dữ liệu đánh giá. Vui lòng thử lại sau.');
    console.error('Lỗi khi tải đánh giá:', error);
  } finally {
    loading.value = false;
  }
};

// Xử lý thay đổi trang
const handlePageChange = (page) => {
  currentPage.value = page;
  loadDanhGia();
};

// Mở dialog phản hồi
const openReplyDialog = (row) => {
  replyForm.value = {
    idDanhGia: row.idDanhGia,
    noiDungPhanHoi: row.noiDungPhanHoi || '',
  };
  dialogReplyVisible.value = true;
};

// Đóng dialog phản hồi
const handleCloseReplyDialog = (done) => {
  ElMessageBox.confirm('Bạn có chắc muốn đóng mà không lưu?', 'Xác nhận', {
    confirmButtonText: 'Đồng ý',
    cancelButtonText: 'Hủy',
    type: 'warning',
  })
    .then(() => {
      done();
    })
    .catch(() => {});
};

// Gửi phản hồi
const submitReplyForm = () => {
  replyFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await DanhGiaSanPhamAdminService.phanHoiDanhGia(replyForm.value.idDanhGia, {
          noiDungPhanHoi: replyForm.value.noiDungPhanHoi,
        });
        ElMessage.success('Gửi phản hồi thành công');
        dialogReplyVisible.value = false;
        loadDanhGia();
      } catch (error) {
        ElMessage.error('Lỗi khi gửi phản hồi. Vui lòng thử lại.');
        console.error('Lỗi khi gửi phản hồi:', error);
      }
    }
  });
};

// Xử lý phê duyệt đánh giá
const handleApprove = async (id) => {
  try {
    await ElMessageBox.confirm('Bạn có chắc muốn phê duyệt đánh giá này?', 'Xác nhận', {
      confirmButtonText: 'Phê duyệt',
      cancelButtonText: 'Hủy',
      type: 'warning',
    });
    await DanhGiaSanPhamAdminService.pheDuyetDanhGia(id);
    ElMessage.success('Phê duyệt đánh giá thành công');
    loadDanhGia();
  } catch (error) {
    ElMessage.error('Lỗi khi phê duyệt đánh giá. Vui lòng thử lại.');
    console.error('Lỗi khi phê duyệt:', error);
  }
};

// Xử lý từ chối đánh giá
const handleReject = async (id) => {
  try {
    await ElMessageBox.confirm('Bạn có chắc muốn từ chối đánh giá này?', 'Xác nhận', {
      confirmButtonText: 'Từ chối',
      cancelButtonText: 'Hủy',
      type: 'warning',
    });
    await DanhGiaSanPhamAdminService.tuChoiDanhGia(id);
    ElMessage.success('Từ chối đánh giá thành công');
    loadDanhGia();
  } catch (error) {
    ElMessage.error('Lỗi khi từ chối đánh giá. Vui lòng thử lại.');
    console.error('Lỗi khi từ chối:', error);
  }
};

// Xử lý xóa đánh giá
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('Bạn có chắc muốn xóa đánh giá này?', 'Xác nhận', {
      confirmButtonText: 'Xóa',
      cancelButtonText: 'Hủy',
      type: 'danger',
    });
    await DanhGiaSanPhamAdminService.xoaDanhGia(id);
    ElMessage.success('Xóa đánh giá thành công');
    loadDanhGia();
  } catch (error) {
    ElMessage.error('Lỗi khi xóa đánh giá. Vui lòng thử lại.');
    console.error('Lỗi khi xóa:', error);
  }
};

onMounted(() => {
  loadDanhGia();
});
</script>

<style scoped>
.container {
  max-width: 1400px;
  background-color: #f4f7fe;
  min-height: 100vh;
}
.el-card {
  border-radius: 12px;
  background-color: #ffffff;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}
.el-table {
  border-radius: 12px;
  overflow: hidden;
  background-color: #ffffff;
}
.el-table th {
  background-color: #f8fafc;
  font-weight: 600;
  color: #1f2937;
}
.el-table .bg-green-50 {
  background-color: #f0fdf4;
}
.el-table .bg-yellow-50 {
  background-color: #fefce8;
}
.el-table .bg-red-50 {
  background-color: #fef2f2;
}
.el-table .bg-gray-50 {
  background-color: #f9fafb;
}
.el-button {
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.2s ease;
}
.el-button:hover {
  transform: translateY(-1px);
}
.el-rate .text-yellow-400 {
  color: #facc15 !important;
}
.el-image, video {
  transition: border-color 0.2s ease;
}
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
.el-dialog {
  border-radius: 12px;
}
.el-form-item__label {
  font-weight: 500;
  color: #374151;
}
.el-input, .el-textarea, .el-select {
  border-radius: 8px;
}
.el-pagination {
  font-weight: 500;
}
.el-pagination button, .el-pagination span {
  border-radius: 8px;
}
</style>