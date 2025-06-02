<template>
  <div class="stats-container">
    <div class="stat-card">
      <img src="https://img.icons8.com/ios-filled/50/FFA500/doughnut-chart.png" class="icon" />
      <div class="stat-content">
        <div class="stat-title">Tổng Doanh Thu</div>
        <div class="stat-value">{{ dashboardData?.doanhThuThang ?? '...' }}</div>
      </div>
    </div>

    <div class="stat-card">
      <img src="https://img.icons8.com/ios-filled/50/32CD32/megaphone.png" class="icon" />
      <div class="stat-content">
        <div class="stat-title">Số Đơn Hàng</div>
        <div class="stat-value">{{ dashboardData?.tongSoDonhang ?? '...' }}</div>
      </div>
    </div>

    <div class="stat-card">
      <img src="https://cdnv2.tgdd.vn/mwg-static/common/News/1569702/iphone%2016%20pro%20-%203.jpg" class="icon" />
      <div class="stat-content">
        <div class="stat-title">Sản Phẩm Đã Bán </div>
        <div class="stat-value">{{ dashboardData?.tongSoSanPham ?? '...' }}</div>
      </div>
    </div>

    <div class="stat-card">
      <img src="https://png.pngtree.com/png-clipart/20190516/original/pngtree-users-vector-icon-png-image_3725294.jpg"
        class="icon" />
      <div class="stat-content">
        <div class="stat-title">Số Khách Hàng</div>
        <div class="stat-value">{{ dashboardData?.tongSoKhachHang ?? '...' }}</div>
      </div>
    </div>
  </div>
  <!-- Biều đồ  -->
  <div class="chart-container">
    <div class="header">
      <h2 class="title">Tổng Doanh Thu</h2>
      <p class="subtitle">Giữa các tháng</p>
    </div>

    <div class="chart">

      <div class="chart-placeholder"><canvas id="revenueChart"width="490" height="300"></canvas></div>
    </div>

   

    <div class="footer">
      <p>Updated theo từng tháng</p>
    </div>
  </div>

</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getDashboardAdmin } from '@/Service/Adminservice/ThongKe/ThongKeAdminService'
import { getDoanhThuTheoThang } from '@/Service/Adminservice/ThongKe/ThongKeAdminService';
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
          backgroundColor: '#4bc0c0',
          borderColor: '#379b9b',
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
</script>


<style scoped>
.stats-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  padding: 20px;
  background-color: #faf9fb;
}

.stat-card {
  display: flex;
  align-items: center;
  border: 1px solid #eee;
  border-radius: 12px;
  padding: 15px;
  background: white;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

.icon {
  width: 40px;
  height: 40px;
  margin-right: 12px;
}

.stat-content {
  display: flex;
  flex-direction: column;
}

.stat-title {
  font-size: 14px;
  color: #999;
}

.stat-value {
  font-size: 20px;
  font-weight: bold;
  margin-top: 4px;
  color: #333;
}

.stat-updated {
  font-size: 12px;
  color: #888;
  margin-top: 6px;
}

/* Biều đồ  */
.chart-container {
  width: 100%;
  max-width: 600px;
  /* max-height: 400px; */
  margin-top: 20px;
  margin-left: 20px;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-family: Arial, sans-serif;
  background: #fff;
}


.header {
  margin-bottom: 1rem;
}

.title {
  font-size: 24px;
  font-weight: 600;
  color: #2d3748;
  letter-spacing: 0.5px;
}

.subtitle {
  font-size: 14px;
  color: #718096;
  font-style: italic;
}

.chart {
  margin: 20px 0;
  height: 300px;
  background: #f5f5f5;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #999;
}

.legend {
  display: flex;
  gap: 20px;
  margin-top: 10px;
}

.legend-item {
  display: flex;
  align-items: center;
  font-size: 14px;
}

.dot {
  display: inline-block;
  width: 20px;
  height: 20px;
  margin-right: 6px;
}

.dot.open {
  background-color: #2CA8FF;
}

.footer {
  margin-top: 10px;
  font-size: 12px;
  color: #666;
}
</style>
