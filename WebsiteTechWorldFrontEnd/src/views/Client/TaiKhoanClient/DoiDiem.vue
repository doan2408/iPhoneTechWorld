<script setup>
import { ref, onMounted, watch, reactive } from "vue";
import { getAllVoucher } from "@/Service/ClientService/TichDiem/VoucherServies";
import { getDiemKhaDung } from "@/Service/ClientService/TichDiem/DiemServices";
import { doiDiem } from "@/Service/ClientService/TichDiem/TichDiemServices";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  getHangThanhVien,
  getListHang,
} from "@/Service/ClientService/TichDiem/HangServices";
import { Clock } from "@element-plus/icons-vue";
import { vi } from "element-plus/es/locales.mjs";

// State
const vouchers = ref([]);
const total = ref(0);
const page = ref(1);
const pageSize = 10;

const viDiem = ref({});
const currentTab = ref("MEMBER");
const hangList = ["MEMBER", "SILVER", "GOLD", "DIAMOND"];
const hangThanhVien = ref("");
const errors = reactive([]);
const rankOrder = {
  MEMBER: 0,
  SILVER: 1,
  GOLD: 2,
  DIAMOND: 3,
};

let warningMessageInstance = null;

const showWarningOnce = (message) => {
  // k cho spam message
  if (warningMessageInstance) return; // warning k null => return

  warningMessageInstance = ElMessage.warning({
    message: message,
    timeout: 3000,
    closeButton: true,
    onClose: () => {
      warningMessageInstance = null;
    },
  });
};

// Format tiền
const formatCurrency = (value) => {
  if (typeof value !== "number") return "";
  return value.toLocaleString("vi-VN", { style: "currency", currency: "VND" });
};

// Format points
const formatDiem = (diem) => {
  return new Intl.NumberFormat("vi-VN").format(diem);
};

const formatDate = (dateStr) => {
  if (!dateStr) return "";
  return new Date(dateStr).toLocaleDateString("vi-VN");
};

// checking rank before redeeming
const canRedeemVoucher = (voucherHang, userHang) => {
  return rankOrder[userHang] >= rankOrder[voucherHang];
};

// Params khác (nếu sau dùng lọc nâng cao)
const search = ref(null);
const trangThai = ref(null);
const ngayBatDau = ref(null);
const ngayKetThuc = ref(null);

const confirmAction = async (
  message = "Xác nhận hành động?",
  title = "Thông báo"
) => {
  try {
    await ElMessageBox.confirm(message, title);
    return true;
  } catch {
    return false;
  }
};

// Fetch voucher theo hạng khách hàng
const fetchVoucher = async () => {
  try {
    const params = {
      page: page.value - 1,
      size: pageSize,
      search: search.value,
      trangThai: trangThai.value,
      ngayBatDau: ngayBatDau.value,
      ngayKetThuc: ngayKetThuc.value,
      hangToiThieu: currentTab.value,
    };
    const res = await getAllVoucher({ params });
    vouchers.value = Array.isArray(res.content) ? res.content : [];
    total.value = res.totalElements || 0;
  } catch (err) {
    ElMessage.error("Lỗi khi tải danh sách voucher");
  }
};

// Lấy điểm khả dụng
const fetchDiem = async () => {
  try {
    const res = await getDiemKhaDung();
    viDiem.value = res;
  } catch (err) {
    ElMessage.error("Lỗi khi lấy điểm khả dụng");
  }
};

// Đổi điểm voucher
const doiDiemVouCher = async (idPhieuGiamGia) => {
  const confirmed = await confirmAction("Xác nhận đổi điểm?");
  if (!confirmed) return;

  try {
    await doiDiem(idPhieuGiamGia);
    ElMessage.success("Đổi điểm thành công!");
    fetchVoucher();
    fetchDiem();
  } catch (err) {
    if (Array.isArray(err)) {
      err.forEach(({ field, message }) => {
        showWarningOnce(message);
      });
    } else {
      ElMessage.error("Lỗi khi đổi điểm");
    }
  }
};

//get hang khach hang
const getHang = async () => {
  try {
    const response = await getHangThanhVien();
    hangThanhVien.value = response;
  } catch (err) {
    ElMessage.error(err?.message || "Lỗi khi lấy thông tin hạng");
  }
};

