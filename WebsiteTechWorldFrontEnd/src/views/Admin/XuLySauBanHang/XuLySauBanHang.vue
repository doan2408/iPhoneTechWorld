<template>
    <div class="after-sales-container">
        <!-- Header với chính sách -->
        <header class="page-header">
            <div class="header-content">
                <h1 class="page-title">Xử Lý Sau Bán Hàng</h1>
                <div class="policy-section">
                    <h3>Chính Sách Cửa Hàng</h3>
                    <div class="policy-items">
                        <span class="policy-item">🔄 Đổi trả trong 7 ngày</span>
                        <span class="policy-item">📦 Miễn phí vận chuyển đổi trả</span>
                        <span class="policy-item">💰 Hoàn tiền 100% nếu lỗi từ shop</span>
                        <span class="policy-item">⏰ Xử lý trong 24h</span>
                    </div>
                </div>
            </div>
        </header>

        <!-- Thống kê tổng quan -->
        <section class="stats-section">
            <div class="stats-grid">
                <div class="stat-card urgent">
                    <div class="stat-number">{{ stats.total }}</div>
                    <div class="stat-label">Tổng đơn cần phê duyệt</div>
                </div>
                <div class="stat-card warning">
                    <div class="stat-number">{{ stats.failed }}</div>
                    <div class="stat-label">Giao hàng thất bại</div>
                </div>
                <div class="stat-card info">
                    <div class="stat-number">{{ stats.returns }}</div>
                    <div class="stat-label">Yêu cầu trả hàng</div>
                </div>
                <div class="stat-card success">
                    <div class="stat-number">{{ stats.resolved }}</div>
                    <div class="stat-label">Đã xử lý hôm nay</div>
                </div>
            </div>
        </section>

        <!-- Bộ lọc và tìm kiếm -->
        <section class="filter-section">
            <div class="filter-controls">
                <div class="search-box">
                    <input type="text" v-model="searchQuery" placeholder="Tìm kiếm theo mã đơn, tên khách hàng..."
                        class="search-input">
                    <button class="search-btn">🔍</button>
                </div>
                <select v-model="sortBy" class="sort-select">
                    <option value="asc">Sắp xếp theo ngày giảm dần</option>
                    <option value="desc">Sắp xếp theo ngày tăng dần</option>
                </select>
            </div>
        </section>

        <!-- Tab Navigation -->
        <section class="tabs-section">
            <div class="tab-navigation">
                <button v-for="tab in tabs" :key="tab.id" @click="activeTab = tab.id"
                    :class="['tab-button', { active: activeTab === tab.id }]">
                    {{ tab.label }}
                </button>
            </div>

            <!-- Nội dung tab -->
            <div class="tab-content">
                <div v-if="activeTab === 'all'" class="table-container">
                    <table class="orders-table">
                        <thead>
                            <tr>
                                <th>Mã đơn</th>
                                <th>Khách hàng</th>
                                <th>Loại yêu cầu</th>
                                <th>Ngày yêu cầu xử lý</th>
                                <th>Giá trị</th>
                                <th>Trạng thái</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="order in orders" :key="order.idXuLyBanHang" class="order-row">
                                <td class="order-id-cell">#{{ order.maHoaDon }}</td>
                                <td class="customer-cell">
                                    <div class="customer-info">
                                        <strong>{{ order.tenKhachHang || 'Khách vãng lai' }}</strong>
                                        <span class="phone">{{ order.sdt }}</span>
                                    </div>
                                </td>
                                <td class="status-cell">
                                    <span :class="['order-status', order.trangThaiDonHang]">{{
                                        getStatusText(order.trangThaiDonHang)
                                        }}</span>
                                </td>

                                <td class="date-cell">{{ formatDate(order.thoiGianYeuCau) }}</td>
                                <td class="amount-cell">{{ formatCurrency(order.giaBan) }}</td>
                                <td class="status-cell">
                                    <span :class="['order-status', order.hanhDongSauVuViec]">{{
                                        getStatusText(order.hanhDongSauVuViec)
                                        }}</span>
                                </td>
                                <td class="actions-cell">
                                    <div class="order-actions" v-if="!statusNotXuLy.includes(order.hanhDongSauVuViec)">
                                        <button class="action-btn primary" @click="xuLyClick(order.idHoaDon)">Xử
                                            lý</button>
                                    </div>
                                    <div class="order-actions" v-if="order.hanhDongSauVuViec === 'PENDING'">
                                        <button v-if="order.hanhDongSauVuViec === 'PENDING'" class="action-btn success"
                                            @click="openConfirm('Bạn có chắc chắn chấp nhận yêu cầu này?', () => updateStatus(order.idHoaDon, 'HOLD'))"
                                            title="Chấp nhận">
                                            Chấp nhận
                                        </button>
                                        <button v-if="order.hanhDongSauVuViec === 'PENDING'" class="action-btn danger"
                                            @click="openConfirm('Bạn có chắc chắn từ chối yêu cầu này?', () => updateStatus(order.idHoaDon, 'CANCEL'))"
                                            title="Từ chối">
                                            Từ chối
                                        </button>
                                    </div>
                                    <button v-if="order.hanhDongSauVuViec === 'HOLD'" class="action-btn success"
                                        @click="openConfirm('Xác nhận đã nhận được hàng?', () => updateStatus(order.idHoaDon, 'RECEIVED'))"
                                        title="Đã nhận hàng">
                                        Đã nhận hàng
                                    </button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div v-if="activeTab === 'failed_delivery'" class="table-container">
                    <table class="orders-table">
                        <thead>
                            <tr>
                                <th>Mã đơn</th>
                                <th>Khách hàng</th>
                                <th>Lý do thất bại</th>
                                <th>Thời gian yêu cầu</th>
                                <th>Giá trị</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="order in orders" :key="order.idXuLyBanHang" class="order-row failed">
                                <td class="order-id-cell">#{{ order.maHoaDon }}</td>
                                <td class="customer-cell">
                                    <div class="customer-info">
                                        <strong>{{ order.tenKhachHang || 'Khách vãng lai' }}</strong>
                                        <span class="address">{{ order.sdt }}</span>
                                    </div>
                                </td>
                                <td class="failure-cell">
                                    <span class="failure-reason">{{ order.trangThaiDonHang }}</span>
                                </td>
                                <td class="retry-cell">{{ order.thoiGianYeuCau }}</td>
                                <td class="amount-cell">{{ formatCurrency(order.giaBan) }}</td>
                                <td class="actions-cell">
                                    <div class="order-actions">
                                        <button class="action-btn primary" @click="xuLyClick(order.idHoaDon)">Xử
                                            lý</button>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div v-if="activeTab === 'return'" class="table-container">
                    <table class="orders-table">
                        <thead>
                            <tr>
                                <th>Mã đơn</th>
                                <th>Khách hàng</th>
                                <th>Lý do trả hàng</th>
                                <th>Ngày yêu cầu</th>
                                <th>Giá trị</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="order in orders" :key="order.idXuLyBanHang" class="order-row return">
                                <td class="order-id-cell">#{{ order.maHoaDon }}</td>
                                <td class="customer-cell">
                                    <div class="customer-info">
                                        <strong>{{ order.tenKhachHang || 'Khách vãng lai' }}</strong>
                                        <span class="return-type">{{ order.trangThaiDonHang }}</span>
                                    </div>
                                </td>
                                <td class="return-cell">
                                    <span class="return-reason">{{ order.returnReason }}</span>
                                </td>
                                <td class="date-cell">{{ formatDate(order.thoiGianYeuCau) }}</td>
                                <td class="amount-cell">{{ formatCurrency(order.giaBan) }}</td>
                                <td class="actions-cell">
                                    <div class="order-actions" v-if="!statusNotXuLy.includes(order.hanhDongSauVuViec)">
                                        <button class="action-btn primary" @click="xuLyClick(order.idHoaDon)">Xử
                                            lý</button>
                                    </div>
                                    <div class="order-actions" v-if="order.hanhDongSauVuViec === 'PENDING'">
                                        <button v-if="order.hanhDongSauVuViec === 'PENDING'" class="action-btn success"
                                            @click="openConfirm('Bạn có chắc chắn chấp nhận yêu cầu này?', () => updateStatus(order.idHoaDon, 'HOLD'))"
                                            title="Chấp nhận">
                                            Chấp nhận
                                        </button>
                                        <button v-if="order.hanhDongSauVuViec === 'PENDING'" class="action-btn danger"
                                            @click="openConfirm('Bạn có chắc chắn từ chối yêu cầu này?', () => updateStatus(order.idHoaDon, 'CANCEL'))"
                                            title="Từ chối">
                                            Từ chối
                                        </button>
                                    </div>
                                    <button v-if="order.hanhDongSauVuViec === 'HOLD'" class="action-btn success"
                                        @click="openConfirm('Xác nhận đã nhận được hàng?', () => updateStatus(order.idHoaDon, 'RECEIVED'))"
                                        title="Đã nhận hàng">
                                        Đã nhận hàng
                                    </button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <br>
            <div class="pagination-controls">
                <button @click="prevPage" :disabled="pageNo === 0"> &lt; </button>
                <span>Trang {{ pageNo + 1 }} / {{ totalPages }}</span>
                <button @click="nextPage" :disabled="pageNo + 1 >= totalPages"> &gt; </button>
            </div>
            <br>
            <ConfirmModal v-if="showConfirm" :message="confirmMessage" @confirm="handleConfirm"
                @cancel="showConfirm = false" />
        </section>
    </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { countDonHangByStatus, getAllLyDoXuLy, updateStatusPending } from '@/Service/GuestService/ActionAfterCaseService/ActionAfterCaseServices'
