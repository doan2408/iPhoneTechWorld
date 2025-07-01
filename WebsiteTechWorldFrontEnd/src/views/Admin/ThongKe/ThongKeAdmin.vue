<template>
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
        <div class="stat-title">Số Đơn Hàng</div>
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
        <div class="stat-title">Số Khách Hàng</div>
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
        <canvas id="LetrevenueChart" width="600" height="400"></canvas>
      </div>
    </div>
  </div>
  <!-- Khách hàng trung thành  -->
  <div class="khach-hang-wrapper">
    <h2>Top 5 Khách Hàng Trung Thành</h2>

    <table class="khach-hang-table">
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
          <td>{{ kh.tong_doanh_thu }}</td>
        </tr>
      </tbody>
    </table>

    <p v-if="khachHangList && khachHangList.length === 0">Không có dữ liệu.</p>
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
        <h2 class="title">Tổng Đơn Bị Hủy</h2>
        <p class="subtitle">Giữa các tháng</p>
      </div>

      <div class="chart">
        <div class="chart-placeholder">
          <canvas id="cancelOrderChart"></canvas>
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

import Chart from 'chart.js/auto'

const dashboardData = ref(null)

onMounted(async () => {
  try {
    dashboardData.value = await getDashboardAdmin()
  } catch (error) {
    console.error('Lỗi khi lấy dữ liệu thống kê:', error)
  }
})
const formatCurrency = (value) => {
  if (!value) return '0 VNĐ'
  return Number(value).toLocaleString('vi-VN') + ' VNĐ'
}
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
              callback: value => value.toLocaleString('vi-VN') + ' đ'
            }
          }
        },
        plugins: {
          tooltip: {
            callbacks: {
              label: ctx => ctx.dataset.label + ': ' + ctx.raw.toLocaleString('vi-VN') + ' đ'
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
    const data = await getDoanhThuTheoThang()

    labelsCancel.value = data.map(item => item.thang)
    valuesCancel.value = data.map(item => item.so_don_bi_huy)

    if (cancelChartInstance) cancelChartInstance.destroy()

    cancelChartInstance = new Chart(document.getElementById('cancelOrderChart'), {
      type: 'bar',
      data: {
        labels: labelsCancel.value,
        datasets: [{
          label: 'Số đơn bị huỷ',
          data: valuesCancel.value,
          backgroundColor: '#f87171'
        }]
      },
      options: {
        responsive: true,
        scales: {
          y: { beginAtZero: true }
        }
      }
    })
  } catch (error) {
    console.error('Lỗi lấy dữ liệu đơn huỷ:', error)
  }
}

onMounted(fetchCancelData)
</script>


<style scoped src="@/style/ThongKe/ThongKe.css"></style>