// Chuyển trang
const onPageChange = (p) => {
  page.value = p;
  fetchVoucher();
};

// Khi đổi tab (hạng khách hàng)
watch(currentTab, () => {
  page.value = 1;
  fetchVoucher();
});

onMounted(() => {
  getHang();
  fetchVoucher();
  fetchDiem();
});
</script>

<template>
  <div class="voucher-page">
    <div class="header">
      <h2>Danh sách Voucher</h2>

      <div class="diem-kha-dung">
        Điểm khả dụng: <strong>{{ formatDiem(viDiem.diemKhaDung) }}</strong>
      </div>
      <div class="hang-thanh-vien">
        Hạng: <strong> {{ hangThanhVien }}</strong>
      </div>
      
      <div class="diem-sap-het-han" v-if="viDiem.diemSapHetHan">
        Có:
        <strong>{{ formatDiem(viDiem.diemSapHetHan || 0) }}</strong>
        điểm sắp hết hạn trong 7 ngày tới
      </div>

      <el-tooltip content="Lịch sử điểm" placement="top">
        <router-link to="/client/lichSuDiem" class="history-icon">
          <el-icon><Clock /></el-icon>
        </router-link>
      </el-tooltip>
    </div>

    <el-tabs v-model="currentTab" class="tabs">
      <el-tab-pane
        v-for="hang in hangList"
        :key="hang"
        :label="hang"
        :name="hang"
      >
        <el-table
          :data="vouchers"
          style="width: 100%"
          :empty-text="`Chưa có voucher cho mục ${hang}`"
        >
          <el-table-column prop="tenKhuyenMai" label="Tên voucher" />
          <el-table-column label="Điểm cần đổi">
            <template #default="scope">
              {{ formatDiem(scope.row.soDiemCanDeDoi) }}
            </template>
          </el-table-column>

          <el-table-column label="Giá trị giảm">
            <template #default="scope">
              {{ formatCurrency(scope.row.giaTriKhuyenMai) }}
            </template>
          </el-table-column>

          <el-table-column label="Áp dụng đơn hàng tối thiểu">
            <template #default="scope">
              {{ formatCurrency(scope.row.giaTriDonHangToiThieu) }}
            </template>
          </el-table-column>

          <el-table-column label="Ngày bắt đầu">
            <template #default="scope">
              {{ formatDate(scope.row.ngayBatDau) }}
            </template>
          </el-table-column>

          <el-table-column label="Ngày kết thúc">
            <template #default="scope">
              {{ formatDate(scope.row.ngayKetThuc) }}
            </template>
          </el-table-column>

          <el-table-column label="Thao tác">
            <template #default="scope">
              <el-button
                type="primary"
                size="small"
                :disabled="
                  scope.row.soDiemCanDeDoi > viDiem.diemKhaDung ||
                  !canRedeemVoucher(currentTab, hangThanhVien)
                "
                @click="doiDiemVouCher(scope.row.id)"
              >
                Đổi điểm
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="pagination">
          <el-pagination
            background
            layout="prev, pager, next"
            :total="total"
            :page-size="pageSize"
            @current-change="onPageChange"
          />
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<style scoped>
.voucher-page {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
  background-color: #f8fafc;
  min-height: 80vh;
}

.header {
  display: grid;
  grid-template-columns: 1fr auto auto auto auto;
  grid-template-rows: auto;
  gap: 16px;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #49a9c1 100%);
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  margin-bottom: 24px;
  position: relative;
}