import router from '@/router'
import { useToast } from 'vue-toastification'
import ConfirmModal from '@/views/Popup/ConfirmModal.vue'

// Reactive data
const activeTab = ref('all')
const searchQuery = ref('')
const sortBy = ref('desc')

const statusNotXuLy = ['PENDING', 'HOLD','CANCEL']
const toast = useToast()
// Mock data
const stats = ref({
    total: 0,
    failed: 0,
    returns: 0,
    resolved: 0
})

const getStats = async () => {
    
    try {
    const res = await countDonHangByStatus();
    console.log("Kết quả:", res.data);
    stats.value = res.data
  } catch (error) {
    console.error("Lỗi khi lấy dữ liệu:", error);
  }
}

const tabs = ref([
    { id: 'all', label: 'Tất cả' },
    { id: 'failed_delivery', label: 'Giao hàng thất bại' },
    { id: 'return', label: 'Yêu cầu trả hàng' }
])

const currentTabData = computed(() => {
    return tabs.value.find(tab => tab.id === activeTab.value);
});


const orders = ref([]);
const pageNo = ref(0);
const pageSize = ref(5);
const totalPages = ref(1)
const getAllLyDoXuLyView = async () => {
    const status = activeTab.value === 'all' ? null : activeTab.value.toUpperCase();
    const res = await getAllLyDoXuLy(pageNo.value, pageSize.value, searchQuery.value, status, sortBy.value);
    totalPages.value = res.data.totalPages
    orders.value = res.data.content
}

