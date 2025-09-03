<template>
  <div class="filter-panel">
 
  <input type="date" v-model="startDate" />

  <input type="date" v-model="endDate" />


  <button @click="() => { console.log('Đã click lọc'); fetchDashboardData() }">Lọc</button>
  <button @click="handleExportDashboard">Xuất Excel</button>
  <button @click="handleExportDoanhThu">Xuất Excel Doanh Thu</button>
  <button @click="handleExportKhachHang">Xuất Excel KHTT</button>

</div>

  <div class="stats-container">
    <div class="stat-card">
      <img src="https://img.icons8.com/ios-filled/50/FFA500/doughnut-chart.png" class="icon" />
      <div class="stat-content">
        <div class="stat-title">Tổng Doanh Thu</div>
        <div class="stat-value">{{ formatCurrency(dashboardData?.doanhThuThang) }}</div>
      </div>
    </div>

    <div class="stat-card">
      <img src="https://img.icons8.com/ios-filled/50/32CD32/megaphone.png" class="icon" />
      <div class="stat-content">
        <div class="stat-title">Số Đơn Hàng Hoàn Tất</div>
        <div class="stat-value">{{ dashboardData?.tongSoDonhang ?? '0' }}</div>
      </div>
    </div>

    <div class="stat-card">
      <img src="https://cdnv2.tgdd.vn/mwg-static/common/News/1569702/iphone%2016%20pro%20-%203.jpg" class="icon" />
      <div class="stat-content">
        <div class="stat-title">Sản Phẩm Đã Bán </div>
        <div class="stat-value">{{ dashboardData?.tongSoSanPham ?? '0' }}</div>
      </div>
    </div>

    <div class="stat-card">
      <img src="https://png.pngtree.com/png-clipart/20190516/original/pngtree-users-vector-icon-png-image_3725294.jpg"
        class="icon" />
      <div class="stat-content">
        <div class="stat-title">Số Khách Hàng Trên Hệ Thống</div>
        <div class="stat-value">{{ dashboardData?.tongSoKhachHang ?? '0' }}</div>
      </div>
    </div>
  </div>
  <div class="charts-flex-wrapper">
    <!-- Biểu đồ cột -->
    <div class="chart-container">
      <div class="header">
        <h2 class="title">Tổng Doanh Thu</h2>
        <p class="subtitle">Giữa các tháng</p>
      </div>

      <div class="chart">
        <div class="chart-placeholder">
          <canvas id="revenueChart"></canvas>
        </div>
      </div>
      <br>
      <div class="footer">
        <p>Updated theo từng tháng</p>
      </div>
    </div>

    <!-- Biểu đồ tròn -->
    <div class="product-chart-container">
      <div class="chart-header">
        <h2 class="chart-title">Thống kê sản phẩm bán chạy</h2>
        <p class="chart-subtitle">Dựa trên tổng số lượng bán</p>
      </div>

      <div class="chart-wrapper">
        <canvas id="LetrevenueChart"></canvas>
      </div>
    </div>
  </div>
  <!-- Khách hàng trung thành  -->
  <div class="khach-hang-wrapper">
  <h2>Top 5 Khách Hàng Trung Thành</h2>

  <!-- Bảng dữ liệu -->
  <table class="khach-hang-table" v-if="khachHangList && khachHangList.length > 0">
    <thead>
      <tr>
        <th>STT</th>
        <th>Mã KH</th>
        <th>Tên KH</th>
        <th>Số lần mua</th>
        <th>Doanh Thu</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(kh, index) in khachHangList" :key="kh.ma_khach_hang">
        <td>{{ index + 1 }}</td>
        <td>{{ kh.ma_khach_hang }}</td>
        <td>{{ kh.ten_khach_hang }}</td>
        <td>{{ kh.so_lan_mua }}</td>
        <td>{{ formatCurrency(kh.tong_doanh_thu) }}</td>
      </tr>
    </tbody>
  </table>

  <!-- Thông báo khi không có dữ liệu -->
  <p v-else-if="!loading">Không có dữ liệu.</p>
