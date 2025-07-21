<template>
  <div class="voucher-page">
    <div class="header">
      <h2>Danh sách Voucher</h2>
      <div class="diem-kha-dung">Điểm khả dụng: <strong>{{ diemKhaDung }}</strong></div>
    </div>

    <el-tabs v-model="currentTab" class="tabs">
      <el-tab-pane
        v-for="hang in hangList"
        :key="hang"
        :label="hang"
        :name="hang"
      >
        <el-table :data="filteredVouchers" style="width: 100%">
          <el-table-column prop="tenPhieu" label="Tên voucher" />
          <el-table-column prop="diemDoi" label="Điểm cần đổi" />
          <el-table-column prop="giaTriGiam" label="Giá trị giảm" />
          <el-table-column label="Thao tác">
            <template #default="scope">
              <el-button
                type="primary"
                size="small"
                :disabled="scope.row.diemDoi > diemKhaDung"
                @click="doiDiem(scope.row.id)"
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

<script setup>
import { ref, onMounted, computed } from "vue";
import { getAllVoucher } from "@/Service/ClientService/TichDiem/VoucherServies";
import { getDiemKhaDung } from "@/Service/ClientService/TichDiem/DiemServices";
import { ElMessage } from "element-plus";
import api from "@/Service/LoginService/axiosInstance";

const vouchers = ref([]);
const total = ref(0);
const page = ref(1);
const pageSize = 10;

const diemKhaDung = ref(0);
const currentTab = ref("Diamond");

const hangList = ["DIAMOND", "GOLD", "SILVER", "MEMBER"];

const search = ref(null);
const trangThai = ref(null);
const ngayBatDau = ref(null);
const ngayKetThuc = ref(null);
const hangToiThieu = ref("DIAMOND"); // hoặc để null nếu muốn hiển thị tất cả

const fetchVoucher = async () => {
  try {
    const params = {
      page: page.value - 1,
      size: pageSize,
      search: search.value,
      trangThai: trangThai.value,
      ngayBatDau: ngayBatDau.value,
      ngayKetThuc: ngayKetThuc.value,
      hangToiThieu: hangToiThieu.value,
    };

    const res = await getAllVoucher({ params });
    vouchers.value = res.content;
    total.value = res.totalElements;
  } catch (err) {
    ElMessage.error("Lỗi khi tải danh sách voucher");
  }
};

const fetchDiem = async () => {
  try {
    const res = await getDiemKhaDung();
    diemKhaDung.value = res.diemKhaDung;
  } catch (err) {
    ElMessage.error("Lỗi khi lấy điểm khả dụng");
  }
};

const onPageChange = (p) => {
  page.value = p;
  fetchVoucher();
};

const filteredVouchers = computed(() => {
  return vouchers.value.filter(v => v.hangKhachHang === currentTab.value);
});

const doiDiem = async (idPhieuGiamGia) => {
  try {
    await doiDiem(idPhieuGiamGia);
    ElMessage.success("Đổi điểm thành công!");
    fetchVoucher();
    fetchDiem();
  } catch (err) {
    ElMessage.error(err?.message || "Lỗi khi đổi điểm");
  }
};

onMounted(() => {
  fetchVoucher();
  fetchDiem();
});
</script>

<style scoped>
.voucher-page {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.diem-kha-dung {
  font-size: 16px;
  color: #409eff;
}

.tabs {
  margin-top: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>