.header h2 {
  grid-column: 1;
  grid-row: 1;
  color: white;
  font-size: 28px;
  font-weight: 700;
  margin: 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.diem-sap-het-han {
  grid-column: 2;
  grid-row: 1;
  background: rgba(255, 255, 255, 0.1);
  padding: 8px 16px;
  border-radius: 15px;
  backdrop-filter: blur(5px);
  color: white;
  font-size: 14px;
  font-weight: 500;
}

.diem-sap-het-han strong {
  color: #ff4757;
  font-weight: 700;
}

.diem-kha-dung {
  grid-column: 3;
  grid-row: 1;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(10px);
  padding: 12px 20px;
  border-radius: 25px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  font-size: 16px;
  color: white;
  font-weight: 500;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.diem-kha-dung strong {
  font-weight: 700;
  color: #ffd700;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}

.hang-thanh-vien {
  grid-column: 4;
  grid-row: 1;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(10px);
  padding: 12px 20px;
  border-radius: 25px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  font-size: 16px;
  color: white;
  font-weight: 500;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.hang-thanh-vien strong {
  font-weight: 700;
  color: #ff6b6b;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}

.history-icon {
  grid-column: 5;
  grid-row: 1;
  font-size: 24px;
  color: #ffffff;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.15);
  padding: 12px;
  border-radius: 50%;
  transition: all 0.3s ease;
  text-decoration: none;
}

.history-icon:hover {
  background: rgba(255, 255, 255, 0.25);
  transform: scale(1.1);
}

.tabs {
  margin-top: 24px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.tabs .el-tabs__header {
  margin: 0;
  background: #f8fafc;
  border-bottom: 2px solid #e2e8f0;
}

.tabs .el-tabs__nav-wrap {
  padding: 0 24px;
}

.tabs .el-tabs__item {
  font-weight: 600;
  font-size: 16px;
  padding: 16px 24px;
  color: #64748b;
  transition: all 0.3s ease;
}

.tabs .el-tabs__item:hover {
  color: #667eea;
}

.tabs .el-tabs__item.is-active {
  color: #667eea;
  background: white;
  border-radius: 8px 8px 0 0;
}

.tabs .el-tabs__active-bar {
  background: #667eea;
  height: 3px;
}

.tabs .el-tabs__content {
  padding: 24px;
}

.el-table {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.el-table th {
  background: #f1f5f9;
  color: #334155;
  font-weight: 600;
  border-bottom: 2px solid #e2e8f0;
}

.el-table td {
  border-bottom: 1px solid #f1f5f9;
  padding: 16px 12px;
}

.el-table tr:hover {
  background: #f8fafc;
}

.el-button--primary {
  background: linear-gradient(135deg, #30d153 0%, #16dad3 100%);
  border: none;
  border-radius: 20px;
  padding: 8px 20px;
  font-weight: 600;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
}

.el-button--primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.el-button--primary:disabled {
  background: #cbd5e1;
  color: #94a3b8;
  transform: none;
  box-shadow: none;
}

.pagination {
  margin-top: 24px;
  text-align: center;
  padding: 20px 0;
}

.el-pagination {
  justify-content: center;
}

.el-pagination .el-pager li {
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  margin: 0 4px;
  transition: all 0.3s ease;
}

.el-pagination .el-pager li:hover {
  background: #667eea;
  color: white;
  transform: translateY(-1px);
}

.el-pagination .el-pager li.is-active {
  background: #667eea;
  color: white;
  border-color: #667eea;
}

.el-pagination .btn-prev,
.el-pagination .btn-next {
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.el-pagination .btn-prev:hover,
.el-pagination .btn-next:hover {
  background: #667eea;
  color: white;
  transform: translateY(-1px);
}

/* Responsive Design */
@media (max-width: 768px) {
  .voucher-page {
    padding: 16px;
  }
  
  .header {
    grid-template-columns: 1fr;
    grid-template-rows: auto auto auto auto auto;
    text-align: center;
    gap: 12px;
  }
  
  .header h2 {
    grid-column: 1;
    grid-row: 1;
    font-size: 24px;
  }
  
  .diem-sap-het-han {
    grid-column: 1;
    grid-row: 2;
  }
  
  .diem-kha-dung {
    grid-column: 1;
    grid-row: 3;
    font-size: 14px;
    padding: 10px 16px;
  }
  
  .hang-thanh-vien {
    grid-column: 1;
    grid-row: 4;
    font-size: 14px;
    padding: 10px 16px;
  }
  
  .history-icon {
    grid-column: 1;
    grid-row: 5;
    justify-self: center;
  }
  
  .tabs .el-tabs__content {
    padding: 16px;
  }
  
  .el-table {
    font-size: 14px;
  }
  
  .el-table td {
    padding: 12px 8px;
  }
}

/* Animation cho loading state */
@keyframes shimmer {
  0% {
    background-position: -200px 0;
  }
  100% {
    background-position: calc(200px + 100%) 0;
  }
}

.loading-shimmer {
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200px 100%;
  animation: shimmer 1.5s infinite;
}
</style>