const updateStatus = async (idHoaDon,status) => {
    await updateStatusPending(idHoaDon,status)
    if (status === 'HOLD') {
        toast.success("Bạn đã chấp nhận yêu cầu!")
    } else if (status === 'CANCEL') {
        toast.success("Bạn đã từ chối yêu cầu!")
    } else if (status === 'RECEIVED') {
        toast.success("Đã nhận được hàng")
    }
    getStats()
    getAllLyDoXuLyView()
}

const nextPage = async () => {
    if (pageNo.value + 1 < totalPages.value) {
        pageNo.value++
        await getAllLyDoXuLyView()
    }

}

const prevPage = async () => {
    if (pageNo.value > 0) {
        pageNo.value--
        await getAllLyDoXuLyView()
    }

}

const showConfirm = ref(false)
const confirmMessage = ref('')
let confirmCallback = null

function openConfirm(message, callback) {
    confirmMessage.value = message
    confirmCallback = callback
    showConfirm.value = true
}

function handleConfirm() {
    if (confirmCallback) confirmCallback()
    showConfirm.value = false
}

watch(
    [pageNo, pageSize, searchQuery, activeTab, sortBy],
    () => {
        getAllLyDoXuLyView();
        console.log('order value:', orders.value, 'status', activeTab.value);
    },
    { immediate: true }
);

const xuLyClick = (id) => {
    router.push(`/admin/handle-detail/` + id)
    toast.success("Chuyển thành công qua trang xử lý")
}

const failedOrders = computed(() =>
    orders.value.filter(order => order.status === 'failed')
)

const returnOrders = computed(() =>
    orders.value.filter(order => order.status === 'return')
)

// Methods
const getStatusText = (status) => {
    const statusMap = {
        'failed': 'Giao hàng thất bại',
        'return': 'Yêu cầu trả hàng',
        'processing': 'Đang xử lý'
    }
    return statusMap[status] || status
}

function formatDate(date) {
    if (!date) return "";
    return new Intl.DateTimeFormat("vi-VN", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
    }).format(new Date(date));
}

const formatCurrency = (amount) => {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(amount)
}

onMounted(() => {
    getAllLyDoXuLyView()
    getStats()
})
</script>

<style scoped>
* {
    box-sizing: border-box;
}

.after-sales-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    background-color: #f8fafc;
    min-height: 100vh;
}