</div>

  <!-- sản phẩm sắp hết -->
  <div class="khach-hang-wrapper">
    <h2>Sản phẩm Sắp Hết Hàng</h2>

    <table class="khach-hang-table">
      <thead>
        <tr>
          <th>STT</th>
          <th>Mã Sản Phẩm</th>
          <th>Tên Sản Phảm</th>
          <th>Số Lượng Còn</th>
      
        </tr>
      </thead>
      <tbody>
        <tr v-for="(kh, index) in sanPhamList" :key="kh.ma_san_pham">
          <td>{{ index + 1 }}</td>
          <td>{{ kh.ma_san_pham }}</td>
          <td>{{ kh.ten_san_pham }}</td>
          <td>{{ kh.tong_so_luong }}</td>
          
        </tr>
      </tbody>
    </table>

    <!-- <p v-if="khachHangList && khachHangList.length === 0">Không có dữ liệu.</p> -->
  </div>
<!-- don hàn hủy -->
 
  <div class="chart-container">
    <div class="header">
      <h2 class="title">Tổng Đơn Hủy/ Trả/ Giao thất bại</h2>
      <p class="subtitle">Giữa các tháng</p>
    </div>

    <div class="chart">
      <div class="chart-placeholder">
        <canvas ref="cancelChart"></canvas>
      </div>
    </div>
    <br>
    <div class="footer">
      <p>Updated theo từng tháng</p>
    </div>
  </div>

</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getDashboardAdmin } from '@/Service/Adminservice/ThongKe/ThongKeAdminService'
import { getDoanhThuTheoThang } from '@/Service/Adminservice/ThongKe/ThongKeAdminService';
import { getSanPhamBanChay } from '@/Service/Adminservice/ThongKe/ThongKeAdminService';
import { getKhachHangTrungThanh } from '@/Service/Adminservice/ThongKe/ThongKeAdminService';
import { getSanPhamSapHetHang } from '@/Service/Adminservice/ThongKe/ThongKeAdminService';
import { getDonHangHuy } from '@/Service/Adminservice/ThongKe/ThongKeAdminService';
import { exportDashboardExcel } from '@/Service/Adminservice/ThongKe/ThongKeAdminService';
import { exportDoanhThuTheoThangExcel } from '@/Service/Adminservice/ThongKe/ThongKeAdminService';
import { exportKhachHangTrungThanhExcel } from '@/Service/Adminservice/ThongKe/ThongKeAdminService'




import Chart from 'chart.js/auto'

const dashboardData = ref(null)
const startDate = ref('2025-01-01')
const endDate = ref('')


const fetchDashboardData = async () => {
  try {
    dashboardData.value = await getDashboardAdmin(startDate.value, endDate.value)
  } catch (error) {
    console.error('Lỗi khi lấy dữ liệu thống kê:', error)
  }
}
onMounted(fetchDashboardData)

//fomat tiền 
const formatCurrency = (value) => {
  if (!value) return "0 VNĐ";
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(value);
};
//biểu đồ 
const labels = ref([])
const revenues = ref([])
let chartInstance = null

onMounted(async () => {
  try {
    const data = await getDoanhThuTheoThang()

    labels.value = data.map(item => item.thang)
    revenues.value = data.map(item => item.tong_doanh_thu)

    const ctx = document.getElementById('revenueChart').getContext('2d')

    if (chartInstance) {
      chartInstance.destroy()
    }

    chartInstance = new Chart(ctx, {
      type: 'line',
      data: {
        labels: labels.value,
        datasets: [{
          label: 'Tổng Doanh Thu',
          data: revenues.value,
          backgroundColor: '#40a9ff', // Xanh dương nhạt hơn một chút
          borderColor: '#40a9ff',
          fill: false,
          tension: 0.3,
          pointStyle: 'circle', // điểm hình tròn
          pointRadius: 6,
          pointHoverRadius: 8
        }]
      },
      options: {
        responsive: true,
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              callback: value => value.toLocaleString('vi-VN') + 'VNĐ'
            }
          }
        },
        plugins: {
          tooltip: {
            callbacks: {
              label: ctx => ctx.dataset.label + ': ' + ctx.raw.toLocaleString('vi-VN') + 'VNĐ'
            }
          }
        }
      }
    })
  } catch (error) {
    console.error('Lỗi lấy doanh thu:', error)
  }
})
//biểu đồ tròn 
const tenSP = ref([])
const soLuong = ref([])
let LetchartInstance = null

