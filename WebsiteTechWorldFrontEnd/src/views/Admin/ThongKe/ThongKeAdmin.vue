<template>
  <div class="stats-container">
    <div class="stat-card">
      <img src="https://img.icons8.com/ios-filled/50/FFA500/doughnut-chart.png" class="icon" />
      <div class="stat-content">
        <div class="stat-title">Tổng Doanh Thu</div>
        <div class="stat-value">{{ dashboardData?.doanhThuThang ?? '0' }}</div>
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
          <canvas id="revenueChart" width="450" height="350"></canvas>
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
  <div class="p-4">
    <h2 class="text-xl font-semibold mb-4">Sản phẩm bán chạy</h2>

    <!-- Bộ lọc thời gian -->
    <div class="mb-4 flex gap-2 items-center">
      <label>Ngày bắt đầu:</label>
      <input type="date" v-model="startDate" class="border p-1" />
      <label>Ngày kết thúc:</label>
      <input type="date" v-model="endDate" class="border p-1" />
      <button @click="fetchData" class="px-3 py-1 bg-blue-500 text-white rounded">Lọc</button>
    </div>

    <!-- Bảng dữ liệu -->
    <table class="min-w-full border border-gray-300">
      <thead class="bg-gray-100">
        <tr>
          <th class="border px-4 py-2">Tên sản phẩm</th>
          <th class="border px-4 py-2">Số lượng bán</th>
          <th class="border px-4 py-2">Doanh thu</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(item, index) in items" :key="index">
          <td class="border px-4 py-2">{{ item.ten_san_pham }}</td>
          <td class="border px-4 py-2">{{ item.tong_so_luong_ban }}</td>
          <td class="border px-4 py-2">{{ formatCurrency(item.tong_doanh_thu) }}</td>
        </tr>
        <!-- <tr v-if="items.length === 0">
          <td colspan="3" class="text-center py-4 text-gray-500">Không có dữ liệu</td>
        </tr> -->
      </tbody>
    </table>

    <!-- Phân trang -->
    <div class="pagination flex items-center gap-4 mt-4">
      <button :disabled="page === 1" @click="prevPage"
        class="px-3 py-1 border rounded bg-gray-200 disabled:opacity-50">Trước</button>
      <span>Trang {{ page }}</span>
      <button :disabled="!hasMore" @click="nextPage"
        class="px-3 py-1 border rounded bg-gray-200 disabled:opacity-50">Sau</button>
    </div>

    <!-- Thông báo lỗi -->
    <div v-if="error" class="mt-4 text-red-500">{{ error }}</div>
  </div>




</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getDashboardAdmin } from '@/Service/Adminservice/ThongKe/ThongKeAdminService'
import { getDoanhThuTheoThang } from '@/Service/Adminservice/ThongKe/ThongKeAdminService';
import { getSanPhamBanChay } from '@/Service/Adminservice/ThongKe/ThongKeAdminService';
// import { getTopSanPhamBanChay } from '@/Service/Adminservice/ThongKe/ThongKeAdminService';
import Chart from 'chart.js/auto'

const dashboardData = ref(null)

onMounted(async () => {
  try {
    dashboardData.value = await getDashboardAdmin()
  } catch (error) {
    console.error('Lỗi khi lấy dữ liệu thống kê:', error)
  }
})
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
///danh sách ap 
// const items = ref([])
// const page = ref(1)
// const limit = 10     
// const hasMore = ref(false)
// const error = ref(null)
// const startDate = ref('')
// const endDate = ref('')

// Hàm gọi API
// const fetchData = async () => {
//   error.value = null
//   try {
//     const response = await axiosInstance.get('/admin/thong-ke/top-san-pham-ban-chay', {
//       params: {
//         startDate: startDate.value,
//         endDate: endDate.value,
//         page: page.value,
//         limit
//       }
//     })

//     items.value = response.data.content || []
//     hasMore.value = (response.data.number + 1) < response.data.totalPages
//   } catch (err) {
//     error.value = err.response?.data?.message || 'Lỗi không xác định khi lấy dữ liệu'
//   }
// }

// Hàm phân trang
// const prevPage = () => {
//   if (page.value > 1) {
//     page.value--
//     fetchData()
//   }
// }

// const nextPage = () => {
//   if (hasMore.value) {
//     page.value++
//     fetchData()
//   }
// }

// // Định dạng tiền tệ
// const formatCurrency = (val) => {
//   return Number(val).toLocaleString('vi-VN', {
//     style: 'currency',
//     currency: 'VND'
//   })
// }

// // Khởi tạo ngày mặc định
// onMounted(() => {
//   const today = new Date().toISOString().split('T')[0]
//   startDate.value = today
//   endDate.value = today
//   fetchData()
// })
</script>


<style scoped src="@/style/ThongKe/ThongKe.css"></style>