/* Header Styles */
.page-header {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    padding: 30px;
    border-radius: 12px;
    margin-bottom: 30px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.header-content {
    text-align: center;
}

.page-title {
    font-size: 2.5rem;
    margin: 0 0 20px 0;
    font-weight: 700;
}

.policy-section h3 {
    font-size: 1.2rem;
    margin-bottom: 15px;
    opacity: 0.9;
}

.policy-items {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    gap: 20px;
}

.policy-item {
    background: rgba(255, 255, 255, 0.2);
    padding: 8px 16px;
    border-radius: 20px;
    font-size: 0.9rem;
    backdrop-filter: blur(10px);
}

/* Stats Section */
.stats-section {
    margin-bottom: 30px;
}

.stats-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 20px;
}

.stat-card {
    background: white;
    padding: 25px;
    border-radius: 12px;
    text-align: center;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    border-left: 4px solid;
    transition: transform 0.2s ease;
}

.stat-card:hover {
    transform: translateY(-2px);
}

.stat-card.urgent {
    border-left-color: #ef4444;
}

.stat-card.warning {
    border-left-color: #f59e0b;
}

.stat-card.info {
    border-left-color: #3b82f6;
}

.stat-card.success {
    border-left-color: #10b981;
}

.stat-number {
    font-size: 2.5rem;
    font-weight: 700;
    margin-bottom: 8px;
}

.stat-card.urgent .stat-number {
    color: #ef4444;
}

.stat-card.warning .stat-number {
    color: #f59e0b;
}

.stat-card.info .stat-number {
    color: #3b82f6;
}

.stat-card.success .stat-number {
    color: #10b981;
}

.stat-label {
    color: #64748b;
    font-weight: 500;
}

/* Filter Section */
.filter-section {
    margin-bottom: 30px;
}

.filter-controls {
    display: flex;
    gap: 20px;
    align-items: center;
    flex-wrap: wrap;
}

.search-box {
    display: flex;
    flex: 1;
    min-width: 300px;
}

.search-input {
    flex: 1;
    padding: 12px 16px;
    border: 2px solid #e2e8f0;
    border-right: none;
    border-radius: 8px 0 0 8px;
    font-size: 1rem;
    outline: none;
    transition: border-color 0.2s ease;
}

.search-input:focus {
    border-color: #3b82f6;
}

.search-btn {
    padding: 12px 16px;
    background: #3b82f6;
    color: white;
    border: none;
    border-radius: 0 8px 8px 0;
    cursor: pointer;
    font-size: 1rem;
}

.sort-select {
    padding: 12px 16px;
    border: 2px solid #e2e8f0;
    border-radius: 8px;
    font-size: 1rem;
    outline: none;
    background: white;
}

/* Tabs Section */
.tabs-section {
    background: white;
    border-radius: 12px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    overflow: hidden;
}

.tab-navigation {
    display: flex;
    background: #f8fafc;
    border-bottom: 1px solid #e2e8f0;
}

.tab-button {
    flex: 1;
    padding: 16px 24px;
    border: none;
    background: transparent;
    cursor: pointer;
    font-size: 1rem;
    font-weight: 500;
    color: #64748b;
    transition: all 0.2s ease;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
}

.tab-button:hover {
    background: #e2e8f0;
}

.tab-button.active {
    background: white;
    color: #3b82f6;
    border-bottom: 3px solid #3b82f6;
}

.tab-button.active .tab-count {
    background: #3b82f6;
}