onMounted(async () => {
  try {
    const data = await getSanPhamBanChay()

    tenSP.value = data.map(item => item.ten_san_pham)
    soLuong.value = data.map(item => item.tong_so_luong_ban)

    const ctx = document.getElementById('LetrevenueChart').getContext('2d')

    if (LetchartInstance) {
      LetchartInstance.destroy()
    }

    LetchartInstance = new Chart(ctx, {
      type: 'pie',
      data: {
        labels: tenSP.value,
        datasets: [{
          label: 'Tổng số SP bán',
          data: soLuong.value,
        }]
      },
      options: {
        responsive: true,
      }
    })
  } catch (error) {
    console.error('Lỗi lấy doanh thu:', error)
  }
})
// khách hàng trung thành
const khachHangList = ref([])

onMounted(async () => {
  try {
    khachHangList.value = await getKhachHangTrungThanh()
  } catch (error) {
    console.error('Lỗi khi lấy dữ liệu thống kê:', error)
  }
})
// Sản phẩm hết hàng 
const sanPhamList = ref([])

onMounted(async () => {
  try {
    sanPhamList.value = await getSanPhamSapHetHang()
  } catch (error) {
    console.error('Lỗi khi lấy dữ liệu thống kê:', error)
  }
})

// cột
const cancelChart = ref(null)
let cancelChartInstance = null

const labelsCancel = ref([])
const valuesCancel = ref([])

const fetchCancelData = async () => {
  try {
    const data = await getDonHangHuy()
    console.log('Cancel data:', data)

    labelsCancel.value = data.map(item => item.thang)
    valuesCancel.value = data.map(item => item.so_don_bi_huy)

    if (cancelChartInstance) cancelChartInstance.destroy()

  cancelChartInstance = new Chart(cancelChart.value, {
  type: 'bar',
  data: {
    labels: labelsCancel.value,
    datasets: [{
      label: 'Số đơn hủy/ trả/ giao thất bại',
      data: valuesCancel.value,
      backgroundColor: '#f87171',
      borderColor: '#ef4444',
      borderWidth: 1,
      borderRadius: 6,
      barPercentage: 0.5,       // Độ rộng của cột so với ô
      categoryPercentage: 0.7   // Khoảng cách giữa các cột
    }]
  },
  options: {
    responsive: true,
    maintainAspectRatio: false,
    layout: {
      padding: 20
    },
    scales: {
      y: {
        beginAtZero: true,
        ticks: {
          stepSize: 1,
          font: {
            size: 12
          },
          color: '#4b5563'
        },
        grid: {
          color: '#e5e7eb'
        }
      },
      x: {
        ticks: {
          font: {
            size: 12
          },
          color: '#4b5563'
        },
        grid: {
          display: false
        }
      }
    },
    plugins: {
      legend: {
        labels: {
          color: '#1f2937',
          font: {
            size: 14,
            weight: 'bold'
          }
        }
      },
      tooltip: {
        backgroundColor: '#f87171',
        titleColor: '#fff',
        bodyColor: '#fff'
      }
    }
  }
})
  } catch (error) {
    console.error('Lỗi lấy dữ liệu đơn huỷ:', error)
  }
}

onMounted(fetchCancelData)
const handleExportDashboard = async () => {
  try {
    await exportDashboardExcel(startDate.value, endDate.value)
  } catch (e) {
    console.error("Export Excel Dashboard error:", e)
  }
}
//Xuất doanh thu 
const handleExportDoanhThu = async () => {
  try {
    await exportDoanhThuTheoThangExcel()
  } catch (e) {
    console.error("Export Doanh thu theo tháng error:", e)
  }
}
///
const handleExportKhachHang = async () => {
  try {
    await exportKhachHangTrungThanhExcel()
  } catch (e) {
    console.error("Export Khách hàng trung thành error:", e)
  }
}
</script>


<style scoped src="@/style/ThongKe/ThongKe.css"></style>