/* Table Styles */
.table-container {
    overflow-x: auto;
    background: white;
    border-radius: 8px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.orders-table {
    width: 100%;
    border-collapse: collapse;
    font-size: 0.95rem;
}

.orders-table th {
    background: #f8fafc;
    padding: 16px 12px;
    text-align: left;
    font-weight: 600;
    color: #374151;
    border-bottom: 2px solid #e5e7eb;
    white-space: nowrap;
}

.orders-table td {
    padding: 16px 12px;
    border-bottom: 1px solid #f3f4f6;
    vertical-align: middle;
}

.order-row {
    transition: background-color 0.2s ease;
}

.order-row:hover {
    background-color: #f9fafb;
}

.order-row.failed {
    border-left: 4px solid #ef4444;
}

.order-row.return {
    border-left: 4px solid #f59e0b;
}

.order-id-cell {
    font-weight: 700;
    color: #1e293b;
    font-size: 1rem;
}

.customer-cell .customer-info {
    display: flex;
    flex-direction: column;
    gap: 4px;
}

.customer-cell strong {
    color: #1e293b;
    font-size: 0.95rem;
}

.customer-cell .phone,
.customer-cell .address,
.customer-cell .return-type {
    color: #64748b;
    font-size: 0.85rem;
}

.status-cell .order-status {
    padding: 6px 12px;
    border-radius: 20px;
    font-size: 0.8rem;
    font-weight: 600;
    white-space: nowrap;
}

.status-cell .order-status.failed {
    background: #fee2e2;
    color: #dc2626;
}

.status-cell .order-status.return {
    background: #fef3c7;
    color: #d97706;
}

.failure-cell .failure-reason,
.return-cell .return-reason {
    background: #fee2e2;
    color: #dc2626;
    padding: 6px 12px;
    border-radius: 20px;
    font-size: 0.8rem;
    font-weight: 500;
    display: inline-block;
}

.return-cell .return-reason {
    background: #fef3c7;
    color: #d97706;
}

.retry-cell {
    color: #64748b;
    font-weight: 500;
}

.date-cell {
    color: #64748b;
    white-space: nowrap;
}

.amount-cell {
    font-size: 1rem;
    font-weight: 700;
    color: #059669;
    white-space: nowrap;
}

.actions-cell .order-actions {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
}

.actions-cell .action-btn {
    padding: 8px 16px;
    border: none;
    border-radius: 6px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.2s ease;
    font-size: 0.85rem;
    white-space: nowrap;
}

.actions-cell .action-btn.primary {
    background: #3b82f6;
    color: white;
}

.actions-cell .action-btn.primary:hover {
    background: #2563eb;
}

.actions-cell .action-btn.secondary {
    background: #f1f5f9;
    color: #475569;
    border: 1px solid #e2e8f0;
}

.actions-cell .action-btn.secondary:hover {
    background: #e2e8f0;
}

.actions-cell .action-btn.warning {
    background: #f59e0b;
    color: white;
}

.actions-cell .action-btn.warning:hover {
    background: #d97706;
}

.actions-cell .action-btn.danger {
    background: #ef4444;
    color: white;
}

.actions-cell .action-btn.danger:hover {
    background: #dc2626;
}

.actions-cell .action-btn.success {
    background: #10b981;
    color: white;
}

.actions-cell .action-btn.success:hover {
    background: #059669;
}

/* Responsive Design for Table */
@media (max-width: 768px) {
    .table-container {
        font-size: 0.85rem;
    }

    .orders-table th,
    .orders-table td {
        padding: 12px 8px;
    }

    .actions-cell .order-actions {
        flex-direction: column;
        gap: 4px;
    }

    .actions-cell .action-btn {
        padding: 6px 12px;
        font-size: 0.8rem;
    }
}

/* Responsive Design */
@media (max-width: 768px) {
    .after-sales-container {
        padding: 15px;
    }

    .page-title {
        font-size: 2rem;
    }

    .policy-items {
        flex-direction: column;
        align-items: center;
    }

    .stats-grid {
        grid-template-columns: 1fr;
    }

    .filter-controls {
        flex-direction: column;
        align-items: stretch;
    }

    .search-box {
        min-width: auto;
    }

    .tab-navigation {
        flex-direction: column;
    }

    .order-details {
        flex-direction: column;
        align-items: flex-start;
        gap: 12px;
    }

    .order-actions {
        width: 100%;
        justify-content: stretch;
    }

    .action-btn {
        flex: 1;
    }
}

.pagination-controls {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 8px;
    margin-top: 12px;
    font-size: 14px;
}

/* Pagination - ĐƠN GIẢN */
.pagination {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 15px;
    padding: 15px 0;
}

.page-btn {
    padding: 8px 12px;
    border: 1px solid #ddd;
    border-radius: 6px;
    background: white;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.2s;
}

.page-btn:hover:not(:disabled) {
    background: #f8f9fa;
    border-color: #007bff;
}

.page-btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
    background: #f5f5f5;
}

.page-icon {
    width: 16px;
    height: 16px;
}

.page-info {
    font-size: 14px;
    color: #666;
    font-weight: 500;
    min-width: 60px;
    text-align: center;
}

/* Mobile */
@media (max-width: 480px) {
    .pagination {
        gap: 10px;
        padding: 10px 0;
    }

    .page-btn {
        padding: 6px 10px;
    }

    .page-icon {
        width: 14px;
        height: 14px;
    }

    .page-info {
        font-size: 13px;
        min-width: 50px;
    }
}

.pagination-controls button {
    background-color: #007bff;
    color: white;
    border: none;
    padding: 10px 18px;
    border-radius: 5px;
    cursor: pointer;
    font-size: 0.95em;
    transition: background-color 0.2s ease;
}

.pagination-controls button:hover:not(:disabled) {
    background-color: #0056b3;
}

.pagination-controls button:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
    opacity: 0.7;
}

.pagination-controls span {
    font-weight: 500;
    color: #666;
    font-size: 1em;
}
</style>
